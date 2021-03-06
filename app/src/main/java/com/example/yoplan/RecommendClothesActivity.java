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
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file)); //?????? ????????? ?????????

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
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file)); //?????? ????????? ?????????

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
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file)); //?????? ????????? ?????????

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
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file)); //?????? ????????? ?????????

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
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file)); //?????? ????????? ?????????

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

    //temperuter ????????????????????? ?????? "???????????? temperture"??????????????? ??????????????? ?????????
    //???????????? ??????????????? ????????? ????????? ???????????????
        
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
        String root = getExternalFilesDir(null).getAbsolutePath(); //????????? ?????????
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

            BufferedWriter buffered_five = new BufferedWriter(new FileWriter(temperture_five)); //?????? ????????? ?????????

            ArrayList<String> five = new ArrayList<>();

            five.add("??????");
            five.add("?????????");
            five.add("??????");

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
        BufferedWriter buffered_ten = new BufferedWriter(new FileWriter(temperture_ten)); //?????? ????????? ?????????

        File temperture_fifteen = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + "clothesFolder"+"/","15.txt");
        temperture_fifteen.createNewFile();
        BufferedWriter buffered_fifteen = new BufferedWriter(new FileWriter(temperture_fifteen)); //?????? ????????? ?????????

        File temperture_twenty = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + "clothesFolder"+"/","20.txt");
        temperture_twenty.createNewFile();
        BufferedWriter buffered_twenty = new BufferedWriter(new FileWriter(temperture_twenty)); //?????? ????????? ?????????

        File temperture_twentyfive = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + "clothesFolder"+"/","25.txt");
        temperture_twentyfive.createNewFile();
        BufferedWriter buffered_twentyfive = new BufferedWriter(new FileWriter(temperture_twentyfive)); //?????? ????????? ?????????


        ArrayList<String> ten = new ArrayList<>();
        ArrayList<String> fifteen = new ArrayList<>();
        ArrayList<String> twenty = new ArrayList<>();
        ArrayList<String> twentyfive = new ArrayList<>();



        ten.add("??????");
        ten.add("??????");
        ten.add("???????????????");

        fifteen.add("?????????");
        fifteen.add("?????????");
        fifteen.add("?????????");

        twenty.add("??????");
        twenty.add("?????????");
        twenty.add("?????????");

        twentyfive.add("?????????");
        twentyfive.add("?????????");
        twentyfive.add("?????????");


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