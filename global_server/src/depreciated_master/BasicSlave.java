package depreciated_master;

import java.io.File;

public class BasicSlave {
	// ATTRIBUTES
	protected int id;
	protected String host;
	protected int port;
		
	protected File dbDir;					// directory with DB
	protected File rawDataDir;				// directory with raw data from Instagram API

	
	// CONSTRUCTOR	
	public BasicSlave(int id, String host, int port) {
		super();
		this.id = id;
		this.host = host;
		this.port = port;
	}


	// GETTERS, SETTERS
	public File getDbDir() {
		return dbDir;
	}


	public void setDbDir(File dbDir) {
		this.dbDir = dbDir;
	}


	public File getRawDataDir() {
		return rawDataDir;
	}


	public void setRawDataDir(File rawDataDir) {
		this.rawDataDir = rawDataDir;
	}


	public int getId() {
		return id;
	}


	public String getHost() {
		return host;
	}


	public int getPort() {
		return port;
	}

	// TO STRING
	@Override
	public String toString() {
		return String
				.format("BasicSlave [id=%s, host=%s, port=%s, \ndbDir=%s, \nrawDataDir=%s]",
						id, host, port, dbDir, rawDataDir);
	}

}
