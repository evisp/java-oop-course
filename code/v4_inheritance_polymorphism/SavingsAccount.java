package v4_inheritance_polymorphism;

/**
 * Represents a savings account with interest earning capability and minimum balance requirement.
 * This class extends Account and demonstrates inheritance and method overriding.
 * 
 * Key features:
 * - Earns interest on the balance
 * - Enforces minimum balance requirement
 * - Overrides withdraw() to prevent going below minimum
 * 
 * @author Evis Plaku
 * @version 4.0
 */
public class SavingsAccount extends Account {
    
    /** Annual interest rate (e.g., 0.03 for 3%) */
    private double interestRate;
    
    /** Minimum balance that must be maintained */
    private double minimumBalance;
    
    /** Default minimum balance for savings accounts */
    private static final double DEFAULT_MINIMUM_BALANCE = 100.0;
    
    /**
     * Constructs a new SavingsAccount with specified balance and interest rate.
     * Uses default minimum balance.
     * 
     * @param initialBalance the starting balance (must be non-negative)
     * @param interestRate the annual interest rate (e.g., 0.03 for 3%)
     * @throws IllegalArgumentException if initial balance is negative or interest rate is negative
     */
    public SavingsAccount(double initialBalance, double interestRate) {
        super(initialBalance);  // Call parent constructor
        
        if (interestRate < 0) {
            System.out.println("Interest rate cannot be negative. Provided: " + interestRate);
        }
        
        this.interestRate = interestRate;
        this.minimumBalance = DEFAULT_MINIMUM_BALANCE;
        
        System.out.println("Savings Account #" + getAccountId() + " created with " + 
                         (interestRate * 100) + "% interest rate");
    }
    
    /**
     * Constructs a new SavingsAccount with specified balance, interest rate, and minimum balance.
     * 
     * @param initialBalance the starting balance (must be non-negative)
     * @param interestRate the annual interest rate (e.g., 0.03 for 3%)
     * @param minimumBalance the minimum balance requirement
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public SavingsAccount(double initialBalance, double interestRate, double minimumBalance) {
        super(initialBalance);  // Call parent constructor
        
        if (interestRate < 0) {
        	System.out.println("Interest rate cannot be negative");
        }
        if (minimumBalance < 0) {
        	System.out.println("Minimum balance cannot be negative");
        }
        
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
        
        System.out.println("Savings Account #" + getAccountId() + " created with " + 
                         (interestRate * 100) + "% interest rate and €" + 
                         minimumBalance + " minimum balance");
    }
    
    /**
     * Returns the interest rate.
     * 
     * @return the annual interest rate
     */
    public double getInterestRate() {
        return interestRate;
    }
    
    /**
     * Sets the interest rate.
     * 
     * @param interestRate the new interest rate
     * @throws IllegalArgumentException if interest rate is negative
     */
    public void setInterestRate(double interestRate) {
        if (interestRate < 0) {
        	System.out.println("Interest rate cannot be negative");
        }
        this.interestRate = interestRate;
    }
    
    /**
     * Returns the minimum balance requirement.
     * 
     * @return the minimum balance
     */
    public double getMinimumBalance() {
        return minimumBalance;
    }
    
    /**
     * Sets the minimum balance requirement.
     * 
     * @param minimumBalance the new minimum balance
     * @throws IllegalArgumentException if minimum balance is negative
     */
    public void setMinimumBalance(double minimumBalance) {
        if (minimumBalance < 0) {
        	System.out.println("Minimum balance cannot be negative");
        }
        this.minimumBalance = minimumBalance;
    }
    
    /**
     * Overrides withdraw to enforce minimum balance requirement.
     * This is the key polymorphic behavior - SavingsAccount withdraws differently than Account.
     * 
     * @param amount the amount to withdraw
     * @return true if withdrawal was successful, false otherwise
     */
    /**
     * Withdraws money from the savings account.
     * Overrides parent to enforce minimum balance requirement.
     * 
     * @param amount the amount to withdraw
     * @return true if withdrawal successful, false otherwise
     */
    @Override
    public boolean withdraw(double amount) {
        // Savings-specific validation: check minimum balance
        if (balance - amount < minimumBalance) {
            System.out.println("Cannot withdraw €" + amount + 
                              ". Would violate minimum balance of €" + minimumBalance + 
                              ". Current balance: €" + balance);
            return false;
        }
        
        // Delegate to parent for all standard withdrawal logic
        // (frozen check, positive amount check, balance update, transaction recording)
        return super.withdraw(amount);
    }

    /**
     * Calculates the interest earned based on current balance.
     * 
     * @return the interest amount
     */
    public double calculateInterest() {
        return balance * interestRate;
    }
    
    /**
     * Adds interest to the account based on the interest rate.
     * This is a SavingsAccount-specific method not available in base Account.
     * 
     * @return the amount of interest added
     */
    public double addInterest() {
        double interest = calculateInterest();
        if (interest > 0) {
            balance += interest;
            
            // Record as a deposit transaction
            Transaction transaction = new Transaction(
                Transaction.TransactionType.DEPOSIT,
                interest,
                getAccountId(),
                balance,
                "Interest credited at " + (interestRate * 100) + "%"
            );
            transactionHistory.add(transaction);
            
            System.out.println("Interest of €" + String.format("%.2f", interest) + 
                             " added to Savings Account #" + getAccountId());
        }
        return interest;
    }
    
    /**
     * Returns the account type identifier.
     * 
     * @return "Savings Account"
     */
    @Override
    public String getAccountType() {
        return "Savings Account";
    }
    
    /**
     * Returns a string representation including savings-specific details.
     * 
     * @return formatted string with account details
     */
    @Override
    public String toString() {
        return super.toString() + 
               " [Rate: " + (interestRate * 100) + "%, MinBalance: €" + minimumBalance + "]";
    }
}
