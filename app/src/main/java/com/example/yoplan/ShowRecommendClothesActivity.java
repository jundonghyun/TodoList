package com.example.yoplan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ShowRecommendClothesActivity extends AppCompatActivity {

    private RecyclerViewImageAdapter adapter;
    final Bundle bundle = new Bundle();
    String URL = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recommend_clothes);

        init();

        Intent intent = getIntent();

        String Clothes_name = intent.getStringExtra("Choose_clothes");

        URL = "https://www.giordano.co.kr/shop/search_result.php?search_str="+Clothes_name;




        getData();

        new Thread(){
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(URL).get();

                    Elements elements = doc.select("div.prd_search a img");

                    for(Element elem : elements){
                        String imge = elem.attr("src");

                        addItem(imge);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();





    }

    private void getData(){
        ImageJsoup imageJsoup = new ImageJsoup();
        imageJsoup.execute();
    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.clothes_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewImageAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void addItem(String imgeattr){
        ImageData d = new ImageData();
        d.setImageattr(imgeattr);

        adapter.addItem(d);
    }

    private class ImageJsoup extends AsyncTask<Void, Void, Void> {

        ArrayList<String> imageattr = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... voids) {
            Document doc = null;
            try {
                doc = Jsoup.connect(URL).get();

                Elements elements = doc.select("div.prd_search a img");



                if(elements.size() == 0) {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ShowRecommendClothesActivity.this);
                            builder.setTitle("옷 이미지를 불러올 수 없습니다");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        }
                    });
                }
                else{
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (Element elem : elements) {
                                String imge = elem.attr("src");
                                System.out.println(imge);
                                imageattr.add(imge);
                            }
                            for(int i = 0; i < imageattr.size(); i++){
                                ImageData data = new ImageData();
                                data.setImageattr(imageattr.get(i));

                                adapter.addItem(data);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}