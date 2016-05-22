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
    private static Communication communication;
    private Communication()
    {

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

    public String listenForAnomalies()
    {
        String info = "";
        if (receivingThread != null)
            receivingThread = Thread.currentThread();
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try
        {
            if (socket == null)
            {
                socket = new DatagramSocket();
                socket.setBroadcast(true);
            }

            socket.receive(receivePacket);
            Constants.ip_address = receivePacket.getAddress().getHostAddress();
            info = (new String(receivePacket.getData())).trim();
            Gson gson = new Gson();
           // datas = gson.fromJson(info, new TypeToken<List<ReceiveData>>(){}.getType());
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
        return info;
    }


    public ArrayList<ReceiveData> getData(RequestSenseData request)
    {
        String info = request.toString();
        DatagramPacket packet;
        byte [] sendData = info.getBytes();
        byte[] receiveData = new byte[102400];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        ArrayList<ReceiveData> datas = null;
        try
        {
            if (socket == null)
            {
                socket = new DatagramSocket();
                socket.setBroadcast(true);
            }

            packet = new DatagramPacket(sendData,sendData.length);
            packet.setAddress(InetAddress.getByName(Constants.ip_address));
            packet.setPort(Integer.parseInt(Constants.port));
            socket.send(packet);
            socket.receive(receivePacket);
            Constants.ip_address = receivePacket.getAddress().getHostAddress();
            info = (new String(receivePacket.getData())).trim();
            Gson gson = new Gson();
            datas = gson.fromJson(info, new TypeToken<List<ReceiveData>>(){}.getType());
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
        return datas;
    }
}
