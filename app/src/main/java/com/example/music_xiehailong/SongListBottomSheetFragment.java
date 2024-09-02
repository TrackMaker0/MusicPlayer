package com.example.music_xiehailong;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.ref.WeakReference;

public class SongListBottomSheetFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private SongListAdapter adapter;
    private TextView totalCountView;
    private TextView loopModeView;
    private SongListAdapter.OnDataSetChangeListener onDataSetChangeListener;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list_bottom_sheet, container, false);
        recyclerView = view.findViewById(R.id.song_list_recycler_view);
        totalCountView = view.findViewById(R.id.totalMusicCount);
        loopModeView = view.findViewById(R.id.loopMorden);

        Drawable drawableLeft = null;
        int mode = DataManager.getMusicPlayerService().getLoopState();
        if (mode == 0) {
            loopModeView.setText("顺序播放");
            drawableLeft = getResources().getDrawable(R.drawable.ic_ordered, null);
        } else if (mode == 1) {
            loopModeView.setText("单曲循环");
            drawableLeft = getResources().getDrawable(R.drawable.ic_loop, null);
        } else if (mode == 2) {
            loopModeView.setText("随机播放");
            drawableLeft = getResources().getDrawable(R.drawable.ic_random, null);
        }

        loopModeView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);

        totalCountView.setText(String.valueOf(DataManager.getCount()));

        // 实现 onDataSetChangeListener 接口
        onDataSetChangeListener = new WeakReference<>(new SongListAdapter.OnDataSetChangeListener() {
            @Override
            public void onDataSetChange() {
                if (totalCountView != null) {
                    totalCountView.setText(String.valueOf(DataManager.getCount()));
                }
            }
        }).get();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SongListAdapter(DataManager.getMusicInfoList(), onDataSetChangeListener);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 清除对 View 和 Adapter 的引用，避免内存泄露
        recyclerView.setAdapter(null);
        recyclerView = null;
        adapter = null;
        onDataSetChangeListener = null;
    }
}