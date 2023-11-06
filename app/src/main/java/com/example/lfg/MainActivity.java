package com.example.lfg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

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
    //private int i;
    String name;
    String email;
    String uid;
    //private static final String KEY_EMAIL = "Email";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> removedAL;
    private ArrayList<String> matches;
    private ArrayList<String> matchedGames;
    private Boolean playsAmongus = false;
    private Boolean playsFFXIV = false;
    private Boolean playsFortnite = false;
    private Boolean playsLOL = false;
    private Boolean playsAvengers = false;
    private Boolean playsRoblox = false;
    private Boolean playsMinecraft = false;
    private Boolean playsTwoFort = false;
    private String bothAmongus = "";
    private String bothFFXIV = "";
    private String bothFortnite = "";
    private String bothLOL = "";
    private String bothAvengers = "";
    private String bothRoblox = "";
    private String bothMinecraft = "";
    private String bothTwoFort = "";
    private Boolean otherUserplaysAmongus = false;
    private Boolean otherUserplaysFFXIV = false;
    private Boolean otherUserplaysFortnite = false;
    private Boolean otherUserplaysLOL = false;
    private Boolean otherUserplaysAvengers = false;
    private Boolean otherUserplaysRoblox = false;
    private Boolean otherUserplaysMinecraft = false;
    private Boolean otherUserplaysTwoFort = false;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compareForMatches();

        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        // This retrieves the current user session (basically to get the ID and associated data)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            // Name, email address, and user ID import from firestore
            name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();
        }
        //Initialize matches arraylist
        al = new ArrayList<>();
        removedAL = new ArrayList<>();
        //matches = new ArrayList<>();

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

                            // Grabs data from the firebase documentSnapshot
                            //String emailid = documentSnapshot.getString(KEY_EMAIL);

                            playsAmongus = documentSnapshot.getBoolean(KEY_Amongus);

                            playsFFXIV = documentSnapshot.getBoolean(KEY_FFXIV);

                            playsFortnite = documentSnapshot.getBoolean(KEY_Fortnite);

                            playsLOL = documentSnapshot.getBoolean(KEY_LOL);

                            playsAvengers = documentSnapshot.getBoolean(KEY_Avengers);

                            playsRoblox = documentSnapshot.getBoolean(KEY_Roblox);

                            playsMinecraft = documentSnapshot.getBoolean(KEY_Minecraft);

                            playsTwoFort = documentSnapshot.getBoolean(KEY_TwoFort);

                            // Ignore this map for now
                            // Map<String, Object> directHashMap = documentSnapshot.getData();
                            ///textViewData.setText("User ID: " + uid + "\n" + "User Name: " + name + "\n" + "Email: " + emailid + "\n" + "Among Us: " + playsAmongus + "\n" + "Final Fantasy XIV: " + playsFFXIV + "\n" + "Fortnite: " + playsFortnite + "\n" + "League of Legends: " + playsLOL + "\n" + "Marvel's Avengers: " + playsAvengers + "\n" + "Roblox: " + playsRoblox + "\n" + "Minecraft: " + playsMinecraft + "\n" + "Team Fortress 2: " + playsTwoFort);

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        //Makes fake matches for now will be replaced and improved
        al.add("Try swiping!");

        //introduces swipe cards
        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al);
        SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            ///Function for left card exit
            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                ///Toast.makeText(MainActivity.this, "Rejected!", Toast.LENGTH_SHORT).show();
                removedAL.add(dataObject.toString());

            }

            ///Function for right card exit
            @Override
            public void onRightCardExit(Object dataObject) {
                ///Toast.makeText(MainActivity.this, "Accepted!", Toast.LENGTH_SHORT).show();
                if (!dataObject.toString().equals("Try swiping!")) {
                    if (!dataObject.toString().equals("There's no one around to help.")) {
                        ///String splitUserID = otherUserID.replaceAll("\\s.*", "");

                        if (!matches.contains(dataObject.toString().replaceAll("\\s.*", ""))) {
                            matches.add(dataObject.toString().replaceAll("\\s.*", ""));
                            ///Toast.makeText(MainActivity.this, dataObject.toString().replaceAll("\\s.*", ""), Toast.LENGTH_SHORT).show();
                            FirebaseFirestore.getInstance().collection("users").document(uid).update("matches", matches);
                        }
                    }
                }


            }


            ///Function called when there are no more swipe cards (generates new ones)
            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                if (al.size() <= 1) {
                    compareForMatches();
                }
                al.add("There's no one around to help.");
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                //i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });
    }


    public void goToSettings(View view) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }


    public void compareForMatches() {

        //asynchronously retrieve all documents
        FirebaseFirestore.getInstance()
                .collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        List<DocumentSnapshot> myListOfDocuments = Objects.requireNonNull(task.getResult()).getDocuments();

                        // This is where the firestore data for our user is retrieved.
                        DocumentReference docRef = db.collection("users").document(uid);
                        docRef.get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                // Grabs the task result as a document Snapshot
                                DocumentSnapshot documentSnapshot = task1.getResult();
                                if (documentSnapshot != null) {
                                    if (documentSnapshot.exists()) {
                                        for (int i = 0; i < myListOfDocuments.size(); i++) {
                                            matchedGames = new ArrayList<>();
                                            matches = (ArrayList) documentSnapshot.get("matches");
                                            List<String> matches = (List<String>) documentSnapshot.get("matches");

                                            DocumentSnapshot otherUser = myListOfDocuments.get(i);
                                            String otherUserID = otherUser.getId();
                                            otherUserplaysAmongus = otherUser.getBoolean(KEY_Amongus);
                                            otherUserplaysFFXIV = otherUser.getBoolean(KEY_FFXIV);
                                            otherUserplaysFortnite = otherUser.getBoolean(KEY_Fortnite);
                                            otherUserplaysLOL = otherUser.getBoolean(KEY_LOL);
                                            otherUserplaysAvengers = otherUser.getBoolean(KEY_Avengers);
                                            otherUserplaysRoblox = otherUser.getBoolean(KEY_Roblox);
                                            otherUserplaysMinecraft = otherUser.getBoolean(KEY_Minecraft);
                                            otherUserplaysTwoFort = otherUser.getBoolean(KEY_TwoFort);
                                            if (!otherUserID.equals(uid)) {
                                                ///Toast.makeText(MainActivity.this, "You matched with "+otherUser.getString("Name")+"!", Toast.LENGTH_SHORT).show();
                                                if (otherUserplaysAmongus) {
                                                    if (playsAmongus) {
                                                        if (removedAL.contains(otherUser.getString("User ID") + "\n\n" + matchedGames.toString())) {
                                                            ///Toast.makeText(MainActivity.this, "You didn't match with "+otherUser.getString("Name")+"!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            bothAmongus = "Among Us";
                                                            matchedGames.add(bothAmongus);
                                                        }
                                                    }
                                                }
                                                if (otherUserplaysFFXIV) {
                                                    if (playsFFXIV) {
                                                        if (removedAL.contains(otherUser.getString("User ID") + "\n\n" + matchedGames.toString())) {
                                                            ///Toast.makeText(MainActivity.this, "You didn't match with "+otherUser.getString("Name")+"!", Toast.LENGTH_SHORT).show();

                                                        } else {
                                                            bothFFXIV = "Final Fantasy XIV";
                                                            matchedGames.add(bothFFXIV);
                                                        }
                                                    }
                                                }
                                                if (otherUserplaysFortnite) {
                                                    if (playsFortnite) {
                                                        if (removedAL.contains(otherUser.getString("User ID") + "\n\n" + matchedGames.toString())) {
                                                            ///Toast.makeText(MainActivity.this, "You didn't match with "+otherUser.getString("Name")+"!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            bothFortnite = "Fortnite";
                                                            matchedGames.add(bothFortnite);
                                                        }
                                                    }
                                                }
                                                if (otherUserplaysLOL) {
                                                    if (playsLOL) {
                                                        if (removedAL.contains(otherUser.getString("User ID") + "\n\n" + matchedGames.toString())) {
                                                            ///Toast.makeText(MainActivity.this, "You didn't match with "+otherUser.getString("Name")+"!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            bothLOL = "League of Legends";
                                                            matchedGames.add(bothLOL);
                                                        }
                                                    }
                                                }
                                                if (otherUserplaysAvengers) {
                                                    if (playsAvengers) {
                                                        if (removedAL.contains(otherUser.getString("User ID") + "\n\n" + matchedGames.toString())) {
                                                            ///Toast.makeText(MainActivity.this, "You didn't match with "+otherUser.getString("Name")+"!", Toast.LENGTH_SHORT).show();


                                                        } else {
                                                            bothAvengers = "Marvel's Avengers";
                                                            matchedGames.add(bothAvengers);
                                                        }
                                                    }
                                                }
                                                if (otherUserplaysRoblox) {
                                                    if (playsRoblox) {
                                                        if (removedAL.contains(otherUser.getString("User ID") + "\n\n" + matchedGames.toString())) {
                                                            ///Toast.makeText(MainActivity.this, "You didn't match with "+otherUser.getString("Name")+"!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            bothRoblox = "Roblox";
                                                            matchedGames.add(bothRoblox);
                                                        }
                                                    }
                                                }
                                                if (otherUserplaysMinecraft) {
                                                    if (playsMinecraft) {
                                                        if (removedAL.contains(otherUser.getString("User ID") + "\n\n" + matchedGames.toString())) {
                                                            ///Toast.makeText(MainActivity.this, "You didn't match with "+otherUser.getString("Name")+"!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            bothMinecraft = "Minecraft";
                                                            matchedGames.add(bothMinecraft);
                                                        }
                                                    }
                                                }
                                                if (otherUserplaysTwoFort) {
                                                    if (playsTwoFort) {
                                                        if (removedAL.contains(otherUser.getString("User ID") + "\n\n" + matchedGames.toString())) {
                                                            ///Toast.makeText(MainActivity.this, "You didn't match with "+otherUser.getString("Name")+"!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            bothTwoFort = "Team Fortress 2";
                                                            matchedGames.add(bothTwoFort);
                                                        }
                                                    }
                                                }


                                                if (!al.contains(otherUser.getString("User ID"))) {
                                                    String tempholder = otherUser.getString("User ID") + " \n\n\n\n\n\n\n" + otherUser.getString("Name") + "\n\n\n\n\n\n\n\n" + matchedGames.toString() + "\n\n\n\n\n\n\n\n\n\n\n\n";

                                                    if (!al.contains(tempholder)) {
                                                        if (matches != null && !matches.contains(otherUser.getString("User ID"))) {
                                                            if (matchedGames.size() > 1) {
                                                                al.add(tempholder);

                                                                FirebaseFirestore.getInstance().collection("users").document(uid).update(otherUserID, matchedGames);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                });
    }


}
