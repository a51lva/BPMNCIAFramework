package bpmcia;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
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
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.primefaces.model.file.UploadedFile;

import patterncatalog.ControlFlowDifferentDependenciesPattern;
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
	private Collection< ModelElementInstance> incomeSequenceFlowChangedActivities;
	
	private RemovedActivitiesPattern removedActivitiesPattern;
	private InsertedActivitiesPattern insertedActivitiesPattern;
	private ControlFlowDifferentDependenciesPattern control; 
	
	private Boolean canExecute;
	
	
	@PostConstruct
	public void init() {
		try {
			String fileNameOld = "shipModel.bpmn", fileNameUpdated = "shipModel2.bpmn";
			
			File fileOld = getFileFromResources(fileNameOld), fileUpdated = getFileFromResources(fileNameUpdated);;
			 
		    BpmnModelInstance modelInstanceOld = Bpmn.readModelFromFile(fileOld), modelInstanceUpdated = Bpmn.readModelFromFile(fileUpdated);
		    
		
			ModelElementType elementTypeOld = modelInstanceOld.getModel().getType(FlowElement.class);
						
			Collection<ModelElementInstance> elemenInstancesOld = modelInstanceOld.getModelElementsByType(elementTypeOld);
			
			setElements1(elemenInstancesOld);
			
			setFirstfile(fileNameOld);
						
			ModelElementType activityTypeOld = modelInstanceOld.getModel().getType(Activity.class);
			
			Collection<ModelElementInstance> activityInstancesOld = modelInstanceOld.getModelElementsByType(activityTypeOld);			
			
			ModelElementType gatewayTypeOld = modelInstanceOld.getModel().getType(Gateway.class);
			
			Collection<ModelElementInstance> gatewayInstancesOld = modelInstanceOld.getModelElementsByType(gatewayTypeOld);
			
			
			
			
			ModelElementType elementTypeUpdated = modelInstanceUpdated.getModel().getType(FlowElement.class);
			
			Collection<ModelElementInstance> elemenInstancesUpdated = modelInstanceUpdated.getModelElementsByType(elementTypeUpdated);
			
			setElements2(elemenInstancesUpdated);
			
			setUpdatedfile(fileNameUpdated);
						
			ModelElementType activityTypeUpdated = modelInstanceUpdated.getModel().getType(Activity.class);
			
			Collection<ModelElementInstance> activityInstancesUpdated = modelInstanceUpdated.getModelElementsByType(activityTypeUpdated);			
			
			ModelElementType gatewayTypeUpdated = modelInstanceUpdated.getModel().getType(Gateway.class);
			
			Collection<ModelElementInstance> gatewayInstancesUpdated = modelInstanceUpdated.getModelElementsByType(gatewayTypeUpdated);
			
			
			
			removedActivitiesPattern = new RemovedActivitiesPattern(activityInstancesOld, activityInstancesUpdated);
			insertedActivitiesPattern = new InsertedActivitiesPattern(activityInstancesOld, activityInstancesUpdated);
			
			control = new ControlFlowDifferentDependenciesPattern(activityInstancesOld, activityInstancesUpdated);
			control.setGatewayElementsOld(convertToCollectionGateway(gatewayInstancesOld));
			control.setGatewayElementsUpdated(convertToCollectionGateway(gatewayInstancesUpdated));
			
			setCanExecute(true);
			
			
		}catch (Exception e) {
			System.out.print(e.getMessage());
		}
		
		setAppname("BPMN Based CIA Framework");
		
	}
	
	public ControlFlowDifferentDependenciesPattern getControl() {
		return control;
	}
	
	public void setControl(ControlFlowDifferentDependenciesPattern control) {
		this.control = control;
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
	
	public Collection<ModelElementInstance> getIncomeSequenceFlowChangedActivities() {
		return incomeSequenceFlowChangedActivities;
	}
	
	public void setIncomeSequenceFlowChangedActivities(
			Collection<ModelElementInstance> incomeSequenceFlowChangedActivities) {
		this.incomeSequenceFlowChangedActivities = incomeSequenceFlowChangedActivities;
	}
	
	public void changesExecute() {
		removedActivitiesPattern.execute();
		insertedActivitiesPattern.execute();
		control.execute();
		
		setRemovedActivities(removedActivitiesPattern.getRemovedElements());
		setInsertedActivities(insertedActivitiesPattern.getInsertedElements());
		setIncomeSequenceFlowChangedActivities(control.getChangedElements());
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
	
	private Collection<Gateway> convertToCollectionGateway(Collection< ModelElementInstance> gateways){
		
		Collection<Gateway> result = new ArrayList<Gateway>();
		
		if(gateways != null) {
			for(ModelElementInstance gateway: gateways){
				result.add((Gateway)gateway);
			}
		}
		
		return result;
	}
}