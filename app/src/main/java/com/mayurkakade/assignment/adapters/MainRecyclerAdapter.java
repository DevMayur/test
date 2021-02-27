package com.mayurkakade.assignment.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mayurkakade.assignment.MainActivity;
import com.mayurkakade.assignment.R;
import com.mayurkakade.assignment.database.ContractClass;
import com.mayurkakade.assignment.database.DBHelper;
import com.mayurkakade.assignment.models.ImageModel;
import com.mayurkakade.assignment.models.MainModel;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>{

    List<MainModel> mainList;
    Context context;

    public MainRecyclerAdapter(List<MainModel> mainList, Context context) {
        this.mainList = mainList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.base_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_title.setText(mainList.get(position).getTitle());
        holder.iv_select_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImages(position,holder.images_recycler_view);
            }
        });

        setRecylerView(position,holder);

    }

    private void selectImages(int position, RecyclerView images_recycler_view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        MainActivity.selected_title_id = mainList.get(position).getTitle_id();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((MainActivity)context).startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView iv_select_images;
        RecyclerView images_recycler_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            iv_select_images = itemView.findViewById(R.id.iv_select_images);
            images_recycler_view = itemView.findViewById(R.id.images_recycler_view);
        }


    }



    List<ImageModel> iList;
    public static ImagesRecyclerAdapter adapter;

    public void setRecylerView(int position,ViewHolder holder) {

        iList = new ArrayList<>();
        adapter = new ImagesRecyclerAdapter(context, iList);

        //getData
        DBHelper helper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = helper.getImages(sqLiteDatabase, mainList.get(position).getTitle_id().toString());
        while (cursor.moveToNext()) {
            String imageUri = cursor.getString(cursor.getColumnIndex(ContractClass.ImagesContract.IMAGE_URI));
            long id = cursor.getLong(cursor.getColumnIndex(ContractClass.ImagesContract.ID));
            long title_id = cursor.getLong(cursor.getColumnIndex(ContractClass.ImagesContract.TITLE_ID));
            ImageModel model = new ImageModel(id,title_id,imageUri);
            iList.add(model);
        }

        int numberOfrows = 3;

        holder.images_recycler_view.setLayoutManager(new GridLayoutManager(context, numberOfrows ,RecyclerView.HORIZONTAL, false));
        holder.images_recycler_view.setAdapter(adapter);

        holder.images_recycler_view.setHasFixedSize(true);
        holder.images_recycler_view.setItemViewCacheSize(20);
        holder.images_recycler_view.setDrawingCacheEnabled(true);
        holder.images_recycler_view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);



        adapter.notifyDataSetChanged();

    }
}
