package gavengers.wag.util.model;

import java.util.ArrayList;

public class Appointment {

    private int appointType; // 약속 유형
    private String appointmentName; // 약속 타이틀
    private String startTime;  // 약속 시작 시간
    private String endTime;    // 약속 종료 시간
    private int importance;  // 중요도
    private int place;       // 장소
    private ArrayList<String> participants; // 참여자 리스트 (추후에 )
    private String memoStr;  // 메모 String
    private String writerId; // 작성자
    private String documentId; // 약속 식별 ID

    public Appointment(){ }

    public Appointment(int appointType, String appointmentName, String startTime, String endTime, int importance, int place, ArrayList<String> participants, String memoStr, String writerId, String documentId) {
        this.appointType = appointType;
        this.appointmentName = appointmentName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.importance = importance;
        this.place = place;
        this.participants = participants;
        this.memoStr = memoStr;
        this.writerId = writerId;
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getAppointType() {
        return appointType;
    }

    public void setAppointType(int appointType) {
        this.appointType = appointType;
    }

    public String getAppointmentName() {
        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

    public String getWriterId(){
        return writerId;
    }

    public void setWriterId(String writerId){ this.writerId = writerId; }
}