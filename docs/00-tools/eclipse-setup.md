# Install Eclipse

<p class="meta"><span class="badge">25 minutes</span><span class="badge">Week 1</span><span class="badge badge--accent">Needs: Install Java</span></p>

You will install Eclipse, point it at the JDK you already have, and build a project that proves both are working together. After this page you have somewhere to write code for the rest of the semester.

Eclipse does not compile your Java. It calls the tools you installed on the last page and shows you the results nicely. Keeping that in mind is what lets you fix things when they break, instead of reinstalling and hoping.

!!! mistake "Install Java first"

    If `javac -version` does not report 25 in a fresh PowerShell window, stop and finish [Install Java](java.md). Eclipse ships with its own small runtime and its own compiler, so without a JDK it will appear to work and then fail later in a way that looks unrelated. A clean failure now is much cheaper.

## 1. Download the installer

Go to [eclipse.org/downloads](https://www.eclipse.org/downloads/) and download the Eclipse Installer for Windows x86_64. Save it to your Downloads folder.

## 2. Check the file before you run it

Eclipse is served through mirrors around the world, so the page publishes a checksum you can compare against.

On the download page, find the **SHA-512** link next to the file you downloaded and open it. Then in PowerShell:

```powershell
Get-FileHash "$HOME\Downloads\eclipse-inst-jre-win64.exe" -Algorithm SHA512
```

Compare the output with the published value. They should match. The filename may differ slightly from the example, so use the one you actually downloaded.

This takes fifteen seconds and it is the habit worth forming. You are about to give a program from the internet permission to change your machine.

## 3. Run the installer

1. Open the `.exe`. If Windows shows a blue **Windows protected your PC** box, see the troubleshooting section below.
2. Choose **Eclipse IDE for Java Developers**.
3. Leave the install folder as it is.
4. Accept the licence and click **Install**, then **Launch**.

## 4. Choose a workspace

Eclipse asks where to keep your projects. Use a short, plain path:

```text
C:\dev\workspace
```

Tick **Use this as the default** so you are not asked again.

!!! mistake "Not in OneDrive, Desktop or Documents"

    Those folders sync to the cloud. The sync client rewrites files while Eclipse is reading them, and the result is not a clear error. It is builds that fail then succeed, files that revert while you are editing, and later a git repository that corrupts itself. The failures look random and they will cost you hours. `C:\dev\` is out of the way of all of it.

## 5. Point Eclipse at your JDK

Open **Window** then **Preferences** then **Java** then **Installed JREs**.

If an entry named something like `jdk-25` is listed and ticked, you are done. Eclipse found it on its own.

If the list is empty, or shows only a JRE:

1. Click **Add**, choose **Standard VM**, then **Next**
2. For **JRE home**, browse to `C:\Program Files\Eclipse Adoptium\jdk-25...`
3. Click **Finish**, then tick the new entry so it is the default
4. **Apply and Close**

## 6. Build something

**Create the project.** **File** then **New** then **Java Project**. Name it `Setup`. Leave everything else alone and click **Finish**.

**Create the class.** Right-click the `src` folder, then **New** then **Class**. Name it `Check`. Tick **public static void main(String[] args)**. Click **Finish**.

**Write this**, replacing whatever Eclipse generated inside `main`:

```java
public class Check {
    public static void main(String[] args) {
        System.out.println("Eclipse is running on " + Runtime.version());
    }
}
```

**Run it** with ++ctrl+f11++ or the green triangle. The Console at the bottom should print a line starting with `Eclipse is running on 25`.

That number is the point of the exercise. `Hello, World` would prove only that Eclipse can run something. This proves it is running the Java you installed.

## When it goes wrong

??? failure "Windows protected your PC"

    SmartScreen does not recognise the file yet. Click **More info**, then **Run anyway**.

    Only do this because you checked the hash in step 2. If you skipped that, go back and do it. This dialogue is the last thing standing between you and a file you have not verified.

??? failure "`UnsupportedClassVersionError: ... has been compiled by a more recent version of the Java Runtime`"

    Something compiled your code with one Java version and something else tried to run it with an older one. Almost always the project is set to a newer level than the JRE selected for it.

    Right-click the project, then **Properties**:

    - Under **Java Build Path** then **Libraries**, check which JRE is attached
    - Under **Java Compiler**, check the compliance level

    Both should say 25. If the library entry shows an older JRE, remove it and add the JDK 25 entry you set up in step 5.

??? failure "Eclipse will not start, or says `No Java virtual machine was found`"

    The JDK is missing or not visible to Eclipse. In a fresh PowerShell window:

    ```powershell
    java -version
    ```

    If that fails, the problem is not Eclipse. Go back to [Install Java](java.md).

??? failure "The project uses a different Java version than the workspace"

    These are two separate settings and they can disagree. The workspace default is under **Window** then **Preferences** then **Java** then **Compiler**. The project override is under **Properties** then **Java Compiler**, where a tickbox called **Enable project specific settings** silently outranks the workspace.

    That tickbox is the answer roughly every time a single project misbehaves while everything else is fine.

??? failure "Random compile errors, or files reverting as you edit"

    Your workspace is in a synced folder. Close Eclipse, move the whole workspace folder to `C:\dev\workspace`, and reopen Eclipse pointing at the new location through **File** then **Switch Workspace**.

??? failure "Eclipse is extremely slow"

    Usually antivirus scanning every file the compiler writes. Add your workspace folder and the Eclipse install folder to your antivirus exclusions. On university machines you may need to ask for this.

??? note "Worth turning on while you are here"

    - Line numbers: **Window** then **Preferences** then **General** then **Editors** then **Text Editors** then **Show line numbers**. Every error message you get for the next fourteen weeks refers to a line number.
    - Rename a thing everywhere: ++alt+shift+r++
    - Jump to any class: ++ctrl+shift+t++
    - Find in all files: ++ctrl+h++

## Check yourself

!!! verify "Prove Eclipse and your JDK agree"

    You have already run `Check` inside Eclipse. Now run the same compiled class from outside it.

    In PowerShell:

    ```powershell
    cd C:\dev\workspace\Setup\bin
    java Check
    ```

    You should see the same line you saw in the Eclipse Console.

    That folder, `bin`, is where Eclipse has been quietly putting the `.class` files all along. You have just confirmed that Eclipse is not doing anything magic. It is running `javac` and `java` for you, into a folder you can look inside.

## Think about it

1.  You compared a hash before running the installer. What exactly were you protecting against, given the file came from the official Eclipse site? Now suppose a classmate had sent you the same file over a chat app instead.
2.  Eclipse has its own compiler, separate from `javac`. Your code compiles in Eclipse. What has that proved, and what has it not proved? Now imagine handing that project to someone who will build it from the command line before marking it.
3.  A workspace inside OneDrive breaks builds in ways that look random. Describe what the sync client and the compiler are each doing to the same file. Now suppose it is not your build in that folder but your only copy of a project due tomorrow.
4.  The Java version lives in at least three places: the machine, the workspace, and the project. That seems like an obvious design flaw. Who is it actually for? Think about a team maintaining one application from 2015 and one started last month.

<div class="page-nav" markdown>
[Set up git and GitHub](git-github.md){ .page-nav__next }
</div>
