/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.tools.unitconvert;

/**
 * 描述单位的基类
 *
 * @author Moses
 */
public class Unit implements Comparable {

    private String BaseName;//类型名
//    private int UnitKind_no;//类型号
    private String BaseUnit;//基准单位
    private String UnitName;//单位名称
//    private int Unitno;//单位编号
    private double UnitFactor;//系数因子 乘数计算，例如1bl 即g乘以453.59237 即1bl=453.59237g
    private double UnitOffset;//偏移因子 以加数算，即本单位加一个数可以得到基准单位
    private boolean IsBaseUnit;//是否是基准单位，例如g对于lb来说是基准
    private String Comment;//备注

    /**
     *
     * @param BaseName 类型名
     * @param BaseUnit 基准单位
     * @param UnitName 单位名称
     * @param UnitFactor 系数因子 乘数计算，例如1bl 乘以453.59237 即1bl=453.59237g
     * @param UnitOffset 偏移因子 以加数算，即本单位加一个数可以得到基准单位
     * @param IsBaseUnit 是否是基准单位，例如g对于lb来说是基准
     * @param Comment 备注
     */
    public Unit(String BaseName, String BaseUnit, String UnitName, boolean IsBaseUnit, double UnitFactor, double UnitOffset, String Comment) {
        this.BaseName = BaseName;
        this.BaseUnit = BaseUnit;
        this.UnitName = UnitName;
        this.UnitFactor = UnitFactor;
        this.UnitOffset = UnitOffset;
        this.IsBaseUnit = IsBaseUnit;
        this.Comment = Comment;
    }

    
    /**
     *
     * @param _basename 类型名
     * @param _baseunit 类型名
     * @param _unitname 单位名称
     * @param _isbaseunit 单位名称
     * @param _unitfactor 系数因子 乘数计算，例如1bl 乘以453.59237 即1bl=453.59237g
     * @param _unitoffset 偏移因子 以加数算，即本单位加一个数可以得到基准单位
     * @return
     */
    protected static Unit createInstance(String _basename, String _baseunit, String _unitname, String _isbaseunit, String _unitfactor, String _unitoffset, String _comment) {
        Unit unit = null;
        _unitfactor = _unitfactor.trim();
        _unitoffset = _unitoffset.trim();
        double d_factor = Double.parseDouble(_unitfactor);
        double d_offset = Double.parseDouble(_unitoffset);
        boolean isbase = _isbaseunit.equalsIgnoreCase("true");

        unit = new Unit(_basename, _baseunit, _unitname, isbase, d_factor, d_offset, _comment);



        return unit;
    }

    

    /**
     * @return the BaseName
     */
    public String getBaseName() {
        return BaseName;
    }

    /**
     * @param BaseName the BaseName to set
     */
    public void setBaseName(String BaseName) {
        this.BaseName = BaseName;
    }

    /**
     * @return the BaseUnit
     */
    public String getBaseUnit() {
        return BaseUnit;
    }

    /**
     * @param BaseUnit the BaseUnit to set
     */
    public void setBaseUnit(String BaseUnit) {
        this.BaseUnit = BaseUnit;
    }

    /**
     * @return the UnitName
     */
    public String getUnitName() {
        return UnitName;
    }

    /**
     * @return the UnitName
     */
    public String getUnitNameAbbr() {
        if (UnitName.indexOf("(") != -1) {
            return UnitName.substring(UnitName.indexOf("(")+1, UnitName.lastIndexOf(")"));
        } else {
            return UnitName;
        }
    }

    /**
     * @param UnitName the UnitName to set
     */
    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }

    /**
     * @return the UnitFactor
     */
    public double getUnitFactor() {
        return UnitFactor;
    }

    /**
     * @param UnitFactor the UnitFactor to set
     */
    public void setUnitFactor(double UnitFactor) {
        this.UnitFactor = UnitFactor;
    }

    /**
     * @return the UnitOffset
     */
    public double getUnitOffset() {
        return UnitOffset;
    }

    /**
     * @param UnitOffset the UnitOffset to set
     */
    public void setUnitOffset(double UnitOffset) {
        this.UnitOffset = UnitOffset;
    }

    /**
     * @return the IsBaseUnit
     */
    public boolean isIsBaseUnit() {
        return IsBaseUnit;
    }

    /**
     * @param IsBaseUnit the IsBaseUnit to set
     */
    public void setIsBaseUnit(boolean IsBaseUnit) {
        this.IsBaseUnit = IsBaseUnit;
    }

    /**
     * @return the Comment
     */
    public String getComment() {
        return Comment;
    }

    /**
     * @param Comment the Comment to set
     */
    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    @Override
    public int compareTo(Object obj) {
        if (obj instanceof Unit) {
            Unit o = (Unit) obj;
            int rv = this.getUnitName().compareTo(o.getUnitName());
            return rv;
        }
        return -1;
    }

    public double computefactor(Unit tounit) {
        return computefactor(tounit, 1d);
    }

    public double computefactor(Unit tounit, double value) {
        //暂不支持华氏度

        double rtn = 0;
        //from转化为标准单位 1/this.UnitFactor;
        //再把标准单位转化 tounit.UnitFactor / this.UnitFactor;
        if (this.UnitFactor == 0) {
            return 0d;
        }
        rtn = tounit.UnitFactor / this.UnitFactor;
        return rtn * value;
    }
}
