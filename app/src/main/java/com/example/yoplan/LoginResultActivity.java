package com.example.yoplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

public class LoginResultActivity extends AppCompatActivity {
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_result);

//        Intent intent = getIntent();
//        String nickname = intent.getStringExtra("nickname");
//        String photoUrl = intent.getStringExtra("photoUrl");
//
//        View profile = navigationView.getHeaderView(0);
//        View id = navigationView.getHeaderView(1);
//
//        ImageView iv_profile = (ImageView) profile.findViewById(R.id.iv_profile);
//        Glide.with(this).load(photoUrl).into(iv_profile);
//
//        TextView tv_login_result = (TextView)id.findViewById(R.id.tv_login_result);
//        tv_login_result.setText(nickname);


        startActivity(new Intent(this, MainActivity.class));
    }
}