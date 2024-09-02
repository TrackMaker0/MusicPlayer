package com.example.music_xiehailong;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// 根对象
public class ApiResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private Data data;

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    // Data 对象
    public static class Data {
        @SerializedName("records")
        private List<Module> records;

        @SerializedName("total")
        private int total;

        @SerializedName("size")
        private int size;

        @SerializedName("current")
        private int current;

        @SerializedName("pages")
        private int pages;

        // Getters and Setters
        public List<Module> getRecords() { return records; }
        public void setRecords(List<Module> records) { this.records = records; }

        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }

        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }

        public int getCurrent() { return current; }
        public void setCurrent(int current) { this.current = current; }

        public int getPages() { return pages; }
        public void setPages(int pages) { this.pages = pages; }
    }
}

