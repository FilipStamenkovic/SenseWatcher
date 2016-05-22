package nos.elfak.rs.sensewatcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by filip on 22.5.16..
 */
public class AnomalyData
{
    private String description;
    private ReceiveData lastReading;

    public AnomalyData(){}

    public AnomalyData(String description, ReceiveData lastReading)
    {
        this.description = description;
        this.lastReading = lastReading;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public ReceiveData getLastReading()
    {
        return lastReading;
    }

    public void setLastReading(ReceiveData lastReading)
    {
        this.lastReading = lastReading;
    }

    @Override
    public String toString()
    {
        Gson gson = new GsonBuilder().create();

        return gson.toJson(this);
    }
}
