package AndroidLabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "In onCreate()"); // Step 3 for Lab 3
        // Log.i(ACTIVITY_NAME, "User clicked Start Chat");

        // Step 6 for Lab 3
        Button mainButton = (Button) findViewById(R.id.button);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(nextScreen, 50);
            }
        });

        Button startChat = (Button) findViewById(R.id.button2);
        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                Intent nextScreen = new Intent(StartActivity.this, ChatWindow.class);
                startActivityForResult(nextScreen, 50);
            }
        });

        Button weatherChat = (Button) findViewById(R.id.button3);
        weatherChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "Weather Forecast");
                Intent nextScreen = new Intent(StartActivity.this, WeatherForecast.class);
                startActivityForResult(nextScreen, 50);
            }
        });


    }

    // Step 6 for Lab 3
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {

        // Step 11 for Lab 3
        if (requestCode == 50 && responseCode == Activity.RESULT_OK) {
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
            String messagePassed = data.getStringExtra("Response");
            Toast toast = Toast.makeText(this, messagePassed, Toast.LENGTH_LONG);
            toast.show();
        }
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