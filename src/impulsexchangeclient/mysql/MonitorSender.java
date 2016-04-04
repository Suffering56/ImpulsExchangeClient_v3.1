package impulsexchangeclient.mysql;

import impulsexchangeclient.common.ConstructionEntity;
import impulsexchangeclient.common.OrderEntity;
import impulsexchangeclient.common.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class MonitorSender {

    public MonitorSender(OrderEntity entity) {
        this.entity = entity;
    }

    /**
     * Запуск процесса отправки введенного пользователем заказа в отгрузку.
     *
     * @return boolean: true - если процесс экспорта был успешно завершен. false
     * - если во время экспорта возникли какие-либо ошибки.
     */
    public boolean run() {
        boolean result = false;
        MySqlConnector mySqlInstance = MySqlConnector.getInstance();
        connection = mySqlInstance.connect();
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
                statement = connection.createStatement();
                executeGeneralTransaction();
                connection.commit();
                insertConstructionsData();
                connection.commit();
                result = true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ошибка при работе с базой данных MySQL. \r\n"
                        + "ex: " + ex, this.getClass().getName() + " : run()", JOptionPane.ERROR_MESSAGE);
                try {
                    connection.rollback();
                } catch (SQLException exx) {
                    JOptionPane.showMessageDialog(null, "Произошла ошибка при откате изменений в MySQL. \r\n"
                            + "ex: " + exx, this.getClass().getName() + " : rollback()", JOptionPane.ERROR_MESSAGE);
                }
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Произошла ошибка при импорте схемы конструкций. \r\n"
                        + "Не найден временный файл схемы. \r\n" + "ex: " + ex,
                        this.getClass().getName() + " : insertConstructionsData()", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Произошла ошибка при установлении параметра AutoCommit(true). \r\n"
                            + "ex: " + ex, this.getClass().getName() + " : setAutoCommit(true)", JOptionPane.ERROR_MESSAGE);
                }
                mySqlInstance.disconnect();
            }
        }
        return result;
    }

    /**
     * Перебор заказов, и подготовка соответствующих запросов к базе данных
     * отгрузочной программы (MySQL)
     */
    private void executeGeneralTransaction() throws SQLException {
        String fOrder = entity.getFullOrderName();
        if (!orderExistenceCheck(fOrder)) {                         //если такого заказа НЕТ В ОТГРУЗКЕ
            queriesPreparation(entity);                             //!!!
        } else if (showConfirmDlg(fOrder) == 0) {                   //если была нажата кнопка "Заменить"
            replaceOrder(entity);
        }
    }

    /**
     * Инициализация и запуск запросов к отгрузочной программе, для экспорта
     * информации о заказах. Если заказ - simple, то будет выполнен один запрос
     * в БД, если заказ - difficult, то - несколько(зависит от сложности заказа)
     *
     * @param entity Объект класса OrderEntity содержащий всю необходимую
     * информацию об экспортируемом заказе.
     */
    private void queriesPreparation(OrderEntity entity) throws SQLException {
        if (entity.getDifficultOrderMap().isEmpty()) {
            executeMainQuery(entity, entity.getFullOrderName(), entity.getProductionDate(), entity.getCapacity());
        } else {
            int counter = 0;
            String nz;
            for (String datap : entity.getDifficultOrderMap().keySet()) {
                int nagruz = entity.getDifficultOrderMap().get(datap);
                if (counter == 0) {
                    nz = entity.getFullOrderName();
                } else {
                    nz = "_" + entity.getFullOrderName();
                }
                executeMainQuery(entity, nz, datap, nagruz);
                counter++;
            }
        }
    }

    /**
     * Замена уже существующего в отгрузке заказа новым, в случае совпадения их
     * имен.
     *
     * @param entity Объект класса OrderEntity содержащий всю необходимую
     * информацию об экспортируемом заказе.
     */
    private void replaceOrder(OrderEntity entity) throws SQLException {
        String fOrder = entity.getFullOrderName();
        int deleteResult = statement.executeUpdate("DELETE FROM `jos_zakazi` "
                + "WHERE `nz` LIKE '" + fOrder + "'");                          //удаляем старый заказ
        deleteResult += statement.executeUpdate("DELETE FROM `jos_zakazi` "
                + "WHERE `nz` LIKE '" + "_" + fOrder + "'");                    //удаляем дополнительные заказы (если заказ difficult)
        if (deleteResult > 0) {
            queriesPreparation(entity);                                         //добавляем новый заказ в отгрузку!!!
        } else {                                                                //если заказ почему-то не удалился
            JOptionPane.showMessageDialog(null, "Произошла ошибка при удалении заказа №"
                    + fOrder + ">\r\n" + "Заказ остался в отгрузочной программе без изменений",
                    this.getClass().getName() + " : replaceOrder()", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Инициализация и выполнение запроса на добавление нового заказа в
     * отгрузку.
     *
     * @param entity Объект класса OrderEntity содержащий всю необходимую
     * информацию об экспортируемом заказе.
     * @param nz номер заказа (для сложных заказов в начало номера заказа будет
     * добавлен символ "_" - что означает, что они - части одного большого
     * заказа, и не являются самостоятельными)
     * @param datap дата произвоодства
     * @param nagruz - нагрузка
     * @throws SQLException
     */
    private void executeMainQuery(OrderEntity entity, String nz, String datap, int nagruz) throws SQLException {
        String query = "INSERT INTO `jos_zakazi` "
                + "(`idz`, `nz`, `klient`, `adr`,"
                + " `kolkon`, `dost`, `demont`, `mont`,"
                + " `nagruz`, `zamer`, `brig`, `sbor`,"
                + " `datap`, `datam`, `kont`, `g`,"
                + " `s`, `musor`, `lm`, `komkol`, `kom`"
                + ") VALUES ("
                + "NULL, '" + nz + SEPARATOR + entity.getClient() + SEPARATOR + entity.getAddress() + SEPARATOR
                + entity.getConstructionsCount() + SEPARATOR + entity.getDelivery() + SEPARATOR + entity.getDismantling() + SEPARATOR + entity.getMounting() + SEPARATOR
                + nagruz + SEPARATOR + entity.getMaster() + SEPARATOR + "1" + SEPARATOR + "1" + SEPARATOR
                + datap + SEPARATOR + entity.getMountingDate() + SEPARATOR + entity.getContacts() + SEPARATOR + "0" + SEPARATOR
                + "0" + SEPARATOR + entity.getGarbage() + SEPARATOR + entity.getLamination() + SEPARATOR + entity.getDescription() + SEPARATOR + entity.getComment() + "')";

        PreparedStatement prepStmt = connection.prepareStatement(query);
        prepStmt.executeUpdate();
    }

    private void insertConstructionsData() throws SQLException, FileNotFoundException {
        String dep_id = entity.getFullOrderName().split("/")[0];
        String order_name = entity.getFullOrderName().split("/")[1];

        connection.prepareStatement("DELETE FROM `exchange_constructions` "
                + "WHERE `dep_id` = '" + dep_id + "' AND `order_name` = '" + order_name + "'").executeUpdate();

        String query = "INSERT INTO `exchange_constructions` "
                + "(`dep_id`, `order_name`, `constr_ordno`, `constr_qty`, `constr_scheme`) "
                + "VALUES('" + dep_id + "', '" + order_name + "', ?, ?, ?)";
        PreparedStatement insertPrepStmt = connection.prepareStatement(query);

        for (ConstructionEntity construction : entity.getConstructionsList()) {
            File scheme = construction.getScheme();
            FileInputStream fin = new FileInputStream(scheme);

            insertPrepStmt.setInt(1, construction.getOrdno());
            insertPrepStmt.setInt(2, construction.getQty());
            insertPrepStmt.setBinaryStream(3, fin, (int) scheme.length());

            insertPrepStmt.executeUpdate();
            insertPrepStmt.clearParameters();
            Service.streamClose(fin);
            scheme.delete();
        }
    }

    /**
     * Проверяет есть ли такой заказ в отгрузочной программе.
     *
     * @param fullOrderName номер заказа.
     * @return boolean: true - если такой заказ уже есть в отгрузке и необходимо
     * что-то предпринять. false - в случае если такого заказа нет, и можно
     * спокойно его экспортировать.
     */
    private boolean orderExistenceCheck(String fullOrderName) throws SQLException {
        String query = "SELECT count(*) FROM `jos_zakazi` WHERE `nz` LIKE '" + fullOrderName + "'";
        ResultSet rs = statement.executeQuery(query);
        int count = 0;
        while (rs.next()) {
            count = rs.getInt("count(*)");
        }
        return count > 0;
    }

    /**
     * Создание и отображение диалогового окна с надписью "Такой заказ уже есть
     * в отгрузочной программе" и предложением выбора дальнейших действий.
     *
     * @param fOrder номер заказа.
     * @return int: '0' - если была нажата кнопка "Заменить". '1' - если была
     * нажата кнопка "Отмена. '-1' - если был нажат красный крестик в правом
     * верхнем углу.
     */
    private int showConfirmDlg(String fOrder) {
        return JOptionPane.showOptionDialog(null, "Заказ №" + fOrder + "> уже есть в отгрузочной программе!\r\n"
                + "Заменить его?", "Предупреждение!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                null, new Object[]{"Заменить", "Отмена"}, "Заменить");
    }

    private final OrderEntity entity;
    private Connection connection;
    private Statement statement;
    private static final String SEPARATOR = "', '";
}
