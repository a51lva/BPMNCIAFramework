package patterncatalog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import bpmcia.CIABpmnReportModel;
import bpmcia.CIABpmnUtil;

public class ControlFlowDifferentDependenciesPattern extends ChangePattern{
	private static final String CONTROL_FLOW_DIFFERENT_DEPENDENCY = "Changed ControlFlow in ";
	private static final String CONTROL_FLOW_DIFFERENT = "Changed ControlFlow";
	
	private static final String ACTIVITY = "Activity";
	
	private Collection< ModelElementInstance> changedElements;
	
	private Collection<Gateway> gatewayElementsOld;
	
	private Collection<Gateway> gatewayElementsUpdated;
	
	public ControlFlowDifferentDependenciesPattern() {
		
		this.changedElements = new ArrayList<ModelElementInstance>();
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
		this.bpmnReportModelsChangedELements = new ArrayList<CIABpmnReportModel>();
		
	}
	
	public ControlFlowDifferentDependenciesPattern(Collection< ModelElementInstance> activityElementsOld, Collection< ModelElementInstance> activityElementsUpdated, Collection<Gateway> gatewayElementsOld, Collection<Gateway> gatewayElementsUpdated, String steps) {
		
		this.changedElements = new ArrayList<ModelElementInstance>();
		
		this.modelElementsOld = activityElementsOld;
		
		this.modelElementsUpdated = activityElementsUpdated;
		
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		
		this.executionTime = 0L;
		
		this.gatewayElementsOld = gatewayElementsOld;
		
		this.gatewayElementsUpdated = gatewayElementsUpdated;
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
		this.bpmnReportModelsChangedELements = new ArrayList<CIABpmnReportModel>();
		
		this.steps = steps;
	}
	
	public Collection<ModelElementInstance> getChangedElements() {
		return changedElements;
	}
	
	public void setChangedElements(Collection<ModelElementInstance> changedElements) {
		this.changedElements = changedElements;
	}
	
	public Collection<Gateway> getGatewayElementsOld() {
		return gatewayElementsOld;
	}
	
	public void setGatewayElementsOld(Collection<Gateway> gatewayElementsOld) {
		this.gatewayElementsOld = gatewayElementsOld;
	}
	
	public Collection<Gateway> getGatewayElementsUpdated() {
		return gatewayElementsUpdated;
	}
	
	public void setGatewayElementsUpdated(Collection<Gateway> gatewayElementsUpdated) {
		this.gatewayElementsUpdated = gatewayElementsUpdated;
	}
	
	public void execute() {
		
		setChangedElements(new ArrayList<ModelElementInstance>());
		
		Instant startMoment = Instant.now();
		
		for (ModelElementInstance element:getModelElementsOld()) {
			
			ModelElementInstance equivalentActivityElement = (getEquivalentElement(element)!=null)?getEquivalentElement(element):element;	
			
			ModelElementInstance equivalentActivityElement2 = CIABpmnUtil.getEquivalentElement(equivalentActivityElement, getModelElementsUpdated());
			
			if(equivalentActivityElement2 != null) {
				
				Collection<String> dependenciesActivity = CIABpmnUtil.getDependenciesElementId(equivalentActivityElement, getGatewayElementsOld());
				
				Collection<String> dependenciesActivity2 = CIABpmnUtil.getDependenciesElementId(equivalentActivityElement2, getGatewayElementsUpdated());
				
				if(dependenciesActivity.size() == dependenciesActivity2.size()) {
					
					for(String id: dependenciesActivity) {
						
						if(!dependenciesActivity2.stream().anyMatch(id2 -> id2.equalsIgnoreCase(id))) {		
							
							changedElements.add(equivalentActivityElement);
							
							CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel(equivalentActivityElement.getAttributeValue("name"), ACTIVITY, CONTROL_FLOW_DIFFERENT, "", 1);
							
							bpmnReportModelsChangedELements.add(bpmnReportModel);
							
							break; 
						}
					}
				}else {
					
					changedElements.add(equivalentActivityElement);
					CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel(equivalentActivityElement.getAttributeValue("name"), ACTIVITY, CONTROL_FLOW_DIFFERENT, "", 1);
					
					bpmnReportModelsChangedELements.add(bpmnReportModel);
					
				}
			}
		}
		
		setExecutionTime(calculateExecutionTime(startMoment));
		
	}

	public void calculateInpactedActivities() {
		
		for ( ModelElementInstance element: changedElements ) {	
			
			Collection<String> targetActivities = CIABpmnUtil.getTargetsElementId(element, getModelElementsUpdated());
			
			inpactedActivitiesSteps(targetActivities, element.getAttributeValue("name"), ACTIVITY, CONTROL_FLOW_DIFFERENT_DEPENDENCY, getSteps(), getModelElementsUpdated());
			
			Collection<String> dataAssociation = CIABpmnUtil.getDataAssociationElements( element, CIABpmnUtil.convertToCollectionActvity(getModelElementsUpdated()));
			
			bpmnReportModels.addAll( validateDataAssociationElements( dataAssociation, element, getModelElementsOld(), CONTROL_FLOW_DIFFERENT_DEPENDENCY ) );
			
		}
	}
}
