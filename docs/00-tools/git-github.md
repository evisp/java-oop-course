# Git and GitHub

<p class="meta"><span class="badge">30 minutes</span><span class="badge">Week 1</span><span class="badge badge--accent">Needs: Install Eclipse</span></p>

You will install git, create your own repository on GitHub, put your first project in it, and push it. After this page your work has a history and a copy that is not on your laptop.

This is not administration. Every sprint in this course is delivered from this repository, and at the end of the semester it is the thing you show someone. Starting it properly in week one costs half an hour. Starting it in week nine costs a weekend.

## The four places your work lives

Git is confusing at first because your files exist in several states at once, and you cannot see any of them in Explorer.

<svg viewBox="0 0 520 300" role="img" xmlns="http://www.w3.org/2000/svg"><title>How work moves from your folder to GitHub</title><desc>Four stacked boxes joined by downward arrows. Your folder holds the files you edit. The command git add moves changes into the staging area, which holds what will go into the next commit. The command git commit moves them into your repository, the history stored on your laptop. The command git push copies them to GitHub, the copy other people can see.</desc><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="110" y="10" width="300" height="50" rx="7"/><rect x="110" y="86" width="300" height="50" rx="7"/><rect x="110" y="162" width="300" height="50" rx="7"/></g><rect x="110" y="238" width="300" height="50" rx="7" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><g font-family="Inter, sans-serif" font-size="16" font-weight="600" fill="var(--c-ink)"><text x="130" y="34">Your folder</text><text x="130" y="110">Staging area</text><text x="130" y="186">Your repository</text><text x="130" y="262">GitHub</text></g><g font-family="Inter, sans-serif" font-size="13" fill="var(--c-muted)"><text x="130" y="52">the files you edit</text><text x="130" y="128">what goes into the next commit</text><text x="130" y="204">the whole history, on your laptop</text><text x="130" y="280">the copy that is not on your laptop</text></g><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="260" y1="62" x2="260" y2="82"/><polyline points="255,76 260,84 265,76"/><line x1="260" y1="138" x2="260" y2="158"/><polyline points="255,152 260,160 265,152"/><line x1="260" y1="214" x2="260" y2="234"/><polyline points="255,228 260,236 265,228"/></g><g font-family="JetBrains Mono, monospace" font-size="14" fill="var(--c-accent)"><text x="274" y="78">git add</text><text x="274" y="154">git commit</text><text x="274" y="230">git push</text></g></svg>

Three of those four are on your own machine. Only the last one involves the internet, which is why most of git keeps working on a train.

## 1. Install git

```powershell
winget install -e --id Git.Git
```

Close PowerShell and open a new one, then check:

```powershell
git --version
```

