package InfoCreation;


public class CardInfo {
    private int customerNo;
    private String product;
    private int cardLimit;
    private String cardNumber;
    private int applLopn;

    // Constructor
    public CardInfo(int customerNo, String product, int cardLimit, String cardNumber, int applLopn) {
        this.customerNo = customerNo;
        this.product = product;
        this.cardLimit = cardLimit;
        this.cardNumber = cardNumber;
        this.applLopn = applLopn;
    }

    // Getters
    public int getCustomerNo() {
        return customerNo;
    }

    public String getProduct() {
        return product;
    }

    public int getCardLimit() {
        return cardLimit;
    }

    public String getCardNumber() {
        return cardNumber;
    }
    

    public int getapplLopn() {
        return applLopn;
    }
}
