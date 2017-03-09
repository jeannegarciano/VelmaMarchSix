package com.thesis.velma;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.thesis.velma.helper.OkHttp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.StringTokenizer;

import static android.content.ContentValues.TAG;
import static com.thesis.velma.LandingActivity.db;
import static com.thesis.velma.OnboardingFragment2.dateEnd;
import static com.thesis.velma.OnboardingFragment2.dateStart;
import static com.thesis.velma.OnboardingFragment2.timeStart;

/**
 * Created by jeanneviegarciano on 8/10/2016.
 */
public class OnboardingActivity extends AppCompatActivity {

    private ViewPager pager;
    private SmartTabLayout indicator;
    public Button skip;
    public static Button BtnAddEvent;
    public EditText event;
    Context context;

    public static TextView des;
    public static EditText descrip;
    public static TextView loc;
    public static TextView locate;
    public static TextView distanceduration;
    int PLACE_PICKER_REQUEST = 1;
    public static String travelduration;


    public static String geolocation;
    String modetravel = "driving";
    PlaceAutocompleteFragment autocompleteFragment;
    private PendingIntent pendingIntent;

    ArrayList<String> myConflictEvents = new ArrayList<String>();
    ArrayList<String> myCurrentEvent = new ArrayList<>();
    Dialog dialog;
    String e_name, e_desc, e_sd, e_ed, eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onboarding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        context = this;


        event = (EditText) findViewById(R.id.eventname);
        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (SmartTabLayout) findViewById(R.id.indicator);
        BtnAddEvent = (Button) findViewById(R.id.btnAddEvent);

        BtnAddEvent.setVisibility(View.GONE);

        des = (TextView) findViewById(R.id.eventName);
        descrip = (EditText) findViewById(R.id.name);
        loc = (TextView) findViewById(R.id.location);
        locate = (TextView) findViewById(R.id.locationText);
        locate.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        distanceduration = (TextView) findViewById(R.id.distanceduration);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(14.599512, 120.984222),
                new LatLng(14.599512, 120.984222)));


        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getAddress());//get place details here

                locate.setText("" + place.getAddress());
                geolocation = place.getAddress().toString();

                LandingActivity.latitude = place.getLatLng().latitude;
                LandingActivity.longtiude = place.getLatLng().longitude;


                Log.d("latlang", "" + LandingActivity.latitude + ":" + LandingActivity.longtiude);

                new getDetails().execute();

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
//                    case 0:
//                        return new OnboardingFragment1();
                    case 0:
                        return new OnboardingFragment2();
                    case 1:
                        return new OnboardingFragment3();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        pager.setAdapter(adapter);

        indicator.setViewPager(pager);

        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    BtnAddEvent.setVisibility(View.GONE);
                } else if (position == 1) {
                    OnboardingFragment3.mtxtinvited.setText("");
                    BtnAddEvent.setVisibility(View.VISIBLE);
                }

            }

        });


        BtnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random r = new Random();
                final long unixtime = (long) (1293861599 + r.nextDouble() * 60 * 60 * 24 * 365);


                final String name = event.getText().toString();
                final String eventDescription = descrip.getText().toString();
                final String eventLocation = locate.getText().toString();
                final String startDate = dateStart.getText().toString();
                final String endDate = dateEnd.getText().toString();
                final String startTime = timeStart.getText().toString();
                final String endTime = OnboardingFragment2.timeEnd.getText().toString();
                final String notify = OnboardingFragment2.alarming.getText().toString();
                final String invitedContacts = OnboardingFragment3.mtxtinvited.getText().toString();

                Log.d("StarTime", startDate + " " + startTime);
                Log.d("EndTime", endDate + " " + endTime);

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("startdate", startDate);
                editor.putString("enddate", endDate);
                editor.apply();

                myCurrentEvent.add(name);
                myCurrentEvent.add(eventDescription);
                myCurrentEvent.add(eventLocation);
                myCurrentEvent.add(startDate);
                myCurrentEvent.add(endDate);
                myCurrentEvent.add(startTime);
                myCurrentEvent.add(endTime);
                myCurrentEvent.add(notify);
                myCurrentEvent.add(invitedContacts);
                Log.d("StarTime", startDate + " " + startTime);
                Log.d("EndTime", endDate + " " + endTime);


