package com.example.yoplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ModifyCategoryActivity extends AppCompatActivity {
    private ModifyCategoryAdapter adapter;
    private Button add_clothes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_category);

        Intent intent = getIntent();
        String filename = intent.getStringExtra("filename");
        File file = new File(getExternalFilesDir(null) + "/" + "clothesFolder"+"/"+filename);

        init();

        add_clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(ModifyCategoryActivity.this);

                dialog.setContentView(R.layout.custorm_dialog_clothes);
                dialog.show();

                final EditText editText = (EditText) dialog.findViewById(R.id.titleclo);
                final Button okbutton = (Button) dialog.findViewById(R.id.okButtonclo);
                final Button cancelbutton = (Button) dialog.findViewById(R.id.cancelButtonclo);

                okbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String line = null;
                        try{
                            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                            ArrayList<String> list = new ArrayList<>();
                            list.add(editText.getText().toString());
                            while((line = bufferedReader.readLine()) != null){
                                list.add(line);
                            }

                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file)); //파일 내용을 불러옴
                            for (int i = 0; i < list.size(); i++) {
                                bufferedWriter.append(list.get(i));
                                bufferedWriter.newLine();
                            }
                            bufferedWriter.close();

                        } catch (IOException e){
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();
                        startActivity(new Intent(ModifyCategoryActivity.this, RecommendClothesActivity.class));
                    }
                });
                cancelbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        String line = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file)); //파일 내용을 불러옴

            while((line = bufferedReader.readLine()) != null){
                ClothesData data = new ClothesData();
                data.setCloteseName(line);

                addItem(line);
            }
            adapter.notifyDataSetChanged();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.modify_category_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ModifyCategoryAdapter();
        recyclerView.setAdapter(adapter);

        add_clothes = findViewById(R.id.add_clothes_button);
    }

    private void addItem(String clothes){
        ClothesName d = new ClothesName();
        d.setClothesName(clothes);

        adapter.addItem(d);
    }
}