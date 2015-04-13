package depreciated_master;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class Server {
	
	// raw data from Instagram API :
	public static ArrayList<MyFile> rawDataFiles = new ArrayList<MyFile>();
	
	// running slave machines on separate JVMs :
	public static SlaveMachine [] cluster;		
	
	// information about slave machines :
	public static Slave [] slaveBook;				// For Master
	public static BasicSlave [] basicSlaveBook;		// For MyFile Class


	// Queue of tag to search (for example from different users)
	public static LinkedList <String> requests = new LinkedList <String> ();
	
	public static void main(String[] args) throws Exception {		// put for test "1" in command line arguments
		
		// number of slave machines :
//		int slaves_count = Integer.parseInt(args[0]);
		int slaves_count = 1;

		//======================================= 1. Launch Slaves in parallel processes ==================================//
		System.out.println("==== 1. Launch Slaves in parallel processes ====");
		// TODO 
		
		//--------- Fill Slave Book AND Basic Slave Book ---------//
		slaveBook = new Slave [slaves_count];
		basicSlaveBook = new BasicSlave [slaves_count];
		
		for (int i = 0; i < slaves_count; i++) {
			
				slaveBook[i] = new Slave (i, "localhost", 5000 + i);
				basicSlaveBook[i] = new BasicSlave (i, "localhost", 5000 + i);
		}
		
		//--------- Set directories containing data on different slaves ---------//
		/**
		 * We arbitrary define on which slave machines data is stored
		 */

		File rawDataDir = new File ("test_data\\DB_0");
		File dbDir =  new File ("test_data\\DB_0");
//		File rawDataDir = new File ("H:\\Repositories\\GitHub\\Projects\\InstagramSearchEngine\\data\\rawData");		// directory with all 3 folders of RAW data
//		File dbDir = new File ("H:\\Repositories\\GitHub\\Projects\\InstagramSearchEngine\\data\\DB");					// directory with all 3 DBs (used for search by tag)

			File[] list_rawDataDir = rawDataDir.listFiles();				// 3 folders of RAW data
			File[] list_dbDir = dbDir.listFiles();							// 3 DBs (used for search by tag)
		
			System.out.println( "Raw Data Directory" + Arrays.toString(list_rawDataDir));
//			System.out.println();
			System.out.println("List db Directory" + Arrays.toString(list_dbDir));


		for (int i = 0; i < slaves_count; i++) {
			
				slaveBook[i].setRawDataDir(list_rawDataDir[i]);
				slaveBook[i].setDbDir(list_dbDir[i]);
				
				basicSlaveBook[i].setRawDataDir(list_rawDataDir[i]);
				basicSlaveBook[i].setDbDir(list_dbDir[i]);
		
				System.out.println();
				System.out.println(basicSlaveBook[i]);
		}



		//----------------------------------------- Fill Raw Data Files ---------------------------------------------------//
		for (BasicSlave slave : basicSlaveBook)
						fillRawDataFiles(slave);
		
		//======================================= TEST: display filenames in every DB =====================================//
/*
		for (DB d : dataBases){
			try 				{ for (String s : d.getFileNames()) 		System.out.println(s); }
			catch (Exception e) { e.printStackTrace(); }
			System.out.println("==============");
		}
*/
		//============================================ 2. Launch preliminary work ========================================//
		System.out.println("\n==== 2. Launch preliminary work ====");
		
		Master lamaster = new Master(slaveBook, basicSlaveBook);
		
		if (!rawDataFiles.isEmpty())
			lamaster.doPreliminaryWork(rawDataFiles);	
		else
			//checkDB();
			System.out.println("No raw data for preliminary work. \nFinish.");
		
		
		//=================================== 3. Launch WEB-server listening to the users from WEV =======================//
		//GeneralServer server = new GeneralServer();
        //server.init();
		
		/**
		 * After GeneralServer gets a request (tag) from a user, it creates and executes new TasksBoard object for the same Master.
		 * 
		 */

	}
	
	
	//================= OTHER METHODS =================//
	public static void fillRawDataFiles(BasicSlave slave) {
			String command = "Filenames in DB";
			
			try {
	            Socket socket = new Socket(slave.host, slave.port);
	            
	            // set IO
	            PrintStream outMessage = new PrintStream(socket.getOutputStream());
	            BufferedReader inMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            
	            // send request to slave
	            outMessage.println(command);
	            
	            // get response from slave
	            String message = inMessage.readLine();
	            System.out.println(message);
	            
	            // Option 1 : Slave accepts to provide file names
	            if (!message.equals("Refused")){
		            	
	            		rawDataFiles.add(new MyFile(message, slave));
		            	
		                while(true){
		                	message = inMessage.readLine();
		                	if (!message.equals("End"))
		                		rawDataFiles.add(new MyFile(message, slave));
			                else
		                		break;
		                }
	            }
	            
	            // Option 2 : Slave refuses to provide file names
	            else if (message.equals("Refused")){
	            	System.out.println("Slave refused to give filenames");
	            }
	            
	            // Option 3 : Other unpredicted shit 
	            else {
	            	System.out.println("Something is going wrong with connection");
	            }
	            
	            // Close IO :
	            socket.close();
	            inMessage.close();
	            outMessage.close();
	            
	        }
	        catch (IOException e){
	            e.printStackTrace();
	        }
			
	}

}
