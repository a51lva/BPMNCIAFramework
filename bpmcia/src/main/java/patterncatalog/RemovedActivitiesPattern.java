package patterncatalog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class RemovedActivitiesPattern extends ChangePattern{
	
	private Collection< ModelElementInstance> removedElements;
	
	public RemovedActivitiesPattern() {
		this.removedElements = new ArrayList<ModelElementInstance>();
	}
	
	public RemovedActivitiesPattern(Collection< ModelElementInstance> activityElement1, Collection< ModelElementInstance> activityElements2) {
		this.removedElements = new ArrayList<ModelElementInstance>();
		this.modelElements1 = activityElement1;
		this.modelElements2 = activityElements2;
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		this.executionTime = 0L;
	}
	
	public Collection<ModelElementInstance> getRemovedElements() {
		return removedElements;
	}
	
	public void setRemovedElements(Collection<ModelElementInstance> removedElements) {
		this.removedElements = removedElements;
	}
	
	public void execute() {
		setRemovedElements(new ArrayList<ModelElementInstance>());
		Instant startMoment = Instant.now();
		
		for (ModelElementInstance element:getModelElements1()) {
			
			ModelElementInstance equivalentElement = (getEquivalentElement(element)!=null)?getEquivalentElement(element):element;

			if(!elementExist(equivalentElement, getModelElements2())) {
				removedElements.add(equivalentElement);
			}
		}
		
		setExecutionTime(calculateExecutionTime(startMoment));
	}
}
