package mapper_reducer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Reducer {
	// Array with paths to all files
	private String path_file_in;
	private String path_file_out;
	private int id_of_operation;
	
	public Reducer(int id_of_operation, String path_file_in, String path_file_out) {
		this.id_of_operation = id_of_operation;
		this.path_file_in = path_file_in;
		this.path_file_out = path_file_out;
	}
	
	public void reduceMerge(){
		
		// Path to write file
		String file_out = path_file_out + id_of_operation;
		File outFile = new File(file_out);
		File pathoutFile = outFile.getParentFile();
		if (!pathoutFile.exists()){
			pathoutFile.mkdirs(); // create parent directory and ancestors if necessary
		}
		
		// Prepare to write the outFile
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Open folder with all files
		File folder_in = new File(path_file_in);
		// Read all files
		for (File file_in : folder_in.listFiles()) {
			// Prepare reader for each file
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(new FileInputStream(file_in), "UTF-8"));
				// Write lines in a mergeFile
				String aLine;
				while((aLine = in.readLine()) != null) {
					out.write(aLine);
					out.newLine();
				}
				// Close reader for this file
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			// Close writer
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
