package service;

import java.sql.Connection;
import java.sql.SQLException;

import DataAccess.AddAccstrpf;
import DataAccess.AddExtstrpf;
import Model.CardInfo;

public class LinkGrpAndSpgrpAcc {
    private Integer corpAcc;
    private Integer divisionAcc;
    private Integer grpAcc;
    private Integer spgrpAcc;
    private Connection connection;

    public enum Mode {
        CREDIT, CORPORATE
    }

    private Mode mode;

    public LinkGrpAndSpgrpAcc(Connection connection, int corpAcc, int divisionAcc, int grpAcc) {
        this.connection = connection;
        this.corpAcc = corpAcc;
        this.divisionAcc = divisionAcc;
        this.grpAcc = grpAcc;
        this.mode = Mode.CORPORATE;
    }

    public LinkGrpAndSpgrpAcc(Connection connection, int spgrpAcc, int grpAcc) {
        this.connection = connection;
        this.spgrpAcc = spgrpAcc;
        this.grpAcc = grpAcc;
        this.mode = Mode.CREDIT;
    }

    public boolean linkIfNeeded(CardInfo cardInfo) throws SQLException {
        switch (mode) {
            case CREDIT:
                return linkSpgrpAndGrpAccIfNeeded(cardInfo);
            case CORPORATE:
                return linkCorpHierarchyIfNeeded();
            default:
                throw new IllegalStateException("Unsupported mode: " + mode);
        }
    }

    public boolean linkSpgrpAndGrpAccIfNeeded(CardInfo cardInfo) throws SQLException {
        if (!AddExtstrpf.recordExists(connection, spgrpAcc, grpAcc)) {
            if (!AddExtstrpf.insertCreditLink(connection, spgrpAcc, grpAcc, cardInfo.getCustomerNo())) {
                System.out.println("❌ Failed to insert into EXTSTRPF.");
                return false;
            }

            boolean success = AddAccstrpf.addAccstr(connection, spgrpAcc, grpAcc, null, null );
            if (success) {
                System.out.println("✅ Successfully added record to ACCSTRPF.");
            } else {
                System.out.println("❌ Failed to add record to ACCSTRPF.");
            }

            return success;
        } else {
            System.out.println("✅ EXTSTRPF record already exists.");
            return true;
        }
    }

    private boolean ensureLink(int child, int parent) throws SQLException {
        if (!AddExtstrpf.recordExists(connection, child, parent)) {
            return AddExtstrpf.insertGenericLink(connection, child, parent);
        }
        return true;
    }

    private boolean linkCorpHierarchyIfNeeded() throws SQLException {
        return ensureLink(divisionAcc, grpAcc) && ensureLink(corpAcc, divisionAcc);
    }
}
