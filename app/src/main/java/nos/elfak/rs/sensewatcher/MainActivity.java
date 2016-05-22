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
    public static ArrayList<String> anomalies = new ArrayList<>();
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
        if(!listening)
        {
            ((Button) view).setText(Constants.stopLabel);
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    while (true)
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
        listening = !listening;
    }

    public void seeAnomalies(View view)
    {
    }
}
