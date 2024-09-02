package com.example.music_xiehailong;

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

public class SongListBottomSheetFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private SongListAdapter adapter;
    private TextView totalCountView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list_bottom_sheet, container, false);
        recyclerView = view.findViewById(R.id.song_list_recycler_view);
        totalCountView = view.findViewById(R.id.totalMusicCount);

        totalCountView.setText(String.valueOf(DataManager.getCount()));

        // 实现 onDataSetChangeListener 接口
        SongListAdapter.OnDataSetChangeListener onDataSetChangeListener = new SongListAdapter.OnDataSetChangeListener() {
            @Override
            public void onDataSetChange() {
                totalCountView.setText(String.valueOf(DataManager.getCount()));
            }
        };

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SongListAdapter(DataManager.getMusicInfoList(), onDataSetChangeListener);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
