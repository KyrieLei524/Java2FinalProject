package com.example.springproject.prepareation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class release {
    String release_time;
    Date release_at;

    String tag_name;

    public release(String release_time, String name) throws ParseException {
        this.release_time = release_time;
        this.tag_name = name;

        release_time = release_time.replace("Z", " UTC");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.release_at = format.parse(release_time);
    }
}
