package service;

import java.sql.Connection;
import java.sql.SQLException;
import DataAccess.*;
import Model.*;

public class AccountCreationSpgrp {

	private final AccountAndCardGenerator generator;
	private final Connection conn;

	public AccountCreationSpgrp(Connection conn) {
		this.conn = conn;
		this.generator = new AccountAndCardGenerator(conn);
	}

	public int createSuperGroup(Connection connection, CardInfo cardInfo) throws SQLException {

		boolean xspgrpFound = GetKntrskpf.chkSpgrpAcc(connection, cardInfo);
		

		// check Supergroup account already existed? Create if not exist
		if (!xspgrpFound) {

			cardInfo.setAccCat(6);
			cardInfo.setSpgrpAccNumber(generator.generateAccount(connection, cardInfo));

			if (cardInfo.getSpgrpAccNumber() == 0) {
				System.out.println("❌ Failed to generate supergroup account.");
				return 0;
			}
			cardInfo.setAccNumber(cardInfo.getSpgrpAccNumber());

			// Write KNTRSKPF
			boolean success = AddKntrskpf.addAcc(connection, cardInfo);
			if (!success) {
				System.out.println("❌ Failed to add supergroup account to KNTRSKPF.");
				return 0;
			}

		} else {
			cardInfo.setAccNumber(cardInfo.getSpgrpAccNumber());
		}

		System.out.println("✅ Supergroup account returned successfully: " + cardInfo.getSpgrpAccNumber());
		return cardInfo.getSpgrpAccNumber();
	}
}
