package com.example.contentproviderdemo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.Random;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final String AUTHORITY = "com.example.studentprovider";
    private static final Uri ALL_STUDENT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/student");
    public static final Uri ONE_STUDENT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/student/#");

    private Button mInsertButton;
    private Button mUpdateButton;
    private Button mQueryButton;
    private Button mDeleteButton;
    private Button mStartActivityButton;
    private EditText mNameText;
    private EditText mAgeText;
    private ListView mListView;
    private View.OnClickListener mListener = new MyClickListener();
    private ContentResolver mContentResolver;
    private ContentObserver mObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentResolver = getContentResolver();
        mObserver = new StudentObserver(new Handler());
        mContentResolver.registerContentObserver(ALL_STUDENT_CONTENT_URI, true, mObserver);
        initViews();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContentResolver.unregisterContentObserver(mObserver);
    }

    private void initViews(){
        mInsertButton = (Button) findViewById(R.id.insert);
        mInsertButton.setOnClickListener(mListener);
        mDeleteButton = (Button) findViewById(R.id.delete);
        mDeleteButton.setOnClickListener(mListener);
        mQueryButton = (Button) findViewById(R.id.query);
        mQueryButton.setOnClickListener(mListener);
        mUpdateButton = (Button) findViewById(R.id.update);
        mUpdateButton.setOnClickListener(mListener);
        mStartActivityButton = (Button) findViewById(R.id.start_activity);
        mStartActivityButton.setOnClickListener(mListener);
        mNameText = (EditText) findViewById(R.id.name_edit);
        mAgeText = (EditText) findViewById(R.id.age_edit);
        mListView = (ListView) findViewById(R.id.student_list);
    }

    private class MyClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.update:
                    update();
                    break;
                case R.id.delete:
                    delete();
                    break;
                case R.id.query:
                    query();
                    break;
                case R.id.insert:
                    insert();
                    break;
                case R.id.start_activity:
                    Intent intent = new Intent();
                    intent.setAction("com.example.provider.secondactivity");
                    intent.setData(ALL_STUDENT_CONTENT_URI);
                    startActivity(intent);
                    break;
            }
        }
    }

    private class StudentObserver extends ContentObserver{
        public StudentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.e(TAG, "onChange()...selfChange = " + selfChange + " uri = " + uri);
            query();
        }
    }

    private void update(){
        ContentValues values = new ContentValues();
        String name = mNameText.getText().toString();
        int age = new Random().nextInt(100);
        values.put("age",age);
        Log.e(TAG, "update()...name = " + name + " age = " + age);
        mContentResolver.update(ALL_STUDENT_CONTENT_URI, values, "name=?", new String[]{name});
    }

    private void insert(){
        String name = mNameText.getText().toString();
        int age = Integer.parseInt(mAgeText.getText().toString());
        Log.e(TAG,"insert()...name = " + name + " age = " + age);
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("age", age);
        mContentResolver.insert(ALL_STUDENT_CONTENT_URI, values);
    }

    private void query(){
        Cursor cursor = mContentResolver.query(ALL_STUDENT_CONTENT_URI, null, null, null, null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.student_list_item,cursor,
                    new String[]{"name","age"},new int[]{R.id.name,R.id.age},0);
        mListView.setAdapter(adapter);
    }

    private void delete(){
        int age = Integer.parseInt(mAgeText.getText().toString());
        Log.e(TAG, "delete()...age > " + age);
        mContentResolver.delete(ALL_STUDENT_CONTENT_URI,"age>?",new String []{String.valueOf(age)});
    }

}
