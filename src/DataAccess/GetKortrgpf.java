package DataAccess;

import java.sql.*;

import Model.CardDetails;

public class GetKortrgpf {

	public static void viewCreatedCards(Connection conn) {
		String selectSQL = "SELECT KTKIPN, KTRKID, KTPLIM, KTKRTN FROM KORTRGPF";

		System.out.println("\n=== Created Cards ===");

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectSQL)) {

			boolean hasRecords = false;
			while (rs.next()) {
				hasRecords = true;
				long customerNo = rs.getLong("KTKIPN");
				String product = rs.getString("KTRKID");
				double limit = rs.getDouble("KTPLIM");
				long cardNumber = rs.getLong("KTKRTN");

				System.out.println("CustomerNo: " + customerNo + ", Product: " + product + ", Limit: " + limit
						+ ", Card#: " + cardNumber);
			}

			if (!hasRecords) {
				System.out.println("No cards created yet.");
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving cards from database.");
			e.printStackTrace();
		}
	}

	 public static CardDetails getCardDetails(Connection conn, long cardNumberInput) {
	        String query = """
	            SELECT
	                k.KTKIPN AS customer_no,
	                k.KTRKID AS product,
	                k.KTPLIM AS card_limit,
	                k.KTKRTN AS card_number,
	                a.KRGACC AS group_acc,
	                n.KRBKRD AS credit_limit,
	                n.KRKIPN AS account_holder,
	                n.KRAVST AS account_status,
	                i.IPNAME AS customer_name
	            FROM KORTRGPF k
	            LEFT JOIN ACCSTRPF a ON k.KTKNTN = a.KRKNTN
	            LEFT JOIN KNTRSKPF n ON a.KRGACC = n.KRKNTN
	            LEFT JOIN INPARTPF i ON n.KRKIPN = i.IPKIPN
	            WHERE k.KTKRTN = ?
	        """;

	        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	            pstmt.setLong(1, cardNumberInput);
	            try (ResultSet rs = pstmt.executeQuery()) {
	                if (rs.next()) {
	                    return new CardDetails(
	                        rs.getLong("card_number"),
	                        rs.getLong("customer_no"),
	                        rs.getString("customer_name"),
	                        rs.getString("product"),
	                        rs.getDouble("card_limit"),
	                        rs.getString("group_acc"),
	                        rs.getDouble("credit_limit"),
	                        rs.getString("account_status")
	                    );
	                }
	            }
	        } catch (SQLException e) {
	            System.out.println("❌ Error retrieving specific card information.");
	            e.printStackTrace();
	        }

	        return null;
	    }
}
