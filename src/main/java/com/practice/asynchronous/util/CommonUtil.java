package com.practice.asynchronous.util;

import static com.practice.asynchronous.util.LoggerUtil.log;
import static java.lang.Thread.sleep;

import org.apache.commons.lang3.time.StopWatch;

public class CommonUtil {

    public static StopWatch stopWatch = new StopWatch();

    public static void delay(long delayMilliSeconds)  {
        try{
            sleep(delayMilliSeconds);
        }catch (Exception e){
            LoggerUtil.log("Exception is :" + e.getMessage());
        }

    }

    public static void startTimer() {
        stopWatch.start();
    }

    public static void timeTaken() {
        stopWatch.stop();
        log("Total Time Taken : " + stopWatch.getTime());
    }
}
