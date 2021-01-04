package com.dict.dictionary;

import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataAdapter {

    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public DataAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public DataAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DataAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    //Test function, will probably delete
    public String getTestData(String inputText) {
        try {
            String sql = "SELECT COL_JAPANESE FROM Dictionary where COL_ENGLISH = \""+inputText+";"+"\"";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }else{
                return "null";
            }
            if(mCur.getString(0).length() != 0) {
                return mCur.getString(0);
            }else{
                return "null";
            }
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    //Search the database by english words and returns all the rows that that contain that word
    public Cursor searchEnglish(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        String query = "SELECT kanji,reading,glosses FROM expression, sense where sense.id_expression= expression.id and sense.lang= \"eng\" " +
                "and sense.glosses like \""+"%"+inputText+";"+"%"+"\""+ ";";
        Log.w(TAG, query);
        Cursor mCursor = mDb.rawQuery(query,null);

        if (mCursor != null) {
            mCursor.moveToNext();
        }
        return mCursor;

    }
    //Search the database by japanese words and returns all the rows that that contain that word
    public Cursor searchJPN(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        String query = "SELECT kanji,reading,glosses FROM expression, sense where sense.id_expression= expression.id and sense.lang= \"eng\" " +
                "and (kanji like \""+"%"+inputText+";"+"%"+"\"or reading like \""+"%"+inputText+";"+"%"+"\")"+";";
        Log.w(TAG, query);
        Cursor mCursor = mDb.rawQuery(query,null);

        if (mCursor != null) {
            mCursor.moveToNext();
        }
        return mCursor;

    }



}