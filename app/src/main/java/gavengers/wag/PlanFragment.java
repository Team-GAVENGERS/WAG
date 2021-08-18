package gavengers.wag;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarUtils;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.CalendarWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import gavengers.wag.util.Auth;
import gavengers.wag.util.Firestore;
import gavengers.wag.util.model.Appointment;

public class PlanFragment extends Fragment {
    private ArrayList<Appointment> infoList;
    private MaterialCalendarView materialCalendarView;
    private FloatingActionButton floatingActionButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.plan_layout, container, false);

        floatingActionButton = rootView.findViewById(R.id.f_btn_create_appointment);
        materialCalendarView = rootView.findViewById(R.id.calendar_view);

        getInfoData();

        CalendarDay today = CalendarDay.today();
        materialCalendarView.setCurrentDate(CalendarDay.from(today.getYear(),today.getMonth()+1,today.getDay()));
        materialCalendarView.setSelectedDate(CalendarDay.from(today.getYear(),today.getMonth()+1,today.getDay()));
//        materialCalendarView.clearSelection(); // 선택 모두 취소
        MaterialCalendarView.State state = materialCalendarView.state();
        MaterialCalendarView.StateBuilder stateBuilder= state.edit();
        stateBuilder.setMaximumDate(CalendarDay.from(2022,12,31));
        stateBuilder.setMinimumDate(CalendarDay.from(2019,1,1));
        stateBuilder.isCacheCalendarPositionEnabled(true);
        stateBuilder.commit();

        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
//        materialCalendarView.setContentDescriptionCalendar("에효");
        materialCalendarView.setArrowColor(Color.parseColor("#f1a53e"));
//        materialCalendarView.setLeftArrowMask(getResources().getDrawable(R.drawable.ic_launcher_foreground)); // 이동 버튼 디자인 변경
//        materialCalendarView.setContentDescriptionArrowFuture("앞"); //정체불명
//        materialCalendarView.setContentDescriptionArrowPast("뒤"); //정체불명
        materialCalendarView.setDynamicHeightEnabled(true);
        materialCalendarView.setTitleAnimationOrientation(MaterialCalendarView.HORIZONTAL);
        materialCalendarView.setSelectionColor(Color.parseColor("#f1e3a9"));
        materialCalendarView.setAllowClickDaysOutsideCurrentMonth(true);
        materialCalendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                return day.getYear()+"년 "+day.getMonth()+"월";
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AppointmentActivity.class);
                startActivity(intent);
            }
        });

        materialCalendarView.setDayFormatter(new DayFormatter() {
            @NonNull
            @Override
            public String format(@NonNull CalendarDay day) {
                return day.getDay()+"\n이잉기";
            }
        });

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Log.d("selected",date.getDay()+"");
            }
        });

        materialCalendarView.setOnTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth ,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        materialCalendarView.setCurrentDate(CalendarDay.from(year, month+1, dayOfMonth));
                    }
                }, today.getYear(), today.getMonth(),today.getDay());
                dialog.getDatePicker().setCalendarViewShown(false);
                dialog.show();
            }
        });

        return rootView;
    }

    private void getInfoData(){
        infoList = new ArrayList<>();
        Firestore.getInfo(Auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().size() > 0){
                        for(DocumentSnapshot doc : task.getResult()){
                            Appointment info = doc.toObject(Appointment.class);
                            infoList.add(info);
                        }
                    }
                }else{
                    Toast.makeText(getContext(), "getInfoData ERROR !!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
