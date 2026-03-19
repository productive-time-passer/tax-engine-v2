# Tax Engine v2

Spring Boot service that ingests taxpayer documents, extracts canonical tax facts, and stores them in PostgreSQL.

## What you can do quickly
- Upload a document for a taxpayer.
- Run the extraction pipeline on that document.
- Fetch all extracted facts for that taxpayer.

## Prerequisites
- Docker Desktop (or Docker Engine + Docker Compose plugin)
- `curl`

## Run locally with Docker

### 1) Start services
```bash
docker compose up --build -d
```

This starts:
- `postgres` on `localhost:5432`
- `tax-engine` on `localhost:8080`

### 2) Verify app is healthy
```bash
curl http://localhost:8080/actuator/health
```

Expected response includes `"status":"UP"`.

### 3) Create a sample file and taxpayer id
```bash
TAXPAYER_ID=$(cat /proc/sys/kernel/random/uuid)
echo "Dummy payslip content" > sample-payslip.txt
echo "$TAXPAYER_ID"
```

### 4) Upload the document
```bash
UPLOAD_RESPONSE=$(curl -s -X POST "http://localhost:8080/documents/upload" \
  -F "taxpayerId=${TAXPAYER_ID}" \
  -F "file=@sample-payslip.txt;type=text/plain")

echo "$UPLOAD_RESPONSE"
```

Capture `documentId` from the JSON output.

### 5) Process the uploaded document
```bash
DOCUMENT_ID=<paste-document-id-here>

curl -s -X POST "http://localhost:8080/documents/${DOCUMENT_ID}/process?documentType=payslip" \
  -H "X-Correlation-Id: local-demo-001"
```

### 6) Retrieve generated facts
```bash
curl -s "http://localhost:8080/facts/${TAXPAYER_ID}"
```

You should see one or more persisted fact records.

## Stop and clean up

Stop services:
```bash
docker compose down
```

Stop and remove DB volume too:
```bash
docker compose down -v
```

## Notes
- Database schema is initialized automatically from `src/main/resources/db/migration/V1__init_fact_extraction.sql` when PostgreSQL starts for the first time.
- The app image is built from the project `Dockerfile` using Gradle in a build stage.
