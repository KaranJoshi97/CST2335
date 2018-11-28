package AndroidLabs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolBar extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "TestToolBar";
    Toast toast;
    FloatingActionButton floatingActionButton;
    private String currentMessage = "You selected item 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tool_bar);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        Toolbar lab8_toolbar = findViewById(R.id.lab8_toolBar);
        setSupportActionBar(lab8_toolbar);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fAButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Message to show", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.action_one:
                Log.d("Toolbar", "Action 1 selected");
                Snackbar.make(findViewById(R.id.lab8_toolBar), currentMessage, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_two:
                Log.d("Toolbar", "Action 2 selected");
                AlertDialog.Builder builder = new AlertDialog.Builder(TestToolBar.this);
                builder.setTitle(R.string.dialogAlert);
                // Add the buttons
                builder.setPositiveButton(R.string.proceedButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Response", "Here is my response");
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.action_three:
                Log.d("Toolbar", "Action 3 selected");
                AlertDialog.Builder builder2 = new AlertDialog.Builder(TestToolBar.this);
                LayoutInflater inflater = TestToolBar.this.getLayoutInflater();
                final View newView = inflater.inflate(R.layout.activity_new_dialog, null);
                builder2.setView(newView);
                builder2.setPositiveButton(R.string.newMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText newMessage = (EditText)newView.findViewById(R.id.new_message);
                        currentMessage = newMessage.getText().toString();
                        Toast.makeText(TestToolBar.this,"Message saved in item 1", Toast.LENGTH_SHORT).show();
                    }
                });
                builder2.setNegativeButton(R.string.goBackButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder2.create().show();
                break;
            case R.id.menuItem:
                Log.d("Toolbar","Menu Selected");
                toast = Toast.makeText(getApplicationContext(), R.string.toastString, Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
        return true;
    }
}
