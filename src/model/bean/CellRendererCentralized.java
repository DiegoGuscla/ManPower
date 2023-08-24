/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.bean;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author diego
 */
public class CellRendererCentralized extends DefaultTableCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CellRendererCentralized() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        this.setHorizontalAlignment(CENTER);
        
        Component c = super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
        Color color2 = new Color(248, 248, 248);
        Color color1 = Color.WHITE;
        if (!c.getBackground().equals(table.getSelectionBackground())) {
            Color coleur = (row % 2 == 0 ? color1 : color2);
            c.setBackground(coleur);
            coleur = null;
        }
        return c;
        /*
        return super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
        */
    }
}
