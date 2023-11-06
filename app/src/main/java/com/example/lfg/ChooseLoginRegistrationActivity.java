package com.example.lfg;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChooseLoginRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Gets the current firebase user instance if it exists
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // if the user account exists, sends the user to the main activity instantly
        // Otherwise, loads the login/registration picker prompt
        super.onCreate(savedInstanceState);
        if (user != null) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            setContentView(R.layout.activity_choose_login_registration);
        }
    }

    // button functions to bring to registration or login screen
    public void goToRegister(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}