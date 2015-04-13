package server;

import com.google.gson.Gson;
import config.Messages;
import config.Parameter;
import task.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Send a request to Master Server
 */
public class RequestMaster {

    private String host;
    private int port;
    private Socket socket;
    private BufferedReader theIn;
    private PrintStream theOut;
    private Task request;

    /**
     * Constructor
     */
    public RequestMaster(Task request) {
        String[] parameters = Parameter.master_server();
        this.host = parameters[0];
        this.port = Integer.parseInt(parameters[1]);
        this.request = request;
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
        String command = gson.toJson(request);
        theOut.println(Messages.type_request + Messages.seperator + command);
        System.out.println(Messages.type_request + Messages.seperator + command);
    }


}

