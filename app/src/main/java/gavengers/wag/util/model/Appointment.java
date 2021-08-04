package gavengers.wag.util.model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Appointment {

    private int appointType; // 약속 유형
    private Date startTime;  // 약속 시작 시간
    private Date endTime;    // 약속 종료 시간
    private int importance;  // 중요도
    private int place;       // 장소
    private ArrayList<String> participants; // 참여자 리스트
    private String memoStr;  // 메모 String

    public Appointment(int appointType, Date startTime, Date endTime, int importance, int place, ArrayList<String> participants, String memoStr) {
        this.appointType = appointType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.importance = importance;
        this.place = place;
        this.participants = participants;
        this.memoStr = memoStr;
    }

    public int getAppointType() {
        return appointType;
    }

    public void setAppointType(int appointType) {
        this.appointType = appointType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public String getMemoStr() {
        return memoStr;
    }

    public void setMemoStr(String memoStr) {
        this.memoStr = memoStr;
    }
}