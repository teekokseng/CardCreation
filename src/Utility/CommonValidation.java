package Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommonValidation {

	public static ValidationResult validateCustomerNo(Connection connection, int customerNo, String cardType) {
		System.out.println(cardType + " : Validating customer number: " + customerNo);
		String sql = "SELECT IPKIPN FROM INPARTPF WHERE IPKIPN = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, customerNo);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return new ValidationResult("", "Successful");
			} else {
				return new ValidationResult("G001", "Customer number not found!");
			}

		} catch (SQLException e) {
			System.out.println("Error during " + cardType + " customer number validation.");
			e.printStackTrace();
			return new ValidationResult("EX01", "Database error during validation.");
		}
	}

	public static ValidationResult validateProduct(Connection connection, int customerNo, String productId) {
		String sqlSTRKRSPF = "SELECT SRRKID FROM STRKRSPF WHERE SRRKID = ?";
		String sqlCRDGENPF = "SELECT CGCGEN FROM CRDGENPF WHERE CGCGEN = ?";
		String sqlKNTRSKPF = "SELECT 1 FROM KNTRSKPF WHERE KRKIPN = ? AND KRRKID = ?";

		try {
			boolean found = false;

			// Check in STRKRSPF
			try (PreparedStatement pstmt1 = connection.prepareStatement(sqlSTRKRSPF)) {
				pstmt1.setString(1, productId);
				ResultSet rs1 = pstmt1.executeQuery();
				if (rs1.next()) {
					found = true;
				}
			}

			// Check in CRDGENPF only if not found yet
			if (!found) {
				try (PreparedStatement pstmt2 = connection.prepareStatement(sqlCRDGENPF)) {
					pstmt2.setString(1, productId);
					ResultSet rs2 = pstmt2.executeQuery();
					if (rs2.next()) {
						found = true;
					}
				}
			}

			if (found) {
				return new ValidationResult("", "Successful");
			} else {
				return new ValidationResult("G004", "Product ID not found in either STRKRSPF or CRDGENPF!");
			}

		} catch (SQLException e) {
			System.out.println("Error during product ID validation.");
			e.printStackTrace();
			return new ValidationResult("EX01", "Database error during validation.");
		}
	}
	
	
	public static ValidationResult checkProductAlreadyApplied(Connection connection, int customerNo, String productId) {
	    String sql = "SELECT 1 FROM KNTRSKPF WHERE KRKIPN = ? AND KRRKID = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, customerNo);
	        pstmt.setString(2, productId);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            return new ValidationResult("G005", "Product has already been applied by this customer.");
	        } else {
	            return new ValidationResult("", "Not applied yet.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error during check for product application.");
	        e.printStackTrace();
	        return new ValidationResult("EX02", "Database error during product application check.");
	    }
	}
}
