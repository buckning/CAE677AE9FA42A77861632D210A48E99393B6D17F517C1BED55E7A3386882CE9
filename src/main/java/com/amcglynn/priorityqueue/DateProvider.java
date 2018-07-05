package com.amcglynn.priorityqueue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateProvider {
    public String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
    }

    public Long getTimeDifferenceInSeconds(String startTime, String endTime) {
        long differenceInSeconds;
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").parse(startTime);
            Date endDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").parse(endTime);
            differenceInSeconds = (endDate.getTime() - startDate.getTime()) / 1000;
        } catch (ParseException e) {
            differenceInSeconds = -1;
        }
        return differenceInSeconds;
    }
}
