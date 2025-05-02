package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.CardInfo;

public class GetSrvdefpf {

    // Create a custom data class to hold the values
    public static class SrvDefInfo {
        private String sdsvid;
        private int sdsplv;
        private String sdrkid;
        private String sdkaid;
        private String sdtcgr;
        private String sdcard;

        public SrvDefInfo(String sdsvid, int sdsplv, String sdrkid, String sdkaid, String sdtcgr, String sdcard) {
            this.sdsvid = sdsvid;
            this.sdsplv = sdsplv;
            this.sdrkid = sdrkid;
            this.sdkaid = sdkaid;
            this.sdtcgr = sdtcgr;
            this.sdcard = sdcard;
        }

        // Getters
        public String getSdsvid() { return sdsvid; }
        public int getSdsplv() { return sdsplv; }
        public String getSdrkid() { return sdrkid; }
        public String getSdkaid() { return sdkaid; }
        public String getSdtcgr() { return sdtcgr; }
        public String getSdcard() { return sdcard; }
    }

    public List<SrvDefInfo> srvDefArr(Connection conn, CardInfo cardInfo) {
        List<SrvDefInfo> dataList = new ArrayList<>();
        
        // Adjust the query to use the exact column names from the database (case-sensitive)
        String sql = "SELECT SDSVID, SDSPLV, SDRKID, SDKAID, SDTCGR, SDCARD FROM SRVDEFPF WHERE SDRKID = ? ORDER BY SDSPLV";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set parameter for SDPRID
            stmt.setString(1, cardInfo.getProduct()); // Assuming 'getProduct()' corresponds to SDPRID

            try (ResultSet rs = stmt.executeQuery()) {
                // Iterate over result set and create SrvDefInfo objects for each row
                while (rs.next()) {
                    SrvDefInfo info = new SrvDefInfo(
                        rs.getString("SDSVID"),
                        rs.getInt("SDSPLV"),
                        rs.getString("SDRKID"),
                        rs.getString("SDKAID"),
                        rs.getString("SDTCGR"),
                        rs.getString("SDCARD")
                    );
                    dataList.add(info); // Add the object to the list
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception (e.g., log it, rethrow, or return an empty list)
        }

        return dataList;
    }
}
