/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.tools.unitconvert;

import java.util.List;
import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Moses
 */
public class UnitCombleBoxModel extends DefaultComboBoxModel implements ComboBoxModel {

    Vector<Unit> items = null;
    Unit item = null;

    public UnitCombleBoxModel(Vector<Unit> pItems) {
        items = pItems;
    }

    public void setList(Vector<Unit> pItems) {
        items = pItems;
    }

    @Override
    public void addElement(Object element) {
        insertElementAt(element, 0);
    }

    @Override
    public int getSize() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    @Override
    public Object getElementAt(int index) {
        if (items == null) {
            return null;
        }
        return items.get(index);

    }

    @Override
    public void setSelectedItem(Object anItem) {
        item = (Unit) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return item;
    }
}
