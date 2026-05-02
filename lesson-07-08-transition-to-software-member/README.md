# Lesson 07-08: Transition to Software Member

## Purpose

Turn training into actual team contribution. This page defines what "ready to be a software member" means and how new members should enter real robot work without repeating the 2025 third robot problems.

## Prerequisites

- Weeks 1-6 completed.
- Student has done at least one scoped code task with support.

## Time box

Two weeks of transition work, pairing, and check-ins.

## Learning goals

- Understand what kinds of tasks new members should own first.
- Understand how work should be paired, reviewed, tested, and merged.
- Start contributing to 3rd robot or other scoped tasks without collapsing into low-quality unreviewed code.
- Build the habit of making progress independently while still asking for support correctly.

## What readiness means

A new member does not need to be fully independent at every stage. The baseline expectation is:

- they can read the docs and nearby code they need,
- they can understand enough to start implementing,
- they can make progress by themselves with support,
- they can explain what they tried and where they are stuck.

Typical early tasks include:

- logging,
- sim setup,
- basic superstructure logic,
- small helper classes or helper methods,
- easy but urgent buttons or conditions,
- lower-priority exploratory tasks with guidance.

## Problems this phase is trying to avoid

- low initiative to take on tasks,
- poor understanding of older code,
- weak debugging habits,
- no understanding of robot-side testing,
- overreliance on GPT,
- slow review loops that block progress,
- repeating the 2025 third robot situation where quality control slipped.

## Transition structure

Use this phase to move from lessons into real task flow:

1. Assign a scoped real task.
2. Require the student to explain the implementation plan before coding.
3. Check in daily or at each DP touchpoint.
4. Pair testing on robot with an experienced member when possible.
5. Review code promptly so the student is not blocked for days.

## 3rd robot expectations

The goal is for newer members to write as much real code as possible, but not alone and not without quality control.

Use these guardrails:

- experienced members may still need to participate in critical coding or testing phases,
- new members should not own the whole testing/debugging burden alone,
- pairing during deploy and on-robot debugging is preferred,
- training leads should review quickly unless a critical task prevents it.

## Deploy and Driver Station exposure

After the training sequence, explicitly teach:

- how deployment works,
- what Driver Station is responsible for,
- how to verify the robot is in the expected mode,
- how to test safely with another member.

This should not stay as hidden knowledge held only by experienced members.

## Required mech/elec callout

By this phase, students should have at least basic familiarity with:

- CAN as a real bus with devices and IDs,
- the rough electrical path from code to hardware action,
- how mechanical state can affect software debugging,
- the fact that "it compiled" is not the same as "it works on robot."

## Exercise

- Assign one real scoped task from an existing robot codebase or 3rd robot work.
- Require the student to write or say:
  - what the task is,
  - what files they expect to touch,
  - what evidence will prove it works,
  - when they need review or robot help.
- Have them complete the task with check-ins.
- If robot testing is needed, pair them with an experienced member.

## Debugging prompts

- If the student is stuck, ask what evidence they have, not what they feel.
- If they jump to GPT before reading code, send them back to the local implementation first.
- If testing is the blocker, separate code uncertainty from robot-process uncertainty.

## Deliverable / checkoff

- Student completes at least one real scoped task with support.
- Student participates in review and can respond to feedback without losing track of the code path.
- Student demonstrates a basic deploy or testing workflow with a partner if the task needs it.
- Student shows they can make progress independently instead of waiting passively.

## Exit criteria

By the end of this phase, a student should be ready to be treated as a software member for scoped work. That does not mean they are ready to solo every DP or lead every robot test. It means they can join the actual workflow without being carried through every step.
