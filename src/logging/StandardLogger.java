package logging;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.logging.*;

public class StandardLogger {
    // стандартный логгер

    // создаем объект логера
    private static final Logger LOGGER = Logger.getLogger(StandardLogger.class.getName());
    static {
        // уровни

        //LOGGER.setLevel(Level.SEVERE);        // исключительно фатальные ошибки

//
//        LOGGER.setLevel(Level.WARNING);         // только предупреждения
//
//        LOGGER.setLevel(Level.INFO);        // стандартные сообщения

        LOGGER.setLevel(Level.ALL);        // пишет всё
//        //
//        LOGGER.setLevel(Level.CONFIG);
//
//        //
//        LOGGER.setLevel(Level.FINE);
//
//        //
//        LOGGER.setLevel(Level.FINER);
//
//        //
//        LOGGER.setLevel(Level.FINEST);

        // чтобы писать в xml файлик
        try {
            LOGGER.addHandler(new FileHandler("loggedExample.log.xml"));

            // вывод в виде обычного текста
            FileHandler fileHandler = new FileHandler("loggedFile.log");
            fileHandler.setFormatter(new CustomFormatter());

            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.warning("FileHandler isn't access");
        }
    }

    public static void main(String[] args) {
        // выводжит стандартные сообщения
        LOGGER.info("Program is started with arguments " + Arrays.toString(args));

        // с ошибкой
        try {
            int res = 2 / 0;
        } catch (Exception e) {
            LOGGER.severe("Fatal error: " + e.getMessage());
        }


    }

}

// создадим свой форматтер
class CustomFormatter extends Formatter {

    // для потокобезопасности
    private final static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
      protected DateFormat initialValue() {
          return new SimpleDateFormat("yyyy-MM-dd : mm:ss");
      }
    };

    @Override
    public String format(LogRecord record) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[').append(formateDate(record.getMillis())).append(']').append('\n');
        stringBuilder.append('[').append(record.getLevel());
        stringBuilder.append('[').append(record.getSourceClassName())
                .append('.').append(record.getSourceMethodName()).append(']').append('\n');
        stringBuilder.append(" - ").append(record.getMessage()).append('\n');
        stringBuilder.append('\n');

        return stringBuilder.toString();
    }

    private  String formateDate (long millis) {
        return dateFormat.get().format(new Date(millis));
    }
}

// посмотреть паттерн проектирования фасад
// догирование в несколько файлов
//