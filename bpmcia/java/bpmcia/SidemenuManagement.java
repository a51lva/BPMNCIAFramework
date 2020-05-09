package bpmcia;

public class SidemenuManagement {
	private String selectedMenu;

	public SidemenuManagement(String selected) {
		this.selectedMenu = selected;
	}
	
	public String getSelectedMenu() {
		return selectedMenu;
	}
	
	public void setSelectedMenu(String selected) {
		this.selectedMenu = selected;
	}

}
