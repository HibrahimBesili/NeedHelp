package com.example.needhelp;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



    public class DatabaseHelper extends SQLiteOpenHelper {
        MapsActivity mapsActivity;


        public static final String DATABASE_NAME ="help.db";
        public static final String TABLE_NAME ="user";

        public static final String COL_1 ="ID";
        public static final String COL_2 ="username";
        public static final String COL_3 ="password";
        public static final String COL_4 ="phone_number";
        /*public static final String COL_4 ="ID";
        public static final String COL_5 ="username";
        public static final String COL_6 ="problems";
        public static final String COL_7 ="address";
*/
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE user (ID INTEGER PRIMARY  KEY AUTOINCREMENT, username TEXT, password TEXT,phone_number TEXT)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }

        public long addUser(String user, String password,String phone_number){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("username",user);
            contentValues.put("password",password);
            contentValues.put("phone_number",phone_number);
            long res = db.insert("user",null,contentValues);
            db.close();
            return  res;
        }

        public boolean checkUser(String username, String password){
            String[] columns = { COL_1 };
            SQLiteDatabase db = getReadableDatabase();
            String selection = COL_2 + "=?" + " and " + COL_3 + "=?";
            String[] selectionArgs = { username,password };
            Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
            int count = cursor.getCount();
            cursor.close();
            db.close();

            if(count>0)
                return  true;
            else
                return  false;
        }
    }

