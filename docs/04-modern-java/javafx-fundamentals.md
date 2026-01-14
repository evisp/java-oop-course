# JavaFX Fundamentals (Part 1) - Concepts-First Tutorial


JavaFX is easiest to learn when the focus is not “memorize classes”, but “understand the model.”  
Once the model is clear, the code feels repetitive in a good way: create nodes, arrange them with a layout pane, then connect events to logic.


![Login Form](https://i.imgur.com/dLnqrfK.png)

## 1) The JavaFX mental model

> In JavaFX, you build a UI by creating a **tree** of elements (a scene graph), then showing it in a window. 

Think in three layers:

- **Stage**: the top-level window provided by the operating system. 
- **Scene**: the content container placed inside the Stage. 
- **Nodes**: the actual UI building blocks (controls, shapes, layout panes, etc.) placed into the Scene. 

The scene graph is a hierarchy:

- Every visible element is a `Node`, and branch nodes are typically `Parent` types that can contain children.
- The Scene holds the scene graph, and when you create a Scene you provide a root node (the top of the tree). 

**Illustration snippet (Stage → Scene → root node):**
```java
VBox root = new VBox();
Scene scene = new Scene(root, 400, 350);

stage.setScene(scene);
stage.show();
```

> Practical mindset: JavaFX is not "draw pixels here"; it is "describe structure, then let JavaFX lay it out and render it."

## 2) Application lifecycle (what runs, and when)

JavaFX apps start by extending `Application`. 
When you launch the app, the JavaFX runtime calls lifecycle methods in order: `init()`, then `start(Stage)`, and later `stop()`. 

What this means:

- `start(Stage)` is where you build and show the UI. 
- `launch(args)` boots the JavaFX runtime and triggers that lifecycle. 

**Illustration snippet (entry point):**
```java
public static void main(String[] args) {
    launch(args);
}
```

> A simple rule: if it belongs to the UI, it usually starts inside `start(...)`; if it belongs to cleanup, it usually goes to `stop()`.


## 3) Controls: "interactive nodes" and their properties

Controls (like text fields and buttons) are nodes that users can interact with. 
Most JavaFX coding is configuring controls through properties: text, prompt text, width, alignment, disabled state, etc.

For example, in a login form, the "core" controls are:
- `Label` for readable text ("Username", "Password", feedback).  
- `TextField` for input.  
- `PasswordField` for secret input.  
- `Button` for submitting.

**Illustration snippet (create controls, set prompts):**
```java
TextField usernameField = new TextField();
usernameField.setPromptText("Enter your username");

PasswordField passwordField = new PasswordField();
passwordField.setPromptText("Enter your password");

Button loginBtn = new Button("Login");
Label feedbackLabel = new Label();
```

> Good UI habit: small details (prompt text, spacing, readable labels) often matter more to users than advanced features.


## 4) Events: how the UI "comes alive"

JavaFX is event-driven: user actions trigger events, and event handlers react. 
A login form is a perfect example because it uses a small number of clear events.

Common beginner events:
- Button click → attempt login (an action event). 
- Press Enter in the password field → attempt login (a key event). 

**Illustration snippet (two events, one shared action):**
```java
loginBtn.setOnAction(e -> handleLogin());

passwordField.setOnKeyPressed(e -> {
    if (e.getCode() == KeyCode.ENTER) {
        handleLogin();
    }
});
```

> Best practice: multiple UI events should call the same "business action" method, so logic is not duplicated.


## 5) Layout panes: clean structure instead of manual positioning

Layout panes are containers (nodes) designed to arrange child nodes automatically.   
They exist so you do not hard-code x/y coordinates, and so the UI behaves better when resized.

### Start simple: VBox / HBox
`VBox` arranges children in a vertical column, and `HBox` arranges children in a horizontal row.  
They are ideal for quick, readable UIs and for teaching structure before complexity. 

![VBox HBox](https://i.imgur.com/Cga8H0x.png)

**Illustration snippet (VBox with spacing and padding):**
```java
VBox root = new VBox(10);
root.setStyle("-fx-padding: 20;");
```

### GridPane: forms that "line up"
`GridPane` places nodes in a grid of rows and columns.  
It’s a strong choice when you want labels aligned with their fields. 

![Grid Pane](https://i.imgur.com/zjozr12.png)

**Illustration snippet (label column + field column):**
```java
grid.add(new Label("Username:"), 0, 0);
grid.add(usernameField, 1, 0);
```

### BorderPane: the “professional app shell”
`BorderPane` lays out content into top, bottom, left, right, and center regions.   
It's common when an app grows beyond a single form (top bar, navigation, main content, status line). 

![Border Pane](https://i.imgur.com/FNCJF02.png)

**Illustration snippet (place form in the center):**
```java
BorderPane shell = new BorderPane();
shell.setCenter(formRoot);
```

### FlowPane and StackPane: when you need special behavior
`FlowPane` lays out items in a flow and wraps at the boundary (useful for “chips”, tags, dynamic button sets). 
`StackPane` places nodes in a back-to-front stack (useful for overlays, badges, loading layers). [web:23]

![Flow Pane and Stack Pane](https://i.imgur.com/AwZvte0.png)
> Choosing a layout pane is not about "which is best", but about "what behavior do I want when the window changes?"

> You can’t make life perfectly put together.  
> But you can at least fix the padding.
