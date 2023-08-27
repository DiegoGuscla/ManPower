    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import model.bean.CarForProduction;
import model.bean.CellRendererCentralized;
import model.bean.DataEntryDate;
import model.bean.Manufacturer;
import model.bean.Model;
import model.bean.Property;
import model.dao.CarsForProductionDAO;
import model.dao.ManufacturersDAO;
import model.dao.ModelsDAO;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author diego
 */
public class CarsForProductionListView extends javax.swing.JInternalFrame {

    /**
     * Creates new form ContributorsListView
     */
    
    private CarsForProductionDAO cDAO;
    private DialogBox dialogBox;
    private ManufacturersDAO mDAO;
    private ModelsDAO mdDAO;
    private int totalOfPages = 0;
    private ArrayList<CarForProduction> carsForProduction;
    //private RegisterContributorView registerContributorView;
    
    public CarsForProductionListView() {
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

        /*
        listTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {                    
                    openRegisterContributorsView(String.valueOf(
                        (Integer) listTable.getModel().getValueAt(
                        listTable.getSelectedRow(), 0)));
                    
                }
            }
        });
        */
        
        JTableHeader header = listTable.getTableHeader();
        header.setPreferredSize(new Dimension(100, 30));
        header.setFont(new Font("Arial", Font.BOLD, 13));

        listTable.setDefaultRenderer(Object.class, new CellRendererCentralized());
        //listTable.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        
        newButton.setBackground(Color.decode("#006E96"));
        /*
        
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterContributorsView("");
            }
        });
        */
        
        editButton.setBackground(Color.decode("#006E96"));
        /*
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                openRegisterContributorsView(String.valueOf(
                        (Integer) listTable.getModel().getValueAt(
                        listTable.getSelectedRow(), 0)));                
            }
        });
        */
        
        importListButton.setBackground(Color.decode("#006E96"));
        importListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tesseractOCR();
            }                
        });
        
        updateListButton.setBackground(Color.decode("#006E96"));
        updateListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateList();
            }                
        });

        try {
            //Busca lista
            loadCarsForProductionList(getCarsForProductionDAO().getCarsForProduction());
        } catch (SQLException ex) {
            getDialogBox().showDialogBox("Erro ao buscar lista de Colaboradores!"
                    + " Erro: " + ex.getMessage(), "Atenção!");
        }
               
    }

    private void tesseractOCR() {
        JFileChooser jc = new JFileChooser();
        jc.setDialogTitle("Procurar Arquivo");
        jc.setMultiSelectionEnabled(true);
        jc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Pdf", "pdf");
        jc.setFileFilter(filter);

        int result = jc.showOpenDialog(getContentPane());
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] files = jc.getSelectedFiles();

            File dataDir = new File("tessdata");
            ArrayList<String> dataToSave = new ArrayList<>();
            if (dataDir.exists()) {
                try {
                    Tesseract instance = new Tesseract();
                    instance.setDatapath(dataDir.getAbsolutePath());                    

                    Map<String, PDDocument> documents = new HashMap<>();
                    for (File file : files) {
                        PDDocument document = PDDocument.load(file);
                        totalOfPages = totalOfPages + document.getNumberOfPages();
                        documents.put(file.getName(),document);
                    }
                    
                    JFrame frame = new JFrame("Importando Lista");
                    JProgressBar progressBar = new JProgressBar();
                    JLabel label = new JLabel("0%");
                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setBounds(40, 10, 500, 50);

                    progressBar.setMinimum(0);
                    progressBar.setMaximum(totalOfPages);

                    frame.add(progressBar, BorderLayout.CENTER);
                    progressBar.add(label, BorderLayout.CENTER);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setSize(new Dimension(600, 100));
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.setResizable(false);

                    new Thread(() -> {
                        try {
                            int progress = 0;
                            int imageDPI = 300;
                            PDFRenderer pdfRenderer;
                            for (Map.Entry<String, PDDocument> m : documents.entrySet()) {
                                pdfRenderer = new PDFRenderer(m.getValue());
                                
                                for (int p = 0; p < m.getValue().getNumberOfPages(); p++) { // FOR-EACH PAGE
                                    BufferedImage tempPageBimg = pdfRenderer.renderImageWithDPI(p, imageDPI, ImageType.RGB);
                                    String text = instance.doOCR(tempPageBimg);
                                    if (!text.contains("SOUH.")) {
                                        continue;
                                    }

                                    String array = text.split("SOUH.")[1];

                                    String clientName;
                                    String s;
                                    String searchClient;
                                    String lastResult = "";
                                    int index = 0;
                                    while (true) {
                                        try {
                                            String datas[] = array.split(" ");
                                            for (String data : datas) {
                                                if (data.contains(".") && data.length() == 10) {
                                                    index = array.indexOf(data);
                                                    break;
                                                }
                                            }
                                            if (index == 0) {
                                                continue;
                                            }

                                            //index = array.indexOf(".");
                                            if (index == -1) {
                                                break;
                                            }

                                            s = array.substring(index - 19, index - 1);
                                            s = s + ";" + array.substring(index, index + 10);
                                        } catch (Exception ev) {
                                            /*
                                                getDialogBox().showDialogBox("Erro salvando item na pagina: "
                                                        + String.valueOf(p) + ", linha: " + array, "Erro: " + ev.getMessage());
                                                frame.setVisible(false);
                                                return;
                                             */
                                            break;
                                        }
                                        searchClient = array.substring(index + 10);
                                        clientName = "";
                                        for (int i = 0; i < searchClient.length(); i++) {
                                            char c = searchClient.charAt(i);
                                            if (Character.isDigit(c) && !clientName.isEmpty()) {
                                                array = array.substring(index + 10 + i);
                                                break;
                                            } else {
                                                if (Character.isLetter(c)) {
                                                    clientName = clientName + c;
                                                } else if ((Character.toString(c).equals(" ") && !clientName.isEmpty())
                                                        || Character.toString(c).equals("-")) {
                                                    clientName = clientName + " ";
                                                }
                                            }
                                        }
                                        s = s + ";" + clientName + ";" + m.getKey();
                                        if (lastResult.equals(s)) {
                                            break;
                                        } else {
                                            lastResult = s;
                                            dataToSave.add(s);
                                        }
                                    }
                                    progressBar.setValue(++progress);
                                    int percentage = (int) ((double) progress / totalOfPages * 100);
                                    label.setText(percentage + "%");
                                }
                                m.getValue().close();
                            }
                        } catch (IOException | TesseractException ex) {
                            getDialogBox().showDialogBox("Erro ao ler pdf! Erro: "
                                    + ex.getMessage(), "Atenção!");
                            return;
                        }

                        ArrayList<Model> models = null;
                        try {
                            models = getModelsDAO().getModels();
                        } catch (SQLException ex) {
                            getDialogBox().showDialogBox("Erro ao buscar lista de Modelos!"
                                    + " Erro: " + ex.getMessage(), "Atenção!");
                        }

                        ArrayList<Manufacturer> manufacturers = null;
                        try {
                            manufacturers = getManufacturersDAO().getManufacturers();
                        } catch (SQLException ex) {
                            getDialogBox().showDialogBox("Erro ao buscar lista de Fabricantes!"
                                    + " Erro: " + ex.getMessage(), "Atenção!");
                        }

                        ArrayList<ArrayList<Property>> propertiesList = new ArrayList<>();
                        ArrayList<Property> properties;
                        Property property;
                        for (int i = 0; i <= dataToSave.size() - 1; i++) {
                            properties = new ArrayList<>();
                            property = new Property();
                            property.setProperty("id", Property.PropertyType.INTEGER, null);
                            properties.add(property);

                            String[] data = dataToSave.get(i).split(";");

                            property = new Property();
                            String chassis = data[0].trim();
                            chassis = chassis.replaceAll(" ", "");
                            chassis = chassis.replaceAll("[^A-Za-z0-9]", "");
                            chassis = chassis.replaceAll("O", "0");
                            chassis = chassis.replaceAll("I", "1");
                            
                            /*
                            try {
                                //Checa se ja possui registro no banco
                                if (getCarsForProductionDAO().checkRecord(chassis)) {
                                    continue;
                                }
                            } catch (SQLException ex) {
                            }
                            */

                            String modelCode = chassis.substring(6, 8);
                            switch (modelCode) {
                                case "El":
                                    chassis = chassis.replaceAll("E1", "E1");
                                    modelCode = "E1";
                                    break;
                                case "Ej":
                                    chassis = chassis.replaceAll("Ej", "E1");
                                    modelCode = "E1";
                                    break;
                                case "Al":
                                    chassis = chassis.replaceAll("Al", "A1");
                                    modelCode = "A1";
                                    break;
                                case "Fl":
                                    chassis = chassis.replaceAll("Fl", "F1");
                                    modelCode = "F1";
                                    break;                            
                                default:
                            }
                            
                            String manufacturerCode = chassis.substring(0, 3);
                            switch (manufacturerCode) {                                
                                case "WVl":
                                    chassis = chassis.replaceAll("WVl", "WV1");
                                    manufacturerCode = "WV1";
                                    break;
                                case "WVj":
                                    chassis = chassis.replaceAll("WVj", "WV1");
                                    manufacturerCode = "WV1";
                                    break;
                                default:
                            }

                            property.setProperty("chassis", Property.PropertyType.STRING, chassis);
                            properties.add(property);

                            property = new Property();
                            property.setProperty("deliveryDate",
                                    Property.PropertyType.DATE, DataEntryDate.getDateFromString(
                                            data[1].trim().replace(".", "/")));
                            properties.add(property);

                            property = new Property();
                            property.setProperty("clientName", Property.PropertyType.STRING, data[2].trim());
                            properties.add(property);

                            property = new Property();
                            boolean hasManufacturer = false;
                            for (Manufacturer manufacturer : manufacturers) {
                                if (manufacturerCode.equals(manufacturer.getCode())) {
                                    property.setProperty("manufacturer", Property.PropertyType.STRING,
                                            manufacturer.getName().toUpperCase());
                                    hasManufacturer = true;
                                    break;
                                }
                            }
                            if (!hasManufacturer) {
                                property.setProperty("manufacturer", Property.PropertyType.STRING, "");
                            }
                            properties.add(property);

                            property = new Property();
                            boolean hasModel = false;
                            for (Model model : models) {
                                if (model.getCode().contains(",")) {
                                    String[] code = model.getCode().split(",");
                                    if (modelCode.equals(code[0].trim())
                                            || modelCode.equals(code[1].trim())) {
                                        property.setProperty("model", Property.PropertyType.STRING, model.getName().toUpperCase());
                                        hasModel = true;
                                        break;
                                    }
                                } else {
                                    if (modelCode.equals(model.getCode())) {
                                        property.setProperty("model", Property.PropertyType.STRING, model.getName().toUpperCase());
                                        hasModel = true;
                                        break;
                                    }
                                }

                            }
                            if (!hasModel) {
                                property.setProperty("model", Property.PropertyType.STRING, "");
                            }
                            properties.add(property);
                            
                            property = new Property();
                            property.setProperty("fileName", Property.PropertyType.STRING, data[3].trim());
                            properties.add(property);
                            
                            propertiesList.add(properties);
                        }

                        try {
                            String message = getCarsForProductionDAO().addCarsForProduction(propertiesList);
                            loadCarsForProductionList(getCarsForProductionDAO().getCarsForProduction());
                            frame.setVisible(false);
                            if (message.isEmpty()) {
                                getDialogBox().showDialogBox("Dados salvos com sucesso!",
                                        "Atenção!");
                            } else {
                                getDialogBox().showDialogBox("Erro ao adicionar carros para produção! Erro: " + message,
                                        "Atenção!");
                            }                            
                        } catch (SQLException ex) {
                            frame.setVisible(false);
                            getDialogBox().showDialogBox("Erro ao salvar lista de carros para producao! "
                                    + "Erro: " + ex.getMessage(), "Atenção!");                            
                        }
                    }).start();
                } catch (IOException ex) {
                    System.err.println("Erro ao ler arquivo, " + ex.getMessage());
                }
            }
        }
    }
    
    private void updateList() {
        
    }

    private DialogBox getDialogBox() {
        if(dialogBox == null)
            dialogBox = new DialogBox(getContentPane());
        
        return dialogBox;
    }
    
    private ManufacturersDAO getManufacturersDAO() {
        if(mDAO == null)
            mDAO = new ManufacturersDAO();
        
        return mDAO;
    }
    
    private ModelsDAO getModelsDAO() {
        if(mdDAO == null)
            mdDAO = new ModelsDAO();
        
        return mdDAO;
    }
    
    /*
    private void openRegisterContributorsView(String contributorId) {
        registerContributorView = new RegisterContributorView(contributorId);
        registerContributorView.addInternalFrameListener(new InternalFrameAdapter(){
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                enableContributorsListView();
                registerContributorView.removeAll();
                registerContributorView = null;
                try {
                    loadContributorsList(getContributorsDAO().getContributors());
                } catch (SQLException ex) {
                    getDialoBox().showDialogBox("Erro ao buscar lista de Colaboradores!"
                    + " Erro: " + ex.getMessage(), "Atenção!");
                }
            }
        });
        
        JDesktopPane desktopPane = (JDesktopPane) this.getDesktopPane();
        desktopPane.add(registerContributorView);
        registerContributorView.toFront();
        registerContributorView.setSize(1200, 400);
        registerContributorView.setPreferredSize(new Dimension(1200, 400));
        registerContributorView.setPosition(desktopPane);
        
        registerContributorView.setVisible(true);
        this.setVisible(false);        
    }
    
    
    private void enableContributorsListView() {
        this.setVisible(true);
    }
    */
    
    private CarsForProductionDAO getCarsForProductionDAO() {
        if(cDAO == null)
            cDAO = new CarsForProductionDAO();
        
        return cDAO;
    }
    
    private void loadCarsForProductionList(ArrayList<CarForProduction> carsForProduction) {
        this.carsForProduction = carsForProduction;
        DefaultTableModel model = (DefaultTableModel) listTable.getModel();
        model.setNumRows(0);
        
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        listTable.setRowSorter(sorter);
        
        sorter.setSortable(0, false);
        sorter.setSortable(1, false);
        sorter.setSortable(2, false);
        sorter.setSortable(3, false);
        sorter.setSortable(4, false);
        sorter.setSortable(5, false);
        sorter.setSortable(6, false);
        
        for (CarForProduction carForProduction : carsForProduction) {

            model.addRow(new Object[]{
                carForProduction.getId(),
                carForProduction.getChassis(),
                DataEntryDate.getStringFromDate(carForProduction.getDeliveryDate()),
                carForProduction.getClientName(),
                carForProduction.getManufacturer(),
                carForProduction.getModel(),
                carForProduction.getFileName()
            });
        }
        
        totalLabel.setText("Total: " + carsForProduction.size());
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
        importListButton = new javax.swing.JButton();
        totalLabel = new javax.swing.JLabel();
        updateListButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listTable = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setClosable(true);
        setTitle("ManPower - Carros em Produção");
        setPreferredSize(new java.awt.Dimension(1200, 600));

        editButton.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        editButton.setForeground(java.awt.Color.white);
        editButton.setText("Editar");

        newButton.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        newButton.setForeground(java.awt.Color.white);
        newButton.setText("Novo");

        importListButton.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        importListButton.setForeground(java.awt.Color.white);
        importListButton.setText("Importar Lista");

        totalLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        totalLabel.setText("Total:");

        updateListButton.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        updateListButton.setForeground(java.awt.Color.white);
        updateListButton.setText("Atualizar Lista");

        javax.swing.GroupLayout optionsPanelLayout = new javax.swing.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(importListButton, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateListButton, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalLabel)
                .addContainerGap(543, Short.MAX_VALUE))
        );
        optionsPanelLayout.setVerticalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newButton, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(importListButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalLabel)
                    .addComponent(updateListButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        listTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Chassis", "Data de Entrega", "Cliente", "Fabricante", "Modelo", "Nome do Arquivo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
            listTable.getColumnModel().getColumn(2).setMinWidth(170);
            listTable.getColumnModel().getColumn(2).setMaxWidth(170);
            listTable.getColumnModel().getColumn(5).setMinWidth(200);
            listTable.getColumnModel().getColumn(5).setMaxWidth(200);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(optionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
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
    private javax.swing.JButton importListButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable listTable;
    private javax.swing.JButton newButton;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JButton updateListButton;
    // End of variables declaration//GEN-END:variables
}
