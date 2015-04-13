package depreciated_master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import com.google.gson.Gson;


public class Master {
	
	int lastID = 0;
	int newID = lastID + 1;
	
	
	// ATTRIBUTES
	// information about slave machines :
	public static Slave [] slaveBook;				// For Master
	public static BasicSlave [] basicSlaveBook;		// For MyFile Class
	
	public static TasksBoard TB = new TasksBoard();
	
	public static ArrayList<MyFile> rawDataFiles;
	public static ArrayList<MyFile> filesInDBs;
	
	// CONSTRUCTOR
	public Master(Slave [] slaveBookIN, BasicSlave [] basicSlaveBookIN) {
		slaveBook = slaveBookIN;
		basicSlaveBook = basicSlaveBookIN;
	}

	//============================= PRELIMINARY WORK ==================================//
	
	public void doPreliminaryWork(ArrayList<MyFile> rawDataFilesIN) throws Exception{	
		rawDataFiles = rawDataFilesIN;
		
		// arbitrary defined number of files for 1 Map Count task
		int maxNumFilesInTask = 10;		 // TODO make it dynamic
		
		
		
		//--------------------------- Create MapCounttasks  ----------------------------//
		
		LinkedList <Task> mapCountTasks = genMapCountTasks(maxNumFilesInTask);	
		
		
		
		//---------------- Distribute open tasks to slaves  ----------//
		while (TB.getTasksCount() != 0){
			
			// Check free slaves
			LinkedList <Slave> freeSlaves = getFreeSlaves();
			
			if (freeSlaves.size() != 0){
				
				if (mapCountTasks.size() >= freeSlaves.size()){
					
					sendTask(freeSlaves.size(), freeSlaves, mapCountTasks);
					
				}else if (mapCountTasks.size() < freeSlaves.size()){
					
					sendTask(mapCountTasks.size(), freeSlaves, mapCountTasks);
					
				}
			}
			
			
			//---------------- Listen to reporting Slaves  ----------//
			//TODO
		}
	}

	private static void sendTask(int receivers, LinkedList <Slave> freeSlaves, LinkedList <Task> mapTasks) throws Exception{
		for (int i = 0; i < receivers; i++){
			
			Slave s = freeSlaves.get(i);
			
			Gson gson = new Gson();
			s.currentTask = mapTasks.get(i);		// assign a task to Slave

			String taskJSON = gson.toJson(s.currentTask);
				
			
			// 3. Send task
				System.out.println("Send task");
			
			String confirmation = sendJson(taskJSON, s.getHost(), s.getPort());
				
			// 4. Change status of tasks and slaves
				System.out.println("Change status of tasks and slaves");
				
			if (confirmation.equals("Accepted")){
				
				changeSlaveStatus(s.id, "busy");
				freeSlaves.remove(i);
				
				changeTaskStatus(s.currentTask.id, "inProgress");
				mapTasks.remove(i);
			
			}else if (confirmation.equals("busy")){
				
				changeSlaveStatus(s.id, "busy");
				freeSlaves.remove(i);
			}
			
			
		}
	}

/*	
	//============================= SEARCH ==================================//
	
	public ArrayList <String> doSearch (String request) {
		ArrayList <String> results = new ArrayList <String> ();
		String [] allQueryTokens = request.split(" ");
		
		for (int i = 0; i < allQueryTokens.length; i++) {
			String tag = allQueryTokens[0];
			
			// arbitrary defined number of files for 1 Map Select task
			int maxNumFilesInTask = 10;		 // TODO make it dynamic
			
			//---------------- Create map tasks  ----------//
			genMapSelectTasks(tag, maxNumFilesInTask);
			
			//---------------- Create ReduceMergeTask  ----------//
			LinkedList <Task> mapTasks = TB.getOpenMapTasks();

			ReduceMergeTask rmt1 = new ReduceMergeTask(mapTasks);
			TB.addNewTask(rmt1);		// add task to TasksBoard
			
			//---------------- Distribute open tasks to slaves  ----------//
		while (TB.getTasksCount() != 0){
			// Check free slaves
			LinkedList <Slave> freeSlaves = getFreeSlaves();
			
			if (freeSlaves.size() != 0){
				
				if (mapCountTasks.size() == 0){
					//TODO
					System.out.println("===> No Map tasks");
					
				}else if (mapCountTasks.size() < freeSlaves.size()){
					
					sendTask(mapCountTasks.size(), freeSlaves, mapCountTasks);
					
					int difference = freeSlaves.size() - mapCountTasks.size();	// How many slaves can be assigned to do not Map Tasks
					
					
					sendTask(difference, freeSlaves, reduceTasks);
					
					
					
				}else if (mapCountTasks.size() >= freeSlaves.size()){
					
					sendTask(freeSlaves.size(), freeSlaves, mapCountTasks);
				}
			}
		}
			
			
			
			
		
		//TODO
		//---------- Return results ----------//
		return results;
	}
	
	
	
	
	
	
	
	*/
	
