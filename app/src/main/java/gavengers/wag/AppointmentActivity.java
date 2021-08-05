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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import gavengers.wag.util.Auth;
import gavengers.wag.util.Firestore;
import gavengers.wag.util.model.Appointment;

public class AppointmentActivity extends AppCompatActivity {
    Appointment appointment;
    Spinner spinnerAppointType;
    Spinner spinnerPlaceType;
    Button btnParticipants;
    Button btnCreate;
    EditText et_appointment_name;
    EditText et_participants;
    EditText et_memo;
    String[] items;
    TextView startDate;
    TextView startTime;
    TextView endDate;
    TextView endTime;
    SeekBar seekBar;

    TextView tvAppointParticipants;

    int importanceScore = 0;
    int[] years = new int[2];
    String[] months = new String[2];
    String[] days = new String[2];
    int[] hours = new int[2];
    int[] minutes = new int[2];
    boolean[] isPM = new boolean[2];
    // 0번째 인덱스 : 시작
    // 1번째 인덱스 : 종료

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_layout);

        spinnerAppointType = findViewById(R.id.spinner_appoint);
        spinnerPlaceType = findViewById(R.id.spinner_building);
        btnParticipants = findViewById(R.id.btn_appoint_participants);
        btnCreate = findViewById(R.id.btn_appoint_create);
        tvAppointParticipants = findViewById(R.id.tv_appoint_participants);
        et_appointment_name = findViewById(R.id.et_appointment_name);
        et_participants = new EditText(getApplicationContext());
        et_memo = findViewById(R.id.et_memo);
        seekBar = findViewById(R.id.seekbar_importance);
        items = getResources().getStringArray(R.array.array_appointment);
        startDate = findViewById(R.id.start_date);
        startTime = findViewById(R.id.start_time);
        endDate = findViewById(R.id.end_date);
        endTime = findViewById(R.id.end_time);
        Calendar calendar = Calendar.getInstance();

        hours[0] =calendar.get(Calendar.HOUR_OF_DAY);
        minutes[0] = calendar.get(Calendar.MINUTE);
        isPM[0] = hours[0] >= 12;
        years[0] = CalendarDay.today().getYear();
        months[0] = getMonthsFormatting(CalendarDay.today().getMonth());
        days[0] = getDaysFormatting(CalendarDay.today().getDay());

        hours[1] = hours[0] + 1;
        minutes[1] = minutes[0];
        isPM[1] = hours[1] >= 12;
        years[1] = CalendarDay.today().getYear();
        months[1] = getMonthsFormatting(CalendarDay.today().getMonth());
        days[1] = getDaysFormatting(CalendarDay.today().getDay());

        startTime.setText((isPM[0] ? "오후 " + (hours[0] == 12 ? hours[0] : (hours[0] - 12)) : "오전 " + (hours[0] == 0 ? 12 : hours[0])) + ":" + (minutes[0] < 10 ? "0"+minutes[0] : minutes[0]));
        endTime.setText((isPM[1] ? "오후 " + (hours[1] == 12 ? hours[1] : (hours[1] - 12)) : "오전 " + (hours[1] == 0 ? 12 :hours[1])) + ":" + (minutes[1] < 10 ? "0"+minutes[1] : minutes[1]));


        startDate.setText(years[0]+"-"+months[0]+"-"+days[0]);
        endDate.setText(years[1]+"-"+months[1]+"-"+days[1]);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AppointmentActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        if(years[1] == year && Integer.parseInt(months[1]) == (month +1)
                                && Integer.parseInt(days[1]) == dayOfMonth){
                            if(hours[0] < hours[1]){
                                years[0] = year;
                                months[0] = getMonthsFormatting(month);
                                days[0] = getDaysFormatting(dayOfMonth);
                                startDate.setText(years[0] + "-" + months[0] + "-" + days[0]);
                            }
                            else if(hours[0] == hours[1]){
                                if(minutes[0] < minutes[1]){
                                    years[0] = year;
                                    months[0] = getMonthsFormatting(month);
                                    days[0] = getDaysFormatting(dayOfMonth);
                                    startDate.setText(years[0] + "-" + months[0] + "-" + days[0]);
                                }
                                else{
                                    showToast("종료날짜가 시작날짜보다 앞설 수 없습니다!");
                                }
                            }
                            else{
                                showToast("종료날짜가 시작날짜보다 앞설 수 없습니다!");
                            }
                            return;
                        }
                        if(years[1] >= year && Integer.parseInt(months[1]) >= (month + 1) && Integer.parseInt(days[1]) >= dayOfMonth ) {
                            years[0] = year;
                            months[0] = getMonthsFormatting(month);
                            days[0] = getDaysFormatting(dayOfMonth);
                            startDate.setText(years[0] + "-" + months[0] + "-" + days[0]);
                        }
                        else{
                            showToast("종료날짜가 시작날짜보다 앞설 수 없습니다!");
                        }
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

                        if(years[0] == years[1] && Integer.parseInt(months[0]) == Integer.parseInt(months[1])
                                && Integer.parseInt(days[0]) == Integer.parseInt(days[1])){
                            if(hourOfDay < hours[1]){
                                isPM[0] = hourOfDay >= 12;
                                hours[0] = hourOfDay;
                                minutes[0] = minute;
                                startTime.setText((isPM[0] ? "오후 " + (hours[0] == 12 ? hours[0] : (hours[0] - 12)) : "오전 " + (hours[0] == 0 ? 12 : hours[0])) + ":" + (minutes[0] < 10 ? "0" + minutes[0] : minutes[0]));
                            }
                            else if(hourOfDay == hours[1]){
                                if(minute < minutes[1]){
                                    isPM[0] = hourOfDay >= 12;
                                    hours[0] = hourOfDay;
                                    minutes[0] = minute;
                                    startTime.setText((isPM[0] ? "오후 " + (hours[0] == 12 ? hours[0] : (hours[0] - 12)) : "오전 " + (hours[0] == 0 ? 12 : hours[0])) + ":" + (minutes[0] < 10 ? "0" + minutes[0] : minutes[0]));
                                }
                                else{
                                    showToast("종료날짜가 시작날짜보다 앞설 수 없습니다!");
                                }
                            }
                            else{
                                showToast("종료날짜가 시작날짜보다 앞설 수 없습니다!");
                            }
                            return;
                        }
                        if(years[0] <= years[1] && Integer.parseInt(months[0]) <= Integer.parseInt(months[1])
                                && Integer.parseInt(days[0]) <= Integer.parseInt(days[1])){

                            isPM[0] = hourOfDay >= 12;
                            hours[0] = hourOfDay;
                            minutes[0] = minute;
                            startTime.setText((isPM[0] ? "오후 " + (hours[0] == 12 ? hours[0] : (hours[0] - 12)) : "오전 " + (hours[0] == 0 ? 12 : hours[0])) + ":" + (minutes[0] < 10 ? "0" + minutes[0] : minutes[0]));
                            return;
                        }
                        if(hours[1] == hourOfDay && minutes[1] <= minute){
                            showToast("종료날짜가 시작날짜보다 앞설 수 없습니다!");
                            return;
                        }
                        if(hours[1] >= hourOfDay) {
                            isPM[0] = hourOfDay >= 12;
                            hours[0] = hourOfDay;
                            minutes[0] = minute;
                            startTime.setText((isPM[0] ? "오후 " + (hours[0] == 12 ? hours[0] : (hours[0] - 12)) : "오전 " + (hours[0] == 0 ? 12 : hours[0])) + ":" + (minutes[0] < 10 ? "0" + minutes[0] : minutes[0]));
                        }
                        else{
                            showToast("종료날짜가 시작날짜보다 앞설 수 없습니다!");
                        }
                    }
                }, hours[0], minutes[0], false);
                dialog.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AppointmentActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        if(years[0] == year && Integer.parseInt(months[0]) == (month +1) && Integer.parseInt(days[0]) == dayOfMonth){
                            if(hours[0] < hours[1]){
                                years[1] = year;
                                months[1] = getMonthsFormatting(month);
                                days[1] = getDaysFormatting(dayOfMonth);
                                endDate.setText(years[1] + "-" + months[1] + "-" + days[1]);
                            }
                            else if(hours[0] == hours[1]){
                                if(minutes[0] < minutes[1]){
                                    years[1] = year;
                                    months[1] = getMonthsFormatting(month);
                                    days[1] = getDaysFormatting(dayOfMonth);
                                    endDate.setText(years[1] + "-" + months[1] + "-" + days[1]);
                                }
                                else{
                                    showToast("종료날짜가 시작날짜보다 앞설 수 없습니다!");
                                }
                            }
                            else{
                                showToast("종료날짜가 시작날짜보다 앞설 수 없습니다!");
                            }
                            return;
                        }
                        if(years[0] <= year && Integer.parseInt(months[0]) <= (month +1) && Integer.parseInt(days[0]) <= dayOfMonth )
                        {
                            years[1] = year;
                            months[1] = getMonthsFormatting(month);
                            days[1] = getDaysFormatting(dayOfMonth);
                            endDate.setText(years[1] + "-" + months[1] + "-" + days[1]);
                        }
                        else{
                            showToast("종료날짜가 시작날짜보다 앞설 수 없습니다!");
                        }
                    }
                }, CalendarDay.today().getYear(), CalendarDay.today().getMonth(), CalendarDay.today().getDay());
                dialog.getDatePicker().setCalendarViewShown(false);
                dialog.show();
            }
        });

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //spinner.setAdapter(adapter);

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(AppointmentActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        if(hours[0] == hourOfDay && minutes[0] >= minute){
                            showToast("종료시간이 시작시간보다 앞설 수 없습니다!");
                            return;
                        }
                        if(years[0] == years[1] && Integer.parseInt(months[0]) == Integer.parseInt(months[1])
                                && Integer.parseInt(days[0]) == Integer.parseInt(days[1])){
                            if(hours[0] <= hourOfDay) {
                                isPM[1] = hourOfDay >= 12;
                                hours[1] = hourOfDay;
                                minutes[1] = minute;
                                endTime.setText((isPM[1] ? "오후 " + (hours[1] == 12 ? hours[1] : (hours[1] - 12)) : "오전 " + (hours[1] == 0 ? 12 : hours[1])) + ":" + (minutes[1] < 10 ? "0" + minutes[1] : minutes[1]));
                            }
                            else{
                                showToast("종료시간이 시작시간보다 앞설 수 없습니다!");
                            }
                            return;
                        }
                        if(years[0] <= years[1] && Integer.parseInt(months[0])
                                <= Integer.parseInt(months[1]) && Integer.parseInt(days[0]) <= Integer.parseInt(days[1])){
                            isPM[1] = hourOfDay >= 12;
                            hours[1] = hourOfDay;
                            minutes[1] = minute;
                            endTime.setText((isPM[1] ? "오후 " + (hours[1] == 12 ? hours[1] : (hours[1] - 12)) : "오전 " + (hours[1] == 0 ? 12 : hours[1])) + ":" + (minutes[1] < 10 ? "0" + minutes[1] : minutes[1]));
                        }

                    }
                }, hours[1] , minutes[1], false);
                dialog.show();
            }
        });

        /** author Taehyun Park
         *  스피너 리스너
         */
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

        /** author Taehyun Park
         *  SeekBar 리스너
         */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) { }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                importanceScore = seekBar.getProgress();
            }
        });

        /** author Taehyun Park
         *  Create버튼 누를 시 Firebase에 일정 생성
         */
        // 일정 생성 버튼 클릭 시
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> participants = new ArrayList<String>();
                String[] arrParticipants = null;
                String strMemo = "";
                String strParticipants = "";
                String strAppointName = "";

                if(et_appointment_name.getText().toString() != null || !et_appointment_name.getText().toString().isEmpty()){
                    strAppointName = et_appointment_name.getText().toString();
                }else{
                    Toast.makeText(getApplicationContext(),"일정 제목을 입력해주세요!",Toast.LENGTH_SHORT).show();
                }

                if(tvAppointParticipants.getText().toString() != null){
                    strParticipants = tvAppointParticipants.getText().toString();
                    arrParticipants = strParticipants.split(",");
                    Log.d("strParticipants",strParticipants);
                    System.out.println("strParticipants"+strParticipants);
                    System.out.println("strParticipants length"+arrParticipants.length);
                    for(int i = 0; i < arrParticipants.length; i++){
                        Log.d("arrParticipants[i]",arrParticipants[i]);
                        participants.add(arrParticipants[i]);
                    }
                }else{ Log.d("tvAppointParticipants",tvAppointParticipants.getText().toString()); }

                if(et_memo.getText().toString() == null){
                    strMemo = "";
                }else{ strMemo = et_memo.getText().toString(); }

                appointment = new Appointment(spinnerAppointType.getSelectedItemPosition() + 1,
                        strAppointName,
                        (years[0] + "-" + months[0] + "-" + days[0]+ (isPM[0] ? " 오후 " + (hours[0] == 12 ? hours[0] : (hours[0] - 12)) : " 오전 " + (hours[0] == 0 ? 12 : hours[0])) + ":" + (minutes[0] < 10 ? "0" + minutes[0] : minutes[0])),
                        (years[1] + "-" + months[1] + "-" + days[1]+ (isPM[1] ? " 오후 " + (hours[1] == 12 ? hours[1] : (hours[1] - 12)) : " 오전 " + (hours[1] == 0 ? 12 : hours[1])) + ":" + (minutes[1] < 10 ? "0" + minutes[1] : minutes[1])),
                        importanceScore,
                        spinnerPlaceType.getSelectedItemPosition() + 1,
                        participants,
                        strMemo,
                        Auth.getCurrentUser().getUid());

                Firestore.writeNewPost(appointment)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(),"약속 생성을 성공하였습니다", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "약속 생성을 실패하였습니다", Toast.LENGTH_SHORT).show();
                                Log.d("Failure",e.getMessage());
                            }
                        });
            }
        });

    }
    public String getMonthsFormatting(int month){
        return (month+1) < 10 ? "0"+(month+1) : (month+1)+"";
    }
    public String getDaysFormatting(int day){
        return day < 10 ? "0" + day : day+"";
    }
    public void showToast(String text){
        Toast.makeText(AppointmentActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
