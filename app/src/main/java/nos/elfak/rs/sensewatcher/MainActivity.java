package nos.elfak.rs.sensewatcher;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    Communication communication;
    public static ArrayList<AnomalyData> anomalies = new ArrayList<>();
    boolean listening = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        communication = Communication.getCommunication();
    }

    public void seeResults(View view)
    {
        Intent i = new Intent(this, ResultsActivity.class);
        startActivity(i);
    }

    public void startListening(View view)
    {
        listening = !listening;
        if(listening)
        {
            Intent i = new Intent(this, AnomaliesActivity.class);
            startActivity(i);
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    while (listening)
                    {
                        anomalies.add(communication.listenForAnomalies());
                    }
                }
            }).start();
        }else
        {
            communication.closeSocket();
            anomalies.clear();
            ((Button) view).setText(Constants.listenLabel);

        }
    }

    public void seeAnomalies(View view)
    {
        Intent i = new Intent(this, RequestAnomalies.class);
        startActivity(i);
    }
}
