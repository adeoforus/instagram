package server;

import com.google.gson.Gson;
import config.Parameter;
import task.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Sending a Task to a Slave Machine
 */
public class RequestSlave {

    private String host;
    private int port;
    private Socket socket;
    private BufferedReader theIn;
    private PrintStream theOut;
    private Task task;

    /**
     * Constructor
     */
    public RequestSlave(Task task, int port) {
        String[] parameters = Parameter.slave_server();
        this.host = parameters[0];
        this.port = port;
        this.task = task;
    }

    /**
     * Setting up Socket
     */
    public void init(){
        try{

            socket = new Socket(host, port);
            theOut = new PrintStream(socket.getOutputStream());

            //Send Command
            sendCommand();

            //Closing connections
            socket.close();
            theOut.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Send Command
     */
    public void sendCommand()
    {
        Gson gson = new Gson();
        String command = gson.toJson(task);
        theOut.println(command);
        System.out.println(command);

    }

}

