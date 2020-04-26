package bpmcia;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.primefaces.model.file.UploadedFile;

import patterncatalog.InsertedActivitiesPattern;
import patterncatalog.RemovedActivitiesPattern;

@ManagedBean
@SessionScoped
public class Main {
	private String appname;
	private Collection< ModelElementInstance> elements1;
	private Collection< ModelElementInstance> elements2;
	private String firstfile, updatedfile;
	
	private Collection< ModelElementInstance> removedActivities;
	private Collection< ModelElementInstance> insertedActivities;
	
	private RemovedActivitiesPattern removedActivitiesPattern;
	private InsertedActivitiesPattern insertedActivitiesPattern;
	
	private Boolean canExecute;
	
	
	@PostConstruct
	public void init() {
		try {
			String fileName = "shipModel.bpmn";
			File file = getFileFromResources(fileName);
			 
		    BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
			ModelElementType elementType = modelInstance.getModel().getType(FlowElement.class);
						
			Collection<ModelElementInstance> elemenInstances = modelInstance.getModelElementsByType(elementType);
			
			setElements1(elemenInstances);
			
			setFirstfile(fileName);
			
			
			ModelElementType activityType = modelInstance.getModel().getType(Activity.class);
			
			Collection<ModelElementInstance> activityInstances1 = modelInstance.getModelElementsByType(activityType);			
			
			
			fileName = "shipModel2.bpmn";
			
			file = getFileFromResources(fileName);
			 
		    modelInstance = Bpmn.readModelFromFile(file);
		
			elemenInstances = modelInstance.getModelElementsByType(elementType);
			
			setElements2(elemenInstances);
			
			setUpdatedfile(fileName);
			
			
			Collection<ModelElementInstance> activityInstances2 = modelInstance.getModelElementsByType(activityType);
			
			removedActivitiesPattern = new RemovedActivitiesPattern(activityInstances1, activityInstances2);
			insertedActivitiesPattern = new InsertedActivitiesPattern(activityInstances1, activityInstances2);
			
			setCanExecute(true);
			
			
		}catch (Exception e) {
			System.out.print(e.getMessage());
		}
		
		setAppname("BPMN Based CIA Framework");
		
	}
	
	public String getAppname() {
		return appname;
	}
	
	public void setAppname(String appname) {
		this.appname = appname;
	}
	
	public String getFirstfile() {
		return firstfile;
	}
	
	public void setFirstfile(String firstfile) {
		this.firstfile = firstfile;
	}
	
	public String getUpdatedfile() {
		return updatedfile;
	}
	
	public void setUpdatedfile(String updatedfile) {
		this.updatedfile = updatedfile;
	}
	
	public Collection<ModelElementInstance> getInsertedActivities() {
		return insertedActivities;
	}
	
	public void setInsertedActivities(Collection<ModelElementInstance> insertedActivities) {
		this.insertedActivities = insertedActivities;
	}
	
	public Collection<ModelElementInstance> getRemovedActivities() {
		return removedActivities;
	}
	
	public void setRemovedActivities(Collection<ModelElementInstance> removedActivities) {
		this.removedActivities = removedActivities;
	}
	
	public Collection<ModelElementInstance> getElements1() {
		return elements1;
	}
	
	public void setElements1(Collection<ModelElementInstance> elements1) {
		this.elements1 = elements1;
	}
	
	public Collection<ModelElementInstance> getElements2() {
		return elements2;
	}
	
	public void setElements2(Collection<ModelElementInstance> elements2) {
		this.elements2 = elements2;
	}
	
	public Boolean getCanExecute() {
		return canExecute;
	}
	
	public void setCanExecute(Boolean canExecute) {
		this.canExecute = canExecute;
	}
	
	public void changesExecute() {
		removedActivitiesPattern.execute();
		insertedActivitiesPattern.execute();
		
		setRemovedActivities(removedActivitiesPattern.getRemovedElements());
		setInsertedActivities(insertedActivitiesPattern.getInsertedElements());
	}
	
	private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }	
}