package nos.elfak.rs.sensewatcher;

import android.provider.SyncStateContract;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by filip on 20.5.16..
 */
public class Communication
{
    private DatagramSocket socket;
    private MainActivity activity;
    // private int receivePort;
    private Thread receivingThread;
    // DatagramPacket receivePacket;
    private static Communication communication;
    private boolean receiving = false;
    private boolean poslaoSubscribe = false;
    private Communication(MainActivity activity)
    {
        this.activity = activity;
    }

    public static Communication getCommunication(MainActivity activity)
    {
        if(communication == null)
            communication = new Communication(activity);

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
        receiving = false;
        poslaoSubscribe = false;
        if(receivingThread != null && receivingThread.isAlive())
        {
            receivingThread.interrupt();
            receivingThread = null;
        }

      /*  if(receivingSocket != null)
        {
            receivingSocket.disconnect();
            receivingSocket.close();
            receivingSocket = null;
        }*/

    }


    public void sendData(ArrayList<SensorData> datas, boolean subscribing, String sensor)
    {
        try
        {
            String info;
            DatagramPacket packet;
            byte [] sendData;
            if (socket == null)
            {
                socket = new DatagramSocket();
                socket.setBroadcast(true);
            }if(!subscribing)
        {
            for (int i = 0; i < datas.size(); i++)
            {
                if(sensor != null && !datas.get(i).getSensor().contentEquals(sensor))
                    continue;
                info = datas.get(i).toString();// + "\n";
                sendData = info.getBytes();

                packet = new DatagramPacket(sendData, sendData.length);

                packet.setAddress(InetAddress.getByName(Constants.ip_address));
                packet.setPort(Integer.parseInt(Constants.port));
                socket.send(packet);
            }
        }else
        {
            if(poslaoSubscribe)
                return;
            info = "subscribe\n";
            for(int i = 0; i < datas.size(); i++)
                info += datas.get(i).getSensor() + "\n";


            sendData = info.getBytes();

            packet = new DatagramPacket(sendData,sendData.length);
            packet.setAddress(InetAddress.getByName(Constants.ip_address));
            packet.setPort(Integer.parseInt(Constants.port));
            socket.send(packet);
            poslaoSubscribe = true;
        }
            // closeSocket();
        } catch (IOException e)
        {
            e.printStackTrace();
            closeSocket();
        } catch (Exception e)
        {
            e.printStackTrace();
            closeSocket();
        }
    }

    public ArrayList<ReceiveData> getData(Request request)
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
            packet.setAddress(InetAddress.getByName(SyncStateContract.Constants.ip_address));
            packet.setPort(Integer.parseInt(Constants.port));
            socket.send(packet);
            socket.receive(receivePacket);
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
