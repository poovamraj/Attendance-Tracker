package com.example.poovam.attendancetracker1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.*;
import java.util.Date;


public class MainActivity extends ActionBarActivity implements LocationListener{

    Button CheckInButton;
    Button CheckOutButton;
    ImageButton HRbutton;
    private Socket client;
    String address;
    String response="";
    ImageButton StatsButton;
    LocationManager locationManager;
    Double latitude;
    Double longitude;
    String choice;
    String HR_password="a";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckInButton = (Button) findViewById(R.id.CheckIn);
        CheckOutButton=(Button) findViewById(R.id.CheckOut);
       StatsButton=(ImageButton)findViewById(R.id.StatsButton);
        HRbutton=(ImageButton)findViewById(R.id.HRbutton);

        //GPS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, this);//Network provider is used
                                                                           // because gps doesnt work inside buildings

        //Wifi
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        address = info.getMacAddress();

        CheckInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (true) {
                choice="1";
                SendMessage sendMessageTask = new SendMessage();
                sendMessageTask.execute();
            }
                else
                Toast.makeText(getBaseContext(),"Not inside office yet",Toast.LENGTH_SHORT).show();
            }
        });
        CheckOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (true) {
                    choice = "2";
                    SendMessage sendMessageTask = new SendMessage();
                    sendMessageTask.execute();
                } else
                    Toast.makeText(getBaseContext(), "Not inside office yet", Toast.LENGTH_SHORT).show();
            }
        });
        StatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice="3";
                SendMessage sendMessageTask = new SendMessage();
                sendMessageTask.execute();
            }
        });
        HRbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice="4";
            //Pop up box for HR to enter password
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Enter Password");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HR_password = input.getText().toString();
                        if(HR_password.equals("1234")) {
                            Intent i=new Intent(MainActivity.this,forHR.class);
                            startActivity(i);

                        }
                        else
                        {
                            Toast.makeText(getBaseContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {

        latitude=location.getLatitude();
        longitude=location.getLongitude();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

        Toast.makeText(getBaseContext(), "Switch ON GPS to enter into the app", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);

    }


    private class SendMessage extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

                try {
                    DataOutputStream dataOutputStream = null;
                    DataInputStream dataInputStream = null;
                    client = new Socket("192.168.0.105", 9997);
                    dataOutputStream = new DataOutputStream(client.getOutputStream());
                    dataInputStream = new DataInputStream(client.getInputStream());

                    if (address != null) {

                        dataOutputStream.writeUTF(choice + "-" + address);//request that goes to the server
                        Log.i("Check", choice + "-" + address);//choice represents button pressed
                        response = dataInputStream.readUTF();//and address represents the MAC address of the phone
                        Log.i("response", response);
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
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }


                return null;
            }


        protected void onPostExecute(Void result)
        {
            if(client!=null) {//if connected to network of server
                if (choice.equals("1") || choice.equals("2"))
                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_SHORT).show();
                super.onPostExecute(result);
                if (choice.equals("3")) {
                    Intent i = new Intent(MainActivity.this, StatsShower.class);
                    i.putExtra("key", response);
                    startActivity(i);

                }
            }
            else//if not connected shows exception to connect
                Toast.makeText(getBaseContext(),"Connect to network",Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
