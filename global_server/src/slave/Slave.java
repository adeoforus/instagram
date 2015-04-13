package slave;

import config.Messages;
import config.Parameter;
import depreciated_master.Task;
import java.io.BufferedReader;
import java.io.PrintStream;


public class Slave {

    protected int id;
    protected String host;
    protected int port;
    protected String status;
    protected Task currentTask;
    protected BufferedReader theIn;
    protected PrintStream theOut;

    public Slave(int id){
        String[] parameters = Parameter.slave_server();
        this.id = id;
        this.host = parameters[0];
        this.port = Integer.parseInt(parameters[1])+this.id;
        this.status = Messages.status_avaiable;
    }


    // --- Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }
}
