package com.android.gajju45.pexelapiApp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuggestedAdapter extends RecyclerView.Adapter<SuggestedAdapter.SugestedVH> {

    ArrayList<SuggestedModel> suggestedModels;
    final private RecyclerViewClickListenerInterface clickListener;
    int row_index=0;



    public SuggestedAdapter(ArrayList<SuggestedModel> suggestedModels, RecyclerViewClickListenerInterface clickListener) {
        this.suggestedModels = suggestedModels;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public SugestedVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggested_item, parent, false);
        final SugestedVH sugestedVH = new SugestedVH(view);

        return sugestedVH;
    }

    @Override
    public void onBindViewHolder(@NonNull SugestedVH holder, int position) {
        SuggestedModel suggestedModel = suggestedModels.get(position);
        holder.image.setImageResource(suggestedModel.getImage());
        holder.title.setText(suggestedModel.getTitle());

       /* holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
            }
        });
        if (row_index==position) {
            holder.title.setTextColor(Color.RED);
        } else {
            holder.title.setTextColor(Color.BLACK);
        }

        */






    }

    @Override
    public int getItemCount() {
        return suggestedModels.size();
    }

    public class SugestedVH extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;

        public SugestedVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.suggestedImage);
            title = itemView.findViewById(R.id.sugestedTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });

        }
    }
}
