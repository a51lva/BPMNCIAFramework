package bpmcia;

import java.util.ArrayList;
import java.util.Collection;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import patterncatalog.ArtitactChanges;
import patterncatalog.ControlFlowDifferentDependenciesPattern;
import patterncatalog.GatewayChanges;
import patterncatalog.InsertedActivitiesPattern;
import patterncatalog.InterchangedActivitiesPattern;
import patterncatalog.RemovedActivitiesPattern;

public class CIACalculation {
	private CIABpmnModelInstance oldModel;
	private CIABpmnModelInstance updatedModel;

	private RemovedActivitiesPattern removedActivitiesPattern;
	private InsertedActivitiesPattern insertedActivitiesPattern;
	private ControlFlowDifferentDependenciesPattern controlFlowDifferencePattern;
	private InterchangedActivitiesPattern interchangedActivitiesPattern;
	private GatewayChanges gatewayChanges;	
	private ArtitactChanges artitactChanges;
	
	private Collection< ModelElementInstance> changedElements;	
	
	private Collection<CIABpmnReportModel> bpmnReportModels;
	
	public CIACalculation(CIABpmnModelInstance oldModel, CIABpmnModelInstance updatedModel) {
		
		this.oldModel = oldModel;
		
		this.updatedModel = updatedModel;
		
		this.removedActivitiesPattern = new RemovedActivitiesPattern(
																	  	CIABpmnUtil.getActivityElements(this.oldModel.getModelInstance()), 
																	  	CIABpmnUtil.getActivityElements(this.updatedModel.getModelInstance())
																	);
		
		this.insertedActivitiesPattern = new InsertedActivitiesPattern(
																			CIABpmnUtil.getActivityElements(this.oldModel.getModelInstance()), 
																			CIABpmnUtil.getActivityElements(this.updatedModel.getModelInstance())
																	  );
		
		this.controlFlowDifferencePattern = new ControlFlowDifferentDependenciesPattern(
																							CIABpmnUtil.getActivityElements(this.oldModel.getModelInstance()), 
																							CIABpmnUtil.getActivityElements(this.updatedModel.getModelInstance()), 
																							CIABpmnUtil.convertToCollectionGateway(CIABpmnUtil.getGatewayElements(this.oldModel.getModelInstance())), 
																							CIABpmnUtil.convertToCollectionGateway(CIABpmnUtil.getGatewayElements(this.updatedModel.getModelInstance()))
																						);
		this.interchangedActivitiesPattern = new InterchangedActivitiesPattern(
																				CIABpmnUtil.getActivityElements(this.oldModel.getModelInstance()), 
																				CIABpmnUtil.getActivityElements(this.updatedModel.getModelInstance())
																			  );
		
		this.gatewayChanges = new GatewayChanges(
													CIABpmnUtil.getGatewayElements(this.oldModel.getModelInstance()), 
													CIABpmnUtil.getGatewayElements(this.updatedModel.getModelInstance()), 
													CIABpmnUtil.getActivityElements(this.oldModel.getModelInstance()), 
													CIABpmnUtil.getActivityElements(this.updatedModel.getModelInstance())
												);
		
		this.artitactChanges = new ArtitactChanges(
													CIABpmnUtil.getDataObjectReferenceElements(this.oldModel.getModelInstance()), 
													CIABpmnUtil.getDataObjectReferenceElements(this.updatedModel.getModelInstance()), 
													CIABpmnUtil.getActivityElements(this.oldModel.getModelInstance()), 
													CIABpmnUtil.getActivityElements(this.updatedModel.getModelInstance())
												  );
		
		this.bpmnReportModels = new ArrayList<CIABpmnReportModel>();
	}
	
	public CIABpmnModelInstance getOldModel() {
		return oldModel;
	}
	
	public void setOldModel(CIABpmnModelInstance oldModel) {
		this.oldModel = oldModel;
	}
	
	public CIABpmnModelInstance getUpdatedModel() {
		return updatedModel;
	}
	
