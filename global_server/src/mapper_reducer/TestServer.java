package mapper_reducer;


public class TestServer {

	public static void main(String args[]) {
		
		// Variables for read and write files
		String path_file_in = "C:\\Users\\Administrator\\Dropbox\\adeo_prj\\DB_0\\";
		String path_file_out = "C:\\Users\\Administrator\\Dropbox\\adeo_prj\\";
		
		// Test for MAP_COUNT
		Mapper test_map_count = new Mapper(1, path_file_in, path_file_out + "Map_Count\\");
		test_map_count.mapCount();
		test_map_count = null;
		System.out.println("Test MAP_COUNT done");
		
		// Test for MAP_SELECT
		Mapper test_map_select = new Mapper(2, path_file_out + "Map_Count\\", path_file_out + "Map_Select\\");
		test_map_select.mapSelect("beautiful");
		test_map_select = null;
		System.out.println("Test MAP_SELECT done");
		
		// Test for MAP_SORT_LIKE
		Mapper test_map_sort_by_like = new Mapper(3, path_file_out + "Map_Count\\", path_file_out + "Map_Sort_Like\\") ;
		test_map_sort_by_like.sortByLike();
		test_map_sort_by_like = null;
		System.out.println("Test sort by like done");
		
		// Test for MAP_SORT_CREATEDTIME
		Mapper test_map_sort_createdTime = new Mapper(4, path_file_out + "Map_Count\\", path_file_out + "Map_Sort_CT\\") ;
		test_map_sort_createdTime.sortByCreatedTime();
		test_map_sort_createdTime = null;
		System.out.println("Test sort by created time done");
		
		// Test for REDUCE_MERGE
		Reducer test_reduce_merge = new Reducer(5, path_file_in, path_file_out + "Reduce_Merge\\");
		test_reduce_merge.reduceMerge();
		test_reduce_merge = null;
		System.out.println("Test REDUCE_MERGE done");
		
//		// Test for SHUFFLE
//		File[] files = new File[2];
//		File file1 = new File("C:\\Users\\Administrator\\Dropbox\\adeo_prj\\Data_1");
//		File file2 = new File("C:\\Users\\Administrator\\Dropbox\\adeo_prj\\Data_2");
//		files[0] = file1;
//		files[1] = file2;
//		String file_path_out_shuffle = "C:\\Users\\Administrator\\Dropbox\\adeo_prj\\";
//		Shuffle sh1 = new Shuffle(files, 3, file_path_out_shuffle);
//		sh1.doShuffle();
//		System.out.println("Test SHUFFLE done");
		
		
//		// Test DATAGRAM_SENDER
//		try {
//			DatagramReciver recieve_data = new DatagramReciver(5000, "C:\\Users\\Administrator\\Dropbox\\adeo_prj\\Data_Recieved");
//			DatagramSender send_data = new DatagramSender(file1, InetAddress.getLocalHost(), 5000);
//			recieve_data.receiveData();
//			send_data.sendFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("Test DATAGRAM_SENDER done");
		
	}
}