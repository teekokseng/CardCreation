package Model;

public class CardDetails {
    private long cardNumber;
    private long customerNo;
    private String customerName;
    private String product;
    private double cardLimit;
    private String groupAccount;
    private double creditLimit;
    private String accountStatus;

    // Constructor
    public CardDetails(long cardNumber, long customerNo, String customerName, String product,
                       double cardLimit, String groupAccount, double creditLimit, String accountStatus) {
        this.cardNumber = cardNumber;
        this.customerNo = customerNo;
        this.customerName = customerName;
        this.product = product;
        this.cardLimit = cardLimit;
        this.groupAccount = groupAccount;
        this.creditLimit = creditLimit;
        this.accountStatus = accountStatus;
    }

    // Getters
    public long getCardNumber() { return cardNumber; }
    public long getCustomerNo() { return customerNo; }
    public String getCustomerName() { return customerName; }
    public String getProduct() { return product; }
    public double getCardLimit() { return cardLimit; }
    public String getGroupAccount() { return groupAccount; }
    public double getCreditLimit() { return creditLimit; }
    public String getAccountStatus() { return accountStatus; }

}
