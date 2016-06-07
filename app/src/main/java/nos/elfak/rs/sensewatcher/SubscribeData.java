package nos.elfak.rs.sensewatcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by filip on 7.6.16..
 */
public class SubscribeData
{
    private String type;
    private long id;
    private ArrayList<String> sensors;

    public SubscribeData(){}

    public SubscribeData(String type, long id, ArrayList<String> sensors)
    {
        this.type = type;
        this.id = id;
        this.sensors = sensors;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public ArrayList<String> getSensors()
    {
        return sensors;
    }

    public void setSensors(ArrayList<String> sensors)
    {
        this.sensors = sensors;
    }

    public String convertSensorsToString()
    {
        String s = "";
        for(String str:sensors)
            s += str + "\n";
        return s;
    }

    @Override
    public String toString()
    {
        Gson gson = new GsonBuilder().create();

        return gson.toJson(this);
    }
}
