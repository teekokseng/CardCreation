package CommonCopyBook;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommonValidation {

	public static boolean validateCustomerNumber(Connection connection, int customerNo, String cardType) {
		System.out.println(cardType + " :Validating customer number: " + customerNo);

		String sql = "SELECT IPKIPN FROM INPARTPF WHERE IPKIPN = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, customerNo);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				System.out.println("Customer number not found!");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Error : " + cardType + "- during customer number validation.");
			e.printStackTrace();
			return false;
		}
	}

	public static boolean validateProduct(Connection connection, int customerNo, String productId) {

		String sqlSTRKRSPF = "SELECT SRRKID FROM STRKRSPF WHERE SRRKID = ?";
		String sqlCRDGENPF = "SELECT CGCGEN FROM CRDGENPF WHERE CGCGEN = ?";

		try {
			// Check in STRKRSPF
			try (PreparedStatement pstmt1 = connection.prepareStatement(sqlSTRKRSPF)) {
				pstmt1.setString(1, productId);
				ResultSet rs1 = pstmt1.executeQuery();
				if (rs1.next()) {
					return true;
				}
			}

			// Check in CRDGENPF
			try (PreparedStatement pstmt2 = connection.prepareStatement(sqlCRDGENPF)) {
				pstmt2.setString(1, productId);
				ResultSet rs2 = pstmt2.executeQuery();
				if (rs2.next()) {
					return true;
				}
			}

			// If not found in both
			System.out.println("Product ID not found in either STRKRSPF or CRDGENPF!");
			return false;

		} catch (SQLException e) {
			System.out.println("Error during product ID validation.");
			e.printStackTrace();
			return false;
		}

	}
}
