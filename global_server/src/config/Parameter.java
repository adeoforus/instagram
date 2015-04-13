package config;

/**
 * Required Parameters
 */
public class Parameter {

    public static String[] client_server(){
        String[] parameters = {"localhost","9005"};
        return parameters;
    }

    public static String[] master_server(){
        String[] parameters = {"localhost","9000"};
        return parameters;
    }

    public static String[] slave_server(){
        String[] parameters = {"localhost","5000"};
        return parameters;
    }

    //required paths
    public static final String database_root = "database\\";
    public static final String database_slave_root = "tmp_slave_data\\";
    public static final String slave_output = "tmp_output\\";
    public static final String slave_output_map = "tmp_output_map\\";

    //slave directory paths
    public static final String slave_dir_prefix = "SLAVE_";
    public static final String slave_dir_in = "\\DATA_IN_FOLDER\\";
    public static final String slave_dir_out = "\\DATA_OUT_FOLDER\\";
    public static final String slave_dir_map_out = "\\MAP_FOLDER\\";

    public static final int slave_count = 1;

}