	//======================================= OTHER METHODS ===============================================//
	
		private static String  sendJson(String taskJson, String host, int port) throws Exception{
			
			try {
	            Socket socket = new Socket(host, port);
	            PrintStream theOut = new PrintStream(socket.getOutputStream());
	            theOut.println(taskJson);

	            InputStreamReader theIn = new InputStreamReader(socket.getInputStream());
	            BufferedReader in = new BufferedReader(theIn);

	            String confirmation = in.readLine();
	            
	            socket.close();
	            theIn.close();
	            theOut.close();
	            return confirmation;
	        }
	        catch (IOException e){
	            e.printStackTrace();
	            return "IOException"; 
	        }
		}
		
		//------------------------------- Methods for work with Slaves ---------------------------//
		private static LinkedList <Slave> getFreeSlaves(){
			LinkedList <Slave> freeSlaves = new LinkedList <Slave>();
			
			for (int i = 0; i < slaveBook.length; i++){
				if(slaveBook[i].status.equals("free")){
					freeSlaves.add(slaveBook[i]);
					//System.out.println(slaveBook[i]);
				}
			}
			return freeSlaves;
		}
		
		
		public static BasicSlave getBasicSlave(int slaveID){
			
			for (BasicSlave bs : basicSlaveBook)
				if(bs.id == slaveID)	{ return bs; }
			
			return null;
		}
		
		
		//------------------------------- Methods for generating tasks ---------------------------//
		
		private static void genMapSelectTasks(String tag, int maxNumFilesInTask){
			int numFiles = filesInDBs.size();
			int numTasks = (numFiles/maxNumFilesInTask) + 1;	
			int numFilesInLastTask = numFiles % maxNumFilesInTask;
			
			for (int i = 0; i < numTasks; i++){ 
				if ((i+1) != numTasks){
					genOneMapSelectTask(i, maxNumFilesInTask, tag); 
				}else{
					genOneMapSelectTask(i, numFilesInLastTask, tag); 
				}
			}
		}
		
		
		private static Task genOneMapSelectTask(int i, int numFilesInThisTask, String tag){
			ArrayList <MyFile> files = new ArrayList <MyFile> ();
			
			int start = i*numFilesInThisTask;
			for (int k = 0; k < numFilesInThisTask; k++){
				files.add(filesInDBs.get(start + k));
			}
			
			Task t = new Task("MapSelect", tag, files);
			TB.addNewTask(t);		// add task to TasksBoard
			return t;
		}

		
		private static LinkedList <Task> genMapCountTasks(int numFilesInTask){
			LinkedList <Task> result = new LinkedList <Task>();
			
			int numFiles = rawDataFiles.size();
			int numTasks = (numFiles/numFilesInTask) + 1;	
			int numFilesInLastTask = numFiles % numFilesInTask;
			
			for (int i = 0; i < numTasks; i++){ 
				if ((i+1) != numTasks){
					result.add(genOneMapCountTask(i, numFilesInTask)); 
				}else{
					result.add(genOneMapCountTask(i, numFilesInLastTask)); 
				}
			}
			
			return result;
		}

		
		private static Task genOneMapCountTask(int i, int numFilesInThisTask){
			ArrayList <MyFile> files = new ArrayList <MyFile> ();
			
			int start = i*numFilesInThisTask;
			for (int k = 0; k < numFilesInThisTask; k++){
				files.add(rawDataFiles.get(start + k));
			}
			
			Task t = new Task("MapCount", files);
			TB.addNewTask(t);		// add task to TasksBoard
			return t;
		}
		
		
		
		private static void changeSlaveStatus(int slaveID, String newStatus){
			for (Slave slave : slaveBook) {
				if(slave.id == slaveID){
					slave.status = newStatus;
					break;
				}
			}
		}
		
		
		private static void changeTaskStatus (int taskID, String newStatus){
			for (Task task: TB.getTasks()) {
				if(task.id == taskID){
					task.status = newStatus;
					break;
				}
			}
		}
		
}
