package v4_inheritance_polymorphism;

/**
 * Represents a checking account with overdraft protection and monthly fees.
 * This class extends Account and demonstrates inheritance and method overriding.
 * 
 * Key features:
 * - Allows overdraft up to a specified limit
 * - Charges monthly maintenance fee
 * - Overrides withdraw() to allow negative balance within overdraft limit
 * 
 * @author Evis Plaku
 * @version 4.0
 */
public class CheckingAccount extends Account {
    
    /** Overdraft limit (amount allowed below zero) */
    private double overdraftLimit;
    
    /** Monthly maintenance fee */
    private double monthlyFee;
    
    /** Default monthly fee */
    private static final double DEFAULT_MONTHLY_FEE = 5.0;
    
    /**
     * Constructs a new CheckingAccount with specified balance and overdraft limit.
     * Uses default monthly fee.
     * 
     * @param initialBalance the starting balance (must be non-negative)
     * @param overdraftLimit the overdraft limit allowed (amount that can go negative)
     * @throws IllegalArgumentException if initial balance is negative or overdraft limit is negative
     */
    public CheckingAccount(double initialBalance, double overdraftLimit) {
        super(initialBalance);  // Call parent constructor
        
        if (overdraftLimit < 0) {
        	System.out.println("Overdraft limit cannot be negative. Provided: " + overdraftLimit);
        }
        
        this.overdraftLimit = overdraftLimit;
        this.monthlyFee = DEFAULT_MONTHLY_FEE;
        
        System.out.println("Checking Account #" + getAccountId() + " created with €" + 
                         overdraftLimit + " overdraft protection");
    }
    
    /**
     * Constructs a new CheckingAccount with specified balance, overdraft limit, and monthly fee.
     * 
     * @param initialBalance the starting balance (must be non-negative)
     * @param overdraftLimit the overdraft limit allowed
     * @param monthlyFee the monthly maintenance fee
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public CheckingAccount(double initialBalance, double overdraftLimit, double monthlyFee) {
        super(initialBalance);  // Call parent constructor
        
        if (overdraftLimit < 0) {
        	System.out.println("Overdraft limit cannot be negative");
        }
        if (monthlyFee < 0) {
        	System.out.println("Monthly fee cannot be negative");
        }
        
        this.overdraftLimit = overdraftLimit;
        this.monthlyFee = monthlyFee;
        
        System.out.println("Checking Account #" + getAccountId() + " created with €" + 
                         overdraftLimit + " overdraft and €" + monthlyFee + " monthly fee");
    }
    
    /**
     * Returns the overdraft limit.
     * 
     * @return the overdraft limit
     */
    public double getOverdraftLimit() {
        return overdraftLimit;
    }
    
    /**
     * Sets the overdraft limit.
     * 
     * @param overdraftLimit the new overdraft limit
     * @throws IllegalArgumentException if overdraft limit is negative
     */
    public void setOverdraftLimit(double overdraftLimit) {
        if (overdraftLimit < 0) {
            System.out.println("Overdraft limit cannot be negative");
        }
        this.overdraftLimit = overdraftLimit;
    }
    
    /**
     * Returns the monthly fee.
     * 
     * @return the monthly fee
     */
    public double getMonthlyFee() {
        return monthlyFee;
    }
    
    /**
     * Sets the monthly fee.
     * 
     * @param monthlyFee the new monthly fee
     * @throws IllegalArgumentException if monthly fee is negative
     */
    public void setMonthlyFee(double monthlyFee) {
        if (monthlyFee < 0) {
        	System.out.println("Monthly fee cannot be negative");
        }
        this.monthlyFee = monthlyFee;
    }
    
    /**
     * Returns the available balance including overdraft.
     * This shows what can actually be withdrawn.
     * 
     * @return balance plus overdraft limit
     */
    public double getAvailableBalance() {
        return balance + overdraftLimit;
    }
    
    /**
     * Overrides withdraw to allow overdraft within limit.
     * This is the key polymorphic behavior - CheckingAccount withdraws differently than Account.
     * 
     * @param amount the amount to withdraw
     * @return true if withdrawal was successful, false otherwise
     */
    @Override
    public boolean withdraw(double amount) {
        if (isFrozen()) {
            System.out.println("Cannot withdraw. Account #" + getAccountId() + " is frozen.");
            return false;
        }
        
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return false;
        }
        
        // Checking account specific check: can go negative up to overdraft limit
        double availableBalance = balance + overdraftLimit;
        if (amount > availableBalance) {
            System.out.println("Cannot withdraw €" + amount + ". Exceeds available balance of €" + 
                             String.format("%.2f", availableBalance) + 
                             " (Balance: €" + String.format("%.2f", balance) + 
                             " + Overdraft: €" + overdraftLimit + ")");
            return false;
        }
        
        // Perform withdrawal (may result in negative balance)
        balance -= amount;
        
        // Record transaction
        Transaction transaction = new Transaction(
            Transaction.TransactionType.WITHDRAW,
            amount,
            getAccountId(),
            balance,
            "Withdrawal" + (balance < 0 ? " (using overdraft)" : "")
        );
        transactionHistory.add(transaction);
        
        System.out.println("Withdrawn: €" + amount + " from Checking Account #" + getAccountId());
        if (balance < 0) {
            System.out.println("  (Account now in overdraft. Balance: €" + String.format("%.2f", balance) + ")");
        }
        
        return true;
    }
    
    /**
     * Applies the monthly maintenance fee to the account.
     * This is a CheckingAccount-specific method not available in base Account.
     * 
     * @return true if fee was applied successfully
     */
    public boolean applyMonthlyFee() {
        if (monthlyFee <= 0) {
            return true; // No fee to apply
        }
        
        balance -= monthlyFee;
        
        // Record as a withdrawal transaction
        Transaction transaction = new Transaction(
            Transaction.TransactionType.WITHDRAW,
            monthlyFee,
            getAccountId(),
            balance,
            "Monthly maintenance fee"
        );
        transactionHistory.add(transaction);
        
        System.out.println("Monthly fee of €" + monthlyFee + " applied to Checking Account #" + getAccountId());
        
        if (balance < -overdraftLimit) {
            System.out.println("  WARNING: Account has exceeded overdraft limit!");
        }
        
        return true;
    }
    
    /**
     * Checks if the account is currently in overdraft.
     * 
     * @return true if balance is negative
     */
    public boolean isInOverdraft() {
        return balance < 0;
    }
    
    /**
     * Returns the account type identifier.
     * 
     * @return "Checking Account"
     */
    @Override
    public String getAccountType() {
        return "Checking Account";
    }
    
    /**
     * Returns a string representation including checking-specific details.
     * 
     * @return formatted string with account details
     */
    @Override
    public String toString() {
        String overdraftInfo = isInOverdraft() ? " [IN OVERDRAFT]" : "";
        return super.toString() + 
               " [Overdraft: €" + overdraftLimit + ", Fee: €" + monthlyFee + "]" + overdraftInfo;
    }
}
