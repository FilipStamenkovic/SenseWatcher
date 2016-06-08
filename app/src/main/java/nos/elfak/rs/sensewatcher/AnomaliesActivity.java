package nos.elfak.rs.sensewatcher;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class AnomaliesActivity extends AppCompatActivity
{
    private Communication communication;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anomalies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        communication = Communication.getCommunication();
        addRowToTable(null);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        communication.setActivity(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        communication.setActivity(null);
    }

    public void RefreshTable()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                addRowToTable(null);
                for(AnomalyData data:MainActivity.anomalies)
                {
                    addRowToTable(data);
                }
            }
        });
    }

    private void addRowToTable(AnomalyData anomaly)
    {
        TableLayout table = (TableLayout) findViewById(R.id.anomaly_table);
        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.info_row, null);
        if(anomaly != null)
        {
            ReceiveData data = anomaly.getLastReading();
            TextView view = (TextView) row.findViewById(R.id.sens_id);
            if(data != null)
            {
                view.setText(Long.toString(data.getId()));
                view = (TextView) row.findViewById(R.id.sens_name);
                view.setText(data.getSensor());
                view = (TextView) row.findViewById(R.id.sens_x);
                view.setText(String.format("%.2f", data.getX()));
                view = (TextView) row.findViewById(R.id.sens_y);
                view.setText(String.format("%.2f", data.getY()));
                view = (TextView) row.findViewById(R.id.sens_z);
                view.setText(String.format("%.2f", data.getZ()));
                view = (TextView) row.findViewById(R.id.sens_ts);
                view.setVisibility(View.GONE);
                //view.setText(data.getTimestamp());
            } else
            {
                SubscribeData subscribeData = anomaly.getLastSubscribe();

                view = (TextView) row.findViewById(R.id.sens_id);
                view.setText(Long.toString(subscribeData.getId()));
                view = (TextView) row.findViewById(R.id.sens_name);
                view.setText(subscribeData.convertSensorsToString());
                view = (TextView) row.findViewById(R.id.sens_x);
                view.setVisibility(View.GONE);
                //view.setText(subscribeData.getType());
                view = (TextView) row.findViewById(R.id.sens_y);
                view.setVisibility(View.GONE);
                //view.setText(String.format("%.2f", data.getY()));
                view = (TextView) row.findViewById(R.id.sens_z);
                view.setVisibility(View.GONE);
                view = (TextView) row.findViewById(R.id.sens_ts);
                view.setVisibility(View.GONE);
                //view.setText(data.getTimestamp());
            }
            view = (TextView) row.findViewById(R.id.sens_desc);
            view.setText(anomaly.getDescription());
            view.setVisibility(View.VISIBLE);
        } else
        {
            table.removeAllViews();
        }
        table.addView(row);
    }
}
