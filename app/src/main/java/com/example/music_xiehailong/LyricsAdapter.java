package com.example.music_xiehailong;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LyricsAdapter extends RecyclerView.Adapter<LyricsAdapter.LyricsViewHolder> {

    private List<LrcParser.LrcLine> lrcLines;
    private int currentLineIndex = 0;

    public LyricsAdapter(List<LrcParser.LrcLine> lrcLines) {
        this.lrcLines = lrcLines;
    }

    @NonNull
    @Override
    public LyricsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyrics_item, parent, false);
        return new LyricsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LyricsViewHolder holder, int position) {
        LrcParser.LrcLine lrcLine = lrcLines.get(position);
        holder.lyricsTextView.setText(lrcLine.lyrics);

        // Highlight the current line
        if (position == currentLineIndex) {
            holder.lyricsTextView.setTextColor(Color.WHITE);
            holder.lyricsTextView.setTypeface(holder.lyricsTextView.getTypeface(), Typeface.BOLD);
        } else {
            holder.lyricsTextView.setTextColor(Color.argb(127,255,255,255));
            holder.lyricsTextView.setTypeface(holder.lyricsTextView.getTypeface(), Typeface.NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        return lrcLines.size();
    }

    public void updateCurrentLine(int currentLineIndex) {
        int previousLineIndex = this.currentLineIndex;
        this.currentLineIndex = currentLineIndex;

        notifyItemChanged(previousLineIndex);
        notifyItemChanged(currentLineIndex);
    }

    static class LyricsViewHolder extends RecyclerView.ViewHolder {
        TextView lyricsTextView;

        LyricsViewHolder(@NonNull View itemView) {
            super(itemView);
            lyricsTextView = itemView.findViewById(R.id.lyrics_text_view);
        }
    }
}
