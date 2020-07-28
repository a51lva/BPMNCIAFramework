package patterncatalog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import bpmcia.CIABpmnReportModel;
import bpmcia.CIABpmnUtil;

public class RemovedActivitiesPattern extends ChangePattern{
	
	private static final String REMOVED_ACTIVITY = "Removed Activity";
	
	private static final String ACTIVITY = "Activity";
	
	private Collection< ModelElementInstance> removedElements;
	
	public RemovedActivitiesPattern() {
		
		this.removedElements = new ArrayList<ModelElementInstance>();
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
		this.bpmnReportModelsChangedELements = new ArrayList<CIABpmnReportModel>();
		
	}
	
	public RemovedActivitiesPattern(Collection< ModelElementInstance> activityElement1, Collection< ModelElementInstance> activityElements2, String steps) {
		
		this.removedElements = new ArrayList<ModelElementInstance>();
		
		this.modelElementsOld = activityElement1;
		
		this.modelElementsUpdated = activityElements2;
		
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		
		this.executionTime = 0L;
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
		this.bpmnReportModelsChangedELements = new ArrayList<CIABpmnReportModel>();
		
		this.steps = steps;
		
	}
	
	public Collection<ModelElementInstance> getRemovedElements() {
		return removedElements;
	}
	
	public void setRemovedElements(Collection<ModelElementInstance> removedElements) {
		this.removedElements = removedElements;
	}
	
	public Collection<CIABpmnReportModel> getBpmnReportModels() {
		return bpmnReportModels;
	}
	
	public void setBpmnReportModels(Collection<CIABpmnReportModel> bpmnReportModels) {
		this.bpmnReportModels = bpmnReportModels;
	}
	
	public void execute() {
		
		setRemovedElements(new ArrayList<ModelElementInstance>());
		
		Instant startMoment = Instant.now();
		
		for (ModelElementInstance element:getModelElementsOld()) {
			
			ModelElementInstance equivalentElement = (getEquivalentElement(element)!=null)?getEquivalentElement(element):element;

			if(!CIABpmnUtil.elementExist(equivalentElement, getModelElementsUpdated())) {
				
				removedElements.add(equivalentElement);
				
				CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel( equivalentElement.getAttributeValue("name"), ACTIVITY, REMOVED_ACTIVITY, "", 1);
				
				bpmnReportModelsChangedELements.add(bpmnReportModel);
			}
		}
		
		setExecutionTime(calculateExecutionTime(startMoment));
		
	}
	
	public void calculateInpactedActivities() {
		
		for (ModelElementInstance element:removedElements) {
			
			Collection<String> targetActivities = CIABpmnUtil.getTargetsElementId(element, getModelElementsOld());
			
			inpactedActivitiesSteps(targetActivities, element.getAttributeValue("name"), ACTIVITY, REMOVED_ACTIVITY, getSteps(), getModelElementsOld());
			
			Collection<String> dataAssociation = CIABpmnUtil.getDataAssociationElements( element, CIABpmnUtil.convertToCollectionActvity(getModelElementsOld()));
			
			bpmnReportModels.addAll( validateDataAssociationElements( dataAssociation, element, getModelElementsOld(), REMOVED_ACTIVITY ) );
			
		}
	}
}
