package org.zhudou.timemanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity {
    private ListView list;
    ArrayList logs = new ArrayList<Map>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=(ListView)findViewById(R.id.list);

        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(getBaseContext().getFilesDir()+"time.db",null);
        String sql = "select count(*) as c from sqlite_master where type ='table' and name ='time_logs' ";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            if (count > 0) {
                //存在
                Cursor cursor_time_logs=db.rawQuery("select * from time_logs order by _id desc",null);
                while (cursor_time_logs.moveToNext()) {
                    int id = cursor_time_logs.getInt(cursor_time_logs.getColumnIndex("_id"));
                    String title = cursor_time_logs.getString(cursor_time_logs.getColumnIndex("title"));
                    String status = cursor_time_logs.getString(cursor_time_logs.getColumnIndex("status"));
                    String begin_time = cursor_time_logs.getString(cursor_time_logs.getColumnIndex("begin_time"));
                    String end_time = cursor_time_logs.getString(cursor_time_logs.getColumnIndex("end_time"));
                    String text = cursor_time_logs.getString(cursor_time_logs.getColumnIndex("text"));

                    Map map = new HashMap();
                    map.put("log_id", id);
                    map.put("title", title);
                    map.put("status", status);
                    map.put("begin_time", begin_time);
                    map.put("end_time", end_time);
                    map.put("text", text);
                    logs.add(map);
                    SimpleAdapter simpleAdapter = new MyAdapter(this, logs,R.layout.log, new String[]{"title"}, new int[]{R.id.title});
                    list.setAdapter(simpleAdapter);
                }
            }else{
                //不存在
                db.execSQL("create table time_logs(_id integer primary key autoincrement,title text,begin_time text,end_time text,status text,text text)");
            }
        }

        cursor.close();
    }
}
