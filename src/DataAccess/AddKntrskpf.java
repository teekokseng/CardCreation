package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DataAccess.GetSrvdefpf.SrvDefInfo;
import Model.CardInfo;

public class AddKntrskpf {

	public static boolean addAcc(Connection conn, CardInfo cardInfo) throws SQLException {
		String insertSQL = "INSERT INTO KNTRSKPF (KRKNTN, KRRKID, KRPGRP, KRKIPN, KRACAT, KRKAID, KRSPLV, KRBKRD, KRKNST, KRAVST) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//Prepare the statement
		PreparedStatement pstmt = conn.prepareStatement(insertSQL);

//Map CardInfo fields to the new table columns
		pstmt.setInt(1, cardInfo.getAccNumber());
		pstmt.setString(2, cardInfo.getProduct());
		pstmt.setString(3, null);
		pstmt.setInt(4, cardInfo.getCustomerNo());
		pstmt.setInt(5, cardInfo.getAccCat());
		pstmt.setString(6, cardInfo.getInfo().getSdkaid()); // SDKAID
		pstmt.setInt(7, cardInfo.getInfo().getSdsplv()); // SDSPLV
		pstmt.setInt(8, cardInfo.getCardLimit());
		pstmt.setInt(9, 0);
		pstmt.setInt(10, 9);
		
		int rowsInserted = pstmt.executeUpdate();
		if (rowsInserted > 0) {
			System.out.println("✅ Acc" + cardInfo.getAccNumber() + " created and saved into database successfully!");
			return true;
		} else {
			System.out.println("❌ Failed to save card into database.");
			return false;
		}

	}
}