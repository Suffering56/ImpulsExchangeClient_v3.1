package impulsexchangeclient.mysql;

import impulsexchangeclient.common.Service;
import impulsexchangeclient.options.Options;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Formatter;
import javax.swing.JOptionPane;

public class SwndSender {

    public SwndSender(String orderName) {
        this.orderName = orderName;
        swndFile = new File(Options.getSwndFilePath());
    }

    public boolean run() {
        MySqlConnector mySqlInstance = MySqlConnector.getInstance();
        connection = mySqlInstance.connect();
        if (connection != null) {
            boolean result = initQueries(mySqlInstance);
            return result;
        } else {
            return false;
        }
    }

    private boolean initQueries(MySqlConnector mySqlInstance) {
        FileInputStream in = null;
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            in = new FileInputStream(swndFile);

            exchangeSwndUpdate(in);
            exchangeOrdersUpdate();
            exchangeHistoryUpdate();

            connection.commit();
            return true;

        } catch (SQLException ex) {
            showSqlExceptionDlg(ex);
            rollback();
            return false;
        } catch (FileNotFoundException ex) {
            showFNFException(ex);
            return false;
        } finally {
            setTrueAutoCommit();
            Service.streamClose(in);
            mySqlInstance.disconnect();
        }
    }

    /**
     * Выполняет запросы на обновление таблицы exchange_swnd, содержащей файлы
     * обмена для каждого отдела.
     *
     * @param in - экземпляр InputStream для файла <swnd5.arc>
     * @throws SQLException
     */
    private void exchangeSwndUpdate(InputStream in) throws SQLException {
        PreparedStatement swndPrepStmt;
        if (departmentExistCheck() == true) {
            swndPrepStmt = connection.prepareStatement("UPDATE `exchange_swnd` SET `swnd` = ? "
                    + "WHERE `dep_id` = '" + Options.getDepartmentName() + "'");
            swndPrepStmt.setBinaryStream(1, in, (int) swndFile.length());

        } else {
            swndPrepStmt = connection.prepareStatement("INSERT INTO `exchange_swnd` "
                    + "(`dep_id`, `swnd`) VALUES(?, ?)");
            swndPrepStmt.setInt(1, Integer.valueOf(Options.getDepartmentName()));
            swndPrepStmt.setBinaryStream(2, in, (int) swndFile.length());

        }
        swndPrepStmt.executeUpdate();
    }

    /**
     * Выполняет запросы на обновление таблицы exchange_orders, содержащей
     * данные о заказах, которые были отправлены клиентами (дилерами), но еще не
     * приняты сервером.
     *
     * @throws SQLException
     */
    private void exchangeOrdersUpdate() throws SQLException {
        if (orderExistCheck() == true) {
            PreparedStatement orderDeletePrepStmt = connection.prepareStatement("DELETE FROM `exchange_orders` WHERE `dep_id` = ? AND `order_name` = ?");
            orderDeletePrepStmt.setInt(1, Integer.valueOf(Options.getDepartmentName()));
            orderDeletePrepStmt.setInt(2, Integer.valueOf(orderName));
            orderDeletePrepStmt.executeUpdate();
        }

        PreparedStatement orderInsertPrepStmt = connection.prepareStatement("INSERT INTO `exchange_orders` "
                + "(`dep_id`, `order_name`, `time`) VALUES (?, ?, ?)");
        orderInsertPrepStmt.setInt(1, Integer.valueOf(Options.getDepartmentName()));
        orderInsertPrepStmt.setInt(2, Integer.valueOf(orderName));
        orderInsertPrepStmt.setString(3, getCurrentTimeString());
        orderInsertPrepStmt.executeUpdate();
    }

    /**
     * Выполняет запросы на обновление таблицы exchange_history, содержащей
     * архив всех заказов, отправленных клиентами.
     *
     * @throws SQLException
     */
    private void exchangeHistoryUpdate() throws SQLException {
        PreparedStatement orderInsertPrepStmt = connection.prepareStatement("INSERT INTO `exchange_history` "
                + "(`dep_id`, `order_name`, `time`) VALUES (?, ?, ?)");
        orderInsertPrepStmt.setInt(1, Integer.valueOf(Options.getDepartmentName()));
        orderInsertPrepStmt.setInt(2, Integer.valueOf(orderName));
        orderInsertPrepStmt.setString(3, getCurrentTimeString());
        orderInsertPrepStmt.executeUpdate();
    }

    private boolean departmentExistCheck() throws SQLException {
        ResultSet rs = stmt.executeQuery(
                "SELECT `dep_id` FROM `exchange_swnd` WHERE `dep_id` = '" + Options.getDepartmentName() + "'");
        int size = 0;
        while (rs.next()) {
            size++;
        }
        return size > 0;
    }

    private boolean orderExistCheck() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM `exchange_orders` WHERE `dep_id` = '" + Options.getDepartmentName()
                + "' AND `order_name` = '" + orderName + "'");
        int size = 0;
        while (rs.next()) {
            size++;
        }
        return size > 0;
    }

    private String getCurrentTimeString() {
        Calendar calendar = Calendar.getInstance();
        String currentTimeString = new Formatter().format("%1$td-%1$tm-%1$tY %1$tH:%1$tM:%1$tS", calendar).toString();
        return currentTimeString;
    }

    /**
     ***************************************************************************
     ******* Последующие методы используются для обработки исключений. ********
     */
    private void showSqlExceptionDlg(SQLException ex) {
        String errorMsg = "Неизвестная ошибка. Описание:";
        if (ex.toString().contains("max_allowed_packet")) {
            errorMsg = "Превышен максимально допустимый размер файла для хранения в MySQL. \r\n"
                    + "Проверьте путь к файлу обмена (размер не должен превышать 16МБ). \r\n"
                    + "Обратитесь к системному администратору (нужно изменить параметр <max_allowed_packet> в my.ini на <16M>";
        }
        JOptionPane.showMessageDialog(null, "SQLException. Произошла ошибка при отправке файла обмена <" + Options.getSwndFileName() + ">. \r\n"
                + errorMsg + "\r\n" + "ex: " + ex, this.getClass().getName() + " : run()", JOptionPane.ERROR_MESSAGE);
    }

    private void showFNFException(FileNotFoundException ex) {
        String path = Options.getSwndFilePath().replace(Options.getSwndFileName(), "");
        JOptionPane.showMessageDialog(null, "Не найден файл обмена <" + Options.getSwndFileName() + ">. \r\n"
                + "Проверьте наличие файла в директории <" + path + ">. \r\n"
                + "ex: " + ex, this.getClass().getName() + " : run()", JOptionPane.ERROR_MESSAGE);
    }

    private void rollback() {
        try {
            connection.rollback();
        } catch (SQLException exx) {
            JOptionPane.showMessageDialog(null, "Произошла ошибка при откате изменений в MySQL. \r\n"
                    + "ex.: " + exx, this.getClass().getName() + " :connection.rollback()", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setTrueAutoCommit() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Произошла ошибка при установлении параметра AutoCommit(true). \r\n"
                    + "ex: " + ex, this.getClass().getName() + " : AutoCommit(true)", JOptionPane.ERROR_MESSAGE);
        }
    }

    private final String orderName;
    private final File swndFile;
    private Connection connection;
    private Statement stmt;
}
