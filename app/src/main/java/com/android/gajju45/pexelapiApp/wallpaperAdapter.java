package com.android.gajju45.pexelapiApp;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class wallpaperAdapter extends RecyclerView.Adapter<WallpaperViewHolder> {

    private Context context;
    private List<wallpaperModel> wallpaperModelList;

    public wallpaperAdapter(Context context, List<wallpaperModel> wallpaperModelList) {
        this.context = context;
        this.wallpaperModelList = wallpaperModelList;
    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_item, parent, false);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holders, int position) {
        final WallpaperViewHolder holder = holders;
        wallpaperModel model = wallpaperModelList.get(position);

        holder.photographerName.setText(" " + wallpaperModelList.get(position).getPhotographerName());
        Glide.with(context).load(model.getMediumUrl()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, FullScreenWallpaperActivity.class)
                        .putExtra("originalUrl", model.getOriginalUrl())
                        .putExtra("mediumlUrl", model.getMediumUrl()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return wallpaperModelList.size();
    }


}

class WallpaperViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView photographerName;

    public WallpaperViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imagwiewItem);
        photographerName = itemView.findViewById(R.id.photoGrapherName);
    }
}
