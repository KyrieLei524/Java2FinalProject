package com.example.springproject.prepareation;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.HanLP;
import json.JsonFormater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class GetRepo {
  //Dreamacro/clash
  //youshandefeiyang/IPTV 测试Issues
  //yueyangw/yueyangw.github.io
  //acheong08/ChatGPT
  //ccg2018/ClashA
  //https://github.com/JamesNK/Newtonsoft.Json
  //PyO3/pyo3
  //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
  //PyFilesystem/pyfilesystem2
  public static String repo_url = "https://api.github.com/repos/PyFilesystem/pyfilesystem2";
  public static String repo_name = "PyFilesystem/pyfilesystem2";
  public static String commits_url = repo_url + "/commits";
  public static String issues_url = repo_url + "/issues";
  public static String release_url = repo_url + "/releases";
  //release间的commit数量列表
  public static ArrayList<Integer> commitBetweenRelease = new ArrayList<>();
    //release列表
  public static ArrayList<release> releaseList = new ArrayList<>();
    //release的总数
  public static int release_num;
    //开发者数量
  public static int developers_num;
    //开发者列表
  public static ArrayList<String> developersList = new ArrayList<>();
    //最活跃开发者姓名列表
  public static ArrayList<String> name = new ArrayList<>();
    //最活跃开发者姓名对应的commit数量
  public static ArrayList<Long> commit_num = new ArrayList<>();
    //该库commit列表
  public static ArrayList<commit> commitsList = new ArrayList<>();
    //该库已经关闭的issues的列表
  public static ArrayList<issue> closedIssueList = new ArrayList<>();
    //该库关闭issue数量
  public static int closedIssues_num;
    //该库开放issue数量
  public static int openIssues_num;
    //该库开放issue列表
  public static ArrayList<issue> openIssueList = new ArrayList<>();
    //该库解决issue的平均时间
  public static double issue_solve_average;
    //该库解决issue时间的中位数
  public static double issue_solve_median;
    //该库解决issue时间的方差
  public static double issue_solve_D;
    //该库解决issue时间的极值差
  public static double issue_solve_jicha;
    //该库commit时间分布
  public static Map<String, Long> timeLocation = new LinkedHashMap<>();
    //获取该库关键词列表 降序
  public static Map<String, Integer> keywords = new HashMap<>();

  public static void init() throws IOException, ParseException {
    System.out.println(0);
    getDeveloperNum();
    System.out.println(1);
    getMostActive();
    System.out.println(2);
    getTimeLocation();
    getClosedIssues();
    System.out.println(3);
    getOpenIssues();
    System.out.println(4);
    getSolutionTimeSymbol();
    System.out.println(5);
    getReleaseList();
    System.out.println(6);
    getRealeaseCommit();
    System.out.println(7);

  }

  public static void getTimeLocation(){
        timeLocation =  commitsList.
                stream().
                collect(
                        Collectors.groupingBy(
                                commit::getWeekJ,
                                Collectors.counting()
                        )
                );

    }

  static java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");


  public static void main(String[] args) throws IOException, ParseException {
    init();
    System.out.println(222);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("repo_name", repo_name);
    jsonObject.put("developers_num",developers_num);
    jsonObject.put("active10Developers", activeD());
    jsonObject.put("activeDevelopersEmail", activeD_email());
    jsonObject.put("openIssues_num", openIssues_num);
    jsonObject.put("closedIssues_num", closedIssues_num);
    jsonObject.put("commits_num", commitsList.size());
    jsonObject.put("releaseSolution_var", df.format(issue_solve_D / 3600 / 3600 / 24));
    jsonObject.put("releaseSolution_average", df.format(issue_solve_average / 3600 / 24));
    jsonObject.put("releaseSolution_median", df.format(issue_solve_median / 3600 / 24));
    jsonObject.put("releaseSolution_range", df.format(issue_solve_jicha / 3600 / 24));
    jsonObject.put("releases_num", release_num);
    jsonObject.put("timeLocation", timeLocation);
    jsonObject.put("commit_numBetweenRelease", commitBetweenRelease);
    jsonObject.put("Keywords", getKeywords());


    System.out.println(JsonFormater.format(jsonObject.toJSONString()));

//        System.out.println(getKeywords());
//        getDeveloperNum();
//        getMostActive();
////        for(int i = 0; i < developersList.size(); i++){
////            System.out.println(commitsList.get(i).getName());
////        }
//        System.out.println(activeD());


        for(int i = 0; i < releaseList.size(); i++){
            System.out.println(releaseList.get(i).release_at);
        }

        System.out.println("commit");
        for(int i = 0; i < commitsList.size(); i++){
            System.out.println(commitsList.get(i).commit_at);
        }
        System.out.println(commitBetweenRelease);



    }

  public static Map<String, Long> activeD(){
        Map<String, Long> map = new LinkedHashMap<>();
        for(int i = 0; i < 10; i++){
            map.put(name.get(i), commit_num.get(i));
        }
        return map;
    }
  public static Map<String, String> activeD_email(){
        Map<String, String> map = new LinkedHashMap<>();
        for(int i = 0; i < 10; i++){
            map.put(name.get(i), getEmail(name.get(i)));
        }
        return map;
    }

    public static String getEmail(String name){
        for(int i = 0; i < commitsList.size(); i++){
            if(commitsList.get(i).name.equals(name)){
                return commitsList.get(i).email;
            }
        }
        return null;
    }

    //得到该库中解决问题所用时间的各项特征值
    public static void getSolutionTimeSymbol(){
        double[] numbers = new double[closedIssues_num];
        for(int i = 0; i < closedIssues_num; i++){
            numbers[i] = closedIssueList.get(i).difference;
//            System.out.println(numbers[i]);
        }

        //求均值
        issue_solve_average = average(numbers);
        //求方差
        issue_solve_D= variance(numbers);
        //求中位数
        issue_solve_median = median(numbers);
        //求极值差
        issue_solve_jicha = jizhicha(numbers);
//
//        System.out.println(issue_solve_average);
//        System.out.println(issue_solve_D);
//        System.out.println(issue_solve_median);
//        System.out.println(issue_solve_jicha);
    }
    public static double jizhicha(double[] arr){
        Arrays.sort(arr);
        return arr[arr.length - 1] - arr[0];
    }
    public static double median(double[] arr) {
//        System.out.println(arr.length);
        // 先排序
        Arrays.sort(arr);
        // 如果是偶数，则为中间两个数的和除以2
        if (arr.length % 2 == 0) {
            return (double) ((arr[arr.length / 2 - 1] + arr[arr.length / 2])) / 2;
        }
        // 否则就是中间这个数
        return arr[arr.length / 2];
    }
    public static double average(double[] x) {
        int n = x.length;            //数列元素个数
        double sum = 0;
        for (double i : x) {        //求和
            sum+=i;
        }
        return sum/n;
    }
    public static double variance(double[] x) {
        int n = x.length;            //数列元素个数
        double avg = average(x);    //求平均值
        double var = 0;
        for (double i : x) {
            var += (i-avg)*(i-avg);    //（x1-x）^2+（x2-x）^2+......（xn-x）^2
        }
        return var/n;
    }

    //得到库中closed issues列表 并返回closed issues的数量
    public static int getClosedIssues() throws IOException, ParseException {
        int count = 0;
        int page = 1;
        String s;
        while(true) {
            System.out.println(123123);
            s = issues_url;
            s = s + "?page=" + page++ + "&per_page=100&state=closed";
            URL url = new URL(s);
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
//

            String created_time;
            String closed_time;
            String state;
            String title;
            String body;
            String comments_url;


//            System.out.println(JsonFormater.format(response.toString()));
            JSONArray arr = JSON.parseArray(response.toString());
//            System.out.println(JsonFormater.format(response.toString()));


            for(int i = 0; i < arr.size(); i++) {
                created_time = arr.getJSONObject(i).getString("created_at");
                closed_time = arr.getJSONObject(i).getString("closed_at");
                state = arr.getJSONObject(i).getString("state");
                title = arr.getJSONObject(i).getString("title");
                comments_url = arr.getJSONObject(i).getString("comments_url");
                body = arr.getJSONObject(i).getString("body");

                issue iss = new issue(created_time, closed_time, state, title, comments_url, body);
                closedIssueList.add(iss);
                count++;
            }
            if(arr.size() < 100)break;
        }
        closedIssues_num = closedIssueList.size();
        return count;
    }

    //得到库中open issues的列表 并返回open issues的数量
    public static int getOpenIssues() throws IOException, ParseException {
        int count = 0;
        int page = 1;
        String s;
        while(true) {

            s = issues_url;
            s = s + "?page=" + page++ + "&per_page=100&state=open";
            URL url = new URL(s);
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
//

            String created_time;
            String closed_time;
            String state;
            String title;
            String body;
            String comments_url;



            JSONArray arr = JSON.parseArray(response.toString());
//            System.out.println(JsonFormater.format(response.toString()));


            for(int i = 0; i < arr.size(); i++) {
                created_time = arr.getJSONObject(i).getString("created_at");
                closed_time = arr.getJSONObject(i).getString("closed_at");
                state = arr.getJSONObject(i).getString("state");
                title = arr.getJSONObject(i).getString("title");
                comments_url = arr.getJSONObject(i).getString("comments_url");
                body = arr.getJSONObject(i).getString("body");


                issue iss = new issue(created_time, "unclosed", state, title,  comments_url, body);
                openIssueList.add(iss);
                count++;
            }
            if(arr.size() < 100)break;
        }
        openIssues_num = openIssueList.size();
        return count;
    }

    //返回最活跃开发者的name 的 list （以防同时有多个最活跃的开发者）
    public static void getMostActive(){
        Map<String, Long> mapp =  developersList.
                stream().
                collect(
                        Collectors.groupingBy(
                                String::toString,
                                Collectors.counting()
                        )
                );
//        System.out.println(mapp);
        mapp.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach(x -> {
            name.add(x.getKey());
            commit_num.add(x.getValue());
        });

    }

    //获取本库的开发者数量 并得到commits的列表
    public static void getDeveloperNum() throws IOException, ParseException {
        int page = 1;
        String s = commits_url;
        while(true) {
            s = commits_url;
            s = s + "?page=" + page++ + "&per_page=100";
            URL url = new URL(s);
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
//
//            System.out.println(JsonFormater.format(response.toString()));

            String name;
            String date;
            String email;
            String message;
            String url_html;
            String login;
            JSONArray arr = JSON.parseArray(response.toString());
//            System.out.println(JsonFormater.format(arr.toString()));

            for(int i = 0; i < arr.size(); i++) {
                name = arr.getJSONObject(i).getJSONObject("commit").getJSONObject("author").getString("name");
//                System.out.println(arr.get(i));
//                login = arr.getJSONObject(i).getJSONObject("committer").getString("login");
                login = "";
                email = arr.getJSONObject(i).getJSONObject("commit").getJSONObject("author").getString("email");
                date = arr.getJSONObject(i).getJSONObject("commit").getJSONObject("author").getString("date");
                message = arr.getJSONObject(i).getJSONObject("commit").getString("message");
                url_html = arr.getJSONObject(i).getString("html_url");

                commit commit = new commit(date, url_html, name, email, message, login);

                developersList.add(name);
                commitsList.add(commit);
            }
            if(arr.size() < 100)break;
        }
        developers_num = (int)developersList.stream().distinct().count();
    }

    //获取本库的release列表 和 数量
    public static void getReleaseList() throws IOException, ParseException {
        int page = 1;
        String s = release_url;
        while(true) {
            s = release_url;
            s = s + "?page=" + page++ + "&per_page=100";
            URL url = new URL(s);
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
//
//            System.out.println(JsonFormater.format(response.toString()));


            JSONArray arr = JSON.parseArray(response.toString());
//            System.out.println(JsonFormater.format(arr.toString()));
//            System.out.println(arr.size());

            String time;
            String tag_name;

            for(int i = 0; i < arr.size(); i++) {
                time = arr.getJSONObject(i).getString("created_at");
                tag_name = arr.getJSONObject(i).getString("tag_name");
                release release = new release(time, tag_name);

                releaseList.add(release);

            }
            if(arr.size() < 100)break;
        }
        release_num = releaseList.size();
    }

    //判断一个时间在不在两个时间的中间
    public static boolean checkDate(Date begin, Date end, Date date){
        if(date.getTime() > begin.getTime() && end.getTime() > date.getTime())        return true;
        return false;
    }

    //得到release间的commit数量 release从以前到现在
    public static void getRealeaseCommit(){
        for(int i = releaseList.size()- 1; i > 0; i--){
            int num = 0;
            for(int j = 0; j < commitsList.size(); j++){
                if(checkDate(releaseList.get(i).release_at, releaseList.get(i - 1).release_at, commitsList.get(j).commit_at)) num++;
//                System.out.println(releaseList.get(i).release_at);
//                System.out.println(releaseList.get(i - 1).release_at);
//                System.out.println(commitsList.get(j).commit_at);
//                System.out.println(checkDate(releaseList.get(i).release_at, releaseList.get(i - 1).release_at, commitsList.get(j).commit_at));
            }
            commitBetweenRelease.add(num);
        }
//        for(int i = 0; i < releaseList.size(); i++){
//            System.out.println(releaseList.get(i).release_at.);
//        }
    }

    //获取issue 相关文本 出现频次前十的关键词及次数
    public static Map<String, Float> getKeywords(){
        StringBuilder text = new StringBuilder();
        for(int i = 0; i < openIssueList.size(); i++){
            text.append(openIssueList.get(i).title);
            text.append(openIssueList.get(i).body);
            for(int j = 0; j < openIssueList.get(i).commentsList.size(); j++){
                text.append(openIssueList.get(i).commentsList.get(j));
            }
        }
        for(int i = 0; i < closedIssueList.size(); i++){
            text.append(closedIssueList.get(i).title);
            text.append(closedIssueList.get(i).body);
            for(int j = 0; j < closedIssueList.get(i).commentsList.size(); j++){
                text.append(closedIssueList.get(i).commentsList.get(j));
            }
        }
        Map<String, Float> keywordList = TextRankKeyword2.getTerm_Rank(text.toString(), 700);
        return keywordList;
    }





}
