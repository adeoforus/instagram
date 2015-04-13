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
 * Simulating PHP-Client Task
 */
public class Client {

    private String host;
    private int port;
    protected BufferedReader theIn;
    protected PrintStream theOut;
    private Task request;

    /**
     * Constructor
     * @param tags
     * @param sort
     * @param cloud
     */
    public Client(String tags, int sort, int cloud) {
        String[] parameters = Parameter.client_server();
        this.host = parameters[0];
        this.port = Integer.parseInt(parameters[1]);
        request = new Task(tags, sort, cloud);
    }

    /**
     * Setting up Socket
     */
    public void init(){
        try{

            Socket socket = new Socket(host, port);
            theOut = new PrintStream(socket.getOutputStream());

            //Send Command
            sendCommand();

            theIn = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            //read Response
            readResponse();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Send Command
     */
    public void sendCommand(){

        Gson gson = new Gson();
        String command = gson.toJson(request);
        theOut.println(command);
        System.out.println(command);

    }

    /**
     * Read Response
     */
    public void readResponse(){
        try{

            String message = theIn.readLine();
            System.out.println(message);

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Testing
     * Main Method
     * @param args
     */
    public static void main(String[] args){
        Client request = new Client("barcelona",0,0);
        request.init();

    }
}

