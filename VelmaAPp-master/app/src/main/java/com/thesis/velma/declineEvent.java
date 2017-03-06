package com.thesis.velma;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class declineEvent extends AppCompatActivity {

    int id;
    String i;
    TextView d;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decline_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        d = (TextView)findViewById(R.id.deleteText);


       Bundle bundle = getIntent().getExtras();
        id=bundle.getInt("ID");
        Log.d("ID CANCELED", String.valueOf(id));

        value = Integer.toString(id);
        d.setText(value);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);

    }

}
