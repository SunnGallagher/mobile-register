package service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.NotNull;
import model.UnprocessException;
import model.User;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    public static final String COL_NAME = "users";

    private static final String PATTERN = "yyyyMMdd";

    public String registerNewUser(@NotNull User user) throws InterruptedException, ExecutionException, UnprocessException {
        user.setReferenceId(getReferenceId(user));
        user.setMemberType(validateMemberType(user));
        Firestore fireStore = FirestoreClient.getFirestore();
        DocumentReference newDoc = fireStore.collection(COL_NAME).document();
        newDoc.set(user);
        return newDoc.getId();
    }

    private String getReferenceId(@NotNull User user) {
        String dateInString = new SimpleDateFormat(PATTERN, Locale.ENGLISH).format(new Date());
        String subPhone = user.getPhoneNumber().substring(user.getPhoneNumber().length() - 4);
        return dateInString + subPhone;
    }

    private String validateMemberType(@NotNull User user) throws UnprocessException {
        if(user.getSalary() > 50000) {
            return "platinum";
        } else if(user.getSalary() > 30000 && user.getSalary() < 50000) {
            return "gold";
        } else if(user.getSalary() > 15000 && user.getSalary() < 30000) {
            return "silver";
        } else {
            throw new UnprocessException("Can't register with salary below 15,000");
        }
    }

    public User getUserDetails(String Id) throws InterruptedException, ExecutionException {
        Firestore fireStore = FirestoreClient.getFirestore();
        DocumentReference documentReference = fireStore.collection(COL_NAME).document(Id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        User user = null;

        if(document.exists()) {
            user = document.toObject(User.class);
            return user;
        }else {
            return null;
        }
    }

//    public String updatePatientDetails(Patient person) throws InterruptedException, ExecutionException {
//        Firestore dbFirestore = FirestoreClient.getFirestore();
//        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(person.getName()).set(person);
//        return collectionsApiFuture.get().getUpdateTime().toString();
//    }
//
//    public String deletePatient(String name) {
//        Firestore dbFirestore = FirestoreClient.getFirestore();
//        ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(name).delete();
//        return "Document with Patient ID "+name+" has been deleted";
//    }
}
