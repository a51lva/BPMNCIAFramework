package patterncatalog;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import bpmcia.CIABpmnReportModel;
import bpmcia.CIABpmnUtil;

public abstract class ChangePattern{
	
	Collection< ModelElementInstance> modelElementsOld;
	
	Collection< ModelElementInstance> modelElementsUpdated;
	
	HashMap<String, ModelElementInstance> equivalentMapElements;
	
	String steps;
	
	Long executionTime = 0L;
	
	Collection<CIABpmnReportModel> bpmnReportModels;
	
	Collection<CIABpmnReportModel> bpmnReportModelsChangedELements;
	
	public ChangePattern() {
		this.executionTime = 0L;
		this.modelElementsOld = new ArrayList<ModelElementInstance>();
		this.modelElementsUpdated = new ArrayList<ModelElementInstance>();
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		this.steps = "1";
	}
	
	public Long getExecutionTime() {
		return executionTime;
	}
	
	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}
	
	public Collection<ModelElementInstance> getModelElementsOld() {
		return modelElementsOld;
	}
	
	public void setModelElementsOld(Collection<ModelElementInstance> modelElements) {
		this.modelElementsOld = modelElements;
	}
	
	public Collection<ModelElementInstance> getModelElementsUpdated() {
		return modelElementsUpdated;
	}
	
	public void setModelElementsUpdated(Collection<ModelElementInstance> modelElements) {
		this.modelElementsUpdated = modelElements;
	}
	
	public HashMap<String, ModelElementInstance> getEquivalentMapElements() {
		return equivalentMapElements;
	}
	
	public void setEquivalentMapElements(HashMap<String, ModelElementInstance> equivalentMapElements) {
		this.equivalentMapElements = equivalentMapElements;
	}
	
	public String getSteps() {
		return steps;
	}
	
	public void setSteps(String steps) {
		this.steps = steps;
	}
	
	public Collection<CIABpmnReportModel> getBpmnReportModels() {
		return bpmnReportModels;
	}

	public void setBpmnReportModels(Collection<CIABpmnReportModel> bpmnReportModels) {
		this.bpmnReportModels = bpmnReportModels;
	}

	public Collection<CIABpmnReportModel> getBpmnReportModelsChangedELements() {
		return bpmnReportModelsChangedELements;
	}

	public void setBpmnReportModelsChangedELements(Collection<CIABpmnReportModel> bpmnReportModelsChangedELements) {
		this.bpmnReportModelsChangedELements = bpmnReportModelsChangedELements;
	}

	public Long calculateExecutionTime(Instant startMoment) {
		Instant endMoment = Instant.now();
		return Duration.between(startMoment, endMoment).toMillis();
	}
	
	public String getExecutionTimeFormatted() {
		org.joda.time.Duration jDuration = new org.joda.time.Duration(getExecutionTime()); 
		PeriodFormatter formatter = new PeriodFormatterBuilder()
			     .appendDays()
			     .appendSuffix("d")
			     .appendHours()
			     .appendSuffix("h")
			     .appendMinutes()
			     .appendSuffix("m")
			     .appendSeconds()
			     .appendSuffix("s")
			     .toFormatter();
		String formatted = formatter.print(jDuration.toPeriod());
		
		return formatted;
	}
	
	
	public ModelElementInstance getEquivalentElement(ModelElementInstance element) {
		String elementId = element.getAttributeValue("id");
		
		return getEquivalentMapElements().get(elementId);
	}
	
	public Collection<CIABpmnReportModel> validateDataAssociationElements(Collection<String> dataAssociationIDs, ModelElementInstance element, Collection<ModelElementInstance> modelElements, String changeType){
		
		Collection<CIABpmnReportModel> bpmnReportModels = new ArrayList<CIABpmnReportModel>();
		
		for(String targetId: dataAssociationIDs) {
			
			ModelElementInstance targetElement = CIABpmnUtil.getElement(targetId, modelElements);
			
			if( targetElement != null ) {
				
				CIABpmnReportModel bpmnReportModel = new CIABpmnReportModel(element.getAttributeValue("name"), "Activity", changeType, targetElement.getAttributeValue("name"));
				
				bpmnReportModels.add(bpmnReportModel);
			}
		}
		
		return bpmnReportModels;
	}
}
