package gavengers.wag;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private boolean[] isUsed;
    public ViewGroup rootView;
    private int width;
    private int height;

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.plan_layout, container, false);
        getInfoData();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        floatingActionButton = rootView.findViewById(R.id.f_btn_create_appointment);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AppointmentActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void calendarTotal() {
        materialCalendarView = rootView.findViewById(R.id.calendar_view);
        materialCalendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                return day.getYear() + "년 " + (day.getMonth() + 1) + "월";
            }
        });
        CalendarDay today = CalendarDay.today();
        materialCalendarView.setCurrentDate(CalendarDay.from(today.getYear(), today.getMonth(), today.getDay()));
        materialCalendarView.setSelectionColor(Color.parseColor("#969696"));
//        materialCalendarView.setSelectedDate(CalendarDay.from(today.getYear(), today.getMonth(), today.getDay()));
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        materialCalendarView.setArrowColor(Color.parseColor("#f1a53e"));
        materialCalendarView.setTitleAnimationOrientation(MaterialCalendarView.HORIZONTAL);

        materialCalendarView.setTileHeight((int) (height * 0.11)); // 디바이스 전체 높이의 11%만큼 타일의 높이 설정
        /**
         *  ※기본 설정대로 했을 경우
         * 각 날마다의 보여지는 최대 일정 개수는 2개 (이론상 총 3개 {day + 일정들})
         * 각 일정 Title의 최대 길이는 4 +글씨가 짤리면 다음 줄로 넘어감
         * ※ Tile의 높이를 올릴 경우 Title의 길이는 조절 못하더라도 보여지는 개수 조절 가능
         */
        materialCalendarView.setDayFormatter(new DayFormatter() {
            @NonNull
            @Override
            public String format(@NonNull CalendarDay day) {
                /**
                 * 각각의 날을 캘린더에 표시하는 formatting method
                 * 그 날짜의 일정 데이터를 가져오고 조건에 맞게 일정 데이터의 Title을 함께 출력력
                 */
                Integer[] tmp = compareDayIsThere(day); //
                if (tmp.length == 1) { // 일정이 1개일 때
                    return day.getDay() + "\n" + infoList.get(tmp[0]).getAppointmentName();
                } else if (tmp.length > 1) { // 일정이 2개 이상일 때
                    StringBuilder txt = new StringBuilder(day.getDay() + "\n");
                    for (Integer integer : tmp) {
                        txt.append(infoList.get(integer).getAppointmentName()).append("\n");
                    }
                    return txt.toString();
                } else { // 일정이 없을 때
                    return day.getDay() + "";
                }
            }
        });

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Log.d("selected", date.getDay() + "");
            }
        });

        materialCalendarView.setOnTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        materialCalendarView.setCurrentDate(CalendarDay.from(year, month + 1, dayOfMonth));
                    }
                }, today.getYear(), today.getMonth(), today.getDay());
                dialog.getDatePicker().setCalendarViewShown(false);
                dialog.show();
            }
        });
    }

    private void getInfoData() {
        infoList = new ArrayList<>();
        Firestore.getInfo(Auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Appointment info = doc.toObject(Appointment.class);
                            if (info != null) {
                                checkDateRange(info); // 시작 시간 ~ 종료 시간 범위 비교
                                infoList.add(info);
                            } else {
                                Log.d("appoint null", "data is null");
                            }
                        }
                        isUsed = new boolean[infoList.size()];
                        Arrays.fill(isUsed, false);
                    }
                } else {
                    Toast.makeText(getContext(), "getInfoData ERROR !!", Toast.LENGTH_SHORT).show();
                }
                calendarTotal();
            }
        });
    }

    /**
     * parameter 인 info 의 시작 날짜 ~ 종료 날짜 범위를 체크해서 일수 차이가 0이 아니라면
     * infoList 에 시작시간만 다른 새로운 객체를 추가함
     * author SeungGun
     *
     * @param info
     */
    private void checkDateRange(Appointment info) {
        // 시작 시간 제외하고 추가하기
        // 시작 시간은 compareDayIsThere 함수에서 추가하기 때문
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(info.getStartTime());
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(info.getEndTime());
            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();
            startCal.setTime(startDate);
            endCal.setTime(endDate);
            long diffSec = (endCal.getTimeInMillis() - startCal.getTimeInMillis()) / 1000;
            long diffDays = diffSec / (24 * 60 * 60); // 일 차이

            if (diffDays == 0) {
                // 범위날짜가 없다는 의미로 함수 종료
                return;
            }

            for (long i = 0; i < diffDays; ++i) {
                // 반복하기
                startCal.add(Calendar.DATE, 1);
                String date = startCal.get(Calendar.YEAR) + "-" + getMonthsFormatting(startCal.get(Calendar.MONTH)) + "-" + getDaysFormatting(startCal.get(Calendar.DATE));
                infoList.add(new Appointment(info.getAppointType(), info.getAppointmentName(), date,
                        info.getEndTime(), info.getImportance(), info.getPlace(), info.getParticipants(), info.getMemoStr(), info.getWriterId(), info.getDocumentId()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * parameter의 날짜와 일정 데이터 리스트의 날짜가 동일 할때 그 일정 object의 index를 기록해서 반환
     * author SeungGun
     *
     * @param day
     * @return parameter의 그 날짜에서 일정 데이터 리스트에 해당하는 index 정수 배열
     */
    private Integer[] compareDayIsThere(CalendarDay day) {
        ArrayList<Integer> temp = new ArrayList<>();
        String formatted = day.getYear() + "-" + getMonthsFormatting(day.getMonth()) + "-" + getDaysFormatting(day.getDay());
        for (int i = 0; i < infoList.size(); ++i) {
            if (formatted.equals(infoList.get(i).getStartTime().split(" ")[0])) {
                if (!isUsed[i]) {
                    isUsed[i] = true;
                    temp.add(i);
                }
            }
        }
        if (temp.size() != 0) {
            return temp.toArray(new Integer[temp.size()]);
        } else {
            return new Integer[0];
        }
    }

    /**
     * int 로 받아온 parameter month 를 문자열 형식에 맞게 변환해서 리턴하는 함수
     *
     * @param month
     * @return
     */
    public String getMonthsFormatting(int month) {
        return (month + 1) < 10 ? "0" + (month + 1) : (month + 1) + "";
    }

    /**
     * int 로 받아온 parameter day 를 문자열 형식에 맞게 변환해서 리턴하는 함수
     *
     * @param day
     * @return
     */
    public String getDaysFormatting(int day) {
        return day < 10 ? "0" + day : day + "";
    }
}
