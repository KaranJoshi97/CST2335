package AndroidLabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    // variables
    public static final String DATABASE_NAME = "Messages.db"; // Database name
    public static final int VERSION_NUM = 8; // Database version
    public static final String TABLE_NAME = "ChatMessagesTable"; // Table name
    public static final  String KEY_ID = "_ID"; // Primary key
    public static final  String KEY_MESSAGE = "Message"; // Column name


    public ChatDatabaseHelper (Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    // This function is creating the table
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " Integer PRIMARY KEY AUTOINCREMENT," + KEY_MESSAGE + " text); ");
        //db.execSQL(TABLE_NAME);
        Log.i("ChatDatabaseHelper", "Calling on Create");
    }

    @Override
    // This function is upgrading the table
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion= " + oldVersion +
                " newVersion= " + newVersion);
    }

}
