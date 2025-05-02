package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.CardInfo;

public class AddExtstrpf {

    public static boolean recordExists(Connection conn, int child, int parent) throws SQLException {
        String sql = "SELECT 1 FROM EXTSTRPF WHERE KRSGRP = ? AND KRGROUP = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, child);
            ps.setInt(2, parent);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static boolean insertCreditLink(Connection conn, int spgrpAcc, int grpAcc, int kipn) throws SQLException {
        String insertSql = "INSERT INTO EXTSTRPF (KRCORP, KRSGRP, KRGROUP, KRKIPN) VALUES (0, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setInt(1, spgrpAcc);
            ps.setInt(2, grpAcc);
            ps.setInt(3, kipn);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean insertGenericLink(Connection conn, int child, int parent) throws SQLException {
        String insertSql = "INSERT INTO EXTSTRPF (KRCORP, KRSGRP, KRGROUP, KRKIPN) VALUES (0, ?, ?, 0)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setInt(1, child);
            ps.setInt(2, parent);
            return ps.executeUpdate() > 0;
        }
    }
}
