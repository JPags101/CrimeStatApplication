import corgis.state_crime.StateCrimeLibrary;
import corgis.state_crime.domain.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.Collections;

public class crimeStats {
	private static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
        // Get access to the library
        StateCrimeLibrary stateCrimeLibrary = new StateCrimeLibrary();
        // Access data inside the library
        ArrayList<Report> list_of_report = stateCrimeLibrary.getAllCrimes(false);
        
        System.out.println("***IMPORTANT NOTE: DATA EXCLUSIVE FROM 1960-2012***");
        System.out.println("(Data provided by: Unified Crime Reporting Statistics under collaboration"
        		+ " of the U.S. Department of Justice & FBI)\n");
        choice(list_of_report);
}
	
	/**
	 * Opens option menu and allows user to decide what part of the application to use
	 * also way to terminate the application by pressing 3
	 * @param list
	 */
	
	public static void choice(ArrayList<Report> list) {
		System.out.println("Choose an Option:");
		System.out.println("Press 0 to terminate application");
		System.out.println("Press 1 to view the Top 5 most dangerous/safest states in a given year");
		System.out.println("Press 2 to view a State's crime statistics in a given year");
		System.out.println("Press 3 to view crime table");
		System.out.println("Press 4 to view a random survival tip (Panganiban Programming Inc. is not liable if tips don't work)");
		int userInput = 1;
		
		while (userInput != 0) {
			userInput = sc.nextInt();
			switch (userInput) {
			case 0:
				break;
				
			case 1:
				topFive(list);
				break;
				
			case 2: 
				getStateStats(list);
				break;
				
			case 3:
				crimeTable(list);
				break;
				
			case 4:
				randomTips(list);
				break;
			
			default:
				System.out.println("Please enter a valid number");
				break;
			}
		}
	
	}
	
	/**
	 * Creates multiple array lists to store and sort through specific data
	 * gets the data for only a specific year entered by the user then extracts the total crimes reported from the element
	 * sorts all the elements in list from smallest to largest
	 * puts the top 5 smallest and largest into different arrays to be printed out
	 * @param list
	 */
	
	public static void topFive(ArrayList<Report> list) {
		ArrayList<Report> temp = new ArrayList<Report>();
		ArrayList<Double> stats = new ArrayList<Double>();
		ArrayList<Report> safe = new ArrayList<Report>();
		ArrayList<Report> danger = new ArrayList<Report>();
		
		System.out.println("Please enter a Year between 1960-2012");
		int year = sc.nextInt();
		for (Report r : list) 
			if (r.getYear() == year) temp.add(r);
		
		for (Report t : temp) {
		double current = crimeTotal(t);
		stats.add(current);
		}
		
		Collections.sort(stats);
		
		for (Report r : list) {
		double pointer = crimeTotal(r);
			for (int i = 0; i < 5; i++) {
			if (pointer == stats.get(i)) 
				safe.add(r);
			}
			for (int i = 50; i >= 46; i--) {
				if (pointer == stats.get(i)) 
					danger.add(r);
		}
		}
		
		printTopFive(safe,danger,stats,year);
		choice(list);
	}
	
	/**
	 * prints out the top five safe and danger lists passed on from the topFive method
	 * also adds in the state's total crime
	 * @param safe
	 * @param danger
	 * @param stats
	 * @param year
	 */
	
	public static void printTopFive(ArrayList<Report> safe, ArrayList<Report> danger, ArrayList<Double> stats, int year) {
		System.out.println("Top 5 Safest States in " + year + " (Based on reported totals)\n--------------------");
		for (int i = 0; i < safe.size(); i ++) {
			System.out.println((i + 1) + ") " + safe.get(i).getState() + " (" + stats.get(i) + " total reported crimes)");
		}
		System.out.println("\n--------------------\n");
		System.out.println("Top 5 Most Dangerous States in " + year + " (Based on reported totals)\n--------------------");
		for (int j = 0; j < danger.size(); j++) {
			int k = 50;
			System.out.println((j+1) + ") " + danger.get(j).getState() + " (" +  stats.get(k - j) + " total reported crimes)");
		}
		System.out.println("--------------------\n");
	}
	
	/**
	 * Takes in user year and state name to print out the total values from each data department
	 * allows for user to continue until 0 is pressed, where they are sent back to option menu
	 * @param list
	 */
	
	public static void getStateStats(ArrayList<Report> list) {
		System.out.println("\n*Reported totals*");
		System.out.println("Enter a Year and State to view statistics (Press 0 when done):");
		int year = sc.nextInt();
		while (year != 0) {
			String state = sc.next();
			
			printHeader();
			
		for (Report r : list) {
			if (year == r.getYear() && r.getState().equals(state)) {
				printStats(list,r);
				System.out.println("\n----------------\nEnter another year and state or press 0 to return to Option Menu.");
			}		
		}
		year = sc.nextInt();
	}
		choice(list);
	}
	
	public static void crimeTable(ArrayList<Report> list) {
		System.out.println("\n*Reported totals*");
		System.out.println("Enter a State to view statistics (Press 0 when done):");
		String state = sc.next();
		
		printHeader();
		
		for (Report r : list) {
			if (state.equals(r.getState())) {
				printStats(list,r);
			}
		}
		choice(list);
	}
	
	/**
	 * adds together the elements total property and total violent crimes to achieve the total crimes
	 * @param r
	 * @return double value of total crimes for an element
	 */
	
	public static double crimeTotal(Report r) {
		return r.getData().getTotals().getProperty().getAll() + r.getData().getTotals().getViolent().getAll();
	}
	
	/**
	 * prints the category header for crime stats or table
	 */
	
	public static void printHeader()	{
		String[] categories = new String[] {"Assault", "Murder", "Rape", "Robbery", "Burglary", "Larceny", "Motor", "Total"};
		
		System.out.print("Year");
		for (int i = 0; i < categories.length; i++) System.out.printf("%15s", categories[i]);
		 System.out.print("\n----");
		for (int i = 0; i < categories.length; i++) System.out.printf("%15s","--------");
	}
	
	/**
	 * prints out the full statistics of the Report element passed in
	 * formats to fit into a table of data
	 * @param list
	 * @param r
	 */
	
	public static void printStats(ArrayList<Report> list, Report r) {
		System.out.print("\n" + r.getYear());
		System.out.printf("%15s", r.getData().getTotals().getViolent().getAssault());
		System.out.printf("%15s", r.getData().getTotals().getViolent().getMurder());
		System.out.printf("%15s", r.getData().getTotals().getViolent().getRape());
		System.out.printf("%15s", r.getData().getTotals().getViolent().getRobbery());
		System.out.printf("%15s", r.getData().getTotals().getProperty().getBurglary());
		System.out.printf("%15s", r.getData().getTotals().getProperty().getLarceny());
		System.out.printf("%15s", r.getData().getTotals().getProperty().getMotor());
		System.out.printf("%15s", crimeTotal(r));
		System.out.println();
	}
	
	/**
	 * prints out survival tips some useful most comedic
	 * @return
	 */
	
	public static void randomTips(ArrayList<Report> list) {
		Random rand = new Random();
		
		String[] sug = new String[] {"If you ever get a flat tire, take a picture of it on your phone so for future reference you can use it as a valid excuse.",
				"If you find a hair in your food, heavily salt it before sending it back to the kitchen to make sure you got a new order.",
				"Want kids to behave on road trips? Bring a bag of candy. Anytime they misbehave, throw a piece of candy out the window",
				"Accidentally text the wrong person? Immediately put your phone on airplane mode and once it fails to deliver, delete the message!",
				"If you're too embarrassed to buy something, get a birthday card with it.",
				"If you ever have to park in a city at night, park in front of a bank. \n" + 
				"They're always lit up and have cameras everywhere.",
				"Can't decide if you're hungry? Ask yourself if you \n" + 
				"want an apple. If your answer is \"no\" then you're\n" + 
				"probably bored instead of hungry.",
				"If you ever get stuffed in a trunk, disconnect the \n" + 
				"backlight wires. When a cop pulls them over, kick the door\n" + 
				"so they know you are there.", "Never say sorry to the other driver after an accident that \n" + 
						"wasn't your fault. It's an admission of fault and could be \n" + 
						"used against you in court.",
						""};
		
		System.out.println("Random Tip: " + sug[rand.nextInt(sug.length-1)] + "\n");
		choice(list);
	}
}
