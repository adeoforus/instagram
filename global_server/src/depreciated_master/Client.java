/**
 * Arjun's client
 */
package depreciated_master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client {

    public Client(){

    }

    public void init(){
        try {
            Socket socket = new Socket("localhost", 9005);
            PrintStream theOut = new PrintStream(socket.getOutputStream());
            theOut.println("cat");

            InputStreamReader theIn = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(theIn);

            String message = in.readLine();

            System.out.println(message);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }



}