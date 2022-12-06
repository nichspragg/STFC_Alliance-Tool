package allianceTool;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

import allianceTool.entity.Alliance;
import allianceTool.errorHandler.DbException;
import allianceToolDao.ToolDao;

public class ToolHelpers {

	protected Scanner scanner = new Scanner(System.in);
	NumberFormat c = NumberFormat.getInstance(Locale.US);
	String pattern = "###,###,###,###.###";
	DecimalFormat decimalFormat = new DecimalFormat(pattern);
	protected HashMap<Integer,String> teamNames = new HashMap<Integer,String>();
	protected List<String> allianceTags = new ArrayList <>();
	protected List<Integer> awardedIds = new ArrayList <>();
	
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
			
			private List<String> viewOperations = List.of(
					ANSI_YELLOW+"1)"+ANSI_RESET+" View Top 10 Alliances",
					ANSI_YELLOW+"2)"+ANSI_RESET+" View Alliance Teams",
					ANSI_YELLOW+"3)"+ANSI_RESET+" View Alliance Players",
					ANSI_YELLOW+"4)"+ANSI_RESET+" View Player Stats",
					ANSI_YELLOW+"5)"+ANSI_RESET+" View Player Awards",
					ANSI_CYAN+"6)"+ANSI_RESET+" Edit Mode",
					ANSI_RED+"0)"+ANSI_RESET+" Exit program");
			private List<String> editOperations = List.of(
					ANSI_CYAN+"1)"+ANSI_RESET+" Edit Alliance Data",
					ANSI_CYAN+"2)"+ANSI_RESET+" Edit Alliance Teams",
					ANSI_CYAN+"3)"+ANSI_RESET+" Add New Player",
					ANSI_CYAN+"4)"+ANSI_RESET+" Edit Player Data",
					ANSI_CYAN+"5)"+ANSI_RESET+" Edit Player Awards",
					ANSI_YELLOW+"6)"+ANSI_RESET+" View Mode",
					ANSI_RED+"0)"+ANSI_RESET+" Exit program");
			private List<String> playerSortOptions = List.of(
					"1) Player ID", "2) Player Name", "3) Level");
			private List<String> statsSortOptions = List.of(
					"1) Player ID", "2) Player Name", "3) Level", "4) Power", "5) Resources Mined", "6) PVP",
					"7) PVP Damage", "8) Kill\"Death Ratio", "9) PVE", "10)PVE Damage");
			protected List<String> teamEditOptions = List.of(
					"1) List Current Team Members", "2) Add New Player ", 
					"3) Move Player to/from another team", "4) Remove Player");
			protected List<String> awardEditOptions = List.of(
					"1) Grant Award to Player", "2) Remove Award from Player", 
					"3) Create new Award");
			
