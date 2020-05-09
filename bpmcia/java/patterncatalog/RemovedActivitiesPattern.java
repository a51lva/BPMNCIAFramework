package patterncatalog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import bpmcia.CIABpmnReportModel;
import bpmcia.CIABpmnUtil;

public class RemovedActivitiesPattern extends ChangePattern{
	
	private Collection< ModelElementInstance> removedElements;
	
	private Collection<CIABpmnReportModel> bpmnReportModels;
	
	public RemovedActivitiesPattern() {
		this.removedElements = new ArrayList<ModelElementInstance>();
	}
	
	public RemovedActivitiesPattern(Collection< ModelElementInstance> activityElement1, Collection< ModelElementInstance> activityElements2) {
		this.removedElements = new ArrayList<ModelElementInstance>();
		this.modelElementsOld = activityElement1;
		this.modelElementsUpdated = activityElements2;
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		this.executionTime = 0L;
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
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
			}
		}
		
		setExecutionTime(calculateExecutionTime(startMoment));
	}
	
	public void calculateDirectedInpactedActivities() {
		for (ModelElementInstance element:removedElements) {
			
			Collection<String> targetActivities = CIABpmnUtil.getTargetsElementId(element, getModelElementsOld());
			
			for(String targetId: targetActivities) {
				ModelElementInstance targetElement = CIABpmnUtil.getElement(targetId, getModelElementsOld());
				if(targetElement != null && !CIABpmnUtil.elementExist(targetElement, removedElements) ) {
					
					CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel(element.getAttributeValue("name"), "Removed Activity", targetElement.getAttributeValue("name"));
					
					bpmnReportModels.add(bpmnReportModel);
				}
			}
		}
	}
}
