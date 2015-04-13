package depreciated_master;


public class Slave extends BasicSlave{
	// ATTRIBUTES
	protected String status = "free";
	protected Task currentTask;
	
	// CONSTRUCTOR
	public Slave(int id, String host, int port) {
		super(id, host, port);
	}
	
	// GETTERS, SETTERS
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

	@Override
	public String toString() {
		return String
				.format("BasicSlave [id=%s, host=%s, port=%s, status=%s, \ncurrentTask=%s, \ndbDir=%s, \nrawDataDir=%s]",
						id, host, port, status,
						currentTask, dbDir, rawDataDir);
	}

	
	// TO STRING

	
	

		
	
	
}
