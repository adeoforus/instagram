package depreciated_master;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

public class SlaveMachine extends Slave{

	//================================== LAUNCH SLAVE MACHINE ==================================//
	public static void main(String[] args) throws Exception {
		/*
		int id = Integer.parseInt(args[0]);
		String host = args[1];
		int port = Integer.parseInt(args[2]);
		File dbDir = new File(args[3]);
		*/

		//------------------------------------------------- test values ----------------------------------------------//
		int id = 0;
		String host = "localhost";
		int port = 5000;
		File rawDataDir = new File ("test_data\\DB_0");
		File dbDir =  new File ("test_data\\DB_0");
//		File rawDataDir = new File ("H:\\Repositories\\GitHub\\Projects\\InstagramSearchEngine\\data\\rawData\\DB_0");
//		File dbDir =  new File ("H:\\Repositories\\GitHub\\Projects\\InstagramSearchEngine\\data\\DB\\DB_0");
		//------------------------------------------------------------------------------------------------------------//

		SlaveMachine slave = new SlaveMachine(id, host, port);
		slave.setRawDataDir(rawDataDir);
		slave.setDbDir(dbDir);

		slave.start();

	}

	// ATTRIBUTES
	private ServerSocket servak;

	// CONSTRUCTOR
	public SlaveMachine(int id, String host, int port) {
		super(id, host, port);
	}

	//======================================= OTHER METHODS ===============================================//			

	/**
	 * GeneralServer Setup
	 * @throws Exception
	 */
	public void start () throws Exception{
				
		System.out.println( "Listening to Master's command \n" );
		try{
			servak = new ServerSocket(port);

			while(true){
				// LISTEN TO ANY ONE
				listen();

				// DO WORK - launch subprocess

					/**
					 * Here need to execute Mappers, Reducers, Shufflers and so on
					 * //TODO
					 */


				// REPORT TO MASTER
					//TODO
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
				
	}


	/**
	 * Listening to GeneralServer
	 */
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
//	        if (message.equals("Filenames in DB")){
//		        	try {
//						// ACCEPT
//		        		File[] list_of_Files = rawDataDir.listFiles();
//		        			for (File file: list_of_Files){
//								String filename = file.getName();
//								output.println(filename);				// send filename
//								//System.out.println(filename);
//							}
//		        		output.println("End");
//		        		System.out.println("Accepted");
//						System.out.println("Filenames provided");
//
//					} catch (Exception e) {
//						e.printStackTrace();
//						output.println("Refused");						// REFUSE
//						System.out.println("Refused");
//					}
//
//		    }else {
//		        	if (status.equals("free")){
//
//				        	try {
//								processInput(message);
//								response = "Accepted";
//					        	status = "busy";
//							} catch (Exception e) {
//								e.printStackTrace();
//								response = "Error";
//					        }
//			        }else {
//			        		response = status;
//			        }
//
//			        // send response
//			        output.println(response);
//			}
	        
	        
	        
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
	
	
	
}
