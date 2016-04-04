package impulsexchangeclient.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class Service {

    /**
     * Определение количества дней, необходимых для производства заказа, на
     * основе его нагрузки (capacity).
     *
     * @param capacity - Нагрузка, выраженная в минутах, при условии, что длина
     * рабочего дня - 720 минут (Service.MAX_DAILY_CAPACITY).
     * @return int - количество дней, необходимых для производства заказа.
     * Учитывается только время его производства рабочими в цеху. (т.е. время,
     * необходимое на закупку материалов и т.д. не учитывается)
     */
    public static int extractOrderReqDays(int capacity) {
        int orderReqDays = Math.round((float) Math.ceil(
                (double) capacity / (double) Service.MAX_DAILY_CAPACITY));
        return orderReqDays;
    }

    /**
     * Преобразуем дату (calendar) в формат необходимый для SQL-запросов.
     *
     * @param c Дата, представленная, как объект Calendar.
     * @return String - строка вида: "гггг-мм-дд". Например, для даты '23 ноября
     * 2015 года' будет возвращена строка: "2015-11-23".
     */
    public static String convertToSqlDate(Calendar c) {
        return new Formatter().format("%1$tY-%1$tm-%1$td", c).toString();
    }

    /**
     * Вычисляет остаток свободного времени в указанный день у рабочих цеха.
     *
     * @param datap Дата производства, по которой и будет вычисляться остаток.
     * @param capacitiesMap Таблица нагрузок за весь необходимый для вычислений
     * период (30 дней + orderReqDays)
     * @return int - остаток свободного времени у рабочих за указанный день.
     * Означает, что на такое количество минут еще можно нагрузить цех.
     */
    public static int getDailyBalance(String datap, Map<String, Integer> capacitiesMap) {
        int dailyBalance = Service.MAX_DAILY_CAPACITY;
        if (capacitiesMap.containsKey(datap)) {
            dailyBalance -= capacitiesMap.get(datap);
        }
        return dailyBalance;
    }

    /**
     * Данная функция используется для хоть какого-то шифрования важных настроек
     * программы.
     *
     * @param normalText Строка, которая будет зашифрована.
     * @return String - зашифрованная строка.
     */
    public static String encode(String normalText) {
        char[] encodedChars = new char[normalText.length()];
        for (int i = 0; i < normalText.length(); i++) {
            encodedChars[i] = (char) ((byte) normalText.charAt(i) + 5);
        }
        return String.valueOf(encodedChars);
    }

    /**
     * Данная функция используется для расшифровки настроек, хранящихся в XML.
     *
     * @param xmlText Строка, которая будет расшифрована.
     * @return String - расшифрованная строка.
     */
    public static String decode(String xmlText) {
        char[] decodedChars = new char[xmlText.length()];
        for (int i = 0; i < xmlText.length(); i++) {
            decodedChars[i] = (char) ((byte) xmlText.charAt(i) - 5);
        }
        return String.valueOf(decodedChars);
    }

    public static void streamClose(InputStream in) {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException ex) {
            showExceptionDlg(ex);
        }
    }

    public static void streamClose(OutputStream out) {
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException ex) {
            showExceptionDlg(ex);
        }
    }

    public static void streamClose(BufferedReader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ex) {
            showExceptionDlg(ex);
        }
    }

    private static void showExceptionDlg(IOException ex) {
        JOptionPane.showMessageDialog(null, "Ошибка освобождения ресурсов (stream.close()). \r\n"
                + "ex: " + ex, "Service : StreamClose", JOptionPane.ERROR_MESSAGE);
    }

    /**
     *
     * @param order строка типа: <номер заказа=дата время;>
     * @param group 1 - вернет номер заказа, 2 - вернет дату + время
     * @return подстроку соответствующую параметру group
     */
    public static String extractOrderParam(String order, int group) {
        String orderName = "null";
        Pattern p = Pattern.compile("(\\d+)=(.+)");
        Matcher m = p.matcher(order);
        if (m.matches()) {
            orderName = m.group(group);
        } else {
            JOptionPane.showMessageDialog(null, "Строка не соответствует регулярному выражению <" + order + ">",
                    "Service : extractOrderParam", JOptionPane.ERROR_MESSAGE);
        }
        return orderName;
    }

    /**
     * Минимальное количество дней, необходимое для производства заказа.
     */
    public static final int MINIMAL_DATE_OFFSET = 3;
    /**
     * Длина списка для выбора даты монтажа.
     */
    public static final int MONTH_DURATION = 30;
    /**
     * Максимальная суточная нагрузка. Увеличение данного значения может
     * привести к тому, что рабочие цеха станут не успевать выполнять заказы.
     */
    public static final int MAX_DAILY_CAPACITY = 720;
}
