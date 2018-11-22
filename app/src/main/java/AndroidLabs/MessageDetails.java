package AndroidLabs;

import android.app.Activity;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class MessageDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        // Get Bundle back
        Bundle infoToPass =  getIntent().getExtras(); //this is bundle from line 65, FragmentExample.java
        MessageFragment fragment = new MessageFragment();
        fragment.isPhoneorTablet = false;
        fragment.setArguments(infoToPass); //give information to bundle

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment); //load a fragment into the framelayout
        fragmentTransaction.addToBackStack("name doesn't matters.");  //changes the back button behaviour
        fragmentTransaction.commit(); //actually load it
    }
}