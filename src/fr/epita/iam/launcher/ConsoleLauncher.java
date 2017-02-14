/**
 * 
 */
package fr.epita.iam.launcher;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.services.JDBCIdentityDAO;

/**
 * @author tbrou
 *
 */
public class ConsoleLauncher {
	
	private static JDBCIdentityDAO dao;

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, SQLException {
		System.out.println("Hello, welcome to the IAM application ");
		Scanner scanner = new Scanner(System.in);
		dao = new JDBCIdentityDAO();
		
		
		
		//authentication
		System.out.println("Please enter your login");
		String login = scanner.nextLine();
		System.out.println("Please enter your password");
		String password = scanner.nextLine();
		
		if(!authenticate(login, password)){
			System.out.println("Invalid login or password. Please retry with the correct one.");
			scanner.close();
			return;
		}
		
		// menu
		String answer = menu(scanner);
		
		switch (answer) {
		case "a":
			// creation
			createIdentity(scanner);
			break;
		case "b":
			//modify
			modifyIdentity(scanner);
			break;
		case "c":
			//delete
			deleteIdentity(scanner);
			break;
		case "d":
			listIdentities();
			break;
		default:
			System.out.println("This option is not recognized ("+ answer + ")");
			break;
		}
		
		scanner.close();

	}
/**
 * Modifying the Identity
 * @param scanner
 */
	private static void modifyIdentity(Scanner scanner) {
		System.out.println("You've selected : Identity Modify");
		System.out.println("Please enter the Identity email address");
		String eid = scanner.nextLine();
		System.out.println("Please enter the Identity display name");
		String dname1=scanner.nextLine();
		String id = null;
		try {
			id = dao.select(eid,dname1);
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(id == null)
		{
			System.out.println("No such Identity exists. Please retry");
		scanner.close();
	}
		else
		{
		System.out.println("Please enter the new Identity Display Name:");
		String dname=scanner.nextLine();
		System.out.println("Please enter the new Identity email address:");
		String email=scanner.nextLine();
		Identity newIdentity = new Identity(id, dname,email);
		try {
			dao.modifyIdentity(newIdentity);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("You have succesfully updated this identity :" + newIdentity);
		}
	}
	
	/**
	 * Deleting the Identity
	 * @param scanner
	 */
	
	private static void deleteIdentity(Scanner scanner) {
		System.out.println("You've selected : Identity Delete");
		System.out.println("Please enter the Identity email address");
		String eid = scanner.nextLine();
		System.out.println("Please enter the Identity display name");
		String dname=scanner.nextLine();
		String id = null;
		try {
			id = dao.select(eid,dname);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(id == null)
		{
			System.out.println("No such Identity exists. Please retry");
		scanner.close();
	}
		else
		{
		Identity newIdentity = new Identity(id, dname,eid);
		try {
			dao.deleteIdentity(newIdentity);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("You have succesfully deleted this identity :" + newIdentity);
		}
	}


	/**
	 * Listing all the identities
	 * @throws SQLException 
	 * 
	 */
	private static void listIdentities() throws SQLException {
		System.out.println("This is the list of all identities in the system");
		List<Identity> list = dao.readAll();
		int size = list.size();
		for(int i = 0; i < size; i++){
			System.out.println( i+ "." + list.get(i));
		}
		
	}

	/**
	 * Creating an Identity
	 * @param scanner
	 * @throws SQLException 
	 */
	private static void createIdentity(Scanner scanner) throws SQLException {
		System.out.println("You've selected : Identity Creation");
		System.out.println("ENTER YOUR NAME");
		String dname = scanner.nextLine();
		System.out.println("ENTER YOUR EMAIL");
		String email = scanner.nextLine();
		Identity newIdentity = new Identity(null, dname, email);
		dao.writeIdentity(newIdentity);
		System.out.println("IDENTITY SUCCESSFULLY CREATED :" + newIdentity);
	}

	/**
	 * Switch Menu described below
	 * @param scanner
	 * @return
	 */
	private static String menu(Scanner scanner) {
		System.out.println("You're authenticated");
		System.out.println("Here are the actions you can perform :");
		System.out.println("a. Create Identity");
		System.out.println("b. Modify Identity");
		System.out.println("c. Delete Identity");
		System.out.println("d. List Identities");
		System.out.println("e. quit");
		System.out.println("your choice (a|b|c|d|e) ? : ");
		String answer = scanner.nextLine();
		return answer;
	}

	/**
	 * @param login
	 * @param password
	 */
	private static boolean authenticate(String login, String password) {

		// TODO replace this hardcoded check by the real authentication method
		return "aviral".equals(login) && "aviral".equals(password);
	}

}
