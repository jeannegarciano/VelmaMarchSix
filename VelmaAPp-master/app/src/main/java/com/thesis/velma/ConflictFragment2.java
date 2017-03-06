package com.thesis.velma;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.thesis.velma.helper.DataBaseHandler;

import java.util.Calendar;

/**
 * Created by jeanneviegarciano on 8/10/2016.
 */
public class ConflictFragment2 extends Fragment implements View.OnClickListener {
    View rootView;
    public static int sYear, sMonth, sDay, sHour, sMinute;
    public static int eYear, eMonth, eDay, eHour, eMinute;
    public static TextView dateStart;
    public static TextView dateEnd;
    public static TextView timeStart;
    public static TextView timeEnd;
    public static EditText alarming;
    String id;
    String con_sd, con_ed;
    int PLACE_PICKER_REQUEST = 1;
    DatePickerDialog datePickerDialog;
    Context mcontext;
    public static DataBaseHandler db;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.onboarding_screen2, container, false);
        db = new DataBaseHandler(mcontext);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        id = preferences.getString("key", "value");
        String newtime = preferences.getString("date", "value");
        Log.i("Event Frag:", id+" "+newtime);
        String[] date_list = getResources().getStringArray(R.array.date_list);


        Log.i("Event Ac4", ""+id);
        final Cursor c = db.editConflictEvent(Long.valueOf(id));
//        Log.i("Event Ac5", ""+c);

        c.moveToFirst();
//
        con_sd = c.getString(2);
        con_ed = c.getString(3);
        Log.i("Event conAct", con_sd +" "+ con_ed);




        datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        dateStart.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, sYear, sMonth, sDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);


        dateStart = (TextView) rootView.findViewById(R.id.startdate);
        dateStart.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        dateStart.setInputType(InputType.TYPE_NULL);
        dateEnd = (TextView) rootView.findViewById(R.id.enddate);
        dateEnd.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        timeStart = (TextView) rootView.findViewById(R.id.starttime);
        timeStart.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        timeEnd = (TextView) rootView.findViewById(R.id.endtime);
        timeEnd.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        alarming = (EditText) rootView.findViewById(R.id.alarm);
        alarming.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        dateStart.setOnClickListener(this);
        dateEnd.setOnClickListener(this);
        timeStart.setOnClickListener(this);
        timeEnd.setOnClickListener(this);
        alarming.setOnClickListener(this);
        dateStart.setText(con_sd);
        dateEnd.setText(con_ed);

        int temp = 0;
        while(temp != 24) {
            String i = "00";
            int temp2=temp;
            if(temp == 23){
                i = "59";
                temp2=temp-1;
            }
            if (newtime.toString().equals(""+temp+":00 - "+(temp2+1)+":"+i+"")) {
                timeStart.setText(""+temp+":00");
                timeEnd.setText(""+(temp2+1)+":"+i+"");
                Log.i("Event Me", ""+temp+":00 - "+(temp2+1)+":"+i+"");
                //  Toast.makeText(mcontext, "", Toast.LENGTH_SHORT).show();
            }
            temp++;
        }



        return rootView;
    }


    @Override
    public void onClick(View view) {
        if (view == dateStart) {
            final Calendar c = Calendar.getInstance();
            sYear = c.get(Calendar.YEAR);
            sMonth = c.get(Calendar.MONTH);
            sDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog.show();
        } else if (view == dateEnd) {
            final Calendar c = Calendar.getInstance();
            eYear = c.get(Calendar.YEAR);
            eMonth = c.get(Calendar.MONTH);
            eDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            dateEnd.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, eYear, eMonth, eDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
            datePickerDialog.show();
        } else if (view == timeStart) {

            final Calendar c = Calendar.getInstance();
            sHour = c.get(Calendar.HOUR_OF_DAY);
            sMinute = c.get(Calendar.MINUTE);


            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            timeStart.setText(hourOfDay + ":" + minute);
                        }
                    }, sHour, sMinute, false);

            timePickerDialog.show();

        } else if (view == timeEnd) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            eHour = c.get(Calendar.HOUR_OF_DAY);
            eMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            timeEnd.setText(hourOfDay + ":" + minute);
                        }
                    }, eHour, eMinute, false);
            timePickerDialog.show();
        } else if (view == alarming) {

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setIcon(R.drawable.alarm);
            alertBuilder.setTitle("Alarm every: ");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.select_dialog_item);
            arrayAdapter.add("At time of event");
            arrayAdapter.add("10 minutes before the event");
            arrayAdapter.add("20 minutes before the event");
            arrayAdapter.add("30 minutes before the event");
            arrayAdapter.add("40 minutes before the event");
            arrayAdapter.add("50 minutes before the event");
            arrayAdapter.add("1 hour before the event");

            alertBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });

            alertBuilder.setAdapter(arrayAdapter,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            String strOS = arrayAdapter.getItem(which);
                            alarming.setText(strOS);
                            dialog.dismiss();
                        }
                    });

            final AlertDialog alertDialog = alertBuilder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    ListView listView = alertDialog.getListView();
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                        @Override
                        public boolean onItemLongClick(
                                AdapterView<?> parent, View view,
                                int position, long id) {
                            // TODO Auto-generated method stub
                            String strOS = arrayAdapter.getItem(position);
                            Toast.makeText(getContext(),
                                    "Long Press - Deleted Entry " + strOS,
                                    Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            return true;
                        }
                    });
                }
            });

            alertDialog.show();
        }


    }

}
