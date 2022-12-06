package allianceTool;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import allianceTool.entity.Alliance;
import allianceTool.entity.AwardList;
import allianceTool.entity.Awards;
import allianceTool.entity.Player;
import allianceTool.entity.Team;
import allianceToolDao.ToolDao;

public class ToolMain extends ToolHelpers{

	public static void main(String[] args) {
		new ToolMain().processViewSelections();
		
	} // end of main
	
	private void processViewSelections() {
		boolean done = false;
		while(!done) {
			try {
				int selection = getUserSelectionView();
				
				switch(selection) {
				case 0:
					done = exitMenu();
					break;
				case 1: //View Alliances
					viewAlliances();
					holdingPattern();
					break;
				case 2: //View Teams
					viewTeams();
					break;
				case 3: //View Players
					int idOrAll = getIntInput("\nWould you like to view " +ANSI_GREEN+ "(1)" +ANSI_RESET+ " a single player,"
							+ " or " +ANSI_GREEN+ "(2)" +ANSI_RESET+ " all players?");
					switch(idOrAll) {
					case 1:
						int pId = getPlayerId();
						String stringId = Integer.toString (pId);
						List<Player> players = ToolDao.findPlayerInfo(stringId, 4);
						viewStats(players);
						viewAwardedPlayer(pId);
						holdingPattern();
						break;
					case 2:
						viewAllPlayers();
						break;
						}
					break;
				case 4: //View Stats
					printStatsSortOptions();
					String sortOption = sortOption();
					List<Player> players = ToolDao.viewPlayers(sortOption);
					System.out.println(ANSI_BLUE_BACKGROUND + "\nAll Players" + ANSI_RESET);
					viewStats(players);
					break;
				case 5: //View Awards
					idOrAll = getIntInput("\nWould you like to view awards for " +ANSI_GREEN+ "(1)" +ANSI_RESET+ " a single player,"
							+ " or " +ANSI_GREEN+ "(2)" +ANSI_RESET+ " all players?");
					switch(idOrAll) {
						case 1:
							int playerId = getPlayerId();
							System.out.println("");
							viewAwardedPlayer(playerId);
							holdingPattern();
							break;
						case 2:
							viewAllAwarded();
							break;
							}
					break;
				case 6: //Edit Mode
					done = true;
					processEditSelections();
					break;
				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
				
				} //end of switch
			}
			catch(Exception e){
				System.out.println("\nError: " + e + " Try again.");
				e.printStackTrace();
				}
			} //end of while
		}

	private void processEditSelections() {
		boolean done = false;
		while(!done) {
			try {
				int selection = getUserSelectionEdit();
				
				switch(selection) {
				case 0:
					done = exitMenu();
					break;
				case 1: //Edit Alliance Data
					editAlliance();
					break;
				case 2: //Edit Teams
					editTeams();
					break;
				case 3: //Add Player Data
					checkPlayer(1);
					break;
				case 4: //Edit Player Data
					checkPlayer(2);
					break;
				case 5: //Edit Awards
					editAwards();
					break;
				case 6: //View Mode
					done = true;
					processViewSelections();
					break;
				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
				} //end of switch
			}
			catch(Exception e){
				System.out.println("\nError: " + e + " Try again.");
				e.printStackTrace();
				}
			} //end of while
		}
	

