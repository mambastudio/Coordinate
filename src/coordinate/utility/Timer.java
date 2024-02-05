/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

/**
 *
 * @author user
 */
public class Timer {
    private long startTime, endTime;

    public Timer() {
        startTime = endTime = 0;
    }

    public void start() {
        startTime = endTime = System.nanoTime();
    }

    public void end() {
        endTime = System.nanoTime();
    }

    public long nanos() {
        return endTime - startTime;
    }

    public double seconds() {
        return (endTime - startTime) * 1e-9;
    }

    public static String toString(long nanos) {
        Timer t = new Timer();
        t.endTime = nanos;
        return t.toString();
    }

    public static String toString(double seconds) {
        Timer t = new Timer();
        t.endTime = (long) (seconds * 1e9);
        return t.toString();
    }

    @Override
    public String toString() {
        long nanos = nanos();
        long millis = nanos / (1000 * 1000);
        if (millis < 1000) {
            return String.format("%dms %dns", millis, nanos % (1000 * 1000));
        }

        long hours = millis / (60 * 60 * 1000);
        millis -= hours * 60 * 60 * 1000;
        long minutes = millis / (60 * 1000);
        millis -= minutes * 60 * 1000;
        long seconds = millis / 1000;
        millis -= seconds * 1000;

        return String.format("%dhrs : %02dmin : %02d.%03dsec %dns", hours, minutes, seconds, millis, nanos % 1000);
    }
    
    public static Timer timeThis(TimerCallBack callback)
    {
        Timer timer = new Timer();
        timer.start();
        callback.execute();
        timer.end();
        return timer;
    }
    
    public static interface TimerCallBack
    {
        public void execute();
    }
}
