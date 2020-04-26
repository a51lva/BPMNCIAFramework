package patterncatalog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public class InsertedActivitiesPattern extends ChangePattern{
	
	private Collection< ModelElementInstance> insertedElements;
	
	public InsertedActivitiesPattern() {
		this.insertedElements = null;
	}
	
	public InsertedActivitiesPattern(Collection< ModelElementInstance> activityElement1, Collection< ModelElementInstance> activityElements2) {
		this.insertedElements = new ArrayList<ModelElementInstance>();
		this.modelElements1 = activityElement1;
		this.modelElements2 = activityElements2;
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
		this.executionTime = 0L;
	}
	
	public Collection<ModelElementInstance> getInsertedElements() {
		return insertedElements;
	}
	
	public void setInsertedElements(Collection<ModelElementInstance> insertedElements) {
		this.insertedElements = insertedElements;
	}
	
	public void execute() {
		setInsertedElements(new ArrayList<ModelElementInstance>());
		Instant startMoment = Instant.now();
		
		for (ModelElementInstance element:getModelElements2()) {
			
			ModelElementInstance equivalentElement = (getEquivalentElement(element)!=null)?getEquivalentElement(element):element;

			if(!elementExist(equivalentElement, getModelElements1())) {
				insertedElements.add(equivalentElement);
			}
		}
		
		setExecutionTime(calculateExecutionTime(startMoment));
	}
}
