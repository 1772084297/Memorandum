package com.example.a17720.memorandum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

//展示标题与内容界面
//可以进行界面优化

public class NoteActivity extends AppCompatActivity {

    private TextView tvTitle,tvContent;

    private String TAG = "MyNoteActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
//        初始化textview
        tvTitle = findViewById(R.id.tvTitle1);
        tvContent =findViewById(R.id.tvContent1);

        Intent intent = getIntent();
        int noteId =  intent.getIntExtra("noteId",0);
        Data data = DataSupport.find(Data.class,noteId);
        Log.d(TAG,data.getTitle());
        tvTitle.setText(data.getTitle());
        tvContent.setText(data.getContent());
    }
}
