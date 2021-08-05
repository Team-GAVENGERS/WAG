package gavengers.wag;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

import gavengers.wag.util.model.Appointment;

public class AppointmentActivity extends AppCompatActivity {
    Appointment appointment;
    Spinner spinnerAppointType;
    Spinner spinnerPlaceType;
    Button btnParticipants;
    Button btnCreate;
    EditText et_participants;
    EditText et_memo;
    String[] items;
    TextView startDate;
    TextView startTime;
    TextView endDate;
    TextView endTime;
    TextView tvAppointParticipants;
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

        spinnerAppointType = findViewById(R.id.spinner_appoint);
        spinnerPlaceType = findViewById(R.id.spinner_building);
        btnParticipants = findViewById(R.id.btn_appoint_participants);
        btnCreate = findViewById(R.id.btn_appoint_create);
        tvAppointParticipants = findViewById(R.id.tv_appoint_participants);
        et_participants = new EditText(getApplicationContext());
        et_memo = findViewById(R.id.et_memo);

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

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //spinner.setAdapter(adapter);

        // 일정 유형 스피너 선택 리스너
        spinnerAppointType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(getApplicationContext(),items[position],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        // 약속 장소 스피너 선택 리스너
        spinnerPlaceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        btnParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(AppointmentActivity.this);

                dlg.setTitle("참여자 리스트 입력")
                        .setMessage("참여자를 입력하세요 (,단위로 입력)")
                        .setCancelable(false)
                        .setView(et_participants)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String value = et_participants.getText().toString();
                                tvAppointParticipants.setText(value);
                                if(et_participants.getParent() != null) { // 중복 View 오류 방지를 위해 반드시 removeView 필요
                                    ((ViewGroup)et_participants.getParent()).removeView(et_participants); // <- fix
                                }
                            }
                        });

                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if(et_participants.getParent() != null) {  // 중복 View 오류 방지를 위해 반드시 removeView 필요
                            ((ViewGroup)et_participants.getParent()).removeView(et_participants);
                        }
                    }
                });
                dlg.create().show();
            }
        });
        // 일정 생성 버튼 클릭 시
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strParticipants;


                if(et_participants.getText().toString() == null){
                    strParticipants = "";
                }else{
                    strParticipants = et_participants.getText().toString();
                }


                appointment.setAppointType(spinnerAppointType.getSelectedItemPosition() + 1);
                appointment.setStartTime();
                appointment.setEndTime();
                appointment.setImportance();
                appointment.setPlace();
                appointment.setParticipants();
                appointment.setMemoStr(strParticipants);

                // firestore 코드
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
