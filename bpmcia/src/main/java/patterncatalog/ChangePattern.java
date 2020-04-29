package patterncatalog;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public abstract class ChangePattern{
	
	Collection< ModelElementInstance> modelElements1;
	Collection< ModelElementInstance> modelElements2;
	HashMap<String, ModelElementInstance> equivalentMapElements;
	Long executionTime = 0L;
	
	public ChangePattern() {
		this.executionTime = 0L;
		this.modelElements1 = new ArrayList<ModelElementInstance>();
		this.modelElements2 = new ArrayList<ModelElementInstance>();
		this.equivalentMapElements = new HashMap<String, ModelElementInstance>();
	}
	
	public Long getExecutionTime() {
		return executionTime;
	}
	
	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}
	
	public Collection<ModelElementInstance> getModelElements1() {
		return modelElements1;
	}
	
	public void setModelElements1(Collection<ModelElementInstance> modelElements1) {
		this.modelElements1 = modelElements1;
	}
	
	public Collection<ModelElementInstance> getModelElements2() {
		return modelElements2;
	}
	
	public void setModelElements2(Collection<ModelElementInstance> modelElements2) {
		this.modelElements2 = modelElements2;
	}
	
	public HashMap<String, ModelElementInstance> getEquivalentMapElements() {
		return equivalentMapElements;
	}
	
	public void setEquivalentMapElements(HashMap<String, ModelElementInstance> equivalentMapElements) {
		this.equivalentMapElements = equivalentMapElements;
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
	
	
	public boolean elementExist(ModelElementInstance element, Collection<ModelElementInstance> elements) {
		boolean result = elements.stream().anyMatch(el -> el.getAttributeValue("id").equalsIgnoreCase(element.getAttributeValue("id")));
		return result;
	}
	
	public ModelElementInstance getEquivalentElement(ModelElementInstance element) {
		String elementId = element.getAttributeValue("id");
		
		return getEquivalentMapElements().get(elementId);
	}
	
	public ModelElementInstance getEquivalentElement(ModelElementInstance element, Collection<ModelElementInstance> elements) {
		if(elementExist(element, elements)) {
			Optional<ModelElementInstance> opElement = elements.stream().filter(el -> el.getAttributeValue("id").equalsIgnoreCase(element.getAttributeValue("id"))).findFirst();
			
			return opElement.get();
		}
		
		return null;
	}
}
