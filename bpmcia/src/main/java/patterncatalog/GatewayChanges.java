package patterncatalog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import bpmcia.CIABpmnReportModel;
import bpmcia.CIABpmnUtil;

public class GatewayChanges extends ChangePattern{
	
	private static final String GATEWAYS_WITH_ID = "Gateways with ID: ";

	private static final String GATEWAY = "Gateway";

	private static final String REMOVED_GATEWAY = "Removed Gateway";

	private static final String INSERTED_GATEWAY = "Inserted Gateway";

	private Collection< ModelElementInstance> changedGateways;
	
	private Collection< ModelElementInstance> gatewayElementsOld, gatewayElementsUpdated;
	
	public GatewayChanges() {
		
		this.bpmnReportModelsChangedELements = new ArrayList<CIABpmnReportModel>();
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
		this.bpmnReportModelsChangedELements = new ArrayList<CIABpmnReportModel>();
		
	}
	
	public GatewayChanges(Collection<ModelElementInstance> gatewayElementsOld, Collection<ModelElementInstance> gatewayElementsUpdated, Collection< ModelElementInstance> oldElements, Collection< ModelElementInstance> updatedElements, String steps) {
		
		this.gatewayElementsOld = gatewayElementsOld;
		
		this.gatewayElementsUpdated = gatewayElementsUpdated;
		
		this.modelElementsOld = oldElements;
		
		this.modelElementsUpdated = updatedElements;
		
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		
		this.executionTime = 0L;
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
		this.bpmnReportModelsChangedELements = new ArrayList<CIABpmnReportModel>();
		
		this.steps = steps;
		
	}

	public Collection<ModelElementInstance> getGatewayElementsOld() {
		return gatewayElementsOld;
	}

	public void setGatewayElementsOld(Collection<ModelElementInstance> gatewayElementsOld) {
		this.gatewayElementsOld = gatewayElementsOld;
	}

	public Collection<ModelElementInstance> getGatewayElementsUpdated() {
		return gatewayElementsUpdated;
	}

	public void setGatewayElementsUpdated(Collection<ModelElementInstance> gatewayElementsUpdated) {
		this.gatewayElementsUpdated = gatewayElementsUpdated;
	}

	public Collection<CIABpmnReportModel> getBpmnReportModels() {
		return bpmnReportModels;
	}

	public void setBpmnReportModels(Collection<CIABpmnReportModel> bpmnReportModels) {
		this.bpmnReportModels = bpmnReportModels;
	}
	
	public Collection<ModelElementInstance> getChangedGateways() {
		return changedGateways;
	}

	public void setChangedGateways(Collection<ModelElementInstance> changedGateways) {
		this.changedGateways = changedGateways;
	}

	public void execute() {
		
		changedGateways = new ArrayList<ModelElementInstance>();
		
		Instant startMoment = Instant.now();
		
		for ( ModelElementInstance element: getGatewayElementsUpdated() ) {
			
			ModelElementInstance equivalentElement = ( getEquivalentElement( element ) != null ) ? getEquivalentElement( element ) : element;

			if( !CIABpmnUtil.elementExist( equivalentElement, getGatewayElementsOld() ) ) {				
				
				changedGateways.add(equivalentElement);
				
				CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel(GATEWAYS_WITH_ID + equivalentElement.getAttributeValue("id"), GATEWAY, INSERTED_GATEWAY, "", 1);
				
				bpmnReportModelsChangedELements.add(bpmnReportModel);
				
				calculateInpactedActivities( equivalentElement, getModelElementsUpdated(), INSERTED_GATEWAY ); 
				
				
			}			
			
		}
		
		for ( ModelElementInstance element: getGatewayElementsOld() ) {
			
			ModelElementInstance equivalentElement = ( getEquivalentElement( element ) != null ) ? getEquivalentElement( element ) : element;

			if( !CIABpmnUtil.elementExist( equivalentElement, getGatewayElementsUpdated() ) ) {
				
				changedGateways.add(equivalentElement);
				
				CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel(GATEWAYS_WITH_ID + equivalentElement.getAttributeValue("id"), GATEWAY, REMOVED_GATEWAY, "", 1);
				
				bpmnReportModelsChangedELements.add(bpmnReportModel);
				
				calculateInpactedActivities(equivalentElement, getModelElementsOld(), REMOVED_GATEWAY ); 
				
			}
		}
		
		setExecutionTime( calculateExecutionTime( startMoment ) );
	}
	
	
	private void calculateInpactedActivities(ModelElementInstance element, Collection<ModelElementInstance> elements, String changeType) {
		
		Collection<String> targetActivities = CIABpmnUtil.getTargetsElementId( element, elements );
		
		inpactedActivitiesSteps(targetActivities, element.getAttributeValue("name"), GATEWAY, changeType, getSteps(), elements);
	
	}
	
}
