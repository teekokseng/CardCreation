package InfoCreation;

import java.sql.*;

public class AddAnsokrpf {

	public static int createApplicationRecord(Connection conn, int customerNo, String productId) throws SQLException {
		String selectSQL = "SELECT LOSEQN FROM LOPNUMPF WHERE LOTYP = 'LOPN'";
		String updateSQL = "UPDATE LOPNUMPF SET LOSEQN = ? WHERE LOTYP = 'LOPN'";
		String insertSQL = "INSERT INTO ANSOKRPF (ANLOPN, ANKIPN, ANEMNM, ANRKID) VALUES (?,?,?,?)";

		int nextSeq;

		// Step 1: Get current running number
		try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
				ResultSet rs = selectStmt.executeQuery()) {
			if (rs.next()) {
				nextSeq = rs.getInt("loseqn") + 1;
			} else {
				throw new SQLException("LOPN sequence not found!");
			}
		}

		// Step 2: Update running number
		try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
			updateStmt.setInt(1, nextSeq);
			updateStmt.executeUpdate();
		}

		// Step 3: Insert into ANSOKRPF
		try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
			insertStmt.setInt(1, nextSeq); // application_no
			insertStmt.setInt(2, customerNo);
			insertStmt.setString(3, null); // Proper null value (not "NULL" string)
			insertStmt.setString(4, productId);
			insertStmt.executeUpdate();
		}

		// No commit/rollback here — handled by caller
		System.out.println("Application record prepared with application_no: " + nextSeq);
		return nextSeq;
	}

}
