package com.example.contentproviderserver;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private Button mQueryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueryBtn = (Button) findViewById(R.id.query_contacts);
        mQueryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryContactInfo();
            }
        });
    }

    /**
     * 使用系统ContentProvider
     */
    private void queryContactInfo(){
        String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    cols,null,null,null);
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.e(TAG, "queryContactInfo()...name = " + name + " number = " + number);
        }
    }
}
