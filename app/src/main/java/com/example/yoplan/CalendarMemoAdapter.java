package com.example.yoplan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarMemoAdapter extends RecyclerView.Adapter<CalendarMemoAdapter.ViewHolder> {

    private static Context context;
    private ArrayList<CalendarData> mData = new ArrayList<>();


    @NonNull
    @Override
    public CalendarMemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listcalendar_item, parent, false);
        this.context = parent.getContext();

        return new CalendarMemoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarMemoAdapter.ViewHolder holder, int position) {
        holder.onBind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void addItem(CalendarData data){
        boolean checked = false;

        if(mData.size() == 0){
            mData.add(data); //리사이클뷰에 데이터가 없다면 그냥 추가
        }
        else{ //리사이클뷰에 데이터가 있다면
            ArrayList<String> list = new ArrayList<>();
            for(int i = 0; i < mData.size(); i++){
                list.add(mData.get(i).title); //현재 리사이클러뷰에 있는 데이터를 모두 list에 저장
            }

            for(int i = mData.size()-1; i >= 0; i--){
                if(!data.getDate().equals(mData.get(i).getDate())){ // 리사이클뷰와 집어넣을 데이터의 날짜를 비교함
                    mData.remove(i); // 날짜가 다르다면 = 캘린더의 다른날짜를 선택한 것이므로 이전에 존재했던 리사이클뷰의 아이템은 삭제되어야함
                }
            }

            if(mData.size() == 0){
                mData.add(data);

                return;
            }

            for(int i = 0; i < list.size(); i++){
                if(!data.getTitle().equals(list.get(i))){
                    checked = true; //겹치는 데이터가 없다면 true
                }
                else{
                    checked = false; //겹치는 데이터가 한개라도 존재한다면 false
                    break;
                }
            }
            if(checked == true){
                mData.add(data); //겹치는 데이터가 없기 때문에 리사이클뷰에 새롭게 추가해줌
            }
        }
    }

    void deleteItem(int pos){
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void removeDiary(String key){
        SharedPreferences.Editor editor = MainActivity.sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, contents;
        CheckBox checkBox;
        private CalendarData data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.CalendarTitle);
            contents = itemView.findViewById(R.id.CalendarContents);
            checkBox = itemView.findViewById(R.id.CalendarCheckbox);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CheckBox)v).isChecked()){
                        title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        title.setTextColor(Color.GRAY);

                        contents.setPaintFlags(contents.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                        contents.setTextColor(Color.GRAY);
                    }
                    else{
                        title.setPaintFlags(0);
                        title.setTextColor(Color.BLACK);

                        contents.setPaintFlags(0);
                        contents.setTextColor(Color.BLACK);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("삭제하시겠습니까?")
                                .setPositiveButton("제거", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteItem(getAdapterPosition());
                                        removeDiary(MainActivity.key);
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        builder.show();
                    }
                }
            });
            if(checkBox.isChecked()){
                deleteItem(getAdapterPosition());
                removeDiary(MainActivity.key);
            }
        }

        void onBind(CalendarData data){
            this.data = data;
            title.setText(data.getTitle());
            contents.setText(data.getContents());
        }
    }


}
