package bpmcia;

import java.io.File;
import java.util.Collection;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

public class CIABpmnModelInstance {
	
	private Collection< ModelElementInstance> elements;
	private String filename;
	BpmnModelInstance modelInstance;

	public CIABpmnModelInstance(String filename) {
		this.filename = filename;		
		
		File file = CIABpmnUtil.getFileFromResources(filename, getClass().getClassLoader());		 
	    this.modelInstance = Bpmn.readModelFromFile(file);
	    
	    ModelElementType elementType = modelInstance.getModel().getType(FlowElement.class);
		
		this.elements = modelInstance.getModelElementsByType(elementType);		
	}
	
	public CIABpmnModelInstance(BpmnModelInstance modelInstance) {
		this.modelInstance = modelInstance;
	    
	    ModelElementType elementType = modelInstance.getModel().getType(FlowElement.class);
		
		this.elements = modelInstance.getModelElementsByType(elementType);		
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public Collection<ModelElementInstance> getElements() {
		return elements;
	}
	
	public void setElements(Collection<ModelElementInstance> elements) {
		this.elements = elements;
	}
	
	public BpmnModelInstance getModelInstance() {
		return modelInstance;
	}
	
	public void setModelInstance(BpmnModelInstance modelInstance) {
		this.modelInstance = modelInstance;
	}
	
	
}
