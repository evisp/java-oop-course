package v7_gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardView {

    private final Bank bank;
    private final SceneNavigator navigator;
    private final BorderPane root = new BorderPane();

    // UI fields that get refreshed
    private Label customersValue;
    private Label accountsValue;
    private Label assetsValue;
    private Label feesValue;

    private LineChart<String, Number> txTrendChart;
    private PieChart assetsPie;

    // Bottom panel
    private ListView<String> topCustomersList;

    private Label status;

    public DashboardView(Bank bank, SceneNavigator navigator) {
        this.bank = bank;
        this.navigator = navigator;
        build();
        refresh();
    }

    public Parent getRoot() {
        return root;
    }

    private void build() {
        root.setStyle("-fx-background-color: #F4F6FA;");
        root.setPadding(new Insets(18));

        // ===== Top bar =====
        Label title = new Label("Admin Dashboard");
        title.setStyle("-fx-text-fill: #0F172A; -fx-font-size: 20px; -fx-font-weight: 800;");

        Label bankName = new Label(bank.getBankName());
        bankName.setStyle("-fx-text-fill: #475569; -fx-font-size: 12px;");

        VBox titleBox = new VBox(2, title, bankName);

        Button refreshBtn = primaryButton("Refresh");
        Button saveBtn = secondaryButton("Save");
        Button backupBtn = secondaryButton("Backup");
        Button logoutBtn = ghostButton("Logout");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, titleBox, spacer, refreshBtn, saveBtn, backupBtn, logoutBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);

        VBox top = new VBox(12, topBar, new Separator());
        root.setTop(top);

        // ===== Center content =====
        VBox center = new VBox(16);
        center.setPadding(new Insets(12, 0, 0, 0));

        GridPane kpis = new GridPane();
        kpis.setHgap(12);
        kpis.setVgap(12);

        customersValue = new Label("-");
        accountsValue = new Label("-");
        assetsValue = new Label("-");
        feesValue = new Label("-");

        kpis.add(kpiCard("Customers", customersValue), 0, 0);
        kpis.add(kpiCard("Accounts", accountsValue), 1, 0);
        kpis.add(kpiCard("Total assets", assetsValue), 2, 0);
        kpis.add(kpiCard("Monthly fee revenue", feesValue), 3, 0);

        // Charts row
        HBox chartsRow = new HBox(12);
        chartsRow.setAlignment(Pos.TOP_LEFT);

        txTrendChart = buildTxTrendChart();
        assetsPie = buildAssetsPie();

        VBox leftChartCard = card("Transactions (last 14 days)", txTrendChart);
        VBox rightChartCard = card("Assets by type", assetsPie);

        HBox.setHgrow(leftChartCard, Priority.ALWAYS);
        leftChartCard.setPrefWidth(680);
        rightChartCard.setPrefWidth(340);

        chartsRow.getChildren().addAll(leftChartCard, rightChartCard);

        // ===== Bottom panel: Top customers =====
        topCustomersList = new ListView<>();
        topCustomersList.setMinHeight(220);
        topCustomersList.setPrefHeight(220);
        topCustomersList.setFixedCellSize(28);
        topCustomersList.setPlaceholder(new Label("No customers found"));

        // IMPORTANT: force visible text (prevents “blank rows” look)
        topCustomersList.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };
            cell.setStyle("-fx-text-fill: #0F172A; -fx-font-size: 12px;");
            return cell;
        });

        // Keep it simple: white background so it’s always readable
        topCustomersList.setStyle(
                "-fx-background-color: white;" +
                "-fx-control-inner-background: white;" +
                "-fx-selection-bar: rgba(37,99,235,0.18);"
        );

        VBox topCustomersCard = card("Top customers (by total balance)", topCustomersList);

        status = new Label("");
        status.setStyle("-fx-text-fill: #64748B; -fx-font-size: 12px;");

        center.getChildren().addAll(kpis, chartsRow, topCustomersCard, status);
        root.setCenter(center);

        // ===== Actions =====
        refreshBtn.setOnAction(e -> refresh());

        saveBtn.setOnAction(e -> {
            try {
                BankDataManager.saveBank(bank, "turingbank.ser");
                status.setText("Saved: bank_data/turingbank.ser");
            } catch (Exception ex) {
                status.setText("Save failed: " + ex.getMessage());
            }
        });

        backupBtn.setOnAction(e -> {
            try {
                String backupName = BankDataManager.backupBank(bank);
                status.setText("Backup created: " + backupName);
            } catch (Exception ex) {
                status.setText("Backup failed: " + ex.getMessage());
            }
        });

        logoutBtn.setOnAction(e -> navigator.showLogin());
    }

    private void refresh() {
        Stats s = computeStats();

        customersValue.setText(String.valueOf(s.customerCount));
        accountsValue.setText(String.valueOf(s.accountCount));
        assetsValue.setText(euro(s.totalAssets));
        feesValue.setText(euro(s.monthlyFeeRevenue));

        refreshTrendChart();
        refreshAssetsPie();
        refreshTopCustomers();

        status.setText("Updated: " + java.time.LocalDateTime.now()
                + " | Top customers shown: " + topCustomersList.getItems().size());
    }

    // ===== Top customers =====
    private double totalBalanceFor(Customer c) {
        double total = 0.0;
        for (Account a : (List<Account>) c.getAccounts()) {
            total += a.getBalance();
        }
        return total;
    }

    private void refreshTopCustomers() {
        List<Customer> customers = new ArrayList<>((List<Customer>) bank.getCustomers());

        customers.sort(Comparator.comparingDouble(this::totalBalanceFor).reversed());

        List<String> rows = new ArrayList<>();
        int limit = Math.min(8, customers.size());

        for (int i = 0; i < limit; i++) {
            Customer c = customers.get(i);
            double bal = totalBalanceFor(c);
            int accounts = c.getAccountCount();

            rows.add((i + 1) + ". " + c.getName()
                    + " | Balance: " + euro(bal)
                    + " | Accounts: " + accounts);
        }

        topCustomersList.setItems(FXCollections.observableArrayList(rows));
    }

    // ===== Stats / analytics =====
    private Stats computeStats() {
        int customerCount = bank.getCustomerCount();

        int accountCount = 0;
        double totalAssets = 0.0;
        double monthlyFeeRevenue = 0.0;
        double savingsAssets = 0.0;
        double checkingAssets = 0.0;

        int frozenAccounts = 0;
        int overdraftAccounts = 0;

        for (Customer c : (List<Customer>) bank.getCustomers()) {
            for (Account a : (List<Account>) c.getAccounts()) {
                accountCount++;
                totalAssets += a.getBalance();
                monthlyFeeRevenue += a.calculateMonthlyFee();

                if (a.isFrozen()) frozenAccounts++;

                if (a instanceof SavingsAccount) {
                    savingsAssets += a.getBalance();
                } else if (a instanceof CheckingAccount) {
                    checkingAssets += a.getBalance();
                    if (a.getBalance() < 0) overdraftAccounts++;
                }
            }
        }

        return new Stats(customerCount, accountCount, totalAssets, monthlyFeeRevenue,
                savingsAssets, checkingAssets, frozenAccounts, overdraftAccounts);
    }

    private Map<LocalDate, Integer> computeTxCountsLastDays(int days) {
        Map<LocalDate, Integer> map = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();

        for (int i = days - 1; i >= 0; i--) {
            map.put(today.minusDays(i), 0);
        }

        for (Customer c : (List<Customer>) bank.getCustomers()) {
            for (Account a : (List<Account>) c.getAccounts()) {
                for (Transaction t : (List<Transaction>) a.getAuditTrail()) {
                    LocalDate d = t.getTimestamp().toLocalDate();
                    if (map.containsKey(d)) {
                        map.put(d, map.get(d) + 1);
                    }
                }
            }
        }

        return map;
    }

    // ===== Charts =====
    private LineChart<String, Number> buildTxTrendChart() {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        y.setForceZeroInRange(true);

        LineChart<String, Number> chart = new LineChart<>(x, y);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setCreateSymbols(true);
        chart.setVerticalGridLinesVisible(false);
        chart.setHorizontalGridLinesVisible(true);
        chart.setAlternativeRowFillVisible(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setMinHeight(260);
        chart.setStyle("-fx-background-color: transparent;");
        return chart;
    }

    private void refreshTrendChart() {
        Map<LocalDate, Integer> data = computeTxCountsLastDays(14);
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<LocalDate, Integer> e : data.entrySet()) {
            String label = e.getKey().getMonthValue() + "/" + e.getKey().getDayOfMonth();
            series.getData().add(new XYChart.Data<>(label, e.getValue()));
        }

        txTrendChart.getData().setAll(series);
    }

    private PieChart buildAssetsPie() {
        PieChart pie = new PieChart();
        pie.setLegendVisible(true);
        pie.setLabelsVisible(true);
        pie.setClockwise(true);
        pie.setMinHeight(260);
        pie.setStyle("-fx-background-color: transparent;");
        return pie;
    }

    private void refreshAssetsPie() {
        Stats s = computeStats();
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Savings", s.savingsAssets),
                new PieChart.Data("Checking", s.checkingAssets)
        );
        assetsPie.setData(pieData);
    }

    // ===== UI helpers =====
    private VBox kpiCard(String label, Label value) {
        Label l = new Label(label);
        l.setStyle("-fx-text-fill: #64748B; -fx-font-size: 12px; -fx-font-weight: 600;");
        value.setStyle("-fx-text-fill: #0F172A; -fx-font-size: 22px; -fx-font-weight: 800;");

        VBox box = new VBox(6, l, value);
        box.setPadding(new Insets(14));
        box.setMinHeight(84);
        box.setAlignment(Pos.TOP_LEFT);
        styleCard(box);
        return box;
    }

    private VBox card(String title, Parent content) {
        Label t = new Label(title);
        t.setStyle("-fx-text-fill: #0F172A; -fx-font-size: 13px; -fx-font-weight: 800;");

        VBox box = new VBox(12, t, content);
        box.setPadding(new Insets(14));
        styleCard(box);
        return box;
    }

    private void styleCard(Region region) {
        region.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-radius: 14;" +
                "-fx-border-color: rgba(15,23,42,0.08);" +
                "-fx-border-width: 1;"
        );

        DropShadow ds = new DropShadow();
        ds.setRadius(18);
        ds.setOffsetY(10);
        ds.setColor(Color.color(0, 0, 0, 0.10));
        region.setEffect(ds);
    }

    private Button primaryButton(String text) {
        Button b = new Button(text);
        b.setStyle(
                "-fx-background-color: #2563EB;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: 800;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 8 12 8 12;"
        );
        return b;
    }

    private Button secondaryButton(String text) {
        Button b = new Button(text);
        b.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: #0F172A;" +
                "-fx-font-weight: 800;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-border-color: rgba(15,23,42,0.12);" +
                "-fx-border-width: 1;" +
                "-fx-padding: 8 12 8 12;"
        );
        return b;
    }

    private Button ghostButton(String text) {
        Button b = new Button(text);
        b.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #2563EB;" +
                "-fx-font-weight: 800;" +
                "-fx-padding: 8 10 8 10;"
        );
        return b;
    }

    private static String euro(double v) {
        return "€" + String.format("%.2f", v);
    }

    private static class Stats {
        final int customerCount;
        final int accountCount;
        final double totalAssets;
        final double monthlyFeeRevenue;
        final double savingsAssets;
        final double checkingAssets;
        final int frozenAccounts;
        final int overdraftAccounts;

        Stats(int customerCount, int accountCount, double totalAssets, double monthlyFeeRevenue,
              double savingsAssets, double checkingAssets, int frozenAccounts, int overdraftAccounts) {
            this.customerCount = customerCount;
            this.accountCount = accountCount;
            this.totalAssets = totalAssets;
            this.monthlyFeeRevenue = monthlyFeeRevenue;
            this.savingsAssets = savingsAssets;
            this.checkingAssets = checkingAssets;
            this.frozenAccounts = frozenAccounts;
            this.overdraftAccounts = overdraftAccounts;
        }
    }
}
