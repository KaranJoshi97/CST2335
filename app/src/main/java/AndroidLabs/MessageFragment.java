package AndroidLabs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    ChatWindow parent = null;
    public boolean isPhoneorTablet;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final Bundle infoToPass = getArguments(); //returns the arguments set before

        String messagedPassed = infoToPass.getString("Message");
        final long idPassed = infoToPass.getLong("ID");

        View screen = inflater.inflate(R.layout.layout_fragment, container, false);
        TextView message = screen.findViewById(R.id.message);
        TextView id = screen.findViewById(R.id.id);

        message.setText("Message is: " + messagedPassed);
        id.setText("ID = " + idPassed);

        Button deleteButton = (Button) screen.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPhoneorTablet) {
                    parent.deleteMessage(idPassed);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(parent.getFragment());
                    fragmentTransaction.commit();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("ID", idPassed);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish(); // go to previous activity
                }
            }
        });
        return screen;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if(isPhoneorTablet)
            parent = (ChatWindow) context; //find out which activity has the fragment
    }
}