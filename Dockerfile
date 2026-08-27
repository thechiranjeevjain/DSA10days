FROM mcr.microsoft.com/powershell:7.4-ubuntu-22.04

RUN apt-get update \
    && apt-get install -y --no-install-recommends openjdk-17-jdk maven git ca-certificates \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /workspace
COPY . .

RUN pwsh -NoProfile -File ./verify-all.ps1

CMD ["pwsh", "-NoProfile", "-File", "./verify-all.ps1"]
