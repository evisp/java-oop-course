# Install Java

<p class="meta"><span class="badge">15 minutes</span><span class="badge">Week 1</span><span class="badge">No prerequisites</span></p>

You will install the Java Development Kit, confirm from a terminal that it is the version this course uses, and understand what you just put on your machine. After this page you can compile and run Java without any editor at all.

Java is not one program. It is a compiler, a runtime and a set of tools, and the difference between them is behind most of the confusing errors beginners hit. Ten minutes here saves an afternoon in week three.

## What a JDK actually contains

<svg viewBox="0 0 520 320" role="img" xmlns="http://www.w3.org/2000/svg"><title>What the JDK contains and where Eclipse fits</title><desc>A box labelled Eclipse sits at the top with an arrow pointing down into a large dashed box labelled the JDK. Inside the dashed box are three rows: javac which compiles your code to class files, java which runs the compiled code, and the remaining tools jshell, jar and jlink. A bracket beside the java row notes that a JRE contains only that part.</desc><rect x="150" y="14" width="220" height="46" rx="8" fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"/><text x="260" y="36" font-family="Inter, sans-serif" font-size="17" font-weight="600" fill="var(--c-ink)" text-anchor="middle">Eclipse</text><text x="260" y="52" font-family="Inter, sans-serif" font-size="12" fill="var(--c-muted)" text-anchor="middle">calls the tools below for you</text><g stroke="var(--c-muted)" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="260" y1="62" x2="260" y2="126" stroke-dasharray="4 4"/><polyline points="255,119 260,127 265,119"/></g><rect x="26" y="88" width="468" height="216" rx="12" fill="none" stroke="var(--c-accent)" stroke-width="1.5" stroke-dasharray="6 6"/><text x="46" y="110" font-family="Inter, sans-serif" font-size="14" font-weight="600" fill="var(--c-accent)">The JDK, what you install on this page</text><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="46" y="128" width="300" height="48" rx="7"/><rect x="46" y="186" width="300" height="48" rx="7"/><rect x="46" y="244" width="300" height="44" rx="7"/></g><g font-family="JetBrains Mono, monospace" font-size="16" font-weight="500" fill="var(--c-accent)"><text x="66" y="152">javac</text><text x="66" y="210">java</text><text x="66" y="270">jshell jar jlink</text></g><g font-family="Inter, sans-serif" font-size="13" fill="var(--c-muted)"><text x="66" y="168">turns your .java files into .class files</text><text x="66" y="226">runs the .class files</text><text x="230" y="270">the rest of the toolkit</text></g><g stroke="var(--c-muted)" stroke-width="1.5" fill="none"><path d="M364 190 L372 190 L372 230 L364 230"/></g><text x="382" y="206" font-family="Inter, sans-serif" font-size="13" fill="var(--c-muted)">a JRE is</text><text x="382" y="222" font-family="Inter, sans-serif" font-size="13" fill="var(--c-muted)">only this part</text></svg>

A JRE can run Java. It cannot compile it. That is the whole distinction, and it is why installing a JRE by mistake produces a machine that runs other people's programs but not your own.

## 1. Install Temurin 25

This course uses **Eclipse Temurin 25**, which is the free OpenJDK build published by the Eclipse Adoptium project.

Open PowerShell and run:

```powershell
winget install -e --id EclipseAdoptium.Temurin.25.JDK
```

Close PowerShell when it finishes and open a new one. The installer changes your PATH, and an already-open terminal will not see the change.

