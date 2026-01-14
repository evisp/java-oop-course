package v7_gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LoginView {

    private static final String HERO_IMAGE_URL =
            "https://picsum.photos/900/900?blur=1"; // placeholder

    private final Bank bank;
    private final SceneNavigator navigator;

    private final StackPane root = new StackPane();

    public LoginView(Bank bank, SceneNavigator navigator) {
        this.bank = bank;
        this.navigator = navigator;
        build();
    }

    public Parent getRoot() {
        return root;
    }

    private void build() {
        // App background
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #0F172A, #111827);"
        );
        root.setPadding(new Insets(30));

        // Card container
        BorderPane card = new BorderPane();
        card.setMaxWidth(980);
        card.setPrefHeight(460);
        card.setStyle(
                "-fx-background-color: #0B1220;" +
                "-fx-background-radius: 18;" +
                "-fx-border-radius: 18;" +
                "-fx-border-color: rgba(255,255,255,0.08);" +
                "-fx-border-width: 1;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setRadius(28);
        shadow.setOffsetY(14);
        shadow.setColor(Color.color(0, 0, 0, 0.55));
        card.setEffect(shadow);

        // LEFT: hero image + branding overlay
        StackPane left = new StackPane();
        left.setPrefWidth(420);
        left.setMinWidth(360);

        ImageView hero = new ImageView(new Image(HERO_IMAGE_URL, true));
        hero.setPreserveRatio(false);
        hero.setFitWidth(420);
        hero.setFitHeight(460);

        Region overlay = new Region();
        overlay.setStyle(
                "-fx-background-color: linear-gradient(to bottom, rgba(0,0,0,0.15), rgba(0,0,0,0.70));"
        );

        VBox brandBox = new VBox(8);
        brandBox.setPadding(new Insets(26));
        brandBox.setAlignment(Pos.BOTTOM_LEFT);

        Label brand = new Label("Bank Management");
        brand.setStyle("-fx-text-fill: white; -fx-font-size: 26px; -fx-font-weight: 800;");

        Label tagline = new Label("Secure. Simple. Educational OOP demo.");
        tagline.setStyle("-fx-text-fill: rgba(255,255,255,0.80); -fx-font-size: 13px;");

        Label bankName = new Label("Loaded bank: " + bank.getBankName());
        bankName.setStyle("-fx-text-fill: rgba(255,255,255,0.72); -fx-font-size: 12px;");

        brandBox.getChildren().addAll(brand, tagline, bankName);

        left.getChildren().addAll(hero, overlay, brandBox);

        // Clip left side corners visually by matching the card radius
        left.setStyle("-fx-background-radius: 18 0 0 18; -fx-border-radius: 18 0 0 18;");

        // RIGHT: form
        VBox right = new VBox(14);
        right.setPadding(new Insets(32));
        right.setAlignment(Pos.TOP_LEFT);
        right.setStyle("-fx-background-color: #0B1220; -fx-background-radius: 0 18 18 0;");

        Label title = new Label("Admin Login");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: 800;");

        Label subtitle = new Label("Use your admin credentials to continue.");
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.70); -fx-font-size: 13px;");

        VBox header = new VBox(6, title, subtitle);

        TextField username = new TextField();
        username.setPromptText("Username");
        styleInput(username);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        styleInput(password);

        CheckBox remember = new CheckBox("Remember me");
        remember.setStyle("-fx-text-fill: rgba(255,255,255,0.78);");

        Hyperlink forgot = new Hyperlink("Forgot password?");
        forgot.setStyle("-fx-text-fill: #93C5FD;");
        forgot.setOnAction(e -> {
            // keep simple for now; could show a dialog later
        });

        HBox options = new HBox(10, remember, spacer(), forgot);
        options.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(options.getChildren().get(1), Priority.ALWAYS);

        Label status = new Label("");
        status.setStyle("-fx-text-fill: #FCA5A5; -fx-font-size: 12px;");

        Button loginBtn = new Button("Sign in");
        loginBtn.setDefaultButton(true);
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle(
                "-fx-background-color: #2563EB;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 12;" +
                "-fx-padding: 10 14 10 14;"
        );

        Button exitBtn = new Button("Exit");
        exitBtn.setMaxWidth(Double.MAX_VALUE);
        exitBtn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.08);" +
                "-fx-text-fill: rgba(255,255,255,0.90);" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 12;" +
                "-fx-padding: 10 14 10 14;"
        );

        loginBtn.setOnAction(e -> {
            String u = safeTrim(username.getText());
            String p = password.getText() == null ? "" : password.getText();

            if ("admin".equals(u) && "admin".equals(p)) {
                status.setText("");
                navigator.showDashboard();
            } else {
                status.setText("Invalid credentials. Try admin / admin.");
            }
        });

        exitBtn.setOnAction(e -> System.exit(0));

        VBox form = new VBox(12,
                header,
                new Separator(),
                username,
                password,
                options,
                status,
                loginBtn,
                exitBtn
        );
        form.setFillWidth(true);

        right.getChildren().add(form);

        card.setLeft(left);
        card.setCenter(right);

        root.getChildren().add(card);
    }

    private static void styleInput(TextField tf) {
        tf.setStyle(
                "-fx-background-color: rgba(255,255,255,0.06);" +
                "-fx-text-fill: rgba(255,255,255,0.92);" +
                "-fx-prompt-text-fill: rgba(255,255,255,0.45);" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: rgba(255,255,255,0.12);" +
                "-fx-border-width: 1;" +
                "-fx-padding: 10 12 10 12;"
        );
    }

    private static Region spacer() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}
