package master;

/**
 * Class storing information about slave machines
 */
public class TrackerSlave {

    public int id;
    public int port;
    public String host;
    public String status;
    public String DATA_OUT_FOLDER;
    public String DATA_IN_FOLDER;
    public String MAP_FOLDER;


    public TrackerSlave(
            int id, int port, String host, String status,
            String DATA_IN_FOLDER, String DATA_OUT_FOLDER, String MAP_FOLDER
    ){
        this.id=id;
        this.port=port;
        this.host=host;
        this.status=status;
        this.DATA_IN_FOLDER = DATA_IN_FOLDER;
        this.DATA_OUT_FOLDER = DATA_OUT_FOLDER;
        this.MAP_FOLDER = MAP_FOLDER;
    }

    @Override
    public String toString() {
        return "TrackerSlave{" +
                "id=" + id +
                ", port=" + port +
                ", host='" + host + '\'' +
                ", status='" + status + '\'' +
                ", DATA_OUT_FOLDER='" + DATA_OUT_FOLDER + '\'' +
                ", DATA_IN_FOLDER='" + DATA_IN_FOLDER + '\'' +
                ", MAP_FOLDER='" + MAP_FOLDER + '\'' +
                '}';
    }
}
