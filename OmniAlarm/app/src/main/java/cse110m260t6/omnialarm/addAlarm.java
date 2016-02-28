package cse110m260t6.omnialarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by dadongjing on 2/6/16.
 */
public class addAlarm extends AppCompatActivity{

        Button choose_time;
        EditText T1;
        EditText T2;
        TextView T3;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_alarm);


            //find the button by id
            choose_time = (Button)findViewById(R.id.Choose_time);
            
            /* add button logic design */
            choose_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* for the time picker, we need to check if it support API 16. Otherwise,
                     * we are going to use text box in the xml file to obtain hour and minute.
                     */

                    //get the hour and minute user specified
                    T1 = (EditText)findViewById(R.id.hour);
                    T2 = (EditText)findViewById(R.id.minute);
                    T3 = (TextView)findViewById(R.id.time_not_valid);
                    String hour = T1.getText().toString();
                    String minute = T2.getText().toString();
                    //get the temp alarm from database
                    Alarm myAl = Database.getTempAlarm();

                    if(!hour.equals("")  && !minute.equals("")){
                        int hour_int = Integer.parseInt(hour);
                        int minute_int = Integer.parseInt(minute);
                        if(hour_int < 24 && hour_int > 0 && minute_int > 0 && minute_int < 60){

                            //set up the calendar object of the specified hour and minute
                            Calendar myCa = Calendar.getInstance();
                            myCa.set(Calendar.HOUR_OF_DAY,hour_int);
                            myCa.set(Calendar.MINUTE,minute_int);



                            //update the alarm time
                            myAl.setTime(myCa);

                            //update the temp alarm in the database
                            Database.updateTemp(myAl);

                            //jump to next page that prompt user to enter ring tone
                            Intent jumpActivity = new Intent(v.getContext(), cse110m260t6.omnialarm.ringTone.class);
                            startActivity(jumpActivity);
                        }
                        else{
                            String errorMes = new String("Time NOT VALID!");
                            T3.setText(errorMes);
                        }
                    }
                    else{
                        String errorMessage = new String("Please INPUT TIME");
                        T3.setText(errorMessage);
                    }

                }
            });

        }


    }

