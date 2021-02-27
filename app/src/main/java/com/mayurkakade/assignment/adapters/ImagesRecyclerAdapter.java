package com.mayurkakade.assignment.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mayurkakade.assignment.R;
import com.mayurkakade.assignment.models.ImageModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ImagesRecyclerAdapter extends RecyclerView.Adapter<ImagesRecyclerAdapter.ViewHolder> {

    Context context;
    List<ImageModel> imageList;

    public ImagesRecyclerAdapter(Context context, List<ImageModel> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Picasso.get().load(imageList.get(position).getImage_uri()).fit().into(holder.imageView);
        } else {
            Picasso.get().load(new File(imageList.get(position).getImage_uri())).fit().into(holder.imageView);
        }
        Log.d("image_paths","path " + position +" :" + imageList.get(position).getImage_uri() );
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_item);
        }
    }
}
