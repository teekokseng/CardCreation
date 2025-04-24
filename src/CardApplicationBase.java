import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import InfoCreation.AddAnsokrpf;
import InfoCreation.AddKortrgpf;
import InfoCreation.CardInfo;
import InfoRetrieval.CRDGENPF;
import InfoRetrieval.CrdGenInfo;

public abstract class CardApplicationBase {

	protected int customerNo;
	protected String productId;
	protected int cardLimit;
	

	public CardApplicationBase(int customerNo, String productId, int cardLimit) {
		this.customerNo = customerNo;
		this.productId = productId;
		this.cardLimit = cardLimit;
	}

	// Abstract Methods that Child Classes MUST implement
	// ==================================================
	protected abstract boolean preValidate();// Validation before creating card

	protected abstract boolean postProcess();// Processing after validation (generate card, insert into DB, etc.)

	
	
	// Generic method
	// ==============
	public boolean applyCard() {
		if (preValidate()) {
			return postProcess();

		} else {
			System.out.println("Validation failed for customer " + customerNo + ", product " + productId);
			return false;
		}
	}

	// Generate ANSOKRPF
	// =================
	public static int generateApplicationFile(Connection conn, int customerNo, String productId) throws SQLException {
	    return AddAnsokrpf.createApplicationRecord(conn, customerNo, productId);
	}
		 

	// Generate Card Number
	// ====================
	public static String generateCardNumber(Connection conn, String productId) {

		CrdGenInfo crdGenInfo = CRDGENPF.getCardBin(conn, productId);

		if (crdGenInfo == null || crdGenInfo.getCgufnr() == null) {
			System.out.println("Error: No card bin found for the product ID.");
			return null;
		}

		String cardBin = crdGenInfo.getCgufnr();

		// Generate card number: BIN + random 6 digits
		String cardNumber = cardBin + String.format("%06d", (int) (Math.random() * 1000000));
		System.out.println("Generated Credit Card number: " + cardNumber);

		return cardNumber;
	}

	// Write KORTRGPF
	// ==============
	public static boolean saveCardToDatabase(Connection conn, CardInfo cardInfo) throws SQLException {
		 return AddKortrgpf.addCard(conn, cardInfo);
	}

	// Write KNTRSKPF,ACCSTRPF
	// =======================
	public static void generateAccount(int customerNo, String productID) {

	}

}
