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

    // Getters and Setters
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }
}