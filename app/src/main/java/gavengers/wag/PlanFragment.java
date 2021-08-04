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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarUtils;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.CalendarWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlanFragment extends Fragment {
    private MaterialCalendarView materialCalendarView;
    private FloatingActionButton floatingActionButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.plan_layout, container, false);

        floatingActionButton = rootView.findViewById(R.id.f_btn_create_appointment);
        materialCalendarView = rootView.findViewById(R.id.calendar_view);
        CalendarDay d = CalendarDay.today();
        materialCalendarView.clearSelection();
        MaterialCalendarView.State da = materialCalendarView.state();

        MaterialCalendarView.StateBuilder aaa= da.edit();
        aaa.setMaximumDate(CalendarDay.from(2022,12,31));
        aaa.setMinimumDate(CalendarDay.from(2019,1,1));
        aaa.isCacheCalendarPositionEnabled(true);
        aaa.commit();
        materialCalendarView.setContentDescriptionCalendar("에효");
        materialCalendarView.setArrowColor(Color.parseColor("#f1a53e"));
        materialCalendarView.setLeftArrowMask(getResources().getDrawable(R.drawable.ic_launcher_foreground));
        materialCalendarView.setContentDescriptionArrowFuture("앞");
        materialCalendarView.setContentDescriptionArrowPast("뒤");
        materialCalendarView.setDynamicHeightEnabled(true);
        materialCalendarView.setTitleAnimationOrientation(MaterialCalendarView.HORIZONTAL);
        materialCalendarView.setCurrentDate(CalendarDay.from(2021,10,1));
        materialCalendarView.setSelectionColor(Color.parseColor("#f1e3a9"));
        materialCalendarView.setAllowClickDaysOutsideCurrentMonth(true);
        materialCalendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                return day.getYear()+"에에에엥ㅇㅇㅇ"+day.getMonth();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AppointmentActivity.class);
                startActivity(intent);
            }
        });

//        materialCalendarView.setWeekDayFormatter(new WeekDayFormatter() {
//            @Override
//            public CharSequence format(int dayOfWeek) {
//                return dayOfWeek+"ㅇ정뱔비";
//            }
//        });
        materialCalendarView.setDayFormatter(new DayFormatter() {
            @NonNull
            @Override
            public String format(@NonNull CalendarDay day) {
                return day.getDay()+"\n이잉기";
            }
        });

        List<CalendarDay> list = materialCalendarView.getSelectedDates();
//        materialCalendarView.set
        //materialCalendarView.selectRange(CalendarDay.from(2019,1,1),CalendarDay.from(2022, 12,31));
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //Log.d("dwd",list.get(0).getDay()+"");
                Log.d("selected",date.getDay()+"");
            }
        });

        materialCalendarView.setOnTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth ,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.d("date",year +" "+(month+1)+" "+dayOfMonth);
                    }
                }, d.getYear(), d.getMonth(),d.getDay());
                dialog.getDatePicker().setCalendarViewShown(false);
                dialog.show();
            }
        });
        return rootView;


    }
}
