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
    private SubscribeData lastSubscribe;

    public AnomalyData(){}

    public AnomalyData(String description)
    {
        this.description = description;
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

    public SubscribeData getLastSubscribe()
    {
        return lastSubscribe;
    }

    public void setLastSubscribe(SubscribeData lastSubscribe)
    {
        this.lastSubscribe = lastSubscribe;
    }

    @Override
    public String toString()
    {
        Gson gson = new GsonBuilder().create();

        return gson.toJson(this);
    }
}
