package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import DataAccess.*;
import Model.*;

public class AccountCreation {

	private AccountAndCardGenerator generator;

	public AccountCreation(Connection connection) {
		this.generator = new AccountAndCardGenerator(connection);
	}

	public boolean createAccountsAndCards(Connection connection, CardInfo cardInfo) throws SQLException {

		// Fetch the list of SrvDefInfo based on connection and cardInfo
		GetSrvdefpf getSrvdefpf = new GetSrvdefpf();

		List<GetSrvdefpf.SrvDefInfo> resultList = getSrvdefpf.srvDefArr(connection, cardInfo);

		// Loop through the results
		for (GetSrvdefpf.SrvDefInfo info : resultList) {
			cardInfo.setAccCat(3);

			// Generate the Account Number using AccountAndCardGenerator
			// Generate account number
			int generatedAccount = generator.generateAccount(connection, cardInfo);
			if (generatedAccount == 0) {
				System.out.println("❌ Failed to generate account number.");
				return false;
			}

			cardInfo.setAccNumber(generatedAccount);

			// Set group account if applicable
			if (info.getSdsplv() == 0) {
				cardInfo.setAccCat(2);
				cardInfo.setGrpAccNumber(generatedAccount);
			}

			// Attach SrvDefInfo to cardInfo
			cardInfo.setInfo(info);

			// Write ACCSTRPF - sub account
			if (cardInfo.getAccCat() == 3) {

				if (info.getSdsplv() > 2) {

					if ("RTL".equals(info.getSdtcgr())) {
						cardInfo.setSubAccType("3");
					} else if ("CSH".equals(info.getSdtcgr())) {
						cardInfo.setSubAccType("4");
					}

				} else if (info.getSdsplv() == 2) {
					cardInfo.setSubAccType("2");
				} else {
					cardInfo.setSubAccType("1");
				}
				
					
					boolean success = AddAccstrpf.addAccstr(connection,  cardInfo.getGrpAccNumber(), cardInfo.getAccNumber(), cardInfo.getInfo().getSdtcgr(),cardInfo.getSubAccType());
				if (!success) {
					System.out.println("❌ Failed to add KNTRSKPF record.");
					return false;
				}
			}

			// Write KNTRSKPF
			boolean success = AddKntrskpf.addAcc(connection, cardInfo);
			if (!success) {
				System.out.println("❌ Failed to add KNTRSKPF record.");
				return false;
			}

			// Generate Card Number
			if ("1".equals(info.getSdcard())) {
				long  generatedCardNumber = generator.generateCardNumber(cardInfo);
				cardInfo.setCardNumber(generatedCardNumber);

				if (generatedCardNumber == 0) {
					System.out.println("❌ Failed to generate card number.");
					return false;
				}

				success = generator.saveCardToDatabase(cardInfo);
				if (!success) {
					System.out.println("❌ Failed to save card to database.");
					return false;
				}
			}

		}

		return true; // If all accounts and cards are successfully created
	}
}
