package AndroidLabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static AndroidLabs.ChatDatabaseHelper.KEY_ID;
import static AndroidLabs.ChatDatabaseHelper.TABLE_NAME;

public class ChatWindow extends Activity {

    private static final String ACTIVITY_NAME = "ChatWindow";
    private ListView listView;
    private EditText editText;
    private Button sendButton;
    private ArrayList<String> chatMessagesList;
    private ChatAdapter messageAdapter;

    private Cursor cursor;
    private ContentValues contentValues;
    private ChatDatabaseHelper chatHelper;
    private SQLiteDatabase db;

    // Lab 7
    private View frameLayout;
    private Boolean isPhoneorTablet;
    private MessageFragment messageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.i(ACTIVITY_NAME, "In onCreate()");  //

        editText = (EditText)findViewById(R.id.editText);
        listView = (ListView)findViewById(R.id.chatView);
        sendButton = (Button)findViewById(R.id.sendButton);
        chatMessagesList = new ArrayList<>();

        //in this case, “this” is the ChatWindow, which is-A Context object
        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        // Lab 7
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        isPhoneorTablet = (frameLayout != null);

        // Lab 5
        contentValues = new ContentValues();
        chatHelper = new ChatDatabaseHelper(this);
        db = chatHelper.getWritableDatabase();
        // Lab 5
        cursor = db.rawQuery("SELECT * FROM " + chatHelper.TABLE_NAME, null);

        // Lab 5
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            chatMessagesList.add(cursor.getString(cursor.getColumnIndex(chatHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(chatHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }
        // Lab 5
        Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());
        for(int columnIndex = 0; columnIndex < cursor.getColumnCount(); columnIndex++){
            Log.i(ACTIVITY_NAME, cursor.getColumnName(columnIndex));
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editText.getText().toString();
                chatMessagesList.add(messageText);
                messageAdapter.notifyDataSetChanged();
                // Lab 5
                contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, editText.getText().toString());
                db.insert(ChatDatabaseHelper.TABLE_NAME, null, contentValues);
                cursor = db.rawQuery("SELECT * FROM " + chatHelper.TABLE_NAME, null);
                messageAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Bundle infoToPass  = new Bundle();
                infoToPass.putString("Message", chatMessagesList.get(position));
                infoToPass.putLong("ID", id);
                if(isPhoneorTablet) {
                    messageFragment = new MessageFragment();
                    messageFragment.isPhoneorTablet = true;
                    messageFragment.setArguments(infoToPass);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, messageFragment);
                    fragmentTransaction.addToBackStack("Done.");
                    fragmentTransaction.commit();

                } else {
                    //go to new window:
                    Intent nextPage = new Intent(ChatWindow.this, MessageDetails.class);
                    nextPage.putExtras(infoToPass); //send info
                    startActivityForResult(nextPage, 420);
                }
            }
        });
    }



    public void deleteMessage(long id) {
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(id) });
        cursor = db.rawQuery("SELECT * FROM " + chatHelper.TABLE_NAME, null);

        chatMessagesList.clear();
        while(cursor.moveToNext()){
            chatMessagesList.add(cursor.getString(cursor.getColumnIndex(chatHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(chatHelper.KEY_MESSAGE)));
        }

        messageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == 420 && responseCode == RESULT_OK) {
            deleteMessage(data.getExtras().getLong("ID"));
        }
    }


//    // Steps 2 and 3 for Lab 3
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.i(ACTIVITY_NAME, "In onResume()");
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.i(ACTIVITY_NAME, "In onStart()");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.i(ACTIVITY_NAME, "In onPause()");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.i(ACTIVITY_NAME, "In onStop()");
//    }


    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx){
            super(ctx,0);
        }

        public int getCount() {
            return chatMessagesList.size();
        }

        public String getItem(int position){
            return chatMessagesList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            // This will recreate your View that you made in the resource file.
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position%2 == 0){
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }
            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;
        }

        public long getItemId(int position){
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(KEY_ID));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        //Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    public MessageFragment getFragment() {
        return messageFragment;
    }
}