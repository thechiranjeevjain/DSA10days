# DevOps, cloud and networking — active recall

**Baseline:** Java 17+ (Java 21 features marked), Spring Boot 3.x, PostgreSQL where applicable. P0 = closed-book recall; P1 = explain and implement with documentation. Snippets are focused fragments, not standalone projects.

## P0 — Networking
DNS resolves names; TCP reliable ordered byte stream; TLS authenticates server and encrypts transport; HTTP defines request/response semantics. HTTP/2 multiplexes streams over one connection; HTTP/3 uses QUIC. Connect timeout vs read timeout vs total deadline. Reverse proxy routes inbound traffic; load balancer spreads traffic; NAT translates addresses. 502 bad gateway, 503 unavailable, 504 gateway timeout: investigate upstream and proxy evidence rather than assuming root cause.

## P0 — Docker
```dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/app.jar app.jar
USER 10001
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
```
Build artifact in CI or multi-stage build; pin approved image versions/digests in production. Image is immutable layers; container is isolated process with writable layer. Use non-root, external configuration, secrets manager, resource limits, health checks and image scanning.
```bash
docker build -t hotel:local .
docker run --rm -p 8080:8080 --memory=1g hotel:local
```
## P0 — Kubernetes
Deployment manages replicas/rollout; Service stable endpoint; Ingress/Gateway routes external traffic; ConfigMap nonsecret config; Secret is not automatically encrypted at rest. Readiness gates traffic, liveness restarts unhealthy containers, startup probe protects slow startup. Requests influence scheduling; limits constrain resources (CPU throttling, memory OOM). HPA needs metrics and realistic requests. PDB constrains voluntary disruptions, not all failures.
```bash
kubectl get pods -n app
kubectl describe pod POD -n app
kubectl logs POD -n app --previous
kubectl rollout status deployment/hotel -n app
kubectl get events -n app --sort-by=.lastTimestamp
```
## P1 — AWS architecture
VPC subnets/routes; public subnet has route to internet gateway, private workloads often use NAT for outbound internet or VPC endpoints. Security groups stateful, NACLs stateless. ALB layer 7, NLB layer 4. IAM roles and least privilege; avoid long-lived credentials. RDS Multi-AZ for availability, read replicas for read scaling; details vary by deployment type. S3 object storage; SQS queue (standard at-least-once, possible reorder); SNS pub/sub; CloudWatch logs/metrics; ECS/EKS compute orchestration. Multi-AZ does not replace backups or regional DR.

## P0 — Delivery and rollback
CI: compile → unit/integration tests → scan → immutable artifact → deploy → smoke tests → monitor. Canary/blue-green reduce rollout risk; DB migrations use expand/contract to preserve old/new compatibility. Rollback app is not necessarily rollback data. Terraform plan review, remote state locking where supported, protected state, drift detection; do not store secrets casually in state. Jenkins pipeline credentials must not appear in logs.

## Recall drills
- [ ] Diagnose pod CrashLoopBackOff vs readiness failure vs OOMKilled.
- [ ] Explain request path from browser through DNS/TLS/ALB to service.
- [ ] Design safe rolling deployment with graceful termination and schema change.
- [ ] Explain AWS network isolation and least-privilege service identity.

References: https://kubernetes.io/docs/ ; https://docs.docker.com/ ; https://docs.aws.amazon.com/
