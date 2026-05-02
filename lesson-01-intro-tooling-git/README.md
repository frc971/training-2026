# Lesson 01: Software Intro / Tooling Setup / Git + GitHub

## Purpose

Get every member into a usable development environment before deeper robot lessons start. This includes Java/WPILib setup, editor setup, repo setup, and the minimum Git workflow needed to work on team code without guessing.

## Prerequisites

- Complete the Java eval before coming to this lesson.
- Have a laptop with permission to install tools.
- Have a GitHub account.

## Time box

1-2 hours

## Learning goals

- Understand what tools are required to work on our Java WPILib robot repos.
- Clone a repo, open it, and run basic Gradle or WPILib tasks.
- Understand the Git flow well enough to make a branch, commit, pull, and open a PR later.
- Know where to go when setup breaks.

## In our codebase

- Main training reference repo: [971-second-robot-2026](../../971-second-robot-2026/README.md)
- Training lesson repo: [training-2026](../README.md)

## Lesson content

This page is intentionally a structured placeholder. The note from planning is that Week 1 can mostly reuse last year's setup wiki, but that material is not currently present in this workspace. When that older page is available, adapt it into this folder and keep the Git section below.

For 2026, the setup lesson needs to cover four things clearly:

1. WPILib and Java setup.
2. Editor setup.
3. Repo cloning and opening.
4. Git and GitHub basics.

At the end of the lesson, a student should be able to pull a repo, switch branches, run a local command, and explain how their code gets from laptop to review.

## Minimum setup topics to cover

- Install the current WPILib toolchain used by the team.
- Confirm Java version and that the WPILib VS Code extension or equivalent tooling works.
- Clone `971-second-robot-2026`.
- Open the repo and verify the student can navigate `src/main/java`.
- Explain `build.gradle`, `vendordeps`, and `src/main/deploy` at a very high level.
- Show basic Git commands:
  - `git status`
  - `git checkout -b <branch>`
  - `git add`
  - `git commit`
  - `git pull --rebase`
  - `git push`

## Git concepts to teach

- Working tree vs staged changes vs committed history.
- Why we use branches.
- Why small commits are easier to review.
- Why pulling before pushing matters.
- What a merge conflict is at a basic level.

## Debugging prompts

- If WPILib commands do not run, check Java version first.
- If Gradle fails immediately, read the first real error instead of the last line.
- If Git feels confusing, always start with `git status`.

## Exercise

- Clone `971-second-robot-2026`.
- Create a branch named `training/<your-name>-setup`.
- Add a local note in a scratch file outside the repo if needed, but do not change robot code yet.
- Run `git status` and explain what it means.
- Pull the latest changes and explain what changed in your branch state.

## Deliverable / checkoff

- Student can open the repo and find `Robot.java`, `RobotContainer.java`, and `build.gradle`.
- Student can explain the difference between a branch and a commit.
- Student can run `git status` and interpret the result.
- Student can describe how they would start a normal software task on the team.

## Before next lesson

- Make sure the environment still works at home.
- Be ready to use simulator and inspect the codebase structure next week.
