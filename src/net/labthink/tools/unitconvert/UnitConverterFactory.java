/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.tools.unitconvert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import net.labthink.utils.other.ResourceManager;

/**
 * 单位转换工厂<br/>
 * 用于加载单位、生成转换项等一切对外工作。 单例模式
 *
 * @author Moses
 */
public class UnitConverterFactory {

    List conversionTypes = null;
    HashMap map = new HashMap();//
    Vector allunit = new Vector();

    public UnitConverterFactory() {
        conversionTypes = this.loadData("net/labthink/tools/unitconvert/outputUnitConvert.dat");
        rebuildAllUnit();
    }

    public static void main(String[] args) {
//        File f = new File(".");
//        System.out.println(f.getAbsolutePath());
        UnitConverterFactory ucf = new UnitConverterFactory();
//        List domainData = new ArrayList();
//        UnitType ut= new UnitType();
//        ut.setTypeName("Weight");
//        Unit unit = new Unit("Weight","g","g",true,1,0,"");
//        ut.addConversions(unit);
//        unit = new Unit("Weight","g","kg",true,0.001d,0,"");
//        ut.addConversions(unit);
//        domainData.add(ut);
//        
//        ucf.saveData(domainData,"outputUnitConvert.dat");


        List l = ucf.loadData("net/labthink/tools/unitconvert/outputUnitConvert.dat");
        Iterator iter = l.iterator();
        while (iter.hasNext()) {
            UnitType ut = (UnitType) iter.next();
            System.out.println(ut.getTypeName() + " baseuint is:" + ut.getBaseUnit().getUnitName());
        }
        System.out.println(l);
    }

    public Vector getComboVector() {
        return allunit;
    }

    /**
     * Writes the List to file.
     *
     * @param domainData List
     */
    public void saveData(List domainData, String outputFileName) {
        //iterate through conversiontypes and write out.

        Iterator iter = domainData.iterator();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./" + outputFileName));
            while (iter.hasNext()) {
                UnitType utype = (UnitType) iter.next();
                List lst = new ArrayList();
                lst.addAll(utype.getConversions());
                Collections.sort(lst);
                Iterator iter2 = lst.iterator();
                while (iter2.hasNext()) {
                    Unit unit = (Unit) iter2.next();

                    String temp = unit.getBaseName() + "," + unit.getBaseUnit() + "," + unit.getUnitName() + "," + unit.isIsBaseUnit() + ","
                            + unit.getUnitFactor() + "," + unit.getUnitOffset() + "," + unit.getComment();
                    temp = saveConvert(temp, false);
                    temp = temp + "\r\n";
                    writer.write(temp);
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List loadData(String resourceName) {

        Date date1 = new Date();
        InputStream is = ResourceManager.getResourceAsStream(resourceName);
//        System.out.println("is:"+is);
        loadDataFromStream(is, map);

        Date date2 = new Date();
        System.out.println("Loaded file - " + (date2.getTime() - date1.getTime()) + " miliseconds");
        List rv = new ArrayList(map.values());

        return rv;
    }

    /**
     * 从文件中读出单位
     *
     * @param stream
     * @param map
     */
    private void loadDataFromStream(InputStream stream, HashMap map) {
        if (stream == null) {
            return;
        }
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(stream, "8859_1"));
            String line = null;

            while ((line = input.readLine()) != null) {
                line = loadConvert(line);
                StringTokenizer stok = new StringTokenizer(line.replaceAll(",", ", "), ",");
                if (!line.startsWith("#") && stok.countTokens() == 7) {
                    String _basename = stok.nextToken().trim();
                    String _baseunit = stok.nextToken().trim();
                    String _unitname = stok.nextToken().trim();
                    String _isbaseunit = stok.nextToken().trim();
                    String _unitfactor = stok.nextToken().trim();
                    String _unitoffset = stok.nextToken().trim();
                    String _comment = stok.nextToken().trim();

                    Unit unit = Unit.createInstance(_basename, _baseunit, _unitname, _isbaseunit, _unitfactor, _unitoffset, _comment);

                    UnitType ct = getUnitType(map, _basename);
                    ct.addConversions(unit);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Could not load data from stream");
        } catch (IOException ex) {
            System.out.println("Could not load data from stream");
        } catch (Exception ex) {
            System.out.println("Could not load data from stream");
        } finally {
            try {
                if (input != null) {
                    // flush and close both "input" and its underlying FileReader
                    input.close();
                }
            } catch (IOException ex) {
                System.out.println("Could not load data from stream");
            }
        }

    }

    /**
     * Simply looks in the map for the conversionType (indicated by the string)
     * and returns it. If it is not found, then it creates it, puts it in the
     * HashMap and returns it to you.
     *
     * @param _map HashMap of existing ConversionTypes
     * @param type String name of the conversion type
     * @return ConversionType
     */
    public static UnitType getUnitType(HashMap _map, String type) {
        if (_map.containsKey(type)) {
            return (UnitType) _map.get(type);
        }
        UnitType ct = new UnitType();
        ct.setTypeName(type);
        _map.put(type, ct);
        return ct;
    }

    public UnitType getUnitTypeFromMap(String type) {
        if (map.containsKey(type)) {
            return (UnitType) map.get(type);
        } else {
            return null;
        }
    }

    /**
     * This method was taken from java.util.Properties Converts encoded
     * &#92;uxxxx to unicode chars and changes special saved chars to their
     * original forms
     */
    private String loadConvert(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    /**
     * This method was taken from java.util.Properties Converts unicodes to
     * encoded &#92;uxxxx and writes out any of the characters in
     * specialSaveChars with a preceding slash
     */
    private String saveConvert(String theString, boolean escapeSpace) {
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len * 2);

        for (int x = 0; x < len; x++) {
            char aChar = theString.charAt(x);
            switch (aChar) {
                case ' ':
                    if (x == 0 || escapeSpace) {
                        outBuffer.append('\\');
                    }

                    outBuffer.append(' ');
                    break;
                case '\\':
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    break;
                case '\t':
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    break;
                case '\n':
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    break;
                case '\r':
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    break;
                case '\f':
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    break;
                default:
                    if ((aChar < 0x0020) || (aChar > 0x007e)) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >> 8) & 0xF));
                        outBuffer.append(toHex((aChar >> 4) & 0xF));
                        outBuffer.append(toHex(aChar & 0xF));
                    } else {
                        if (specialSaveChars.indexOf(aChar) != -1) {
                            outBuffer.append('\\');
                        }
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }
    private static final String specialSaveChars = "=: \t\r\n\f#!";

    /**
     * Convert a nibble to a hex character This method was taken from
     * java.util.Properties
     *
     * @param nibble the nibble to convert.
     */
    private static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }
    /**
     * A table of hex digits This method was taken from java.util.Properties
     */
    private static final char[] hexDigit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
        'F'};

    private void rebuildAllUnit() {
        Collections.sort(conversionTypes);
        Iterator iter = conversionTypes.iterator();
        while (iter.hasNext()) {
            UnitType ut = (UnitType) iter.next();
            allunit.addAll(ut.getConversions());
        }
    }
}
