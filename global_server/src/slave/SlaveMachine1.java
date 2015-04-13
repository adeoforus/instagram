package slave;

import com.google.gson.Gson;
import config.Messages;
import mapper_reducer.Mapper;
import task.Task;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SlaveMachine1 extends Slave{

    private Task task;


    public SlaveMachine1(int id){
        super(id);
        init();
    }

    /**
     * Setting up environment variables
     */
    public void init()
    {
    }

    /**
     * Setting up Server Socket
     */
    public void initServer(){
        System.out.println("Slave Server "+ id +" : ");
        try{
            ServerSocket socket = new ServerSocket(port);

            while(true)
            {
                // open socket
                Socket connection = socket.accept();
                System.out.println( "Master Command Received");

                // read command
                InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
                theIn = new BufferedReader(inputStream);
                readCommand();

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * Reading Command
     */
    public void readCommand(){
        try
        {
            String command = theIn.readLine();
            System.out.println("Slave " + id + " Command: " + command);

            //Do Master's Orders
            Gson gson = new Gson();
            this.task = gson.fromJson(command, Task.class);
            System.out.println("Slave " + id + " Format" + task.toString());

            executeCommand();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Send Response
     */
    public void sendResponse()
    {
        String responseString = "Successfully Recieved Command at slave_"+id ;
        theOut.println(responseString);
    }


    /**
     * Executing Command
     */
    private void executeCommand()
    {
        //TODO check what is the task

        //Mapper_Count
        Mapper MapCount = new Mapper(0, task.DATA_IN_FOLDER, task.MAP_FOLDER);
        MapCount.mapCount();
        File source_copy_count = new File(task.MAP_FOLDER + "\\" + 0);
        File source_past_count = new File(task.DATA_OUT_FOLDER + "\\" + 0);
        copyFile(source_copy_count, source_past_count, true);


        //Mapper Select
        source_copy_count = new File(task.MAP_FOLDER + "\\" + 1);
        source_past_count = new File(task.DATA_OUT_FOLDER + "\\" + 1);
        int id_task_select = 1;
        Mapper MapSelect = new Mapper(id_task_select, task.DATA_OUT_FOLDER, task.MAP_FOLDER);
        MapSelect.mapSelect(task.tag);
        copyFile(source_copy_count, source_past_count, true);


        //Mapper_Sort
        int id_task_sort_like = 10;
        Mapper MapSortLike = new Mapper(id_task_sort_like, task.DATA_OUT_FOLDER, task.MAP_FOLDER);
        MapSortLike.sortByLike();

        ResponseToMaster response = new ResponseToMaster(id, Messages.response_OK + "/" + task.MAP_FOLDER + id_task_sort_like);
        response.init();

    }

    /**
     * Copy File
     * @param path_in
     * @param path_out
     * @param delete
     */
    private void copyFile(File path_in, File path_out, boolean delete)
    {
        try{
            if (delete){
                Files.copy(path_in.toPath(), path_out.toPath(), StandardCopyOption.REPLACE_EXISTING);
                path_in.delete();
            }else{
                Files.copy(path_in.toPath(), path_out.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Test
     */
    private void test(){
        Task task = new Task("barcelona", 0, 0);
        task.setDataDirectories(0, "tmp_slave_data\\SLAVE_0\\", "tmp_output\\SLAVE_0\\","tmp_output_map\\SLAVE_0\\");

        this.task = task;
        System.out.println(this.task.toString());

        executeCommand();

    }

    public static void main(String[] args) {
        //Initialising the Slave Server
        int slave_id = 0;
        SlaveMachine1 slave = new SlaveMachine1(slave_id);
        slave.initServer();
//        slave.test();

    }
}
