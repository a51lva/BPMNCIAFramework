package bpmcia;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Main {
	private String appname;
	@PostConstruct
	public void init() {
		setAppname("BPMN Based CIA Framework");		
	}
	
	public String getAppname() {
		return appname;
	}
	
	public void setAppname(String appname) {
		this.appname = appname;
	}
}
