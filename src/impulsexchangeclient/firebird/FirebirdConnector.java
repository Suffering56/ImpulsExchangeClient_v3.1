package impulsexchangeclient.firebird;

import impulsexchangeclient.options.Options;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

public class FirebirdConnector {

    private FirebirdConnector() {
        //private!
    }

    /**
     * SingleTone.
     *
     * @return Новый экземпляр FirebirdConnector при первом вызове данного
     * метода. При повторном вызове будет вовзращен уже существующий экземпляр.
     */
    public static FirebirdConnector getInstance() {
        if (instance == null) {
            instance = new FirebirdConnector();
        }
        return instance;
    }

    /**
     * Попытка установления соединения с БД Firebird. В случае ошибок на экране
     * появится информационное окно, с сообщением об ошибке и дополнительных
     * рекомендациях.
     *
     * @return объект Connection в случае успешного установления соединения.
     * null - в случае возникновения каких-либо ошибок.
     */
    public Connection connect() {
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Ошибка при загрузке драйвера (Class.forName). \r\n"
                    + "ex: " + ex, this.getClass().getName() + " : connect()", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Properties properties = new Properties();
        properties.setProperty("user", Options.getFirebirdUser());
        properties.setProperty("password", Options.getFirebirdPassword());
        properties.setProperty("charSet", Options.getFirebirdEncoding());
        String url = "jdbc:firebirdsql:localhost/3050:" + Options.getFirebirdDatabasePath();
        try {
            connection = DriverManager.getConnection(url, properties);
        } catch (SQLException | RuntimeException ex) {
            disconnect();
            String errorMsg = "Неизвестная ошибка.";

            if (ex.toString().contains("335544344")) {
                errorMsg = "Возник конфликт с программой <СуперОкна>, или не найден файл базы данных.\r\n"
                        + "ЧТОБЫ ИСПРАВИТЬ ОШИБКУ, ПОЖАЛУЙСТА ЗАКРОЙТЕ ПРОГРАММУ <СуперОкна>.\r\n"
                        + "Либо укажите верный путь к базе в настройках программы.";
            } else if (ex.toString().contains("335544721")) {
                errorMsg = "Сервер не найден. Запустите/перезапустите Firebird-server.";
            } else if (ex.toString().contains("335544472")) {
                errorMsg = "Неверный логин и/или пароль. Проверьте настройки программы..";
            } else if (ex.toString().contains("335544323")) {
                errorMsg = "Указанный файл не является базой данных Firebird, либо поврежден. \r\n";
            } else if (ex.toString().contains("RuntimeException")) {
                errorMsg = "Указана неверная кодировка. Проверьте настройки Firebird.";
            }
            JOptionPane.showMessageDialog(null, errorMsg + "\r\n"
                    + "ex: " + ex, this.getClass().getName() + " : connect()", JOptionPane.ERROR_MESSAGE);
        }
        return connection;
    }

    /**
     * Попытка корректного закрытия соединения с БД Firebird. В случае ошибки
     * будет отображено соответствующее сообщение. В любом случае переменной
     * connection будет присвоено значение null.
     *
     * @return
     */
    public boolean disconnect() {
        boolean result = false;
        try {
            if (connection != null) {
                connection.close();
            }
            result = true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ошибка при закрытии соединения: <connection.close()>. \r\n"
                    + "ex: " + ex, this.getClass().getName() + " : disconnect()", JOptionPane.ERROR_MESSAGE);
        } finally {
            connection = null;
        }
        return result;
    }

    private static FirebirdConnector instance = null;
    private Connection connection;
}
