package nio;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

// Path - позволяет работать с файловой системой
// посмотреть методы

public class NIOFiles {
    public static void main(String[] args) throws IOException {
        File file = new File("nio.txt");
        if (!file.exists()) {
            file.createNewFile();
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 600; i++) {
            stringBuilder.append(i).append("\n");
        }

//        writeToFile(file.toPath(), stringBuilder.toString()); // преобразуем файл к пас и пишем в файл

        readFromFile(file.toPath());

    }

    // пишем в файл

    public static void writeToFile (Path path, String string) throws IOException {
//        try (FileChannel channel = (FileChannel)
//                Files.newByteChannel(path, StandardOpenOption.APPEND)) { // создаем канал, через него можно писать и считывать
//            ByteBuffer buffer = ByteBuffer.allocate(string.getBytes().length); // создаем буфер заданного размера
//            buffer.put(string.getBytes(Charset.forName("utf-8"))); // кладем данные в буфер
//
//            buffer.flip(); // перемещаем позицию в ноль, а лимит в конец данных
//
//            int written = channel.write(buffer); // берем канал и записываем туда буфер
//            System.out.println("written: " + written + "bytes to file" + path.getFileName());
//
//
//        }

        ////////// 2-й вариант ////////////

        // класс файлс может писать, читать, удалять файлы и многое другое
        Files.write(path, string.getBytes(), StandardOpenOption.APPEND);
    }

    // читаем из файла
    public static void readFromFile (Path path) throws IOException{
        int count = 0;

        try (FileChannel channel = (FileChannel) Files.newByteChannel(path)){
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            do {
                count = channel.read(byteBuffer);
                System.out.println("Прочитано: " + count);

                if (count != -1) {
                    byteBuffer.rewind(); // подготавливаем буфер

                    StringBuilder builder = new StringBuilder();

                    for (int i = 0; i < count; i++) {
                        builder.append(new String(byteBuffer.array()), 0, byteBuffer.remaining());
                    }
                    System.out.println(builder.toString());
                }
            } while (count != -1);

        }

        // данные считаются в лист
        // каждая строчка будет элементом списка
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        for (String s : lines) {
            System.out.println(s);
        }

        //
        Files.lines(path, StandardCharsets.UTF_8).forEach(System.out::println);

        //
//        Files.copy(); // тоже копирование из одного в другой
    }

}

// Channel - то же самое что и стрим
// работает как на вход данных, так и на выход
// все данные из канала поступают в Buffer
// можно из буфера в канал, а потом в файл
// все данные работают через буфер
//
// Buffer  -  имеет размер (capacity), задается сразу и не изменяется
//
// есть position - отвечает за положение курсора в буфере, изначально равна нулю
// когда помещаем данные в буфер, позиция смещается
//
// Limit - не может быть больше размера, устанавливается на ту позицию, где кончаются данные
// чтобы не считывать пустое место
// т.е данные считываются от позиции до лимита

// с позиции начинается чтение и запись данных

// методы:

// flip - ставит лимит на место текущей позиции, а позицию в ноль

// rewind - для повторного считывания из буфера
// просто поднимает позицию на ноль

// clear - если данные считали и хотим записать новые
// очищает данные, будут записаны новые

// у каналов есть методы //////////////
// transfer to - копирование файла из одного канала в другой

// selector - позволяет переключаться между каналами
// селектор нужно подписывать на каналы
// реагирует на события
//
// Когда работаем в нио с каналами и селектором, то всегда нужно разблокировать каналы .configureBlocking(false)

////// методы буфера /////////////
// limit() - лимит буфера, вернет текущую позицию
// limit(new Limit)  - установить на другую позицию
// position - --//--
//
