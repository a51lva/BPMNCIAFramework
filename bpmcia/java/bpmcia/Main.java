package bpmcia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.primefaces.shaded.json.JSONArray;

@ManagedBean
@SessionScoped
public class Main{
		
	private String appname;
	private String firstfile, updatedfile;
	private Collection< ModelElementInstance> elementsOldModel;
	private Collection< ModelElementInstance> elementsUpdatedModel;
	
	private Collection< ModelElementInstance> changedElements;
	
	private CIACalculation ciaCalculation;
	private CIABpmnModelInstance oldModel, updatedModel;
	private Boolean canExecute;
	
	private JSONArray pieChart = new JSONArray();
	private JSONArray oldData = new JSONArray();
	private JSONArray updatedData = new JSONArray();
	private JSONArray columnCategories = new JSONArray();
	
	private Collection<CIABpmnReportModel> bpmnReportModels;
	
	private SidemenuManagement sidebarMenu;
	
	private Integer ciaSteps = 1;
	
	
	@PostConstruct
	public void init() {
		try {
			
			sidebarMenu = new SidemenuManagement("dashboard");
			
			String fileNameOld = "Under-warranty-after-sales-service-business-process-model.bpmn", fileNameUpdated = "Under-warranty-after-sales-service-business-process-model2.bpmn";
			
			setFirstfile(fileNameOld);
			
			setUpdatedfile(fileNameUpdated);
			
			oldModel = new CIABpmnModelInstance(fileNameOld);
			updatedModel = new CIABpmnModelInstance(fileNameUpdated);
			
			setElementsOldModel(oldModel.getElements());
			setElementsUpdatedModel(updatedModel.getElements());
			ciaCalculation = new CIACalculation(oldModel, updatedModel);
			
			bpmnReportModels = new ArrayList<CIABpmnReportModel>();
			
			changedElements = new ArrayList<ModelElementInstance>();
			
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
	
	public Boolean getCanExecute() {
		return canExecute;
	}
	
	public void setCanExecute(Boolean canExecute) {
		this.canExecute = canExecute;
	}
	
	public Collection<ModelElementInstance> getChangedElements() {
		return changedElements;
	}

	public void setChangedElements(Collection<ModelElementInstance> changedElements) {
		this.changedElements = changedElements;
	}

	public Collection<ModelElementInstance> getElementsOldModel() {
		return elementsOldModel;
	}
	
	public void setElementsOldModel(Collection<ModelElementInstance> elementsOldModel) {
		this.elementsOldModel = elementsOldModel;
	}
	
	public Collection<ModelElementInstance> getElementsUpdatedModel() {
		return elementsUpdatedModel;
	}
	
	public void setElementsUpdatedModel(Collection<ModelElementInstance> elementsUpdatedModel) {
		this.elementsUpdatedModel = elementsUpdatedModel;
	}
	
	public CIACalculation getCiaCalculation() {
		return ciaCalculation;
	}
	
	public void setCiaCalculation(CIACalculation ciaCalculation) {
		this.ciaCalculation = ciaCalculation;
	}
	
	public CIABpmnModelInstance getOldModel() {
		return oldModel;
	}
	
	public void setOldModel(CIABpmnModelInstance oldModel) {
		this.oldModel = oldModel;
	}
	
	public CIABpmnModelInstance getUpdatedModel() {
		return updatedModel;
	}
	
	public void setUpdatedModel(CIABpmnModelInstance updatedModel) {
		this.updatedModel = updatedModel;
	}
	
	public Collection<CIABpmnReportModel> getBpmnReportModels() {
		return bpmnReportModels;
	}
	
	public void setBpmnReportModels(Collection<CIABpmnReportModel> bpmnReportModels) {
		this.bpmnReportModels = bpmnReportModels;
	}
	
	public String getPieChartData() {
		return pieChart.toString();
	}
	
	public String getOldData() {
		return oldData.toString();
	}
	
	public String getUpdatedData() {
		return updatedData.toString();
	}
	
	public String getColumnCategories() {
		return columnCategories.toString();
	}
	
	public SidemenuManagement getSidebarMenu() {
		return sidebarMenu;
	}
	
	public void setSidebarMenu(SidemenuManagement sidebarMenu) {
		this.sidebarMenu = sidebarMenu;
	}
	
	public Integer getCiaSteps() {
		return ciaSteps;
	}

	public void setCiaSteps(Integer ciaSteps) {
		this.ciaSteps = ciaSteps;
	}

	public void changesExecute() {
		
		clearLists();
		
		ciaCalculation.execute();
		
		setChangedElements(ciaCalculation.getChangedElements());
		
		setBpmnReportModels(ciaCalculation.getBpmnReportModels());
		
		List<Object> piechartArray = new ArrayList<Object>();
		
		int changedActivitiesPercentage = 0, notChangedActivities = 0;
		
		if(getElementsOldModel().size() > 0) {
			
			int reports = getChangedElements().size();
			
			int oldmodels = CIABpmnUtil.getActivityElements(this.oldModel.getModelInstance()).size();
			
			double div = (double) reports / oldmodels;
			
			changedActivitiesPercentage = (int)( div * 100 );
			
		}
		
		notChangedActivities = 100 - changedActivitiesPercentage;
		
		piechartArray.add("Unchanged Activities");
		
		piechartArray.add(notChangedActivities);
		
		pieChart.put(piechartArray);
		
		piechartArray = new ArrayList<Object>();
		
		piechartArray.add("Changed Activities");
		
		piechartArray.add(changedActivitiesPercentage);
		
		pieChart.put(piechartArray);
		
		List<String> listOfTypes = new ArrayList<String>();
		
		oldModel.getElements().forEach(el -> {
			
			ModelElementType elementType = el.getElementType();
			
			String elementTypeName = elementType.getTypeName();
			
			if(!listOfTypes.contains(elementTypeName) && !elementTypeName.isEmpty()) {
				
				listOfTypes.add(elementTypeName);
				
				columnCategories.put(elementTypeName);
				
				oldData.put( oldModel.getModelInstance().getModelElementsByType(elementType).size() );
				
				updatedData.put( updatedModel.getModelInstance().getModelElementsByType(elementType).size() );				
			}
		});
		
		updatedModel.getElements().forEach(el -> {
			
			ModelElementType elementType = el.getElementType();
			
			String elementTypeName = elementType.getTypeName();
			
			if(!listOfTypes.contains(elementTypeName) && !elementTypeName.isEmpty()) {
				
				columnCategories.put(elementTypeName);
				
				oldData.put(0);

				updatedData.put( updatedModel.getModelInstance().getModelElementsByType(elementType).size() );
				
			}
		});
		
		sidebarMenu.setSelectedMenu("result");		
		
	}

	public void clear() {
		
		firstfile = "";
		
		updatedfile = "";
		
		setCanExecute(false);
		
		clearLists();
	}
	
	private void clearLists() {
		
		bpmnReportModels.clear();
		
		changedElements.clear();
		
		oldData = new JSONArray();
		
		updatedData = new JSONArray();
		
		columnCategories = new JSONArray();
		
		pieChart = new JSONArray();
	}
}