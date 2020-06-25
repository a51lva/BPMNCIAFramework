package bpmcia;

public class CIABpmnReportModel {
	private String changedElement, elementType, changePattern, inpactedActivity;	
	
	public CIABpmnReportModel() {
		this.changedElement = "";
		this.changePattern = "";
		this.inpactedActivity = "";
	}
	
	public CIABpmnReportModel(String changedActivity, String elementType, String changePattern, String inpactedActivity) {
		this.changedElement = changedActivity;
		this.elementType = elementType;
		this.changePattern = changePattern;
		this.inpactedActivity = inpactedActivity;
	}
	
	public String getChangedElement() {
		return changedElement;
	}
	
	public void setChangedElement(String changedElement) {
		this.changedElement = changedElement;
	}
	
	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
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
