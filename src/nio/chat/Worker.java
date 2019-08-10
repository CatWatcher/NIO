package nio.chat;

public abstract class Worker implements Runnable {


    @Override
    public void run() {
        try {
            init();

            while (!isInterrupted()) {
                loop();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stop();
            } catch (Exception e) {
                // игнорируем эксепшн
                // можно при желании записать в лог
            }
        }
    }

    // тут будем инициализировать все необходимое
    protected void init () throws Exception {}

    // закрытие всех соединений
    protected void stop () throws Exception {}

    // основной функционадл будет тут
    protected void loop () throws Exception {}

    protected boolean isInterrupted () {
        return Thread.currentThread().isInterrupted();
    }


}
