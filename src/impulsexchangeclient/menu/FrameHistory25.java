package impulsexchangeclient.menu;

import impulsexchangeclient.FrameMain;
import impulsexchangeclient.common.Service;
import impulsexchangeclient.mysql.MySqlConnector;
import impulsexchangeclient.options.Options;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class FrameHistory25 extends javax.swing.JFrame {

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jOrdersList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jDateList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Архив (последние 25 заказов)");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jOrdersList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                anyListKeyPressed(evt);
            }
        });
        jOrdersList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jOrdersListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jOrdersList);

        jDateList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                anyListKeyPressed(evt);
            }
        });
        jDateList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jDateListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jDateList);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Номер заказа");
        jLabel1.setFocusable(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Дата");
        jLabel2.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(3, 3, 3))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jScrollPane1});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(3, 3, 3))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public FrameHistory25(FrameMain mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        boolean success = readHistory();
        if (success) {
            mainFrame.setEnabled(false);
        }
        setLocationRelativeTo(null);
        jOrdersList.setModel(ordersList);
        jDateList.setModel(datesList);
    }

    private boolean readHistory() {
        MySqlConnector mySqlInstance = MySqlConnector.getInstance();
        Connection connection = mySqlInstance.connect();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT `history` FROM `exchange` WHERE `department_id` = '" + Options.getDepartmentName() + "'");
            String oldHistoryString = "";
            while (rs.next()) {
                oldHistoryString = rs.getString("history");
                if (rs.getString("history") == null) {
                    oldHistoryString = "";
                }
            }
            ordersList.clear();
            datesList.clear();
            int counter = 0;
            for (String order : oldHistoryString.split(";")) {
                if (counter < 25) {
                    ordersList.addElement(Service.extractOrderParam(order, 1));
                    datesList.addElement(Service.extractOrderParam(order, 2));
                    counter++;
                } else {
                    break;
                }
            }
            return true;

        } catch (SQLException ex) {
            String errorMsg = "Неизвестная ошибка. Описание:";
            JOptionPane.showMessageDialog(null, "SQLException. Произошла ошибка при чтении архива. \r\n"
                    + errorMsg + "\r\n" + "ex: " + ex, this.getClass().getName() + " : readHistory()", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            mySqlInstance.disconnect();
        }
    }

    private void jDateListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jDateListValueChanged
        jOrdersList.setSelectedIndex(jDateList.getSelectedIndex());
    }//GEN-LAST:event_jDateListValueChanged

    private void jOrdersListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jOrdersListValueChanged
        jDateList.setSelectedIndex(jOrdersList.getSelectedIndex());
    }//GEN-LAST:event_jOrdersListValueChanged

    private void anyListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_anyListKeyPressed
        if (evt.getKeyCode() == 27) {
            mainFrame.setEnabled(true);
            this.dispose();
        }
    }//GEN-LAST:event_anyListKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        mainFrame.setEnabled(true);
    }//GEN-LAST:event_formWindowClosing

    private final DefaultListModel ordersList = new DefaultListModel();
    private final DefaultListModel datesList = new DefaultListModel();
    private final FrameMain mainFrame;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jDateList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jOrdersList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
