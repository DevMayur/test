package com.mayurkakade.assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mayurkakade.assignment.adapters.MainRecyclerAdapter;
import com.mayurkakade.assignment.database.ContractClass;
import com.mayurkakade.assignment.database.DBHelper;
import com.mayurkakade.assignment.models.MainModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static long selected_title_id = -1;
    FloatingActionButton fab_add;
    RecyclerView recyclerView_main;
    List<MainModel> mainList;
    MainRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askPermissions();
        initViews();
    }

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 107;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 109;

    private void askPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        createDialogToAskPermissions();
                    }
                }
                case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        createDialogToAskPermissions();
                    }
                }
            }
        }

    }

    private void createDialogToAskPermissions() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("Please Enable Storage Permissions for App to work properly !");
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        askPermissions();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void initViews() {
        fab_add = findViewById(R.id.fab_add);
        fab_add.setOnClickListener(this);

        recyclerView_main = findViewById(R.id.items_recycler_view);
        initMainRecyclerView();

    }

    private void initMainRecyclerView() {
        recyclerView_main.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mainList = new ArrayList<>();
        mainList.clear();
        adapter = new MainRecyclerAdapter(mainList,MainActivity.this);
        recyclerView_main.setAdapter(adapter);
        //getData
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = helper.getTitles(sqLiteDatabase);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(ContractClass.TitlesContract.TITLE));
            long id = cursor.getLong(cursor.getColumnIndex(ContractClass.TitlesContract.ID));
            MainModel model = new MainModel(title,id);
            mainList.add(model);
        }
        adapter.notifyDataSetChanged();
    }


    public void createDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_box, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        EditText et_title = dialogView.findViewById(R.id.et_title);
        Button bt_ok = dialogView.findViewById(R.id.bt_ok);
        Button bt_cancel = dialogView.findViewById(R.id.bt_cancel);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTitleToRecyclerView(et_title);
            }

            private void addTitleToRecyclerView(EditText et_title) {
                if (!TextUtils.isEmpty(et_title.getText())) {
                    addTitle(et_title.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this, "Please Enter title and then click Ok", Toast.LENGTH_SHORT).show();
                }
            }
            private void addTitle(String title) {
                DBHelper helper = new DBHelper(MainActivity.this);
                SQLiteDatabase database = helper.getWritableDatabase();
                long id = helper.createTitle(title,database);
                if (id != -1) {
                    mainList.add(new MainModel(title,id));
                    adapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add) {
            createDialog(MainActivity.this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for(int i = 0; i < count; i++) {
                        Uri imgUri = data.getClipData().getItemAt(i).getUri();
                        long title_id = MainActivity.selected_title_id;
                        if (title_id != -1) {
                            DBHelper helper = new DBHelper(MainActivity.this);
                            SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
                                long imageId = helper.addImage(title_id, (bitmap) ,sqLiteDatabase);
                                initMainRecyclerView();
                            } catch (IOException ignored) {

                            }
                        }
                    }
                } else if (data != null) {
                        Uri imgUri = data.getData();
                        long title_id = MainActivity.selected_title_id;
                        if (title_id != -1) {
                            DBHelper helper = new DBHelper(MainActivity.this);
                            SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
                                long imageId = helper.addImage(title_id, (bitmap) ,sqLiteDatabase);
                                initMainRecyclerView();
                            } catch (IOException ignored) {

                            }
                        }
                    }
                }
            }
        }

}