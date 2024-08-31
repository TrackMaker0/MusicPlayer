package com.example.music_xiehailong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class HotMusicFragment extends Fragment {

    private static final String ARG_MUSIC_LIST = "music_list";
    private RecyclerView recyclerView;
    private MusicAdapter adapter;
    private List<MusicInfo> musicInfoList;

    public static HotMusicFragment newInstance(List<MusicInfo> musicList) {
        HotMusicFragment fragment = new HotMusicFragment();
        Bundle args = new Bundle();

        Gson gson = new Gson();
        String musicListJson = gson.toJson(musicList);
        args.putString(ARG_MUSIC_LIST, musicListJson);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String musicListJson = getArguments().getString(ARG_MUSIC_LIST);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<MusicInfo>>() {}.getType();
            musicInfoList = gson.fromJson(musicListJson, listType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化 RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);

        // 初始化 Adapter
        adapter = new MusicAdapter(getContext(), musicInfoList);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}