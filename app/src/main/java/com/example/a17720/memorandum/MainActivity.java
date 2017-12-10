package com.example.a17720.memorandum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;  //显示备忘录文件
    //每页显示的页数
    private static int page_size = 8;
    //初始化页数
    private static int page_no = 1, page_count = 0, count = 0;
    //添加 首页 末页 三个按钮
    private Button btnAdd, btnFirst, btnEnd;
    //前一页 后一页  之后优化为图形化按钮
    private Button btnNext, btnPre;
    //适配器
    private SimpleAdapter simpleAdapter;
    //进度条
    private ProgressBar progressBar;
    private ActivityManager activityManager;

    List<Data> dataList;

    private String TAG = "MyMainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置显示进度条
        setProgressBarVisibility(true);
        setContentView(R.layout.activity_main);

        //实例化activityManager
        activityManager = ActivityManager.getInstance();
        activityManager.AddActivity(this);

        //初始化按钮
        btnAdd = findViewById(R.id.btnAdd);
        btnEnd = findViewById(R.id.btnEnd);
        btnFirst = findViewById(R.id.btnFirst);
        btnNext = findViewById(R.id.btnNext);
        btnPre = findViewById(R.id.btnPre);

        //初始化进度条
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.listview);

        //初始化数据库
        LitePal.getDatabase();

        //listview的事件监听器 进入查看详情界面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Data data = dataList.get(position);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,NoteActivity.class);
                intent.putExtra("noteId",data.getId());
                startActivity(intent);
            }
        });
        //listView的事件监听器，长按编辑
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle(dataList.get(position).getTitle());
                final int noteId = dataList.get(position).getId();
                Log.d(TAG,String.valueOf(noteId));
                adb.setItems(new String[]{"删除", "修改"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                //数据库删除   通过唯一标识来修改
                                DataSupport.delete(Data.class,noteId);
                                //将list中的数据删除   或许没有必要，因为fenye()会更新list
                                dataList.remove(position);
                                Toast.makeText(MainActivity.this, "数据删除成功", Toast.LENGTH_SHORT).show();

                                //刷新页面
                                fenye();
                                break;
                            case 1:
                                //修改 将所选项的id，title，content传递给AddActivity,并启动
                                Intent intent = new Intent();
                                intent.putExtra("noteId",noteId);
                                Log.d(TAG,String.valueOf(noteId));
                                intent.setClass(MainActivity.this, AddNoteActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                    }
                });
                adb.show();
                return true;
            }
        });


        //添加按钮事件监听器
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示进度条      进度条不会消失
//                progressBar.setVisibility(View.VISIBLE);
//                progressBar.setProgress(0);
                //进入添加界面
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });
        //首页按钮事件监听器
        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "首页", Toast.LENGTH_SHORT).show();
            }
        });
        //尾页按钮事件监听器
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "尾页", Toast.LENGTH_SHORT).show();
            }
        });
        //上一页按钮事件监听器
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "上一页", Toast.LENGTH_SHORT).show();
            }
        });
        //下一页按钮事件监听器
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "下一页", Toast.LENGTH_SHORT).show();
            }
        });
        //刷新数据
        fenye();
    }


    //获取数据库中的数据并分页显示     目前只实现简单的获取数据库中的数据，数据简单表示
    public void fenye() {
        //从数据库中获取的数据
        dataList = DataSupport.findAll(Data.class);
//        //获取总页数       之后实现分页功能
//        count = books.size();
//        page_count = count % page_size ==0? count/page_size:count/page_size+1;
//        //到达首页与尾页的情况
//        if (page_no<1)
//            page_no = 1;
//        if (page_no>page_count)
//            page_no = page_count;
//        //未完待定。。。。
//        //设置适配器
//        if (count>0){
//            //适配器

        NoteAdapter adapter = new NoteAdapter(MainActivity.this, R.layout.item_title, dataList);
        listView.setAdapter(adapter);

    }

    @Override//如果用户按下了back键     使用dialog提醒用户是否退出
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
            adb.setTitle("提醒")
                    .setMessage("确定退出？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activityManager.exitAllProgress();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }
}



