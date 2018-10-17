package AndroidLabs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

public class ChatWindow extends Activity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    protected ListView listView;
    protected EditText editText;
    protected Button sendButton;
    protected ArrayList<String> chatMessagesList;
    protected ChatAdapter messageAdapter;

    protected ContentValues contentValues;
    protected ChatDatabaseHelper chatHelper;
    protected SQLiteDatabase db;

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
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        // Lab 5
        contentValues = new ContentValues();
        chatHelper = new ChatDatabaseHelper(this);
        db = chatHelper.getWritableDatabase();

        Cursor cursor = db.query(false, chatHelper.TABLE_NAME, new String[]{chatHelper.KEY_ID, chatHelper.KEY_MESSAGE},
                null,null,null,null,null,null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            chatMessagesList.add(cursor.getString(cursor.getColumnIndex(chatHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(chatHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }

        Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());
        for(int columnIndex = 0; columnIndex < cursor.getColumnCount(); columnIndex++){
            Log.i(ACTIVITY_NAME, cursor.getColumnName(columnIndex));
        }


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editText.getText().toString();
                chatMessagesList.add(messageText);
                // Lab 5
                contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, editText.getText().toString());
                db.insert(ChatDatabaseHelper.TABLE_NAME, null, contentValues);
                messageAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });
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

//    protected void onSendClick(View view){
//        messageAdapter.notifyDataSetChanged();
//        editText.setText("");
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
            return position;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        //Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}