	protected void clearTeams() {
		teamNames.clear(); }
	protected void clearAlliances() {
		while(allianceTags.size()>0) {
			allianceTags.remove(0);
			} }
	protected void clearAwardedIDs() {
		while(awardedIds.size()>0) {
			awardedIds.remove(0);
			} }
	protected String sortOption() {
		String sortOption = "";
		
		int selection = getIntInput("Enter a menu selection");
				
		switch(selection) {
			case 1: //Player ID
				sortOption = "player_id";
				System.out.println(ANSI_YELLOW+ "\n*Sorting by Player ID*\n" +ANSI_RESET);
				break;
			case 2: //Player Name
				sortOption = "player_name";
				System.out.println(ANSI_YELLOW+ "\n*Sorting by Player Name*\n" +ANSI_RESET);
				break;
			case 3: //Level
				sortOption = "lvl DESC";
				System.out.println(ANSI_YELLOW+ "\n*Sorting by Player LVL*\n" +ANSI_RESET);
				break;
			case 4: //Power
				sortOption = "pwr DESC";
				System.out.println(ANSI_YELLOW+ "\n*Sorting by Player Power*\n" +ANSI_RESET);
				break;
			case 5: //Resources Mined
				sortOption = "resources_mined DESC";
				System.out.println(ANSI_YELLOW+ "\n*Sorting by Resources Mined*\n" +ANSI_RESET);
				break;
			case 6: //PVP
				sortOption = "pvp_total DESC";
				System.out.println(ANSI_YELLOW+ "\n*Sorting by PVP*\n" +ANSI_RESET);
				break;
			case 7: //PVP Damage
				sortOption = "pvp_damage DESC";
				System.out.println(ANSI_YELLOW+ "\n*Sorting by PVP Damage*\n" +ANSI_RESET);
				break;
			case 8: //Kill\Death Ratio
				sortOption = "kd_ratio DESC";
				System.out.println(ANSI_YELLOW+ "\n*Sorting by Kill/Death Ratio*\n" +ANSI_RESET);
				break;
			case 9: //PVE
				sortOption = "pve DESC";
				System.out.println(ANSI_YELLOW+ "\n*Sorting by PVE*\n" +ANSI_RESET);
				break;
			case 10: //PVE Damage
				sortOption = "pve_damage DESC";
				System.out.println(ANSI_YELLOW+ "\n*Sorting by PVE Damage*\n" +ANSI_RESET);
				break;
			} // end of switch
		return sortOption;
		}
	protected void printPlayerSortOptions() {
		System.out.println("\nHow do you want them sorted?");
		playerSortOptions.forEach(line -> System.out.println("  " + line));
		}
	protected void printStatsSortOptions() {
		System.out.println("\nHow do you want them sorted?");
		statsSortOptions.forEach(line -> System.out.println("  " + line));
		}
	protected void holdingPattern() {
		System.out.println("Press enter to continue...");
		scanner.nextLine();
		}
	private void printViewOperations() {
		System.out.println("\nThese are the available "+ANSI_YELLOW+"VIEW"+ANSI_RESET+" selections.");
		viewOperations.forEach(line -> System.out.println("  " + line));}
	protected int getUserSelectionView() {
		printViewOperations();
		Integer input = getIntInput("Enter a menu selection");
		return Objects.isNull(input) ? -1 : input;
		}
	private void printEditOperations() {
		System.out.println("\nThese are the available "+ANSI_CYAN+"EDIT"+ANSI_RESET+" selections.");
		editOperations.forEach(line -> System.out.println("  " + line));}
	protected int getUserSelectionEdit() {
		printEditOperations();
		Integer input = getIntInput("Enter a menu selection");
		return Objects.isNull(input) ? -1 : input;
		}
	private void printAwardOperations() {
		System.out.println("\nThese are the available "+ANSI_YELLOW+"AWARD"+ANSI_RESET+" selections.");
		awardEditOptions.forEach(line -> System.out.println("  " + line));}
	protected int getAwardSelectionView() {
		printAwardOperations();
		Integer input = getIntInput("Enter a menu selection");
		return Objects.isNull(input) ? -1 : input;
		}
	protected Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		if(Objects.isNull(input)) {
			return null; }
		try {
			input = input.replace(",","");
			return Integer.valueOf(input);
			}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
			}
		}
	protected Long getLongInput(String prompt) {
		String input = getStringInput(prompt);
		if(Objects.isNull(input)) {
			return null; }
		try {
			input = input.replace(",","");
			return Long.parseLong(input);
			}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
			}
		}
	protected String getStringInput(String prompt){
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		return input.isBlank() ? null : input.trim();
		}
	protected BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		if(Objects.isNull(input)) {
			return null; }
		try {
			return new BigDecimal(input).setScale(2);
			}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
			}
		}
	protected boolean isEmpty(String input){
		boolean done = false;
		if(input == null || input.length() < 1){
			done = true; }
		return done;
		}
	protected boolean isInt(String input){
		boolean done = false;
		if(input.matches("-?\\d+")) {
			done = true;
		} else { done = false; }
		return done;
		}
	protected static final String capitalize(String str){  
		if (str == null || str.length() == 0) return str;  
		return str.substring(0, 1).toUpperCase() + str.substring(1);  
		}  
	protected boolean getYnInput(String prompt){
		boolean done = false;
		boolean answer = false;
		while(!done) {
			System.out.print(prompt + "[Y/N]: ");
			String input = scanner.nextLine();
			if(input.length()>0) {
			char letter = input.charAt(0);
				if(letter == 'y' ) {
					answer = true;
					done = true; }
				else if (letter == 'n'){
					answer = false;
					done = true; }}
				else { System.out.println("Please entr Y or N");  }}
		return answer;
		}
	protected boolean exitMenu() {
		System.out.println("The end is neigh. Goodbye!");
		return true;
		}
	protected void printTable(String titles[],Map<Integer, Integer> titleLengths, String[][] table){
		boolean leftJustifiedRows = true;
		Map<Integer, Integer> dataLengths = new HashMap<>();
		Arrays.stream(table).forEach(a -> Stream.iterate(0, (i -> i < a.length), (i -> ++i)).forEach(i -> {
			if (dataLengths.get(i) == null) {
				dataLengths.put(i, 0); }
			if (dataLengths.get(i) < a[i].length()) {
				dataLengths.put(i, a[i].length()); }
			}));
		Map<Integer, Integer> columnLengths = new HashMap<>();
		for(int i=0; i<titleLengths.size(); i++) {
			if(titleLengths.get(i)>dataLengths.get(i)) {
				columnLengths.put(i, titleLengths.get(i)); }
			else { columnLengths.put(i, dataLengths.get(i)); }
			}
		final StringBuilder formatString = new StringBuilder("");
		String flag = leftJustifiedRows ? "-" : "";
		columnLengths.entrySet().stream().forEach(e -> formatString.append("| %" + flag + e.getValue() + "s "));
		formatString.append("|\n");

		System.out.printf(formatString.toString(), titles);
		Stream.iterate(0, (i -> i < table.length), (i -> ++i))
				.forEach(a -> System.out.printf(formatString.toString(), table[a]));
		}
} //end of all
