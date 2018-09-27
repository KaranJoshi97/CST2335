package AndroidLabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");  // Step 3 for Lab 3

        // Step 4 for Lab 3
        final EditText emailLogin = (EditText)findViewById(R.id.emailLogin);
        final SharedPreferences prefs = getSharedPreferences("Email Address", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("DefaultEmail", "email@domain.com");
        emailLogin.setText(emailAddress);

        // Adding the button
        Button buttonLogin = (Button)findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userEmail = emailLogin.getText().toString();
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("DefaultEmail", userEmail);
                edit.commit(); //write to disk

                Intent nextScreen = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(nextScreen);
            }
        });
    }

    // Steps 2 and 3 for Lab 3
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

}
