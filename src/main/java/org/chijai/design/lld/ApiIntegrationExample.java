package org.chijai.design.lld;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

/**
 * ============================================================================
 * PROBLEM STATEMENT
 * ============================================================================
 *
 * A monitoring system exposes a REST API that returns information about
 * devices running in a data center.
 *
 * Each device contains:
 *      • Device ID
 *      • Device Name
 *      • Temperature
 *      • Utilization
 *
 * Build a production-quality service that:
 *
 * 1. Fetches device data from the external API.
 * 2. Converts the response into Java domain objects.
 * 3. Filters devices based on business rules.
 * 4. Sorts the results.
 * 5. Returns the processed list.
 *
 * Example Requirement:
 *
 * Return all devices having
 *
 *      temperature >= 70°C
 *
 * sorted in descending order of temperature.
 *
 * ============================================================================
 * WHAT THE INTERVIEWER IS ACTUALLY TESTING
 * ============================================================================
 *
 * This is NOT a JSON parsing question.
 *
 * This is NOT an HTTP question.
 *
 * This is a Software Engineering question.
 *
 * The interviewer wants to evaluate:
 *
 * ✓ Separation of Concerns
 * ✓ Clean Code
 * ✓ SOLID Principles
 * ✓ Dependency Injection
 * ✓ Error Handling
 * ✓ Retry Strategy
 * ✓ Testability
 * ✓ Maintainability
 * ✓ Production Readiness
 *
 * ============================================================================
 * APPROACH
 * ============================================================================
 *
 * Instead of writing everything inside main():
 *
 *      HTTP
 *        ↓
 *      Parse
 *        ↓
 *      Filter
 *        ↓
 *      Sort
 *        ↓
 *      Print
 *
 * we separate responsibilities into layers.
 *
 *                  Main
 *                   │
 *                   ▼
 *             DeviceService
 *                   │
 *                   ▼
 *               ApiClient
 *              /         \
 *             /           \
 *     MockApiClient   HttpApiClient
 *
 * Business logic should never know
 * where the data came from.
 *
 * ============================================================================
 * CLASS RESPONSIBILITIES
 * ============================================================================
 *
 * Device
 *      Represents one device.
 *
 * ApiClient
 *      Abstraction for fetching devices.
 *
 * MockApiClient
 *      Returns deterministic test data.
 *
 * HttpApiClient
 *      Calls the real REST API.
 *
 * DeviceService
 *      Contains business logic such as
 *      filtering and sorting.
 *
 * RetryExecutor
 *      Retries temporary failures using
 *      exponential backoff.
 *
 * ErrorClassifier
 *      Determines whether an exception or
 *      HTTP status should be retried.
 *
 * ============================================================================
 * ERROR HANDLING
 * ============================================================================
 *
 * Retry:
 *
 *      429
 *      500
 *      502
 *      503
 *      504
 *      Timeout
 *      Connection Refused
 *
 * Don't Retry:
 *
 *      400
 *      401
 *      403
 *      404
 *      IllegalArgumentException
 *
 * ============================================================================
 * COMPLEXITY
 * ============================================================================
 *
 * Parsing      : O(n)
 *
 * Filtering    : O(n)
 *
 * Sorting      : O(n log n)
 *
 * Overall      : O(n log n)
 *
 * Space        : O(n)
 *
 * ============================================================================
 * INTERVIEW ANSWER
 * ============================================================================
 *
 * "Before writing code, I'd separate networking,
 * parsing and business logic into independent
 * components.
 *
 * This keeps the code testable, maintainable,
 * and easy to extend.
 *
 * I'd inject the ApiClient into DeviceService so
 * the business logic can be unit tested using a
 * MockApiClient without making network calls.
 *
 * For resilience, I'd retry only transient
 * failures (timeouts, 5xx, 429) using exponential
 * backoff with jitter, while failing fast for
 * permanent client errors such as 400 and 404.
 *
 * If this service had to handle millions of
 * devices, I'd introduce pagination, streaming
 * parsers, caching, metrics, logging and a
 * circuit breaker."
 *
 * ============================================================================
 */

