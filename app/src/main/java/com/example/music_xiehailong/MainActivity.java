package com.example.music_xiehailong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements Interceptor {

    public static final String TAG = "MyMainActivity";
    private HomeAdapter adapter;
    private RecyclerView recyclerView;
    private List<HomeItem> homeItems;
    private OkHttpClient okHttpClient;
    private Request request;
    public static final String BASE_URL_RETROFIT = "https://hotfix-service-prod.g.mi.com";
    public static final String BASE_URL_OKHTTP = "https://hotfix-service-prod.g.mi.com/music/homePage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeOkHttpRequest();

        homeItems = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new HomeAdapter(this, homeItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    private void onMessageEvent(MessageEvent event) {
//        adapter.notifyDataSetChanged();
//    }

    public void makeOkHttpRequest() {

        // 创建OkHttpClient请求
        request = new Request.Builder()
                .get()
                .url(BASE_URL_OKHTTP)
                .build();

        // 创建OkHttpClient，并添加拦截器
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(this)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();

                    Gson gson = new Gson();
                    ApiResponse apiResponse = gson.fromJson(responseData, ApiResponse.class);

                    // 获取每个 MusicInfo 对象
                    if (apiResponse != null && apiResponse.getData() != null) {
                        List<Module> modules = apiResponse.getData().getRecords();
                        for (Module module : modules) {
                            UpdateFragment(module);
                        }
                    }

//                    EventBus.getDefault().postSticky(new MessageEvent());

                } else {
                    Log.e(TAG, "OkHttp Request failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void UpdateFragment(Module module) {
        if (module.getStyle() == 1) {
            runOnUiThread(() -> {
                List<MusicInfo> musicInfoList = module.getMusicInfoList();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.banner_container, BannerFragment.newInstance(musicInfoList))
                        .commit();
            });
        } else if (module.getStyle() == 2) {
            runOnUiThread(() -> {
                List<MusicInfo> musicInfoList = module.getMusicInfoList();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.exclusive_container, ExclusiveSongFragment.newInstance(musicInfoList))
                        .commit();
            });
        } else if (module.getStyle() == 3) {
            runOnUiThread(() -> {
                for (MusicInfo musicInfo : module.getMusicInfoList()) {

                    homeItems.add(new HomeItem(module.getStyle(), module.getModuleName(), Collections.singletonList(musicInfo)));
                }
                adapter.notifyDataSetChanged();  // Notify the adapter about the data change
            });
        } else if (module.getStyle() == 4) {
            runOnUiThread(() -> {
                List<MusicInfo> musicInfoList = module.getMusicInfoList();
                for (int i = 0; i < musicInfoList.size(); i += 2) {
                    homeItems.add(new HomeItem(module.getStyle(), module.getModuleName(),
                            Arrays.asList(musicInfoList.get(i), musicInfoList.get(i+1))));
                }
                adapter.notifyDataSetChanged();  // Notify the adapter about the data change
            });
        }
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        // 添加请求头信息
        requestBuilder.addHeader("content-type", "application/json");

        // get接口添加公共参数
        if (TextUtils.equals(request.method(), "GET")) {
            HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
            httpUrlBuilder.addQueryParameter("current", "1");
            httpUrlBuilder.addQueryParameter("size", "5");

            requestBuilder.url(httpUrlBuilder.build());
        }
        return chain.proceed(requestBuilder.build());
    }
}