package bpmcia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import patterncatalog.ControlFlowDifferentDependenciesPattern;
import patterncatalog.InsertedActivitiesPattern;
import patterncatalog.RemovedActivitiesPattern;

public class CIACalculation {
	private CIABpmnModelInstance oldModel;
	private CIABpmnModelInstance updatedModel;

	private RemovedActivitiesPattern removedActivitiesPattern;
	private InsertedActivitiesPattern insertedActivitiesPattern;
	private ControlFlowDifferentDependenciesPattern controlFlowDifferencePattern;
	
	
	private Collection< ModelElementInstance> removedActivities;
	private Collection< ModelElementInstance> insertedActivities;
	private Collection< ModelElementInstance> incomeSequenceFlowChangedActivities;
	
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
	
	public void setInsertedActivities(Collection<ModelElementInstance> insertedActivities) {
		this.insertedActivities = insertedActivities;
	}
	
	public ControlFlowDifferentDependenciesPattern getControlFlowDifferencePattern() {
		return controlFlowDifferencePattern;
	}
	
	public void setControlFlowDifferencePattern(ControlFlowDifferentDependenciesPattern controlFlowDifferencePattern) {
		this.controlFlowDifferencePattern = controlFlowDifferencePattern;
	}
	
	public Collection<ModelElementInstance> getRemovedActivities() {
		return removedActivities;
	}
	
	public void setRemovedActivities(Collection<ModelElementInstance> removedActivities) {
		this.removedActivities = removedActivities;
	}
	
	public Collection<ModelElementInstance> getInsertedActivities() {
		return insertedActivities;
	}
	
	public void setInsertedActivitiesPattern(InsertedActivitiesPattern insertedActivitiesPattern) {
		this.insertedActivitiesPattern = insertedActivitiesPattern;
	}
	
	public Collection<ModelElementInstance> getIncomeSequenceFlowChangedActivities() {
		return incomeSequenceFlowChangedActivities;
	}
	
	public void setIncomeSequenceFlowChangedActivities(
			Collection<ModelElementInstance> incomeSequenceFlowChangedActivities) {
		this.incomeSequenceFlowChangedActivities = incomeSequenceFlowChangedActivities;
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
		
		setRemovedActivities(removedActivitiesPattern.getRemovedElements());
		setInsertedActivities(insertedActivitiesPattern.getInsertedElements());
		setIncomeSequenceFlowChangedActivities(controlFlowDifferencePattern.getChangedElements());
		
		calculateDirectedInpactedActivities();
	}
	
	private void calculateDirectedInpactedActivities() {
		removedActivitiesPattern.calculateDirectedInpactedActivities();
		insertedActivitiesPattern.calculateDirectedInpactedActivities();
		controlFlowDifferencePattern.calculateDirectedInpactedActivities();
		
		getBpmnReportModels().addAll(insertedActivitiesPattern.getBpmnReportModels());
		getBpmnReportModels().addAll(removedActivitiesPattern.getBpmnReportModels());
		getBpmnReportModels().addAll(controlFlowDifferencePattern.getBpmnReportModels());
	}
}
