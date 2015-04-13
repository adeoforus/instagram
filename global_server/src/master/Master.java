package master;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.Messages;
import config.Parameter;
import server.RequestSlave;
import task.Task;
import task.TaskScheduler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class which managers work load between slave machines
 */
public class Master {

    private TrackerSlave[] slaves;
    public TaskScheduler scheduler;

    /**
     * Constructor
     */
    public Master()
    {
        scheduler = new TaskScheduler();
        initSlaveMachines();
    }

    /**
     * Storing Information about Slave Machines
     */
    public void initSlaveMachines()
    {
        int slaveCount = Parameter.slave_count;
        String[] parameters = Parameter.slave_server();
        slaves = new TrackerSlave[slaveCount];
        for(int i=0;i<slaveCount;i++){
            TrackerSlave tracker = new TrackerSlave(
                i,
                (Integer.parseInt(parameters[1])+i),
                parameters[0],
                Messages.status_avaiable,
                Parameter.slave_dir_prefix + i + Parameter.slave_dir_in,
                Parameter.slave_output+Parameter.slave_dir_prefix + i + "\\",
                Parameter.slave_output_map+Parameter.slave_dir_prefix + i + "\\"
            );
            slaves[i] = tracker;
        }

        //TODO Verify connection with all Slaves, else set status == error
    }

    /**
     * Updating Slave Information
     * @param id
     * @param message
     */
    public void updateSlaves(int id, String message)
    {
        if(message.equals(Messages.response_OK)){
            slaves[id].status = Messages.status_avaiable;
        }else{
            slaves[id].status = Messages.status_error;
        }
    }

    /**
     * Updating Tasks in Scheduler
     * @param slaveDetails
     * @param resultPath
     */
    public void updateScheduler(String slaveDetails, String resultPath)
    {
        String[] array = slaveDetails.split(Messages.seperator_slave);
        int task_id = Integer.parseInt(array[0]);
        String responseMessage = array[1];

        updateSlaves(task_id, responseMessage);
        scheduler.updateTaskStatus(task_id, responseMessage);

        System.err.println("Response from Server Result: " + resultPath);
        System.err.println("");

    }

