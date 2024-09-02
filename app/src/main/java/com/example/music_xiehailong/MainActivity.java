package com.example.music_xiehailong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private HomeBaseQuickAdapter adapter;
    private MusicPlayerService musicPlayerService;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable checkIfPreparedRunnable;
    private ObjectAnimator rotateAnimator;
    private Drawable pauseDrawable, playDrawable;
    private SongListBottomSheetFragment bottomSheet;
    private Intent serviceIntent;
    private ImageView floatingCover, floatingPlay, floatingList;
    private TextView floatingMusicName, floatingAuthor;
    private View floatingFill;
    private View floatingView;
    private boolean isBound = false;
    private boolean isLoading = false;
    private int current = 1;
    private int size = 4;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder) service;
            musicPlayerService = binder.getService();
            isBound = true;
            DataManager.setMusicPlayerService(musicPlayerService);
            BindListenerAndAdapter();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitData();

        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        DataManager.loadLikeStatus(this);

//        // 改为使用EventBus
//        DataManager.setOnListEmptyListener(new DataManager.OnListEmptyListener() {
//            @Override
//            public void OnListEmpty() {
//                floatingView.setVisibility(View.GONE);
//                if (bottomSheet != null) bottomSheet.dismiss();
//            }
//        });

        serviceIntent = new Intent(this, MusicPlayerService.class);
        startService(serviceIntent);

        homeItems = new ArrayList<>();
        makeOkHttpRequest();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void InitData() {
        floatingCover = findViewById(R.id.floatingCover);
        floatingMusicName = findViewById(R.id.floatingMusicName);
        floatingAuthor = findViewById(R.id.floatingAuthor);
        floatingPlay = findViewById(R.id.floatingPlay);
        floatingList = findViewById(R.id.floatingList);
        floatingFill = findViewById(R.id.floatingFill);
        floatingView = findViewById(R.id.floatingView);
        pauseDrawable = getDrawable(R.drawable.ic_pause_black);
        playDrawable = getDrawable(R.drawable.ic_play_black);
    }

    private void BindListenerAndAdapter() {

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

        floatingFill.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
            startActivity(intent);
        });

        floatingPlay.setOnClickListener(v -> {
            if (musicPlayerService.isPlayAllowed()) {
                musicPlayerService.setPlayAllowed(false);
                musicPlayerService.pauseMusic();
                ChangeStateToPause();
            } else {
                musicPlayerService.setPlayAllowed(true);
                musicPlayerService.playMusic();
                ChangeStateToPlay();
            }
        });

        floatingList.setOnClickListener(v -> {
            bottomSheet = new SongListBottomSheetFragment();
            bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
        });

        startRandomModuleWhenPrePared();
    }

    private void startRandomModuleWhenPrePared() {
        // 创建一个 Runnable 来检查歌曲是否准备好
        checkIfPreparedRunnable = new Runnable() {
            @Override
            public void run() {
                if (!homeItems.isEmpty()) {
                    // 准备好了,随机选择模块
                    HomeItem randomItem = homeItems.get(new Random().nextInt(homeItems.size()));
                    if (randomItem.getContentItems() != null && !randomItem.getContentItems().isEmpty()) {
                        List<MusicInfo> randomMusic = randomItem.getContentItems();
                        DataManager.addAll(randomMusic);
                    }
                    musicPlayerService.setCurrentSongIndex(0);
                } else {
                    // 还未准备好，继续检查
                    handler.postDelayed(this, 100); // 每100ms检查一次
                }
            }
        };

        // 启动检查
        handler.post(checkIfPreparedRunnable);
    }

    private void ChangeStateToPause() {
        floatingPlay.setImageResource(R.drawable.ic_play_black);  // 切换为播放图标
        // 停止动画
        float currentRotation = floatingPlay.getRotation();
        setCurrentRotationAngle(currentRotation);
        if (rotateAnimator != null && rotateAnimator.isRunning()) rotateAnimator.cancel();
    }

    private void ChangeStateToPlay() {
        floatingPlay.setImageResource(R.drawable.ic_pause_black);  // 切换为暂停图标
        // 开始动画
        if (rotateAnimator != null && rotateAnimator.isRunning()) rotateAnimator.cancel();
        float currentRotation = getCurrentRotationAngle();
        startRotationAnimation(currentRotation);
    }

    private void startRotationAnimation(float startAngle) {
        rotateAnimator = ObjectAnimator.ofFloat(floatingCover, "rotation", startAngle, startAngle + 360f);
        rotateAnimator.setDuration(10000); // Duration of one full rotation
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        rotateAnimator.start();
    }

    private float getCurrentRotationAngle() {
        Object tag = floatingPlay.getTag();
        if (tag instanceof Float) {
            return (Float) tag;
        } else {
            return 0f;
        }
    }

    private void setCurrentRotationAngle(float angle) {
        floatingPlay.setTag(angle); // Save the current angle
    }

    private void updateFloatingView() {
        MusicInfo currentMusicInfo = musicPlayerService.getCurrentMusicInfo();
        if (currentMusicInfo != null) {
            Glide.with(this)
                    .load(currentMusicInfo.getCoverUrl())
                    .placeholder(R.drawable.placeholder) // 设置加载中的占位图
                    .error(R.drawable.error) // 设置加载失败的占位图
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(floatingCover);
            floatingMusicName.setText(currentMusicInfo.getMusicName());
            floatingAuthor.setText(currentMusicInfo.getAuthor());

            // 同步播放组件
            checkPlayState();
        }
    }

    private void checkPlayState() {
        Drawable currentDrawable = floatingPlay.getDrawable();
        if (musicPlayerService.isPlayAllowed()) { // 允许播放
            if (currentDrawable != playDrawable) { // 但是暂停状态(播放图标)
                ChangeStateToPlay();
            }
        } else {
            if (currentDrawable != pauseDrawable) {
                ChangeStateToPause();
            }
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMusicStartEvent(MusicChangeEvent event) throws IOException {
        updateFloatingView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMusicListEmptyEvent(MusicListEmptyEvent event) {
        if (event.isEmpty) {
            floatingView.setVisibility(View.GONE);
            if (bottomSheet != null) bottomSheet.dismiss();
        } else {
            floatingView.setVisibility(View.VISIBLE);
            DataManager.nextMusic();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicPlayerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        EventBus.getDefault().register(this);
        if (DataManager.getCount() != 0) floatingView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DataManager.saveLikeStatus(this);
        EventBus.getDefault().unregister(this);
        if (rotateAnimator != null && rotateAnimator.isRunning()) rotateAnimator.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bottomSheet = null;
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
        stopService(serviceIntent);
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