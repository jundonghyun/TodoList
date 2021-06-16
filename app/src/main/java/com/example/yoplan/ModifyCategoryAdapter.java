package com.example.yoplan;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ModifyCategoryAdapter extends RecyclerView.Adapter<ModifyCategoryAdapter.ItemViewHolder> {
    private static Context context;
    private ArrayList<ClothesName> mdata = new ArrayList<>();

    @NonNull
    @Override
    public ModifyCategoryAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clothes_item_list, parent,false);
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

    public void addItem(ClothesName data){
        mdata.add(data);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        private ClothesName data;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.ClothesName);
        }

        public void onBind(ClothesName clothesName){
            this.data = clothesName;
            textView.setText(data.getClothesName());
        }
    }
}

class ClothesName{
    String ClothesName;

    public String getClothesName() {
        return ClothesName;
    }

    public void setClothesName(String clothesName) {
        ClothesName = clothesName;
    }
}
