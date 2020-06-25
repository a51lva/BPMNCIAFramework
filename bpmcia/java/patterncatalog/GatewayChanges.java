package patterncatalog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import bpmcia.CIABpmnReportModel;
import bpmcia.CIABpmnUtil;

public class GatewayChanges extends ChangePattern{
	
	private Collection<CIABpmnReportModel> bpmnReportModels;
	
	private Collection< ModelElementInstance> changedGateways;
	
	private Collection< ModelElementInstance> gatewayElementsOld, gatewayElementsUpdated;
	
	public GatewayChanges() {
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
	}
	
	public GatewayChanges(Collection<ModelElementInstance> gatewayElementsOld, Collection<ModelElementInstance> gatewayElementsUpdated, Collection< ModelElementInstance> oldElements, Collection< ModelElementInstance> updatedElements) {
		
		this.gatewayElementsOld = gatewayElementsOld;
		
		this.gatewayElementsUpdated = gatewayElementsUpdated;
		
		this.modelElementsOld = oldElements;
		
		this.modelElementsUpdated = updatedElements;
		
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		
		this.executionTime = 0L;
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
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
				
				calculateInpactedActivities( equivalentElement, getModelElementsUpdated(), "Inserted Gateway" ); 
				
				changedGateways.add(equivalentElement);
				
			}			
			
		}
		
		for ( ModelElementInstance element: getGatewayElementsOld() ) {
			
			ModelElementInstance equivalentElement = ( getEquivalentElement( element ) != null ) ? getEquivalentElement( element ) : element;

			if( !CIABpmnUtil.elementExist( equivalentElement, getGatewayElementsUpdated() ) ) {
				
				calculateInpactedActivities(equivalentElement, getModelElementsOld(), "Removed Gateway" ); 
				
				changedGateways.add(equivalentElement);
				
			}
		}
		
		setExecutionTime( calculateExecutionTime( startMoment ) );
	}
	
	
	private void calculateInpactedActivities(ModelElementInstance element, Collection<ModelElementInstance> elements, String chageType) {
		
		Collection<String> targetActivities = CIABpmnUtil.getTargetsElementId( element, elements );
		
		for( String targetId: targetActivities ) {
			
			ModelElementInstance targetElement = CIABpmnUtil.getElement( targetId, elements );
			
			if( targetElement != null ) {
				
				CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel("Gateways with ID: " + element.getAttributeValue("id"), "Gateway", chageType, targetElement.getAttributeValue("name"));
			
				bpmnReportModels.add(bpmnReportModel);
			}
		}
	}
	
}
