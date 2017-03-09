package com.thesis.velma;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.thesis.velma.LandingActivity.db;

/**
 * Created by jeanneviegarciano on 8/10/2016.
 */
public class OnboardingFragment2 extends Fragment implements View.OnClickListener {
    View rootView;
    public static int sYear, sMonth, sDay, sHour, sMinute;
    public static int eYear, eMonth, eDay, eHour, eMinute;
    public static TextView dateStart;
    public static TextView dateEnd;
    public static TextView timeStart;
    public static TextView timeEnd;
    public static EditText alarming;
    int PLACE_PICKER_REQUEST = 1;
    DatePickerDialog datePickerDialog;

    Context mcontext;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.onboarding_screen2, container, false);


        // Get current date by calender

        datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        // dateStart.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        dateStart.setText(String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year);


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


        mcontext = getActivity();

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

                            dateEnd.setText(String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year);

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


//                            final String name = event.getText().toString();
//                            final String eventDescription = descrip.getText().toString();
//                            final String eventLocation = locate.getText().toString();
                            final String startDate = dateStart.getText().toString();
                            final String endDate = dateEnd.getText().toString();
                            final String startTime = timeStart.getText().toString();
                            final String endTime = OnboardingFragment2.timeEnd.getText().toString();
//                            final String notify = OnboardingFragment2.alarming.getText().toString();
//                            final String invitedContacts = OnboardingFragment3.mtxtinvited.getText().toString();

                            Log.d("StarTime", startDate + " " + startTime);
                            Log.d("EndTime", endDate + " " + endTime);

//                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
//                            editor.putString("startdate", startDate);
//                            editor.putString("enddate", endDate);
//                            editor.apply();

//                            myCurrentEvent.add(name);
//                            myCurrentEvent.add(eventDescription);
//                            myCurrentEvent.add(eventLocation);
//                            myCurrentEvent.add(startDate);
//                            myCurrentEvent.add(endDate);
//                            myCurrentEvent.add(startTime);
//                            myCurrentEvent.add(endTime);
//                            myCurrentEvent.add(notify);
//                            myCurrentEvent.add(invitedContacts);
                            Log.d("StarTime", startDate + " " + startTime);
                            Log.d("EndTime", endDate + " " + endTime);


                            Cursor mycursor = db.searchEvents(startDate, startTime, endDate, endTime);
                            //Cursor mycursor = db.searchEvents("08-03-2017", "11:00", "08-03-2017", "1:00");

                            //Toast.makeText(getBaseContext(), "" + mycursor.getCount(), Toast.LENGTH_SHORT).show();
                            while (mycursor.moveToNext()) {

                                String eventstarttime = mycursor.getString(mycursor.getColumnIndex("StartTime"));

                                //Toast.makeText(getBaseContext(), "Here", Toast.LENGTH_SHORT).show();

                                SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                                try {
                                    Date Date1 = format.parse(eventstarttime);
                                    Date Date2 = format.parse(startTime);
                                    long mills = Date1.getTime() - Date2.getTime();
                                    Log.d("Data1", "" + Date1.getTime());
                                    Log.d("Data2", "" + Date2.getTime());
                                    int Hours = (int) (mills / (1000 * 60 * 60));
                                    int Mins = (int) (mills / (1000 * 60)) % 60;
                                    int myduration = 0;

                                    String diff = Hours + ":" + Mins; // updated value every1 second

                                    String timeduration;
                                    String[] duration = OnboardingActivity.travelduration.split(" ");


                                    if (duration.length > 2) {
                                        int mydurationH = Hours + Integer.parseInt(duration[0]);
                                        timeduration = mydurationH + ":" + Mins;
                                        myduration = Integer.parseInt(duration[0]);
                                    } else {
                                        int mydurationM = Mins + Integer.parseInt(duration[0]);
                                        timeduration = Hours + ":" + mydurationM;
                                        myduration = Integer.parseInt(duration[0]);
                                    }

                                    Toast.makeText(mcontext, "" + Hours + ":" + Integer.parseInt(duration[0]), Toast.LENGTH_SHORT).show();
                                    //if (diff.compareTo(timeduration) < 5) {
                                    // if(timeduration > Hours)

                                    if (myduration > Hours) {
                                        Toast.makeText(mcontext, "Cannot add event theres a conflict of location. Change time or location.", Toast.LENGTH_SHORT).show();
                                        OnboardingActivity.BtnAddEvent.setEnabled(false);
                                        return;
                                    } else {
                                        OnboardingActivity.BtnAddEvent.setEnabled(true);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }


                        }
                    }, sHour, sMinute, false);

            timePickerDialog.show();

        } else if (view == timeEnd)

        {

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
        } else if (view == alarming)

        {

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setIcon(R.drawable.alarm);
            alertBuilder.setTitle("Alarm before: ");
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
