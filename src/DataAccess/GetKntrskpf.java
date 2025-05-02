package DataAccess;

import java.sql.*;

import Model.CardInfo;

public class GetKntrskpf {

	public static boolean chkSpgrpAcc(Connection conn, CardInfo cardInfo) {
		String sql = "SELECT KRKNTN FROM KNTRSKPF WHERE KRKIPN = ? AND KRACAT = 6";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, cardInfo.getCustomerNo());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				cardInfo.setSpgrpAccNumber(rs.getInt("KRKNTN"));
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Error while retrieving card bin from CRDGENPF.");
			e.printStackTrace();
			return false;
		}
	}
}
