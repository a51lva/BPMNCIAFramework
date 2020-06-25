package patterncatalog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import bpmcia.CIABpmnReportModel;
import bpmcia.CIABpmnUtil;

public class InsertedActivitiesPattern extends ChangePattern{
	
	private Collection< ModelElementInstance> insertedElements;
	private Collection<CIABpmnReportModel> bpmnReportModels;
	
	public InsertedActivitiesPattern() {
		
		this.insertedElements = new ArrayList<ModelElementInstance>();
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
	}
	
	public InsertedActivitiesPattern(Collection< ModelElementInstance> activityElements1, Collection< ModelElementInstance> activityElements2) {
		
		this.insertedElements = new ArrayList<ModelElementInstance>();
		
		this.modelElementsOld = activityElements1;
		
		this.modelElementsUpdated = activityElements2;
		
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		
		this.executionTime = 0L;
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
	}
	
	public Collection<ModelElementInstance> getInsertedElements() {
		return insertedElements;
	}
	
	public void setInsertedElements(Collection<ModelElementInstance> insertedElements) {
		this.insertedElements = insertedElements;
	}
	
	public Collection<CIABpmnReportModel> getBpmnReportModels() {
		return bpmnReportModels;
	}
	
	public void setBpmnReportModels(Collection<CIABpmnReportModel> bpmnReportModels) {
		this.bpmnReportModels = bpmnReportModels;
	}
	
	public void execute() {
		
		setInsertedElements( new ArrayList<ModelElementInstance>() );
		
		Instant startMoment = Instant.now();
		
		for ( ModelElementInstance element: getModelElementsUpdated() ) {
			
			ModelElementInstance equivalentElement = ( getEquivalentElement( element ) != null ) ? getEquivalentElement( element ) : element;

			if( !CIABpmnUtil.elementExist( equivalentElement, getModelElementsOld() ) ) {
				
				insertedElements.add(equivalentElement);
				
			}
		}
		
		setExecutionTime( calculateExecutionTime( startMoment ) );
	}
	
	public void calculateInpactedActivities() {
		
		for ( ModelElementInstance element: insertedElements ) {	
			
			Collection<String> targetActivities = CIABpmnUtil.getTargetsElementId(element, getModelElementsUpdated());
			
			for( String targetId: targetActivities ) {
				
				ModelElementInstance targetElement = CIABpmnUtil.getElement(targetId, getModelElementsUpdated());
				
				if( targetElement != null ) {
					
					CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel(element.getAttributeValue("name"), "Activity", "Inserted Activity", targetElement.getAttributeValue("name"));
				
					bpmnReportModels.add(bpmnReportModel);
				}
			}
			
			Collection<String> dataAssociation = CIABpmnUtil.getDataAssociationElements( element, CIABpmnUtil.convertToCollectionActvity(getModelElementsUpdated()));
			
			bpmnReportModels.addAll( validateDataAssociationElements( dataAssociation, element, getModelElementsOld(), "Inserted Activity" ) );
			
		}
	}
}
