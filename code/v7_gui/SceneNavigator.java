package v7_gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneNavigator {

    private final Stage stage;
    private final Bank bank;

    public SceneNavigator(Stage stage, Bank bank) {
        this.stage = stage;
        this.bank = bank;
    }

    public void showLogin() {
        LoginView view = new LoginView(bank, this);
        Scene scene = new Scene(view.getRoot(), 920, 520);
        stage.setScene(scene);
    }


    public void showDashboard() {
        DashboardView view = new DashboardView(bank, this);
        Scene scene = new Scene(view.getRoot(), 700, 420);
        stage.setScene(scene);
    }
}
