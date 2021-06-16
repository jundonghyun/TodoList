package com.example.yoplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    public String titletemp = null;
    public String contentstemp = null;
    public CalendarView calendarView;
    public static TextView showtemperture;
    public Button save_Btn;
    public TextView diaryTextView;
    public RecyclerView recyclerView;
    public static String key = null;
    public static SharedPreferences sp;
    public Gson gson;
    public String contact_memo;
    public String s = null;
    private long backKeyPressedTime = 0;

    private CalendarMemoAdapter adapter;
    private TextView tv_login_result;
    private ImageView iv_profile;
    private Button btn;
    private DrawerLayout drawerLayout;
    private Context context;
    private String tyear, tmonth, tday;
    private Location location;
    private double Latitude, Longitude;
    public static final int MULTIPLE_PERMISSIONS = 1801;
    private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};


    private static final float RE = 6371.00877f; // 지구 반경(km)
    private static final float GRID = 5.0f; // 격자 간격(km)
    private static final float SLAT1 = 30.0f; // 투영 위도1(degree)
    private static final float SLAT2 = 60.0f; // 투영 위도2(degree)
    private static final float OLON = 126.0f; // 기준점 경도(degree)
    private static final float OLAT = 38.0f; // 기준점 위도(degree)
    private static final float XO = 43f; // 기준점 X좌표(GRID)
    private static final float YO = 136f; // 기1준점 Y좌표(GRID)
    private static Bundle bundle = new Bundle();
    private static HashMap convert_Geo = new HashMap<>();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast toast = null;
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true); //1단계 태스크 백그라운드로 이동
            GoogleSignIn.getClient(MainActivity.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                    .signOut();


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask(); // 2단계 액티비티종료 + 태스크 리스트에서 지우기
            }
            Process.killProcess(Process.myPid()); // 3단계 앱 프로세스 종료
            toast.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        calendarView = findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = findViewById(R.id.save_Btn);
        recyclerView = findViewById(R.id.memoRecyclerview);
        showtemperture = findViewById(R.id.showTemperture);
        btn = findViewById(R.id.clo_btn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Home_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        drawerLayout = findViewById(R.id.home_drawer);

        NavigationView navigationView = findViewById(R.id.home_navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {

                menuitem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuitem.getItemId();
                String title = menuitem.getTitle().toString();

                if(id == R.id.login){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                if(id == R.id.log_out){
                    GoogleSignIn.getClient(MainActivity.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                            .signOut();

                    Toast.makeText(MainActivity.this, "로그아웃되었습니다", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        View view = navigationView.getHeaderView(0);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        ImageView iv_profile =(ImageView)view.findViewById(R.id.iv_profile);
        TextView tv_login_result = (TextView)view.findViewById(R.id.tv_login_result);

        if(firebaseAuth.getCurrentUser() != null){
            Glide.with(this).load(firebaseAuth.getCurrentUser().getPhotoUrl()).into(iv_profile);
            tv_login_result.setText(firebaseAuth.getCurrentUser().getEmail());
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String s = (String) showtemperture.getText();
                if(showtemperture.getText().equals("") || showtemperture.getText().equals("null°C")){
                    Snackbar.make(v, "날씨를 불러오는 중입니다 잠시만 기다려주세요",Snackbar.LENGTH_SHORT).show();
                }
                else{
                    if(auth.getCurrentUser() != null){
                        Intent intent = new Intent(MainActivity.this, RecommendClothesActivity.class);
                        intent.putExtra("Temperture", showtemperture.getText());
                        startActivity(intent);
                    }
                    else{
                        Snackbar.make(v, "로그인이 필요합니다",Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });



        sp = getSharedPreferences("shared", MODE_PRIVATE);
        gson = new GsonBuilder().create();

        checkPermissions();

        //UpdeateLocation();


        convert_Geo = LatLngToXY((float)Latitude, (float)Longitude);



        try {
            GetTemperture();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        init();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int m = month + 1;
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
                tyear = String.valueOf(year);
                tmonth = String.valueOf(month+1);
                tday = String.valueOf(dayOfMonth);

                s = String.format("%d/%d/%d", year, m, dayOfMonth);
                key = diaryTextView.getText().toString();
                contact_memo = sp.getString(key, "");

                try {
                    checkfile(tyear, tmonth, tday);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog log = new Dialog(MainActivity.this);

                log.setContentView(R.layout.custorm_dialog);

                log.show();

                final EditText edittitle = (EditText) log.findViewById(R.id.edttitle);
                final EditText editcontents = (EditText) log.findViewById(R.id.edtcontents);
                final Button okbutton = (Button) log.findViewById(R.id.okButton);
                final Button cancelbutton = (Button) log.findViewById(R.id.cancelButton);

                okbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveDiary(edittitle.getText().toString(), editcontents.getText().toString());
                        titletemp = edittitle.getText().toString();
                        contentstemp = editcontents.getText().toString();
                        save_Btn.setVisibility(View.INVISIBLE);

                        try {
                            makefile(tyear, tmonth, tday, titletemp, contentstemp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        log.dismiss();
                    }
                });

                cancelbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        log.dismiss();
                        Toast.makeText(MainActivity.this, "취소하였습니다", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private static void GetTemperture() throws IOException, JSONException {
        URL url = null;
        String line = null;

        long now = System.currentTimeMillis();
        Date mdate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");
        String getDate = simpleDate.format(mdate);
        String currentTime = getTime();

        if(currentTime == "0200"){
            int temp = Integer.parseInt(getDate);
            temp = temp - 1;
            getDate = String.valueOf(temp);

            currentTime = "2300";
        }


        Handler handler = new Handler(){
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle bundle = msg.getData();
                String line = bundle.getString("line");
                String temperture = null;

                try {

                    JSONObject jsonObject = new JSONObject(line);
                    JSONObject response = (JSONObject) jsonObject.get("response");
                    JSONObject headerObj = (JSONObject) response.get("body");
                    JSONObject bodyobj = (JSONObject) headerObj.get("items");

                    JSONArray jArray = bodyobj.getJSONArray("item");


                    for(int i = 0; i < jArray.length(); i++){
                        JSONObject obj = jArray.getJSONObject(i);
                        Log.d("TAG", String.valueOf(obj));

                        if(obj.getString("category").equals("T3H")){
                            temperture = obj.getString("fcstValue");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(temperture);
                showtemperture.setText(temperture+"°C");
            }
        };


        String finalGetDate = getDate;
        String finalCurrentTime = currentTime;
        new Thread(){
            @Override
            public void run() {

                URL url = null;
                String line = null;

                String lat = String.valueOf(convert_Geo.get("nx"));
                String longi = String.valueOf(convert_Geo.get("ny"));

                System.out.println(lat);
                System.out.println(longi);

                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"); /*URL*/
                try {
                    urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=xAF6cET%2B%2B8jrdqpELj%2BaeeLc7prCIWVogTvRh8WOm2mimqH7pVYWoVX%2FpTmfksQx0bTtLLCfsGlTnGsDkihVnw%3D%3D"); /*Service Key*/
                    urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("-", "UTF-8")); /*공공데이터포털에서 받은 인증키*/
                    urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
                    urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
                    urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
                    urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(finalGetDate, "UTF-8")); /*15년 12월 1일발표*/
                    urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(finalCurrentTime, "UTF-8")); /*05시 발표*/
                    urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("91", "UTF-8")); /*예보지점 X 좌표값*/
                    urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("90", "UTF-8")); /*예보지점의 Y 좌표값*/
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println(urlBuilder);
                try {
                    url = new URL(urlBuilder.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.setRequestProperty("Content-type", "application/json");

                BufferedReader rd = null;
                try {
                    if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    } else {
                        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                StringBuilder sb = new StringBuilder();
                while (true) {
                    try {
                        if (!((line = rd.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sb.append(line);
                    System.out.println(sb);
                }
                try {
                    rd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                line = sb.toString();
                conn.disconnect();

                bundle.putString("line", line);
                Message msg = handler.obtainMessage();
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }.start();
    }


    private static String getTime(){
        String[] Base_time = {"0200","0500","0800","1100","1400","1700","2000","2300"};
        String time = "";
        Calendar cal = Calendar.getInstance();

        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if(hour < 5){
             time = "0200";
        }
        else if(hour <= 8){
            time = "0500";
        }
        else if(hour <= 11){
            time = "0800";
        }
        else if(hour <= 14){
            time = "1100";
        }
        else if(hour <= 17){
            time = "1400";
        }
        else if(hour <= 20) {
            time = "1700";
        }
        else if(hour <= 23){
            time = "2000";
        }
        else{
            time = "2300";
        }

        return time;
    }

    private static HashMap LatLngToXY(float lat, float lng){
        float DEGRAD = ((float)Math.PI) / 180.0f;
        float RADDEG = 180.0f / ((float)Math.PI);

        float re = RE / GRID;
        float slat1 = SLAT1 * DEGRAD;
        float slat2 = SLAT2 * DEGRAD;
        float olon = OLON * DEGRAD;
        float olat = OLAT * DEGRAD;

        float sn = (float)Math.tan(((float)Math.PI) * 0.25 + slat2 * 0.5) / (float)Math.tan(((float)Math.PI) * 0.25 + slat1 * 0.5);
        sn = (float)Math.log((float)Math.cos(slat1) / (float)Math.cos(slat2)) / (float)Math.log(sn);
        float sf = (float)Math.tan((float)Math.PI * 0.25 + slat1 * 0.5);
        sf = (float)Math.pow(sf, sn) * (float)Math.cos(slat1) / sn;
        float ro = (float)Math.tan((float)Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / (float)Math.pow(ro, sn);
        Map rs = new HashMap();
        float ra = (float)Math.tan((float)Math.PI * 0.25 + (lat) * DEGRAD * 0.5);
        ra = re * sf / (float)Math.pow(ra, sn);
        float  theta = lng * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        rs.put("nx",(int)((float)Math.floor(ra * Math.sin(theta) + XO + 0.5)));
        rs.put("ny",(int)((float)Math.floor(ro - ra * Math.cos(theta) + YO + 0.5)));
        return (HashMap) rs;
    }

    private void checkfile(String year, String month, String day) throws IOException {
        String t = year + month + day;
        String line = null;
        File filename = new File(String.valueOf(getExternalFilesDir(null)));
        File temp = new File(getExternalFilesDir(null), t + ".txt");
        File[] files = filename.listFiles();


        if (files.length != 0) {
            for (int i = 0; i < files.length; i++) {
                if (temp.getName().equals(files[i].getName())) {
                    recyclerView.setVisibility(View.VISIBLE);
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(temp));
                    while ((line = bufferedReader.readLine()) != null) {
                        String title, content;
                        int idx = line.indexOf("*");
                        if (idx == -1) {
                            continue;
                        }
                        title = line.substring(0, idx);
                        content = line.substring(idx + 1, line.length());

                        addItem(title, content);

                    }
                    break;
                } else {
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }



    private void makefile(String year, String month, String day, String title, String content) throws IOException {
        String t = year + month + day;
        String insertline = title + "*" + content;
        String line = null;
        Boolean check = false;
        File loadfolder = new File(getExternalFilesDir(null) + "/");
        File[] files = loadfolder.listFiles();
        File filename = new File(getExternalFilesDir(null), t + ".txt");
        ArrayList<String> list = new ArrayList<>();

        if(files.length != 0){
            for(int i = 0; i < files.length; i++){
                if(filename.getName().equals(files[i].getName())){
                    check = true;
                }
            }
        }
        else{
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename)); //list에 있는 내용을 다시 쓰기위해서 writer를 사용해서 파일에 씀
                filename.createNewFile();

                bufferedWriter.append(insertline);
                bufferedWriter.newLine();
                bufferedWriter.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (check == true) { //파일이 있을 경우
            recyclerView.setVisibility(View.VISIBLE);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename)); //파일 내용을 불러옴
            while ((line = bufferedReader.readLine()) != null) { //파일 내용을 불러와서 list에 모두 저장
                list.add(line);
            }
            list.add(insertline); //list의 마지막에 가장 마지막으로 입력한 일정을 저장

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename)); //list에 있는 내용을 다시 쓰기위해서 writer를 사용해서 파일에 씀
            for (int j = 0; j < list.size(); j++) {
                try {

                    bufferedWriter.append(list.get(j));
                    bufferedWriter.newLine();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            bufferedWriter.close();
        }
        else{
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename)); //list에 있는 내용을 다시 쓰기위해서 writer를 사용해서 파일에 씀
                filename.createNewFile();

                bufferedWriter.append(insertline);
                bufferedWriter.newLine();
                bufferedWriter.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }


    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CalendarMemoAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void addItem(String title, String contents) {
        String t = tyear + tmonth + tday;
        CalendarData data = new CalendarData();
        data.setTitle(title);
        data.setContents(contents);
        data.setDate(t);

        adapter.addItem(data);
        adapter.notifyDataSetChanged();

    }

    @SuppressLint("WrongConstant")
    public void saveDiary(String title, String contents) {
        CalendarData data = new CalendarData();
        data.setTitle(title);
        data.setContents(contents);
        contact_memo = gson.toJson(data, CalendarData.class);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, contact_memo);
        editor.commit();
    }

    private void UpdeateLocation() {
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Latitude = location.getLatitude();
        Longitude = location.getLongitude();

    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);

            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    /**
     * checkPermission 메소드를 실행한 후 콜백 메소드
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                boolean isDeny = false;
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            isDeny = true;
                        }
                        //makeDir();
                    }
                }

                if (isDeny) {
                    showNoPermissionToastAndFinish();
                }
            }
        }
    }

    /**
     * 권한 거부 시 실행 메소드
     */
    private void showNoPermissionToastAndFinish() {

        Toast toast = Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT);
        toast.show();

        finish();
    }

//    public void makeDir() {
//        String root = getExternalFilesDir(null).getAbsolutePath(); //내장에 만든다
//        String directoryName = "clothesFolder";
//        final File myDir = new File(root + "/" + directoryName);
//        if (!myDir.exists()) {
//            myDir.mkdir();
//        } else {
//            System.out.println("file: " + root + "/" + directoryName +"already exists");
//        }
//    }
}