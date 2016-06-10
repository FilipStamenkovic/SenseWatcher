package nos.elfak.rs.sensewatcher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class RequestAnomalies extends AppCompatActivity implements NestedScrollView.OnScrollChangeListener
{
    Communication communication;
    int pageSize = -1;
    ArrayList<AnomalyData> datas = new ArrayList<>();
    ArrayList<AnomalyData> receivedData;
    View picker;
    boolean hasMoreData = true;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_anomalies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(communication == null)
        {
            communication = Communication.getCommunication();
        }
        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.scroller);
        if(nestedScrollView != null)
            nestedScrollView.setOnScrollChangeListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null)
        {
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestAnomalies.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    builder.setMessage("Message").setTitle("Choose page size");

                    builder.setView(createView());
                    builder.setPositiveButton("Request data", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            addRowToTable(null);
                            pageSize = ((NumberPicker) picker.findViewById(R.id.picker)).getValue();
                            t = new Thread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    getData();
                                }
                            });
                            t.start();

                            dialog.dismiss();
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addRowToTable(AnomalyData anomaly)
    {
        TableLayout table = (TableLayout) findViewById(R.id.table);
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

    private View createView()
    {
        picker = getLayoutInflater().inflate(R.layout.dialog_view, null);
        NumberPicker num = (NumberPicker) picker.findViewById(R.id.picker);
        num.setMinValue(10);
        num.setMaxValue(1000);
        num.setValue(50);
        View v = picker.findViewById(R.id.checkboxes);
        v.setVisibility(View.GONE);
        return picker;
    }

    private synchronized void getData()
    {
        RequestSenseData request = new RequestSenseData(pageSize, datas.size(), null);
        request.setType("download_anomalies");
        receivedData = communication.getDataAnomalyData(request);
        if (receivedData == null || receivedData.size() == 0)
            hasMoreData = false;
        else
        {
            hasMoreData = true;
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    for(AnomalyData data : receivedData)
                        addRowToTable(data);
                }
            });

            datas.addAll(receivedData);
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
    {
        View view = v.getChildAt(v.getChildCount() - 1);
        int diff = (view.getBottom() - (v.getHeight() + v.getScrollY()));

        // if diff is zero, then the bottom has been reached
        if (diff < 10)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    getData();
                }
            }).start();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (t != null)
            t.interrupt();
    }
}
