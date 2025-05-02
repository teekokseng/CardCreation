package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.CrdGenInfo;

public class GetCrdgenpf {
	
	public static CrdGenInfo getCardBin(Connection conn, String productId) {
	    String sql = "SELECT CGCGEN, CGDESC, CGUFNR FROM CRDGENPF WHERE CGCGEN = ? LIMIT 1"; 
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, productId);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Create CrdGenInfo object and fill in the data
	            CrdGenInfo info = new CrdGenInfo();
	            info.setCgcgen(rs.getString("CGCGEN"));
	            info.setCgdesc(rs.getString("CGDESC"));
	            info.setCgufnr(rs.getString("CGUFNR"));
	            return info;
	        } else {
	            System.out.println("No card bin found in CRDGENPF.");
	            return null;
	        }
	    } catch (SQLException e) {
	        System.out.println("Error while retrieving card bin from CRDGENPF.");
	        e.printStackTrace();
	        return null;
	    }
	}
}