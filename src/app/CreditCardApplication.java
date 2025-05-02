package app;

import java.sql.Connection;
import java.sql.SQLException;

import DataAccess.AddAnsokrpf;
import Model.CardInfo;
import Utility.ValidationResult;
import service.AccountCreation;
import service.AccountCreationSpgrp;
import service.LinkGrpAndSpgrpAcc;

public class CreditCardApplication extends CardApplicationBase {

	private final AccountCreation accountCreation;
	private final AccountCreationSpgrp accountCreationSpgrp;

	public CreditCardApplication(int customerNo, String productId, int cardLimit, Connection conn) {
		super(customerNo, productId, cardLimit, conn);
		this.conn = conn;
		this.accountCreation = new AccountCreation(conn);
		this.accountCreationSpgrp = new AccountCreationSpgrp(conn);
	}

	@Override
	protected boolean preValidate() {

		ValidationResult result;
		result = Utility.CommonValidation.validateCustomerNo(conn, customerNo, "CREDIT");
		result = Utility.CommonValidation.validateProduct(conn, customerNo, productId);
		result = Utility.CommonValidation.checkProductAlreadyApplied(conn, customerNo, productId);


		if (result.getCode() == "") {
			return true;

		} else {
			System.out.println("Validation failed: " + result.getCode() + " - " + result.getMessage());
			return false;
		}
	}

	@Override
	protected boolean postProcess() {
		try {
			conn.setAutoCommit(false); // 🚦 Start manual transaction

			// Creating CardInfo object
			CardInfo cardInfo = new CardInfo(customerNo, productId, cardLimit);

			// Generate application record (Lopn)

			int generatedLopn = AddAnsokrpf.createApplicationRecord(conn, cardInfo);
			cardInfo.setApplLopn(generatedLopn);
			if (generatedLopn == 0) {
				System.out.println("❌ Failed to generate application number.");
				return false;
			}

			// Create accounts and cards
			boolean success = accountCreation.createAccountsAndCards(conn, cardInfo);
			if (!success) {
				System.out.println("❌ Failed to create accounts and cards.");
				return false;
			}

			// Check and create supergroup account
			int spgrpAcc = accountCreationSpgrp.createSuperGroup(conn, cardInfo);
			if (spgrpAcc == 0) {
				System.out.println("❌ Failed to create super group account .");
				return false;
			}

			// Link group and supergroup
			LinkGrpAndSpgrpAcc linker = new LinkGrpAndSpgrpAcc(conn, cardInfo.getSpgrpAccNumber(),
					cardInfo.getGrpAccNumber());
			boolean linked = linker.linkSpgrpAndGrpAccIfNeeded(cardInfo);

			conn.commit(); // ✅ Everything succeeded
			System.out.println("✅ Credit card application completed successfully.");
			return true;

		} catch (SQLException e) {
			try {
				System.out.println("⚠️ Exception occurred. Rolling back transaction.");
				conn.rollback();
			} catch (SQLException rollbackEx) {
				System.out.println("‼️ Failed to rollback transaction.");
				rollbackEx.printStackTrace();
			}
			e.printStackTrace();
			return false; // Returning false as transaction failed

		} finally {
			// Resetting auto-commit mode
			try {
				conn.setAutoCommit(true); // ♻️ Restore default mode
			} catch (SQLException e) {
				System.out.println("⚠️ Failed to reset auto-commit.");
				e.printStackTrace();
			}
		}
	}

}
