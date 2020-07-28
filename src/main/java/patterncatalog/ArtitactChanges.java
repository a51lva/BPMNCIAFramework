package patterncatalog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.DataOutputAssociation;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import bpmcia.CIABpmnReportModel;
import bpmcia.CIABpmnUtil;

public class ArtitactChanges extends ChangePattern{
	
	private static final String ARTIFACT = "Artifact";

	private static final String REMOVED_ARTIFACT = "Removed Artifact";

	private static final String INSERTED_ARTIFACT = "Inserted Artifact";

	private Collection< ModelElementInstance> changedArtifacts;
	
	private Collection< ModelElementInstance> oldArtifactElements, updatedArtifactElements;
	
	public ArtitactChanges() {
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
		this.bpmnReportModelsChangedELements = new ArrayList<CIABpmnReportModel>();
		
		this.changedArtifacts = new ArrayList<ModelElementInstance>();
		
	}
	
	public ArtitactChanges(Collection<ModelElementInstance> oldArtifactElements, Collection<ModelElementInstance> updatedArtifactElements, Collection< ModelElementInstance> oldElements, Collection< ModelElementInstance> updatedElements) {
		
		this.oldArtifactElements = oldArtifactElements;
		
		this.updatedArtifactElements = updatedArtifactElements;
		
		this.modelElementsOld = oldElements;
		
		this.modelElementsUpdated = updatedElements;
		
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		
		this.executionTime = 0L;
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
		this.bpmnReportModelsChangedELements = new ArrayList<CIABpmnReportModel>();
		
	}	

	public Collection<CIABpmnReportModel> getBpmnReportModels() {
		return bpmnReportModels;
	}

	public void setBpmnReportModels(Collection<CIABpmnReportModel> bpmnReportModels) {
		this.bpmnReportModels = bpmnReportModels;
	}

	public Collection<ModelElementInstance> getChangedArtifacts() {
		return changedArtifacts;
	}

	public void setChangedArtifacts(Collection<ModelElementInstance> changedArtifacts) {
		this.changedArtifacts = changedArtifacts;
	}

	public Collection<ModelElementInstance> getOldArtifactElements() {
		return oldArtifactElements;
	}

	public void setOldArtifactElements(Collection<ModelElementInstance> oldArtifactElements) {
		this.oldArtifactElements = oldArtifactElements;
	}

	public Collection<ModelElementInstance> getUpdatedArtifactElements() {
		return updatedArtifactElements;
	}

	public void setUpdatedArtifactElements(Collection<ModelElementInstance> updatedArtifactElements) {
		this.updatedArtifactElements = updatedArtifactElements;
	}

	public void execute() {
		
		changedArtifacts = new ArrayList<ModelElementInstance>();
		
		Instant startMoment = Instant.now();
		
		for ( ModelElementInstance element: getUpdatedArtifactElements() ) {
			
			ModelElementInstance equivalentElement = ( getEquivalentElement( element ) != null ) ? getEquivalentElement( element ) : element;

			if( !CIABpmnUtil.elementExistByName( equivalentElement, getOldArtifactElements() ) ) {
				
				changedArtifacts.add(equivalentElement);
				
				CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel(equivalentElement.getAttributeValue("name"), ARTIFACT, INSERTED_ARTIFACT, "", 1);
				
				bpmnReportModelsChangedELements.add(bpmnReportModel);
				
				calculateInpactedActivities(equivalentElement, CIABpmnUtil.convertToCollectionActvity(getModelElementsUpdated()), INSERTED_ARTIFACT );
				
			}			
			
		}
		
		for ( ModelElementInstance element: getOldArtifactElements() ) {
			
			ModelElementInstance equivalentElement = ( getEquivalentElement( element ) != null ) ? getEquivalentElement( element ) : element;

			if( !CIABpmnUtil.elementExistByName( equivalentElement, getUpdatedArtifactElements() ) ) {
				
				changedArtifacts.add(equivalentElement);
				
				CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel(equivalentElement.getAttributeValue("name"), ARTIFACT, REMOVED_ARTIFACT, "", 1);

				bpmnReportModelsChangedELements.add(bpmnReportModel);
				
				calculateInpactedActivities(equivalentElement, CIABpmnUtil.convertToCollectionActvity(getModelElementsOld()), REMOVED_ARTIFACT ); 
								
			}
		}
		
		setExecutionTime( calculateExecutionTime( startMoment ) );
	}
	
	
	private void calculateInpactedActivities(ModelElementInstance element, Collection<Activity> activities, String changeType) {
					
		for(Activity at: activities) {
			
			Collection<DataOutputAssociation> dataOutputAssociations = at.getDataOutputAssociations();
			
			for(DataOutputAssociation doa: dataOutputAssociations ) {
				
				if( doa.getTarget().getId().equalsIgnoreCase(element.getAttributeValue("id"))) {
					
					CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel(element.getAttributeValue("name"), ARTIFACT, changeType, at.getAttributeValue("name"), 1);
					
					bpmnReportModels.add(bpmnReportModel);
					
				}
			}
		}
	}
	
}
