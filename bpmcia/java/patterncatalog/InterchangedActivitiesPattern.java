package patterncatalog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import bpmcia.CIABpmnReportModel;
import bpmcia.CIABpmnUtil;

public class InterchangedActivitiesPattern extends ChangePattern{
	private Collection< ModelElementInstance> interchangedElements;
	private Collection<CIABpmnReportModel> bpmnReportModels;
	
	public InterchangedActivitiesPattern() {
		
		this.interchangedElements = new ArrayList<ModelElementInstance>();
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
	}
	
	public InterchangedActivitiesPattern(Collection< ModelElementInstance> activityElements1, Collection< ModelElementInstance> activityElements2) {
		
		this.interchangedElements = new ArrayList<ModelElementInstance>();
		
		this.modelElementsOld = activityElements1;
		
		this.modelElementsUpdated = activityElements2;
		
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		
		this.executionTime = 0L;
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
	}
	
	public Collection<ModelElementInstance> getInterchangedElements() {
		return interchangedElements;
	}
	
	public void setInterchangedElements(Collection<ModelElementInstance> insertedElements) {
		this.interchangedElements = insertedElements;
	}
	
	public Collection<CIABpmnReportModel> getBpmnReportModels() {
		return bpmnReportModels;
	}
	
	public void setBpmnReportModels(Collection<CIABpmnReportModel> bpmnReportModels) {
		this.bpmnReportModels = bpmnReportModels;
	}
	
	public void execute() {
		
		setInterchangedElements( new ArrayList<ModelElementInstance>() );
		
		Instant startMoment = Instant.now();
		
		for ( ModelElementInstance element: getModelElementsOld() ) {
			
			ModelElementInstance equivalentElement = ( getEquivalentElement( element ) != null ) ? getEquivalentElement( element ) : element;

			if( CIABpmnUtil.elementExist( equivalentElement, getModelElementsUpdated() ) ) {
				
				ModelElementInstance updatedEquivalentElement = CIABpmnUtil.getEquivalentElement(equivalentElement, getModelElementsUpdated());
				
				String oldElementName = equivalentElement.getAttributeValue("name");
				
				String updateElementName = updatedEquivalentElement.getAttributeValue("name");
				
				if( !oldElementName.equalsIgnoreCase(updateElementName) ) {
					interchangedElements.add(updatedEquivalentElement);
				}				
			}
		}
		
		setExecutionTime( calculateExecutionTime( startMoment ) );
	}
	
	public void calculateInpactedActivities() {
		
		for ( ModelElementInstance element: interchangedElements ) {	
			
			Collection<String> targetActivities = CIABpmnUtil.getTargetsElementId(element, getModelElementsUpdated());
			
			for( String targetId: targetActivities ) {
				
				ModelElementInstance targetElement = CIABpmnUtil.getElement(targetId, getModelElementsUpdated());
				
				if( targetElement != null ) {
					
					CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel(element.getAttributeValue("name"), "Activity", "Interchanged Activity", targetElement.getAttributeValue( "name" ) );
				
					bpmnReportModels.add( bpmnReportModel );
				}
			}
			
			Collection<String> dataAssociation = CIABpmnUtil.getDataAssociationElements( element, CIABpmnUtil.convertToCollectionActvity(getModelElementsUpdated() ) );
			
			bpmnReportModels.addAll( validateDataAssociationElements( dataAssociation, element, getModelElementsUpdated(), "Interchanged Activity"  ) );
		}
	}
}
