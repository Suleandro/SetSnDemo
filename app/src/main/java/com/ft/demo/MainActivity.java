package com.ft.demo;

import android.app.Activity;
import android.os.Bundle;

import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.ftpos.apiservice.aidl.device.IDeviceF100;
import com.ftpos.library.smartpos.device.Device;
import com.ftpos.library.smartpos.servicemanager.OnServiceConnectCallback;
import com.ftpos.library.smartpos.servicemanager.ServiceManager;


public class MainActivity extends Activity {
    private Device device;
    private TextView logText;
    private TextView tvData;
    private EditText etData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        logText = findViewById(R.id.tv_log);
        tvData = findViewById(R.id.tv_data);
        etData = findViewById(R.id.et_data);


        /**
         * Binding service, you can confirm whether the binding service is completed through the callback interface.
         * Optional.
         */
        ServiceManager.bindPosServer(this, new OnServiceConnectCallback() {
            @Override
            public void onSuccess() {
                Log.e("FTDEMO", "Binding Success！");

                device = Device.getInstance(MainActivity.this);
                Log.e("FTDEMO", "SDK Version:" + device.getSDKVersionName());
                Log.e("FTDEMO", "Serial Number:"+device.getSerialNumber());

                try {
                    String strCustomerSN = device.getCustomerSN();
                    if( strCustomerSN != null) {
                        Log.e("FTDEMO", "Serial Number Customer:" + strCustomerSN);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int var1) {
                Log.e("FTDEMO", "Binding Fail！");
            }
        });

        RelativeLayout rlSn = findViewById(R.id.rl_set_sn);
        rlSn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setSn();
            }
        });

        /* Added Info SN */
        RelativeLayout rlInfoSn = findViewById(R.id.rl_info_sn);
        rlInfoSn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InfoSn();
            }
        });



    }

    private void setSn() {
        setLogText("");
        try {
            device.setCustomerSN(etData.getText().toString());
            setLogText("Success");
        } catch (RemoteException e) {
            e.printStackTrace();
            setLogText("fail");

        }
    }

    private void InfoSn() {
        setLogText("");
        try {
            setLogText("Serial Number:" + device.getSerialNumber()+ "\n "
            + "Client Serial Number:" + device.getCustomerSN() + " ");
        }
        catch (RemoteException e)
        {
            setLogText("fail");
        }
    }

    private void setLogText(String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logText.setText(text);
            }
        });
    }

}


