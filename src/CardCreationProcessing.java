import java.sql.Connection;

import InfoCreation.CardInfo;

public interface CardCreationProcessing {
    
    // Method to generate the application file - create ANSOKRPF
    void generateApplicationFile(String customerNo, String productId);
    
    // Method to generate the card number 
    String generateCardNumber(String cardBin);
    
    // Method to save card info into the database - create KORTRGPF
    void saveCardToDatabase(Connection conn, CardInfo cardInfo);
    
    // Method to generate an account - create KNTRSKPF
    void generateAccount(String customerNo, String productId, String cardNumber);
}
