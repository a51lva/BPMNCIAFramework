package patterncatalog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class ControlFlowDifferentDependenciesPattern extends ChangePattern{
	private Collection< ModelElementInstance> changedElements;
	private Collection<Gateway> gatewayElementsOld;
	private Collection<Gateway> gatewayElementsUpdated;
	
	public ControlFlowDifferentDependenciesPattern() {}
	
	public ControlFlowDifferentDependenciesPattern(Collection< ModelElementInstance> elements1, Collection< ModelElementInstance> elements2) {
		this.changedElements = new ArrayList<ModelElementInstance>();
		this.modelElements1 = elements1;
		this.modelElements2 = elements2;
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		this.executionTime = 0L;
	}
	
	public Collection<ModelElementInstance> getChangedElements() {
		return changedElements;
	}
	
	public void setChangedElements(Collection<ModelElementInstance> changedElements) {
		this.changedElements = changedElements;
	}
	
	public Collection<Gateway> getGatewayElementsOld() {
		return gatewayElementsOld;
	}
	
	public void setGatewayElementsOld(Collection<Gateway> gatewayElementsOld) {
		this.gatewayElementsOld = gatewayElementsOld;
	}
	
	public Collection<Gateway> getGatewayElementsUpdated() {
		return gatewayElementsUpdated;
	}
	
	public void setGatewayElementsUpdated(Collection<Gateway> gatewayElementsUpdated) {
		this.gatewayElementsUpdated = gatewayElementsUpdated;
	}
	
	public void execute() {
		
		setChangedElements(new ArrayList<ModelElementInstance>());
		
		Instant startMoment = Instant.now();
		
		for (ModelElementInstance element:getModelElements1()) {
			
			ModelElementInstance equivalentActivityElement = (getEquivalentElement(element)!=null)?getEquivalentElement(element):element;	
			
			ModelElementInstance equivalentActivityElement2 = getEquivalentElement(equivalentActivityElement, getModelElements2());
			
			if(equivalentActivityElement2 != null) {
				
				Collection<String> dependenciesActivity = getDependenciesElementId(equivalentActivityElement, getGatewayElementsOld());
				Collection<String> dependenciesActivity2 = getDependenciesElementId(equivalentActivityElement2, getGatewayElementsUpdated());
				
				if(dependenciesActivity.size() == dependenciesActivity2.size()) {
					for(String id: dependenciesActivity) {
						if(!dependenciesActivity2.stream().anyMatch(id2 -> id2.equalsIgnoreCase(id))) {						
							changedElements.add(equivalentActivityElement);
							break;
						}
					}
				}else {
					changedElements.add(equivalentActivityElement);
				}
			}
		}
		
		setExecutionTime(calculateExecutionTime(startMoment));
	}
	
	private boolean isGatewayElement(String id, Collection<Gateway> gateways) {
		return gateways.stream().anyMatch(el -> {
			System.out.println("ID element: " + id+" Id Gateway: " + el.getId());
			
			boolean result = el.getId().equalsIgnoreCase(id);
			
			return result;
		});
	}
	
	private Collection<String> getDependenciesElementId( ModelElementInstance element, Collection<Gateway> gateways) {
		
		FlowNode node = (FlowNode)element;
		
		Collection<SequenceFlow> incomings = node.getIncoming();
		
		Collection<String> result = new ArrayList<String>();
		
		if(incomings.size() == 1) {
			
			SequenceFlow incomeFlow = incomings.stream().findFirst().get();
			
			FlowNode source  = incomeFlow.getSource();
			
			String sourceID = source.getId();
			
			if(isGatewayElement(sourceID, gateways)) {
				
				result.add(sourceID);
				
				return getDependenciesElementId((ModelElementInstance)source, gateways);
				
			}else {
				result.add(sourceID);
			}
			
		}else {
		
			for (SequenceFlow sf:incomings) {
				
				String sourceID = sf.getSource().getId();
				
				if(isGatewayElement(sourceID, gateways)) {
					result.add(sourceID);
					return getDependenciesElementId((ModelElementInstance)sf.getSource(), gateways);				
				}else {						
					result.add(sourceID);
				}
			}
		}
		
		return result;
	}

}
