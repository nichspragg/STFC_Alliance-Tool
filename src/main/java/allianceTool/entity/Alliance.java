package allianceTool.entity;

import java.util.Date;


public class Alliance {
	  private String allianceTag;
	  private String allianceName;
	  private int numMembers;
	  private String allianceLeader;
	  private long alliancePower;
	  private Date allianceUpdated;

  public String getAllianceTag() {
    return allianceTag;
  	}
  public void setAllianceTag(String allianceTag) {
	    this.allianceTag = allianceTag;
	  }
  public String getAllianceName() {
	    return allianceName;
	  	}
  public void setAllianceName(String allianceName) {
	    this.allianceName = allianceName;
	  }
  public int getNumMembers() {
	    return numMembers;
	  	}
  public void setNumMembers(int numMembers) {
	    this.numMembers = numMembers;
	  }
  public String getAllianceLeader() {
	    return allianceLeader;
	  	}
  public void setAllianceLeader(String allianceLeader) {
	    this.allianceLeader = allianceLeader;
	  }
  public long getAlliancePower() {
	    return alliancePower;
	  	}
  public void setAlliancePower(long alliancePower) {
	    this.alliancePower = alliancePower;
	  }
  public Date getAllianceUpdated() {
	    return allianceUpdated;
	  	}
  public void setAllianceUpdated(Date allianceUpdated) {
	    this.allianceUpdated = allianceUpdated;
	  }

} //end of class