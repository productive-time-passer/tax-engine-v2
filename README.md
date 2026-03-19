# Tax Engine v2

Tax Engine v2 is a Spring Boot service that:
- accepts taxpayer documents,
- extracts and normalizes tax facts,
- persists those facts in PostgreSQL,
- exposes APIs for facts, tax context, advisory output, and copilot chat.

---

## 1) What you can run locally

You can run this project in two easy ways:

1. **Fastest (recommended):** run everything with Docker Compose.
2. **Developer mode:** run PostgreSQL in Docker, app from your IDE/terminal with `bootRun`.

---

## 2) Prerequisites

- **Docker Desktop** (or Docker Engine + Compose plugin)
- **Java 21** (for local JVM run)
- `curl`

Optional but useful:
- `jq` (pretty JSON in examples)

---

## 3) Quick start (recommended): Docker Compose

### 3.1 Start the stack

```bash
docker compose up --build -d
```

This starts:
- PostgreSQL: `localhost:5432`
- App: `localhost:8080`

### 3.2 Verify health

```bash
curl -s http://localhost:8080/actuator/health
```

Expected response includes `"status":"UP"`.

### 3.3 End-to-end smoke test

```bash
# Linux/macOS
TAXPAYER_ID=$(cat /proc/sys/kernel/random/uuid 2>/dev/null || uuidgen)
echo "Dummy payslip content" > sample-payslip.txt

UPLOAD_RESPONSE=$(curl -s -X POST "http://localhost:8080/documents/upload" \
  -F "taxpayerId=${TAXPAYER_ID}" \
  -F "file=@sample-payslip.txt;type=text/plain")

echo "$UPLOAD_RESPONSE"

DOCUMENT_ID=$(echo "$UPLOAD_RESPONSE" | jq -r '.id // .documentId')

curl -s -X POST "http://localhost:8080/documents/${DOCUMENT_ID}/process?documentType=payslip" \
  -H "X-Correlation-Id: local-demo-001"

curl -s "http://localhost:8080/facts/${TAXPAYER_ID}"
```

If you don't have `jq`, copy `DOCUMENT_ID` manually from the upload response.

### 3.4 Stop services

```bash
docker compose down
```

Also remove the database volume:

```bash
docker compose down -v
```

---

## 4) Developer mode: run app locally, DB in Docker

### 4.1 Start only PostgreSQL

```bash
docker compose up -d postgres
```

### 4.2 Start the app from terminal

```bash
./gradlew bootRun
```

The default datasource in `application.yml` already points to local PostgreSQL:
- URL: `jdbc:postgresql://localhost:5432/tax_engine`
- user: `tax_user`
- password: `tax_password`

You can now call the same APIs on `http://localhost:8080`.

---

## 5) API cheatsheet

### OpenAPI / Swagger
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI spec (JSON): `http://localhost:8080/v3/api-docs`

### Documents
- `POST /documents/upload` (multipart: `taxpayerId`, `file`)
- `POST /documents/{id}/process?documentType=payslip`

### Facts
- `GET /facts/{taxpayerId}`

### Tax context
- `GET /tax-context/{taxpayerId}/{financialYear}`

### Advisory
- `GET /advisory/{taxpayerId}/{financialYear}`

### Copilot chat
- `POST /copilot/chat`

Example payload:

```json
{
  "conversationId": "demo-conversation",
  "taxpayerId": "11111111-1111-1111-1111-111111111111",
  "financialYear": "2024-25",
  "message": "How can I reduce my tax this year?"
}
```

> Note: Copilot LLM calls are stubbed by default (`copilot.llm.stub-mode=true`), so local runs do not require external LLM credentials.

---

## 6) Run tests

```bash
./gradlew test --no-daemon
```

---

## 7) Troubleshooting

- **Port 5432 already in use:** stop local PostgreSQL service or remap compose port.
- **Port 8080 already in use:** stop conflicting app or run Spring on another port (`--server.port=8081`).
- **Container healthy but app fails DB connect:** wait a few seconds and retry; app depends on DB healthcheck.
- **Fresh reset needed:** `docker compose down -v` then `docker compose up --build -d`.

---

## 8) Project notes

- App is built with Spring Boot 3.3, Java 21, Gradle.
- Docker image build is multi-stage and produces a single runnable JAR.
- DB bootstrap SQL is loaded from:
  - `src/main/resources/db/migration/V1__init_fact_extraction.sql`
