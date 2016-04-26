package impulsexchangeclient.firebird;

import impulsexchangeclient.entities.ConstructionEntity;
import impulsexchangeclient.options.Options;
import impulsexchangeclient.entities.OrderEntity;
import impulsexchangeclient.common.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
                JOptionPane.showMessageDialog(null, "Ошибка при работе с базой данных FireBird. \r\n"
                        + "ex: " + ex, this.getClass().getName() + " : run()", JOptionPane.ERROR_MESSAGE);
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
                        this.getClass().getName() + " : extractGeneralData()", JOptionPane.ERROR_MESSAGE);
                return false;
            default:
                JOptionPane.showMessageDialog(null, "Найдено сразу " + size + " заказа с номером <" + Options.getDepartmentName() + "/"
                        + orderName + "> в базе данных! \r\n" + "Пожалуйста создайте новый заказ в программе СуперОкна.",
                        this.getClass().getName() + " : extractGeneralData()", JOptionPane.ERROR_MESSAGE);
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

        constructionsList.clear();
        while (rs.next()) {
            int QTY = rs.getInt("QTY");
            int ORDNO = rs.getInt("ORDNO");
            //Blob scheme = rs.getBlob("SCHEME");
            constructionsCount += QTY;

            InputStream is = rs.getBinaryStream("SCHEME");
            File scheme = readScheme(is);
            constructionsList.add(new ConstructionEntity(ORDNO, QTY, scheme));
        }
        entity.setConstructionsCount(constructionsCount);
        entity.setConstructionsList(constructionsList);
    }

    /**
     * Запуск CapacityCalculator, для вычисления нагрузки по заказу, на основе
     * данных о конструкциях.
     */
    private void exctractCapacity() throws SQLException {
        CapacityCalculator calc = new CapacityCalculator(statement);
        int capacity = calc.calculate(entity.getInvno(), constructionsList);
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

    private File readScheme(InputStream is) {
        FileOutputStream out = null;
        try {
            if (is != null) {
                File scheme = File.createTempFile("scheme", ".emf");
                out = new FileOutputStream(scheme);
                byte[] buffer = new byte[1];
                while (is.read(buffer) > 0) {
                    out.write(buffer);
                }
                return scheme;
            } else {
                return null;
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Произошла ошибка при извлечении данных о конструкциях. \r\n"
                    + "ex: " + ex, this.getClass().getName() + " : readScheme()", JOptionPane.ERROR_MESSAGE);
            return null;
        } finally {
            Service.streamClose(out);
        }
    }

    private final String orderName;
    private Statement statement;
    private OrderEntity entity;
    private final List<ConstructionEntity> constructionsList = new ArrayList<>();
}
