package com.restaurant.rakshith;// Restaurant Homework
// Instructor-provided code.
// You SHOULD heavily modify this file to make it interface with your own classes.

//java util is imported to use collections here and not to use Scanner.
import java.io.*;
import java.util.*;

/**
 * This class represents the text user interface (UI) for the restaurant
 * program, allowing the user to view and manage the restaurant and its objects.
 * Scanner is used from com.restaurant.rakshith package and not from
 * java.util.* library.
 * @author Rakshith
 * @version 1.0.0
 */
public class RestaurantTextUI {
	// file name from which to read the restaurant data
	private static final String DEFAULT_RESTAURANT_FILENAME = "tables.txt";
    private  Restaurant restaurant ;
 	/**
	 * Constructs a new text user interface for managing a restaurant.
	 */
	public RestaurantTextUI() {
		restaurant = new Restaurant("Rakshith's Restaurant");
		System.out.println("Restaurant Simulator");
    }
	
	/**
	 * Reads the information about the restaurant from the default restaurant
	 * file.
	 * @return true if the data was read successfully; false if there were any errors
	 */
	public boolean readRestaurantData() {
		File restaurantFile = ValidInputReader.getValidFile(
				"File name for restaurant data [" + DEFAULT_RESTAURANT_FILENAME + "]?",
				DEFAULT_RESTAURANT_FILENAME);

		// TODO: read restaurant info from tables file;
		// return true if it was successful and false if not
        StringBuilder restaurantName = new StringBuilder();
		StringBuilder numbersSb = new StringBuilder();

		try {
			Scanner in = new Scanner(restaurantFile).useDelimiter("\\n");
			for (int i = 0; i < 2; i++) {
					if (i == 0){
					restaurantName.append(in.next());
					}
				else if (i == 1)
					numbersSb.append(in.next());
			}
			//If there was a name specified change it to that new name.
			restaurant.setName(restaurantName.toString());
		}catch (FileNotFoundException e) {
			// when there is an error reading the file,
			System.out.println("Unable to read restaurant data: File not Found.");
			e.printStackTrace();
			return false;
		}
		for (int i = 0; i < numbersSb.length(); i+=2)
			restaurant.addTable(new Table(++Table.index, Character.getNumericValue(numbersSb.charAt(i))));

		return true;
    }
	
	/**
	 * Displays the main menu of choices and prompts the user to enter a choice.
	 * Once a valid choice is made, initiates other code to handle that choice.
	 */
	public void mainMenu() {
		// main menu
		displayOptions();
		while (true) {
			String choice = ValidInputReader.getValidString(
					"Main menu, enter your choice:",
					"^[sSaAdDrRpPtTcCwWqQ!?]$").toUpperCase();
			if (choice.equals("S")) {
				serversOnDuty();
			} else if (choice.equals("A")) {
					addServer();
			} else if (choice.equals("D")) {
				dismissServer();
			} else if (choice.equals("R")) {
				cashRegister();
			} else if (choice.equals("P")) {
				partyToBeSeated();
			} else if (choice.equals("T")) {
				tableStatus();
			} else if (choice.equals("C")) {
				checkPlease();
			} else if (choice.equals("W")) {
				waitingList();
			} else if (choice.equals("Q")) {
				break;
			} else if (choice.equals("?")) {
				displayOptions();
			} else if (choice.equals("!")) {
				rickRoll();
			}
			System.out.println();
		}
	}
	
	// Displays the list of key commands the user can use.
	private void displayOptions() {
		System.out.println();
		System.out.println("Main System Menu");
		System.out.println("----------------");
		System.out.println("S)ervers on duty");
		System.out.println("A)dd server");
		System.out.println("D)ismiss server");
		System.out.println("R)egister");
		System.out.println("P)arty has arrived");
		System.out.println("T)ables status");
		System.out.println("C)heck, please");
		System.out.println("W)aiting list");
		System.out.println("?) display this menu of choices again");
		System.out.println("Q)uit");
	}
	
	// Called when S key is pressed from main menu.
	// Displays all servers who are currently working.
	private void serversOnDuty() {
		System.out.println("Servers currently on duty:");
		if(restaurant.getServers().size()==0)
            System.out.println("None");
        // TODO: display current servers, e.g.:
		// Servers #1 ($49.76 in total tips)
        int counter=1;
        for(Servers currentServer: restaurant.getServers().values()){
            System.out.println("Servers #"+counter+" ($"+
                    currentServer.getTips()+" in total tips)");
            counter++;
        }
	}
	
