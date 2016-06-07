package nos.elfak.rs.sensewatcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by filip on 22.5.16..
 */
public class AnomalyData
{
    private String description;
    private String lastReading;
    private transient ReceiveData receiveData;
    private transient SubscribeData subscribeData;

    public AnomalyData(){}

    public AnomalyData(String description, String lastReading)
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

    public String getLastReading()
    {
        return lastReading;
    }

    public void setLastReading(String lastReading)
    {
        this.lastReading = lastReading;
    }

    public ReceiveData getReceiveData()
    {
        return receiveData;
    }

    public void setReceiveData(ReceiveData receiveData)
    {
        this.receiveData = receiveData;
    }

    public SubscribeData getSubscribeData()
    {
        return subscribeData;
    }

    public void setSubscribeData(SubscribeData subscribeData)
    {
        this.subscribeData = subscribeData;
    }

    public void setTransientObjects()
    {
        receiveData = null;
        subscribeData = null;
        Gson gson = new GsonBuilder().create();

        try
        {
            receiveData = gson.fromJson(lastReading, ReceiveData.class);
        }
        catch (Exception ex)
        {
            receiveData = null;
            subscribeData = gson.fromJson(lastReading, SubscribeData.class);
        }
    }

    @Override
    public String toString()
    {
        Gson gson = new GsonBuilder().create();

        return gson.toJson(this);
    }
}
