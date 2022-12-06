package allianceToolDao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import allianceTool.entity.Alliance;
import allianceTool.entity.AwardList;
import allianceTool.entity.Awards;
import allianceTool.entity.Player;
import allianceTool.entity.Team;
import allianceTool.errorHandler.DbException;




public class ToolDao extends DaoBase{
	private static final String ALLIANCE_TABLE = "alliances";
	private static final String TEAM_TABLE = "teams";
	private static final String PLAYER_TABLE = "players";
	private static final String AWARDS_TABLE = "awards";
	private static final String AWARDS_GIVEN = "awards_given";
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	

public static List<Alliance> viewAlliances() {
	// @formatter: off 
	String sql = "SELECT * FROM " + ALLIANCE_TABLE + " ORDER BY alliance_Power DESC LIMIT 10";
	// @formatter: on
		try(Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			try(PreparedStatement stmt = conn.prepareStatement(sql)){
				try(ResultSet rs = stmt.executeQuery()){
					List<Alliance> alliances = new LinkedList<>();
					while(rs.next()) {
						alliances.add(extract(rs, Alliance.class)); }
				return alliances;
				}
		} catch (Exception e) { // catch inner try
			rollbackTransaction(conn);
			throw new DbException(e); }
		} catch (SQLException e) { //catch outer try
			throw new DbException(e); }
		}
public static List<Team> viewTeams() {
	// @formatter: off 
	String sql = "SELECT * FROM " + TEAM_TABLE + " ORDER BY team_id";
	// @formatter: on
		try(Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			try(PreparedStatement stmt = conn.prepareStatement(sql)){
				try(ResultSet rs = stmt.executeQuery()){
					List<Team> teams = new LinkedList<>();
					while(rs.next()) {
						teams.add(extract(rs, Team.class)); }
				return teams;
				}
			} catch (Exception e) { // catch inner try
				rollbackTransaction(conn);
				throw new DbException(e); }
		} catch (SQLException e) { //catch outer try
			throw new DbException(e); }
		}
public static List<Player> fetchTeamMembers(Integer tNum, String teamName){
	// @formatter: off 
	String sql = "SELECT * FROM " + PLAYER_TABLE
			+ " WHERE team_id = ?"
			+ " ORDER BY lvl DESC";
	// @formatter: on
			try(Connection conn = DbConnection.getConnection()){
				startTransaction(conn);
				System.out.println(ANSI_BLUE_BACKGROUND+ "\nTeam: [" + tNum + "] " + teamName +ANSI_RESET);
				try(PreparedStatement stmt = conn.prepareStatement(sql)){
					setParameter(stmt, 1, tNum, Integer.class);
					try(ResultSet rs = stmt.executeQuery()){
						List<Player> players = new LinkedList<>();
						while(rs.next()) {
							players.add(extract(rs, Player.class)); }
					return players;					
					}
				} catch (Exception e) { // catch inner try
					rollbackTransaction(conn);
					throw new DbException(e); }
			} catch (SQLException e) { //catch outer try
				throw new DbException(e); }
			}
public static List<Player> fetchNullTeamMembers(String teamName) {
	// @formatter: off 
		String sql = "SELECT * FROM " + PLAYER_TABLE
				+ " WHERE isnull(team_id) AND players.act=true";
		// @formatter: on
				try(Connection conn = DbConnection.getConnection()){
					startTransaction(conn);
					System.out.println("\n" +ANSI_BLUE_BACKGROUND+ "NOT ASSSIGNED TO A TEAM" +ANSI_RESET+ "\n");
					try(PreparedStatement stmt = conn.prepareStatement(sql)){
						try(ResultSet rs = stmt.executeQuery()){
							List<Player> players = new LinkedList<>();
							while(rs.next()) {
								players.add(extract(rs, Player.class)); }
						return players;					
						}
					} catch (Exception e) { // catch inner try
						rollbackTransaction(conn);
						throw new DbException(e); }
				} catch (SQLException e) { //catch outer try
					throw new DbException(e); }
				}
public static Team fetchTeamDuties(int num) {
	// @formatter: off 
	String sql = "SELECT * FROM " + TEAM_TABLE
			+ " WHERE team_id = ?";
	// @formatter: on
	try(Connection conn = DbConnection.getConnection()){
		startTransaction(conn);
		try{
			Team selected = null;
			try(PreparedStatement stmt = conn.prepareStatement(sql)){
				setParameter(stmt, 1, num, Integer.class);
				try(ResultSet rs = stmt.executeQuery()){
					if(rs.next()) {
						selected = extract(rs, Team.class); }
						} 
				}
			commitTransaction(conn);
			return selected;
			
			} catch (Exception e) { // catch 2nd try
				rollbackTransaction(conn);
				throw new DbException(e); }
		} catch (SQLException e) { //catch outer try
			throw new DbException(e); }
	}
public static List<Player> viewPlayers(String sortOption) {
	// @formatter: off 
	String sql = "SELECT * FROM " + PLAYER_TABLE 
			+ " WHERE players.act=true" + " ORDER BY " + sortOption;
	// @formatter: on
		try(Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			try(PreparedStatement stmt = conn.prepareStatement(sql)){
				try(ResultSet rs = stmt.executeQuery()){
					List<Player> players = new LinkedList<>();
					while(rs.next()) {
						players.add(extract(rs, Player.class)); }
				return players;
				}
		} catch (Exception e) { // catch inner try
			rollbackTransaction(conn);
			throw new DbException(e); }
		} catch (SQLException e) { //catch outer try
			throw new DbException(e); }
		}
public static List<Player> findPlayerInfo(String playerName, int selector){
	String sql = null;
	// @formatter: off
	if(selector == 1 || selector == 2) { sql = "SELECT *"
				+  " FROM " + PLAYER_TABLE 
				+ " WHERE player_name LIKE '%" + playerName + "%' AND players.act=true"; }
	if(selector == 3) { sql = "SELECT *"
			+  " FROM " + PLAYER_TABLE 
			+ " WHERE player_name LIKE '%" + playerName + "%'"; }
	if(selector == 4) { 
		int playerId = Integer.valueOf(playerName);
				sql = "SELECT *"
				+  " FROM " + PLAYER_TABLE 
				+ " WHERE player_id = " + playerId; }
	// @formatter: on
			try(Connection conn = DbConnection.getConnection()){
				startTransaction(conn);
				try(PreparedStatement stmt = conn.prepareStatement(sql)){
					try(ResultSet rs = stmt.executeQuery()){
						List<Player> players = new LinkedList<>();
						while(rs.next()) {
							players.add(extract(rs, Player.class));
						} return players; }
				} catch (Exception e) { // catch inner try
					rollbackTransaction(conn);
					throw new DbException(e); }
			} catch (SQLException e) { //catch outer try
				throw new DbException(e); }
			}
public static Alliance fetchAllianceByTag(String allianceTag) {
	String sql = "SELECT * FROM " + ALLIANCE_TABLE + " WHERE alliance_tag = ?";
	try(Connection conn = DbConnection.getConnection()){
		startTransaction(conn);
		try{
			Alliance selected = null;
			try(PreparedStatement stmt = conn.prepareStatement(sql)){
				setParameter(stmt, 1, allianceTag, String.class);
				try(ResultSet rs = stmt.executeQuery()){
					if(rs.next()) {
						selected = extract(rs, Alliance.class); }
						} 
				}

			commitTransaction(conn);
			return selected;
			
			} catch (Exception e) { // catch 2nd try
				rollbackTransaction(conn);
				throw new DbException(e); }
		} catch (SQLException e) { //catch outer try
			throw new DbException(e); }
	}
public static boolean modifyAllianceTag(String tag, String lead) {
	// @formatter:off
		String sql = "UPDATE " + ALLIANCE_TABLE + " SET "
			+ "alliance_tag = '" + tag + "'"
			+ " WHERE alliance_leader = '" + lead + "'";
	// @formatter:on
				try(Connection conn = DbConnection.getConnection()){
					startTransaction(conn);
					try(PreparedStatement stmt = conn.prepareStatement(sql)){
						boolean check = stmt.executeUpdate() == 1;
						commitTransaction(conn);
						return check;
					} catch (Exception e) { // catch inner try
						rollbackTransaction(conn);
						throw new DbException(e); }
				} catch (SQLException e) { //catch outer try
					throw new DbException(e); }
		}
public static boolean modifyAllianceDetails(Alliance update) {
	// @formatter:off
		String sql = "UPDATE " + ALLIANCE_TABLE + " SET "
			+ "alliance_name = '" + update.getAllianceName() + "', "
			+ "alliance_leader = '" + update.getAllianceLeader() + "', "
			+ "num_members = " + update.getNumMembers() + ", " 
			+ "alliance_power = " + update.getAlliancePower() + ", " 
			+ "alliance_updated = CURRENT_DATE()"
			+ " WHERE alliance_tag = '" + update.getAllianceTag() + "'";
	// @formatter:on
				try(Connection conn = DbConnection.getConnection()){
					startTransaction(conn);
					try(PreparedStatement stmt = conn.prepareStatement(sql)){
						boolean check = stmt.executeUpdate() == 1;
						commitTransaction(conn);
						return check;
					} catch (Exception e) { // catch inner try
						rollbackTransaction(conn);
						throw new DbException(e); }
				} catch (SQLException e) { //catch outer try
					throw new DbException(e); }
		}
public static boolean modifyTeamDuties(Team update) {
	// @formatter:off
		String sql = "UPDATE " + TEAM_TABLE + " SET "
			+ "team_name = '" + update.getTeamName() + "', "
			+ "team_refine = '" + update.getTeamRefine() + "', "
			+ "two_system_defense = '" + update.getTwoSystemDefense() + "', "
			+ "three_system_defense = '" + update.getThreeSystemDefense() + "' " 
			+ " WHERE team_id = " + update.getTeamId();
	// @formatter:on
				try(Connection conn = DbConnection.getConnection()){
					startTransaction(conn);
					try(PreparedStatement stmt = conn.prepareStatement(sql)){
						boolean check = stmt.executeUpdate() == 1;
						commitTransaction(conn);
						return check;
					} catch (Exception e) { // catch inner try
						rollbackTransaction(conn);
						throw new DbException(e); }
				} catch (SQLException e) { //catch outer try
					throw new DbException(e); }
		}
public static Player fetchPlayer(int newId) {
	String sql = "SELECT * FROM " + PLAYER_TABLE + " WHERE player_id = ?";
	try(Connection conn = DbConnection.getConnection()){
		startTransaction(conn);
		try{
			Player selected = null;
			try(PreparedStatement stmt = conn.prepareStatement(sql)){
				setParameter(stmt, 1, newId, Integer.class);
				try(ResultSet rs = stmt.executeQuery()){
					if(rs.next()) {
						selected = extract(rs, Player.class); }
					}
				}
			commitTransaction(conn);
			return selected;
			
			} catch (Exception e) { // catch 2nd try
				rollbackTransaction(conn);
				throw new DbException(e); }
		} catch (SQLException e) { //catch outer try
			throw new DbException(e); }
	}
public static boolean addPlayerToTeam(int teamNum, int playerId, String teamName) {
	String sql = "UPDATE " + PLAYER_TABLE + " SET"
			+ " team_id = ?"
			+ " WHERE player_id = ?";
			
	try(Connection conn = DbConnection.getConnection()){
		startTransaction(conn);
		try(PreparedStatement stmt = conn.prepareStatement(sql)){
			setParameter(stmt, 1, teamNum, Integer.class);
			setParameter(stmt, 2, playerId, Integer.class);
			boolean check = stmt.executeUpdate() == 1;
			commitTransaction(conn);
			System.out.println("Player successfully added to team " +ANSI_BLUE_BACKGROUND+ teamName +ANSI_RESET);
			return check;
			} catch (Exception e) { // catch inner try
				rollbackTransaction(conn);
				throw new DbException(e); }
		} catch (SQLException e) { //catch outer try
			throw new DbException(e); }
	}
public static boolean removePlayerFromTeam(int playerId) {
	String sql = "UPDATE " + PLAYER_TABLE + " SET"
			+ " team_id = null"
			+ " WHERE player_id = ?";
			
	try(Connection conn = DbConnection.getConnection()){
		startTransaction(conn);
		try(PreparedStatement stmt = conn.prepareStatement(sql)){
			setParameter(stmt, 1, playerId, Integer.class);
			boolean check = stmt.executeUpdate() == 1;
			commitTransaction(conn);
			System.out.println("Player successfully removed from their old team");
			return check;
			} catch (Exception e) { // catch inner try
				rollbackTransaction(conn);
				throw new DbException(e); }
		} catch (SQLException e) { //catch outer try
			throw new DbException(e); }
	}
public static int addNewPlayer(Player player) {
	// @formatter:off
			String sql = ""
					+ "INSERT INTO " + PLAYER_TABLE
					+ " (alliance_tag, player_name, lvl, act) "
					+ " VALUES"
					+ "(?, ?, ?, true)";
			// @formatter:on
			try(Connection conn = DbConnection.getConnection()){
				startTransaction(conn);
				try(PreparedStatement stmt = conn.prepareStatement(sql)){
					setParameter(stmt, 1, player.getAllianceTag(), String.class);
					setParameter(stmt, 2, player.getPlayerName(), String.class);
					setParameter(stmt, 3, player.getLvl(), Integer.class);
					
					stmt.executeUpdate();
					int playerId = getLastInsertId(conn, PLAYER_TABLE);
					commitTransaction(conn);
					return playerId;
				} catch (Exception e) { // catch inner try
					rollbackTransaction(conn);
					throw new DbException(e);
					}
				} catch (SQLException e) { //catch outer try
					throw new DbException(e);
					}
			}
public static boolean updatePlayerDetails(Player updatePlayer) {
	// @formatter:off
		String sql = "UPDATE " + PLAYER_TABLE + " SET "
			+ "alliance_tag = ?, "
			+ "player_name = ?, "
			+ "pwr = ?, "
			+ "resources_mined = " +updatePlayer.getResourcesMined()+ ", " // 4 
			+ "pvp_total = ?, "
			+ "pvp_damage = " +updatePlayer.getPvpDamage()+ ", " // 6
			+ "kd_ratio = ?, "
			+ "pve = ?, "
			+ "pve_damage = ?, "
			+ "lvl = ?, "
			+ "player_updated = CURDATE(), "
			+ "act = true "
			+ "WHERE player_id = ?" ;
	// @formatter:on

		try(Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			
			try(PreparedStatement stmt = conn.prepareStatement(sql)){
				setParameter(stmt, 1, updatePlayer.getAllianceTag(), String.class);
				setParameter(stmt, 2, updatePlayer.getPlayerName(), String.class);
				setParameter(stmt, 3, updatePlayer.getPwr(), Integer.class);
				setParameter(stmt, 4, updatePlayer.getPvpTotal(), Integer.class);
				setParameter(stmt, 5, updatePlayer.getKdRatio(), BigDecimal.class);
				setParameter(stmt, 6, updatePlayer.getPve(), Integer.class);
				setParameter(stmt, 7, updatePlayer.getPveDamage(), String.class);
				setParameter(stmt, 8, updatePlayer.getLvl(), Integer.class);
				setParameter(stmt, 9, updatePlayer.getPlayerId(), Integer.class);
				
				boolean check = stmt.executeUpdate() == 1;
				commitTransaction(conn);
				return check;
			
			} catch (Exception e) { // catch inner try
				rollbackTransaction(conn);
				throw new DbException(e); }
		} catch (SQLException e) { //catch outer try
			throw new DbException(e);
		}
	}
public static boolean deletePlayer(int playerId) {
		// @formatter:off
		String sql = "DELETE FROM " + PLAYER_TABLE + " WHERE player_id = ?";
		// @formatter:on
			try(Connection conn = DbConnection.getConnection()){
				startTransaction(conn);
				
				try(PreparedStatement stmt = conn.prepareStatement(sql)){
					setParameter(stmt, 1, playerId, Integer.class);
					
					boolean check = stmt.executeUpdate() == 1;
					commitTransaction(conn);
					return check;
					
					} catch (Exception e) { // catch inner try
						rollbackTransaction(conn);
						throw new DbException(e); }
				} catch (SQLException e) { //catch outer try
					throw new DbException(e);
					}
		}
public static boolean setInactive(int playerId) {
	// @formatter:off
	String sql = "UPDATE " + PLAYER_TABLE + " SET act = false" 
	+" WHERE player_id = ?";
	// @formatter:on
	
	try(Connection conn = DbConnection.getConnection()){
		startTransaction(conn);
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)){
			setParameter(stmt, 1, playerId, Integer.class);
			
			boolean check = stmt.executeUpdate() == 1;
			commitTransaction(conn);
			return check;
		
		} catch (Exception e) { // catch inner try
			rollbackTransaction(conn);
			throw new DbException(e); }
	} catch (SQLException e) { //catch outer try
		throw new DbException(e);
		}
}
public static List<Awards> viewAllAwards() {
	// @formatter: off 
	String sql = "SELECT * FROM " + AWARDS_TABLE;
	// @formatter: on
		try(Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			try(PreparedStatement stmt = conn.prepareStatement(sql)){
				try(ResultSet rs = stmt.executeQuery()){
					List<Awards> award = new LinkedList<>();
					while(rs.next()) {
						award.add(extract(rs, Awards.class)); }
				return award;
				}
		} catch (Exception e) { // catch inner try
			rollbackTransaction(conn);
			throw new DbException(e); }
		} catch (SQLException e) { //catch outer try
			throw new DbException(e); }
		}
