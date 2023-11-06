package com.example.lfg;

import android.annotation.SuppressLint;
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

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String name;
    String email;
    String uid;
    private TextView textViewData;

    // These have to be identical to the names of the keys on the firestore!
    // The app will NOT work if they aren't!
    private static final String KEY_Amongus = "Among Us";
    private static final String KEY_FFXIV = "Final Fantasy XIV";
    private static final String KEY_Fortnite = "Fortnite";
    private static final String KEY_LOL = "League of Legends";
    private static final String KEY_Avengers = "Marvel's Avengers";
    private static final String KEY_Roblox = "Roblox";
    private static final String KEY_Minecraft = "Minecraft";
    private static final String KEY_TwoFort = "Team Fortress 2";
    private static final String KEY_EMAIL = "Email";
    private Map<String,Object> stuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textViewData = findViewById(R.id.textViewData);
        textViewData.setMovementMethod(new ScrollingMovementMethod());

        // This retrieves the current user session (basically to get the ID and associated data)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            // Name, email address, and user ID import from firestore
            name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();
        }

        // This is where the firestore data for our user is retrieved from and printed out.
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG ="" ;

            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Grabs the task result as a document Snapshot
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());

                            // Grabs data from the firebase documentSnapshot
                            String emailid = documentSnapshot.getString(KEY_EMAIL);

                            Boolean playsAmongus = documentSnapshot.getBoolean(KEY_Amongus);
                            Boolean playsFFXIV = documentSnapshot.getBoolean(KEY_FFXIV);
                            Boolean playsFortnite = documentSnapshot.getBoolean(KEY_Fortnite);
                            Boolean playsLOL = documentSnapshot.getBoolean(KEY_LOL);
                            Boolean playsAvengers = documentSnapshot.getBoolean(KEY_Avengers);
                            Boolean playsRoblox = documentSnapshot.getBoolean(KEY_Roblox);
                            Boolean playsMinecraft = documentSnapshot.getBoolean(KEY_Minecraft);
                            Boolean playsTwoFort = documentSnapshot.getBoolean(KEY_TwoFort);


                            stuff = (Map) documentSnapshot.get("User Variables");
                            if(stuff!=null){
                                boolean competitivebool=false;
                                competitivebool = (boolean) stuff.get("Competitive");
                                boolean casualbool=false;
                                casualbool = (boolean) stuff.get("Casual");
                                boolean coopbool=false;
                                coopbool = (boolean) stuff.get("Co-op Focused");
                                boolean pvebool=false;
                                pvebool = (boolean) stuff.get("PVE Focused");
                                boolean achievementbool=false;
                                achievementbool = (boolean) stuff.get("Achievement Hunter");
                                String gamertags="";
                                gamertags = (String) documentSnapshot.get("Gamertags");
                                String ffxivserver="";
                                ffxivserver = (String) stuff.get("FFXIV Server and Free Company");
                                String minecraftserver="";
                                minecraftserver = (String) stuff.get("Minecraft Server");
                                String customgames="";
                                customgames = (String) stuff.get("Custom Games");
                                String tf2rankclass="";
                                tf2rankclass = (String) stuff.get("Team Fortress 2 Rank and Class");
                                String discordtags="";
                                discordtags = (String) stuff.get("Discord Tag");
                                textViewData.setText("User ID: " + uid + "\n" + "User Name: " + name +
                                        "\n" + "Email: " + emailid +"\nGamertags: "+gamertags+"\nDiscord Tag: "+discordtags+ "\n\nGames: \n" + "Among Us: " + playsAmongus +
                                        "\n" + "Final Fantasy XIV: " + playsFFXIV + "\n" + "Fortnite: " +
                                        playsFortnite + "\n" + "League of Legends: " + playsLOL + "\n" +
                                        "Marvel's Avengers: " + playsAvengers + "\n" + "Roblox: " + playsRoblox +
                                        "\n" + "Minecraft: " + playsMinecraft + "\n" + "Team Fortress 2: "
                                        + playsTwoFort + "\n\n"+"Competitive: "+competitivebool+
                                        "\nCasual: "+casualbool+"\nCo-op Focused: "+coopbool+
                                        "\nPVE Focused: "+pvebool
                                        +"\nAchievement Hunter: "+achievementbool
                                        +"\n\nAdditional Information:"
                                        +"\nFFXIV Server and Free Company: "+ffxivserver
                                        +"\nMinecraft Server: "+minecraftserver
                                        +"\nTeam Fortress 2 Rank and Class: "+tf2rankclass
                                        +"\nCustom Games: "+customgames

                                );
                            }else{
                                textViewData.setText("User ID: " + uid + "\n" + "User Name: " + name +
                                        "\n" + "Email: " + emailid +"\n\nGames: \n" + "Among Us: " + playsAmongus +
                                        "\n" + "Final Fantasy XIV: " + playsFFXIV + "\n" + "Fortnite: " +
                                        playsFortnite + "\n" + "League of Legends: " + playsLOL + "\n" +
                                        "Marvel's Avengers: " + playsAvengers + "\n" + "Roblox: " + playsRoblox +
                                        "\n" + "Minecraft: " + playsMinecraft + "\n" + "Team Fortress 2: "
                                        + playsTwoFort + "\n\n"
                                );
                            }




                            // Ignore this map for now
                            // Map<String, Object> directHashMap = documentSnapshot.getData();


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

    //Buttons to go to other activities.
    public void goToSettings(View view) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void goToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void logoutUser(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), ChooseLoginRegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToMatches(View view) {
        Intent intent = new Intent(getApplicationContext(), MatchesActivity.class);
        startActivity(intent);
    }
}

