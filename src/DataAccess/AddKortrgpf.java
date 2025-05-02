package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.CardInfo;

public class AddKortrgpf {

	public static boolean addCard(Connection conn, CardInfo cardInfo) throws SQLException {
		String insertSQL = "INSERT INTO KORTRGPF (KTKIPN, KTKRTN, KTKNTN, KTEMNM, KTPLIM, KTTLIM, KTKRST,KTLOPN,KTRKID) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?,?,?)";

//Prepare the statement
		PreparedStatement pstmt = conn.prepareStatement(insertSQL);

//Map CardInfo fields to the new table columns
		pstmt.setInt(1, cardInfo.getCustomerNo()); // ktkipn (mapped from customer_no)
		pstmt.setLong(2, cardInfo.getCardNumber());
		pstmt.setInt(3, cardInfo.getAccNumber());  // ktkntn (No value provided, maybe a placeholder or null)
		pstmt.setString(4, ""); // ktemnm (No value provided, maybe a placeholder or null)
		pstmt.setDouble(5, cardInfo.getCardLimit()); // ktplim (mapped from card_limit)
		pstmt.setDouble(6, cardInfo.getCardLimit()); // kttlim (mapped from card_limit or a different field)
		pstmt.setString(7, "001"); // ktkrst (No value provided, maybe a placeholder or null)
		pstmt.setInt(8, cardInfo.getApplLopn());  
		pstmt.setString(9, cardInfo.getProduct()); 

		int rowsInserted = pstmt.executeUpdate();
		if (rowsInserted > 0) {
			System.out.println("✅ Card created and saved into KORTRGPF successfully!");
			return true;
		} else {
			System.out.println("❌ Failed to save card into database.");
			return false;
		}

	}
}