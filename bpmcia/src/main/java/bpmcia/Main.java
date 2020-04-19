package bpmcia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

@ManagedBean
@SessionScoped
public class Main {
	private String appname;
	private Collection< ModelElementInstance> taskElements;
	@PostConstruct
	public void init() {
		try {
			
			Main app = new Main();
			
			 String fileName = "shipModel.bpmn";
			 File file = app.getFileFromResources(fileName);
			 
		     BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
			// find element instance by ID
		     StartEvent start = (StartEvent) modelInstance.getModelElementById("startEvent");
	
			// find all elements of the type task
			ModelElementType taskType = modelInstance.getModel().getType(Task.class);
			Collection<ModelElementInstance> taskInstances = modelInstance.getModelElementsByType(taskType);
			setTaskElements(taskInstances);
			
			System.out.print(start.getTextContent());
		}catch (Exception e) {
			System.out.print(e.getMessage());
		}
		
		setAppname("BPMN Based CIA Framework");
		
	}
	
	public String getAppname() {
		return appname;
	}
	
	public void setAppname(String appname) {
		this.appname = appname;
	}
	
	public Collection<ModelElementInstance> getTaskElements() {
		return taskElements;
	}
	
	public void setTaskElements(Collection<ModelElementInstance> taskElements) {
		this.taskElements = taskElements;
	}
	
	// get file from classpath, resources folder
    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }
    
    private static void printFile(File file) throws IOException {

        if (file == null) return;

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
	
	
}