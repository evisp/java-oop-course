package v7_gui;

public final class BankBootstrap {

    private BankBootstrap() {}

    private static final String MAIN_SAVE = "turingbank.ser";

    public static Bank loadOrCreateBank() {
        try {
            return BankDataManager.loadBank(MAIN_SAVE);
        } catch (Exception e) {
            try {
                return DemoDataGenerator.generateAndSave(
                        MAIN_SAVE,
                        80,
                        2,
                        80,
                        180,
                        120
                );
            } catch (Exception ex) {
                return new Bank("Empty Bank");
            }
        }
    }
}
