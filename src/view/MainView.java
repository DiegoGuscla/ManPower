/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import model.bean.Contributor;

/**
 *
 * @author RG Digital
 */
public class MainView extends javax.swing.JFrame {
  
    private DialogBox dialogBox;
    private JPanel imagePanel;
    private JDesktopPane mainPanel;
    private CarsForProductionListView carsForProductionListView;
    private ManufacturersListView manufacturersListView;
    private ModelsListView modelsListView;
    private ProducedCarsListView producedCarsListView;
    //private Contributor contributor;

    /**
     * Creates new form MainView
     *
     * @param contributor
     */
    public MainView(Contributor contributor) {
        //this.contributor = contributor;

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

        /*
        //Executa update caso necessário
        OrderDAO oDAO = new OrderDAO();
        try {
            oDAO.updates();
        } catch (SQLException ex) {
            getDialogBox().showDialogBox("Erro ao executar a Atualização no Banco. Erro: " 
                    + ex.getMessage(), "Atenção");
        }
        */
         
        this.getContentPane().setBackground(Color.white);
        this.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

        Container contentPane = this.getContentPane();
        contentPane.setBackground(Color.white);
        contentPane.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon(getClass().getResource(
                "/images/icon.png"));
        super.setIconImage(icon.getImage());

        //super.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/rgd/desktop/images/icon.ico")));
        imagePanel = new JPanel();
        imagePanel.setBackground(Color.white);
        contentPane.add(imagePanel, BorderLayout.CENTER);

        imagePanel.setLayout(new BorderLayout());

        JLabel picLabel = new JLabel();
        picLabel.setIcon(new ImageIcon(getClass().getResource(
                "/images/wp_manpower.png"))); // NOI18N
        picLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(picLabel, BorderLayout.CENTER);

        optionsMenuBar.setBackground(Color.decode("#006E96"));

        carsForProductionMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCarsForProductionListView();
            }
        });
        
        producedCarsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProducedCarsListView();
            }
        });
        
        manufacturerMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openManufacturersListView();
            }
        });
        
        modelsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openModelsListView();
            }
        });
    }

    private void openCarsForProductionListView() {
        imagePanel.setVisible(false);

        Container contentPane = this.getContentPane();
        contentPane.setBackground(Color.white);
        contentPane.setLayout(new BorderLayout());

        mainPanel = new JDesktopPane();
        mainPanel.setBackground(Color.white);
        contentPane.add(mainPanel);
        
        mainPanel.setLayout(new BorderLayout());

        carsForProductionListView = new CarsForProductionListView();
        carsForProductionListView.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                imagePanel.setVisible(true);
                carsForProductionListView = null;
                enableMainView();
                enableMenus(true);
            }
        });
        carsForProductionListView.setSize(1800, 800);
        carsForProductionListView.setPreferredSize(new Dimension(1800, 800));
        carsForProductionListView.setPosition(contentPane);
        mainPanel.add(carsForProductionListView);

        carsForProductionListView.toFront();
        carsForProductionListView.setVisible(true);
        enableMenus(false);
        this.revalidate();
    }
    
    private void openProducedCarsListView() {
        imagePanel.setVisible(false);

        Container contentPane = this.getContentPane();
        contentPane.setBackground(Color.white);
        contentPane.setLayout(new BorderLayout());

        mainPanel = new JDesktopPane();
        mainPanel.setBackground(Color.white);
        contentPane.add(mainPanel);
        
        mainPanel.setLayout(new BorderLayout());

        producedCarsListView = new ProducedCarsListView();
        producedCarsListView.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                imagePanel.setVisible(true);
                producedCarsListView = null;
                enableMainView();
                enableMenus(true);
            }
        });
        producedCarsListView.setSize(1800, 800);
        producedCarsListView.setPreferredSize(new Dimension(1800, 800));
        producedCarsListView.setPosition(contentPane);
        mainPanel.add(producedCarsListView);

        producedCarsListView.toFront();
        producedCarsListView.setVisible(true);
        enableMenus(false);
        this.revalidate();
    }
    
    private void openManufacturersListView() {
        imagePanel.setVisible(false);

        Container contentPane = this.getContentPane();
        contentPane.setBackground(Color.white);
        contentPane.setLayout(new BorderLayout());

        mainPanel = new JDesktopPane();
        mainPanel.setBackground(Color.white);
        contentPane.add(mainPanel);

        manufacturersListView = new ManufacturersListView();
        manufacturersListView.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                imagePanel.setVisible(true);
                manufacturersListView = null;
                enableMainView();
                enableMenus(true);
            }
        });
        manufacturersListView.setSize(1200, 600);
        manufacturersListView.setPreferredSize(new Dimension(1200, 600));
        manufacturersListView.setPosition(contentPane);
        mainPanel.add(manufacturersListView);

        manufacturersListView.toFront();
        manufacturersListView.setVisible(true);
        enableMenus(false);
        this.revalidate();
    } 
    
    private void openModelsListView() {
        imagePanel.setVisible(false);

        Container contentPane = this.getContentPane();
        contentPane.setBackground(Color.white);
        contentPane.setLayout(new BorderLayout());

        mainPanel = new JDesktopPane();
        mainPanel.setBackground(Color.white);
        contentPane.add(mainPanel);

        modelsListView = new ModelsListView();
        modelsListView.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                imagePanel.setVisible(true);
                modelsListView = null;
                enableMainView();
                enableMenus(true);
            }
        });
        modelsListView.setSize(1200, 600);
        modelsListView.setPreferredSize(new Dimension(1200, 600));
        modelsListView.setPosition(contentPane);
        mainPanel.add(modelsListView);

        modelsListView.toFront();
        modelsListView.setVisible(true);
        enableMenus(false);
        this.revalidate();
    }
    
    private void enableMenus(boolean enabled) {
        registerMenu.setEnabled(enabled);        
    }

    private DialogBox getDialogBox() {
        if (dialogBox == null) {
            dialogBox = new DialogBox(getContentPane());
        }

        return dialogBox;
    }

    private void enableMainView() {
        this.setEnabled(true);

        if (mainPanel != null) {
            this.getContentPane().remove(mainPanel);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        optionsMenuBar = new javax.swing.JMenuBar();
        registerMenu = new javax.swing.JMenu();
        usersMenuItem = new javax.swing.JMenuItem();
        manufacturerMenuItem = new javax.swing.JMenuItem();
        modelsMenuItem = new javax.swing.JMenuItem();
        productionMenu = new javax.swing.JMenu();
        carsForProductionMenuItem = new javax.swing.JMenuItem();
        producedCarsMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RGD DESKTOP ERP");
        setBackground(new java.awt.Color(146, 224, 252));

        optionsMenuBar.setBackground(new java.awt.Color(0, 110, 150));
        optionsMenuBar.setForeground(java.awt.Color.white);
        optionsMenuBar.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        optionsMenuBar.setMinimumSize(new java.awt.Dimension(80, 21));
        optionsMenuBar.setPreferredSize(new java.awt.Dimension(90, 50));

        registerMenu.setBackground(new java.awt.Color(0, 110, 150));
        registerMenu.setForeground(java.awt.Color.white);
        registerMenu.setText("Cadastros");
        registerMenu.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        registerMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        registerMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        registerMenu.setMinimumSize(new java.awt.Dimension(27, 25));
        registerMenu.setPreferredSize(new java.awt.Dimension(100, 19));

        usersMenuItem.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        usersMenuItem.setText("Usuários");
        usersMenuItem.setActionCommand("Usuários");
        usersMenuItem.setPreferredSize(new java.awt.Dimension(150, 40));
        registerMenu.add(usersMenuItem);

        manufacturerMenuItem.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        manufacturerMenuItem.setText("Fabricantes");
        manufacturerMenuItem.setPreferredSize(new java.awt.Dimension(103, 40));
        registerMenu.add(manufacturerMenuItem);

        modelsMenuItem.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        modelsMenuItem.setText("Modelos");
        modelsMenuItem.setPreferredSize(new java.awt.Dimension(103, 40));
        registerMenu.add(modelsMenuItem);

        optionsMenuBar.add(registerMenu);

        productionMenu.setBackground(new java.awt.Color(0, 110, 150));
        productionMenu.setForeground(java.awt.Color.white);
        productionMenu.setText("Produção");
        productionMenu.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        productionMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productionMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        productionMenu.setMinimumSize(new java.awt.Dimension(27, 25));
        productionMenu.setPreferredSize(new java.awt.Dimension(100, 19));

        carsForProductionMenuItem.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        carsForProductionMenuItem.setText("Carros em Produção");
        carsForProductionMenuItem.setPreferredSize(new java.awt.Dimension(150, 40));
        productionMenu.add(carsForProductionMenuItem);

        producedCarsMenuItem.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        producedCarsMenuItem.setText("Carros Produzidos");
        producedCarsMenuItem.setPreferredSize(new java.awt.Dimension(103, 40));
        productionMenu.add(producedCarsMenuItem);

        optionsMenuBar.add(productionMenu);

        setJMenuBar(optionsMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 989, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 621, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem carsForProductionMenuItem;
    private javax.swing.JMenuItem manufacturerMenuItem;
    private javax.swing.JMenuItem modelsMenuItem;
    private javax.swing.JMenuBar optionsMenuBar;
    private javax.swing.JMenuItem producedCarsMenuItem;
    private javax.swing.JMenu productionMenu;
    private javax.swing.JMenu registerMenu;
    private javax.swing.JMenuItem usersMenuItem;
    // End of variables declaration//GEN-END:variables
}
