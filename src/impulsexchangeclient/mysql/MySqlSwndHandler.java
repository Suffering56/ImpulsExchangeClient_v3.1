package impulsexchangeclient.mysql;

import impulsexchangeclient.common.Service;
import impulsexchangeclient.options.Options;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Formatter;

public class MySqlSwndHandler {

    public MySqlSwndHandler(Statement stmt) {
        this.stmt = stmt;
    }

    public String getNewOrdersString(String orderName) throws SQLException {
        String oldOrdersString = null;
        ResultSet rs = stmt.executeQuery(
                "SELECT `orders` FROM `exchange_swnd` WHERE `department_id` = '" + Options.getDepartmentName() + "'");
        while (rs.next()) {
            oldOrdersString = rs.getString("orders");
        }

        String currentTime = getCurrentTimeString();
        String newOrdersString = orderName + "=" + currentTime + ";";
        if (oldOrdersString != null) {
            newOrdersString += getUniqueOrdersString(oldOrdersString, orderName);
        }
        return newOrdersString;
    }

    private String getUniqueOrdersString(String oldOrdersString, String orderName) {
        String uniqueOrdersString = "";
        if ((";" + oldOrdersString).contains(";" + orderName + "=")) {
            for (String order : oldOrdersString.split(";")) {
                String item = Service.extractOrderParam(order, 1);
                if (!item.equals(orderName)) {
                    uniqueOrdersString += order + ";";
                }
            }
        } else {
            uniqueOrdersString = oldOrdersString;
        }

        return uniqueOrdersString;
    }

    public String getNewHistoryString(String orderName) throws SQLException {
        String oldHistoryString = null;
        ResultSet rs = stmt.executeQuery(
                "SELECT `history` FROM `exchange_swnd` WHERE `dep_id` = '" + Options.getDepartmentName() + "'");
        while (rs.next()) {
            oldHistoryString = rs.getString("history");
        }

        String currentTime = getCurrentTimeString();
        String newHistoryString = orderName + "=" + currentTime + ";";
        if (oldHistoryString != null) {
            newHistoryString += oldHistoryString;
        }
        return newHistoryString;
    }

    private String getCurrentTimeString() {
        Calendar calendar = Calendar.getInstance();
        String currentTimeString = new Formatter().format("%1$td-%1$tm-%1$tY %1$tH:%1$tM:%1$tS", calendar).toString();
        return currentTimeString;
    }

    private final Statement stmt;
}
