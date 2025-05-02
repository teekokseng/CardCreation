package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DataAccess.GetSrvdefpf.SrvDefInfo;
import Model.CardInfo;

public class AddAccstrpf {

	public static boolean addAccstr(Connection conn, int grpAcc, int accNum, String accTcgr, String accSuat) throws SQLException {
		String insertSQL = "INSERT INTO ACCSTRPF (KRGACC, KRKNTN, KRTCGR, KRSUAT) "
				+ "VALUES (?, ?, ?, ?)";
//Prepare the statement
		PreparedStatement pstmt = conn.prepareStatement(insertSQL);

//Map CardInfo fields to the new table columns
		pstmt.setInt(1, grpAcc);
		pstmt.setInt(2, accNum);
		pstmt.setString(3, accTcgr);
		pstmt.setString(4, accSuat);
		
		
		int rowsInserted = pstmt.executeUpdate();
		if (rowsInserted > 0) {
			return true;
		} else {
			System.out.println("❌ Failed to save card into database.");
			return false;
		}

	}
}