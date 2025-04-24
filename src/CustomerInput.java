public class CustomerInput {
    private String customerNo;
    private String product;
    private double cardLimit;

    // Constructors, getters, and setters
    public CustomerInput(String customerNo, String product, double cardLimit) {
        this.customerNo = customerNo;
        this.product = product;
        this.cardLimit = cardLimit;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getCardLimit() {
        return cardLimit;
    }

    public void setCardLimit(double cardLimit) {
        this.cardLimit = cardLimit;
    }
}