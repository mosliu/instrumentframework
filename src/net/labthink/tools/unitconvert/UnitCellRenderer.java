/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.tools.unitconvert;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Moses
 */
public class UnitCellRenderer implements ListCellRenderer {

    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);

        if (value instanceof Unit) {
            Unit u = (Unit)value;
            renderer.setText(u.getBaseName()+":"+u.getUnitName()+"  "+u.getComment());
        } else {
            renderer.setText("");
        }
        return renderer;

    }
}
