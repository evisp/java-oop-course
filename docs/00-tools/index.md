# Set up

<p class="meta"><span class="badge">About an hour</span><span class="badge">Week 1</span><span class="badge badge--accent">Start here</span></p>

Before you write a line of Java you need three things on your machine: the Java toolkit itself, somewhere to write code, and somewhere to keep it. This section installs all three and proves each one works.

Do them in this order. Most setup problems people bring to class are caused by installing the editor before Java, then wondering why nothing runs.

<svg viewBox="0 0 520 200" role="img" xmlns="http://www.w3.org/2000/svg"><title>Three tools, one project</title><desc>Three boxes along the top, labelled Java, Eclipse and Git, each joined by a line to a single box below them labelled your project. The three tools are separate installations that together produce one piece of work.</desc><g stroke="var(--c-border)" stroke-width="1.5" fill="none"><path d="M90 74 L90 104 Q90 116 102 116 L248 116"/><path d="M260 74 L260 116"/><path d="M430 74 L430 104 Q430 116 418 116 L272 116"/></g><g fill="var(--c-surface)" stroke="var(--c-border)" stroke-width="2"><rect x="20" y="22" width="140" height="52" rx="8"/><rect x="190" y="22" width="140" height="52" rx="8"/><rect x="360" y="22" width="140" height="52" rx="8"/></g><g font-family="Inter, sans-serif" font-size="17" font-weight="600" fill="var(--c-ink)" text-anchor="middle"><text x="90" y="46">Java</text><text x="260" y="46">Eclipse</text><text x="430" y="46">Git</text></g><g font-family="Inter, sans-serif" font-size="13" fill="var(--c-muted)" text-anchor="middle"><text x="90" y="64">compiles and runs</text><text x="260" y="64">where you write</text><text x="430" y="64">where it is kept</text></g><rect x="150" y="128" width="220" height="54" rx="8" fill="var(--c-raised)" stroke="var(--c-accent)" stroke-width="2"/><text x="260" y="152" font-family="Inter, sans-serif" font-size="17" font-weight="600" fill="var(--c-ink)" text-anchor="middle">Your project</text><text x="260" y="170" font-family="Inter, sans-serif" font-size="13" fill="var(--c-muted)" text-anchor="middle">one repository, all semester</text></svg>

## The three pieces

<div class="grid cards" markdown>

-   **1. Java**

    ---

    The compiler, the runtime and the tools. This is the part that actually runs your code, and the part that breaks if you get it wrong.

    [:octicons-arrow-right-24: Install Java](java.md)

-   **2. Eclipse**

    ---

    Where you write, run and debug. It is a convenience layer over the tools you installed in step 1.

    [:octicons-arrow-right-24: Install Eclipse](eclipse-setup.md)

-   **3. Git and GitHub**

    ---

    Your work lives in a repository from week one, not in a folder on your laptop.

    [:octicons-arrow-right-24: Set up git](git-github.md)

</div>

## About the editor

The course uses Eclipse. Not because it is better than IntelliJ IDEA or VS Code, but because when everyone has the same screens, a problem takes two minutes to solve instead of twenty.

If you already work happily in another editor, use it. Every project in this course is plain Java and later plain Maven, so nothing here depends on Eclipse. You still need the exact Java version from step 1, and you still need git.

!!! mistake "Do not keep your code in OneDrive or Dropbox"

    Sync clients rewrite files while the compiler is reading them. The result is not a clear error, it is intermittent nonsense: builds that fail and then succeed, files that revert, a git repository that corrupts itself. Put your work somewhere plain, like `C:\dev\`, and let git be your backup.

## Where each piece goes

| Piece | Lands in | You will use it |
|---|---|---|
| Temurin JDK 25 | `C:\Program Files\Eclipse Adoptium\` | Every week |
| Eclipse | `C:\Users\<you>\eclipse\` | Every week |
| Your workspace | `C:\dev\` | Every week |
| Git | `C:\Program Files\Git\` | Every week |

## Later in the course

Two more tools arrive when you need them, and their setup pages will appear here.

**Maven** in week 10, when you add JUnit and need a way to pull in a library.

**JavaFX and Scene Builder** in week 13, when your program gets a window.

## Check yourself

!!! verify "You are set up when all three of these work"

    Open PowerShell and run each one. You should get a version number, not an error.

    ```powershell
    java -version
    javac -version
    git --version
    ```

    If any of them says the command is not recognised, that piece is not installed or not on your PATH. Go back to its page.

## Think about it

1.  You are installing three separate tools rather than one program that does everything. What does that separation buy you? Now suppose next year you want to try a different editor, or a different Java version for one project only.
2.  The course asks everyone to install the same versions. What is lost by that, and who is it really for? Imagine instead that your code runs on your machine but not on the machine of the person marking it.
3.  A tool you install today will have a newer version before the semester ends. What should make you update, and what should make you leave it alone until the course is over?

<div class="page-nav" markdown>
[Install Java](java.md){ .page-nav__next }
</div>
