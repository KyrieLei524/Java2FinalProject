package com.example.springproject.prepareation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class issue {
    Date created_at;
    Date closed_at;
    String created_time;

    String closed_time;

    long difference = 0;

    String state;

    String title;

    String body;

    String comments_url;

    ArrayList<String> commentsList = new ArrayList<>();

    public issue(String created_at, String closed_at, String state, String title, String comments_url, String body) throws ParseException, IOException {
        created_at = created_at.replace("Z", " UTC");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.created_at = format.parse(created_at);
        this.created_time = defaultFormat.format(this.created_at);

        this.closed_time = closed_at;
        if(!closed_at.equals("unclosed")) {
            closed_at = closed_at.replace("Z", " UTC");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
            SimpleDateFormat defaultFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.closed_at = format.parse(closed_at);
            this.closed_time = defaultFormat.format(this.closed_at);
        }

        this.state = state;
        this.title = title;
        this.body = body;
        this.comments_url = comments_url;
        getComments();

        getDifference();
    }

    //获取该issue的comments内容列表
    public void getComments() throws IOException {
        URL url = new URL(comments_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer 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");
//            connection.setRequestProperty("");
        connection.connect();

        StringBuilder response = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));

        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
//        System.out.println(JsonFormater.format(response.toString()));

        JSONArray arr = JSON.parseArray(response.toString());
//        System.out.println(JsonFormater.format(arr.toString()));
        for(int i = 0; i < arr.size(); i++){
            commentsList.add(arr.getJSONObject(i).getString("body"));
        }
//        System.out.println(commentsList);

    }

    //计算两个Date的时间差
    public long getDifference(){
        if(closed_time.equals("unclosed"))return 0;

        long day = 0;//天数差
        long hour = 0;//小时数差
        long min = 0;//分钟数差
        long second=0;//秒数差
        long diff=0 ;//毫秒差
        long time1 = created_at.getTime();
        long time2 = closed_at.getTime();
        diff = time2 - time1;
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        second = diff/1000;

        this.difference = second;
        return second;

    }

    public static void main(String[] args) throws ParseException, IOException {
        issue i = new issue("2022-11-27T12:12:58Z", "2022-11-27T12:13:58Z", "closed", "测试", "https://api.github.com/repos/youshandefeiyang/IPTV/issues/8/comments", "test");
    }
}
