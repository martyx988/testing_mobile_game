# AGENTS.md — Codex Operating Manual
Python Backend / Stock Data

This repository is developed with Codex in VS Code using a **continuous sprint workflow** that simulates a small backend engineering team.

Codex must follow the workflow, rules, and documentation structure defined in this file.

If a user prompt conflicts with this file, Codex must **ask for clarification before proceeding**.

---

# Purpose of This File

`AGENTS.md` defines **how work is performed in this repository**, not the detailed behavior of the system itself.

This file contains:

- repository workflow rules
- quality standards
- validation requirements
- documentation responsibilities
- task execution procedures

Detailed technical system behavior is documented in:

- `docs/architecture.md`
- `docs/technical/`
- `docs/api.md`
- `docs/runbook.md`

Codex must consult these documents when relevant.

---

# Core Principles

Codex must follow these principles:

- Prefer **small incremental changes**
- Preserve existing architecture unless explicitly instructed
- Maintain deterministic and reproducible behavior
- Prefer validation over assumptions
- Avoid large refactors unless necessary
- Tests and documentation are **first-class citizens**
- No silent failures
- Explicit error handling is required

---

# Repository Documentation Structure

The repository maintains two levels of documentation.

## Level 1 — Initiative Coordination

These files coordinate large tasks and project direction.

### SPEC.md

Defines **what the system or initiative must accomplish**.

Contains:

- goals
- scope
- non-goals
- constraints
- acceptance criteria
- examples or expected behavior

SPEC.md answers:

**"What problem are we solving?"**

---

### PLAN.md

Defines the **approved implementation strategy**.

Contains:

- high-level milestones
- affected system areas
- implementation sequencing
- risk areas
- validation strategy

PLAN.md answers:

**"What is the agreed path to implement the spec?"**

Codex must treat PLAN.md as the **source of truth for implementation sequencing**.

---

### IMPLEMENT.md

Defines the **execution rules for Codex while implementing work**.

Typical contents include:

- implementation guidelines
- validation commands
- scope restrictions
- instructions for handling failures
- instructions for updating documentation

Codex must follow IMPLEMENT.md during implementation.

---

### DOCUMENTATION.md

A **live execution log and status document**.

Used to record:

- milestone progress
- key decisions
- validation outcomes
- system changes
- documentation updates
- known follow-ups

Codex must update this file during major task progress.

---

## Level 2 — Detailed Task Documentation

Location:

docs/tasks/

Filename format:

YYYY-MM-DD-HH-MM_<task-slug>.md

Each file documents a **single engineering task**.

Required sections:

- Task Contract
- Relevant PLAN.md milestone
- Local Implementation Plan
- Architecture Notes
- Test Plan
- Progress Log
- Validation Results
- Final Summary

Task files contain **detailed execution history** that should not clutter top-level documents.

---

# Agent Workflow

All tasks must follow this workflow.

User Request  
↓  
Planning  
↓  
Clarifying Questions (if needed)  
↓  
Architecture Review  
↓  
Testing (tests first)  
↓  
Implementation  
↓  
Validation  
↓  
QA Expansion  
↓  
Review  
↓  
Completion  

Codex must **never implement code before planning is completed**.

---

# Phase 1 — Planning

When the user requests a task:

1. Use Codex Plan Mode
2. Do NOT modify code.
3. Analyze relevant parts of the repository.
4. Identify affected modules.
5. Review related documentation (`SPEC.md`, `PLAN.md`, architecture docs).
6. Produce a structured implementation plan.

Required planning output format:

## Task
Short description.

## Understanding
Current system behavior.

## Plan
Step-by-step implementation strategy.

## Files Likely Affected
List of modules.

## Tests
Tests that will be added or modified.

## Documentation
Documentation updates required.

## Risks
Edge cases or technical risks.

## Open Questions
Any unclear requirements.

If open questions exist, Codex must **stop and ask the user**.

Implementation may proceed only after the plan is approved or confirmed safe.

### Planning Restrictions

During planning Codex must NOT:

- modify files
- generate implementation code
- commit changes
- perform refactors

