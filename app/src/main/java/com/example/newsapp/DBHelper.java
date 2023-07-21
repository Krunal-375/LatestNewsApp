package com.example.newsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Queue;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_Name = "newsdatabase";
    private static final int DB_Version = 1;
    private static final String Table_Name = "news";
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String LINK = "link";
    public DBHelper(@Nullable Context context) {
        super(context,DB_Name,null,DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+ Table_Name+""+
                "("+ID+" INTEGER  PRIMARY KEY AUTOINCREMENT, "+TITLE+" VARCHAR(255) ,"+LINK+" VARCHAR(255));";
        try {
            db.execSQL(CREATE_TABLE);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS "+Table_Name;
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public long insertData(String title,String link){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE,title);
        contentValues.put(LINK,link);
        return db.insert(Table_Name,null,contentValues);
    }
    public Cursor getCursorForData(){
        String query = "Select * From "+Table_Name;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }
    public String GetData(){
        String query = "Select * From "+Table_Name;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(ID));
            String title = cursor.getString(cursor.getColumnIndex(TITLE));
            String link = cursor.getString(cursor.getColumnIndex(LINK));
            buffer.append(id +" " +title+" "+link);
        }
        return buffer.toString();
    }
    public int deleteUserBasedOnTITLE(String title){
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {title};
        int count = db.delete(Table_Name,TITLE + "=?",args);
        return count;
    }
    public int updateTitle(String oldTitle, String newTitle){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE,newTitle);
        String[] args = {oldTitle};
        int count = db.update(Table_Name,contentValues,TITLE + "=?",args);
        return count;
    }
}
