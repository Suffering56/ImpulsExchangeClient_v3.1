package impulsexchangeclient.firebird;

import impulsexchangeclient.options.Options;
import impulsexchangeclient.common.OrderEntity;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class FirebirdExtractor {

    public FirebirdExtractor(String orderName) {
        this.orderName = orderName;
    }

    /**
     * Основная функция. Запускает функции для извлечения всех необходимых
     * данных о заказе из БД СуперОкна (Firebird)
     *
     * @return Объект класса OrderEntity, в котором содержится вся полученная из
     * БД информация.
     */
    public OrderEntity run() {
        FirebirdConnector fbInstance = FirebirdConnector.getInstance();
        Connection connection = fbInstance.connect();
        if (connection != null) {
            try {
                statement = connection.createStatement();
                if (extractGeneralData()) {             //Получаем информацию о заказе. Затем... если такой существует:
                    extractClientData();                //Получаем инфмормацию о клиенте
                    extractAdditionalData();            //Получаем информацию о доп. работах
                    extractConstructionsData();         //Подсчет количества конструкций
                    exctractCapacity();                 //Вычисляем производственную нагрузку
                }
            } catch (SQLException ex) {
                entity = null;
                JOptionPane.showMessageDialog(null, "Ошибка при работе с базой данных Firebird. \r\n"
                        + "ex.toString(): " + ex, "FirebirdExtractor.extractData()", JOptionPane.ERROR_MESSAGE);
            } finally {
                fbInstance.disconnect();
            }
        }
        return entity;
    }

    /**
     * Извлекаем основную информацию о заказе, содержащуюся в таблице INVOICES.
     *
     * @return boolean: true - если все запросы были выполнены успешно и можно
     * дальше продолжать отправлять запросы к БД. false - если возникли ошибки
     * при выполнении запросов и необходимо запретить выполнение всех остальных
     * запросов к БД, т.к. они обязательно приведут к дополнительным ошибкам.
     */
    private boolean extractGeneralData() throws SQLException {
        int size = getResultSetSize();      //Проверяем наличие данного заказа в БД
        switch (size) {
            case 1:
                //Получаем основную информацию о заказе
                ResultSet rs = statement.executeQuery("SELECT * FROM INVOICES where INVN = " + orderName
                        + " AND DEPNO  = " + Options.getDepartmentName());
                while (rs.next()) {
                    entity = new OrderEntity(
                            Options.getDepartmentName() + "/" + orderName,
                            rs.getInt("INVNO"),
                            rs.getInt("CLNUM"));
                    entity.setMaster(rs.getString("ZMRNAME"));
                }
                return true;
            case 0:
                JOptionPane.showMessageDialog(null, "Заказ № <" + Options.getDepartmentName() + "/" + orderName + "> не найден в базе данных СуперОкна!",
                        "FirebirdDataLoader.extractGeneralData()", JOptionPane.INFORMATION_MESSAGE);
                return false;
            default:
                JOptionPane.showMessageDialog(null, "Найдено сразу " + size + " заказа с номером <" + Options.getDepartmentName() + "/" + orderName + "> в базе данных! \r\n"
                        + "Пожалуйста создайте новый заказ в программе СуперОкна.",
                        "FirebirdDataLoader.extractGeneralData()", JOptionPane.INFORMATION_MESSAGE);
                return false;
        }
    }

    /**
     * Получаем данные о клиенте (ФИО, Адрес и Контактные данные).
     */
    private void extractClientData() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM CLIENTS where CLNUM = " + entity.getClnum()
                + " AND CLDEP  = " + Options.getDepartmentName());
        while (rs.next()) {
            entity.setClient(rs.getString("CLNAME"));
            entity.setAddress(rs.getString("CLADDRESS"));
            entity.setContacts(rs.getString("CLPHONE"));
        }
    }

    /**
     * Получаем данные о дополнительных работах (монтаж, демонтаж, доставка).
     */
    private void extractAdditionalData() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM DOPWORK where INVNO = " + entity.getInvno()
                + " AND DEPNO  = " + Options.getDepartmentName());
        while (rs.next()) {
            switch (rs.getInt("WRKNO")) {
                case 4:
                    entity.setDismantling(1);    //демонтаж
                    break;
                case 5:
                    entity.setMounting(1);       //монтаж
                    break;
                case 6:
                    entity.setDelivery(1);       //доставка
                    break;
            }
        }
    }

    /**
     * Получаем данные о конструкциях (их идентификаторы и количество,
     * необходимые для дальнейшего подсчета нагрузки).
     */
    private void extractConstructionsData() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM INVSPEC where INVNO = " + entity.getInvno()
                + " AND DEPNO  = " + Options.getDepartmentName());
        int constructionsCount = 0;
        ordnoMap.clear();
        while (rs.next()) {
            int QTY = rs.getInt("QTY");
            constructionsCount += QTY;
            ordnoMap.put(rs.getInt("ORDNO"), QTY);  //key = ORDNO, value = QTY
        }
        entity.setConstructionsCount(constructionsCount);
    }

    /**
     * Запуск CapacityCalculator, для вычисления нагрузки по заказу, на основе
     * данных о конструкциях.
     */
    private void exctractCapacity() throws SQLException {
        CapacityCalculator calc = new CapacityCalculator(statement);
        int capacity = calc.calculate(entity.getInvno(), ordnoMap);
        entity.setCapacity(capacity);
    }

    /**
     * Выполнение запроса, на определение есть ли вообще такой заказ в отгрузке.
     *
     * @return int - количество строк, возвращенных в результате
     * соответствующего запроса. 0 - означает, что такой заказ не был найден. 1
     * - что такой заказ был найден. 2 и больше - такого не может быть,
     * обратитесь к психиатору.
     */
    private int getResultSetSize() throws SQLException {
        int size = 0;
        ResultSet rs = statement.executeQuery("SELECT count(*) FROM INVOICES where INVN = " + orderName
                + " AND DEPNO  = " + Options.getDepartmentName());
        while (rs.next()) {
            size = rs.getInt("COUNT");
        }
        return size;
    }

    private final String orderName;
    private Statement statement;
    private OrderEntity entity;
    private final Map<Integer, Integer> ordnoMap = new HashMap<>();
}
