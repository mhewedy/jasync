package com.github.mhewedy.jasync;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        new Test().invoker();
    }

    private static class Test {

        void invoker() {
            Promise<?> p1 = Task.async(this::callLongRunningFunction1);
            Promise<?> p2 = Task.async(this::callLongRunningFunction2);

            Task.await(p1, p2);
        }

        private void callLongRunningFunction1() {
            System.out.println("sleeping thread1: " + Thread.currentThread());
            int waitTime = sleepRandomBetween(3000, 5000);
            System.out.println("thread1: " + Thread.currentThread() + " waited: " + waitTime);
        }


        private void callLongRunningFunction2() {
            System.out.println("sleeping thread2: " + Thread.currentThread());
            int waitTime = sleepRandomBetween(3000, 5000);
            System.out.println("thread2: " + Thread.currentThread() + " waited: " + waitTime);
        }
    }

    private static int sleepRandomBetween(int low, int high) {
        int waitTime = 0;
        try {
            Random r = new Random();
            waitTime = r.nextInt(high - low) + low;
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return waitTime;
    }
}
