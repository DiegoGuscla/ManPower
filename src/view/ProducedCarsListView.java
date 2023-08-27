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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import model.bean.ProducedCar;
import model.bean.Property;
import model.dao.ManufacturersDAO;
import model.dao.ModelsDAO;
import model.dao.ProducedCarsDAO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author diego
 */
public class ProducedCarsListView extends javax.swing.JInternalFrame {

    /**
     * Creates new form ContributorsListView
     */
    
    private ProducedCarsDAO pDAO;
    private DialogBox dialogBox;
    private ManufacturersDAO mDAO;
    private ModelsDAO mdDAO;
    
    public ProducedCarsListView() {
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
                readXlsx();
            }                
        });

        try {
            //Busca lista
            loadProducedCarsList(getProducedCarsDAO().getProducedCars());
        } catch (SQLException ex) {
            getDialogBox().showDialogBox("Erro ao buscar lista de Carros Produzidos!"
                    + " Erro: " + ex.getMessage(), "Atenção!");
        }
    }

    private void readXlsx() {
        JFileChooser jc = new JFileChooser();
        jc.setDialogTitle("Procurar Arquivo");
        jc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Xlsx", "xlsx");
        jc.setFileFilter(filter);

        int result = jc.showOpenDialog(getContentPane());
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = jc.getSelectedFile();
            String path = file.getPath();

            FileInputStream xlsxFile;
            XSSFWorkbook workbook;
            try {
                xlsxFile = new FileInputStream(new File(path));

                //Create Workbook instance holding reference to .xlsx file
                workbook = new XSSFWorkbook(xlsxFile);
            } catch (FileNotFoundException e) {
                getDialogBox().showDialogBox("Erro ao carregar planilha, "
                        + "tente novamente ou entre em contato com um administrador. Erro: " + e.getMessage(),
                        "Atenção");
                return;
            } catch (IOException ex) {
                getDialogBox().showDialogBox("Erro ao carregar planilha, "
                        + "tente novamente ou entre em contato com um administrador. Erro: " + ex.getMessage(),
                        "Atenção");
                return;
            }

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            int totalNoOfPages = sheet.getLastRowNum();
            ArrayList<String> dataToSave = new ArrayList<>();

            JFrame frame = new JFrame("Importando Lista");
            JProgressBar progressBar = new JProgressBar();
            JLabel label = new JLabel("0%");
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setBounds(40, 10, 500, 50);

            progressBar.setMinimum(0);
            progressBar.setMaximum(totalNoOfPages);

            frame.add(progressBar, BorderLayout.CENTER);
            progressBar.add(label, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(new Dimension(600, 100));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setResizable(false);

            new Thread(() -> {
                int progress = 0;
                for (int p = 1; p < totalNoOfPages; p++) { // FOR-EACH PAGE
                    Row row = sheet.getRow(p);
                    Cell cell = row.getCell(2);
                    if (cell.getCellType().equals(CellType.STRING)
                            && cell.getStringCellValue().equals("voorbereiding wagens")) {
                        Date javaDate= DateUtil.getJavaDate((double) row.getCell(0).getNumericCellValue());
                        String s = row.getCell(5).getStringCellValue().toUpperCase();
                        String[] datas = s.split("-");
                        
                        //Chassis
                        s = datas[0] + ";";                        
                        if(datas.length == 4) {
                            //Modelo + cor + cliente
                            s = s + datas[1] + ";" + datas[2] + ";" + datas[3];
                        } else {
                            //Entrada incorreta no app harvest entao ignora
                            s = s + "null;null;null";
                        }
                        s = s + ";" + new SimpleDateFormat("dd/MM/yyyy").format(javaDate)
                                + ";" + row.getCell(12).getStringCellValue() 
                                + " " + row.getCell(13).getStringCellValue();
                        dataToSave.add(s);
                    }
                    
                    progressBar.setValue(++progress);
                    int percentage = (int) ((double) progress / totalNoOfPages * 100);
                    label.setText(percentage + "%");
                }
                
                ArrayList<Manufacturer> manufacturers = null;
                try {
                    manufacturers = getManufacturersDAO().getManufacturers();
                } catch (SQLException ex) {
                    getDialogBox().showDialogBox("Erro ao buscar lista de Fabricantes!"
                            + " Erro: " + ex.getMessage(), "Atenção!");
                }

                ArrayList<Model> models = null;
                try {
                    models = getModelsDAO().getModels();
                } catch (SQLException ex) {
                    getDialogBox().showDialogBox("Erro ao buscar lista de Modelos!"
                            + " Erro: " + ex.getMessage(), "Atenção!");
                }

                ArrayList<ArrayList<Property>> propertiesList = new ArrayList<>();
                ArrayList<Integer> carForProductionToDelete = new ArrayList<>();
                //HashMap<String, ArrayList<Property>> propertiesList = new HashMap<>();
                ArrayList<Property> properties;
                Property property;
                for (int i = 0; i <= dataToSave.size() - 1; i++) {
                    properties = new ArrayList<>();
                    property = new Property();
                    property.setProperty("id", Property.PropertyType.INTEGER, null);
                    properties.add(property);

                    String[] data = dataToSave.get(i).split(";");
                    String chassis = data[0].trim();
                    chassis = chassis.replaceAll(" ", "");
                    chassis = chassis.replaceAll("[^A-Za-z0-9]", "");
                    chassis = chassis.replaceAll("O", "0");
                    
                    if (chassis.length() != 17) {
                        getDialogBox().showDialogBox("Um ou mais registro possuem erros, "
                                + "por favor revise a tabela e tente novamente. Erro: " + chassis, "Atenção!");
                        frame.setVisible(false);
                        return;
                    }     
                    
                    try {
                        //Checa se ja possui registro no banco
                        if (getProducedCarsDAO().checkRecord(chassis.substring(chassis.length() - 5, chassis.length())))
                            continue;
                    } catch (SQLException ex) {
                    }
                    
                    property = new Property();
                    property.setProperty("chassis", Property.PropertyType.STRING, chassis);
                    properties.add(property);

                    property = new Property();
                    property.setProperty("productionDate",
                            Property.PropertyType.DATE, DataEntryDate.getDateFromString(
                                    data[4]));
                    properties.add(property);

                    CarForProduction carForProduction = null;
                    try {
                        carForProduction = getProducedCarsDAO().getCarForProductionToDelete(
                                chassis.substring(chassis.length() - 6, chassis.length() - 1));
                    } catch (SQLException ex) {
                    }

                    property = new Property();
                    if (carForProduction != null) {
                        carForProductionToDelete.add(carForProduction.getId());                        
                        property.setProperty("manufacturer", Property.PropertyType.STRING, carForProduction.getManufacturer());
                    } else {
                        if (manufacturers != null) {
                            boolean hasManufacturer = false;
                            for (Manufacturer manufacturer : manufacturers) {
                                if (chassis.substring(0, 3).equals(manufacturer.getCode())) {
                                    property.setProperty("manufacturer", Property.PropertyType.STRING,
                                            manufacturer.getName().toUpperCase());
                                    hasManufacturer = true;
                                    break;
                                }
                            }
                            if (!hasManufacturer) {
                                property.setProperty("manufacturer", Property.PropertyType.STRING, "");
                            }
                        } else {
                            property.setProperty("manufacturer", Property.PropertyType.STRING, "");
                        }
                    }
                    properties.add(property);
                    
                    property = new Property();
                    if (carForProduction != null && !carForProduction.getModel().isEmpty()) {
                        property.setProperty("model", Property.PropertyType.STRING, carForProduction.getModel());
                    } else {
                        if (models != null) {
                            boolean hasModel = false;
                            for (Model model : models) {
                                if (chassis.substring(6, 8).equals(model.getCode())) {
                                    property.setProperty("model", Property.PropertyType.STRING,
                                            model.getName().toUpperCase());
                                    hasModel = true;
                                    break;
                                }
                            }
                            if (!hasModel) {
                                property.setProperty("model", Property.PropertyType.STRING,
                                        (data[1].equals("null") ? "" : data[1]));
                            }
                        } else {
                            property.setProperty("model", Property.PropertyType.STRING,
                                    (data[1].equals("null") ? "" : data[1]));
                        }
                    }
                    properties.add(property);

                    property = new Property();
                    property.setProperty("color", Property.PropertyType.STRING, data[2]);
                    properties.add(property);
                    
                    property = new Property();
                    property.setProperty("employee", Property.PropertyType.STRING, data[5].toUpperCase());
                    properties.add(property);
                    
                    property = new Property();
                    property.setProperty("observation", Property.PropertyType.STRING, "");
                    properties.add(property);

                    property = new Property();
                    if (carForProduction != null) {
                         property.setProperty("clientName", Property.PropertyType.STRING, 
                            carForProduction.getClientName());
                    } else {
                         property.setProperty("clientName", Property.PropertyType.STRING, 
                            data[3].equals("null") ? "" : data[3].toUpperCase());
                    }                   
                    properties.add(property);

                    property = new Property();
                    property.setProperty("carIdentification", Property.PropertyType.STRING, 
                            chassis.substring(chassis.length() - 5, chassis.length()));
                    properties.add(property);

                    propertiesList.add(properties);
                }

                try {
                    getProducedCarsDAO().saveProducedCarAndDeleteCarForProduction(propertiesList, carForProductionToDelete);
                    loadProducedCarsList(getProducedCarsDAO().getProducedCars());
                    frame.setVisible(false);
                    getDialogBox().showDialogBox("Dados salvos com sucesso!",
                            "Atenção!");
                } catch (SQLException ex) {
                    frame.setVisible(false);
                    getDialogBox().showDialogBox("Erro ao salvar Fabricante! "
                            + "Erro: " + ex.getMessage(), "Atenção!");
                }
            }).start();
        }
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
    
    private ProducedCarsDAO getProducedCarsDAO() {
        if(pDAO == null)
            pDAO = new ProducedCarsDAO();
        
        return pDAO;
    }
    
    private void loadProducedCarsList(ArrayList<ProducedCar> producedCars) {
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
        sorter.setSortable(7, false);
        
        for (ProducedCar producedCar : producedCars) {

            model.addRow(new Object[]{
                producedCar.getId(),
                producedCar.getChassis(),
                DataEntryDate.getStringFromDate(producedCar.getProductionDate()),
                producedCar.getEmployee(),
                producedCar.getClientName(),
                producedCar.getManufacturer(),
                producedCar.getModel(),
                producedCar.getColor()
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
        importListButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listTable = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setClosable(true);
        setTitle("ManPower - Carros Produzidos");
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
                .addComponent(importListButton, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(765, Short.MAX_VALUE))
        );
        optionsPanelLayout.setVerticalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newButton, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(importListButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        listTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Chassis", "Data de Produção", "Funcionário", "Cliente", "Fabricante", "Modelo", "Cor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
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
            listTable.getColumnModel().getColumn(2).setMinWidth(150);
            listTable.getColumnModel().getColumn(2).setMaxWidth(150);
            listTable.getColumnModel().getColumn(7).setMinWidth(100);
            listTable.getColumnModel().getColumn(7).setMaxWidth(100);
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
    // End of variables declaration//GEN-END:variables
}
