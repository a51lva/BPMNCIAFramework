package bpmcia;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

public class CIABpmnUtil {

	public static final boolean isGatewayElement(String id, Collection<Gateway> gateways) {
		return gateways.stream().anyMatch(el -> {
			boolean result = el.getId().equalsIgnoreCase(id);
			
			return result;
		});
	}
	
	public static final boolean isActivityElement(String id, Collection<ModelElementInstance> activities) {
		return activities.stream().anyMatch(el -> {
			boolean result = el.getAttributeValue("id").equalsIgnoreCase(id);			
			return result;
		});
	}
	
	public static final Collection<String> getDependenciesElementId( ModelElementInstance element, Collection<Gateway> gateways) {
		
		FlowNode node = (FlowNode)element;
		
		Collection<SequenceFlow> incomings = node.getIncoming();
		
		Collection<String> result = new ArrayList<String>();
		
				
		for (SequenceFlow sf:incomings) {
			
			String sourceID = sf.getSource().getId();
			
			if(isGatewayElement(sourceID, gateways)) {
				return getDependenciesElementId((ModelElementInstance)sf.getSource(), gateways);				
			}else {						
				result.add(sourceID);
			}
		}
		
		return result;
	}
	
	public static final Collection<String> getTargetsElementId( ModelElementInstance element, Collection<ModelElementInstance> activities) {
		
		FlowNode node = (FlowNode)element;
		
		Collection<SequenceFlow> outgoings = node.getOutgoing();
		
		Collection<String> result = new ArrayList<String>();
		
				
		for (SequenceFlow sf:outgoings) {
			
			String targetID = sf.getTarget().getId();
			
			if(isActivityElement(targetID, activities)) {
				result.add(targetID);								
			}else {						
				return getTargetsElementId((ModelElementInstance)sf.getTarget(), activities);
			}
		}
		
		return result;
	}
	
	public static final File getFileFromResources(String fileName, ClassLoader classLoader) {

        URL resource = classLoader.getResource(fileName);
        
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }
	
	public static final Collection<ModelElementInstance> getActivityElements(BpmnModelInstance modelInstance){
		ModelElementType activityType = modelInstance.getModel().getType(Activity.class);
		
		return modelInstance.getModelElementsByType(activityType);
	}
	
	public static final Collection<ModelElementInstance> getGatewayElements(BpmnModelInstance modelInstance){
		ModelElementType gatewayType = modelInstance.getModel().getType(Gateway.class);
		
		return modelInstance.getModelElementsByType(gatewayType);
	}
	
	public static final Collection<Gateway> convertToCollectionGateway(Collection< ModelElementInstance> gateways){
		
		Collection<Gateway> result = new ArrayList<Gateway>();
		
		if(gateways != null) {
			for(ModelElementInstance gateway: gateways){
				result.add((Gateway)gateway);
			}
		}
		
		return result;
	}
	
	public static final boolean elementExist(ModelElementInstance element, Collection<ModelElementInstance> elements) {
		boolean result = elements.stream().anyMatch(el -> el.getAttributeValue("id").equalsIgnoreCase(element.getAttributeValue("id")));
		return result;
	}
	
	public static final ModelElementInstance getEquivalentElement(ModelElementInstance element, Collection<ModelElementInstance> elements) {
		if(elementExist(element, elements)) {
			Optional<ModelElementInstance> opElement = elements.stream().filter(el -> el.getAttributeValue("id").equalsIgnoreCase(element.getAttributeValue("id"))).findFirst();
			
			return opElement.get();
		}
		
		return null;
	}
	
	public static final ModelElementInstance getElement(String elementId, Collection<ModelElementInstance> elements) {
		try {
		Optional<ModelElementInstance> opElement = elements.stream().filter(el -> el.getAttributeValue("id").equalsIgnoreCase(elementId)).findFirst();
			if(opElement != null) {
				return opElement.get();
			}
		}catch(NoSuchElementException ex) {
			return null;
		}		
		
		return null;
	}

}
