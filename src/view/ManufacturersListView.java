/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import model.bean.CellRendererCentralized;
import model.bean.Manufacturer;
import model.dao.ManufacturersDAO;

/**
 *
 * @author diego
 */
public class ManufacturersListView extends javax.swing.JInternalFrame {

    /**
     * Creates new form ContributorsListView
     */
    
    private ManufacturersDAO mDAO;
    private DialogBox dialogBox;    
    private RegisterManufacturerView registerManufacturerView;
    
    public ManufacturersListView() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        initComponents();

        //optionsPanel.setBackground(Color.white);
        listTable.setBackground(Color.white);
        listTable.setRowHeight(30);
        listTable.setFont(new Font("Arial", Font.PLAIN, 12));
        listTable.setPreferredScrollableViewportSize(listTable.getPreferredSize());
        listTable.setOpaque(false);

        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalTextPosition(DefaultTableCellRenderer.CENTER);
        listTable.setDefaultRenderer(String.class, dtcr);

        listTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {                    
                    openRegisterManufacturerView(String.valueOf(
                        (Integer) listTable.getModel().getValueAt(
                        listTable.getSelectedRow(), 0)));
                    
                }
            }
        });        
        
        JTableHeader header = listTable.getTableHeader();
        header.setPreferredSize(new Dimension(100, 30));
        header.setFont(new Font("Arial", Font.BOLD, 13));

        listTable.setDefaultRenderer(Object.class, new CellRendererCentralized());
        
        newButton.setBackground(Color.decode("#006E96"));        
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterManufacturerView("");
            }
        });        
        
        editButton.setBackground(Color.decode("#006E96"));
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                openRegisterManufacturerView(String.valueOf(
                        (Integer) listTable.getModel().getValueAt(
                        listTable.getSelectedRow(), 0)));                
            }
        });        

        try {
            //Busca lista
            loadManufacturersList(getManufacturersDAO().getManufacturers());
        } catch (SQLException ex) {
            getDialoBox().showDialogBox("Erro ao buscar lista de Fabricantes!"
                    + " Erro: " + ex.getMessage(), "Atenção!");
        }
               
    }
    
    private DialogBox getDialoBox() {
        if(dialogBox == null)
            dialogBox = new DialogBox(getContentPane());
        
        return dialogBox;
    }
    
    private void openRegisterManufacturerView(String manufacturerId) {
        registerManufacturerView = new RegisterManufacturerView(manufacturerId);
        registerManufacturerView.addInternalFrameListener(new InternalFrameAdapter(){
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                enableContributorsListView();
                registerManufacturerView.removeAll();
                registerManufacturerView = null;
                try {
                    loadManufacturersList(getManufacturersDAO().getManufacturers());
                } catch (SQLException ex) {
                    getDialoBox().showDialogBox("Erro ao buscar lista de Fabricantes!"
                    + " Erro: " + ex.getMessage(), "Atenção!");
                }
            }
        });
        
        JDesktopPane desktopPane = (JDesktopPane) this.getDesktopPane();
        desktopPane.add(registerManufacturerView);
        registerManufacturerView.toFront();
        registerManufacturerView.setSize(1200, 400);
        registerManufacturerView.setPreferredSize(new Dimension(1200, 400));
        registerManufacturerView.setPosition(desktopPane);
        
        registerManufacturerView.setVisible(true);
        this.setVisible(false);        
    }    
    
    private void enableContributorsListView() {
        this.setVisible(true);
    }
    
    private ManufacturersDAO getManufacturersDAO() {
        if(mDAO == null)
            mDAO = new ManufacturersDAO();
        
        return mDAO;
    }
    
    private void loadManufacturersList(ArrayList<Manufacturer> manufacturers) {
        DefaultTableModel model = (DefaultTableModel) listTable.getModel();
        model.setNumRows(0);
        
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        listTable.setRowSorter(sorter);
        
        sorter.setSortable(0, false);
        sorter.setSortable(1, false);
        sorter.setSortable(2, false);
        
        for (Manufacturer manufacturer : manufacturers) {

            model.addRow(new Object[]{
                manufacturer.getId(),
                manufacturer.getName(),
                manufacturer.getCode()
            });

        }
    }

    public void setPosition(Container container) {
        Dimension d = container.getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        optionsPanel = new javax.swing.JPanel();
        editButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listTable = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setClosable(true);
        setTitle("ManPower - Lista de Fabricantes");
        setPreferredSize(new java.awt.Dimension(1200, 600));

        editButton.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        editButton.setForeground(java.awt.Color.white);
        editButton.setText("Editar");

        newButton.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        newButton.setForeground(java.awt.Color.white);
        newButton.setText("Novo");

        javax.swing.GroupLayout optionsPanelLayout = new javax.swing.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        optionsPanelLayout.setVerticalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newButton, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        listTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Fabricante", "Código"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        listTable.setGridColor(java.awt.Color.black);
        listTable.setOpaque(false);
        listTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(listTable);
        if (listTable.getColumnModel().getColumnCount() > 0) {
            listTable.getColumnModel().getColumn(0).setMinWidth(70);
            listTable.getColumnModel().getColumn(0).setMaxWidth(70);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(optionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1196, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(optionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton editButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable listTable;
    private javax.swing.JButton newButton;
    private javax.swing.JPanel optionsPanel;
    // End of variables declaration//GEN-END:variables
}
