import java.sql.Connection;
import java.sql.SQLException;

import CommonCopyBook.CommonValidation;
import InfoCreation.CardInfo;

public class DebitCardApplication extends CardApplicationBase {

	private final Connection connection;

	public DebitCardApplication(int customerNo, String productId, int cardLimit, Connection connection) {
		super(customerNo, productId, cardLimit);
		this.connection = connection;
	}

	@Override
	protected boolean preValidate() {
		return CommonValidation.validateCustomerNumber(connection, customerNo, "Credit Card")
				&& CommonValidation.validateProduct(connection, customerNo, productId);
	}
	
	@Override
	protected boolean postProcess() {
	    try {
	        connection.setAutoCommit(false); // 🚦 Start manual transaction

	        int generatedLopn = CardApplicationBase.generateApplicationFile(connection, customerNo, productId);
	        if (generatedLopn == 0) {
	            System.out.println("❌ Failed to generate application number.");
	            return false; // No rollback here, we handle it in the catch block
	        }

	        String generatedCardNumber = CardApplicationBase.generateCardNumber(connection, productId);
	        if (generatedCardNumber == null) {
	            System.out.println("❌ Failed to generate card number.");
	            return false; // Same here
	        }

	        CardInfo cardInfo = new CardInfo(customerNo, productId, cardLimit, generatedCardNumber, generatedLopn);
	        boolean success = CardApplicationBase.saveCardToDatabase(connection, cardInfo);
	        if (!success) {
	            System.out.println("❌ Failed to save card to database.");
	            return false; // Same here as well
	        }

	        CardApplicationBase.generateAccount(customerNo, productId);

	        connection.commit(); // ✅ Everything succeeded
	        System.out.println("✅ Credit card application completed successfully.");
	        return true;

	    } catch (SQLException e) {
	        // Exception handling with rollback
	        try {
	            System.out.println("⚠️ Exception occurred. Rolling back transaction.");
	            connection.rollback();
	        } catch (SQLException rollbackEx) {
	            System.out.println("‼️ Failed to rollback transaction.");
	            rollbackEx.printStackTrace();
	        }
	        e.printStackTrace();
	        return false; // Returning false as transaction failed

	    } finally {
	        // Resetting auto-commit mode
	        try {
	            connection.setAutoCommit(true); // ♻️ Restore default mode
	        } catch (SQLException e) {
	            System.out.println("⚠️ Failed to reset auto-commit.");
	            e.printStackTrace();
	        }
	    }
	}
}