//                Cursor mycursor = db.searchEvents(startDate, startTime, endDate, endTime);
//                //Cursor mycursor = db.searchEvents("08-03-2017", "11:00", "08-03-2017", "1:00");
//
//                //Toast.makeText(getBaseContext(), "" + mycursor.getCount(), Toast.LENGTH_SHORT).show();
//                while (mycursor.moveToNext()) {
//
//                    String eventstarttime = mycursor.getString(mycursor.getColumnIndex("StartTime"));
//
//                    //Toast.makeText(getBaseContext(), "Here", Toast.LENGTH_SHORT).show();
//
//                    SimpleDateFormat format = new SimpleDateFormat("hh:mm");
//                    try {
//                        Date Date1 = format.parse(eventstarttime);
//                        Date Date2 = format.parse(startTime);
//                        long mills = Date1.getTime() - Date2.getTime();
//                        Log.d("Data1", "" + Date1.getTime());
//                        Log.d("Data2", "" + Date2.getTime());
//                        int Hours = (int) (mills / (1000 * 60 * 60));
//                        int Mins = (int) (mills / (1000 * 60)) % 60;
//
//                        String diff = Hours + ":" + Mins; // updated value every1 second
//
//                        String timeduration;
//                        String[] duration = travelduration.split(" ");
//                        int mydurationH = Hours + Integer.parseInt(duration[0]);
//                        int mydurationM = Mins + Integer.parseInt(duration[0]);
//
//                        if (duration.length > 2) {
//                            timeduration = mydurationH + ":" + Mins;
//                        } else {
//                            timeduration = Hours + ":" + mydurationM;
//                        }
//
//                        Toast.makeText(getBaseContext(), "" + diff.compareTo(timeduration), Toast.LENGTH_SHORT).show();
//                        if (diff.compareTo(timeduration) < 5) {
//                            Toast.makeText(getBaseContext(), "Here 1.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getBaseContext(), "Cannot add event theres a conflict of location.", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                }

                final Cursor c = db.conflictChecker(startDate, startTime, endDate, endTime);


                //region saveEvent
                if (name.isEmpty()) {
                    Toast.makeText(context, "Invalid Event Name", Toast.LENGTH_SHORT).show();
                } else if (eventDescription.isEmpty()) {
                    Toast.makeText(context, "Invalid Event Description", Toast.LENGTH_SHORT).show();
                } else if (startDate.isEmpty() || endDate.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                    Toast.makeText(context, "Please add Starting date and completion date.", Toast.LENGTH_SHORT).show();
                } else {

                    Log.i("Event con", "1");


                    if (c != null && c.getCount() > 0) {

                        Log.i("Event con", "2");
                        c.moveToFirst();
                        while (!c.isAfterLast()) {

                            Toast.makeText(OnboardingActivity.this, "Conflict in: " + c.getString(1) + "   " + c.getString(2) + "  " + c.getString(3) + "  " + c.getString(4), Toast.LENGTH_LONG).show();
                            Log.i("Event log", c.getString(0) + " >> " + c.getString(1) + " >> " + c.getString(2) + " >> " + c.getString(3) + " >> " + c.getString(4) + " >> ");


                            c.moveToNext();
                        }


                        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                .setTitle("Error in inserting event")
                                .setMessage("There is conflict, please change date and time or prioritize this event")
                                .setPositiveButton("Add current event", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        Log.i("Event con", "4");
                                        myCurrentEvent.add(name);
                                        myCurrentEvent.add(eventDescription);
                                        myCurrentEvent.add(eventLocation);
                                        myCurrentEvent.add(startDate);
                                        myCurrentEvent.add(endDate);
                                        myCurrentEvent.add(startTime);
                                        myCurrentEvent.add(endTime);
                                        myCurrentEvent.add(notify);
                                        myCurrentEvent.add(invitedContacts);


                                        c.moveToFirst();

                                        e_name = c.getString(1);
                                        e_desc = c.getString(2);
                                        e_sd = c.getString(3);
                                        e_ed = c.getString(6);


                                        event.setText(e_name);
                                        descrip.setText(e_desc);
                                        OnboardingFragment2.dateStart.setText(e_sd);
                                        OnboardingFragment2.dateEnd.setText(e_ed);
                                        Log.i("Event con", e_name + " " + e_desc + " " + e_sd + " " + e_ed);


                                        String sd = startDate;
                                        String ed = endDate;
                                        String st = startTime;
                                        String et = endTime;

                                        StringTokenizer sdtoken = new StringTokenizer(sd, "-");
                                        StringTokenizer edtoken = new StringTokenizer(ed, "-");
                                        StringTokenizer sttoken = new StringTokenizer(st, ":");
                                        StringTokenizer ettoken = new StringTokenizer(et, ":");

                                        String sdd = sdtoken.nextToken();
                                        String sdm = sdtoken.nextToken();
                                        String sdy = sdtoken.nextToken();
                                        String edd = edtoken.nextToken();
                                        String edm = edtoken.nextToken();
                                        String edy = edtoken.nextToken();

                                        String sth = sttoken.nextToken();
                                        String stm = sttoken.nextToken();
                                        String eth = ettoken.nextToken();
                                        String etm = ettoken.nextToken();

                                        if (sdd.length() == 1) {
                                            sdd = "0" + sdd;
                                        }
                                        if (sdm.length() == 1) {
                                            sdm = "0" + sdm;
                                        }
                                        if (edd.length() == 1) {
                                            edd = "0" + edd;
                                        }
                                        if (edm.length() == 1) {
                                            edm = "0" + edm;
                                        }

                                        if (sth.length() == 1) {
                                            sth = "0" + sth;
                                        }
                                        if (stm.length() == 1) {
                                            stm = "0" + stm;
                                        }
                                        if (eth.length() == 1) {
                                            eth = "0" + eth;
                                        }
                                        if (etm.length() == 1) {
                                            etm = "0" + etm;
                                        }
                                        sd = sdd + "-" + sdm + "-" + sdy;
                                        ed = edd + "-" + edm + "-" + edy;

                                        st = sth + ":" + stm;
                                        et = eth + ":" + etm;

                                        LandingActivity.db.saveEvent(LandingActivity.imei, unixtime, name, eventDescription, eventLocation, sd, st, ed, et, notify, invitedContacts, LandingActivity.latitude, LandingActivity.longtiude);
                                        OkHttp.getInstance(getBaseContext()).saveEvent(unixtime, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, notify, invitedContacts);

                                        for (int i = 0; i <= OnboardingFragment3.invitedContacts.size() - 1; i++) {
                                            String[] target = OnboardingFragment3.invitedContacts.get(i).split("@");
                                            OkHttp.getInstance(context).sendNotification("Invitation", unixtime, name, eventDescription, eventLocation,
                                                    startDate, startTime, endDate, endTime, notify, invitedContacts, target[0] + "Velma");//target[0]
                                        }


                                        db.deleteEvent(Long.parseLong(eventID));
                                        OkHttp.getInstance(context).deleteEvent(eventID);


                                        dialog.dismiss();


                                    }


                                }).setNegativeButton("Change", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        OnboardingFragment2.dateStart.setText("");
                                        OnboardingFragment2.dateEnd.setText("");
                                        OnboardingFragment2.timeStart.setText("");
                                        OnboardingFragment2.timeEnd.setText("");
                                        dialog.dismiss();


                                        // Do stuff when user neglects.
                                    }
                                }).setNeutralButton("Suggest",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent in = new Intent(OnboardingActivity.this, DateListView.class);
//
                                                in.putExtra("eventID", eventID);
                                                startActivity(in);
//
                                            }
                                        }

                                );

                        //    builder.setTitle("Select Event Priority");


