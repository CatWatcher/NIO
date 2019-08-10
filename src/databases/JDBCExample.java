package databases;

import java.sql.*;

public class JDBCExample {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        createTable();

//        insertIntoBook("Faust", 342);
//        insertIntoBook("Faust", 342);
//        insertIntoBook("Faust", 342);
//        insertIntoBook("Faust", 342);

        selectAll();
    }

    // регистрируем драйвер
    // запрос на сервер
    // создаем таблицу
    public static void createTable() throws ClassNotFoundException, SQLException {
        // запрос
        String sqlCreate = "CREATE TABLE IF NOT EXISTS Book (\n" +
                "id SERIAL PRIMARY KEY, \n" +
                "title TEXT NOT NULL, \n" +
                "pageCount INTEGER NOT NULL\n" +
                ");";

        Class.forName("org.postgresql.Driver");

        // выполняем подключение
        // в гет конекшн передаем данные о нашей базе
        try (Connection connection = DriverManager
                // по хорошему юрл, имя и пароль лучше пихать в проперти файл или ещё куда-то
                .getConnection("jdbc:postgresql://localhost:5432/akio889DB", "akio889", "дущтфквщ09")) {
            try (Statement statement = connection.createStatement()) {
                int res = statement.executeUpdate(sqlCreate);
                System.out.println(res);

//                statement.executeUpdate(); // для создания и изменения таблицы (вернет число измененных строк)
//                statement.executeQuery(); // используется для получения каких-то данных (вернет данные)
            }

        }

    }

    // вставка данных
    public static void insertIntoBook(String title, int pageCount) throws SQLException, ClassNotFoundException {
        // для запросов где данные вставляются через переменные
        String sqlInsert = "INSERT INTO Book (title, pageCount) VALUES (?, ?);";

        Class.forName("org.postgresql.Driver");

        try (Connection connection = DriverManager
                // по хорошему юрл, имя и пароль лучше пихать в проперти файл или ещё куда-то
                .getConnection("jdbc:postgresql://localhost:5432/akio889DB", "akio889", "дущтфквщ09")) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
                preparedStatement.setString(1, title);
                preparedStatement.setInt(2, pageCount);
                int res = preparedStatement.executeUpdate();


            }

        }
    }

    // получение данных
    public static void selectAll() throws SQLException, ClassNotFoundException {
        String sqlSelectAll = "SELECT * FROM Book";

        Class.forName("org.postgresql.Driver");

        try (Connection connection = DriverManager
                // по хорошему юрл, имя и пароль лучше пихать в проперти файл или ещё куда-то
                .getConnection("jdbc:postgresql://localhost:5432/akio889DB", "akio889", "дущтфквщ09")) {
            try (Statement statement = connection.createStatement()) {

                ResultSet resultSet = statement.executeQuery(sqlSelectAll);
                // достаем данные из резалтсета
                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    int pageCount = resultSet.getInt("pageCount");
                    System.out.println("title: " + title);
                    System.out.println("page count: " + pageCount);
                    System.out.println();
                }

            }

        }
    }
}
