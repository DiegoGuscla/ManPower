/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JDesktopPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import model.bean.Contributor;
import model.bean.DataEntryDate;
import model.bean.DataEntryNumber;
import model.bean.Property;
import model.dao.CarsForProductionDAO;

/**
 *
 * @author diego
 */
public class RegisterCarForProductionView extends javax.swing.JInternalFrame {

    /**
     * Creates new form RegisterContributorViewNew
     */
    
    private DialogBox dialogBox;
    private CarsForProductionDAO cDAO;
    
    public RegisterCarForProductionView(String carForProductionId) {
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
        
        //getContentPane().setBackground(Color.white);
        //buttonsPanel.setBackground(Color.white);
        //userPanel.setBackground(Color.white);
        //addressPanel.setBackground(Color.white);
        saveButton.setBackground(Color.decode("#006E96"));
        searchButton.setBackground(Color.decode("#006E96"));
        codTextField.setEditable(false);
        
        situationComboBox.setBackground(Color.white);
        situationComboBox.getComponent(0).setBackground(Color.decode("#CADBEC"));
        
        typeComboBox.setBackground(Color.white);
        typeComboBox.getComponent(0).setBackground(Color.decode("#CADBEC"));
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveContributor();
            }

        });
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getCep();
            }
        });

        /*
        if (!carForProductionId.isEmpty()) {
            try {
                loadCarForProduction(getCarsForProductionDAO().getCarForProduction(carForProductionId));
            } catch (SQLException ex) {
                getDialoBox().showDialogBox("Erro ao carregar dados do colaborador! Erro: "
                        + ex.getMessage(), "Atenção!");
            }
        }
        */
    }
    
    /*
    private void loadCarForProduction(Contributor contributor) {
        if (contributor == null) {
            getDialoBox().showDialogBox("Não foi possível carregar daddos do "
                    + "colaborador! Erro: NullPointer", "Atenção!");
            return;
        }

        codTextField.setText(String.valueOf(contributor.getId()));
        nameTextField.setText(contributor.getName());
        //situationComboBox.setSelectedIndex(
        //        contributor.getSituation().equals("1") ? 0 : 1);
        situationComboBox.setSelectedItem(contributor.getSituation());
        cpfTextField.setText(DataEntryNumber.printCPF(contributor.getCpf()));
        rgTextField.setText(contributor.getRg());
        birthDateTextField.setText(DataEntryDate.getStringFromDate(contributor.getBirthDate()));
        userTextField.setText(contributor.getUser());
        pwTextField.setText(contributor.getPassword());

        //comentar esse bloco
        int index = 0;
        switch (contributor.getType()) {
            case "2":
                index = 1;
                break;
            case "3":
                index = 2;
                break;
            case "4":
                index = 3;
                break;
            case "5":
                index = 4;
                break;
            default:
        }

        typeComboBox.setSelectedIndex(index);
        //Comentar esse bloco
    
        typeComboBox.setSelectedItem(contributor.getType());
        cepTextField.setText(contributor.getCep());
        stateTextField.setText(contributor.getState());
        cityTextField.setText(contributor.getCity());
        districtTextField.setText(contributor.getDistrict());
        addressTextField.setText(contributor.getAddress());
        numberTextField.setText(contributor.getNumber());
        complementTextField.setText(contributor.getComplement());
        phoneTextField.setText(contributor.getPhone());
        cellPhoneTextField.setText(contributor.getCellPhone());
        emailTextField.setText(contributor.getEmail());
    }
     */

    /*
    *Checa campos de entrada e salva colaborador
     */
    private void saveContributor() {
        if (nameTextField.getText().isEmpty()) {
            getDialoBox().showDialogBox("Preencha o campo Nome", "Atenção!");
            nameTextField.requestFocusInWindow();
            return;
        }

        if (cpfTextField.getText().isEmpty()) {
            getDialoBox().showDialogBox("Preencha o campo CPF", "Atenção!");
            cpfTextField.requestFocusInWindow();
            return;
        }

        //Checa se é um CPF Válido
        if (!DataEntryNumber.isCpfCnpj(cpfTextField.getText(), "cpf")) {
            getDialoBox().showDialogBox("CPF Inválido", "Atenção!");
            cpfTextField.requestFocusInWindow();
            return;
        }

        if (userTextField.getText().isEmpty()) {
            getDialoBox().showDialogBox("Preencha o campo Usuário", "Atenção!");
            userTextField.requestFocusInWindow();
            return;
        }

        if (String.valueOf(pwTextField.getPassword()).isEmpty()) {
            getDialoBox().showDialogBox("Preencha o campo Senha", "Atenção!");
            pwTextField.requestFocusInWindow();
            return;
        }

        //Verifica formato da data
        if (birthDateTextField.getText().length() != 10) {
            getDialoBox().showDialogBox("Data de Nascimento fora do formato!",
                    "Atenção!");
            birthDateTextField.requestFocusInWindow();
            return;
        }

        //Cria lista de propriedades a serem salvas
        ArrayList<Property> properties = new ArrayList<>();
        Property property = new Property();
        if (codTextField.getText().isEmpty()) {
            property.setProperty("id", Property.PropertyType.INTEGER, null);
        } else {
            property.setProperty("id", Property.PropertyType.INTEGER, Integer.valueOf(codTextField.getText()));
        }
        properties.add(property);

        property = new Property();
        property.setProperty("name", Property.PropertyType.STRING, nameTextField.getText().toUpperCase());
        properties.add(property);

        property = new Property();
        property.setProperty("cpf", Property.PropertyType.STRING, DataEntryNumber.formatCpfCnpj(cpfTextField.getText()));
        properties.add(property);

        property = new Property();
        property.setProperty("rg", Property.PropertyType.STRING, rgTextField.getText());
        properties.add(property);

        //String[] s = situationComboBox.getSelectedItem().toString().split("-");

        property = new Property();
        property.setProperty("situation", Property.PropertyType.STRING, 
                situationComboBox.getSelectedItem().toString());
        properties.add(property);

        property = new Property();
        property.setProperty("birthDate", Property.PropertyType.DATE, birthDateTextField.getText());
        properties.add(property);

        property = new Property();
        property.setProperty("user", Property.PropertyType.STRING, userTextField.getText());
        properties.add(property);

        property = new Property();
        property.setProperty("password", Property.PropertyType.STRING, String.valueOf(pwTextField.getPassword()));
        properties.add(property);
        
        property = new Property();
        property.setProperty("authorizationPassword", 
                Property.PropertyType.STRING, String.valueOf(autorizationPasswordTextField.getPassword()));
        properties.add(property);

        //s = typeComboBox.getSelectedItem().toString().split("-");

        property = new Property();
        property.setProperty("type", Property.PropertyType.STRING, 
                typeComboBox.getSelectedItem().toString());
        properties.add(property);

        property = new Property();
        property.setProperty("cep", Property.PropertyType.STRING, cepTextField.getText());
        properties.add(property);

        property = new Property();
        property.setProperty("state", Property.PropertyType.STRING, stateTextField.getText().toUpperCase());
        properties.add(property);

        property = new Property();
        property.setProperty("city", Property.PropertyType.STRING, cityTextField.getText().toUpperCase());
        properties.add(property);

        property = new Property();
        property.setProperty("district", Property.PropertyType.STRING, districtTextField.getText().toUpperCase());
        properties.add(property);

        property = new Property();
        property.setProperty("address", Property.PropertyType.STRING, addressTextField.getText().toUpperCase());
        properties.add(property);

        property = new Property();
        property.setProperty("number", Property.PropertyType.STRING, numberTextField.getText());
        properties.add(property);

        property = new Property();
        property.setProperty("complement", Property.PropertyType.STRING, complementTextField.getText().toUpperCase());
        properties.add(property);

        property = new Property();
        property.setProperty("phone", Property.PropertyType.STRING, DataEntryNumber.getNumbersFromString(phoneTextField.getText()));
        properties.add(property);

        property = new Property();
        property.setProperty("cellPhone", Property.PropertyType.STRING, DataEntryNumber.getNumbersFromString(cellPhoneTextField.getText()));
        properties.add(property);

        property = new Property();
        property.setProperty("email", Property.PropertyType.STRING, emailTextField.getText());
        properties.add(property);

        property = new Property();
        property.setProperty("comments", Property.PropertyType.STRING, "");
        properties.add(property);

        HashMap<String, ArrayList<Property>> propertiesList = new HashMap<>();
        propertiesList.put(codTextField.getText().isEmpty()
                ? "new" : codTextField.getText(), properties);

        /*
        try {
            Integer id = getContributorsDAO().saveContributor(propertiesList);
            if (codTextField.getText().isEmpty())
                codTextField.setText(String.valueOf(id));
            
            getDialoBox().showDialogBox("Dados salvos com sucesso!",
                    "Atenção!");
        } catch (SQLException ex) {
            getDialoBox().showDialogBox("Erro ao salvar colaborador! "
                    + "Erro: " + ex.getMessage(), "Atenção!");
        }
        */
    }

    private void getCep() {
        String cep = cepTextField.getText();
        if (cep.isEmpty()) {
            getDialoBox().showDialogBox("Preencha o campo CEP", "Atenção!");
            return;
        }

        if (cep.contains(".")) {
            cep = cep.replaceAll("\\.", "");
        }

        if (cep.contains("-")) {
            cep = cep.replaceAll("-", "");
        }

        if (cep.length() != 8) {
            getDialoBox().showDialogBox("CEP Inválido", "Atenção!");
            return;
        }

        URL url;
        try {
            url = new URL("https://api.postmon.com.br/v1/cep/" + cep + "?format=xml"); //+ "?format=xml");

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            // le ate o final  
            StringBuffer newData = new StringBuffer();
            String s = "";
            while (null != ((s = br.readLine()))) {
                newData.append(s);
            }
            br.close();

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(newData.toString()));
            Document doc = db.parse(is);

            NodeList nodes = doc.getElementsByTagName("result");

            Element element = null;

            //Posiciona informações de endereço
            element = (Element) nodes.item(0);

            stateTextField.setText(element.getElementsByTagName("estado").item(0).getTextContent());
            addressTextField.setText(element.getElementsByTagName("logradouro").item(0).getTextContent());
            districtTextField.setText(element.getElementsByTagName("bairro").item(0).getTextContent());
            cityTextField.setText(element.getElementsByTagName("cidade").item(0).getTextContent());
        } catch (MalformedURLException ex) {
            getDialoBox().showDialogBox("Não foi possível buscar endereço "
                    + "pelo CEP informado, confira as informações inseridas. Erro: " + ex.getMessage(), "Atenção!");
        } catch (IOException ex) {
            getDialoBox().showDialogBox("Não foi possível buscar endereço "
                    + "pelo CEP informado, confira as informações inseridas. Erro: " + ex.getMessage(), "Atenção!");
        } catch (ParserConfigurationException ex) {
            getDialoBox().showDialogBox("Não foi possível buscar endereço "
                    + "pelo CEP informado, confira as informações inseridas. Erro: " + ex.getMessage(), "Atenção!");
        } catch (SAXException ex) {
            getDialoBox().showDialogBox("Não foi possível buscar endereço "
                    + "pelo CEP informado, confira as informações inseridas. Erro: " + ex.getMessage(), "Atenção!");
        }
    }    

    private DialogBox getDialoBox() {
        if (dialogBox == null) {
            dialogBox = new DialogBox(getContentPane());
        }

        return dialogBox;
    }

    private CarsForProductionDAO getCarsForProductionDAO() {
        if (cDAO == null) {
            cDAO = new CarsForProductionDAO();
        }

        return cDAO;
    }

    public void setPosition(JDesktopPane container) {
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

        buttonsPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        userPanel = new javax.swing.JPanel();
        codTextField = new javax.swing.JTextField();
        nameTextField = new javax.swing.JTextField();
        situationComboBox = new javax.swing.JComboBox<>();
        rgTextField = new javax.swing.JTextField();
        pwTextField = new javax.swing.JPasswordField();
        birthDateTextField = new javax.swing.JFormattedTextField();
        typeComboBox = new javax.swing.JComboBox<>();
        cpfTextField = new javax.swing.JTextField();
        userTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        autorizationPasswordTextField = new javax.swing.JPasswordField();
        jSeparator2 = new javax.swing.JSeparator();
        addressPanel = new javax.swing.JPanel();
        cepTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        stateTextField = new javax.swing.JTextField();
        cityTextField = new javax.swing.JTextField();
        districtTextField = new javax.swing.JTextField();
        addressTextField = new javax.swing.JTextField();
        numberTextField = new javax.swing.JTextField();
        complementTextField = new javax.swing.JTextField();
        phoneTextField = new javax.swing.JTextField();
        cellPhoneTextField = new javax.swing.JTextField();
        emailTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setClosable(true);
        setTitle("RGD DESKTOP ERP - Registro de Colaboradores");
        setPreferredSize(new java.awt.Dimension(1200, 550));

        saveButton.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        saveButton.setForeground(java.awt.Color.white);
        saveButton.setText("Salvar");

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        codTextField.setEditable(false);
        codTextField.setBackground(new java.awt.Color(255, 255, 255));

        situationComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ativo", "Inativo" }));
        situationComboBox.setPreferredSize(new java.awt.Dimension(113, 24));

        typeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Impressor", "Arte Finalista", "Financeiro", "Representante", "Expedição" }));
        typeComboBox.setPreferredSize(new java.awt.Dimension(113, 24));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Código:");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Tipo:");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Situação:");

        jLabel9.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("*Nome:");

        jLabel10.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("RG:");

        jLabel11.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("*Usuário:");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("*CPF:");

        jLabel13.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Data de Nascimento:");

        jLabel12.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("*Senha:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel3.setText("Ex: 01/01/2017");

        jLabel25.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("*Senha de Autorização:");
        jLabel25.setToolTipText("");

        javax.swing.GroupLayout userPanelLayout = new javax.swing.GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(userPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(codTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(situationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(userPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(userPanelLayout.createSequentialGroup()
                        .addComponent(userTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pwTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(rgTextField)
                    .addComponent(nameTextField))
                .addGap(18, 18, 18)
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addComponent(birthDateTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addComponent(cpfTextField)
                    .addComponent(autorizationPasswordTextField))
                .addGap(28, 28, 28))
        );
        userPanelLayout.setVerticalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(userPanelLayout.createSequentialGroup()
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(codTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(userPanelLayout.createSequentialGroup()
                                .addGap(0, 8, Short.MAX_VALUE)
                                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rgTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(situationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(userPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(userTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pwTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(userPanelLayout.createSequentialGroup()
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cpfTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(jLabel3)
                        .addGap(1, 1, 1)
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(birthDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(autorizationPasswordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        searchButton.setBackground(new java.awt.Color(255, 255, 255));
        searchButton.setForeground(java.awt.Color.white);
        searchButton.setText("Buscar");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel4.setText("Ex: GO");

        jLabel16.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Endereço:");

        jLabel18.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("CEP:");

        jLabel14.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Endereço:");

        jLabel15.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Fone:");

        jLabel17.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Celular:");

        jLabel19.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("*Email:");

        jLabel20.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("UF:");

        jLabel22.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Cidade:");

        jLabel23.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Número:");

        jLabel21.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Bairro:");

        jLabel24.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Complemento:");

        javax.swing.GroupLayout addressPanelLayout = new javax.swing.GroupLayout(addressPanel);
        addressPanel.setLayout(addressPanelLayout);
        addressPanelLayout.setHorizontalGroup(
            addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addressPanelLayout.createSequentialGroup()
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addressPanelLayout.createSequentialGroup()
                        .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addressPanelLayout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cepTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(addressPanelLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel16)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(stateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addressPanelLayout.createSequentialGroup()
                        .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addressTextField)
                            .addComponent(emailTextField))))
                .addGap(18, 18, 18)
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(addressPanelLayout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(cityTextField))
                    .addGroup(addressPanelLayout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(numberTextField))
                    .addGroup(addressPanelLayout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(phoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(complementTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                    .addComponent(districtTextField)
                    .addComponent(cellPhoneTextField))
                .addGap(27, 27, 27))
        );
        addressPanelLayout.setVerticalGroup(
            addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addressPanelLayout.createSequentialGroup()
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addressPanelLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addressPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cepTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(districtTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(complementTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cellPhoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addComponent(jSeparator2)
            .addComponent(userPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(addressPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(userPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(addressPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addressPanel;
    private javax.swing.JTextField addressTextField;
    private javax.swing.JPasswordField autorizationPasswordTextField;
    private javax.swing.JFormattedTextField birthDateTextField;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JTextField cellPhoneTextField;
    private javax.swing.JTextField cepTextField;
    private javax.swing.JTextField cityTextField;
    private javax.swing.JTextField codTextField;
    private javax.swing.JTextField complementTextField;
    private javax.swing.JTextField cpfTextField;
    private javax.swing.JTextField districtTextField;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField numberTextField;
    private javax.swing.JTextField phoneTextField;
    private javax.swing.JPasswordField pwTextField;
    private javax.swing.JTextField rgTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JComboBox<String> situationComboBox;
    private javax.swing.JTextField stateTextField;
    private javax.swing.JComboBox<String> typeComboBox;
    private javax.swing.JPanel userPanel;
    private javax.swing.JTextField userTextField;
    // End of variables declaration//GEN-END:variables
}
