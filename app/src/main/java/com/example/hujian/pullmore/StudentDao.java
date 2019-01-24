package com.example.hujian.pullmore;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * <p>文件描述：<p>
 * <p>作者：hujian<p>
 * <p>创建时间：2018/10/18/018<p>
 * <p>更改时间：2018/10/18/018<p>
 * <p>版本号：1<p>
 */
public class StudentDao {
    private SQLiteDatabase db;
    private Context context;
    final static String CREATE_TABLE="create table stu(" +
            "_id integer primary key autoincrement," +
            "name text," +
            "gender text)";
    final static String DROP_TABLE="drop table stu";

    public StudentDao(Context context) {
        this.db = db;
        this.context=context;
        String path= Environment.getExternalStorageDirectory()+ File.separator;
        Log.e("TAGG", path );
        SQLiteOpenHelper helper=new SQLiteOpenHelper(context,"student.db",null,4) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(CREATE_TABLE);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL(DROP_TABLE);
                db.execSQL(CREATE_TABLE);

            }
        };
        db=helper.getWritableDatabase();
    }
    public Object getStudent(){

        Cursor cursor=db.rawQuery("select * from stu",null);
        if (cursor==null){
            return "is null";
        }
        else {
            return cursor;
        }
    }
    public void addstudent(){
        db.execSQL("insert into stu(name,gender)values('hujian','m')");
        db.execSQL("insert into stu(name,gender)values('hujian2','m')");
        db.execSQL("insert into stu(name,gender)values('hujian3','m')");
        db.execSQL("insert into stu(name,gender)values('hujian4','m')");
        db.execSQL("insert into stu(name,gender)values('hujian5','m')");
        db.execSQL("insert into stu(name,gender)values('hujian6','m')");
        db.execSQL("insert into stu(name,gender)values('hujian7','m')");
        db.execSQL("insert into stu(name,gender)values('hujian8','m')");
        db.execSQL("insert into stu(name,gender)values('hujian9','m')");
        db.execSQL("insert into stu(name,gender)values('hujian10','m')");
        db.execSQL("insert into stu(name,gender)values('hujian11','m')");
        db.execSQL("insert into stu(name,gender)values('hujian12','m')");
        db.execSQL("insert into stu(name,gender)values('hujian13','m')");
    }
}
