package com.mayurkakade.assignment.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;

public class DBHelper extends SQLiteOpenHelper {
    Context context;
    public DBHelper(@Nullable Context context) {
        super(context, ContractClass.DATABASE_NAME, null, ContractClass.DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Queries.CREATE_TABLE_TITLES);
        db.execSQL(Queries.CREATE_TABLE_IMAGES);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Queries.DROP_TABLE_TITLES);
        db.execSQL(Queries.DROP_TABLE_IMAGES);
        onCreate(db);
    }

    public static class DbBitmapUtility {
        // convert from bitmap to byte array
        public static byte[] getBytes(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();
        }
        // convert from byte array to bitmap
        public static Bitmap getImage(byte[] image) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
    }

    public long createTitle(String title, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContractClass.TitlesContract.TITLE, title);
        final long insert = db.insert(ContractClass.TitlesContract.TABLE_TITLE, null, contentValues);
        return insert;
    }

    public Cursor getTitles (SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + ContractClass.TitlesContract.TABLE_TITLE, null);
    }

    public long addImage(Long id, Bitmap imageUri,SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContractClass.ImagesContract.TITLE_ID, id.toString());
        if (imageUri != null) {
            try {
                if (getImageUri(context, imageUri) != null) {
                    String img = getImageUri(context, imageUri).toString();
                    contentValues.put(ContractClass.ImagesContract.IMAGE_URI,img);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("check_statement", "Error : " + e.getLocalizedMessage());
            }
        }
        final long insert = db.insert(ContractClass.ImagesContract.TABLE_TITLE, null, contentValues);
        return insert;
    }

    public Cursor getImages(SQLiteDatabase db, String id) {
        return db.rawQuery("SELECT * FROM " + ContractClass.ImagesContract.TABLE_TITLE + " WHERE " + ContractClass.ImagesContract.TITLE_ID +" = " + id, null);
    }


    static int counter = 0;

    private Uri getImageUri(Context context, Bitmap inImage) throws IOException {
        Uri path = null;
        File f;
        /*if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            return null;
        } else */if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            final ContentValues  contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, Calendar.getInstance().getTime().toString() + " " + counter);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "assignmentSpace");

            final ContentResolver resolver = context.getContentResolver();

            OutputStream stream = null;
            Uri uri = null;

            try
            {
                final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                uri = resolver.insert(contentUri, contentValues);

                if (uri == null)
                {
                    throw new IOException("Failed to create new MediaStore record.");
                }

                stream = resolver.openOutputStream(uri);

                if (stream == null)
                {
                    throw new IOException("Failed to get output stream.");
                }

                if (!inImage.compress(Bitmap.CompressFormat.JPEG, 100, stream))
                {
                    throw new IOException("Failed to save bitmap.");
                }
            }
            catch (IOException e)
            {
                if (uri != null)
                {
                    resolver.delete(uri, null, null);
                }

                e.printStackTrace();
            }
            finally
            {
                if (stream != null)
                {
                    stream.close();
                }
            }
            return uri;
        }
        else
        {
            f = new File(Environment.getExternalStorageDirectory(), "assignmentSpace");
            if (!f.exists()) {
                f.mkdirs();
            }
            File root;
            root = Environment.getExternalStorageDirectory();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File img = new File(root.getAbsolutePath() + "/assignmentSpace/", Calendar.getInstance().getTime().toString()+ " " + counter);
            try {
                FileOutputStream fos = new FileOutputStream(img);
                fos.write(bytes.toByteArray());
                fos.close();
                path = Uri.parse(root.getAbsolutePath() + "/assignmentSpace/" + Calendar.getInstance().getTime().toString()+ " " + counter);
                Log.d("path_debug", "path : " +path);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("path_debug", "error : " + e.getLocalizedMessage());
            }
            Log.d("path_debug", "path : " + path);
            counter++;
            return path;
        }
    }

}
