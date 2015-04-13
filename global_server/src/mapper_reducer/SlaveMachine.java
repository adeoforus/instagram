package mapper_reducer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import com.google.gson.Gson;

public class SlaveMachine extends Slave{
	// ATTRIBUTES
	private ServerSocket servak;
	
	
	// CONSTRUCTOR
	public SlaveMachine(int id, String host, int port) {
		super(id, host, port);
	}

	//================================== LAUNCH SLAVE MACHINE ==================================//
	public static void main(String[] args) throws Exception {
		/*
		int id = Integer.parseInt(args[0]);
		String host = args[1];
		int port = Integer.parseInt(args[2]);
		File dbDir = new File(args[3]);
		*/
		
		//------------------------------------------------- test values ----------------------------------------------------//
		int id = 0;																											//
		String host = "localhost";																							//
		int port = 5000;																									//
		File rawDataDir = new File ("H:\\Repositories\\GitHub\\Projects\\InstagramSearchEngine\\data\\rawData\\DB_0");		// 
		File dbDir =  new File ("H:\\Repositories\\GitHub\\Projects\\InstagramSearchEngine\\data\\DB\\DB_0");				// 
		//------------------------------------------------------------------------------------------------------------------//
		
		SlaveMachine slave = new SlaveMachine(id, host, port);
		slave.setRawDataDir(rawDataDir);
		slave.setDbDir(dbDir);
		
		slave.start();
	}
	


	//======================================= OTHER METHODS ===============================================//			
	
	public void start () throws Exception{
				
		String DATA_OUT_FOLDER = "C:\\Users\\Administrator\\Dropbox\\adeo_prj\\adeo-java-instagram\\SLAVE_1\\DATA_OUT_FOLDER\\";
		String DATA_IN_FOLDER = "C:\\Users\\Administrator\\Dropbox\\adeo_prj\\adeo-java-instagram\\SLAVE_1\\DATA_IN_FOLDER\\";
		String MAP_FOLDER = "C:\\Users\\Administrator\\Dropbox\\adeo_prj\\adeo-java-instagram\\SLAVE_1\\MAP_FOLDER\\";
		
			System.out.println( "Listening to Master's command \n" );
			try{
				servak = new ServerSocket(port);

				while(true){
					// LISTEN TO ANY ONE
					listen();

					// DO WORK - launch subprocess
					// Step 1 - Store the data in DATA_IN_FOLDER
					ArrayList<MyFile> files = currentTask.files;

					for (MyFile file : files){
						String nameOfFile = file.fileName;
						File source_copy = new File(DATA_OUT_FOLDER + nameOfFile);
						File source_past = new File(DATA_IN_FOLDER + nameOfFile);
						copyFile(source_copy, source_past);
					}

					// Step 2 - Do the task

					//0 MapCount
					//1 MapSelect
					//2 MapSortLike
					//3 MapSortTime

					switch (0) {
					case 0:
						int id_task_count = currentTask.id;
						Mapper MapCount = new Mapper(id_task_count, DATA_IN_FOLDER, MAP_FOLDER);
						MapCount.mapCount();
						File source_copy_count = new File(MAP_FOLDER + id_task_count);
						File source_past_count = new File(DATA_OUT_FOLDER + id_task_count);
						copyFile(source_copy_count, source_past_count);
						break;
					case 1:
						int id_task_select = currentTask.id;
						Mapper MapSelect = new Mapper(id_task_select, DATA_IN_FOLDER, MAP_FOLDER);
						MapSelect.mapSelect(currentTask.tag);
						File source_copy_select = new File(MAP_FOLDER + id_task_select);
						File source_past_select = new File(DATA_OUT_FOLDER + id_task_select);
						copyFile(source_copy_select, source_past_select);
						break;
					case 2:
						int id_task_sort_like = currentTask.id;
						Mapper MapSortLike = new Mapper(id_task_sort_like, DATA_IN_FOLDER, MAP_FOLDER);
						MapSortLike.sortByLike();
						File source_copy_sort_like = new File(MAP_FOLDER + id_task_sort_like);
						File source_past_sort_like = new File(DATA_OUT_FOLDER + id_task_sort_like);
						copyFile(source_copy_sort_like, source_past_sort_like);
						break;
					case 3:
						int id_task_sort_time = currentTask.id;
						Mapper MapSortTime = new Mapper(id_task_sort_time, DATA_IN_FOLDER, MAP_FOLDER);
						MapSortTime.sortByLike();
						File source_copy_sort_time = new File(MAP_FOLDER + id_task_sort_time);
						File source_past_sort_time = new File(DATA_OUT_FOLDER + id_task_sort_time);
						copyFile(source_copy_sort_time, source_past_sort_time);
						break;
					default:
						System.out.println("This command doesn't exist, please check the Task type");
						break;
					}

					// REPORT TO MASTER
					// Step 3 - Report

				}
			}
			catch (IOException e){
				e.printStackTrace();
			}
				
		}
	
	public void listen(){
		
		try {
			Socket connection = servak.accept();
		
        
	        // input/output reader
	        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        PrintStream output = new PrintStream(connection.getOutputStream());
	        
	        // get input
	        String message = input.readLine();
	        System.out.println("Command : " + message);
	        
	        String response = "";
	        
	        // process input
	        if (message.equals("Filenames in DB")){
		        	try {
						// ACCEPT
		        		File[] list_of_Files = rawDataDir.listFiles();
		        			for (File file: list_of_Files){
								String filename = file.getName();
								output.println(filename);				// send filename
								//System.out.println(filename);
							}
		        		output.println("End");
		        		System.out.println("Accepted");
						System.out.println("Filenames provided");
						
					} catch (Exception e) {
						e.printStackTrace();
						output.println("Refused");						// REFUSE
						System.out.println("Refused");
					}
		        	
		    }else {
		        	if (status.equals("free")){
			        	
				        	try {
								processInput(message);
								response = "Accepted";
					        	status = "busy";	
							} catch (Exception e) {
								e.printStackTrace();
								response = "Error";
					        }
			        }else {
			        		response = status;
			        }
			       
			        // send response
			        output.println(response);
			}
	        
	        
	        
	        // CLOSE IO
	        connection.close();
	        output.close();
	        input.close();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void processInput(String taskJson){
		
		Gson gson = new Gson();
		currentTask = gson.fromJson(taskJson, Task.class);

		System.out.println(currentTask);
         
        
	}
	
	private void copyFile(File path_in, File path_out){
		try{
    		Files.copy(path_in.toPath(), path_out.toPath(), StandardCopyOption.REPLACE_EXISTING);
    	}
    	catch(IOException e){
    		e.printStackTrace();
    	}
	}
	
}