	protected void viewAlliances() {
		clearAlliances();
		List<Alliance> alliances = ToolDao.viewAlliances();
		
		System.out.println(ANSI_BLUE_BACKGROUND + "\n Alliances" + ANSI_RESET);
		alliances.forEach(alliance -> {
			allianceTags.add(alliance.getAllianceTag());});
		int count = alliances.size();
		String titles[] = new String[] {"Alliance Name", "Members", "Leader", "Power", "Updated"};
		Map<Integer, Integer> titleLengths = new HashMap<>();
			titleLengths.put(0, 13);
			titleLengths.put(1, 7);
			titleLengths.put(2, 6);
			titleLengths.put(3, 5);
			titleLengths.put(4, 7);
		String [][] allArrays = new String[count][];
		
		for(int i = 0; i<count ; i++) {
			Alliance alliance = alliances.get(i);
			String tag = alliance.getAllianceTag();
			String name = alliance.getAllianceName();
			String tagName = "[" + tag + "] " + name;
			String members = Integer.toString(alliance.getNumMembers());
			
			String power = decimalFormat.format(alliance.getAlliancePower());
			String date = alliance.getAllianceUpdated().toString();
			allArrays[i] = new String[] {
				tagName, members, alliance.getAllianceLeader(), power, date}; 
				}
		printTable(titles, titleLengths, allArrays);
		}
	protected void viewTeams() {
		clearTeams();
		
		List<Team> teams = ToolDao.viewTeams();
		
		System.out.println(ANSI_BLUE_BACKGROUND + "\nTeams: " + ANSI_RESET);
		teams.forEach(team -> {
			teamNames.put(team.getTeamId(), team.getTeamName());});
		int count = teams.size();
		String titles[] = new String[] {"Team #", "Team Name", "Refine", "Two System Defense", "Three System Defense"};
		Map<Integer, Integer> titleLengths = new HashMap<>();
			titleLengths.put(0, 6);
			titleLengths.put(1, 9);
			titleLengths.put(2, 6);
			titleLengths.put(3, 18);
			titleLengths.put(4, 20);
		String [][] teamArrays = new String[count][];
		
		for(int i = 0; i<count ; i++) {
			Team team = teams.get(i);
			String num = String.valueOf(team.getTeamId());
			teamArrays[i] = new String[] { num,
				team.getTeamName(), team.getTeamRefine(),
				team.getTwoSystemDefense(), team.getThreeSystemDefense() }; 
				}
		printTable(titles, titleLengths, teamArrays);
		fetchTeam(count, teamArrays);
		}
	private void fetchTeam(int count, String[][] teamArrays) {
		boolean done = false;
		while(!done) {
		String teamNum = getStringInput("Enter a team # to view members, or press ENTER to exit");
		if(isEmpty(teamNum)){
			done = true; }
		else if(isInt(teamNum)) {		
			int tNum = Integer.parseInt(teamNum);
			if(tNum>0 && teamNames.containsKey(tNum)) {
				String teamName = (String)teamNames.get(tNum);
				List<Player> players = ToolDao.fetchTeamMembers(tNum, teamName);
				viewBasic(players);
				done = true;
			}} else {System.out.println(ANSI_RED + "\nValid team number not entered. Please check list provided." + ANSI_RESET);} 
		} }
	protected void viewAllPlayers() {
		printPlayerSortOptions();
		String sortOption = sortOption();
		List<Player> players = ToolDao.viewPlayers(sortOption);
		
		viewBasic(players);
		}
	protected void viewBasic(List<Player> players) {
		System.out.println(ANSI_BLUE_BACKGROUND+ "Players:" +ANSI_RESET);

		int count = players.size();
		String titles[] = new String[] {"ID", "Name", "Level"};
		Map<Integer, Integer> titleLengths = new HashMap<>();
			titleLengths.put(0, 2);
			titleLengths.put(1, 4);
			titleLengths.put(2, 5);

		String [][] allArrays = new String[count][];
		
		for(int i = 0; i<count ; i++) {
			Player player = players.get(i);
			String iD = decimalFormat.format(player.getPlayerId());
			String lvl = decimalFormat.format(player.getLvl());
			allArrays[i] = new String[] {
				iD, player.getPlayerName(), lvl}; 
				}
		printTable(titles, titleLengths, allArrays);
			holdingPattern();
		}
	protected void viewStats(List<Player> players) {
		int count = players.size();
		String titles[] = new String[] {"ID", "Name", "Level", "Power", "RSS Mined", "PVP", "PVP Damage", "K/D Ratio", "PVE", "PVE Damage", "Updated"};
		Map<Integer, Integer> titleLengths = new HashMap<>();
			titleLengths.put(0, 2);
			titleLengths.put(1, 4);
			titleLengths.put(2, 5);
			titleLengths.put(3, 5);
			titleLengths.put(4, 9);
			titleLengths.put(5, 3);
			titleLengths.put(6, 10);
			titleLengths.put(7, 9);
			titleLengths.put(8, 3);
			titleLengths.put(9, 10);
			titleLengths.put(10, 7);

		String [][] allArrays = new String[count][];

		for(int i = 0; i<count ; i++) {
			Player player = players.get(i);
			String iD = decimalFormat.format(player.getPlayerId());
			String lvl = decimalFormat.format(player.getLvl());
			String pwr = decimalFormat.format(player.getPwr());
			String rss = decimalFormat.format(player.getResourcesMined());
			String pvp = decimalFormat.format(player.getPvpTotal());
			String pvpD = decimalFormat.format(player.getPvpDamage());
			String kdR = decimalFormat.format(player.getKdRatio());
			String PVE = decimalFormat.format(player.getPve());
			String date = player.getPlayerUpdated().toString();
			allArrays[i] = new String[] {iD, player.getPlayerName(), lvl, pwr, rss, pvp, pvpD, kdR, PVE, player.getPveDamage(), date};
			}
		printTable(titles, titleLengths, allArrays);
	//	holdingPattern();
		}
	private void editAlliance() {
		clearAlliances();
		List<Alliance> alliances = ToolDao.viewAlliances();
		System.out.println(ANSI_BLUE_BACKGROUND + "\n Alliances" + ANSI_RESET);
		alliances.forEach(alliance -> {
			allianceTags.add(alliance.getAllianceTag());});
		System.out.println(allianceTags + "\n");
		String allianceTag = getStringInput("Enter alliance tag you wish to edit. (Exaple: SNW)");
		allianceTag = allianceTag.toUpperCase();
		if(allianceTag==null || allianceTag.length()<1 ) {
			System.out.println("No input provided."); }
		else if(allianceTags.contains(allianceTag)) {
			fetchAllianceDetailsByTag(allianceTag);
		
			Alliance update = ToolDao.fetchAllianceByTag(allianceTag);
			
			System.out.println("\nOnly new information is needed. If no change, press ENTER.");
			String allianceTagNew = getStringInput("Enter the alliance tag [" + update.getAllianceTag() + "]");
				update.setAllianceTag(Objects.isNull(allianceTagNew) ? update.getAllianceTag() : allianceTagNew.toUpperCase());
				String lead = update.getAllianceLeader();
				if(allianceTagNew != null) {
					ToolDao.modifyAllianceTag(allianceTagNew.toUpperCase(), lead); }
			String allianceName = getStringInput("Enter the alliance name [" + update.getAllianceName() + "]");
				update.setAllianceName(Objects.isNull(allianceName) ? update.getAllianceName() : allianceName);				
			String allianceLeader = getStringInput("Enter the alliance leader [" + update.getAllianceLeader() + "]");
				update.setAllianceLeader(Objects.isNull(allianceLeader) ? update.getAllianceLeader() : allianceLeader);
			String numMembers = getStringInput("Enter the alliance member count [" + update.getNumMembers() + "]");
				update.setNumMembers(Objects.isNull(numMembers) ? update.getNumMembers() : Integer.valueOf(numMembers));		
			long alliancePower = getLongInput("Enter the alliance power [" + decimalFormat.format(update.getAlliancePower()) + "]");
						update.setAlliancePower(Objects.isNull(alliancePower) ? update.getAlliancePower() : alliancePower);	
			
			ToolDao.modifyAllianceDetails(update);
			System.out.println("");
			fetchAllianceDetailsByTag(update.getAllianceTag());
		//		scanner.nextLine();
				holdingPattern();
			} else {System.out.println(ANSI_RED + "\nValid alliance tag not entered. Please check spelling from list provided." + ANSI_RESET);}
		}
	private void fetchAllianceDetailsByTag(String allianceTag) {
				Alliance fetch = ToolDao.fetchAllianceByTag(allianceTag);
				System.out.println("[" +ANSI_PURPLE+ fetch.getAllianceTag() + "] " +ANSI_RESET
						+ANSI_PURPLE+ fetch.getAllianceName() +ANSI_RESET
						+ "\t Leader: " +ANSI_PURPLE+ fetch.getAllianceLeader() +ANSI_RESET
						+ "\t Members: " +ANSI_PURPLE+ fetch.getNumMembers() +ANSI_RESET
						+ "\t Power: " +ANSI_PURPLE+ decimalFormat.format(fetch.getAlliancePower()) +ANSI_RESET
						+ "\t Updated: " +ANSI_PURPLE+ fetch.getAllianceUpdated() +ANSI_RESET	);
		}
	private void editTeams() {
		clearTeams();
				
		List<Team> teams = ToolDao.viewTeams();
		System.out.println(ANSI_BLUE_BACKGROUND + "\n Teams" + ANSI_RESET);
		teams.forEach(team -> {
			teamNames.put(team.getTeamId(), team.getTeamName());});
		System.out.println(teamNames + "\n");
		String teamNum = getStringInput("Enter a " +ANSI_BLUE_BACKGROUND+ "team #" +ANSI_RESET+ " you wish to edit \nor enter 0 for unassigned members");
		
		if(isEmpty(teamNum)) {
			System.out.println("No input provided."); }
		else if (isInt(teamNum)){
			int num = Integer.parseInt(teamNum);
				if(num >0 && teamNames.containsKey(num)) {
					int selection = getIntInput("Would you like to edit " +ANSI_GREEN+ "(1)" +ANSI_RESET+ " Team data, or "
							+ANSI_GREEN+ "(2)" +ANSI_RESET+ " Team members");
					switch(selection) {
					case 1:
						modifyTeamDuties(teams, num);
						holdingPattern();
						break;
					case 2:
						modifyTeamMembers(teams, num);
						break;
					default:
						break;
						}
				} else if(num == 0) {
					List<Player> players = ToolDao.fetchNullTeamMembers("not assigned");
					viewBasic(players);
				}
				else {System.out.println(ANSI_RED+ "\nValid team # not entered. Please check list provided." +ANSI_RESET);}
			} else {System.out.println(ANSI_RED+ "\nValid team # not entered. Please check list provided." +ANSI_RESET);}
		}
	private void modifyTeamMembers(List<Team> teams, int teamNum) {
		boolean done = false;
		boolean check;
		Team team = teams.get(teamNum-1);
		String teamName = team.getTeamName();
		while(!done) {
			try {
				System.out.println("\nWhich option do you choose for team "+ANSI_BLUE_BACKGROUND+ teamName +ANSI_RESET+"?");
		teamEditOptions.forEach(line -> System.out.println("  " + line));
		String select = getStringInput("  or press ENTER to go back");
		if(isEmpty(select)) {
			done = true; }
		else if (isInt(select)){
			int selection = Integer.parseInt(select);
			System.out.println("");
		switch(selection) {
		case 1: //List Current Team Members
			List<Player> players = ToolDao.fetchTeamMembers(teamNum, teamName);
			viewBasic(players);
			break;
		case 2: //Add New Player
			check = getYnInput("Do you know the player ID?");
				if(check) {
					addPlayertoTeam(teamNum, teamName);
				} else {
					findPlayerInfo(2); 
					check = getYnInput("\nIs the desired player listed?");
					if(check) {
						addPlayertoTeam(teamNum, teamName);
					} }
			break;
		case 3: //Move Player to/from another team
			check = getYnInput("Do you know the player ID?");
			if(check) {
				movePlayer(teams, teamNum, teamName);
			} else {
				findPlayerInfo(2); 
				check = getYnInput("\nIs the desired player listed?");
				if(check) {
					movePlayer(teams, teamNum, teamName);
				} }
			break;
		case 4: //Remove Player
			check = getYnInput("Do you know the player ID?");
			if(check) {
				removePlayerFromTeam();
			} else {
				findPlayerInfo(2); 
				check = getYnInput("\nIs the desired player listed?");
				if(check) {
					removePlayerFromTeam();
				} }
			break;
		default:
			break;
		}}//end of switch
		}//end of try
			catch(Exception e){
				System.out.println("\nError: " + e + " Try again.");
				e.printStackTrace();
				}
			}//end of while
	}//end of method
	private void removePlayerFromTeam() {
		int playerId = getIntInput("Enter player ID");
		Player player = ToolDao.fetchPlayer(playerId);
		boolean check = getYnInput("Remove " +ANSI_YELLOW+ player.getPlayerName() +ANSI_RESET+ 
				" from their current team?");
		if(check) {
			ToolDao.removePlayerFromTeam(playerId);
			}
		}
	private void movePlayer(List<Team> teams, int teamNum, String teamName) {
		int playerId = getIntInput("Enter player ID");
		Player player = ToolDao.fetchPlayer(playerId);
		boolean check = getYnInput("Move " +ANSI_YELLOW+ player.getPlayerName() +ANSI_RESET+
				" from their current team?");
		if(check) {
			teams.forEach(team -> {
				teamNames.put(team.getTeamId(), team.getTeamName());});
			System.out.println("\n" + teamNames + "\n");
			String teamNum2 = getStringInput("Enter the team # you wish to move to");
				if(isEmpty(teamNum2)) {
					System.out.println("No input provided."); }
				else if (isInt(teamNum2)){
					int num2 = Integer.parseInt(teamNum2);
					if(num2 >0 && num2 <= teams.size()) {
					Team team2 = teams.get(num2-1);
					String teamName2 = team2.getTeamName();
					boolean check2 = getYnInput("Move " +ANSI_YELLOW+ player.getPlayerName() +ANSI_RESET+ 
							" to team " +ANSI_BLUE_BACKGROUND+ teamName2 +ANSI_RESET+ "?");
					if(check2) {
						System.out.println("");
						ToolDao.removePlayerFromTeam(playerId);
						ToolDao.addPlayerToTeam(num2, playerId, teamName2);
					}
					} else {System.out.println(ANSI_RED+ "\nValid team # not entered. Please check list provided." +ANSI_RESET);}
				} else {System.out.println(ANSI_RED+ "\nValid team # not entered. Please check list provided." +ANSI_RESET);}
			}
		}
	private void addPlayertoTeam(int teamNum, String teamName) {
		int playerId = getIntInput("Enter player ID");
		Player player = ToolDao.fetchPlayer(playerId);
		boolean check = getYnInput("Add " +ANSI_YELLOW+ player.getPlayerName() +ANSI_RESET+
				" to team " +ANSI_BLUE_BACKGROUND+ teamName +ANSI_RESET+ "?");
		if(check) {
			ToolDao.addPlayerToTeam(teamNum, playerId, teamName);
			}
		}
	private void findPlayerInfo(int selector) {
		String playerName = getStringInput("Enter all or part of player name to lookup");
		List<Player> players = ToolDao.findPlayerInfo(playerName, selector);
		int count = players.size();
		// selector 1 from view menu / players (DAO only returns active players)
		if(selector==1) {
			System.out.println(ANSI_BLUE_BACKGROUND+ "\nPlayers with \"" + playerName +"\"" +ANSI_RESET);
			viewStats(players);
			}
		// selector 2 from editPlayer & getPlayerId (DAO only returns active players)
		if(selector==2) {
			for(int i = 0; i<count ; i++) {
				Player player = players.get(i);
				System.out.println("[ID:" + player.getPlayerId() + "] " + player.getPlayerName() + "\t LVL: " + player.getLvl()  );
				}
			}
		// selector 3 from checkPlayer (DAO returns all)
		if(selector==3) {
			for(int i = 0; i<count ; i++) {
				Player player = players.get(i);
				System.out.println("[ID:" + player.getPlayerId() + "] " + player.getPlayerName() + "\t LVL: " + player.getLvl()  );
				}
			}
		// selector 4 from addPlayer (id as string) (DAO returns all)
		if(selector==4) {
			System.out.println(ANSI_BLUE_BACKGROUND+ "\nPlayers with \"" + playerName +"\"" +ANSI_RESET);
			viewStats(players);
			}
		}
	private void modifyTeamDuties(List<Team> teams, int num) {
			Team update = ToolDao.fetchTeamDuties(num);
		System.out.println("[" +ANSI_PURPLE+ update.getTeamName() +ANSI_RESET+ "] " 
				+ "\t Refine: " +ANSI_PURPLE+ update.getTeamRefine() +ANSI_RESET 
				+ "\t Two System Defense: " +ANSI_PURPLE+ update.getTwoSystemDefense() +ANSI_RESET
				+ "\t Three System Defense: " +ANSI_PURPLE+ update.getThreeSystemDefense() +ANSI_RESET);
		
		System.out.println("\nOnly " +ANSI_BLUE_BACKGROUND+ "NEW" +ANSI_RESET+ " information is needed. If no change, press ENTER.");
		
		String teamName = getStringInput("Enter the team name [" + update.getTeamName() + "]");
			update.setTeamName(Objects.isNull(teamName) ? update.getTeamName() : capitalize(teamName));	
		String teamRefine = getStringInput("Enter the refine assignment [Emitters/Diodes/Reactors/Swing]");
			update.setTeamRefine(Objects.isNull(teamRefine) ? update.getTeamRefine() : capitalize(teamRefine));				
		String twoSystemDefense = getStringInput("Enter the 2-system defense [Alpha/Beta]");
			update.setTwoSystemDefense(Objects.isNull(twoSystemDefense) ? update.getTwoSystemDefense() : capitalize(twoSystemDefense));
		String threeSystemDefense = getStringInput("Enter the 3-system defense [Alpha/Beta/Gamma]");
		update.setThreeSystemDefense(Objects.isNull(threeSystemDefense) ? update.getThreeSystemDefense() : capitalize(threeSystemDefense));		
		
		ToolDao.modifyTeamDuties(update);
		
		Team updated = ToolDao.fetchTeamDuties(num);
		System.out.println("\n Updated info: \n"
				+ "[" +ANSI_PURPLE+ updated.getTeamName() +ANSI_RESET+ "] " 
				+ "\t Refine: " +ANSI_PURPLE+ updated.getTeamRefine() +ANSI_RESET
				+ "\t Two System Defense: " +ANSI_PURPLE+ updated.getTwoSystemDefense() +ANSI_RESET
				+ "\t Three System Defense: " +ANSI_PURPLE+ updated.getThreeSystemDefense() +ANSI_RESET);
		}
	private void checkPlayer(int i) {
		boolean check = false;
		if(i==1) {
			check = getYnInput("Has this player been in the alliance before?");
			if(check) {
				System.out.println("\nPlease check complete player list before adding (they may have an ID already)");
				findPlayerInfo(3);
				boolean check2 = getYnInput("\nIs the desired player listed?");
					if(check2) {
						editPlayer(getIntInput("Enter player ID"), null, 0);
						} else { addPlayer(); }
				} else {
					addPlayer();
				}
			}
		if(i==2) {
			check = getYnInput("Do you know the player ID?");
			if(check) {
				int playerId = getIntInput("Enter player ID");
				String stringId = Integer.toString (playerId);
				List<Player> players = ToolDao.findPlayerInfo(stringId, 4);
				viewStats(players);
				boolean checkRemove = getYnInput("Do you want to remove the player?");
				if(checkRemove) {
					removePlayer(playerId);
					} else { editPlayer(playerId, null, 0); }
			} else {
				findPlayerInfo(2); 
				check = getYnInput("\nIs the desired player listed?");
				if(check) {
					int playerId = getIntInput("Enter player ID");
					String stringId = Integer.toString (playerId);
					List<Player> players = ToolDao.findPlayerInfo(stringId, 4);
					viewStats(players);
					boolean checkRemove = getYnInput("Do you want to remove the player?");
					if(checkRemove) {
						removePlayer(playerId);
						} else { editPlayer(playerId, null, 0); }
				} }
			}
		}
	private void addPlayer() {
		Player newPlayer = new Player();
		newPlayer.setAllianceTag((getStringInput("Enter alliance Tag (ex: SNW)")).toUpperCase());
		newPlayer.setPlayerName(getStringInput("Enter player name"));
		newPlayer.setLvl(getIntInput("Enter player level"));
		
		int newId = ToolDao.addNewPlayer(newPlayer);
		newPlayer.setPlayerId(newId);

		editPlayer(newId, newPlayer, 1);
		
		}
	private void editPlayer(int newId, Player newPlayer, int i) {
		Player updatePlayer = new Player();
		String allianceTag = null;
		String playerName = null;
		if(i==1) {
			updatePlayer = newPlayer;
		} else {
			updatePlayer = ToolDao.fetchPlayer(newId);
			allianceTag = getStringInput("Enter the player alliance [if different]");
			playerName = getStringInput("Enter the player name [if different]");
			Integer lvl = getIntInput("Player level [current]");
			updatePlayer.setLvl(Objects.isNull(lvl) ? updatePlayer.getLvl() : lvl);
			}
		
		updatePlayer.setAllianceTag(Objects.isNull(allianceTag) ? updatePlayer.getAllianceTag() : allianceTag.toUpperCase());
		updatePlayer.setPlayerName(Objects.isNull(playerName) ? updatePlayer.getPlayerName() : playerName);
		int power = getIntInput("Enter player power");
		updatePlayer.setPwr(Objects.isNull(power) ? updatePlayer.getPwr() : power);
		Long resourcesMined = getLongInput("Enter resources mined");
		updatePlayer.setResourcesMined(Objects.isNull(resourcesMined) ? updatePlayer.getResourcesMined() : resourcesMined);
		int pvpTotal = getIntInput("Enter PVP Total");
		updatePlayer.setPvpTotal(Objects.isNull(pvpTotal) ? updatePlayer.getPvpTotal() : pvpTotal);
		Long pvpDamage = getLongInput("Enter PVP Damage");
		updatePlayer.setPvpDamage(Objects.isNull(pvpDamage) ? updatePlayer.getPvpDamage() : pvpDamage);
		BigDecimal kdRatio = getDecimalInput("Enter kill/death ratio");
		updatePlayer.setKdRatio(Objects.isNull(kdRatio) ? updatePlayer.getKdRatio() : kdRatio);
		int pve = getIntInput("Enter PVE Total");
		updatePlayer.setPve(Objects.isNull(pve) ? updatePlayer.getPve() : pve);
		String pveDamage = getStringInput("Enter PVE Damage");
		updatePlayer.setPveDamage(Objects.isNull(pveDamage) ? updatePlayer.getPveDamage() : pveDamage);
		
		boolean success = ToolDao.updatePlayerDetails(updatePlayer);
		if(success) {
			String stringId = Integer.toString (newId);
			List<Player> players = ToolDao.findPlayerInfo(stringId, 4);
			viewStats(players);
			}
		}
	private void removePlayer(int playerId) {
		Player player = ToolDao.fetchPlayer(playerId);
		boolean checkInactive = getYnInput("Do you want to set " + player.getPlayerName() + " as inactive?");
		if(checkInactive) {
			boolean checkAgain = getYnInput("Are you sure you want to set [" + player.getAllianceTag() + "] "
					+ANSI_PURPLE+ANSI_WHITE_BACKGROUND+ player.getPlayerName() +ANSI_RESET+ " to "
					+ANSI_RED+ANSI_WHITE_BACKGROUND+"inactive status"+ANSI_RESET+"?");
				if(checkAgain) {
					boolean inactive = ToolDao.setInactive(playerId); 
					if(inactive) {System.out.println("Player: [" + player.getAllianceTag() +"] "
							+player.getPlayerName()+ " was set to inactive status");
					holdingPattern();}
				}
		} else { 
			boolean checkDelete = getYnInput("Do you want to delete " + player.getPlayerName() + " from the database?"); 
			if(checkDelete) {
				boolean checkAgain = getYnInput("Are you sure you want to "+ANSI_RED+ANSI_WHITE_BACKGROUND+"delete"+ANSI_RESET
						+" [" + player.getAllianceTag() + "] "
						+ANSI_PURPLE+ANSI_WHITE_BACKGROUND+ player.getPlayerName() +ANSI_RESET+ " from the database?");
				if(checkAgain) {
					boolean deleted = ToolDao.deletePlayer(playerId); 
					if(deleted) {System.out.println("Player: [" + player.getAllianceTag() +"] "
							+player.getPlayerName()+ " was deleted");
					holdingPattern();}
			}}}
		
		}
	private void viewAllAwarded() {
		List<AwardList> awarded = ToolDao.viewAllAwarded();
		System.out.println(ANSI_BLUE_BACKGROUND + "\n Awards" + ANSI_RESET);
		int count = awarded.size();
		String titles[] = new String[] {"Award", "Player"};
		Map<Integer, Integer> titleLengths = new HashMap<>();
			titleLengths.put(0, 5);
			titleLengths.put(1, 6);
		String [][] allArrays = new String[count][];
		String placeHolder = null;
		for(int i = 0; i<count ; i++) {
			AwardList award = awarded.get(i);
			String awardName = award.getAwardName();
			String playerName = award.getPlayerName();
			if(awardName.equals(placeHolder)) {
				allArrays[i] = new String[] {"", playerName}; 
			} else {
				allArrays[i] = new String[] {awardName, playerName}; 
				placeHolder = awardName;
			} }
		printTable(titles, titleLengths, allArrays);
		holdingPattern();
		}
	private void viewAwardedPlayer(int playerId) {
		if(checkIfAwarded(playerId)) {
			List<AwardList> awarded = ToolDao.viewAwardedId(playerId);
			int count = awarded.size();;
			String titles[] = new String[] {"Award ID", "Award Name"};
			Map<Integer, Integer> titleLengths = new HashMap<>();
				titleLengths.put(0, 8);
				titleLengths.put(1, 10);
			String [][] allArrays = new String[count][];
			for(int i = 0; i<count ; i++) {
				AwardList award = awarded.get(i);
				allArrays[i] = new String[] {
						String.valueOf(award.getAwardId()), award.getAwardName()}; 
				}
			AwardList award = awarded.get(0);
			System.out.println(ANSI_BLUE_BACKGROUND + "\n Awards for " + award.getPlayerName() + ANSI_RESET);
			printTable(titles, titleLengths, allArrays);
		} else { System.out.println("\n" +ANSI_BLUE_BACKGROUND+ "No awards for player with ID: " + playerId +ANSI_RESET); }
		}
	private boolean checkIfAwarded(int playerId) {
		clearAwardedIDs();
		List<AwardList> awarded = ToolDao.viewAllAwarded();
		awarded.forEach(award -> {
			awardedIds.add(award.getPlayerId());});
		boolean containsId = awardedIds.contains(playerId);
		return containsId;
	}
	private int getPlayerId() {
		boolean check = getYnInput("Do you know the player ID?");
		int playerId = 0;
		if(check) {
			playerId = getIntInput("Enter player ID"); 
			} else { findPlayerInfo(2); 
				check = getYnInput("\nIs the desired player listed?");
				if(check) {playerId = getIntInput("Enter player ID"); }
				}
		return playerId;
		}
	private void editAwards() {
		int selection = getAwardSelectionView();
		switch(selection) {
		case 1: //Grant Award to Player
			grantAward();
			break;
		case 2: //Remove Award from Player
			removeAward();
			break;
		case 3: //Create new Award
			createNewAward();
			break;
			}
		}
	private void createNewAward() {
		String newAward = getStringInput("\nEnter the Name of the new award");
		if(ToolDao.createNewAward(newAward)) {
			System.out.println("Award " +ANSI_GREEN+ newAward +ANSI_RESET+ " created");
			};
		}
	private void grantAward() {
		int playerId = getPlayerId();
		Player player = ToolDao.fetchPlayer(playerId);
		List<Awards> awards = ToolDao.viewAllAwards();
		
		int count = awards.size();
		String titles[] = new String[] {"Award #", "Award Name"};
		Map<Integer, Integer> titleLengths = new HashMap<>();
			titleLengths.put(0, 7);
			titleLengths.put(1, 10);
		Map<Integer, String> awardsMap = new HashMap<>();
		String [][] awardArrays = new String[count][];
		
		for(int i = 0; i<count ; i++) {
			Awards thisAward = awards.get(i);
			awardArrays[i] = new String[] { 
					String.valueOf(thisAward.getAwardId()), thisAward.getAwardName() };
			awardsMap.put(thisAward.getAwardId(), thisAward.getAwardName());
				}
		System.out.println("");
		printTable(titles, titleLengths, awardArrays);
		
		boolean done = false;
		while(!done) {
		String awardNum = getStringInput("Enter award # to give award to " +ANSI_GREEN+ player.getPlayerName() +ANSI_RESET
				+", or press ENTER to exit");
		if(isEmpty(awardNum)){
			done = true; }
		else if(isInt(awardNum)) {		
			int aNum = Integer.parseInt(awardNum);
			if(aNum>0 && awardsMap.containsKey(aNum)) {
				if(ToolDao.grantAward(aNum, playerId)) {
					System.out.println("Award " +ANSI_GREEN+ awardsMap.get(aNum) +ANSI_RESET+ " given to "
							+ANSI_GREEN+ player.getPlayerName() +ANSI_RESET	);
					viewAwardedPlayer(playerId);
					}
				done = true;
			}} else {System.out.println(ANSI_RED + "\nValid award number not entered. Please check list provided." + ANSI_RESET);} 
		}
		
	}
	private void removeAward() {
		int playerId = getPlayerId();
		Player player = ToolDao.fetchPlayer(playerId);
		viewAwardedPlayer(playerId);
		System.out.println("");
		List<AwardList> awarded = ToolDao.viewAwardedId(playerId);
		int count = awarded.size();
		
		Map<Integer, String> awardsMap = new HashMap<>();
		for(int i = 0; i<count ; i++) {
			AwardList thisAwarded = awarded.get(i);
			awardsMap.put(thisAwarded.getAwardId(), thisAwarded.getAwardName());
				}
	
		boolean done = false;
		while(!done) {
		String awardNum = getStringInput("Enter award # to remove from " +ANSI_GREEN+ player.getPlayerName() +ANSI_RESET
				+", or press ENTER to exit");
		if(isEmpty(awardNum)){
			done = true; }
		else if(isInt(awardNum)) {		
			int aNum = Integer.parseInt(awardNum);
			if(aNum>0 && awardsMap.containsKey(aNum)) {
				if(ToolDao.removeAward(aNum, playerId)) {
					System.out.println("Award " +ANSI_GREEN+ awardsMap.get(aNum) +ANSI_RESET+ " has been removed from "
							+ANSI_GREEN+ player.getPlayerName() +ANSI_RESET	);
					}
				done = true;
			} else {System.out.println(ANSI_RED + "Valid award number not entered. Please check list provided.\n" + ANSI_RESET);}
			} else {System.out.println(ANSI_RED + "Valid award number not entered. Please check list provided.\n" + ANSI_RESET);} 
		}
		
	}
	

	
} //end of all



