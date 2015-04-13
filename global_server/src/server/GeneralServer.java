package server;

import com.google.gson.Gson;
import config.Parameter;
import task.Task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class responbile for receiving messages from PHP-Client
 * And transmits data to Master GeneralServer
 */
public class GeneralServer {

    private int port;
    private BufferedReader theIn;
    private PrintStream theOut;

    /**
     * Constructor
     */
    public GeneralServer(){
        String[] parameters = Parameter.client_server();
        port = Integer.parseInt(parameters[1]);
    }

    /**
     * Setting up GeneralServer Socket
     */
    public void init(){

        System.out.println("Client General Server: ");
        try{
            ServerSocket socket = new ServerSocket(port);

            while(true)
            {
                // open socket
                Socket connection = socket.accept();
                System.out.println( "Client Connected");

                // read command
                InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
                theIn = new BufferedReader(inputStream);
                readCommand();

                // send response
                theOut = new PrintStream(connection.getOutputStream());
                sendResponse();

                //Close Connections
                connection.close();
                theIn.close();
                theOut.close();

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
        try {

            String command = theIn.readLine();
            System.out.println("Command: " + command);

            Gson gson = new Gson();
            Task task = gson.fromJson(command, Task.class);
            boolean status = task.analyseParameters();

            if(status){
                //Call Master

                //Create a task for master
                RequestMaster masterRequest = new RequestMaster(task);
                masterRequest.init();
            }
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

        String responseString = "Successfully Recieved Command";
        theOut.println(responseString);

    }

    /**
     * Main Method
     * @param args
     */
    public static void main(String[] args){

        GeneralServer server = new GeneralServer();
        server.init();

    }

}

