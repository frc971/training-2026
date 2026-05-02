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

# Tooling Setup

This document is based off of the [971 Github Setup](https://docs.google.com/document/d/1oYIhKGeCQ42U5o2rO3sTElFUovLhWpPwCLImjnabmNA/edit?tab=t.0) document

## Windows Users

It's highly suggested that you [install](https://learn.microsoft.com/en-us/windows/wsl/install) Windows Subsystem for Linux (WSL) and use that as your terminal. It will make your life easier. If you are not sure whether you have WSL, run `wsl` in the command prompt or type `wsl` in the search bar.

## GitHub Setup

### 1. Create a GitHub account

If you don't have one, create one.

## SSH Access to GitHub

We'll use GitHub's official CLI tool to set up SSH access automatically.

### 2. Install GitHub CLI

**For Ubuntu or WSL, run:**
```
sudo snap install gh
```

**For MacOS, run:**
```
brew install gh
```

Alternatively, install from the [latest GitHub release](https://github.com/cli/cli/releases/latest).

**To confirm it's installed correctly, run:**
```
gh --version
```

### 3. Authenticate and set up SSH

Run this command to sign in to GitHub and set up SSH:

```
gh auth login
```

- When asked to choose a GitHub host, choose **GitHub.com**
- For protocol, choose **SSH**
- Don't use a passphrase
- Authenticate using "Login with a web browser"
- Continue following the instructions. It will generate and register your SSH key with GitHub automatically.

You can confirm that everything worked by running:

```
gh auth status
```

This should say you're logged in with SSH.

### 4. Test SSH connection

Test the SSH connection to GitHub:

```
ssh -T git@github.com
```

You should see a success message with your username. If not, follow the troubleshooting steps at:
[https://docs.github.com/en/authentication/connecting-to-github-with-ssh/testing-your-ssh-connection](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/testing-your-ssh-connection)

### 5. Optional: Set up commit signing with SSH

If you'd like to sign your commits using SSH, run:

```
git config --global gpg.format ssh
git config --global user.signingkey ~/.ssh/id_ed25519.pub
```

## WPILib and VS Code

### 6. Install WPILib/VSCode

Follow the instructions here:
[https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-2/wpilib-setup.html](https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-2/wpilib-setup.html)

- Select **"Install for this User"**
- Install **"Everything"**
- Choose **"Download for this computer only"**

## Download the Training Repository

### 7. `cd` to the location where you want the `training-2025` folder

#### Windows users only:

It’s recommended to save source code to your Windows home directory (`C:\Users\<username>` or `/mnt/c/Users/<username>` in WSL) so that apps like VSCode, notepad, etc. can access it easily.

Then do a soft link from the WSL home directory to it so you can access it easily from the terminal.

```
cd /mnt/c/Users; cd $USER
```

If that doesn't work, type:

```
ls -1d /mnt/c/Users/*
```

to see what home directories are available and `cd` into it.

Once you are sure that you are in the location where you want to source, do:

```
ln -sf $PWD/training-2025 ~/training-2025
```

### 8. Clone the repository

```
git clone git@github.com:frc971/training-2025.git
```

If you don't have git, download it [here](https://git-scm.com/downloads).

## Open in VS Code

### 9. Open the folder

- Go to **File -> Open Folder**
- Navigate to the directory where you did `git clone`
- Go inside `training-2025` and click **Open**

## Final Step

### 10. Read [`CONTRIBUTING.md`](https://github.com/frc971/971-robot-2025/blob/main/CONTRIBUTING.md)
