import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleLogin extends Application {

    // UI components that need to be accessed from multiple methods
    private TextField usernameField;
    private PasswordField passwordField;
    private Label feedbackLabel;

    @Override
    public void start(Stage primaryStage) {

        // Main vertical layout container
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");

        // Title label
        Label title = new Label("Login");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        // Username label and input field
        Label userLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        // Password label and input field
        Label passLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        // Login button
        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(100);

        // Label used to display feedback messages
        feedbackLabel = new Label("");

        // Handle button click
        loginBtn.setOnAction(event -> handleLogin());

        // Allow pressing Enter to submit the form
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });

        // Add all UI elements to the layout
        root.getChildren().addAll(
            title,
            userLabel, usernameField,
            passLabel, passwordField,
            loginBtn, feedbackLabel
        );

        // Create and display the scene
        Scene scene = new Scene(root, 400, 350);
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Handles login validation and feedback
    private void handleLogin() {

        // Read user input
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check for empty fields
        if (username.isEmpty() || password.isEmpty()) {
            feedbackLabel.setText("Please fill in all fields");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // Simple credential check (for demonstration purposes only)
        if (username.equals("admin") && password.equals("password")) {

            // Successful login message
            feedbackLabel.setText("Login successful!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
        } else {

            // Error message for incorrect credentials
            feedbackLabel.setText("Invalid username or password");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }

    // Application entry point
    public static void main(String[] args) {
        launch(args);
    }
}
