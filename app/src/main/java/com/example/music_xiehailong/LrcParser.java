package com.example.music_xiehailong;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcParser {

    public interface LrcCallback {
        void onSuccess(List<LrcLine> lrcLines);
        void onError(IOException e);
    }

    public static class LrcDownloader {
        private final String LRC_URL;
        public LrcDownloader(String lrc_url) {
            this.LRC_URL = lrc_url;
        }
        public String downloadLrc() throws IOException {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(LRC_URL)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                return response.body().string();
            }
        }
    }

    public static class LrcLine {
        public final int timeInMillis;
        public final String lyrics;

        public LrcLine(int timeInMillis, String lyrics) {
            this.timeInMillis = timeInMillis;
            this.lyrics = lyrics;
        }

        @Override
        public String toString() {
            return "Time: " + timeInMillis + "ms, Lyrics: " + lyrics;
        }
    }

    public void parseLrcFromUrl(String lrc_url, LrcCallback callback) {
        new Thread(() -> {
            try {
                String lrcContent = new LrcDownloader(lrc_url).downloadLrc();
                List<LrcLine> lrcLines = parseLrc(lrcContent);
                callback.onSuccess(lrcLines);
            } catch (IOException e) {
                callback.onError(e);
            }
        }).start();
    }

    private List<LrcLine> parseLrc(String lrcContent) {
        List<LrcLine> lrcLines = new ArrayList<>();

        String[] lines = lrcContent.split("\n");
        Pattern pattern = Pattern.compile("\\[(\\d{2}):(\\d{2})\\.(\\d{2})\\]");

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                int minutes = Integer.parseInt(matcher.group(1));
                int seconds = Integer.parseInt(matcher.group(2));
                int millis = Integer.parseInt(matcher.group(3)) * 10;

                int timeInMillis = (minutes * 60 + seconds) * 1000 + millis;
                String lyrics = line.substring(matcher.end()).trim();

                lrcLines.add(new LrcLine(timeInMillis, lyrics));
            }
        }

        return lrcLines;
    }
}
