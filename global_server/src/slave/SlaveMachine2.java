package slave;

import config.Messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SlaveMachine2 extends Slave{

    private String DATA_OUT_FOLDER;
    private String DATA_IN_FOLDER;
    private String MAP_FOLDER;

    public SlaveMachine2(int id){
        super(id);
        init();
        initServer();

    }

    /**
     * Setting up environment variables
     */
    public void init(){
        DATA_OUT_FOLDER = "SLAVE_2\\DATA_OUT_FOLDER\\";
        DATA_IN_FOLDER = "SLAVE_2\\DATA_IN_FOLDER\\";
        MAP_FOLDER = "SLAVE_2\\MAP_FOLDER\\";
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

                // send response
                theOut = new PrintStream(connection.getOutputStream());
                sendResponse();

                //Close Connections
                connection.close();
                theIn.close();
                theOut.close();

                // Test : Send OK Response
                try {

                    Thread.sleep(1000); //1000 milliseconds is one second.
                    ResponseToMaster response = new ResponseToMaster(id, Messages.response_OK);
                    response.init();

                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
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
            System.out.println("Command: " + command);

            //Do Master's Orders

        }catch (IOException e){
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


    public static void main(String[] args){
        SlaveMachine2 slave = new SlaveMachine2(1);
        slave.init();
    }
}
