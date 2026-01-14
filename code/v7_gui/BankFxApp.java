package v7_gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class BankFxApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bank System (JavaFX)");

        Bank bank = BankBootstrap.loadOrCreateBank();

        SceneNavigator navigator = new SceneNavigator(primaryStage, bank);
        navigator.showLogin();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
