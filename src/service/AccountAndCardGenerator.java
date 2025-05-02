package service;

import java.sql.*;
import Model.CardInfo;
import Model.CrdGenInfo;
import DataAccess.*;

public class AccountAndCardGenerator {

	private Connection conn;

	public AccountAndCardGenerator(Connection conn) {
		this.conn = conn;
	}

	// generate Account
	public int generateAccount(Connection connection, CardInfo cardInfo) throws SQLException {
		int customerNumber = cardInfo.getCustomerNo();
		int newSequence = getAndUpdateSequence(customerNumber);
		String sequenceStr = String.format("%04d", newSequence);

		String partialAccount = "    " + cardInfo.getApplLopn() + sequenceStr;

		int checkDigit = calculateCheckDigit(partialAccount.trim());
		String fullAccountStr = partialAccount.trim() + checkDigit;

		return Integer.parseInt(fullAccountStr);
	}

	private int getAndUpdateSequence(int customerNumber) throws SQLException {
		PreparedStatement selectStmt = conn.prepareStatement("SELECT IPKNTL FROM INPARTPF WHERE IPKIPN = ?");
		selectStmt.setLong(1, customerNumber);
		ResultSet rs = selectStmt.executeQuery();

		int currentSeq = 0;
		if (rs.next()) {
			currentSeq = rs.getInt("IPKNTL");
		}

		int newSeq = currentSeq + 1;

		PreparedStatement updateStmt = conn.prepareStatement("UPDATE INPARTPF SET IPKNTL = ? WHERE IPKIPN = ?");
		updateStmt.setInt(1, newSeq);
		updateStmt.setLong(2, customerNumber);
		updateStmt.executeUpdate();

		return newSeq;
	}

	private int calculateCheckDigit(String input) {
		int sum = 0;
		for (int i = input.length() - 1; i >= 0; i--) {
			int digit = Character.getNumericValue(input.charAt(i));
			sum += digit;
		}
		return sum % 10;
	}

	// generate CARD
	public long generateCardNumber(CardInfo cardinfo) throws SQLException {
		CrdGenInfo crdGenInfo = GetCrdgenpf.getCardBin(conn, cardinfo.getProduct());

		if (crdGenInfo == null || crdGenInfo.getCgufnr() == null) {
			System.out.println("Error: No card bin found for the product ID.");
			return 0;
		}

		String prefix = "000";
		String cardBin = crdGenInfo.getCgufnr();
		long newSequence = getAndUpdateCardSequence(cardBin);
		String sequenceStr = String.format("%07d", newSequence);

		String partialCardNumber = prefix + cardBin + sequenceStr;
		int checkDigit = calculateCardCheckDigit(partialCardNumber);

		String fullCardStr = partialCardNumber + checkDigit;
		return Long.parseLong(fullCardStr);
	}

	public boolean saveCardToDatabase(CardInfo cardInfo) throws SQLException {
		return AddKortrgpf.addCard(conn, cardInfo);
	}

	private long getAndUpdateCardSequence(String bin) throws SQLException {
		// Query to get the current sequence for the given bin (LOTYP)
		PreparedStatement selectStmt = conn.prepareStatement("SELECT LOSEQN FROM LOPNUMPF WHERE LOTYP = ?");
		selectStmt.setString(1, bin);
		ResultSet rs = selectStmt.executeQuery();

		long currentSeq = 0;
		if (rs.next()) {
			// If record found, get the current sequence
			currentSeq = rs.getLong("LOSEQN");
		} else {
			// If not found, insert a new record with an initial sequence of 1
			PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO LOPNUMPF (LOTYP, LOSEQN) VALUES (?, ?)");
			insertStmt.setString(1, bin);
			insertStmt.setLong(2, 1); // Initialize sequence to 1
			insertStmt.executeUpdate();

			// Return the initial sequence value
			return 1;
		}

		// Increment the sequence
		long newSeq = currentSeq + 1;

		// Update the sequence for the given bin
		PreparedStatement updateStmt = conn.prepareStatement("UPDATE LOPNUMPF SET LOSEQN = ? WHERE LOTYP = ?");
		updateStmt.setLong(1, newSeq);
		updateStmt.setString(2, bin);
		updateStmt.executeUpdate();

		return newSeq;
	}

	private int calculateCardCheckDigit(String input) {
		int sum = 0;
		boolean shouldDouble = false;

		for (int i = input.length() - 1; i >= 0; i--) {
			int digit = Character.getNumericValue(input.charAt(i));
			if (shouldDouble) {
				digit *= 2;
				if (digit > 9) {
					digit -= 9;
				}
			}
			sum += digit;
			shouldDouble = !shouldDouble;
		}

		int checkDigit = (10 - (sum % 10)) % 10;
		return checkDigit;
	}
}
