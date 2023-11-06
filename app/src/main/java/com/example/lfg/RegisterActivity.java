package com.example.lfg;

import static com.example.lfg.R.id.Amongus;
import static com.example.lfg.R.id.Avengers;
import static com.example.lfg.R.id.FFXIV;
import static com.example.lfg.R.id.Fortnite;
import static com.example.lfg.R.id.LOL;
import static com.example.lfg.R.id.Minecraft;
import static com.example.lfg.R.id.Roblox;
import static com.example.lfg.R.id.TwoFort;
import static com.example.lfg.R.id.email;
import static com.example.lfg.R.id.enterRegistration;
import static com.example.lfg.R.id.gamerTags;
import static com.example.lfg.R.id.name;
import static com.example.lfg.R.id.password;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    // Declarations
    boolean playsAmongus = false;
    boolean playsFFXIV = false;
    boolean playsFortnite = false;
    boolean playsLOL = false;
    boolean playsAvengers = false;
    boolean playsMinecraft = false;
    boolean playsRoblox = false;
    boolean playsTwoFort = false;

    private EditText mPassword, mEmail, mName, mGamerTags;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseUser user;
    private final boolean[] booleanArray = new boolean[8];
    private int amountPlayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializes some firebase functionality, tries to get the current logged in status
        //lets the user create an account if they aren't logged in
        //technically a superfluous check but may be useful if code is refactored later.
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = firebaseAuth -> {
            user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Button mRegister = findViewById(enterRegistration);
        mPassword = findViewById(password);
        mEmail = findViewById(email);
        mName = findViewById(name);
        mGamerTags = findViewById(gamerTags);

        mRegister.setOnClickListener(view -> {
            //takes in email, password, name, trims the ends and passes them to the create account function
            final String email = mEmail.getText().toString().trim();
            final String password = mPassword.getText().toString().trim();
            final String name = mName.getText().toString().trim();
            final String gamerTags = mGamerTags.getText().toString().trim();




            //creates a basic account with the specified email and password
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Please check your information is correct!", Toast.LENGTH_SHORT).show();
                } else {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();

                    if (user != null) {
                        user.updateEmail(email)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                    }
                                });
                    }

                    if (user != null) {
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task12 -> {
                                    if (task12.isSuccessful()) {
                                    }
                                });
                    }
                    booleanArray[0] = playsAmongus;
                    booleanArray[1] = playsFFXIV;
                    booleanArray[2] = playsFortnite;
                    booleanArray[3] = playsLOL;
                    booleanArray[4] = playsAvengers;
                    booleanArray[5] = playsRoblox;
                    booleanArray[6] = playsMinecraft;
                    booleanArray[7] = playsTwoFort;

                    String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                    HashMap<String, Object> userInfo = new HashMap<>();
                    userInfo.put("Name", name);
                    userInfo.put("Email", email);
                    userInfo.put("User ID", userId);
                    userInfo.put("Gamertags", gamerTags);


                    userInfo.put("Among Us", playsAmongus);
                    userInfo.put("Final Fantasy XIV", playsFFXIV);
                    userInfo.put("Fortnite", playsFortnite);
                    userInfo.put("League of Legends", playsLOL);
                    userInfo.put("Marvel's Avengers", playsAvengers);
                    userInfo.put("Roblox", playsRoblox);
                    userInfo.put("Minecraft", playsMinecraft);
                    userInfo.put("Team Fortress 2", playsTwoFort);

                    userInfo.put("Amount of games played", amountPlayed);


                    FirebaseFirestore.getInstance().collection("users")
                            .document("" + userId)
                            .set(userInfo)
                            .addOnSuccessListener(aVoid -> Toast.makeText(RegisterActivity.this, "Registered!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show());

                    ArrayList<String> matches = new ArrayList<>();
                    matches.add("");
                    FirebaseFirestore.getInstance().collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).update("matches", matches);
                }
            });
        });
    }

    // Needed for checkboxes to function as intended.
    @SuppressLint("NonConstantResourceId")
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case Amongus:
                playsAmongus = checked;
                break;
            case FFXIV:
                playsFFXIV = checked;
                break;
            case Fortnite:
                playsFortnite = checked;
                break;
            case LOL:
                playsLOL = checked;
                break;
            case Avengers:
                playsAvengers = checked;
                break;
            case Roblox:
                playsRoblox = checked;
                break;
            case Minecraft:
                playsMinecraft = checked;
                break;
            case TwoFort:
                playsTwoFort = checked;
                break;
        }
        amountPlayed=0;
        for (boolean x : booleanArray
        ) {
            amountPlayed++;

        }

    }


    //Checks for firebase
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

}
