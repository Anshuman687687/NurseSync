# NurseSync — Project Scope

> **Version:** 1.0  
> **Last Updated:** 21 February 2026  
> **Status:** Hackathon MVP — Active Development  

---

## Table of Contents

1. [Executive Summary & Problem Statement](#1-executive-summary--problem-statement)  
2. [System Architecture & Data Flow](#2-system-architecture--data-flow)  
3. [Tech Stack Breakdown](#3-tech-stack-breakdown)  
4. [Core Features — MVP vs Post-Hackathon](#4-core-features--mvp-vs-post-hackathon)  
5. [UI/UX & Design System Guidelines](#5-uiux--design-system-guidelines)  
6. [API Contract Outline](#6-api-contract-outline)  

---

## 1. Executive Summary & Problem Statement

### Executive Summary

**NurseSync** is a cross-platform, voice-first clinical logging application designed to eliminate the cognitive overhead ICU nurses face when documenting patient care. By leveraging real-time speech recognition, structured data extraction via LLMs, and automated shift handoff generation, NurseSync transforms a nurse's natural speech into structured, auditable medical records — without ever requiring them to touch a keyboard during critical care.

### The Problem

ICU nurses are responsible for some of the highest-stakes documentation in healthcare:

- **Charting burden:** Studies show nurses spend up to **35% of their shift** on documentation rather than direct patient care.
- **Error-prone handoffs:** Shift transitions are the #1 source of clinical communication failures. Critical tasks — a missed wound dressing, an overdue medication — fall through the cracks when they exist only in someone's memory or on a scribbled sticky note.
- **Hands-occupied environments:** Nurses are routinely gloved, gowned, and mid-procedure. Traditional EHR input (keyboard/mouse/tablet) is physically impractical and breaks sterile workflows.
- **Multilingual challenges:** In many hospital environments, nurses speak multiple languages during shifts. Existing tools offer zero multilingual support for voice input.

### The Solution

NurseSync provides a single floating **record button** — always accessible across every screen. A nurse speaks naturally:

> *"Gave 10 mg of paracetamol to Ishaan Bhalla at 10 PM."*

The system automatically:

1. **Transcribes** the audio (multilingual, via Whisper).
2. **Extracts** structured data — patient name, action, dosage, time, priority — into a clean JSON record.
3. **Flags** low-confidence transcriptions for human review.
4. **Generates** an end-of-shift handoff summary, heavily weighted toward missed or critical tasks.
5. **Enables** an AI shift agent that the incoming nurse can query conversationally about the outgoing shift's events.

---

## 2. System Architecture & Data Flow

### High-Level Architecture

```
┌──────────────────────────────────────────────────────────┐
│                      CLIENT LAYER                        │
│   Tauri Shell (Rust) + React/TypeScript Frontend         │
│   ┌────────────────────────────────────────────────┐     │
│   │  React Application                             │     │
│   │  ├── Zustand (Global State)                    │     │
│   │  ├── Voice Recorder Module (MediaRecorder API) │     │
│   │  ├── Tab Navigation (Dashboard, Schedule,      │     │
│   │  │   Patients, Profile)                        │     │
│   │  └── HTTP Client (fetch / axios)               │     │
│   └────────────────────────────────────────────────┘     │
│   Platforms: macOS, Windows, iOS, Android                │
└──────────────────────┬───────────────────────────────────┘
                       │ HTTPS (REST + WebSocket)
                       ▼
┌──────────────────────────────────────────────────────────┐
│                    BACKEND LAYER                         │
│            FastAPI (Python 3.11+)                        │
│   ┌────────────────────────────────────────────────┐     │
│   │  API Gateway & Auth Middleware                 │     │
│   │  ├── /api/v1/voice      → Voice Pipeline       │     │
│   │  ├── /api/v1/logs       → Log CRUD             │     │
│   │  ├── /api/v1/handoff    → Handoff Generation   │     │
│   │  ├── /api/v1/chat       → Shift Agent          │     │
│   │  └── /api/v1/documents  → Document Parsing     │     │
│   └────────────────────────────────────────────────┘     │
│   ┌────────────────────────────────────────────────┐     │
│   │  AI / ML Pipeline                              │     │
│   │  ├── Whisper (Speech-to-Text, Multilingual)    │     │
│   │  ├── LLM (Structured Extraction + Agent)       │     │
│   │  └── Document Parser (OCR + LLM)               │     │
│   └────────────────────────────────────────────────┘     │
└──────────────────────┬───────────────────────────────────┘
                       │ SQL (Supabase Client)
                       ▼
┌──────────────────────────────────────────────────────────┐
│                   DATABASE LAYER                         │
│              PostgreSQL (via Supabase)                   │
│   ┌────────────────────────────────────────────────┐     │
│   │  Tables:                                       │     │
│   │  ├── users                                     │     │
│   │  ├── patients                                  │     │
│   │  ├── shifts                                    │     │
│   │  ├── voice_logs (raw transcript + audio ref)   │     │
│   │  ├── structured_logs (parsed JSON records)     │     │
│   │  ├── handoff_summaries                         │     │
│   │  ├── documents (uploaded files + parsed text)  │     │
│   │  └── chat_history                              │     │
│   └────────────────────────────────────────────────┘     │
│   Supabase Auth (JWT) + Row-Level Security               │
│   Supabase Storage (audio files, uploaded documents)     │
└──────────────────────────────────────────────────────────┘
```

### Data Flow — Voice Logging Pipeline

```
  NURSE SPEAKS
       │
       ▼
  ┌─────────────┐     ┌──────────────────┐     ┌───────────────────┐
  │  Record via  │────▶│  Upload audio    │────▶│  Whisper          │
  │  FAB Button  │     │  to FastAPI      │     │  Transcription    │
  │  (Frontend)  │     │  POST /voice     │     │  (multilingual)   │
  └─────────────┘     └──────────────────┘     └────────┬──────────┘
                                                        │
                                                        ▼
                                               ┌───────────────────┐
                                               │  Transcript       │
                                               │  Chunking &       │
                                               │  Normalization    │
                                               └────────┬──────────┘
                                                        │
                                                        ▼
                                               ┌───────────────────┐
                                               │  LLM Structured   │
                                               │  Extraction       │
                                               │                   │
                                               │  → patient_name   │
                                               │  → action         │
                                               │  → dosage         │
                                               │  → time           │
                                               │  → priority       │
                                               │  → confidence     │
                                               └────────┬──────────┘
                                                        │
                                            ┌───────────┴───────────┐
                                            │                       │
                                      confidence ≥ 0.8        confidence < 0.8
                                            │                       │
                                            ▼                       ▼
                                    ┌──────────────┐     ┌──────────────────┐
                                    │  Auto-save   │     │  Flag for        │
                                    │  to database │     │  human review    │
                                    │  as verified │     │  (editable UI)   │
                                    └──────────────┘     └──────────────────┘
```

### Data Flow — Shift Handoff

```
  END OF SHIFT TRIGGER (manual or time-based)
       │
       ▼
  ┌─────────────────────────────────────────────────────┐
  │  Aggregate all structured_logs for current shift    │
  │  ├── Group by patient                               │
  │  ├── Identify MISSED tasks (scheduled but not done) │
  │  └── Identify CRITICAL events (high priority)       │
  └──────────────────────┬──────────────────────────────┘
                         │
                         ▼
  ┌──────────────────────────────────────────────────────┐
  │  LLM Summary Generation                             │
  │  ├── Prioritize: missed tasks > critical > routine   │
  │  ├── Per-patient narrative                           │
  │  └── Overall shift summary                           │
  └──────────────────────┬───────────────────────────────┘
                         │
                         ▼
  ┌──────────────────────────────────────────────────────┐
  │  Store handoff_summary in DB                         │
  │  Inject as context for incoming Shift Agent          │
  └──────────────────────────────────────────────────────┘
```

---

## 3. Tech Stack Breakdown

### Frontend

| Layer             | Technology            | Rationale                                                                 |
|-------------------|-----------------------|---------------------------------------------------------------------------|
| **Shell**         | Tauri v2              | Cross-platform (macOS, Windows, iOS, Android) with a Rust core. Sub-10 MB binaries vs Electron's 150 MB+. Native OS access for audio capture. |
| **UI Framework**  | React 19 + TypeScript | Component-driven, type-safe UI. Massive ecosystem and hackathon velocity. |
| **Styling**       | Tailwind CSS          | Utility-first, rapid iteration. Custom design tokens for the NurseSync design system. |
| **State**         | Zustand               | Minimal boilerplate global state. Perfect for managing recording state, active shift, patient context across tabs. |
| **Icons**         | Lucide React          | Consistent, clean medical-grade iconography. Tree-shakeable.             |
| **Fonts**         | Outfit / Clash Display (headings), DM Sans (body) | Characterful, legible, avoids generic AI-slop typography. |
| **Build**         | Vite 7                | Near-instant HMR, first-class Tauri integration.                         |

### Backend

| Layer                 | Technology        | Rationale                                                             |
|-----------------------|-------------------|-----------------------------------------------------------------------|
| **Framework**         | FastAPI            | Async-first Python framework. Auto-generated OpenAPI docs. Best-in-class for ML pipeline integration. |
| **Speech-to-Text**    | OpenAI Whisper     | State-of-the-art multilingual ASR. Supports Hindi, Spanish, Tagalog, and 90+ languages out of the box. |
| **LLM**              | OpenAI GPT-4o / Claude | Structured JSON extraction, handoff summary generation, shift agent reasoning. |
| **Document Parsing**  | LLM + pytesseract (OCR fallback) | Parse prescriptions, discharge summaries, and lab reports into structured text. |
| **Task Queue**        | Background Tasks (FastAPI) / Celery (post-MVP) | Async processing for audio transcription and LLM calls. |

### Database & Infrastructure

| Layer            | Technology          | Rationale                                                              |
|------------------|---------------------|------------------------------------------------------------------------|
| **Database**     | PostgreSQL (Supabase) | Relational integrity for medical records. JSONB columns for flexible structured log storage. Row-Level Security for multi-tenant nurse access. |
| **Auth**         | Supabase Auth        | JWT-based auth, magic link / password login. Pre-built, no custom auth needed for hackathon. |
| **File Storage** | Supabase Storage     | Audio files (.webm/.wav) and uploaded documents (PDF, images).         |
| **Hosting**      | Railway / Render (Backend), Supabase (DB) | Simple deployment, free tiers suitable for hackathon demo.            |

---

## 4. Core Features — MVP vs Post-Hackathon

### MVP (Hackathon Deliverable) ✅

These features **must** be functional for the demo.

| #  | Feature                        | Description                                                                                                      | Priority    |
|----|--------------------------------|------------------------------------------------------------------------------------------------------------------|-------------|
| 1  | **Voice Recording**            | Global floating action button (FAB) accessible from all tabs. Records audio via `MediaRecorder` API. Visual waveform feedback during recording. | 🔴 Critical |
| 2  | **Audio Transcription**        | Audio uploaded to FastAPI → transcribed via Whisper. Multilingual support (at minimum: English + Hindi). Returns raw transcript to frontend. | 🔴 Critical |
| 3  | **Structured Data Extraction** | LLM parses transcript into structured JSON: `{ patient_name, action, dosage, time, priority, confidence_score }`. Low-confidence entries flagged. | 🔴 Critical |
| 4  | **Editable Transcripts**       | Nurse can review and manually correct any transcription or extracted field before final save.                     | 🔴 Critical |
| 5  | **Dashboard View**             | Stats overview (patient count, active alerts, shift progress). AI insights card. Patient list with vitals preview. | 🔴 Critical |
| 6  | **Patient Log Timeline**       | Per-patient chronological feed of all voice logs and extracted actions for the current shift.                     | 🟡 High     |
| 7  | **Shift Handoff Summary**      | One-click generation of an end-of-shift summary. Prioritizes missed and critical tasks. Readable, printable format. | 🟡 High     |
| 8  | **Tab Navigation**             | Fixed bottom tab bar: Dashboard, Schedule, Patients, Profile. Smooth transitions.                                | 🟡 High     |
| 9  | **Shift Agent (Chat)**         | Conversational AI interface. Injected with current shift's logs as context. Incoming nurse can ask: *"Was Sarah's wound dressing done?"* | 🟡 High     |
| 10 | **Auth (Basic)**               | Supabase email/password login. Session persistence. Nurse profile with assigned shift + unit.                    | 🟡 High     |

### Post-Hackathon 🚀

These features elevate NurseSync from a demo to a deployable product.

| #  | Feature                           | Description                                                                                                      |
|----|-----------------------------------|------------------------------------------------------------------------------------------------------------------|
| 11 | **Document Parsing**              | Upload prescriptions, lab reports, discharge summaries. OCR + LLM extraction. Cross-reference doctor's orders against logged actions. |
| 12 | **Real-Time Collaboration**       | Multiple nurses viewing/editing the same patient's log via Supabase Realtime (WebSocket).                        |
| 13 | **Smart Scheduling**              | Auto-generate task schedules from doctor's orders. Push notifications for upcoming tasks (Tauri native notifications). |
| 14 | **Offline Mode**                  | Local SQLite cache (via Tauri's SQL plugin) for recording and logging when network is unavailable. Sync on reconnect. |
| 15 | **FHIR/HL7 Integration**         | Export structured logs in FHIR-compliant format for EHR interoperability.                                        |
| 16 | **Voice Streaming (Real-Time)**   | Stream audio via WebSocket for live transcription instead of batch upload.                                        |
| 17 | **Role-Based Access Control**     | Differentiate between nurse, charge nurse, and attending physician views and permissions.                         |
| 18 | **Analytics Dashboard**           | Shift-over-shift trends, documentation compliance rates, average response times.                                 |
| 19 | **Localization**                  | Full UI localization (not just voice — button labels, menus, handoff reports in the nurse's preferred language).  |
| 20 | **Audit Trail**                   | Immutable log of all edits to transcripts and structured records for compliance.                                 |

---

## 5. UI/UX & Design System Guidelines

### Design Philosophy

**High-End Medical Utilitarian** — The interface should feel like a precision instrument, not a consumer app. Think: Braun medical devices meets Apple Health. Every pixel serves a purpose. No decorative noise.

### Color Palette

| Token                  | Value       | Usage                                                        |
|------------------------|-------------|--------------------------------------------------------------|
| `--color-bg`           | `#F8FAFC`   | Global page background. Subtle cool gray, never pure white.  |
| `--color-surface`      | `#FFFFFF`   | Card and panel backgrounds. Pure white for contrast.         |
| `--color-accent`       | `#0D9488`   | Primary teal/mint. Active states, links, FAB ring, alerts.   |
| `--color-accent-light` | `#CCFBF1`   | Accent background tint. Badges, subtle highlights.           |
| `--color-accent-dark`  | `#0F766E`   | Hover/pressed state for accent elements.                     |
| `--color-danger`       | `#EF4444`   | Critical alerts, missed tasks, recording indicator.          |
| `--color-warning`      | `#F59E0B`   | Moderate priority flags, trending vitals.                    |
| `--color-success`      | `#10B981`   | Completed tasks, stable vitals, confirmed actions.           |
| `--color-text-primary` | `#0F172A`   | Primary text. Slate-900 for maximum readability.             |
| `--color-text-secondary`| `#64748B`  | Secondary/muted text. Timestamps, labels.                    |
| `--color-border`       | `#E2E8F0`   | Card borders, dividers. Barely visible structure.            |

### Typography

| Role        | Font Family       | Weight       | Size (Desktop) | Notes                                          |
|-------------|-------------------|--------------|----------------|-------------------------------------------------|
| **H1**      | Outfit            | 700 (Bold)   | 28px / 1.2     | Page titles. Used sparingly.                    |
| **H2**      | Outfit            | 600 (Semi)   | 22px / 1.3     | Section headers (e.g., "My Patients").          |
| **H3**      | Outfit            | 600 (Semi)   | 18px / 1.3     | Card titles, patient names.                     |
| **Body**    | DM Sans           | 400 (Regular)| 15px / 1.6     | All body text, descriptions, log entries.       |
| **Body Bold** | DM Sans         | 500 (Medium) | 15px / 1.6     | Inline emphasis, stat values.                   |
| **Caption** | DM Sans           | 400 (Regular)| 13px / 1.5     | Timestamps, metadata, labels.                   |
| **Mono**    | JetBrains Mono    | 400          | 13px / 1.5     | Vitals, dosages, numerical data.                |

> ⛔ **Banned fonts:** Inter, Roboto, Arial, Helvetica, system-ui defaults. These are non-negotiable.

### Elevation & Shadows

```css
/* Card resting state */
--shadow-card: 0 1px 3px rgba(0, 0, 0, 0.04), 0 1px 2px rgba(0, 0, 0, 0.06);

/* Card hover / interactive state */
--shadow-card-hover: 0 4px 12px rgba(0, 0, 0, 0.08), 0 2px 4px rgba(0, 0, 0, 0.04);

/* FAB and modals */
--shadow-elevated: 0 8px 24px rgba(0, 0, 0, 0.12), 0 4px 8px rgba(0, 0, 0, 0.06);

/* Active recording FAB */
--shadow-recording: 0 0 0 6px rgba(239, 68, 68, 0.2), 0 8px 24px rgba(239, 68, 68, 0.15);
```

### Spacing Scale

Use a **4px base unit** system: `4, 8, 12, 16, 20, 24, 32, 40, 48, 64, 80`.

- Card padding: `20px` (desktop), `16px` (mobile).
- Card gap: `12px`.
- Section gap: `24px`.
- Page margin: `24px` (desktop), `16px` (mobile).

### Border Radius

| Element       | Radius  |
|---------------|---------|
| Cards         | `16px`  |
| Buttons       | `12px`  |
| Badges/Chips  | `8px`   |
| FAB           | `50%` (circle) |
| Input fields  | `10px`  |

### Component Specifications

#### Floating Action Button (FAB) — Voice Recorder

The FAB is the **single most important UI element** in the application.

- **Position:** Fixed bottom-right, overlapping the bottom tab bar by ~50%. Centered on mobile.
- **Size:** `64px` diameter (desktop), `56px` (mobile).
- **Default State:** White background, teal microphone icon, elevated shadow.
- **Recording State:** Pulsing red ring animation (`--shadow-recording`), white microphone icon on red background, recording duration timer appears above.
- **Interaction:** Single tap to start/stop recording. Long-press to cancel.

#### Bottom Tab Bar

- **Height:** `64px` + safe area inset (mobile).
- **Background:** `#FFFFFF` with top border `1px solid var(--color-border)`.
- **Icons:** Lucide icons, `24px`. Inactive: `--color-text-secondary`. Active: `--color-accent` with a subtle filled background dot beneath.
- **Labels:** `11px` DM Sans below icons.
- **Tabs:** Dashboard, Schedule, Patients, Profile.

#### Patient Cards

- White background, `16px` radius, `--shadow-card`.
- Top row: Room number badge (teal background), patient name (Outfit 600), status chip.
- Body: Vitals grid (BPM, BP, SpO2) using monospace numerals.
- Footer: Next task with timestamp, or completed task with checkmark.

#### AI Insight Card

- Teal-tinted left border (`4px solid var(--color-accent)`).
- Icon + bold heading + description text.
- Dismiss button (ghost style, right-aligned).

### Motion & Animation

- **Page transitions:** Horizontal slide, 250ms ease-out.
- **Card hover:** `transform: translateY(-2px)`, shadow transition, 150ms.
- **FAB recording pulse:** `@keyframes pulse` — scale 1 → 1.08, opacity ring, 1.5s infinite.
- **Skeleton loaders:** Shimmer animation for all data-fetching states. No empty white screens.
- **Toast notifications:** Slide in from top-right, auto-dismiss after 4s.

### Accessibility

- All interactive elements: minimum `44px` tap target.
- Color contrast: WCAG AA minimum (4.5:1 for text, 3:1 for large text).
- Focus rings: `2px solid var(--color-accent)`, `2px` offset.
- Screen reader labels on all icon-only buttons.

---

## 6. API Contract Outline

### Base Configuration

```
Base URL:     https://api.nursesync.dev/api/v1
Auth:         Bearer <supabase_jwt>
Content-Type: application/json (unless multipart)
```

All responses follow a standard envelope:

```json
{
  "success": true,
  "data": { ... },
  "error": null,
  "timestamp": "2026-02-21T15:30:00Z"
}
```

---

### 6.1 Authentication

#### `POST /auth/login`

Login with Supabase credentials (proxied or direct). 

```
Request:
{
  "email": "nurse@hospital.org",
  "password": "••••••••"
}

Response:
{
  "success": true,
  "data": {
    "user_id": "uuid",
    "name": "Priya Sharma",
    "role": "icu_nurse",
    "unit": "ICU-A",
    "current_shift": "evening",
    "access_token": "eyJhbG...",
    "refresh_token": "dGhpcyBpcyBh..."
  }
}
```

#### `POST /auth/refresh`

Refresh an expired JWT.

---

### 6.2 Voice Pipeline

#### `POST /voice/upload`

Upload audio for transcription and structured extraction.

```
Request: multipart/form-data
  - audio: File (.webm, .wav, .m4a) — max 25 MB
  - shift_id: string (uuid)
  - language_hint?: string (e.g., "en", "hi", "auto")

Response:
{
  "success": true,
  "data": {
    "voice_log_id": "uuid",
    "transcript": {
      "raw_text": "Gave 10 mg of paracetamol to Ishaan Bhalla at 10 PM",
      "language_detected": "en",
      "duration_seconds": 4.2
    },
    "extracted": {
      "patient_name": "Ishaan Bhalla",
      "action": "medication_administered",
      "details": "paracetamol 10mg oral",
      "time": "22:00",
      "priority": "routine",
      "confidence_score": 0.94
    },
    "needs_review": false
  }
}
```

#### `PUT /voice/{voice_log_id}/transcript`

Edit a transcript after review.

```
Request:
{
  "corrected_text": "Gave 10 mg of paracetamol to Ishaan Bhalla at 10 PM",
  "corrected_fields": {
    "patient_name": "Ishaan Bhalla",
    "time": "22:00"
  }
}

Response:
{
  "success": true,
  "data": {
    "voice_log_id": "uuid",
    "status": "verified",
    "updated_extraction": { ... }
  }
}
```

---

### 6.3 Logs

#### `GET /logs?shift_id={id}&patient_id={id}`

Fetch structured logs for a shift, optionally filtered by patient.

```
Response:
{
  "success": true,
  "data": {
    "logs": [
      {
        "log_id": "uuid",
        "voice_log_id": "uuid",
        "patient_name": "Ishaan Bhalla",
        "patient_id": "uuid",
        "action": "medication_administered",
        "details": "paracetamol 10mg oral",
        "time": "22:00",
        "priority": "routine",
        "confidence_score": 0.94,
        "status": "verified",
        "created_at": "2026-02-21T22:01:00Z"
      }
    ],
    "total": 1,
    "flagged_count": 0
  }
}
```

#### `GET /logs/{log_id}`

Fetch a single log entry with full detail (including raw transcript, audio URL).

#### `DELETE /logs/{log_id}`

Soft-delete a log entry (mark as `deleted`, retain for audit).

---

### 6.4 Handoff

#### `POST /handoff/generate`

Generate an end-of-shift handoff summary.

```
Request:
{
  "shift_id": "uuid",
  "include_patients": ["uuid", "uuid"],  // optional, defaults to all
  "highlight_missed": true
}

Response:
{
  "success": true,
  "data": {
    "handoff_id": "uuid",
    "shift_id": "uuid",
    "summary": {
      "overall": "Evening shift (14:00–22:00) in ICU-A. 12 patients, 34 logged actions. 2 critical items require immediate attention.",
      "critical_items": [
        {
          "patient_name": "Sarah Johnson",
          "room": "302",
          "issue": "Wound dressing change scheduled for 20:00 — NOT completed",
          "priority": "critical"
        }
      ],
      "per_patient": [
        {
          "patient_id": "uuid",
          "patient_name": "Sarah Johnson",
          "room": "302",
          "summary": "Post-op recovery. Vitals stable. Morning meds administered at 08:15. ⚠️ Wound dressing change at 20:00 NOT completed.",
          "pending_tasks": ["Wound dressing change"],
          "completed_tasks": ["Morning meds", "Vitals check x3", "IV fluid change"]
        }
      ]
    },
    "generated_at": "2026-02-21T22:05:00Z"
  }
}
```

#### `GET /handoff/{handoff_id}`

Retrieve a previously generated handoff.

---

### 6.5 Shift Agent (Chat)

#### `POST /chat/message`

Send a message to the shift agent.

```
Request:
{
  "shift_id": "uuid",
  "message": "Was Sarah Johnson's wound dressing completed?",
  "conversation_id": "uuid"  // null for new conversation
}

Response:
{
  "success": true,
  "data": {
    "conversation_id": "uuid",
    "reply": "No — Sarah Johnson's wound dressing (Room 302) was scheduled for 20:00 but was not completed during the evening shift. This is flagged as a critical pending task in the handoff summary.",
    "sources": [
      {
        "log_id": "uuid",
        "reference": "Scheduled task: wound dressing at 20:00, status: pending"
      }
    ]
  }
}
```

#### `GET /chat/history?conversation_id={id}`

Retrieve full chat history for a conversation.

---

### 6.6 Patients

#### `GET /patients?unit={unit_id}`

List patients assigned to the nurse's unit.

```
Response:
{
  "success": true,
  "data": {
    "patients": [
      {
        "patient_id": "uuid",
        "name": "Sarah Johnson",
        "room": "302",
        "diagnosis": "Post-op recovery — appendectomy",
        "admitted_at": "2026-02-19T10:00:00Z",
        "vitals": {
          "heart_rate": 82,
          "blood_pressure": "118/76",
          "spo2": 97,
          "temperature": 37.1
        },
        "active_tasks": 2,
        "alert_level": "warning"
      }
    ]
  }
}
```

#### `GET /patients/{patient_id}`

Full patient detail including log history, tasks, and vitals timeline.

---

### 6.7 Documents (Post-MVP)

#### `POST /documents/upload`

Upload a prescription, lab report, or discharge summary.

```
Request: multipart/form-data
  - file: File (.pdf, .jpg, .png) — max 10 MB
  - patient_id: string (uuid)
  - document_type: "prescription" | "lab_report" | "discharge_summary"

Response:
{
  "success": true,
  "data": {
    "document_id": "uuid",
    "parsed_text": "...",
    "extracted_orders": [
      {
        "medication": "Amoxicillin 500mg",
        "frequency": "TID",
        "duration": "7 days",
        "matched_logs": ["uuid"]  // cross-referenced with existing logs
      }
    ],
    "compliance_status": "partial"  // all_met | partial | non_compliant
  }
}
```

---

### 6.8 Shifts

#### `GET /shifts/current`

Get the current active shift for the authenticated nurse.

#### `POST /shifts/start`

Start a new shift (clock in).

#### `POST /shifts/end`

End the current shift (clock out). Auto-triggers handoff generation prompt.

---

## Database Schema (Simplified)

```sql
-- Core tables
users (
  id UUID PRIMARY KEY,
  email TEXT UNIQUE NOT NULL,
  name TEXT NOT NULL,
  role TEXT DEFAULT 'icu_nurse',
  unit TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

patients (
  id UUID PRIMARY KEY,
  name TEXT NOT NULL,
  room TEXT,
  diagnosis TEXT,
  admitted_at TIMESTAMPTZ,
  unit TEXT,
  active BOOLEAN DEFAULT TRUE
);

shifts (
  id UUID PRIMARY KEY,
  nurse_id UUID REFERENCES users(id),
  unit TEXT,
  shift_type TEXT,  -- 'morning', 'evening', 'night'
  started_at TIMESTAMPTZ,
  ended_at TIMESTAMPTZ,
  status TEXT DEFAULT 'active'  -- 'active', 'completed'
);

voice_logs (
  id UUID PRIMARY KEY,
  shift_id UUID REFERENCES shifts(id),
  audio_url TEXT,
  raw_transcript TEXT,
  language_detected TEXT,
  duration_seconds FLOAT,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

structured_logs (
  id UUID PRIMARY KEY,
  voice_log_id UUID REFERENCES voice_logs(id),
  shift_id UUID REFERENCES shifts(id),
  patient_id UUID REFERENCES patients(id),
  patient_name TEXT,
  action TEXT,
  details TEXT,
  dosage TEXT,
  time_recorded TIME,
  priority TEXT DEFAULT 'routine',  -- 'routine', 'high', 'critical'
  confidence_score FLOAT,
  status TEXT DEFAULT 'pending',    -- 'pending', 'verified', 'flagged', 'deleted'
  edited_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

handoff_summaries (
  id UUID PRIMARY KEY,
  shift_id UUID REFERENCES shifts(id),
  summary_json JSONB,
  generated_at TIMESTAMPTZ DEFAULT NOW()
);

chat_history (
  id UUID PRIMARY KEY,
  conversation_id UUID,
  shift_id UUID REFERENCES shifts(id),
  role TEXT,  -- 'user', 'assistant'
  message TEXT,
  sources JSONB,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

documents (
  id UUID PRIMARY KEY,
  patient_id UUID REFERENCES patients(id),
  file_url TEXT,
  document_type TEXT,
  parsed_text TEXT,
  extracted_data JSONB,
  uploaded_by UUID REFERENCES users(id),
  created_at TIMESTAMPTZ DEFAULT NOW()
);
```

---

## Quick Reference — File Structure

```
nursesync/
├── src/                          # React frontend
│   ├── assets/
│   ├── components/
│   │   ├── ui/                   # Reusable primitives (Button, Card, Badge, Input)
│   │   ├── layout/               # TabBar, Header, FAB
│   │   ├── voice/                # VoiceRecorder, Waveform, TranscriptEditor
│   │   ├── patients/             # PatientCard, PatientList, VitalsGrid
│   │   ├── handoff/              # HandoffSummary, CriticalItemCard
│   │   └── chat/                 # ChatBubble, ChatInput, AgentPanel
│   ├── pages/
│   │   ├── Dashboard.tsx
│   │   ├── Schedule.tsx
│   │   ├── Patients.tsx
│   │   └── Profile.tsx
│   ├── stores/                   # Zustand stores
│   │   ├── useAuthStore.ts
│   │   ├── useShiftStore.ts
│   │   ├── useVoiceStore.ts
│   │   └── usePatientStore.ts
│   ├── services/                 # API client functions
│   │   ├── api.ts                # Base axios/fetch config
│   │   ├── voiceService.ts
│   │   ├── logService.ts
│   │   ├── handoffService.ts
│   │   └── chatService.ts
│   ├── types/                    # TypeScript interfaces
│   │   ├── patient.ts
│   │   ├── log.ts
│   │   ├── shift.ts
│   │   └── api.ts
│   ├── hooks/                    # Custom React hooks
│   │   ├── useVoiceRecorder.ts
│   │   └── useShiftAgent.ts
│   ├── App.tsx
│   ├── App.css
│   └── main.tsx
├── src-tauri/                    # Tauri Rust backend
├── backend/                      # FastAPI backend (separate repo or monorepo)
│   ├── app/
│   │   ├── main.py
│   │   ├── routers/
│   │   │   ├── voice.py
│   │   │   ├── logs.py
│   │   │   ├── handoff.py
│   │   │   ├── chat.py
│   │   │   └── documents.py
│   │   ├── services/
│   │   │   ├── whisper_service.py
│   │   │   ├── llm_service.py
│   │   │   └── document_parser.py
│   │   ├── models/
│   │   ├── schemas/
│   │   └── config.py
│   ├── requirements.txt
│   └── Dockerfile
├── Project Scope.md              # ← You are here
├── index.html
├── package.json
├── tsconfig.json
├── vite.config.ts
└── tailwind.config.ts
```

---

## Appendix: Hackathon Demo Script (Suggested)

1. **Login** → Show the clean auth screen. Login as Nurse Priya.
2. **Dashboard** → Walk through stats, AI insight card, patient list.
3. **Voice Log** → Tap the FAB. Speak: *"Gave 10 mg of paracetamol to Ishaan Bhalla at 10 PM."* Show real-time transcription and structured extraction.
4. **Low Confidence** → Speak muffled audio. Show the flagged entry and transcript editing flow.
5. **Patient Timeline** → Navigate to a patient. Show the chronological log of all voice entries.
6. **Handoff** → Trigger end-of-shift summary. Highlight the missed wound dressing for Sarah Johnson.
7. **Shift Agent** → Ask: *"Was Sarah's wound dressing done?"* Show the AI response with source citations.
8. **Cross-Platform** → Quick flash of the same app running on macOS and an iPhone simulator.

---

*Built for the frontlines. Designed to disappear. NurseSync lets nurses nurse.*
