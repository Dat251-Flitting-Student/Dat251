package no.hvl.dat251.flittig_student;

import java.util.Timer;
import java.util.TimerTask;

public class StopWatch {
    static int elapsedTime = 0;
    static int points = 0;
    public static boolean b1 = true;
    Timer timer = new Timer();
    protected static StopWatch obj;

    static class Helper extends TimerTask {
        //    int elapsedTime = 0;
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        public static int i = 0;

        public void run() {
            if (!b1) {
                synchronized (StopWatch.obj) {
                    StopWatch.obj.notify();
                }
            }
            System.out.println("Seconds " + ++seconds);
            elapsedTime += 1;
            if (seconds % 3 == 0) {
                if (b1) {
                    points += 10;
                } else {
                    synchronized (StopWatch.obj) {
                        StopWatch.obj.notify();
                    }
                }
            }
            if (seconds == 10) {
//                System.out.println("The program ran for " + elapsedTime + " seconds!!!");
                synchronized (StopWatch.obj) {
                    StopWatch.obj.notify();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        obj = new StopWatch();

        Timer timer = new Timer();
        TimerTask task = new Helper();

        timer.schedule(task, 1000, 1000);

//        timer.scheduleAtFixedRate(task, date, 5000);

        System.out.println("Timer running");
        synchronized (obj) {
            //make the main thread wait
            obj.wait();

            //once timer has scheduled the task 4 times,
            //main thread resumes
            //and terminates the timer
            timer.cancel();

            //purge is used to remove all cancelled
            //tasks from the timer'stack queue
            System.out.println(timer.purge());
        }

    }
}
//        System.out.println("----------TESTS----------");
//        System.out.println("Expected seconds: 10 -> Result: " + elapsedTime);
//        System.out.println("Expected points: 30 -> Result: " + points);