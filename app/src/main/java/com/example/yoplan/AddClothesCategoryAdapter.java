package com.example.yoplan;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AddClothesCategoryAdapter extends RecyclerView.Adapter<AddClothesCategoryAdapter.ItemViewHolder> {
    private static Context context;
    private ArrayList<FileName> mdata = new ArrayList<>();
    @NonNull
    @Override
    public AddClothesCategoryAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clothes_item_list, parent, false);
        context = parent.getContext();

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddClothesCategoryAdapter.ItemViewHolder holder, int position) {
        holder.onBind(mdata.get(position));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void addItem(FileName data){
        mdata.add(data);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        private FileName data;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.ClothesName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ModifyCategoryActivity.class);
                    intent.putExtra("filename", textView.getText());
                    context.startActivity(intent);
                }
            });
        }
        public void onBind(FileName fileName){
            this.data = fileName;
            textView.setText(data.getFilename());
        }
    }
}

class FileName{
    String Filename;

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String filename) {
        Filename = filename;
    }
}
