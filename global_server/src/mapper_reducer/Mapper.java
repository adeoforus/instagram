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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.gson.*;

public class Mapper {
	
	// Variables for read and write file
	private String path_file_in;
	private String path_file_out;
	private int id_of_operation;
	
	// Variable for parsing JSON
	private List<String> medias = new ArrayList<String>();
	private String user_id;
	private String link;
	private String created_time;
	private String media_id;
	private String img_url;
	private int likes_count;
	
	// Constructor for Mapper_COUNT
	public Mapper(int id_of_operation, String path_file_in, String path_file_out) {
		this.id_of_operation = id_of_operation;
		this.path_file_in = path_file_in;
		this.path_file_out = path_file_out;
	}
	
	// Write results in new file
	private void writeInFile(){
		try {
			String file_name = String.valueOf(id_of_operation);
			String file_to_write = path_file_out + file_name;
			File fileDir_write = new File(file_to_write);
			File paretnDir = new File(path_file_out);
			if (!paretnDir.exists()){
				paretnDir.mkdirs(); // create parent dir and ancestors if necessary
			}
			// Write data in the file
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir_write), "UTF-8"));
			for (String s : medias){
				out.write(s);
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	public void sortByLike() {
		try{
			List<MediaMap> listMedia = new ArrayList<MediaMap>();
			// Open folder with all files
			File folder_in = new File(path_file_in);
			Gson gson = new GsonBuilder().create();
			// For each file in folder do
			for (File file_in : folder_in.listFiles()){
				BufferedReader in =  new BufferedReader(new InputStreamReader(new FileInputStream(file_in), "UTF-8"));
				String str;
				while ((str = in.readLine()) != null)
				{
					MediaMap media = gson.fromJson(str, MediaMap.class); 	// Transform JSON to the object
					listMedia.add(media); 
				} 
				in.close();
				file_in.delete();
			}
			Collections.sort(listMedia, new MediaMapComparatorLike());
			Iterator<MediaMap> it = listMedia.iterator();
			String media_json;
			while (it.hasNext()) {
				MediaMap m = it.next();
				media_json = gson.toJson(m);
				medias.add(media_json);
			}
			writeInFile();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void sortByCreatedTime() {
		try{
			List<MediaMap> listMedia = new ArrayList<MediaMap>();
			// Open folder with all files
			File folder_in = new File(path_file_in);
			Gson gson = new GsonBuilder().create();
			// For each file in folder do
			for (File file_in : folder_in.listFiles()){
				BufferedReader in =  new BufferedReader(new InputStreamReader(new FileInputStream(file_in), "UTF-8"));
				String str;
				while ((str = in.readLine()) != null) {
					MediaMap media = gson.fromJson(str, MediaMap.class); 	// Transform JSON to the object
					listMedia.add(media); 
				} 
				in.close();
				file_in.delete();
			}
			Collections.sort(listMedia, new MediaMapComparatorCreatedTime());
			
			Iterator<MediaMap> it = listMedia.iterator();
			String media_json;
			while (it.hasNext()) {
				MediaMap m = it.next();
				media_json = gson.toJson(m);
				medias.add(media_json);
			}
			writeInFile();
		} 
		catch (IOException e) {
            System.out.println(e.getMessage());
        }
		
	}
	
	public void mapCount(){
		try{
			// Open folder with all files
			File folder_in = new File(path_file_in);
			// For each file in folder do
			for (File file_in : folder_in.listFiles()){
				BufferedReader in =  new BufferedReader(new InputStreamReader(new FileInputStream(file_in), "UTF-8"));
				// Read string by string
				String str;
				String media_json;
				Gson gson = new GsonBuilder().create();
				while ((str = in.readLine()) != null) {
					MediaInstagram media = gson.fromJson(str, MediaInstagram.class); 	// Transform JSON to the object
					List<String> tags = media.getTags(); 							 	// Get list of the TAGS from this object
					user_id = media.getUser_id();										// USER_ID
					link = media.getLink();												// LINK
					created_time = media.getCreated_time();								// CREATED_TIME
					media_id = media.getMedia_id();										// MEDIA_ID
					img_url = media.getImg_url();										// IMG_URL
					likes_count = media.getLikes_count();								// LIKES_COUNT
					// Creating new objects, each object contain only one tag
					for (int i = 0; i < tags.size(); i++) {
						MediaMap media_count = new MediaMap(); 		// Create new object
						media_count.setUser_id(user_id);
						media_count.setTags(tags.get(i));
						media_count.setLink(link);
						media_count.setCreated_time(created_time);
						media_count.setMedia_id(media_id);
						media_count.setImg_url(img_url);
						media_count.setLikes_count(likes_count);
						media_json = gson.toJson(media_count);		// Transform object to the JSON
						medias.add(media_json);						// Store this JSON string in global list
					} // end for
				} // end while
				in.close();
				file_in.delete();
			} // end for
			// Write new file with results
			writeInFile();
		}
		catch (IOException e) {
            System.out.println(e.getMessage());
        }
	}
	
	public void mapSelect(String tag_for_search){
		try{
			// Open folder with all files
			File folder_in = new File(path_file_in);
			// For each file in folder do
			for (File file_in : folder_in.listFiles()){
				BufferedReader in =  new BufferedReader(new InputStreamReader(new FileInputStream(file_in), "UTF-8"));
				// Read string by string
				String str;
				String media_json;
				Gson gson = new GsonBuilder().create();
				while ((str = in.readLine()) != null) {
					MediaMap media = gson.fromJson(str, MediaMap.class); 	// Transform JSON to the object
					String tag = media.getTags();
					if( tag_for_search.equals(tag)){
						media_json = gson.toJson(media);		// Transform object to the JSON
						medias.add(media_json);					// Store this JSON string in global list
					} // end for
				} // end while
				in.close();
				file_in.delete();
			} // end for
			// Write new file with results
			writeInFile();
		}
		catch (IOException e) {
            System.out.println(e.getMessage());
        }
	}
}

