package slave;

import config.Messages;
import config.Parameter;
import sun.misc.resources.Messages_es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Respond to Master Server
 */
public class ResponseToMaster {

    private String host;
    private int port;
    private Socket socket;
    private BufferedReader theIn;
    private PrintStream theOut;
    private int id;
    private String message;

    /**
     * Constructor
     */
    public ResponseToMaster(int id, String message)
    {
        String[] parameters = Parameter.master_server();
        this.host = parameters[0];
        this.port = Integer.parseInt(parameters[1]);
        this.id = id;
        this.message = message;
    }

    /**
     * Setting up Socket
     */
    public void init()
    {
        try
        {
            Socket socket = new Socket(host, port);
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
        String response = id + Messages.seperator_slave + message + "\n";
        theOut.println(Messages.type_response + Messages.seperator + response);
        System.out.println(Messages.type_response + Messages.seperator + response);
    }


}