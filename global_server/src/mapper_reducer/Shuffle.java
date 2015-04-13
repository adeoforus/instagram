package mapper_reducer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


public class Shuffle {
	
	// Array with paths to all files
	private File[] inFiles;
	private int numberOfDB;
	private ArrayList<ArrayList<String>> media_for_dbs;
	private String pathOut;
		
	public Shuffle(File[] inFiles, int numberOfDB, String pathOut) {
		this.inFiles = inFiles;
		this.numberOfDB = numberOfDB;
		this.pathOut = pathOut;
	}
	
	// Return the number of DB where to store or receive given tag
	private int getTheNumberOfDB(String tag, int number_of_DB){
		// Setting for hash function
		int hash = 7;
		for (int i = 0; i < tag.length(); i++) {
			hash = hash*31 + tag.charAt(i); // charAt can return negative value
		}
		int go_to_DB = hash % number_of_DB;
		go_to_DB = (go_to_DB < 0 ? -go_to_DB : go_to_DB); // Convert negative to positive
		return go_to_DB;
	}
	
	// Prepare list to store information
	private void setNumberOflists(){
		media_for_dbs = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < numberOfDB; i++) {
			ArrayList<String> data_for_db = new ArrayList<String>();
			media_for_dbs.add(data_for_db);
		}
	}
	// Write results in new file
	private void writeInFile(){
		for (int i = 0; i < numberOfDB; i++) {
			if (media_for_dbs.get(i).size() > 0){ // If we have data for this DB do
				try {
					String file_to_write = pathOut + "Data_for_DB" + i; // Path to the new file
					File fileDir_write = new File(file_to_write);
					File paretnDir = new File(pathOut); // Path to a folder
					if (!paretnDir.exists()){
						paretnDir.mkdirs(); // create parent dir and ancestors if necessary
					}
					// Write data in the file
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir_write), "UTF-8"));
					for (int j = 0; j < media_for_dbs.get(i).size(); j++) { // Go throw list
						out.write(media_for_dbs.get(i).get(j)); // Get JSON from list and write in a file
						out.newLine();
					}
					out.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}	
	
	
	public void doShuffle(){
		// Prepare main list to write information
		setNumberOflists();
		// Prepare Gson
		Gson gson = new GsonBuilder().create();
		// Read all files
		for(File f : inFiles){
			// Prepare reader for each file
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
				String aLine;
				while((aLine = in.readLine()) != null) {
					JsonObject jobj = gson.fromJson(aLine, JsonObject.class);
					String tag_from_media = jobj.get("tags").toString();
					media_for_dbs.get(getTheNumberOfDB(tag_from_media, numberOfDB)).add(aLine);
				} // end while
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // end for
		
		// Write output
		writeInFile();
	}
}
