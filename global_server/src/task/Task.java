package task;

import config.Messages;

/**
 * Task Type by PHP-Client Server
 */
public class Task {

    public String tag;
    public int sort;
    public int cloud;
    public String DATA_OUT_FOLDER;
    public String DATA_IN_FOLDER;
    public String MAP_FOLDER;
    public int task_id;
    public String status;
    public String result;

    /**
     * Constructor
     * @param tag
     * @param sort
     * @param cloud
     */
    public Task(String tag, int sort, int cloud){
        this.tag = tag;
        this.sort = sort;
        this.cloud = cloud;
        this.status = Messages.task_pending;
        this.result = "";
    }

    /**
     * Settings data input, output, map directories
     * @param task_id
     * @param DATA_IN_FOLDER
     * @param DATA_OUT_FOLDER
     * @param MAP_FOLDER
     */
    public void setDataDirectories(int task_id, String DATA_IN_FOLDER, String DATA_OUT_FOLDER, String MAP_FOLDER){
        this.task_id = task_id;
        this.DATA_IN_FOLDER=DATA_IN_FOLDER;
        this.DATA_OUT_FOLDER=DATA_OUT_FOLDER;
        this.MAP_FOLDER=MAP_FOLDER;
    }

    /**
     * Settings result location
     * @param result
     */
    public void setResult(String result)
    {
        this.result = result;
    }

    /**
     * Verification that request seems normal
     * @return
     */
    public boolean analyseParameters(){
        boolean status = false;
        if(!tag.trim().equals("")){
            status=true;
        }
        return status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "tag='" + tag + '\'' +
                ", sort=" + sort +
                ", cloud=" + cloud +
                ", DATA_OUT_FOLDER='" + DATA_OUT_FOLDER + '\'' +
                ", DATA_IN_FOLDER='" + DATA_IN_FOLDER + '\'' +
                ", MAP_FOLDER='" + MAP_FOLDER + '\'' +
                ", task_id=" + task_id +
                ", status='" + status + '\'' +
                ", result='" + result + '\'' +
                '}';
    }

}
