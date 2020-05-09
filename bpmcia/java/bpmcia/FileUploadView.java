package bpmcia;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;

import com.google.common.io.Files;

@ManagedBean
@SessionScoped
public class FileUploadView {
     
    private UploadedFile file;
    private UploadedFiles files;
    
    @PostConstruct
	public void init() {}
 
    public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }
 
    public UploadedFiles getFiles() {
        return files;
    }
 
    public void setFiles(UploadedFiles files) {
        this.files = files;
    }
 
    public void upload() {
        if (file != null) {
            FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
     
    public void uploadMultiple() {
        if (files != null) {
            for (UploadedFile f : files.getFiles()) {
                FacesMessage message = new FacesMessage("Successful", f.getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }
     
    public void handleFileUpload(FileUploadEvent event)  throws IOException{
    	String filename = event.getFile().getFileName();
    	InputStream file = event.getFile().getInputStream();
    	
    	byte[] buffer = new byte[file.available()];
        file.read(buffer);
        
        String path = System.getProperty("user.home") + "\\Documents\\GitHub\\BPMNCIAFramework\\bpmcia\\src\\main\\resources\\uploads\\";
              
        File targetFile = new File(path+filename);
        Files.write(buffer, targetFile);
        
    	System.out.println(event.getFile().getFileName());
        FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
}
