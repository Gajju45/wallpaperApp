package com.android.gajju45.pexelapiApp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuggestedAdapter extends RecyclerView.Adapter<SuggestedAdapter.SugestedVH> {

    ArrayList<SuggestedModel> suggestedModels;
    final private RecyclerViewClickListenerInterface clickListener;
    int row_index=0;
    int selectedPosition=0;




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
        holder.colorContainer.setCardBackgroundColor(suggestedModel.getColor_contain());
        holder.title.setText(suggestedModel.getTitle());

        if(selectedPosition==position) {
             holder.colorContainer.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
             holder.title.setTextColor(Color.parseColor("#000000"));
         }
        else{
            holder.colorContainer.setCardBackgroundColor(suggestedModel.getColor_contain());
            holder.title.setTextColor(Color.parseColor("#FFFFFF"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
                clickListener.onItemClick(position);
            }
        });




    }
    //Random color function
   /*** private int getRandomColor() {
        List<Integer> colorCode=new ArrayList<>();
        colorCode.add(R.color.blue);
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.skyblue);
        colorCode.add(R.color.lightPurple);
        colorCode.add(R.color.lightGreen);
        colorCode.add(R.color.gray);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.greenlight);
        colorCode.add(R.color.notgreen);
        Random randomColor=new Random();
        int number=randomColor.nextInt(colorCode.size());
        return colorCode.get(number);
    }***/

    @Override
    public int getItemCount() {
        return suggestedModels.size();
    }

    public class SugestedVH extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        CardView colorContainer;

        public SugestedVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.suggestedImage);
            title = itemView.findViewById(R.id.sugestedTitle);
            colorContainer = itemView.findViewById(R.id.suggested_card_view);
          /****  itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyDataSetChanged();
                    clickListener.onItemClick(getAdapterPosition());
                }
            });  ****/

        }
    }
}
