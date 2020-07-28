package patterncatalog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import bpmcia.CIABpmnReportModel;
import bpmcia.CIABpmnUtil;

public class InterchangedActivitiesPattern extends ChangePattern{
	
	private static final String INTERCHANGED_ACTIVITY = "Updated Activity";
	
	private static final String ACTIVITY = "Activity";
	
	private Collection< ModelElementInstance> interchangedElements;
	
	public InterchangedActivitiesPattern() {
		
		this.interchangedElements = new ArrayList<ModelElementInstance>();
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
		this.bpmnReportModelsChangedELements = new ArrayList<CIABpmnReportModel>();
		
	}
	
	public InterchangedActivitiesPattern(Collection< ModelElementInstance> activityElements1, Collection< ModelElementInstance> activityElements2, String steps) {
		
		this.interchangedElements = new ArrayList<ModelElementInstance>();
		
		this.modelElementsOld = activityElements1;
		
		this.modelElementsUpdated = activityElements2;
		
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		
		this.executionTime = 0L;
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
		this.bpmnReportModelsChangedELements = new ArrayList<CIABpmnReportModel>();
		
		this.steps = steps;
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
	
	public String getSteps() {
		return steps;
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
					
					CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel( updatedEquivalentElement.getAttributeValue("name"), ACTIVITY, INTERCHANGED_ACTIVITY, "", 1);
					
					bpmnReportModelsChangedELements.add(bpmnReportModel);
				}				
			}
		}
		
		setExecutionTime( calculateExecutionTime( startMoment ) );
	}
	
	public void calculateInpactedActivities() {
		
		for ( ModelElementInstance element: interchangedElements ) {	
			
			Collection<String> targetActivities = CIABpmnUtil.getTargetsElementId(element, getModelElementsUpdated());
			
			inpactedActivitiesSteps(targetActivities, element.getAttributeValue("name"), ACTIVITY, INTERCHANGED_ACTIVITY, getSteps(), getModelElementsUpdated());
			
			Collection<String> dataAssociation = CIABpmnUtil.getDataAssociationElements( element, CIABpmnUtil.convertToCollectionActvity(getModelElementsUpdated() ) );
			
			bpmnReportModels.addAll( validateDataAssociationElements( dataAssociation, element, getModelElementsUpdated(), INTERCHANGED_ACTIVITY  ) );
		}
	}
}