////////////////////////////////////////////////////////////////////////////////////
                        ListView modeList = new ListView(context);
                        myConflictEvents.add(myCurrentEvent.get(0));
                        c.moveToFirst();
                        eventID = c.getString(0);
                        Log.i("Event con", "5");

                        while (!c.isAfterLast()) {


                            Log.i("Event conn", " " + c.getString(c.getColumnIndex("EventName")));
                            Log.i("Event id", eventID);
                            myConflictEvents.add(c.getString(c.getColumnIndex("EventName"))); //this adds an element to the list.
                            c.moveToNext();
                        }


                        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, myConflictEvents);
                        modeList.setAdapter(modeAdapter);
                        builder.setView(modeList);
                        builder.create();
                        builder.show();


//


                    } else {
                        String sd = startDate;
                        String ed = endDate;
                        String st = startTime;
                        String et = endTime;

                        StringTokenizer sdtoken = new StringTokenizer(sd, "-");
                        StringTokenizer edtoken = new StringTokenizer(ed, "-");
                        StringTokenizer sttoken = new StringTokenizer(st, ":");
                        StringTokenizer ettoken = new StringTokenizer(et, ":");

                        String sdd = sdtoken.nextToken();
                        String sdm = sdtoken.nextToken();
                        String sdy = sdtoken.nextToken();
                        String edd = edtoken.nextToken();
                        String edm = edtoken.nextToken();
                        String edy = edtoken.nextToken();

                        String sth = sttoken.nextToken();
                        String stm = sttoken.nextToken();
                        String eth = ettoken.nextToken();
                        String etm = ettoken.nextToken();

                        if (sdd.length() == 1) {
                            sdd = "0" + sdd;
                        }
                        if (sdm.length() == 1) {
                            sdm = "0" + sdm;
                        }
                        if (edd.length() == 1) {
                            edd = "0" + edd;
                        }
                        if (edm.length() == 1) {
                            edm = "0" + edm;
                        }

                        if (sth.length() == 1) {
                            sth = "0" + sth;
                        }
                        if (stm.length() == 1) {
                            stm = "0" + stm;
                        }
                        if (eth.length() == 1) {
                            eth = "0" + eth;
                        }
                        if (etm.length() == 1) {
                            etm = "0" + etm;
                        }
                        sd = sdd + "-" + sdm + "-" + sdy;
                        ed = edd + "-" + edm + "-" + edy;

                        st = sth + ":" + stm;
                        et = eth + ":" + etm;


                        String[] mydates = startDate.split("-");
                        String[] mytimes = startTime.split(":");


                        //HARDCODED VALUES 10:51
                        Calendar calNow = Calendar.getInstance();
                        Calendar calSet = (Calendar) calNow.clone();


                        Log.d("Calendar.YEAR", "" + Integer.parseInt(mydates[2]));
                        Log.d("Calendar.MONTH", "" + Integer.parseInt(mydates[1]));
                        Log.d("Calendar.DATE", "" + Integer.parseInt(mydates[0]));
                        Log.d("Calendar.HOUR_OF_DAY", "" + Integer.parseInt(mytimes[0]));
                        Log.d("Calendar.MINUTE", "" + Integer.parseInt(mytimes[1]));

                        String date = "" + mydates[0] + "-" + mydates[1] + "-" + mydates[2];

                        int AM_PM;
                        if (Integer.parseInt(mytimes[0]) < 12) {
                            AM_PM = 0;
                        } else {
                            AM_PM = 1;
                        }


                        calSet.setTimeInMillis(System.currentTimeMillis());
                        calSet.clear();
                        calSet.set(Integer.parseInt(mydates[2]), Integer.parseInt(mydates[1]) - 1, Integer.parseInt(mydates[0]), Integer.parseInt(mytimes[0]), Integer.parseInt(mytimes[1]) - 10);

                        int count = LandingActivity.db.retrieveDayEvent();
//                LandingActivity.db.close();


                        Intent myIntent = new Intent(context, AlarmReceiver.class);


                        myIntent.putExtra("unix", unixtime);
                        myIntent.putExtra("name", name);
                        myIntent.putExtra("description", eventDescription);
                        myIntent.putExtra("location", eventLocation);
                        myIntent.putExtra("start", startTime);
                        myIntent.putExtra("end", endTime);
                        myIntent.putExtra("dateS", startDate);
                        myIntent.putExtra("dateE", endDate);
                        myIntent.putExtra("people", invitedContacts);

                        Log.d("MyData", name);

                        pendingIntent = PendingIntent.getBroadcast(context, count, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        Log.d("Count", "" + count);

                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);


                        LandingActivity.db.saveEvent(LandingActivity.imei, unixtime, name, eventDescription, eventLocation, sd, st, ed, et, notify, invitedContacts, LandingActivity.latitude, LandingActivity.longtiude);
                        OkHttp.getInstance(getBaseContext()).saveEvent(unixtime, name, eventDescription, eventLocation, startDate, startTime, endDate, endTime, notify, invitedContacts);

                        for (int i = 0; i <= OnboardingFragment3.invitedContacts.size() - 1; i++) {
                            String[] target = OnboardingFragment3.invitedContacts.get(i).split("@");
                            OkHttp.getInstance(context).sendNotification("Invitation", unixtime, name, eventDescription, eventLocation,
                                    startDate, startTime, endDate, endTime, notify, invitedContacts, target[0] + "Velma");//target[0]
                        }

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();


                    }
                }
                //endregion

            }
        });


    }

    public String addTime(int hour, int minute, int minutesToAdd) {
        Calendar calendar = new GregorianCalendar(1990, 1, 1, hour, minute);
        calendar.add(Calendar.MINUTE, minutesToAdd);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String date = sdf.format(calendar.getTime());
        return date;
    }

    class getDetails extends AsyncTask<Void, Void, String> {

        protected String getASCIIContentFromEntity(HttpEntity entity)
                throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);
                if (n > 0)
                    out.append(new String(b, 0, n));
            }
            return out.toString();
        }

        @Override
        protected String doInBackground(Void... params) {

            String text = null;
            String coordinates = LandingActivity.latitude + "," + LandingActivity.longtiude;
            Log.d("Coordinates", coordinates);
            try {
                String regAPIURL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + LandingActivity.origlatitude + "," + LandingActivity.origlongitude;
                regAPIURL = regAPIURL + "&destinations=" + URLEncoder.encode(coordinates);
                regAPIURL = regAPIURL + "&mode=" + URLEncoder.encode(modetravel);
                regAPIURL = regAPIURL + "&key=AIzaSyDWjoAbJf9uDrLCFAM_fCSWxP0muVEGbOA";
                Log.d("URI", regAPIURL);
                HttpGet httpGet = new HttpGet(regAPIURL);
                HttpParams httpParameters = new BasicHttpParams();
                int timeoutConnection = 60000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 60000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);

            } catch (Exception e) {
                text = null;
            }

            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Result", s);


            if (s != null) {

                try {

                    String distance = new JSONObject(s)
                            .getJSONArray("rows")
                            .getJSONObject(0)
                            .getJSONArray("elements")
                            .getJSONObject(0)
                            .getJSONObject("distance").getString("text");

                    String duration = new JSONObject(s)
                            .getJSONArray("rows")
                            .getJSONObject(0)
                            .getJSONArray("elements")
                            .getJSONObject(0)
                            .getJSONObject("duration").getString("text");

                    travelduration = duration;

                    distanceduration.setText("Distance : " + distance + ": Duration : " + duration);
                    distanceduration.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }


}
