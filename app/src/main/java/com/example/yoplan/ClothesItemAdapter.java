package com.example.yoplan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClothesItemAdapter extends RecyclerView.Adapter<ClothesItemAdapter.ItemViewHolder> {

    private static Context context;
    private ArrayList<ClothesData> mdata = new ArrayList<>();
    private AdapterView.OnItemClickListener mListener = null;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clothes_item_list, parent, false);
        context = parent.getContext();

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(mdata.get(position));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void addItem(ClothesData data){
        mdata.add(data);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        private ClothesData data;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.ClothesName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(v.getContext(), ShowRecommendClothesActivity.class);
                        intent.putExtra("Choose_clothes", textView.getText());
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void onBind(ClothesData clothesData) {
            this.data = clothesData;
            textView.setText(data.getCloteseName());
        }
    }
}
