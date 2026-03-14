# Gita Backend 🕉

A Spring Boot REST API that serves Bhagavad Gita verses based on detected emotions. Part of the [Gita Wisdom](https://gita-frontend-rho.vercel.app) full-stack project.

## What it does

Exposes a single endpoint that takes an emotion string and returns 3 relevant Bhagavad Gita verses with Sanskrit text, transliteration, word meanings, and full English translation (Bhaktivedanta).

## API

### `GET /api/verses?emotion={emotion}`

**Supported emotions:**
`anger` · `sadness` · `fear` · `joy` · `confusion` · `lust` · `greed` · `attachment/delusion` · `grief` · `anxiety` · `disgust` · `surprise` · `contempt` · `shame/guilt`

**Example:**
```
GET /api/verses?emotion=anger
```

**Response:**
```json
[
  {
    "chapter": 2,
    "verse": 62,
    "sanskrit": "ध्यायतो विषयान्पुंसः...",
    "transliteration": "dhyāyato viṣayān puṁsaḥ...",
    "wordMeanings": "Contemplating; sense objects...",
    "translation": "When a person dwells upon sense objects..."
  }
]
```

### `GET /api/health`
Returns `{ "status": "ok" }` — used to verify the service is running.

## Tech Stack

- Java 17
- Spring Boot 3.5
- Maven
- Jackson (JSON parsing)
- REST API

## How it works

1. On startup, loads `verse_translated.json` (700+ verses) into memory via `@PostConstruct`
2. Maps each of the 14 emotions to 3 hand-curated verse IDs
3. Serves matched verses on request with full translation data

## Running locally
```bash
./mvnw spring-boot:run
```

API available at `http://localhost:8080`

## Deployed on

[Railway](https://railway.app) — `https://gita-backend-production.up.railway.app`

## Part of

| Component | Repo/Link |
|---|---|
| Frontend | [gita-frontend](https://gita-frontend-rho.vercel.app) |
| Emotion Model | [gita-nlp](https://huggingface.co/spaces/palakhf/gita-nlp) |
| Backend (this) | [gita-backend](https://github.com/palak-609/gita-backend) |
