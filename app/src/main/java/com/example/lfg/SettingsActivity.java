package com.example.lfg;

import static com.example.lfg.R.id.Discordtag;
import static com.example.lfg.R.id.FFXIVServer;
import static com.example.lfg.R.id.MinecraftServer;
import static com.example.lfg.R.id.TF2Details;
import static com.example.lfg.R.id.achievement;
import static com.example.lfg.R.id.casual;
import static com.example.lfg.R.id.competitive;
import static com.example.lfg.R.id.coop;
import static com.example.lfg.R.id.customGames;
import static com.example.lfg.R.id.pvefocus;
import static com.example.lfg.R.id.submit;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    // Declarations
    boolean competitivebool = false;
    boolean casualbool = false;
    boolean achievementhunterbool = false;
    boolean pvefocusbool = false;
    boolean coopfocusbool = false;
    private EditText mCustomGames, mDiscordtag, mMinecraftServer, mTF2Details, mFFXIVServer;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseUser user;


  @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_settings);

      //Initializes some firebase functionality, tries to get the current logged in status
      mAuth = FirebaseAuth.getInstance();
      firebaseAuthStateListener = firebaseAuth -> {
          user = FirebaseAuth.getInstance().getCurrentUser();
      };

      Button submitbutton = findViewById(submit);
      String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();


      HashMap<String, Object> userChecks = new HashMap<>();
      HashMap<String, Object> checksHolder = new HashMap<>();

      mCustomGames=findViewById(customGames);
      mDiscordtag=findViewById(Discordtag);
      mMinecraftServer=findViewById(MinecraftServer);
      mTF2Details=findViewById(TF2Details);
      mFFXIVServer=findViewById(FFXIVServer);

      submitbutton.setOnClickListener(view -> {
          final String customGames = mCustomGames.getText().toString().trim();
          final String discordTag = mDiscordtag.getText().toString().trim();
          final String minecraftServer = mMinecraftServer.getText().toString().trim();
          final String tf2Details = mTF2Details.getText().toString().trim();
          final String ffxivServer = mFFXIVServer.getText().toString().trim();

          userChecks.put("Custom Games",customGames);
          userChecks.put("Discord Tag",discordTag);
          userChecks.put("Minecraft Server",minecraftServer);
          userChecks.put("Team Fortress 2 Rank and Class",tf2Details);
          userChecks.put("FFXIV Server and Free Company",ffxivServer);

          userChecks.put("Competitive", competitivebool);
          userChecks.put("Casual", casualbool);
          userChecks.put("Achievement Hunter", achievementhunterbool);
          userChecks.put("PVE Focused", pvefocusbool);
          userChecks.put("Co-op Focused", coopfocusbool);
          checksHolder.put("User Variables",userChecks);
          FirebaseFirestore.getInstance().collection("users")
                  .document("" + userId)
                  .set(checksHolder, SetOptions.merge())
                  .addOnSuccessListener(aVoid -> Toast.makeText(SettingsActivity.this, "Saved!", Toast.LENGTH_SHORT).show())
                  .addOnFailureListener(e -> Toast.makeText(SettingsActivity.this, "Error, please retry!" + e.getMessage(), Toast.LENGTH_SHORT).show());
          Intent intent = new Intent(getApplicationContext(), MainActivity.class);
          startActivity(intent);
      });
  }

    public void logoutUser(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), ChooseLoginRegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    // Needed for checkboxes to function as intended.
    @SuppressLint("NonConstantResourceId")
    public void onCheckbox2Clicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case competitive:
                competitivebool = checked;
                break;
            case casual:
                casualbool = checked;
                break;
            case achievement:
                achievementhunterbool = checked;
                break;
            case pvefocus:
                pvefocusbool = checked;
                break;
            case coop:
                coopfocusbool = checked;
                break;
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