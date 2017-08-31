package com.example.contentproviderserver;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Description:
 * Author: qiubing
 * Date: 2017-08-28 16:46
 */
public class StudentContentProvider extends ContentProvider{

    private static final String TAG = "StudentContentProvider";
    private static final String AUTHORITY = "com.example.studentprovider";
    private static final String ONE_STUDENT_MIME_TYPE = "vnd.android.cursor.item/one.student";
    private static final String ALL_STUDENT_MIME_TYPE = "vnd.android.cursor.dir/all.student";
    private static UriMatcher sUriMatcher;

    private static final int ONE_STUDENT = 1;
    private static final int ALL_STUDENT = 2;

    private SQLiteDatabase mSQLiteDB;
    private MySQLiteOpenHelper mSQLiteHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY,"student", ALL_STUDENT);
        sUriMatcher.addURI(AUTHORITY,"student/#", ONE_STUDENT);
    }

    @Override
    public boolean onCreate() {
        Log.e(TAG,"onCreate()...");
        mSQLiteHelper = new MySQLiteOpenHelper(getContext());
        mSQLiteDB = mSQLiteHelper.getWritableDatabase();
        return true;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        int code = sUriMatcher.match(uri);
        Log.e(TAG,"delete()...code = " + code + " uri = " + uri);
        int count = 0;
        switch (code){
            case ONE_STUDENT:
                // TODO delete one student data
                long id = ContentUris.parseId(uri);
                String whereClause = "_id" + "=" + id;
                if (!TextUtils.isEmpty(where)){
                    whereClause = whereClause + " and " + where;
                }
                count = mSQLiteDB.delete(MySQLiteOpenHelper.TABLE_NAME,whereClause,whereArgs);
                break;
            case ALL_STUDENT:
                count = mSQLiteDB.delete(MySQLiteOpenHelper.TABLE_NAME,where,whereArgs);
                break;
        }
        notifyDataChanged(uri);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        int code = sUriMatcher.match(uri);
        String retType = null;
        switch (code){
            case ONE_STUDENT:
                retType = ONE_STUDENT_MIME_TYPE;
                break;
            case ALL_STUDENT:
                retType = ALL_STUDENT_MIME_TYPE;
                break;
        }
        Log.e(TAG,"getType()...uri = " + uri + " retType = " + retType);
        return retType;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri retUri = null;
        int code = sUriMatcher.match(uri);
        Log.e(TAG,"insert()...code = " + code + " uri = " + uri + " values = " + values);
        switch (code){
            case ALL_STUDENT:
                long rawId = mSQLiteDB.insert(MySQLiteOpenHelper.TABLE_NAME,null,values);
                retUri = ContentUris.withAppendedId(uri, rawId);
                if (rawId > 0){
                    notifyDataChanged(uri);
                }
                break;
        }
        return retUri;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String where, String[] whereArgs, String sortOrder) {
        int code = sUriMatcher.match(uri);
        Log.e(TAG,"query()...code = " + code + " uri = " + uri);
        Cursor cursor = null;
        switch (code){
            case ONE_STUDENT:
                // TODO query one student data
                long id = ContentUris.parseId(uri);
                String whereCause = "_id" + "=" + id;
                if (!TextUtils.isEmpty(where)){
                    whereCause = whereCause + " and " + where;
                }
                cursor = mSQLiteDB.query(MySQLiteOpenHelper.TABLE_NAME,projection,whereCause,
                        whereArgs,null,null,sortOrder);
                break;
            case ALL_STUDENT:
                cursor = mSQLiteDB.query(MySQLiteOpenHelper.TABLE_NAME, projection, where,
                        whereArgs, null, null, sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        int code = sUriMatcher.match(uri);
        Log.e(TAG,"update()...code = " + code + " uri = " + uri + " values = " + values);
        int count = 0;
        switch (code){
            case ONE_STUDENT:
                long id = ContentUris.parseId(uri);
                String whereCause = "_id"  + "=" + id;
                if (!TextUtils.isEmpty(where)){
                    whereCause = whereCause + " and " + where;
                }
                count = mSQLiteDB.update(MySQLiteOpenHelper.TABLE_NAME,values,whereCause,whereArgs);
                break;
            case ALL_STUDENT:
                count = mSQLiteDB.update(MySQLiteOpenHelper.TABLE_NAME,values,where,whereArgs);
                break;
        }
        notifyDataChanged(uri);
        return count;
    }

    private void notifyDataChanged(Uri uri){
        getContext().getContentResolver().notifyChange(uri,null);
    }
}
