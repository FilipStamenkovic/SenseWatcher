package nos.elfak.rs.sensewatcher;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by filip on 20.5.16..
 */
public class Communication
{
    private DatagramSocket socket;
    private Thread receivingThread;
    private AnomaliesActivity activity;
    private static Communication communication;
    private Communication()
    {

    }

    public void setActivity(AnomaliesActivity activity)
    {
        this.activity = activity;
    }

    public static Communication getCommunication()
    {
        if(communication == null)
            communication = new Communication();

        return communication;
    }

    public void closeSocket()
    {
        if(socket != null)
        {
            socket.disconnect();
            socket.close();
            socket = null;
        }
        if(receivingThread != null && receivingThread.isAlive())
        {
            receivingThread.interrupt();
            receivingThread = null;
        }
    }

    public AnomalyData listenForAnomalies()
    {
        String info = "";
        if (receivingThread == null)
            receivingThread = Thread.currentThread();
        byte[] receiveData = new byte[1024];
        AnomalyData data = null;
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try
        {
            if (socket == null)
            {
                socket = new DatagramSocket(Constants.port);
                socket.setBroadcast(true);
            }
            receivePacket.setPort(Constants.port);
            socket.receive(receivePacket);
            Constants.ip_address = receivePacket.getAddress().getHostAddress();
            info = (new String(receivePacket.getData())).trim();
            Gson gson = new Gson();
            data = gson.fromJson(info, AnomalyData.class);
            if(activity != null)
                activity.RefreshTable();
        } catch (SocketException e)
        {
            e.printStackTrace();
            closeSocket();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
            closeSocket();
        } catch (IOException e)
        {
            e.printStackTrace();
            closeSocket();
        }
        return data;
    }

    public ArrayList<ReceiveData> getData(RequestSenseData request)
    {
        String info = request.toString();
        DatagramPacket packet;
        DatagramSocket sock = null;
        byte [] sendData = info.getBytes();
        byte[] receiveData = new byte[102400];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        ArrayList<ReceiveData> datas = null;
        try
        {
            sock = new DatagramSocket();
            sock.setBroadcast(true);

            packet = new DatagramPacket(sendData,sendData.length);
            packet.setAddress(InetAddress.getByName(Constants.ip_address));
            packet.setPort(Constants.sendPort);
            sock.send(packet);
            sock.receive(receivePacket);
            Constants.ip_address = receivePacket.getAddress().getHostAddress();
            info = (new String(receivePacket.getData())).trim();
            Gson gson = new Gson();
            datas = gson.fromJson(info, new TypeToken<List<ReceiveData>>(){}.getType());
        } catch (SocketException e)
        {
            e.printStackTrace();
            //closeSocket();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
            //closeSocket();
        } catch (IOException e)
        {
            e.printStackTrace();
           // closeSocket();
        } finally
        {
            if(sock != null)
            {
                sock.disconnect();
                sock.close();
            }
        }
        return datas;
    }
}
