package app;
import java.sql.Connection;


public interface CardCreationProcessing {
    
    // Method to generate the application file - create ANSOKRPF
    void generateApplicationFile();
    
    // Method to generate the card number 
    String generateCardNumber();
    
    // Method to save card info into the database - create KORTRGPF
    void saveCardToDatabase();
    
    // Method to generate an account - create KNTRSKPF
    void generateAccount();
}
