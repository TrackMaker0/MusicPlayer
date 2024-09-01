package com.example.music_xiehailong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
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


    public static final String BASE_URL_OKHTTP = "https://hotfix-service-prod.g.mi.com/music/homePage";
    public static final String TAG = "MyMainActivity";
    private SwipeRefreshLayout swipeRefreshView;
    private List<HomeItem> homeItems;
    private List<MusicInfo> musicInfoList;
    private HomeBaseQuickAdapter adapter;
    private boolean isLoading = false;
    private int current = 1;
    private int size = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeItems = new ArrayList<>();
        makeOkHttpRequest();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new HomeBaseQuickAdapter(this, homeItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipeRefreshView = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshView.setOnRefreshListener(() -> {
            if (isLoading || adapter.getLoadMoreModule().isLoading()) return;
            isLoading = true;
            homeItems.clear();
            makeOkHttpRequest();
        });

        adapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (isLoading || swipeRefreshView.isRefreshing()) return;
            isLoading = true;
            makeOkHttpRequest();
        });
    }

    public void makeOkHttpRequest() {
        // 创建OkHttpClient请求
        Request request = new Request.Builder().get().url(BASE_URL_OKHTTP).build();

        // 创建OkHttpClient，并添加拦截器
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(this).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();

                    Gson gson = new Gson();
                    ApiResponse apiResponse = gson.fromJson(responseData, ApiResponse.class);

                    runOnUiThread(() -> {
                        // 获取每个 MusicInfo 对象
                        if (apiResponse != null && apiResponse.getData() != null) {
                            List<Module> modules = apiResponse.getData().getRecords();
                            for (Module module : modules) {
                                UpdateHomeItemList(module);
                            }
                        }
                        isLoading = false;
                        swipeRefreshView.setRefreshing(false);
                        adapter.getLoadMoreModule().loadMoreComplete();
                    });
                } else {
                    Log.e(TAG, "OkHttp Request failed with code: " + response.code());
                    runOnUiThread(() -> {
                        isLoading = false;
                        swipeRefreshView.setRefreshing(false);
                        adapter.getLoadMoreModule().loadMoreComplete();
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    isLoading = false;
                    swipeRefreshView.setRefreshing(false);
                    adapter.getLoadMoreModule().loadMoreComplete();
                });
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void UpdateHomeItemList(Module module) {
        if (module.getStyle() == 1) {
            homeItems.add(new HomeItem(module.getStyle(), module.getModuleName(), module.getMusicInfoList()));
//            adapter.notifyItemChanged(0);
            adapter.notifyDataSetChanged();
        } else if (module.getStyle() == 2) {
            if (homeItems.size() > 1) {
                List<MusicInfo> existingList = homeItems.get(1).getContentItems();
                List<MusicInfo> updatedList = new ArrayList<>(existingList);
                updatedList.addAll(module.getMusicInfoList());
                homeItems.get(1).setContentItems(updatedList);
                adapter.notifyItemChanged(1);
            } else {
                homeItems.add(new HomeItem(module.getStyle(), module.getModuleName(), module.getMusicInfoList()));
            }
//            adapter.notifyItemChanged(1);
            adapter.notifyDataSetChanged();
        } else if (module.getStyle() == 3) {
            int start = homeItems.size();
            for (MusicInfo musicInfo : module.getMusicInfoList()) {
                homeItems.add(new HomeItem(module.getStyle(), module.getModuleName(), Collections.singletonList(musicInfo)));
            }
//            adapter.notifyItemRangeChanged(start, homeItems.size() - start);
            adapter.notifyDataSetChanged();
        } else if (module.getStyle() == 4) {
            int start = homeItems.size();
            List<MusicInfo> musicInfoList = module.getMusicInfoList();
            for (int i = 0; i < musicInfoList.size(); i += 2) {
                homeItems.add(new HomeItem(module.getStyle(), module.getModuleName(), Arrays.asList(musicInfoList.get(i), musicInfoList.get(i + 1))));
            }
//            adapter.notifyItemRangeChanged(start, homeItems.size() - start);
            adapter.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (homeItems.isEmpty()) {
            current = 1;
            size = 4;
        } else {
            current += size;
            size = 1;
        }

        Log.d(TAG, "intercept: " + current + "and" + size);

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        // 添加请求头信息
        requestBuilder.addHeader("content-type", "application/json");

        // get接口添加公共参数
        if (TextUtils.equals(request.method(), "GET")) {
            HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
            httpUrlBuilder.addQueryParameter("current", String.valueOf(current));
            httpUrlBuilder.addQueryParameter("size", String.valueOf(size));

            requestBuilder.url(httpUrlBuilder.build());
        }
        return chain.proceed(requestBuilder.build());
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
    }
}