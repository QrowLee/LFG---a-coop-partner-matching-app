package com.example.lfg;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    //private int i;
    String name;
    String email;
    String uid;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String completedList = "";
    private TextView textViewMatchesData;
    private DocumentSnapshot otherdocumentSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_matches);
        textViewMatchesData = findViewById(R.id.textViewMatchesData);
        textViewMatchesData.setMovementMethod(new ScrollingMovementMethod());
        // This retrieves the current user session (basically to get the ID and associated data)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            // Name, email address, and user ID import from firestore
            name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();
        }

        // This is where the firestore data for our user is retrieved.
        DocumentReference docRef = db.collection("users").document(uid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Grabs the task result as a document Snapshot
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());
                            ArrayList matches = (ArrayList) documentSnapshot.get("matches");
                            if (matches != null) {
                                for (int i = 0; i < matches.size(); i++) {
                                    String otherUserID = matches.get(i).toString();
                                    //String splitUserID = otherUserID.substring(0,otherUserID.lastIndexOf(" "));
                                    String splitUserID = otherUserID.replaceAll("\\s.*", "");
                                    if (!splitUserID.equals("")) {
                                        List<String> matchedGames = (List<String>) documentSnapshot.get(splitUserID);
                                        long gamesPlayed = (long) documentSnapshot.get("Amount of games played");
                                        //Get another user reference
                                        DocumentReference otherUserDocRef = FirebaseFirestore.getInstance().collection("users").document(splitUserID);
                                        otherUserDocRef.get().addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        otherdocumentSnapshot = task1.getResult();
                                                    }
                                                    String userName = null;
                                                    if (otherdocumentSnapshot != null) {
                                                        userName = otherdocumentSnapshot.getString("Name");
                                                    }
                                                    String gamerTags = null;
                                                    if (otherdocumentSnapshot != null) {
                                                        gamerTags = otherdocumentSnapshot.getString("Gamertags");
                                                    }
                                                    if (otherdocumentSnapshot != null && otherdocumentSnapshot.getString("Gamertags") == null) {
                                                        gamerTags = "No Gamertags Available!";
                                                    }
                                                    if (matchedGames != null) {
                                                        double size = matchedGames.size();
                                                        size = size / gamesPlayed * 100;
                                                        matchedGames.toString();
                                                        completedList = completedList + userName + "\n" + "Similarity Percentage: " + size + "%" + "\nGamertags:\n" + gamerTags + "\n" + "Common Games:\n" + matchedGames + "\n\n";
                                                        completedList = completedList.replace(", ", "\n");
                                                        completedList = completedList.replace("[", "");
                                                        completedList = completedList.replace("]", "");
                                                        textViewMatchesData.setText(completedList);
                                                    } else {
                                                        textViewMatchesData.setText(R.string.nomatchesyet);
                                                    }
                                                }
                                        );
                                    } else {
                                        textViewMatchesData.setText(R.string.nomatchesyet);

                                    }
                                }
                            }
                        } else {

                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    public void goToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}