package com.example.poovam.attendancetracker1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Poovam on 12-Apr-16.
 */
public class forHR extends Activity {

    String message="not connected-not connected-not connected-not connected-not connected";

    String []parts;
    Socket client;
    String response;
    TextView Attendance_percentage;
    TextView Permission_left;
    TextView Leave_left;
    TextView Days_on_leave;
    EditText Employee_id;
    int flag;
    Button check;
    String emp_ID;
    BarChart chart;
    int permission;
    int leave;
    int percent;

   public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forhumanresource);


        Employee_id=(EditText)findViewById(R.id.Employee_id);
        Attendance_percentage=(TextView)findViewById(R.id.Attendance_percentage);
        Permission_left=(TextView)findViewById(R.id.Permission_left);
        Leave_left=(TextView)findViewById(R.id.Leave_left);
        Days_on_leave=(TextView)findViewById(R.id.Days_on_leave);
        check= (Button)findViewById(R.id.check);
        chart=(BarChart)findViewById(R.id.chart);
        chart.setTouchEnabled(true);


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emp_ID = Employee_id.getText().toString();
                SendMessage sendMessageTask = new SendMessage();
                sendMessageTask.execute();

            }
        });

    }

    public void split()
    {
        parts=message.split("-");

    }

    public void setAttendancePercentage()
    {
        Integer NUMERATOR=Integer.parseInt(parts[0]);
        Integer DENOMINATOR=Integer.parseInt(parts[1]);
        if(DENOMINATOR!=0) {
           percent = (NUMERATOR * 100) / DENOMINATOR;
            Attendance_percentage.setText("Attendance Percentage:" + percent + "%");
        }
        else
        {
            Attendance_percentage.setText("Server Error");
        }
    }
    public void setLeaveleft()
    {
        Leave_left.setText("Leave Left: "+parts[2]);
        leave=Integer.parseInt(parts[2]);
    }
    public void setPermission_left()
    {
        Permission_left.setText("Permissions Left: "+parts[3]);
        permission=Integer.parseInt(parts[3]);
    }
    public void setDays_on_leave()
    {
        Days_on_leave.setText("Days on Leave: "+parts[4]);
    }
    public void setChart()
    {

        permission=(permission*100)/6;
        leave=(leave*100)/6;


        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(percent, 0));
        entries.add(new BarEntry(permission,1));
        entries.add(new BarEntry(leave,2));

        BarDataSet dataset = new BarDataSet(entries, "Performance");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Percentage");
        labels.add("Permissions left");
        labels.add("Leaves left");

        BarData data = new BarData(labels, dataset);
        chart.setData(data);
    }


    private class SendMessage extends AsyncTask<Void, Void, Void> {//connection establishes in this layout only when query is given


        @Override
        protected Void doInBackground(Void... params) {

            try {
                DataOutputStream dataOutputStream = null;
                DataInputStream dataInputStream = null;
                client = new Socket("192.168.0.105", 9997);
                dataOutputStream = new DataOutputStream(client.getOutputStream());
                dataInputStream = new DataInputStream(client.getInputStream());

                if (emp_ID != null) {
                    dataOutputStream.writeUTF(4 + "-" + emp_ID);
                    Log.i("ID",emp_ID);
                    response = dataInputStream.readUTF();
                    Log.i("aloha",response);


                }


            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (client != null) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            return null;
        }


        protected void onPostExecute(Void result)
        {
            if(client!=null) {
                message = response;
                split();//splits message from server
                setAttendancePercentage();//sets information to respective text field
                setPermission_left();
                setLeaveleft();
                setDays_on_leave();
                chart.invalidate();
                setChart();
            }
            else
            {
                Toast.makeText(getBaseContext(),"Connect to network",Toast.LENGTH_SHORT).show();
            }

        }

    }


}

