package com.example.yoplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AddClothesCategoryActivity extends AppCompatActivity {
    public File loadfolder;
    private AddClothesCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_category);

        loadfolder = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + "clothesFolder");
        File[] list = loadfolder.listFiles();
        init();

        for(File f : list){
            addItem(f.getName());
        }
    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.add_category_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AddClothesCategoryAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void addItem(String filename){
        FileName d = new FileName();
        d.setFilename(filename);

        adapter.addItem(d);
    }
}