	// Called when A key is pressed from main menu.
	// Adds one more server to the system.
	private void addServer() {
		System.out.println("Adding a new server to our workforce:");

		// TODO: add server and display current count of servers, e.g.:
		// Current server count: 3
        Servers newServer= new Servers(++Servers.addServerIndex,true);
		restaurant.addServer(newServer);
        System.out.println("There are "+ restaurant.getServerCountOnDuty()
        + " Servers on duty");


    }
	
	// Called when D key is pressed from main menu.
	// Sends one server home for the night (if possible).
	private void dismissServer() {
		// when there are no servers,
        if(restaurant.getServers().size()==0)
		System.out.println("No servers to dismiss.");
		
		// when only one server remains with tables remaining,
        if(restaurant.getServers().size()==1)
            //Check if any table is occupied.
            //if the restaurant's emptyTable count is not equal to all the number of tables in restaurant
            //then any or many tables are occupied.
            if(restaurant.getEmptyTables()!=restaurant.getTables().size()) {
                    System.out.println("Sorry, the server cannot cash out now;");
                    System.out.println("there are still tables remaining and this is the only server left.");
                }
		// when the server is able to be dismissed,
        if(restaurant.getServers().size()!=0){
        Map.Entry<Integer,Servers> ServerTobeDismissed = restaurant.getServers().entrySet().iterator().next();
		System.out.println("Dismissing a server:");

		// TODO: cash out server and display current count of servers
		// Servers #2 cashes out with $47.95 in total tips.
        // Servers now available: 3

        System.out.println("Server #"+ServerTobeDismissed.getKey()+" cashes out with "
                +"$"+ ServerTobeDismissed.getValue().getTips()+ "in total tips.");

        //Assign the tables he was serving to the next server.
        Map<Integer,Table> tablesServed= new LinkedHashMap<>(ServerTobeDismissed.getValue().getTablesServed());
        restaurant.getServers().remove(ServerTobeDismissed.getKey());
        Map.Entry<Integer,Servers> nextServer  = restaurant.getServers().entrySet().iterator().next();
        nextServer.getValue().getTablesServed().putAll(tablesServed);

        System.out.println("Servers now available: "+ restaurant.getServerCountOnDuty());

        }
    }
	// Called when R key is pressed from main menu.
	// Displays how much money is in the restaurant's cash register.
	private void cashRegister() {
		System.out.println("Cash register:");

		// TODO: display total money earned, e.g.:
		// Total money earned = $877.50
        System.out.println("Total money earned = $"+restaurant.getCashRegister());
    }
	
	// Called when T key is pressed from main menu.
	// Displays the current status of all tables.
	private void tableStatus() {
		System.out.println("Tables status:");

		// TODO: show restaurant's table statuses, e.g.:
		// Table 5 (2-top): Jones party of 2 - Servers #2
		// Table 6 (4-top): empty

        for (Table currentTable: restaurant.getTables().values())
            System.out.println(currentTable);

 	}
	
