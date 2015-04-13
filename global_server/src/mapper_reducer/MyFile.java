package mapper_reducer;

public class MyFile {
	public String fileName; 
	public BasicSlave slave; 
	
	MyFile (String fileName, BasicSlave slave){
		this.fileName = fileName;
		this.slave = slave;
	}
	
	// TO STRING
	public String toString(){
		return "MyFile [fileName: " + fileName + ", \nslave where file is stored: \n" + slave + "]";
	}
	
}
