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
	
	private Collection<CIABpmnReportModel> bpmnReportModels;
	
	private Collection< ModelElementInstance> changedArtifacts;
	
	private Collection< ModelElementInstance> oldArtifactElements, updatedArtifactElements;
	
	public ArtitactChanges() {
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
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
				
				calculateInpactedActivities(equivalentElement, CIABpmnUtil.convertToCollectionActvity(getModelElementsUpdated()), "Inserted Artifact" );
				
				changedArtifacts.add(equivalentElement);
				
			}			
			
		}
		
		for ( ModelElementInstance element: getOldArtifactElements() ) {
			
			ModelElementInstance equivalentElement = ( getEquivalentElement( element ) != null ) ? getEquivalentElement( element ) : element;

			if( !CIABpmnUtil.elementExistByName( equivalentElement, getUpdatedArtifactElements() ) ) {
				
				calculateInpactedActivities(equivalentElement, CIABpmnUtil.convertToCollectionActvity(getModelElementsOld()), "Removed Artifact" ); 
				
				changedArtifacts.add(equivalentElement);
				
			}
		}
		
		setExecutionTime( calculateExecutionTime( startMoment ) );
	}
	
	
	private void calculateInpactedActivities(ModelElementInstance element, Collection<Activity> activities, String chageType) {
					
		for(Activity at: activities) {
			
			Collection<DataOutputAssociation> dataOutputAssociations = at.getDataOutputAssociations();
			
			for(DataOutputAssociation doa: dataOutputAssociations ) {
				
				if( doa.getTarget().getId().equalsIgnoreCase(element.getAttributeValue("id"))) {
					
					CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel(element.getAttributeValue("name"), "Artifact", chageType, at.getAttributeValue("name"));
					
					bpmnReportModels.add(bpmnReportModel);
					
				}
			}
		}
	}
	
}