	// Called when C key is pressed from main menu.
	// Helps process a party's check to leave the restaurant.
	private void checkPlease() {
		System.out.println("Send the check to a party that has finished eating:");
		String partyName = ValidInputReader.getValidString("Party's name?", "^[a-zA-Z '-]+$");
		
		// when such a party is sitting at a table in the restaurant,
		double subtotal = ValidInputReader.getValidDouble("Bill subtotal?", 0.0, 9999.99);
		double tip = ValidInputReader.getValidDouble("Tip?", 0.0, 9999.99);

        Table selectedTable=null;

		for (Table currentTable: restaurant.getTables().values())
		    if(currentTable.getParty().getName().equalsIgnoreCase(partyName))
                selectedTable = currentTable.clone();
            else{
                // when such a party is NOT sitting at a table in the restaurant,
                System.out.println("There is no party by that name.");
            }

        // TODO: give tip to server, e.g.:
		// Gave tip of $9.50 to Servers #2.
        try{
            if(selectedTable.getServer()!=null)
                selectedTable.getServer().setTips(tip);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

		// update restaurant's cash register, e.g.
        // Gave total of $39.75 to cash register.
        restaurant.addToCashRegister(0.1*subtotal+subtotal);
        selectedTable.setOccupiedStatus(false);
		System.out.println("Seating from waiting list:");
		// when a party on the waiting list can now be seated, e.g.:
		// Table 6 (6-top): Erickson party of 5 - Servers #2

        //Removes the Party element from waitList after shifting from WaitList
        //to Regular table. This is an infinite loop just to keep trying until
        //there is a party who gets mapped appropriately.
        //Ex: if the first party in the list was not mapped, then
        //it tries to map the second party and so on until there is a mapping or
        //there are no more elements in the waitList.

        Iterator<Map.Entry<String, Party>> iterator =  restaurant.getWaitList().entrySet().iterator();
        Map.Entry<String, Party> waitListElement = iterator.next();
        int counter=0;
        while(true){
            if(counter!=0)
            while (iterator.hasNext())
             waitListElement = iterator.next();
            if (restaurant.optimizedTableMapping(waitListElement.getValue())) {
                System.out.println("Party from waitingList was seated in the regular table.");
                restaurant.removeFromWaitList(waitListElement.getValue());
                break;
            }
            counter++;
        }
    }
	
	// Called when W key is pressed from main menu.
	// Displays the current waiting list, if any.
	private void waitingList() {
		System.out.println("Waiting list:");
		
		// TODO: show restaurant's waiting list, e.g.:
		// Johnson party of 7
		// Erickson party of 6

        if(restaurant.getWaitList().size()>0)
            for (Party currentWaitParty: restaurant.getWaitList().values())
                System.out.println(currentWaitParty);

        // when there is nobody on the waiting list,
        if(restaurant.getWaitList().size()==0)
            System.out.println("empty");

	}
	
	// Called when P key is pressed from main menu.
	// Helps seat a newly arriving party at a table in the restaurant.
	private void partyToBeSeated() {
		// when there are no servers,
        if(restaurant.getServers().size()==0) {
            System.out.println("Sorry, there are no servers here yet to seat this party");
            System.out.println("and take their orders.  Add servers and try again.");
        }
		// when there is at least one server,
		String partyName = ValidInputReader.getValidString("Party's name?", "^[a-zA-Z '-]+$");
		int partySize = ValidInputReader.getValidInt("How many people in the party?", 1, 99999);
		
		// when a duplicate party name is found,

        List<Table> ServedTables = new ArrayList<>(restaurant.getTables().values());
        List<Party> partiesServed = new ArrayList<>(restaurant.getWaitList().values());

        if(Party.checkRepeatedNames(ServedTables,partiesServed,partyName)) {
            System.out.println("We already have a party with that name in the restaurant.");
            System.out.println("Please try again with a unique party name.");
        }
		// TODO: try to seat this party
         Party currentParty = new Party(partyName,partySize);

		if(!restaurant.optimizedTableMapping(currentParty))
            // when all tables large enough to accommodate this party are taken,
            System.out.println("Sorry, there is no open table that can seat this party now.");

        if(restaurant.getBiggestTableSize()<partySize){
        // when the restaurant doesn't have any tables big enough to ever seat this party
          System.out.println("Sorry, the restaurant is unable to seat a party of this size.");
          boolean wait = ValidInputReader.getYesNo("Place this party onto the waiting list? (y/n)");
        // TODO: put this party on the waiting list
		if(wait)
		    restaurant.addToWaitList(currentParty);

	    }
	}
	
	
	// You know what this method does.
	private void rickRoll() {
		// TODO: tell you how I'm feeling; make you understand
		System.out.println("We're no strangers to love");
		System.out.println("You know the rules and so do I");
		System.out.println("A full commitment's what I'm thinking of");
		System.out.println("You wouldn't get this from any other guy");
		System.out.println("I just wanna tell you how I'm feeling");
		System.out.println("Gotta make you understand");
		System.out.println();
		System.out.println("Never gonna give you up");
		System.out.println("Never gonna let you down");
		System.out.println("Never gonna run around and desert you");
		System.out.println("Never gonna make you cry");
		System.out.println("Never gonna say goodbye");
		System.out.println("Never gonna tell a lie and hurt you");
	}
	
	// This helper is just put into the text UI code to mark places where you
	// will need to add or modify this file.  Crashes with a runtime exception.
	private void crash(String message) {
		// Math.random() < 10 will always be true;  so why is it there?
		// I can't just throw because Eclipse will then warn about dead code
		// for any code that occurs after a call to crash().
		// So I wrap the exception throw in an "opaque predicate" to fool it.
		if (Math.random() < 10) {
			throw new RuntimeException("Not yet implemented: " + message);
		}
	}
}