	public void setUpdatedModel(CIABpmnModelInstance updatedModel) {
		this.updatedModel = updatedModel;
	}
	
	public RemovedActivitiesPattern getRemovedActivitiesPattern() {
		return removedActivitiesPattern;
	}
	
	public void setRemovedActivitiesPattern(RemovedActivitiesPattern removedActivitiesPattern) {
		this.removedActivitiesPattern = removedActivitiesPattern;
	}
	
	public InsertedActivitiesPattern getInsertedActivitiesPattern() {
		return insertedActivitiesPattern;
	}
	
	public ControlFlowDifferentDependenciesPattern getControlFlowDifferencePattern() {
		return controlFlowDifferencePattern;
	}
	
	public void setControlFlowDifferencePattern(ControlFlowDifferentDependenciesPattern controlFlowDifferencePattern) {
		this.controlFlowDifferencePattern = controlFlowDifferencePattern;
	}
	
	public InterchangedActivitiesPattern getInterchangedActivitiesPattern() {
		return interchangedActivitiesPattern;
	}
	
	public void setInterchangedActivitiesPattern(InterchangedActivitiesPattern interchangedActivitiesPattern) {
		this.interchangedActivitiesPattern = interchangedActivitiesPattern;
	}	
	
	public GatewayChanges getGatewayChanges() {
		return gatewayChanges;
	}

	public ArtitactChanges getArtitactChanges() {
		return artitactChanges;
	}

	public void setArtitactChanges(ArtitactChanges artitactChanges) {
		this.artitactChanges = artitactChanges;
	}

	public void setGatewayChanges(GatewayChanges gatewayChanges) {
		this.gatewayChanges = gatewayChanges;
	}

	public Collection<ModelElementInstance> getChangedElements() {
		return changedElements;
	}

	public void setChangedElements(Collection<ModelElementInstance> changedElements) {
		this.changedElements = changedElements;
	}

	public void setInsertedActivitiesPattern(InsertedActivitiesPattern insertedActivitiesPattern) {
		this.insertedActivitiesPattern = insertedActivitiesPattern;
	}
	
	public Collection<CIABpmnReportModel> getBpmnReportModels() {
		return bpmnReportModels;
	}
	
	public void setBpmnReportModels(Collection<CIABpmnReportModel> bpmnReportModels) {
		this.bpmnReportModels = bpmnReportModels;
	}
	
	public void execute() {
		
		removedActivitiesPattern.execute();
		
		insertedActivitiesPattern.execute();
		
		controlFlowDifferencePattern.execute();
		
		interchangedActivitiesPattern.execute();
		
		gatewayChanges.execute();
		
		artitactChanges.execute();
		
		setChangedElements(removedActivitiesPattern.getRemovedElements());
		setChangedElements(insertedActivitiesPattern.getInsertedElements());
		setChangedElements(controlFlowDifferencePattern.getChangedElements());
		setChangedElements(interchangedActivitiesPattern.getInterchangedElements());
		
		calculateInpactedActivities();
		
	}
	
	private void calculateInpactedActivities() {
		
		removedActivitiesPattern.calculateInpactedActivities();
		
		insertedActivitiesPattern.calculateInpactedActivities();
		
		controlFlowDifferencePattern.calculateInpactedActivities();
		
		interchangedActivitiesPattern.calculateInpactedActivities();
		
		getBpmnReportModels().addAll(insertedActivitiesPattern.getBpmnReportModels());
		getBpmnReportModels().addAll(removedActivitiesPattern.getBpmnReportModels());
		getBpmnReportModels().addAll(controlFlowDifferencePattern.getBpmnReportModels());
		getBpmnReportModels().addAll(interchangedActivitiesPattern.getBpmnReportModels());
		getBpmnReportModels().addAll(gatewayChanges.getBpmnReportModels());
		getBpmnReportModels().addAll(artitactChanges.getBpmnReportModels());
		
	}
}
