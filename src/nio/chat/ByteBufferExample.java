package nio.chat;

import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ByteBufferExample {
    public static void main(String[] args) {
        // размер буфера изменить нельзя
        ByteBuffer buffer = ByteBuffer.allocate(16);

        assert buffer.position() == 0; // assert - проверяет выполняется ли утверждение
                                        // обязательно удалить это из продакшн версии
                                        // в конфигурациях VM option к файлу ставим флаг -ea

        buffer.putInt(100); // добавляем в буфер, позиция переместилась на 4
                            // int занимает 4 байта, значит и позиция смещается на 4

        buffer.putDouble(100.25); // позиция на 12

        buffer.flip();

        int someInt = buffer.getInt(); // позиция при чтении смещается так же, как при записи
        double someDouble = buffer.getDouble();
        System.out.println(someInt);
        System.out.println(someDouble);

    }
}