public class ApiIntegrationExample {

    // ============================================================
    // DOMAIN
    // ============================================================

    public record Device(
            String id,
            String name,
            double temperature,
            double utilization
    ) {
        public Device {
            Objects.requireNonNull(id);
            Objects.requireNonNull(name);
        }
    }

    // ============================================================
    // API CLIENT
    // ============================================================

    interface ApiClient {
        List<Device> fetchDevices() throws Exception;
    }

    // ============================================================
    // MOCK IMPLEMENTATION
    // ============================================================

    static class MockApiClient implements ApiClient {

        @Override
        public List<Device> fetchDevices() {

            List<Device> list = new ArrayList<>();

            list.add(new Device(
                    "D1",
                    "GPU-A",
                    75,
                    91));

            list.add(new Device(
                    "D2",
                    "GPU-B",
                    62,
                    51));

            list.add(new Device(
                    "D3",
                    "GPU-C",
                    81,
                    97));

            list.add(new Device(
                    "D4",
                    "GPU-D",
                    70,
                    66));

            return list;
        }
    }

    // ============================================================
    // REAL HTTP CLIENT
    // (Assumes endpoint returns CSV:
    // id,name,temp,util
    // ============================================================

    static class HttpApiClient implements ApiClient {

        private final HttpClient client;

        private final String endpoint;

        HttpApiClient(String endpoint) {

            this.endpoint = endpoint;

            this.client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();
        }

        @Override
        public List<Device> fetchDevices() throws Exception {

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create(endpoint))
                            .timeout(Duration.ofSeconds(10))
                            .build();

            HttpResponse<String> response =
                    RetryExecutor.execute(() ->
                            client.send(request,
                                    HttpResponse.BodyHandlers.ofString()));

            int status = response.statusCode();

            if (ErrorClassifier.isRetryable(status)) {
                throw new IOException(
                        "Retryable HTTP Error : " + status);
            }

            if (status >= 400) {
                throw new RuntimeException(
                        "HTTP Error : " + status);
            }