    /**
     * Assigning a task to a Slave Machine
     */
    public void assignTaskToSlave(Task task)
    {
        boolean status = scheduler.add(task);
        if(status) {
            ArrayList<Integer> index = getFreeSlave();
            System.out.println("Assigning Task to : " + index);
            if (!index.isEmpty()) {
                //Distributing work load
                HashMap<Integer, String> options = distributeWorkLoad(index, task);

                //Executing Orders
                int count = 0;
                Iterator it = options.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    it.remove();

                    slaves[((Integer) entry.getKey())].status = Messages.status_not_avaiable;
                    task.setDataDirectories(
                            count,
                            (String) entry.getValue(), //Input data location
                            slaves[((Integer) entry.getKey())].DATA_OUT_FOLDER,
                            slaves[((Integer) entry.getKey())].MAP_FOLDER
                    );
                    count++;

                    System.out.println(task.toString());

                    //Send Task Request to Slave
                    RequestSlave requestSlave = new RequestSlave(
                            task,
                            slaves[((Integer) entry.getKey())].port
                    );
                    requestSlave.init();
                }

            } else {
                System.err.println("No Free Slaves");
            }
        } else{
            System.err.println("Error : Could not add task to scheduler");
        }
    }


    /**
     * Creates Required Directory Structure
     * Copies Files from Database to Temporary Locations
     * Returns Distributed Work Load
     * 1 - id of slave (index on list slaves)
     * 2 - Path to Input Directory
     * @return
     */
    private HashMap<Integer, String> distributeWorkLoad(ArrayList<Integer> index, Task task )
    {
        HashMap<Integer, String> options = new HashMap<Integer, String>();
        int slaveCount = index.size(); //Number of Free Slaves
        ArrayList<HashMap<String,String>> databaseInformation = getDatabaseInformation(task); //Information about databases

        System.err.println(databaseInformation.toString());

        if(!databaseInformation.isEmpty()){
            //Global Cumulative Size of Databases
            int total_size = getGlobalDatabaseSize(databaseInformation);
            //Remainder
            int remainder = total_size%slaveCount;
            //Files Per Slave
            int sizePerSlave = (total_size-remainder)/slaveCount;

            System.err.println("total size " + total_size);
            System.err.println("number of slaves " + slaveCount);
            System.err.println("files per slave "+ sizePerSlave);
            System.err.println("remainder " + remainder);

            //Setting up input directory structure for slaves
            boolean status = true; //Keeping track of successful progress
            File root;
            //  Input Root
            root = new File(Parameter.database_slave_root);
            if(!root.isDirectory()){
                status = root.mkdir();
            }
            //  Output Root
            root = new File(Parameter.slave_output);
            if(!root.isDirectory()){
                status = root.mkdir();
            }

            //  Output Map Root
            root = new File(Parameter.slave_output_map);
            if(!root.isDirectory()){
                status = root.mkdir();
            }

            if(status)
            {
                //  Root\Sub_Roots
                for( int i=0; i<slaveCount;i++ )
                {
                    File sub_root;
                    //Input Data Directories
                    sub_root = new File(
                        Parameter.database_slave_root +
                            Parameter.slave_dir_prefix + i + "\\"
                    );

                    if(!sub_root.isDirectory()){
                        status = sub_root.mkdir();
                    } else
                    {
                        //Empty directory if already exists
                        File[] files = sub_root.listFiles();
                        if(files!=null) {
                            for(File f: files) {
                                if(!f.isDirectory()) {
                                    f.delete();
                                }
                            }
                        }
                    }

                    //Output Data Directories
                    sub_root = new File(
                        Parameter.slave_output
                            +Parameter.slave_dir_prefix + i + "\\"

                    );

                    if(!sub_root.isDirectory()){
                        status = sub_root.mkdir();
                    }

                    //Output Map Data Directories
                    sub_root = new File(
                        Parameter.slave_output_map
                                +Parameter.slave_dir_prefix + i + "\\"

                    );

                    if(!sub_root.isDirectory()){
                        status = sub_root.mkdir();
                    }

                    //Directory Information For Slaves
                    options.put(
                        i,
                        Parameter.database_slave_root +
                            Parameter.slave_dir_prefix + i + "\\"
                    );
                }
            }

            if(status)
            {
                // Copying files to temporary location
                // j : keeping track of files done
                // k : keeping track of files per Slave
                // c : keeping track of slaves done
                int j=0,k=0,c=0;
                for(HashMap<String,String> database : databaseInformation)
                {
                    File[] files = (new File(database.get("path"))).listFiles();

                    if(files!=null)
                    {
                        for(File file : files)
                        {
                            try {
                                Files.copy(file.toPath(),
                                        (new File(
                                                Parameter.database_slave_root +
                                                        Parameter.slave_dir_prefix + c + "\\" +
                                                        file.getName())).toPath(),
                                        StandardCopyOption.REPLACE_EXISTING);
//                                System.err.println(file.toString());
//                                System.err.println(j + " ---- j  ----   ");
//                                System.err.println(k + " ---- k  ----  ");
//                                System.err.println(c + " ---- c  ----  ");
                                j++;k++;
                                if(k==sizePerSlave-1 && c!= slaveCount-1){
                                    c++;
                                    k=0;
                                }
                            }
                            catch (IOException e)
                            {
                                status=false;
                            }
                        }
                    }
                }
            }
            if(!status)
            {
                System.err.println("WARNING : SOME ERROR FOUND SOMEWHERE");
            }
        }

        return options;
    }

    /**
     * Getting Database Information
     *  -   Count
     *  -   Path to each Database
     * @return
     */
    private ArrayList<HashMap<String,String>> getDatabaseInformation(Task task){
        ArrayList<HashMap<String,String>> options = new ArrayList<HashMap<String,String>>();
        String path = Parameter.database_root;
        File dir = new File(path);
        if(dir.isDirectory())
        {
            File[] databases = dir.listFiles();
            if(databases != null)
            {
                for(File database : databases)
                {
                    int size = getDatabaseSize(database.toString(), task.tag); //Verifying that database is relevant to Task
                    if(size > 0)
                    {
                        HashMap<String,String> info = new HashMap<String,String>();
                        info.put("path", database.toString());
                        info.put("size", Integer.toString(size));
                        options.add(info);
                    }
                }
            }
        }
        return options;
    }

    /**
     * Get size of a Database
     * Check if tag exists in Database
     * Else does not include database
     * @param path
     * @return
     */
    private int getDatabaseSize(String path, String tags){
        File[] contents = (new File(path)).listFiles();
        if(contents != null && contents.length > 0)
        {
            Gson gson = new GsonBuilder().create();
            iterateOverDatabase :
            for(File file : contents){
                try {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(file), "UTF-8")
                    );
                    String str;
                    while ((str = in.readLine()) != null) {
                        if(str.contains(tags))
                        {
                            break iterateOverDatabase;
                        }
                    }
                    in.close();
                }
                catch (FileNotFoundException e1){}
                catch (IOException e2){}
            }
            return contents.length;
        }
        return 0;
    }

    /**
     * Calculate cumulative size of databases
     * @param databases
     * @return
     */
    private int getGlobalDatabaseSize(ArrayList<HashMap<String,String>> databases){
        int size=0;

        if(databases!=null)
        {
            for(HashMap<String,String> database : databases)
            {
                size += Integer.parseInt(database.get("size"));
            }

        }

        return size;
    }


    /**
     * Get list of free slaves
     * @return
     */
    private ArrayList<Integer> getFreeSlave()
    {
        ArrayList<Integer> index = new ArrayList<Integer>();


        for(TrackerSlave slave : slaves){
            if(slave.status.equals(Messages.status_avaiable)){
                index.add(slave.id);
            }
//            System.out.println(slave.toString());
        }

        return index;
    }

    // ---
    // --- Test
    // ---

    /**
     * Test
     */
    private  void test()
    {
        Task request = new Task("markfrolich",0,0);
        assignTaskToSlave(request);

    }

    /**
     * Test
     * Main Method
     * @param args
     */
    public static void main(String[] args){
        Master master = new Master();

        master.test();
        System.out.println("finish");

    }
}
