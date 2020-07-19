package patterncatalog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import bpmcia.CIABpmnReportModel;
import bpmcia.CIABpmnUtil;

public class InsertedActivitiesPattern extends ChangePattern{
	
	private static final String ACTIVITY = "Activity";
	
	private static final String INSERTED_ACTIVITY = "Inserted Activity";
	
	private Collection< ModelElementInstance> insertedElements;
	
	public InsertedActivitiesPattern() {
		
		this.insertedElements = new ArrayList<ModelElementInstance>();
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>(); 
		
		this.bpmnReportModelsChangedELements = new ArrayList<CIABpmnReportModel>();
		
	}
	
	public InsertedActivitiesPattern(Collection< ModelElementInstance> activityElements1, Collection< ModelElementInstance> activityElements2, String steps) {
		
		this.insertedElements = new ArrayList<ModelElementInstance>();
		
		this.modelElementsOld = activityElements1;
		
		this.modelElementsUpdated = activityElements2;
		
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		
		this.executionTime = 0L;
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
		this.bpmnReportModelsChangedELements = new ArrayList<CIABpmnReportModel>();
		
		this.steps = steps;
		
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
				
				CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel( equivalentElement.getAttributeValue("name"), ACTIVITY, INSERTED_ACTIVITY, "");
				
				bpmnReportModelsChangedELements.add(bpmnReportModel);
				
			}
		}
		
		setExecutionTime( calculateExecutionTime( startMoment ) );
	}
	
	public void calculateInpactedActivities() {
		
		for ( ModelElementInstance element: insertedElements ) {	
			
			Collection<String> targetActivities = CIABpmnUtil.getTargetsElementId(element, getModelElementsUpdated());
			
			inpactedActivitiesSteps(targetActivities, element.getAttributeValue("name"), ACTIVITY, INSERTED_ACTIVITY, getSteps(), getModelElementsUpdated());
			
			Collection<String> dataAssociation = CIABpmnUtil.getDataAssociationElements( element, CIABpmnUtil.convertToCollectionActvity(getModelElementsUpdated()));
			
			bpmnReportModels.addAll( validateDataAssociationElements( dataAssociation, element, getModelElementsOld(), INSERTED_ACTIVITY ) );
			
		}
	}
}