??? note "If winget is not available"

    Download the installer from [git-scm.com/download/win](https://git-scm.com/download/win) and run it. Accept every default. The defaults include Git Credential Manager, which is what saves you from typing a password on every push.

## 2. Tell git who you are

Every commit records a name and an email. Set them once:

```powershell
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

Use the same email you will use for GitHub, so your commits are linked to your account.

## 3. Create your repository on GitHub

<!-- POLICY: change the visibility and naming instructions below to match
     what you actually want from students. -->

1. Create an account at [github.com](https://github.com) if you do not have one, using the email from step 2.
2. Click **New repository**.
3. Name it `java-oop`, followed by your surname. For example `java-oop-hoxha`.
4. Leave it **Public**, unless your lecturer tells you otherwise.
5. Do **not** tick "Add a README". You are about to push an existing folder, and an empty repository makes that simpler.
6. Click **Create repository**.

GitHub now shows you a page of commands. Ignore it. The next step covers what you need.

## 4. Turn your project folder into a repository

In PowerShell, go to the project you made on the last page:

```powershell
cd C:\dev\workspace\Setup
git init
```

Before your first commit, tell git what to ignore. Create a file called `.gitignore` in that folder containing:

```text
bin/
*.class
.settings/
.classpath
.project
```

!!! mistake "Never commit the `bin` folder"

    `bin` holds the `.class` files Eclipse generates from your source. They can be rebuilt from your code in one second, they change on every single build, and committing them fills your history with noise that hides your actual work. The rule underneath: commit what you wrote, never what a tool produced from it.

## 5. Your first commit

```powershell
git add .
git status
```

`git status` lists what is staged. Read it before committing. If you see `bin/` in that list, your `.gitignore` is in the wrong folder or has a typo.

```powershell
git commit -m "First Java project, verifying the setup"
```

## 6. Connect it to GitHub and push

Copy your repository's URL from GitHub. It looks like `https://github.com/yourname/java-oop-surname.git`.

```powershell
git branch -M main
git remote add origin https://github.com/yourname/java-oop-surname.git
git push -u origin main
```

A browser window will open asking you to sign in to GitHub. That is Git Credential Manager. Approve it once and it will not ask again on this machine.

Refresh your repository page. Your files are there.

## When it goes wrong

??? failure "`Invalid username or token. Password authentication is not supported`"

    GitHub stopped accepting account passwords over HTTPS years ago. You need Git Credential Manager to handle sign-in, and it comes with Git for Windows.

    Check which git you are using:

    ```powershell
    where.exe git
    ```

    If it is not under `C:\Program Files\Git\`, you have a different git installed that cannot reach the credential helper. Install Git for Windows from step 1.

    Then point git at the helper explicitly:

    ```powershell
    git config --global credential.helper manager
    ```

    Push again. A browser window should open.

??? failure "`src refspec main does not match any`"

    You have not committed anything yet, so there is no `main` branch to push. Run `git status`. If it lists files as untracked or staged, go back and finish step 5.

??? failure "`failed to push some refs` / `Updates were rejected`"

    GitHub has something your local repository does not, usually because a README or licence was added when the repository was created. Pull it in first:

    ```powershell
    git pull --rebase origin main
    git push -u origin main
    ```

??? failure "You committed `bin/` by accident"

    Add the `.gitignore` if it is missing, then stop tracking the folder without deleting it:

    ```powershell
    git rm -r --cached bin
    git commit -m "Stop tracking compiled output"
    git push
    ```

    The files stay on your disk. Git just stops watching them. The old commits still contain them, which is fine and not worth fixing now.

??? failure "`fatal: not a git repository`"

    You are in the wrong folder, or `git init` was never run. Check where you are with `pwd`, and look for a hidden `.git` folder:

    ```powershell
    Get-ChildItem -Force -Name .git
    ```

## Check yourself

!!! verify "Prove the whole loop works"

    Make a change, commit it, push it, and see it appear.

    1. In Eclipse, open `Check.java` and add a second `System.out.println` line. Save.
    2. In PowerShell, from the project folder:

        ```powershell
        git add .
        git commit -m "Add a second line of output"
        git push
        ```

    3. Refresh GitHub and open `Check.java`. Your new line is there.
    4. Click the **commits** link. You now have two, with your messages and your name.

    That history is the thing this page was really for. From now on it grows every week, and by week 14 it is a record of how you built the whole project.

## Think about it

1.  Git keeps your work in three states on your own machine before anything reaches GitHub. Why not skip the middle and just upload the folder? Now suppose you have changed six files and only two of them are finished.
2.  You were told not to commit `bin/`, but everything in it came from files that are committed. What is the general rule underneath that? Now imagine a generated file that takes an hour to produce rather than a second. Does the rule still hold, and what would you do instead?
3.  A commit message is written for a reader. Who is it, and when do they read it? Now imagine it is you in week twelve, hunting for the last commit where the program still worked. What would you want the messages to look like?
4.  GitHub is unavailable for a whole day. What can you still do, and what can you not? What does your answer tell you about where your repository actually lives?

<div class="page-nav" markdown>
[Back to the roadmap](../roadmap.md){ .page-nav__next }
</div>
