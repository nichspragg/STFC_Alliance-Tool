package allianceTool.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Player {
	private Integer playerId;
	private String allianceTag;
	private String playerName;
	private Integer teamId;
	private int pwr;
	private Long resourcesMined;
	private int pvpTotal;
	private Long pvpDamage;
	private BigDecimal kdRatio;
	private int pve;
	private String pveDamage;
	private int lvl;
	private Date playerUpdated;
	private Boolean act;

	public Integer getPlayerId() {
		return playerId;
		}
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
		}
	public String getAllianceTag() {
		return allianceTag;
		}
	public void setAllianceTag(String allianceTag) {
		this.allianceTag = allianceTag;
		}
	public String getPlayerName() {
		return playerName;
		}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
		}
	public int getPwr() {
		return pwr;
		}
	public void setPwr(int pwr) {
		this.pwr = pwr;
		}
	public Long getResourcesMined() {
		return resourcesMined;
		}
	public void setResourcesMined(Long resourcesMined) {
		this.resourcesMined = resourcesMined;
		}
	public int getPvpTotal() {
		return pvpTotal;
		}
	public void setPvpTotal(int pvpTotal) {
		this.pvpTotal = pvpTotal;
		}
	public Long getPvpDamage() {
		return pvpDamage;
		}
	public void setPvpDamage(Long pvpDamage) {
		this.pvpDamage = pvpDamage;
		}
	public BigDecimal getKdRatio() {
		return kdRatio;
		}
	public void setKdRatio(BigDecimal kdRatio) {
		this.kdRatio = kdRatio;
		}
	public int getPve() {
		return pve;
		}
	public void setPve(int pve) {
		this.pve = pve;
		}
	public String getPveDamage() {
		return pveDamage;
		}
	public void setPveDamage(String pveDamage) {
		this.pveDamage = pveDamage;
		}
	public int getLvl() {
		return lvl;
		}
	public void setLvl(int lvl) {
		this.lvl = lvl;
		}
	public Date getPlayerUpdated() {
		return playerUpdated;
		}
	public void setPlayerUpdated(Date playerUpdated) {
		this.playerUpdated = playerUpdated;
		}
	public Boolean getAct() {
		return act;
		}
	public void setAct(Boolean act) {
		this.act = act;
		}
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
}