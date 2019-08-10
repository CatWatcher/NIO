package kr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

public class ThreadsWriteToFile {
    public static void main(String[] args) {

        ArrayBlockingQueue<MyThread> myThreads = new ArrayBlockingQueue<>(10, true);

        MyThread myThread1 = new MyThread();
        MyThread myThread2 = new MyThread();
        MyThread myThread3 = new MyThread();
        MyThread myThread4 = new MyThread();

        myThread1.start();
        myThread2.start();
        myThread3.start();
        myThread4.start();
    }
}

class WriteThread extends Thread {
    File file = new File("file");
    FileWriter fileWriter;

    {
        try {
            fileWriter = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}

class MyThread extends Thread {
    File file = new File("file");
    FileWriter fileWriter;

    boolean flag = true;

    {
        try {
            fileWriter = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayBlockingQueue<MyThread> myThreads;




    @Override
    public void run() {
        while (flag) {
            try {
                for (int i = 0; i < 5; i++) {
                    String s = Thread.currentThread().getName() + " feed " + i + "tanuki \n";
                    fileWriter.write(s);
                    Thread.sleep(100);
                    if (i == 4) {
                        System.out.println(Thread.currentThread().getName() + " feeded " + i + " tanuki \n");
                        fileWriter.flush();
                        flag = false;

                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
