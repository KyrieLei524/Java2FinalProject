package com.example.springproject.prepareation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class commit {
    public String getCommit_time() {
        return commit_time;
    }

    public void setCommit_time(String commit_time) {
        this.commit_time = commit_time;
    }

    public String getGithubURL_commit() {
        return githubURL_commit;
    }

    public void setGithubURL_commit(String githubURL_commit) {
        this.githubURL_commit = githubURL_commit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCommit_at() {
        return commit_at;
    }

    public void setCommit_at(Date commit_at) {
        this.commit_at = commit_at;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String commit_time;

    String githubURL_commit;

    @Override
    public String toString() {
        return "commit{" +
                "commit_time='" + commit_time + '\'' +
                ", githubURL_commit='" + githubURL_commit + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", commit_at=" + commit_at +
                ", message='" + message + '\'' +
                '}';
    }

    String name;

    String email;

    Date commit_at;

    String message;

    String login;
    String weekJ;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getWeekJ() {
        return weekJ;
    }

    public commit(String commit_time, String githubURL_commit, String name, String email, String message, String login) throws ParseException {
        this.commit_time = commit_time;
        commit_time = commit_time.replace("Z", " UTC");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.commit_at = format.parse(commit_time);

//        this.login = login;
        this.githubURL_commit = githubURL_commit;
        this.name = name;
        this.email = email;
        this.message = message;
        this.weekJ = getWeek(this.commit_at);


    }

    public static String getWeek(Date date){
        String[] weeks = {"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index < 0){
            week_index = 0;
        }
        return weeks[week_index];
    }

    public static void main(String[] args) throws ParseException {
        String commcommit_time = "2022-12-11T14:35:24Z";
        commcommit_time = commcommit_time.replace("Z", " UTC");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date test = format.parse(commcommit_time);
        System.out.println(getWeek(test));
    }
}