??? note "If winget is not available"

    `winget` ships with Windows 11 and recent Windows 10. If the command is not recognised, download the MSI installer instead.

    1. Go to [adoptium.net/temurin/releases](https://adoptium.net/temurin/releases/)
    2. Choose **Windows**, **x64**, **JDK**, version **25**
    3. Download the `.msi` file and run it
    4. On the setup options screen, turn on **Set JAVA_HOME variable** and **Add to PATH**

    Those two options are off by default and they are exactly the ones you need.

!!! mistake "Do not download Java from Oracle"

    Oracle's own build is a different product with different licence terms that have changed more than once, and some downloads want an account. Temurin is built from the same OpenJDK source, it is free for any use, and nothing in this course can tell the difference. If you already installed Oracle's version, uninstall it before continuing so you do not end up with two.

## 2. Check that it worked

In a new PowerShell window:

```powershell
java -version
javac -version
```

Both should report version 25. Something like this:

```text
openjdk version "25.0.4" 2026-07-21
OpenJDK Runtime Environment Temurin-25.0.4+10 (build 25.0.4+10)
```

The important part is the number at the front. The digits after the dot will differ from the example and that is fine.

!!! mistake "`javac` works but `java` shows a different version"

    That means you have more than one Java on the machine and the two commands are finding different ones. It will not cause an error today. It will cause a confusing one in week six. Uninstall the versions you do not need from **Settings** then **Apps**, then check both commands again.

## 3. Run something without an editor

You do not need Eclipse to write Java. Proving that now makes the next page make sense.

Start `jshell`, which is Java's interactive prompt:

```powershell
jshell
```

Type this at the `jshell>` prompt and press ++enter++:

```java
System.out.println("Running on " + Runtime.version());
```

Then type `/exit` to leave.

If that printed a line starting with `Running on 25`, your toolkit is installed, correct, and working. Everything from here is convenience.

## When it goes wrong

??? failure "`'java' is not recognized as an internal or external command`"

    Windows cannot find the program. Two likely causes.

    **You did not open a new terminal.** PATH changes only apply to windows opened afterwards. Close PowerShell, open it again, try again.

    **PATH was not updated during install.** If you used the MSI and left the default options, this is what happened. Run the installer again, choose **Change**, and turn on **Set JAVA_HOME variable** and **Add to PATH**.

    Check what Windows can actually see:

    ```powershell
    where.exe java
    ```

    No output means nothing is on your PATH.

??? failure "The version number is not 25"

    An older Java was already installed and is being found first. See which ones are present:

    ```powershell
    where.exe java
    where.exe javac
    ```

    If more than one path appears, remove the ones you do not want through **Settings** then **Apps**, then open a new terminal and check again.

??? failure "`java` works but `javac` does not"

    You have a JRE, not a JDK. The runtime is installed and the compiler is not. Go back to step 1 and make sure you are installing the package ending in `.JDK`.

??? failure "winget says the package was not found"

    Your package source is out of date. Refresh it and try again:

    ```powershell
    winget source update
    ```

    If it still fails, use the MSI installer from the collapsed block in step 1.

## Check yourself

!!! verify "Prove the toolkit is complete"

    Make a folder, write a file by hand, compile it and run it. No editor involved.

    ```powershell
    mkdir C:\dev\jdkcheck
    cd C:\dev\jdkcheck
    ```

    Create `Check.java` in Notepad with exactly this:

    ```java
    public class Check {
        public static void main(String[] args) {
            System.out.println("Compiled and running on " + Runtime.version());
        }
    }
    ```

    Then:

    ```powershell
    javac Check.java
    java Check
    ```

    You should see a `Check.class` file appear, and a line printed starting with `Compiled and running on 25`. You have now used both halves of the JDK, and you have seen the class file that the diagram at the top of this page was talking about.

## Think about it

1.  Temurin and Oracle's build come from the same OpenJDK source and produce the same output. So what were you actually choosing between? Now imagine you are at a company with four hundred developers and someone from finance asks what the Java on those machines costs.
2.  Compiling and running are two separate commands. Why split them at all, instead of one command that just runs your file? Now think about sending your finished program to someone who will never edit it. Which of the two do they need?
3.  Something on your machine is "the default Java". Where does that setting actually live? Next year a different course asks you to install Java 21 alongside this one. What breaks, and what would you want to have set up today to make that painless?
4.  The `.class` file you produced is not readable and not the thing you wrote. Why does Java do this, when some languages run your source directly? Consider what has to be true for that class file to run on a machine that is not yours.

<div class="page-nav" markdown>
[Install Eclipse](eclipse-setup.md){ .page-nav__next }
</div>
