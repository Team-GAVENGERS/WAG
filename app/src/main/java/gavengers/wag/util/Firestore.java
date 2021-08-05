package gavengers.wag.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import gavengers.wag.util.model.Appointment;

public class Firestore {

    /**
     * Firestore의 Instance를 반환한다
     * @author Taehyun Park
     * @return FirebaseFirestore Instance
     */
    public static FirebaseFirestore getFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }

    /**
     * 새로운 게시물의 정보를 DB에 추가하도록 요청한다.
     * @author Taehyun Park
     * @param newAppo 새 Appointment
     * @return Task<DocumentReference>
     */
    public static Task<DocumentReference> writeNewPost(Appointment newAppo) {
        return getFirestoreInstance().collection("appointment").add(newAppo);
    }


    /**
     * 약속 info를 불러오는 Query를 생성한다
     * @author Taehyun Park
     * @param date ,userId
     * @return Query
     */
    public static Query getInfoDate(String date, String userId) {
        return getFirestoreInstance().collection("appointment").whereEqualTo("date",date).whereEqualTo("writer",userId).limit(10);
    }

}