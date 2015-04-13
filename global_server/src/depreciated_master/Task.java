package depreciated_master;

import java.util.ArrayList;


public class Task {
	// ATTRIBUTES	
	public int id;
	public int slaveID;
	public String status = "open";
	public String type;
	public ArrayList <MyFile> files;		// files in DBs
	public String tag;
	
	
	// CONSTRUCTORS
	public Task(String type, ArrayList <MyFile> files) {
		this.type = type;
		this.files = files;
	}


	// For MapSelect
	public Task(String type, String tag, ArrayList <MyFile> files) {
		this(type, files);
		this.tag = tag;
	}


	// TO STRING
	@Override
	public String toString() {
		return String.format(
				"Task [id=%s, slaveID=%s, status=%s, type=%s, tag=%s, \nfiles=%s]", 
					   id,    slaveID,    status,    type,    tag, 		files);
	}
	
	
	
	
}
