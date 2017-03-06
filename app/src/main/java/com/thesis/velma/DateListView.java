package com.thesis.velma;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.thesis.velma.helper.DataBaseHandler;

/**
 * Created by User on 2/26/2017.
 */

public class DateListView extends ListActivity {

    String sd, ed;
    Context context;
    public static DataBaseHandler db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        db = new DataBaseHandler(context);
        String newDate="";
        boolean flag;
        Cursor c;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sd = preferences.getString("startdate", "value");
        ed = preferences.getString("enddate", "value");
        Log.i("Event list date", sd+" || " +ed);

        // storing string resources into Array
        String[] date_list = getResources().getStringArray(R.array.date_list);

        // ArrayList<String> eventNames = new ArrayList<>();

        for(int i = 0; i < 24; i++){

            c = db.getEventNames(sd, ed,i);

            c.moveToFirst();
            Log.i("Event c", ""+c.getCount());
            Log.i("Event datelist", sd + "  "+ed);
            if(c.getCount()>0){

                date_list[i]= date_list[i]+" "+c.getString(c.getColumnIndex("EventName"));
                c.moveToNext();
                while (!c.isAfterLast()) {

                    date_list[i]= date_list[i]+", "+c.getString(c.getColumnIndex("EventName"));

                    c.moveToNext();
                }
            }

        }

        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, date_list));
        Intent newIn = getIntent();
        final String e_id = newIn.getStringExtra("eventID");
        final ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                String newdate = (lv.getItemAtPosition(position).toString());

                if(newdate.length() > 14 ){
                    Toast.makeText(context, "Event Conflict, choose vacant time", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent i = new Intent(getApplicationContext(),ConflictActivity.class);

                    i.putExtra("eventID",e_id);
                    i.putExtra("newdate", newdate);
                    startActivity(i);

                }

            }
        });


    }



}