---

# Phase 2 — Architecture Review

Before implementation Codex must evaluate:

- system boundaries
- data flow
- validation strategy
- error handling approach

Architecture changes must be documented in:

docs/architecture.md

if system structure changes.

---

# Phase 3 — Testing (Tests First)

Tests must be written **before implementation**.

Tests must cover:

- core functionality
- edge cases
- failure modes
- expected outputs

Public functions must always have tests.

Tests should initially fail before implementation.

---

# Phase 4 — Implementation

Implementation must:

- satisfy tests
- preserve existing architecture
- avoid unrelated refactoring
- maintain coding conventions

If implementation requires deviating from the approved plan, Codex must stop and propose an updated plan.

---

# Phase 5 — Validation

Run validation commands defined in `IMPLEMENT.md`.

Typical example:

pytest

### Environment Rule

If validation fails due to environment issues:

- install missing dependencies
- configure runtime
- retry validation

Skipping tests is **not allowed if solvable**.

---

# Phase 6 — QA Expansion

After validation passes:

- add missing edge case coverage
- confirm downstream usability
- test error handling paths

Coverage must include:

- success cases
- error cases
- edge cases

---

# Phase 7 — Review

Review changes for:

- correctness
- reliability
- error transparency
- code clarity
- test completeness

Reject changes if:

- silent failures exist
- validation is missing
- tests are incomplete

---

# Phase 8 — Completion

A task may complete only if:

- acceptance criteria met
- all tests pass
- validation completed
- documentation updated
- task file finalized

Then:

- always commit changes and push to origin/main

---

# Documentation Responsibilities

Codex must update documentation when behavior changes.

Change Type → Documentation

Developer workflow → README.md  
Architecture changes → docs/architecture.md  
API changes → docs/api.md  
Operational behavior → docs/runbook.md  
Technical decisions → docs/adr.md  

Before finishing a task, Codex must record documentation updates in `DOCUMENTATION.md`.

---

# Testing Standards

Global testing requirements:

- every public function must have tests
- no skipped tests
- explicit validation of outputs
- deterministic behavior required

---

# Error Handling Rules

The system must never allow:

- silent failures
- implicit fallbacks
- unvalidated outputs

Errors must be explicit and actionable.

---

# Definition of Done

A task is complete only if:

- acceptance criteria satisfied
- tests pass
- validation complete
- documentation updated
- task documentation completed
- changes committed and pushed

---

# Communication Format

Codex responses must include:

- current phase
- changes made
- next step
- blockers (with exact command/output if relevant)

---

# Non-Negotiables

Codex must never:

- skip workflow phases
- implement before planning
- skip tests
- ignore solvable environment issues
- introduce secrets or credentials

---

# Skill Routing Rules

Codex must load and apply the relevant skill(s) before planning or implementation when a request matches one or more skill triggers.

Skill Trigger Map:

- Skill: `build-dashboard`
- Use for: dashboard and reporting UI work
- Examples: creating, redesigning, or reviewing dashboards; KPI views and analytics/reporting pages; Grafana dashboards and observability boards; panel hierarchy, readability, and visual polish iterations

- Skill: `interface-design`
- Use for: app UI and interactive product surface design work
- Examples: mobile app UI, product screens, admin tools, settings pages, app flows, and interaction design for software interfaces

Communication requirement:

- When a skill is used, Codex must explicitly say which skill(s) it is using in a short line before proceeding.

Scalability rule:

- Add new skills by appending new `Skill`, `Use for`, and `Examples` entries to the Skill Trigger Map without changing existing workflow phase rules.

If a required skill is unavailable, Codex must state that explicitly and continue with the closest equivalent workflow.

Skill workflows augment and specialize this repository workflow. They do not replace or override `AGENTS.md` phase sequencing. If a skill instruction conflicts with `AGENTS.md`, `AGENTS.md` takes precedence.

---

# Notebook Maintenance Rule

If data loading logic, schemas, or ingestion interfaces change, update:

notebooks/manual_data_load_testing.ipynb

so manual testing remains valid.
