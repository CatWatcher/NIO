package nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start () {
        // один поток для обработки всех соединений
        new Thread(new NioThread()).start();


    }

    private class NioThread extends Worker {

        private  static final int BUF_SYZE = 1024;

        private ByteBuffer byteBuffer;
        private ServerSocketChannel channel;
        private Selector selector;

        @Override
        protected void init() throws IOException {
            // создаем буфер
            byteBuffer = ByteBuffer.allocate(BUF_SYZE);

            // создаем селектор
            selector = Selector.open();

            // открываем канал сервера
            channel = ServerSocketChannel.open();
            // слушаем порт (входящее соединение на определенном порту)
            channel.bind(new InetSocketAddress(12345));
            // перевод в неблокирующий режим
            // если режим неблокирующий, то второй канал тоже может выполнять действия
            // в блокирующем режиме работает только один канал
            channel.configureBlocking(false);
            // регистрируем канал в селекторе на все допустимые события
            // т.е селектор связывается с этим каналом и следит что там происходит
            // validOps - ?
            channel.register(selector, channel.validOps());
        }

        // теперь нужно описать реакцию
        @Override
        protected void loop() throws Exception {
            // ожидание новых событий
            selector.select();

            // получаем ключи, на которые пришли события
            // используются для определения и работы с событиями
            Set<SelectionKey> keys = selector.selectedKeys();

            //
            Iterator<SelectionKey> iterator = keys.iterator();

            // обрабатываем все ключи
            while (iterator.hasNext()) {
                // берем отдельный ключ
                SelectionKey key = iterator.next();

                // флаг что пришло входящее подключение
                // для каждого отдельного подключения создается свой канал
                if (key.isAcceptable()) {
                    // получение канала
                    // воссоздаем клиентский сокет
                    SocketChannel socketChannel = channel.accept();

                    // переводим канал в неблокирующий режим
                    socketChannel.configureBlocking(false);

                    // регестрируем в селекторе
                    // на получение данных
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    // выводим что за клиент подсоединился
                    System.out.println("Client connected: " + socketChannel.getRemoteAddress());


                }
                // означает что пришли данные для чтения
                else if (key.isReadable()) {
                    // читаем входящие данные
                    System.out.println("Read input data...");
                    readData ((SocketChannel) key.channel());
                }

                // проверяем что соединение открыто (исВалид)
                // и что в канал можно писать данные (исВратибл)
                else if (key.isValid() && key.isWritable()) {
                    // записываем данные в канал
                    System.out.println("Write down data to channel..");
                    writeData ((SocketChannel) key.channel(), key);
                }

                // удаляем отработанный ключ
                iterator.remove();
            }
        }

        @Override
        protected void stop() throws IOException {
            channel.close();
            selector.close();
        }

        private void readData (SocketChannel channel)  {
            // готовим буфер для чтения данных
            // (читаем данные в буфер)
            byteBuffer.clear();

            try {
                // читаем поток байтов
                // (возвращает кол-во считаных байтов)
                // и записываем в буфер
                int read = channel.read(byteBuffer);

                // если с потоком что-то случилось, то переменной read присвоится -1
                if (read != -1) {
                    System.out.println(new String(byteBuffer.array(), 0,
                            byteBuffer.position(), Charset.forName("utf-8")));
                } else {
                    System.out.println("Client disconnected..." + channel.getRemoteAddress());
                    channel.close();
                }

                // готовим буфер для записи
                byteBuffer.flip();

                // нужно разослать сообщение всем клиентам
                Set<SelectionKey> keys = selector.keys();

                // для каждого канала получаем ключ
                // и проверяем можно ли туда писать
                for (SelectionKey selectionKey : keys) {
                    // проверяем доступна ли опция записи
                    if ((selectionKey.channel().validOps() & selectionKey.OP_WRITE) > 0) {
                        // тут мы ставим флаг на запись
                        // если его не снять, то он будет писать и писать ещё
                        selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
                    }
                }
            } catch (IOException e) {
                System.out.println("Client was disconnected... ");
                byteBuffer.clear();
                try {
                    channel.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void writeData (SocketChannel channel, SelectionKey key) throws IOException {
            // записываем данные из буфера
            channel.write(byteBuffer);
            // готовим для следущей записи
            byteBuffer.rewind();

            // снимаем флаг
            // &~ - побитовое нет
            key.interestOps(key.interestOps() & ~ SelectionKey.OP_WRITE);
        }
    }
}


