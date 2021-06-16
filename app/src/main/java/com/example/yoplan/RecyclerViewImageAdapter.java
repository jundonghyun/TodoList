package com.example.yoplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;


import java.util.ArrayList;

public class RecyclerViewImageAdapter extends RecyclerView.Adapter<RecyclerViewImageAdapter.ItemViewHolder> {

    private static Context context;
    private ArrayList<ImageData> mdata = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerViewImageAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_list, parent, false);
        context = parent.getContext();

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewImageAdapter.ItemViewHolder holder, int position) {
        holder.onBind(mdata.get(position));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void addItem(ImageData data){ mdata.add(data); }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        private ImageData data;

        public ItemViewHolder(@NonNull View view) {
            super(view);

            img = view.findViewById(R.id.Recycler_Image_Item);
        }

        void onBind(ImageData imageData) {
            this.data =  imageData;
            Glide.with(itemView.getContext()).load(data.getImageattr()).into(img);
        }
    }
}
