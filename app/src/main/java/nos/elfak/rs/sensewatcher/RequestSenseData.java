package nos.elfak.rs.sensewatcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * Created by filip on 22.5.16..
 */
public class RequestSenseData
{
    private String type = "download";
    private int pageSize, offset;
    private ArrayList<String> sensorTypes;

    public RequestSenseData(int pageSize, int offset, ArrayList<String> sensorTypes)
    {
        this.pageSize = pageSize;
        this.offset = offset;
        this.sensorTypes = sensorTypes;
    }

    @Override
    public String toString()
    {
        Gson gson = new GsonBuilder().create();

        return gson.toJson(this);
    }
}
