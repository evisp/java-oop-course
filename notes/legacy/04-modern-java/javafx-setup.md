# JavaFX Setup (Manual and Maven)

> JavaFX setup feels "extra" because, on modern Java (Java 11+), JavaFX is typically not bundled with the JDK, so the project must be told where to find JavaFX libraries at compile-time and at run-time.    

This tutorial starts with the most manual (but very common) approach: **download the JavaFX SDK and attach the JARs**, then it ends by pointing to the cleaner Maven approach.   


## What you're configuring (simple intuition)

There are two separate problems to solve: (1) the code must **compile** (your IDE/compiler must see JavaFX classes), and (2) the app must **run** (the JVM must be able to load JavaFX modules at startup).    

> Common error: you may fix compilation by adding JARs, but still fail at runtime until you add VM options like `--module-path` and `--add-modules`.  

- **Compile-time** = “Can the IDE resolve `import javafx...`?”    
- **Run-time** = “When I press Run, can the JVM locate and load JavaFX?”  


## Manual setup: add JavaFX JARs (compile-time)

The manual approach starts by downloading the JavaFX SDK from OpenJFX and unzipping it somewhere stable.     

The key folder is the SDK’s `lib` directory, because it contains the JavaFX libraries used by IDEs and run configurations.    

### 1. Download and unzip JavaFX SDK
- Download JavaFX SDK for your OS from OpenJFX, then unzip it.     
- Locate the folder: `javafx-sdk-XX/lib` (this is the folder you will reference later).  


### 2. Attach the JAR files to the project

In most IDEs, this is done by adding the JavaFX `lib` JARs as a **library** (so they become available on the project build path).  

> OpenJFX documentation also describes a straightforward “copy the JARs from the SDK lib folder into a project lib folder” approach (shown for VS Code), which is the same idea: make the JARs available to the project. 

- Create a folder in your project like `lib/` (optional but neat). 
- Copy the JARs from `javafx-sdk-XX/lib/` into your project’s `lib/` folder **or** add the SDK `lib` folder directly as a referenced library in your IDE. 
- Ensure the project can now import JavaFX packages without errors.  

> After this step, “red imports” usually disappear because the compiler can see the JavaFX classes.  


## Manual setup: VM options (run-time)

Even if the project compiles, JavaFX may not run until the JVM is told where the JavaFX modules are located and which ones to load.    

> OpenJFX’s “Run HelloWorld using JavaFX SDK” instructions explicitly use `--module-path` and `--add-modules` for this reason.  

### Why “module path” exists (short version)

The Java platform module system was introduced in Java 9, and `--module-path` is the JVM option used to point to directories containing modules. 

### 3. Add VM options to your Run Configuration
Use this pattern (the folder must point to the JavaFX SDK `lib` directory):  

```text
--module-path "PATH_TO_FX/lib" --add-modules javafx.controls
```

- `--module-path ...` tells Java where the JavaFX modules are.  [web:71]  
- `--add-modules javafx.controls` loads the JavaFX Controls module (needed for basic UI controls like Button, Label, TextField).    

If you later use FXML, OpenJFX commonly shows adding `javafx.fxml` too:  

```text
--module-path "PATH_TO_FX/lib" --add-modules javafx.controls,javafx.fxml
```

## Quick "HelloFX" test (confirm setup)

Before starting your real project, confirm setup with a tiny program so you know failures are configuration issues, not your app logic.  

```java
@Override
public void start(Stage stage) {
    stage.setScene(new Scene(new VBox(new Label("Hello JavaFX")), 300, 200));
    stage.show();
}
```

If this runs:

- Your JavaFX libraries are visible to the project (compile-time).    
- Your run configuration is loading JavaFX correctly (run-time).  


## Common problems (and the fast fixes)

### “JavaFX runtime components are missing…”

This message strongly suggests the JVM did not load JavaFX at runtime, which is commonly fixed by adding the correct `--module-path` and `--add-modules` VM options.  [web:72]

Checklist:

- `--module-path` points to the **SDK `lib` folder** (not just the SDK root).  [web:68]  
- `--add-modules` includes at least `javafx.controls` (and `javafx.fxml` if needed).  [web:72]

### Works on one PC, fails on another

Manual SDK paths are machine-specific (different folder locations), which is a major reason classrooms often switch to Maven/Gradle for consistency.   


## The cleaner approach: Maven/Gradle

OpenJFX provides “Getting Started” guidance that includes build-tool based setups, where dependencies are managed by the build system instead of manually linking SDK JARs.    

The main advantage is portability: students don’t depend on a hard-coded local SDK path in their run configuration.  

What changes conceptually:

- Instead of “download SDK and point to lib folder,” the project declares JavaFX as a dependency and the build tool retrieves it.    
- Run configurations become simpler and more consistent across machines.  

> If you are frequently stuck on paths, switching to Maven is usually the fastest way to reduce setup time and focus on JavaFX concepts.  