public static List<AwardList> viewAllAwarded() {
		// @formatter: off 
		String sql = "SELECT awards_given.award_id, awards.award_name, awards_given.player_id, players.player_name "
				+ "FROM " + AWARDS_GIVEN 
				+ " JOIN awards ON awards.award_id = awards_given.award_id"
				+ "	JOIN players ON players.player_id = awards_given.player_id" ;
		// @formatter: on
			try(Connection conn = DbConnection.getConnection()){
				startTransaction(conn);
				try(PreparedStatement stmt = conn.prepareStatement(sql)){
					try(ResultSet rs = stmt.executeQuery()){
						List<AwardList> awarded = new LinkedList<>();
						while(rs.next()) {
							awarded.add(extract(rs, AwardList.class)); }
					return awarded;
					}
			} catch (Exception e) { // catch inner try
				rollbackTransaction(conn);
				throw new DbException(e); }
			} catch (SQLException e) { //catch outer try
				throw new DbException(e); }
			}
public static List<AwardList> viewAwardedId(int playerId) {
	// @formatter: off 
	String sql = "SELECT awards_given.award_id, awards.award_name, awards_given.player_id, players.player_name "
			+ "FROM " + AWARDS_GIVEN 
			+ " JOIN awards ON awards.award_id = awards_given.award_id"
			+ "	JOIN players ON players.player_id = awards_given.player_id"
			+ " WHERE awards_given.player_id = ?";
	// @formatter: on
		try(Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			try(PreparedStatement stmt = conn.prepareStatement(sql)){
				setParameter(stmt, 1, playerId, Integer.class);
				try(ResultSet rs = stmt.executeQuery()){
					List<AwardList> awarded = new LinkedList<>();
					while(rs.next()) {
						awarded.add(extract(rs, AwardList.class)); }
				return awarded;
				}
		} catch (Exception e) { // catch inner try
			rollbackTransaction(conn);
			throw new DbException(e); }
		} catch (SQLException e) { //catch outer try
			throw new DbException(e); }
		}
