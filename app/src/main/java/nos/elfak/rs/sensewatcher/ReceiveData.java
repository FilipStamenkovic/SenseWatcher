package nos.elfak.rs.sensewatcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by filip on 20.5.16..
 */
public class ReceiveData
{
    protected double x;
    protected double y;
    protected double z;
    protected String sensor;
    protected long id;
    protected String type = "download";
    protected String timestamp;

    public ReceiveData(){}

    public ReceiveData(int x, int y, int z, String sensor)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.sensor = sensor;
    }

    public ReceiveData(String sensor)
    {
        this(0, 0, 0, sensor);
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getZ()
    {
        return z;
    }

    public void setZ(double z)
    {
        this.z = z;
    }

    public String getSensor()
    {
        return sensor;
    }

    public void setSensor(String sensor)
    {
        this.sensor = sensor;
    }

    public void setAll(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public String toString()
    {
        Gson gson = new GsonBuilder().create();

        return gson.toJson(this);
    }
}
