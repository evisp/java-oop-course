# Eclipse IDE Setup on Windows

This tutorial shows how to install and set up Eclipse for Java development on Windows. It also explains how to check or set the JDK inside Eclipse and run a quick test project. Estimated time: 20–30 minutes.

## Why this matters
- **Problem**: Java work is slower without a stable IDE and a correct JDK setup. Installing tools wrong leads to errors and lost time.
- **Practical benefits**: Eclipse gives project templates, code completion, refactoring, and a good debugger, so learning and building Java apps is faster.
- **Professional context**: Eclipse is widely used in classrooms and industry, and supports current Java releases. Knowing it is useful for internships and jobs.

## Prerequisites and goals
- Required: Windows 10 or 11 and a few GB of disk space.
- Nice to have: A recent JDK (Java 21+). If not installed, Eclipse can still run if bundled with a JRE, **but a proper JDK is recommended.**
- You will learn: Download and install Eclipse, set the JDK in Eclipse, and create and run a simple Java project.

## High-level plan
Download Eclipse, install it, confirm Java, set the JDK if needed, then build and run a `Hello, World` project to verify everything.

## Step-by-step

### Step 1 — Download Eclipse
- Go to the [official Eclipse downloads page](https://www.eclipse.org/downloads/) and get the Eclipse Installer for Windows (x86_64). Save the `.exe` file.

### Step 2 — Run the installer
- Open the downloaded `.exe`. Choose `Eclipse IDE for Java Developers.`
- Keep the default install folder unless a different location is required. Accept the license and click `Install`
- When finished, click `Launch` to start Eclipse.

### Step 3 — Choose a workspace
- On first launch, pick a workspace folder where projects will be saved (for example, `C:\Users\YourName\EclipseWorkspace`). Check `Use this as default` to skip this prompt next time.

### Step 4 — Check or set the JDK in Eclipse
- Check the detected Java: `Window → Preferences → Java → Installed JREs`. If a JDK is listed and checked, you are set.
- If empty or only a JRE is listed:
    - Install a JDK (for example, Java 21 or newer) using [Oracle](https://www.oracle.com/java/technologies/downloads/) or another trusted vendor.
    - In Eclipse: `Window → Preferences → Java → Installed JREs → Add → Standard VM → Next`. Point JRE home to the JDK folder (e.g., `C:\Program Files\Java\jdk-21`). Select it as default.
- Why this matters: A JDK includes the compiler and tools needed for Java development; a JRE alone is not enough.

### Step 5 — Create and run a test project
- Create project: `File → New → Java Project → Name: HelloWorld → Finish`.
- Create class: `Right-click src → New → Class → Name: Main → Finish.`
- Paste and run:
```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Eclipse!");
    }
}
```
- Click the `Run` button (green triangle) or press `Ctrl+F11`. The Console should show: `Hello, Eclipse!`

## Common issues and quick fixes
- Eclipse won’t start: Make sure you downloaded the 64‑bit installer for 64‑bit Windows. Re-download if the file was corrupted.
- `No Java virtual machine` message: Install a JDK and set it in Installed JREs as shown above.
- Wrong Java version used by a project: `Right‑click project → Properties → Java Build Path → Libraries`; or Java Compiler to select the correct level.

## Next steps
- Learn shortcuts: `Rename (Alt+Shift+R)`, `Open Type (Ctrl+Shift+T)`, `Search (Ctrl+H)`.
- Turn on line numbers: `Window → Preferences → General → Editors → Text Editors → Show line numbers`.
- Connect Git: `Window → Perspective → Open Perspective → Git`; then clone the course repository.

With Eclipse installed and a test project running, the environment is ready for the course work. Keep the setup simple, and move on to coding.