public static boolean grantAward(int aNum, int playerId) {
	// @formatter:off
				String sql = ""
						+ "INSERT INTO " + AWARDS_GIVEN
						+ " (award_id, player_id) "
						+ " VALUES"
						+ "(?, ?)";
				// @formatter:on
				try(Connection conn = DbConnection.getConnection()){
					startTransaction(conn);
					try(PreparedStatement stmt = conn.prepareStatement(sql)){
						setParameter(stmt, 1, aNum, Integer.class);
						setParameter(stmt, 2, playerId, Integer.class);
						
						stmt.executeUpdate();
						commitTransaction(conn);
						return true;
					} catch (Exception e) { // catch inner try
						rollbackTransaction(conn);
						throw new DbException(e);
						}
					} catch (SQLException e) { //catch outer try
						throw new DbException(e);
						}
				}
public static boolean removeAward(int aNum, int playerId) {
	// @formatter:off
				String sql = ""
						+ "DELETE FROM " + AWARDS_GIVEN
						+ " WHERE award_id = ?"
						+ " AND player_id = ?";
				// @formatter:on
				try(Connection conn = DbConnection.getConnection()){
					startTransaction(conn);
					try(PreparedStatement stmt = conn.prepareStatement(sql)){
						setParameter(stmt, 1, aNum, Integer.class);
						setParameter(stmt, 2, playerId, Integer.class);
						
						stmt.executeUpdate();
						commitTransaction(conn);
						return true;
					} catch (Exception e) { // catch inner try
						rollbackTransaction(conn);
						throw new DbException(e);
						}
					} catch (SQLException e) { //catch outer try
						throw new DbException(e);
						}
				}
public static boolean createNewAward(String newAward) {
	// @formatter:off
	String sql = ""
			+ "INSERT INTO " + AWARDS_TABLE
			+ " (award_name) "
			+ " VALUES"
			+ "(?)";
	// @formatter:on
	try(Connection conn = DbConnection.getConnection()){
		startTransaction(conn);
		try(PreparedStatement stmt = conn.prepareStatement(sql)){
			setParameter(stmt, 1, newAward, String.class);
			
			stmt.executeUpdate();
			commitTransaction(conn);
			return true;
		} catch (Exception e) { // catch inner try
			rollbackTransaction(conn);
			throw new DbException(e);
			}
		} catch (SQLException e) { //catch outer try
			throw new DbException(e);
			}
	}
	


} //end of class