            return CsvParser.parse(response.body());
        }
    }

    // ============================================================
    // CSV PARSER
    // ============================================================

    static class CsvParser {

        static List<Device> parse(String csv) {

            List<Device> devices =
                    new ArrayList<>();

            if (csv == null || csv.isBlank()) {
                return devices;
            }

            String[] lines = csv.split("\\R");

            for (int i = 1; i < lines.length; i++) {

                String[] parts =
                        lines[i].split(",");

                if (parts.length != 4) {
                    continue;
                }

                devices.add(
                        new Device(
                                parts[0].trim(),
                                parts[1].trim(),
                                Double.parseDouble(parts[2]),
                                Double.parseDouble(parts[3])
                        )
                );
            }

            return devices;
        }
    }

    // ============================================================
    // BUSINESS SERVICE
    // ============================================================

    static class DeviceService {

        private final ApiClient apiClient;

        DeviceService(ApiClient apiClient) {
            this.apiClient = apiClient;
        }

        List<Device> getHotDevices(
                double threshold)
                throws Exception {

            return apiClient.fetchDevices()

                    .stream()

                    .filter(d ->
                            d.temperature() >= threshold)

                    .sorted(
                            Comparator.comparingDouble(
                                            Device::temperature)
                                    .reversed())

                    .toList();
        }

        List<Device> getHighlyUtilizedDevices(
                double utilization)
                throws Exception {

            return apiClient.fetchDevices()

                    .stream()

                    .filter(d ->
                            d.utilization() >= utilization)

                    .sorted(
                            Comparator.comparingDouble(
                                            Device::utilization)
                                    .reversed())

                    .toList();
        }
    }

    // ============================================================
    // RETRY SUPPORT
    // ============================================================

    @FunctionalInterface
    interface RetryOperation<T> {
        T execute() throws Exception;
    }

    static class RetryExecutor {

        private static final int MAX_RETRIES = 3;

        private static final long INITIAL_DELAY_MS = 1000;

        private static final Random RANDOM = new Random();

        static <T> T execute(RetryOperation<T> operation)
                throws Exception {

            long delay = INITIAL_DELAY_MS;

            Exception lastException = null;

            for (int attempt = 1;
                 attempt <= MAX_RETRIES;
                 attempt++) {

                try {

                    return operation.execute();

                } catch (Exception ex) {

                    lastException = ex;

                    if (!ErrorClassifier.isRetryable(ex)) {
                        throw ex;
                    }

                    if (attempt == MAX_RETRIES) {
                        break;
                    }

                    long jitter =
                            RANDOM.nextLong(250);

                    Thread.sleep(delay + jitter);

                    delay *= 2;
                }
            }

            throw lastException;
        }
    }

    // ============================================================
    // ERROR CLASSIFIER
    // ============================================================

    static class ErrorClassifier {

        static boolean isRetryable(int statusCode) {

            return statusCode == 429
                    || statusCode == 500
                    || statusCode == 502
                    || statusCode == 503
                    || statusCode == 504;
        }

        static boolean isRetryable(Exception ex) {

            return ex instanceof IOException
                    || ex instanceof SocketTimeoutException
                    || ex instanceof ConnectException;
        }
    }

    // ============================================================
    // MAIN
    // ============================================================

    public static void main(String[] args) throws Exception {

        ApiClient client = new MockApiClient();

        DeviceService service =
                new DeviceService(client);

        // --------------------------------------------------------
        // Hot devices
        // --------------------------------------------------------

        List<Device> hot =
                service.getHotDevices(70);

        assert hot.size() == 3;

        assert hot.get(0).id().equals("D3");

        assert hot.get(1).id().equals("D1");

        assert hot.get(2).id().equals("D4");

        // --------------------------------------------------------
        // Utilization
        // --------------------------------------------------------

        List<Device> utilized =
                service.getHighlyUtilizedDevices(90);

        assert utilized.size() == 2;

        assert utilized.get(0).id().equals("D3");

        assert utilized.get(1).id().equals("D1");

        // --------------------------------------------------------
        // No matches
        // --------------------------------------------------------

        List<Device> none =
                service.getHotDevices(100);

        assert none.isEmpty();

        // --------------------------------------------------------
        // CSV Parser
        // --------------------------------------------------------

        String csv = """
                id,name,temp,util
                X1,GPU-X,55,44
                X2,GPU-Y,95,99
                """;

        List<Device> parsed =
                CsvParser.parse(csv);

        assert parsed.size() == 2;

        assert parsed.get(1).temperature() == 95;

        // --------------------------------------------------------
        // Error Classification
        // --------------------------------------------------------

        assert ErrorClassifier.isRetryable(500);

        assert ErrorClassifier.isRetryable(503);

        assert ErrorClassifier.isRetryable(429);

        assert !ErrorClassifier.isRetryable(404);

        assert !ErrorClassifier.isRetryable(400);

        // --------------------------------------------------------
        // Retry Demo
        // --------------------------------------------------------

        final int[] counter = {0};

        String value =
                RetryExecutor.execute(() -> {

                    counter[0]++;

                    if (counter[0] < 3) {
                        throw new IOException("Temporary failure");
                    }

                    return "SUCCESS";
                });

        assert counter[0] == 3;

        assert value.equals("SUCCESS");

        // --------------------------------------------------------
        // Output
        // --------------------------------------------------------

        System.out.println("Hot Devices:");
        hot.forEach(System.out::println);

        System.out.println();

        System.out.println("Highly Utilized Devices:");
        utilized.forEach(System.out::println);

        System.out.println();

        System.out.println("CSV Parsing Successful:");
        parsed.forEach(System.out::println);

        System.out.println();

        System.out.println("✓ All assertions passed.");
    }
}