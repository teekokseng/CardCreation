import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CardCreationMain {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// Get a single shared connection
		Connection conn = DatabaseConnectionManager.getConnection();

		if (conn == null) {
			System.out.println("Database connection failed! Exiting.");
			return; // exit if connection is not established
		}

		System.out.println("===========================================");
		System.out.println("      Welcome to Card Management System    ");
		System.out.println("===========================================\n");

		boolean running = true;
		while (running) {
			System.out.println("\nPlease select an option:");
			System.out.println(" [1] ➔ Create New Card");
			System.out.println(" [2] ➔ View Existing Cards");
			System.out.println(" [3] ➔ Exit");
			System.out.println("-------------------------------------------");
			System.out.print("Enter your choice: ");

			int choice = -1;
			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input, please enter a number!");
				continue;
			}

			switch (choice) {
			case 1:
				createNewCard(scanner, conn);
				break;
			case 2:
				viewCreatedCards(conn);
				break;
			case 3:
				running = false;
				System.out.println("Exiting application. Goodbye!");
				break;
			default:
				System.out.println("Invalid choice. Please select again.");
			}
		}
		// Close the connection when done
		DatabaseConnectionManager.closeConnection();
		
		scanner.close();
	}

	private static void createNewCard(Scanner scanner, Connection conn) {

		System.out.println("-------------------------------------------");
		System.out.println("         Card Type Selection");
		System.out.println("-------------------------------------------\n");
		System.out.println(" [1] ➔ Debit Card");
		System.out.println(" [2] ➔ Credit Card\n");
		System.out.println(" [9] ➔ Back to previous\n");
		System.out.print("Enter your choice: ");

		int cardTypeChoice = -1;
		try {
			cardTypeChoice = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Invalid input, please enter a number!");
			return;
		}

		if (cardTypeChoice == 9) {
			return; // simply exit this method to go back to main menu
		}

		if (cardTypeChoice != 1 && cardTypeChoice != 2) {
			System.out.println("Invalid card type. Creation cancelled.");
			return;
		}

		System.out.print("Enter Customer Number: ");
		int customerNo = Integer.parseInt(scanner.nextLine());
		
		System.out.print("Enter Product: ");
		String product = scanner.nextLine();

		System.out.print("Enter Card Limit: ");
		int cardLimit = Integer.parseInt(scanner.nextLine());

		// 1. Create application object for this request (based on card type)
		CardApplicationBase application;
		if (cardTypeChoice == 1) {
			application = new DebitCardApplication(customerNo, product, cardLimit, conn);
		} else {
			application = new CreditCardApplication(customerNo, product, cardLimit, conn);
		}

		// 2. Run applyCard()
		boolean processSts = application.applyCard();

		if (processSts == false) {
			System.out.println("Card creation failed due to validation issues.");
			return;
		}

		
	}

	private static void viewCreatedCards(Connection conn) {
		String selectSQL = "SELECT customer_no, product, card_limit, card_number FROM KORTRGPF";

		System.out.println("\n=== Created Cards ===");

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectSQL)) {

			boolean hasRecords = false;
			while (rs.next()) {
				hasRecords = true;
				String customerNo = rs.getString("customer_no");
				String product = rs.getString("product");
				double limit = rs.getDouble("card_limit");
				String cardNumber = rs.getString("card_number");

				System.out.println("CustomerNo: " + customerNo + ", Product: " + product + ", Limit: " + limit
						+ ", Card#: " + cardNumber);
			}

			if (!hasRecords) {
				System.out.println("No cards created yet.");
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving cards from database.");
			e.printStackTrace();
		}
	}
}
