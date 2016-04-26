package impulsexchangeclient;

import impulsexchangeclient.menu.FrameHelpAdditional;
import impulsexchangeclient.menu.FrameHelpErrors;
import impulsexchangeclient.menu.FrameHelpManual;
import impulsexchangeclient.entities.OrderEntity;
import impulsexchangeclient.options.Options;
import impulsexchangeclient.firebird.FirebirdExtractor;
import impulsexchangeclient.menu.*;
import impulsexchangeclient.mysql.MonitorSender;
import impulsexchangeclient.mysql.SwndSender;
import java.awt.Toolkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class FrameMain extends javax.swing.JFrame {

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nextBtn = new javax.swing.JButton();
        orderNameField = new javax.swing.JTextField();
        label1 = new javax.swing.JLabel();
        noMonitor = new javax.swing.JCheckBox();
        noSwnd = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        mainMenu = new javax.swing.JMenu();
        departmentOptionsCallBtn = new javax.swing.JMenuItem();
        firebirdOptionsCallBtn = new javax.swing.JMenuItem();
        mySqlOptionsCallBtn = new javax.swing.JMenuItem();
        adminOptionsCallBtn = new javax.swing.JMenuItem();
        exitMenuBtn = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        monitorViewerCallBtn = new javax.swing.JMenuItem();
        archivesMenu = new javax.swing.JMenu();
        archiveCallBtn = new javax.swing.JMenuItem();
        doSearchBtn = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpManualCallBtn = new javax.swing.JMenuItem();
        helpAdditionalCallBtn = new javax.swing.JMenuItem();
        helpErrorsCallBtn = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusable(false);
        setName("mainFrame"); // NOI18N
        setResizable(false);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        nextBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        nextBtn.setText("Далее");
        nextBtn.setFocusPainted(false);
        nextBtn.setPreferredSize(new java.awt.Dimension(151, 21));
        nextBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextBtnActionPerformed(evt);
            }
        });
        nextBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                generalKeyPressed(evt);
            }
        });

        orderNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                orderNameFieldKeyPressed(evt);
            }
        });

        label1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label1.setText("Введите номер заказа:");
        label1.setFocusable(false);

        noMonitor.setText("не добавлять в отгрузку");
        noMonitor.setFocusPainted(false);
        noMonitor.setFocusable(false);
        noMonitor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        noMonitor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                noMonitorItemStateChanged(evt);
            }
        });

        noSwnd.setText("не отправлять файл обмена");
        noSwnd.setFocusPainted(false);
        noSwnd.setFocusable(false);
        noSwnd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        noSwnd.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                noSwndItemStateChanged(evt);
            }
        });

        mainMenu.setText("Опции");

        departmentOptionsCallBtn.setText("Настройки отдела");
        departmentOptionsCallBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departmentOptionsCallBtnActionPerformed(evt);
            }
        });
        mainMenu.add(departmentOptionsCallBtn);

        firebirdOptionsCallBtn.setText("Настройки Firebird");
        firebirdOptionsCallBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firebirdOptionsCallBtnActionPerformed(evt);
            }
        });
        mainMenu.add(firebirdOptionsCallBtn);

        mySqlOptionsCallBtn.setText("Настройки MySQL");
        mySqlOptionsCallBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mySqlOptionsCallBtnActionPerformed(evt);
            }
        });
        mainMenu.add(mySqlOptionsCallBtn);

        adminOptionsCallBtn.setText("Настройки администратора");
        adminOptionsCallBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminOptionsCallBtnActionPerformed(evt);
            }
        });
        mainMenu.add(adminOptionsCallBtn);

        exitMenuBtn.setText("Выход");
        exitMenuBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuBtnActionPerformed(evt);
            }
        });
        mainMenu.add(exitMenuBtn);

        jMenuBar1.add(mainMenu);

        jMenu1.setText("Отгрузка");

        monitorViewerCallBtn.setText("Просмотр готовности заказов");
        monitorViewerCallBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monitorViewerCallBtnActionPerformed(evt);
            }
        });
        jMenu1.add(monitorViewerCallBtn);

        jMenuBar1.add(jMenu1);

        archivesMenu.setText("Архив");

        archiveCallBtn.setText("Последние 25 заказов");
        archiveCallBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                archiveCallBtnActionPerformed(evt);
            }
        });
        archivesMenu.add(archiveCallBtn);

        doSearchBtn.setText("Поиск по заказам");
        doSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doSearchBtnActionPerformed(evt);
            }
        });
        archivesMenu.add(doSearchBtn);

        jMenuBar1.add(archivesMenu);

        helpMenu.setText("Справка");

        helpManualCallBtn.setText("Руководство по использованию");
        helpManualCallBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpManualCallBtnActionPerformed(evt);
            }
        });
        helpMenu.add(helpManualCallBtn);

        helpAdditionalCallBtn.setText("Дополнительные функции");
        helpAdditionalCallBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpAdditionalCallBtnActionPerformed(evt);
            }
        });
        helpMenu.add(helpAdditionalCallBtn);

        helpErrorsCallBtn.setText("Возможные ошибки");
        helpErrorsCallBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpErrorsCallBtnActionPerformed(evt);
            }
        });
        helpMenu.add(helpErrorsCallBtn);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(noSwnd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addComponent(noMonitor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(orderNameField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nextBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(noMonitor, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noSwnd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public FrameMain() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
        orderNameField.requestFocusInWindow();
        noMonitor.doClick();
    }

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        this.setTitle("Отдел № " + Options.getDepartmentName());
    }//GEN-LAST:event_formWindowGainedFocus

    private void archiveCallBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveCallBtnActionPerformed
        new FrameHistoryLast25(this).setVisible(true);
    }//GEN-LAST:event_archiveCallBtnActionPerformed

    private void doSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doSearchBtnActionPerformed
        new FrameHistorySearch(this).setVisible(true);
    }//GEN-LAST:event_doSearchBtnActionPerformed

    private void departmentOptionsCallBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departmentOptionsCallBtnActionPerformed
        new FrameAccessOptions(this, "Department").setVisible(true);
    }//GEN-LAST:event_departmentOptionsCallBtnActionPerformed

    private void firebirdOptionsCallBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firebirdOptionsCallBtnActionPerformed
        new FrameAccessOptions(this, "Firebird").setVisible(true);
    }//GEN-LAST:event_firebirdOptionsCallBtnActionPerformed

    private void mySqlOptionsCallBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mySqlOptionsCallBtnActionPerformed
        new FrameAccessOptions(this, "MySQL").setVisible(true);
    }//GEN-LAST:event_mySqlOptionsCallBtnActionPerformed

    private void exitMenuBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuBtnActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuBtnActionPerformed

    private void generalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_generalKeyPressed
        if (evt.getKeyCode() == 10) {
            nextBtn.doClick();
        }
    }//GEN-LAST:event_generalKeyPressed

    private void monitorViewerCallBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monitorViewerCallBtnActionPerformed
        new FrameMonitorViewer(this).setVisible(true);
    }//GEN-LAST:event_monitorViewerCallBtnActionPerformed

    private void helpManualCallBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpManualCallBtnActionPerformed
        new FrameHelpManual(this).setVisible(true);
    }//GEN-LAST:event_helpManualCallBtnActionPerformed

    private void helpAdditionalCallBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpAdditionalCallBtnActionPerformed
        new FrameHelpAdditional(this).setVisible(true);
    }//GEN-LAST:event_helpAdditionalCallBtnActionPerformed

    private void helpErrorsCallBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpErrorsCallBtnActionPerformed
        new FrameHelpErrors(this).setVisible(true);
    }//GEN-LAST:event_helpErrorsCallBtnActionPerformed

    private void adminOptionsCallBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminOptionsCallBtnActionPerformed
        new FrameAccessOptions(this, "ADMIN").setVisible(true);
    }//GEN-LAST:event_adminOptionsCallBtnActionPerformed


    private void nextBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextBtnActionPerformed
        orderName = orderNameField.getText().trim();
        if (isValidOrder()) {
            selectNextAction();
        } else {
            JOptionPane.showMessageDialog(null, "Некорректный номер заказа!");
        }
    }//GEN-LAST:event_nextBtnActionPerformed

    private void selectNextAction() {
        if (noMonitor.isSelected()) {
            startSending("NO_MONITOR");
        } else {
            extractFirebirdData(orderName);
        }
    }

    /**
     * Извлечение данных об указанном заказе из базы программы СуперОкна. Если
     * данный процесс пройдет успешно, то отобразится FrameMonitor, в противном
     * случае - на экране появится сообщение об ошибке.
     */
    private void extractFirebirdData(String orderName) {
        FirebirdExtractor extractor = new FirebirdExtractor(orderName);
        entity = extractor.run();
        if (entity != null) {
            new FrameMonitor(this, entity).setVisible(true);
        }
    }

    public void startSending(String param) {
        boolean totalResult = true;
        switch (param) {
            case "NO_MONITOR": {
                SwndSender swndSender = new SwndSender(orderName);
                boolean result = swndSender.run();
                if (!result) {
                    totalResult = false;
                }
            }
            break;
            case "NO_SWND": {
                MonitorSender monitorSender = new MonitorSender(entity);
                boolean result = monitorSender.run();
                if (!result) {
                    totalResult = false;
                }
            }
            break;
            default: {
                MonitorSender monitorSender = new MonitorSender(entity);
                boolean monitorSendResult = monitorSender.run();
                if (!monitorSendResult) {
                    totalResult = false;
                }

                SwndSender swndSender = new SwndSender(orderName);
                boolean swndSendResult = swndSender.run();
                if (!swndSendResult) {
                    totalResult = false;
                }
            }
            break;
        }
        if (totalResult) {
            JOptionPane.showMessageDialog(null, "Все операции были успешно завершены!",
                    "Готово!", JOptionPane.INFORMATION_MESSAGE);
        }
        setDefaultState();
    }

    public void setDefaultState() {
        orderNameField.setText(null);
        //noMonitor.setSelected(false);
        noSwnd.setSelected(false);
    }

    private void orderNameFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_orderNameFieldKeyPressed
        if (evt.getKeyCode() == 10) {
            nextBtn.doClick();
        }
    }//GEN-LAST:event_orderNameFieldKeyPressed

    private void noMonitorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_noMonitorItemStateChanged
        if (noMonitor.isSelected()) {
            nextBtn.setText("Отправить");
            noSwnd.setEnabled(false);
        } else {
            nextBtn.setText("Далее");
            noSwnd.setEnabled(true);
        }
    }//GEN-LAST:event_noMonitorItemStateChanged

    private void noSwndItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_noSwndItemStateChanged
        if (noSwnd.isSelected()) {
            noMonitor.setEnabled(false);
        } else {
            noMonitor.setEnabled(true);
        }
    }//GEN-LAST:event_noSwndItemStateChanged

    private boolean isValidOrder() {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(orderName);
        return m.matches();
    }

    public void setEntity(OrderEntity entity) {
        this.entity = entity;
    }

    public boolean isNoSwnd() {
        return noSwnd.isSelected();
    }

    private OrderEntity entity;
    private String orderName;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem adminOptionsCallBtn;
    private javax.swing.JMenuItem archiveCallBtn;
    private javax.swing.JMenu archivesMenu;
    private javax.swing.JMenuItem departmentOptionsCallBtn;
    private javax.swing.JMenuItem doSearchBtn;
    private javax.swing.JMenuItem exitMenuBtn;
    private javax.swing.JMenuItem firebirdOptionsCallBtn;
    private javax.swing.JMenuItem helpAdditionalCallBtn;
    private javax.swing.JMenuItem helpErrorsCallBtn;
    private javax.swing.JMenuItem helpManualCallBtn;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JLabel label1;
    private javax.swing.JMenu mainMenu;
    private javax.swing.JMenuItem monitorViewerCallBtn;
    private javax.swing.JMenuItem mySqlOptionsCallBtn;
    private javax.swing.JButton nextBtn;
    private javax.swing.JCheckBox noMonitor;
    private javax.swing.JCheckBox noSwnd;
    private javax.swing.JTextField orderNameField;
    // End of variables declaration//GEN-END:variables
}
