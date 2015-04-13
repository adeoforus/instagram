package mapper_reducer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Client {
	
	public static final String[] sort = { "Likes", "Created time" };
	
	public static void main(String[] args) {
		 
		 JFrame frame = new JFrame( "DialogSetTag" );
		 
		 // prompt the user to enter tag
		 String tag = JOptionPane.showInputDialog(frame, "Which tag you want to search?");
		 
		 if (tag != null && !tag.isEmpty()){
			 String sorting = (String) JOptionPane.showInputDialog(frame, 
				        "Sort by",
				        "SORT",
				        JOptionPane.QUESTION_MESSAGE, 
				        null, 
				        sort, 
				        sort[0]);
			 if (sorting != null && !sorting.isEmpty()){
				 // TODO send information to the server
				 System.out.println("Tag : " + tag);
				 System.out.println("Sort by : " + sorting);
			 }else{
				 System.out.println("User don't want to search");
			 }
			 
		 }else{
			System.out.println("User don't want to search"); 
		 }
	}

}
