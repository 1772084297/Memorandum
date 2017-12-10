package com.example.a17720.memorandum;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {

    //保存，取消，时间选择器
    private Button btnSave, btnCancel, btnTime;
    //标题，内容
    private EditText etTitle, etContent;
    private ActivityManager activityManager;
    //年月日时分秒，用于保存日历的详细信息
    private int year, month, day, hour, minute, second;
    private Calendar calendar;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    //编辑模式标志
    private boolean EDIT = false;

    private String noteTitle;
    private int noteId;
    private String noteContent;
    private Data data;

    private String TAG = "MyAddNoteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        //将当前activity添加到activity列表中
        activityManager = ActivityManager.getInstance();

        //初始化各个元素
        etTitle = findViewById(R.id.noteTitle1);
        etContent = findViewById(R.id.noteContent1);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        btnTime = findViewById(R.id.noteTime);

        //得到从主活动中传来的noteId跟noteTitle,Add不存在noteId
        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId",0);

        if (noteId!=0) {
            EDIT = true;
        } else
            EDIT = false;

        if (EDIT) { //如果是通过编辑打开,将数据数据显示出来
            Data data = DataSupport.find(Data.class,noteId);
            etTitle.setText(data.getTitle());
            etContent.setText(data.getContent());
        }
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取时间
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
                second = calendar.get(Calendar.SECOND);

                //时间选择器
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String h = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
                        String m = minutes < 10 ? "0" + minutes : minutes + "";
                        String time = "提醒时间  " + h + ":" + m;
                        btnTime.setText(time);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });
        //设置日期选择器  通过长按按钮调用 可能不太合适
        btnTime.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //日期选择器
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                    }
                }, year, month, day);
                datePickerDialog.show();
                return true;
            }
        });

        //保存按钮
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(AddNoteActivity.this);
                //设置标题和信息
                adb.setTitle("保存");
                adb.setMessage("确定要保存么");
                adb.setPositiveButton("保存",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //保存备忘录信息到数据库并关闭
                                saveNote();
                            }
                        });
                adb.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(AddNoteActivity.this, "请注意保存信息", Toast.LENGTH_SHORT).show();
                            }
                        });
                adb.show();
            }
        });

        //取消按钮
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(AddNoteActivity.this);
                //设置标题和消息
                adb.setTitle("提示");
                adb.setMessage("确定不保存么?");
                adb.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                adb.setNegativeButton("取消", null);
                adb.show();
            }
        });

    }

    //设置保存备忘录
    public void saveNote() {
        //取得输入的内容 trim()去掉字符串首尾的空格
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        String time = btnTime.getText().toString().trim();

        //内容与标题都不能为空
        if ("".equals(title) || "".equals(content)) {
            Toast.makeText(AddNoteActivity.this, "标题与内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            //更新数据 到数据库中
            if (EDIT) {
                data = DataSupport.find(Data.class,noteId);
                Data data = new Data();
                data.setTitle(title);
                data.setContent(content);
                data.update(noteId);
            }
            //添加数据 到数据库中
            else {
                Data data = new Data();
                data.setTitle(title);
                data.setContent(content);
                data.save();
                Log.d(TAG,time);
            }
            //根据时间设置闹钟

        }
        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        AddNoteActivity.this.finish();
    }
}
