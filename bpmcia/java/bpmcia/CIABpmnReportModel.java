package bpmcia;

public class CIABpmnReportModel {
	private String changedActivity, changePattern, inpactedActivity;	
	
	public CIABpmnReportModel() {
		this.changedActivity = "";
		this.changePattern = "";
		this.inpactedActivity = "";
	}
	
	public CIABpmnReportModel(String changedActivity, String changePattern, String inpactedActivity) {
		this.changedActivity = changedActivity;
		this.changePattern = changePattern;
		this.inpactedActivity = inpactedActivity;
	}
	
	public String getChangedActivity() {
		return changedActivity;
	}
	
	public void setChangedActivity(String changedActivity) {
		this.changedActivity = changedActivity;
	}
	
	public String getChangePattern() {
		return changePattern;
	}
	
	public void setChangePattern(String changePattern) {
		this.changePattern = changePattern;
	}
	
	public String getInpactedActivity() {
		return inpactedActivity;
	}
	
	public void setInpactedActivity(String inpactedActivity) {
		this.inpactedActivity = inpactedActivity;
	}

}
