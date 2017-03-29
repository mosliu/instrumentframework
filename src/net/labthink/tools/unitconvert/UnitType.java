/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.tools.unitconvert;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Moses
 */
public class UnitType implements Comparable {

    private String typeName;
    private Collection conversions = new HashSet();
    private Unit baseunit = null;

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * @return the conversions
     */
    public Collection getConversions() {
        return conversions;
    }

    public Vector getConversionsVector(){
        return new Vector(conversions);
    }
    
    /**
     * @param conversions the conversions to set
     */
    public void setConversions(Collection conversions) {
        this.conversions = conversions;
    }

    /**
     * @param conversions the conversions to set
     */
    public void addConversions(Unit ub) {
        this.conversions.add(ub);
    }



    /**
     * @param baseunit the baseunit to set
     */
    public void setBaseUnit(Unit baseunit) {
        this.baseunit = baseunit;
    }

    public Unit getBaseUnit() {
        if(baseunit != null){
            return baseunit;
        }
        HashSet a = (HashSet) getConversions();
        for (Iterator it = a.iterator(); it.hasNext();) {
            Unit unit = (Unit) it.next();
            if (unit.isIsBaseUnit()) {
                setBaseUnit(unit);
            }
        }
        if(baseunit==null){
            System.out.println("Caution:this UnitType does not have baseunit!");
        }
        return baseunit;
    }

    public void printDetails() {
        Iterator iter = conversions.iterator();
        while (iter.hasNext()) {
            Unit conv = (Unit) iter.next();
            System.out.println(typeName + ":" + conv.getUnitName());
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof UnitType) {
            if (((UnitType) obj).getTypeName().equals(this.getTypeName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Object obj) {
        if (obj instanceof UnitType) {
            UnitType o = (UnitType) obj;
            int rv = this.getTypeName().compareTo(o.getTypeName());
            return rv;
        }
        return -1;
    }
}
