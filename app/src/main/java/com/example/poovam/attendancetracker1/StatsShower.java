package com.example.poovam.attendancetracker1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.w3c.dom.Text;

import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Poovam on 12-Apr-16.
 */
public class StatsShower extends Activity {

    String message="not connected-not connected-not connected-not connected-not connected";

    String []parts;
    TextView EmpID;
    TextView Attendance_percentage;
    TextView Permission_left;
    TextView Leave_left;
    TextView Days_on_leave;
    BarChart chart;
    int percent;
    int leave;
    int permission;



   public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_shower);
        Intent i=getIntent();
        message=i.getExtras().getString("key");
        Log.i("messageFromClient", message);

        EmpID=(TextView)findViewById(R.id.Emp_id);
        Attendance_percentage=(TextView)findViewById(R.id.Attendance_percentage);
        Permission_left=(TextView)findViewById(R.id.Permission_left);
        Leave_left=(TextView)findViewById(R.id.Leave_left);
        Days_on_leave=(TextView)findViewById(R.id.Days_on_leave);
        chart=(BarChart)findViewById(R.id.chart);
        chart.setTouchEnabled(true);


        split(); //splits the server message
        setEmpID();//sets informations to respective text field
        setAttendancePercentage();
        setPermission_left();
        setLeaveleft();
        setChart();
        setDays_on_leave();
    }
    public void split()
    {
        int i=0;
        parts=message.split("-");

    }
    public void setEmpID()
    {
        EmpID.setText("Employee ID: "+parts[0]);
    }
    public void setAttendancePercentage()
    {
        Integer NUMERATOR=Integer.parseInt(parts[1]);
        Integer DENOMINATOR=Integer.parseInt(parts[2]);
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
        Leave_left.setText("Leave Left: "+parts[3]);
        leave=Integer.parseInt(parts[3]);
    }
    public void setPermission_left()
    {
        Permission_left.setText("Permissions Left: "+parts[4]);
        permission=Integer.parseInt(parts[4]);
    }
    public void setDays_on_leave()
    {
        Days_on_leave.setText("Days on Leave: "+parts[5]);
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
            labels.add("Permissions");
            labels.add("Leaves");

        BarData data = new BarData(labels, dataset);
        chart.setData(data);
    }
}

