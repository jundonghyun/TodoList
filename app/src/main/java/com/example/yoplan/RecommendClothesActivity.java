package com.example.yoplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RecommendClothesActivity extends AppCompatActivity {
    public File loadfolder;
    private ClothesItemAdapter adapter;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_clothes);


        String temperture_String;
        Intent intent = getIntent();
        temperture_String = intent.getStringExtra("Temperture");
        String temp = temperture_String.replaceAll("[^0-9]","");

        int temperture = Integer.parseInt(temp);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser().getEmail().equals("yoplantest@gmail.com")){
            button = findViewById(R.id.add_category);
            button.setVisibility(View.VISIBLE);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RecommendClothesActivity.this, AddClothesCategoryActivity.class));
                }
            });
        }



        loadfolder = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + "clothesFolder");
        File[] checkFile = loadfolder.listFiles();

        if(checkFile == null){
            try {
                makeDir();
                makeClothesFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        init();

        if(temperture >= -10 && temperture < 10){
            String line = null;
            File file = new File(getExternalFilesDir(null) + "/" + "clothesFolder"+"/","5.txt");
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
        else if(temperture >= 10 && temperture < 15){
            String line = null;
            File file = new File(getExternalFilesDir(null) + "/" + "clothesFolder"+"/","10.txt");
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
        else if(temperture >= 15 && temperture < 20){
            String line = null;
            File file = new File(getExternalFilesDir(null) + "/" + "clothesFolder"+"/","15.txt");
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
        else if(temperture >= 20 && temperture < 25){
            String line = null;
            File file = new File(getExternalFilesDir(null) + "/" + "clothesFolder"+"/","20.txt");
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
        else if(temperture >= 25 && temperture < 35){
            String line = null;
            File file = new File(getExternalFilesDir(null) + "/" + "clothesFolder"+"/","25.txt");
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

    //temperuter 불러와야하는데 지금 "현재날씨 temperture"이런식이라 현재날씨를 빼야함
    //빼고나면 현재날씨에 맞춰서 파일을 불러와야함
        
    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.clothes_list_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ClothesItemAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void addItem(String clothes_name){
        ClothesData d = new ClothesData();
        d.setCloteseName(clothes_name);

        adapter.addItem(d);
    }

    public void makeDir() {
        String root = getExternalFilesDir(null).getAbsolutePath(); //내장에 만든다
        String directoryName = "clothesFolder";
        final File myDir = new File(root + "/" + directoryName);
        if (!myDir.exists()) {
            myDir.mkdir();
        } else {
            System.out.println("file: " + root + "/" + directoryName +"already exists");
        }
    }

    private void makeClothesFile() throws IOException {

        File temperture_five = new File(getExternalFilesDir(null) + "/" + "clothesFolder"+"/","5.txt");

        try{
            temperture_five.createNewFile();

            BufferedWriter buffered_five = new BufferedWriter(new FileWriter(temperture_five)); //파일 내용을 불러옴

            ArrayList<String> five = new ArrayList<>();

            five.add("패딩");
            five.add("목도리");
            five.add("장갑");

            for (int i = 0; i < five.size(); i++) {
                buffered_five.append(five.get(i));
                buffered_five.newLine();
            }
            buffered_five.close();

        } catch (IOException e){
            e.printStackTrace();
        }



        File temperture_ten = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + "clothesFolder"+"/","10.txt");
        temperture_ten.createNewFile();
        BufferedWriter buffered_ten = new BufferedWriter(new FileWriter(temperture_ten)); //파일 내용을 불러옴

        File temperture_fifteen = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + "clothesFolder"+"/","15.txt");
        temperture_fifteen.createNewFile();
        BufferedWriter buffered_fifteen = new BufferedWriter(new FileWriter(temperture_fifteen)); //파일 내용을 불러옴

        File temperture_twenty = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + "clothesFolder"+"/","20.txt");
        temperture_twenty.createNewFile();
        BufferedWriter buffered_twenty = new BufferedWriter(new FileWriter(temperture_twenty)); //파일 내용을 불러옴

        File temperture_twentyfive = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + "clothesFolder"+"/","25.txt");
        temperture_twentyfive.createNewFile();
        BufferedWriter buffered_twentyfive = new BufferedWriter(new FileWriter(temperture_twentyfive)); //파일 내용을 불러옴


        ArrayList<String> ten = new ArrayList<>();
        ArrayList<String> fifteen = new ArrayList<>();
        ArrayList<String> twenty = new ArrayList<>();
        ArrayList<String> twentyfive = new ArrayList<>();



        ten.add("코트");
        ten.add("니트");
        ten.add("트렌치코트");

        fifteen.add("가디건");
        fifteen.add("후드티");
        fifteen.add("맨투맨");

        twenty.add("셔츠");
        twenty.add("면바지");
        twenty.add("반팔티");

        twentyfive.add("원피스");
        twentyfive.add("맨소매");
        twentyfive.add("반바지");


        for(int i = 0; i < ten.size(); i++){
            buffered_ten.append(ten.get(i));
            buffered_ten.newLine();
        }
        buffered_ten.close();

        for(int i = 0; i < fifteen.size(); i++){
            buffered_fifteen.append(fifteen.get(i));
            buffered_fifteen.newLine();
        }
        buffered_fifteen.close();

        for(int i = 0; i < twenty.size(); i++){
            buffered_twenty.append(twenty.get(i));
            buffered_twenty.newLine();
        }
        buffered_twenty.close();

        for(int i = 0; i < twentyfive.size(); i++){
            buffered_twentyfive.append(twentyfive.get(i));
            buffered_twentyfive.newLine();
        }
        buffered_twentyfive.close();

    }
}