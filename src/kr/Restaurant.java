package kr;

import java.util.concurrent.ArrayBlockingQueue;

public class Restaurant {
    public static void main(String[] args) {
        ArrayBlockingQueue<Visitors> ticketWindow1 = new ArrayBlockingQueue<>(3, true);
        ArrayBlockingQueue<Visitors> ticketWindow2 = new ArrayBlockingQueue<>(3, true);
        ArrayBlockingQueue<Visitors> ticketWindow3 = new ArrayBlockingQueue<>(3, true);

    }
}

class Visitors extends Thread {

    @Override
    public void run() {

    }
}
