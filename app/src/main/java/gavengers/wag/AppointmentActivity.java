package gavengers.wag;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class AppointmentActivity extends AppCompatActivity {
    Spinner spinner;
    String[] items;
    TextView startDate;
    TextView startTime;
    TextView endDate;
    TextView endTime;
    int years;
    String months;
    String days;
    int hours;
    int minutes;
    boolean isPM;
    boolean isEndPM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_layout);

        spinner = findViewById(R.id.spinner_appoint);
        items = getResources().getStringArray(R.array.array_appointment);
        startDate = findViewById(R.id.start_date);
        startTime = findViewById(R.id.start_time);
        endDate = findViewById(R.id.end_date);
        endTime = findViewById(R.id.end_time);
        Calendar calendar = Calendar.getInstance();

        hours =calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        isPM = hours >= 12;
        isEndPM = hours +1 >= 12;
        startTime.setText((isPM ? "오후 " + (hours == 12 ? hours : (hours - 12)) : "오전 " + (hours == 0 ? 12 : hours)) + ":" + (minutes < 10 ? "0"+minutes : minutes));
        endTime.setText((isEndPM ? "오후 " + (hours +1 == 12 ? hours +1 : (hours +1 - 12)) : "오전 " + (hours +1 == 0 ? 12 : hours +1)) + ":" + (minutes < 10 ? "0"+minutes : minutes));
        years = CalendarDay.today().getYear();
        months = getMonthsFormatting(CalendarDay.today().getMonth());
        days =  getDaysFormatting(CalendarDay.today().getDay());

        startDate.setText(years+"-"+months+"-"+days);
        endDate.setText(years+"-"+months+"-"+days);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AppointmentActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        years = year;
                        months = getMonthsFormatting(month);
                        days = getDaysFormatting(dayOfMonth);
                        startDate.setText(years+"-"+months+"-"+days);
                    }
                }, CalendarDay.today().getYear(), CalendarDay.today().getMonth(), CalendarDay.today().getDay());
                dialog.getDatePicker().setCalendarViewShown(false);
                dialog.show();
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog dialog = new TimePickerDialog(AppointmentActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        isPM = hourOfDay >= 12;
                        hours = hourOfDay;
                        minutes = minute;
                        startTime.setText((isPM ? "오후 " + (hours == 12 ? hours : (hours - 12)) : "오전 " + (hours == 0 ? 12 : hours)) + ":" + (minutes < 10 ? "0"+minutes : minutes));
                    }
                }, hours, minutes, false);
                dialog.show();
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(),items[position],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public String getMonthsFormatting(int month){
        return (month+1) < 10 ? "0"+(month+1) : (month+1)+"";
    }
    public String getDaysFormatting(int day){
        return day < 10 ? "0" + day : day+"";
    }

}
