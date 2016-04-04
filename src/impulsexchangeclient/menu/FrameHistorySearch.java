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

public class FrameHistorySearch extends javax.swing.JFrame {

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        doSearchBtn = new javax.swing.JButton();
        orderField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jOrdersList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jDatesList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Поиск");
        setMinimumSize(new java.awt.Dimension(207, 300));
        setResizable(false);
        setSize(new java.awt.Dimension(207, 300));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        doSearchBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        doSearchBtn.setText("Поиск");
        doSearchBtn.setFocusPainted(false);
        doSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doSearchBtnActionPerformed(evt);
            }
        });
        doSearchBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                generalKeyPressed(evt);
            }
        });

        orderField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                generalKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Введите номер заказа:");
        jLabel1.setFocusable(false);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(207, 300));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(207, 300));

        jOrdersList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                generalKeyPressed(evt);
            }
        });
        jOrdersList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jOrdersListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jOrdersList);

        jScrollPane2.setMinimumSize(new java.awt.Dimension(207, 300));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(207, 300));

        jDatesList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                generalKeyPressed(evt);
            }
        });
        jDatesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jDatesListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jDatesList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(orderField, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(doSearchBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(1, 1, 1))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane1, jScrollPane2});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(orderField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(doSearchBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, orderField});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jScrollPane1, jScrollPane2});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public FrameHistorySearch(FrameMain mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        mainFrame.setEnabled(false);
        setLocationRelativeTo(null);
        jOrdersList.setModel(ordersList);
        jDatesList.setModel(datesList);
    }

    private void doSearch() {
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
            for (String order : oldHistoryString.split(";")) {
                String orderName = Service.extractOrderParam(order, 1);
                if (orderName.contains(orderField.getText())) {
                    ordersList.addElement(orderName);
                    datesList.addElement(Service.extractOrderParam(order, 2));
                }
            }

            if (ordersList.isEmpty()) {
                ordersList.addElement("Нет совпадений");
            }
            if (orderField.getText().equals("")) {
                this.setTitle("Поиск по всем заказам");
            } else {
                this.setTitle("Поиск по заказу: " + orderField.getText());
            }
            orderField.setText("");

        } catch (SQLException ex) {
            String errorMsg = "Неизвестная ошибка. Описание:";
            JOptionPane.showMessageDialog(null, "SQLException. Произошла ошибка при чтении архива. \r\n"
                    + errorMsg + "\r\n" + "ex: " + ex, this.getClass().getName() + " : doSearch()", JOptionPane.ERROR_MESSAGE);
        } finally {
            mySqlInstance.disconnect();
        }
    }

    private void doSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doSearchBtnActionPerformed
        doSearch();
    }//GEN-LAST:event_doSearchBtnActionPerformed

    private void generalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_generalKeyPressed
        if (evt.getKeyCode() == 10) {
            doSearchBtn.doClick();
        } else if (evt.getKeyCode() == 27) {
            mainFrame.setEnabled(true);
            this.dispose();
        }
    }//GEN-LAST:event_generalKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        mainFrame.setEnabled(true);
    }//GEN-LAST:event_formWindowClosing

    private void jOrdersListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jOrdersListValueChanged
        jDatesList.setSelectedIndex(jOrdersList.getSelectedIndex());
    }//GEN-LAST:event_jOrdersListValueChanged

    private void jDatesListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jDatesListValueChanged
        jOrdersList.setSelectedIndex(jDatesList.getSelectedIndex());
    }//GEN-LAST:event_jDatesListValueChanged

    private final DefaultListModel ordersList = new DefaultListModel();
    private final DefaultListModel datesList = new DefaultListModel();
    private final FrameMain mainFrame;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton doSearchBtn;
    private javax.swing.JList jDatesList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jOrdersList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField orderField;
    // End of variables declaration//GEN-END:variables
}
