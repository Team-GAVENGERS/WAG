package gavengers.wag.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth {

    /**
     * FirebaseAuth Instance를 얻어온다
     *
     * @return FirebaseAuth Instance
     * @author Taehyun Park
     */
    public static FirebaseAuth getFirebaseAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    public static FirebaseUser getCurrentUser() {
        return getFirebaseAuthInstance().getCurrentUser();
    }
}