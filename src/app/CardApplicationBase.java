package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CardApplicationBase  {

	protected int customerNo;
	protected String productId;
	protected int cardLimit;
	protected Connection conn;

	public CardApplicationBase(int customerNo, String productId, int cardLimit, Connection conn) {
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

	
}