package com.thesis.velma;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.thesis.velma.helper.DataBaseHandler;

public class acceptEvent extends AppCompatActivity implements View.OnClickListener {

    Context mcontext;
    public static DataBaseHandler db;
    int i;
    TextView title, ename, n, edescription, description, sdText, sd, edText, ed, stText,st, et, etText, fText, f, lText, l;
    TextView userT, userId, eventT, eventId;
    Button accept;
    String en, des,sDate,endDate, sTime, eTime, iFriends,locat, idUser, idEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mcontext = this;

        db = new DataBaseHandler(mcontext);

        title = (TextView)findViewById(R.id.textView);
        ename = (TextView)findViewById(R.id.eventName);
        n = (TextView)findViewById(R.id.name);
        edescription = (TextView)findViewById(R.id.eventDescription);
        description = (TextView)findViewById(R.id.eventDescription1);
        sdText = (TextView)findViewById(R.id.sdate);
        sd = (TextView)findViewById(R.id.sdate1);
        edText = (TextView)findViewById(R.id.edate);
        ed = (TextView)findViewById(R.id.edate1);
        stText = (TextView)findViewById(R.id.stime);
        st = (TextView)findViewById(R.id.stime1);
        etText = (TextView)findViewById(R.id.etime);
        et = (TextView)findViewById(R.id.etime1);
        fText = (TextView)findViewById(R.id.friends);
        f = (TextView)findViewById(R.id.friends1);
        accept = (Button)findViewById(R.id.acceptEvent);
        accept.setOnClickListener(this);
        lText = (TextView)findViewById(R.id.location);
        l = (TextView)findViewById(R.id.location1);
        userT = (TextView)findViewById(R.id.userIdText);
        userId = (TextView)findViewById(R.id.userIdText1);
        eventT = (TextView)findViewById(R.id.eventId);
        eventId = (TextView)findViewById(R.id.eventId1);


        Bundle b = getIntent().getExtras();
        i = b.getInt("ID");

//        db.saveEvent(LandingActivity.imei, b.getLong("eventid"),
//                b.getString("eventname"), b.getString("eventDescription"), b.getString("eventLocation")
//                , b.getString("eventStartDate"), b.getString("eventStartTime"),
//                b.getString("eventEndDate"), b.getString("eventEndTime"), b.getString("notify"),
//                b.getString("invitedfriends"));
//
//
//        Intent intent = new Intent();
//        setResult(RESULT_OK, intent);
//        finish();


        //Okay button from alarm notif
//
//        en = b.getString("name");
//        des = b.getString("description");
//        sDate =  b.getString("dateS");
//        endDate = b.getString("dateE");
//        sTime = b.getString("start");
//        eTime = b.getString("end");
//        iFriends = b.getString("people");
//        locat = b.getString("location");
//
//        n.setText(en);
//        description.setText(des);
//        sd.setText(sDate);
//        ed.setText(endDate);
//        st.setText(sTime);
//        et.setText(eTime);
//        f.setText(iFriends);
//        l.setText(locat);


        en = b.getString("eventname");
        des = b.getString("eventDescription");
        sDate =  b.getString("eventStartDate");
        endDate = b.getString("eventEndDate");
        sTime = b.getString("eventStartTime");
        eTime = b.getString("eventEndTime");
        iFriends = b.getString("invitedfirends");
        locat = b.getString("eventLocation");
        idUser = b.getString("userid");
        idEvent = b.getString("eventid");


        n.setText(en);
        description.setText(des);
        sd.setText(sDate);
        ed.setText(endDate);
        st.setText(sTime);
        et.setText(eTime);
        f.setText(iFriends);
        l.setText(locat);
        userId.setText(idUser);
        eventId.setText(idEvent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(i);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(acceptEvent.this, "Saved button is clicked: ",Toast.LENGTH_LONG).show();

    }
}
