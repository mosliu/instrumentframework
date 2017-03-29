/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.tools.unitconvert.temp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.labthink.tools.unitconvert.Unit;
import net.labthink.tools.unitconvert.UnitConverterFactory;
import net.labthink.tools.unitconvert.UnitType;

/**
 *
 * @author Moses
 */
public class JConvertRead {

    /**
     * Array of string labels.
     */
    String[] p_label;
    /**
     * HTML page encoding.
     */
    String p_page_encoding = "utf-8";
    /**
     * Charset encoding.
     */
    String p_encoding = "utf-8";
    /**
     * Array of category names.
     */
    String[] p_category_name;
    /**
     * Array of Multiple/Submultiple names.
     */
    String[] p_multiplier_name;
    /**
     * Array of Multiple/Submultiple values.
     */
    Double[] p_multiplier_value;
    /**
     * Array of Multiple/Submultiple descriptions.
     */
    String[] p_multiplier_description;
    /**
     * Array of category ID (link to category table: p_category_id).
     */
    Integer[] p_unit_category_id;
    /**
     * Array of unit of measure symbols.
     */
    String[] p_unit_symbol;
    /**
     * Array of unit of measure names.
     */
    String[] p_unit_name;
    /**
     * Array of unit of measure descriptions.
     */
    String[] p_unit_description;
    /**
     * Array of unit of measure conversion scale factors.
     */
    Double[] p_unit_scale;
    /**
     * Array of unit of measure conversion offsets.
     */
    Double[] p_unit_offset;
    /**
     * Array of powers to apply to unit multipliers.
     */
    Double[] p_unit_power;

    public static void main(String[] args) {
        JConvertRead jcr = new JConvertRead();
        jcr.readDataFile(1, "./catdata.txt"); //read data from external text file
        jcr.readDataFile(2, "./unitdata.txt");
        int size = jcr.p_unit_category_id.length;
        HashMap map = new HashMap();//
        Unit u = null;
        int kindindex = -1;
        for (int i = 0; i < size; i++) {
            boolean flag = false;
            if (kindindex != jcr.p_unit_category_id[i]) {
                kindindex = i;
//                flag=true;
            }
            //        Unit unit = new Unit("Weight","g","g",true,1,0,"");
            u = new Unit(jcr.p_category_name[jcr.p_unit_category_id[i]], jcr.p_unit_symbol[kindindex], jcr.p_unit_symbol[i], flag, jcr.p_unit_scale[i], jcr.p_unit_offset[i], jcr.p_unit_name[i]);
            UnitType ct = UnitConverterFactory.getUnitType(map, jcr.p_category_name[jcr.p_unit_category_id[i]]);
            ct.addConversions(u);
        }
        for (Iterator it = map.values().iterator(); it.hasNext();) {
            UnitType ct = (UnitType) it.next();
            HashSet hs = (HashSet) ct.getConversions();
            Unit tempu = null;
            for (Iterator it1 = hs.iterator(); it1.hasNext();) {
                Unit u2 = (Unit) it1.next();
                if (u2.getUnitFactor() == 1d && u2.getUnitOffset() == 0d) {
                    tempu = u2;
                    tempu.setIsBaseUnit(true);
                    break;
                }
            }
            if (tempu != null) {
                for (Iterator it1 = hs.iterator(); it1.hasNext();) {
                    Unit u2 = (Unit) it1.next();
                    u2.setBaseUnit(tempu.getUnitName());
                }
            }
        }


        List domainData = new ArrayList(map.values());

        UnitConverterFactory ucf = new UnitConverterFactory();
        ucf.saveData(domainData, "outputUnitConvert2.dat");

        System.out.println("1");
    }

    /**
     * Read menu items data from external text file "\n" separate items "\t"
     * separate values
     *
     * @param filetype 0=multiplier file, 1= category file, 2=units file
     * @param filename the text file containing menu data
     */
    public void readDataFile(int filetype, String filename) {
        int nfields = 7; //number of data fields
        int i = 0; //temp elements counter
        int num_elements = 0; //number of items (lines)
        String dataline;
        String[] elementdata;
        try {
            //open data file
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            //count elements
            while (null != (dataline = in.readLine())) {
                i++;
            }
            in.close();
            num_elements = i;
            //num_elements = i+1;
            //set arrays size by case

            if (filetype == 1) {
                setCategoriesArraySize(num_elements);
                nfields = 1;
            } else if (filetype == 2) {
                setUnitsArraySize(num_elements);
                nfields = 7;
            }


            i = 0;
            in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            //read lines (each line is one menu element)
            while (null != (dataline = in.readLine())) {
                //get element data array
                elementdata = splitData(dataline, '\t', nfields);
                //assign data
                if (filetype == 0) { //multipliers file
                    p_multiplier_name[i] = getEncodedString(getDefaultValue(elementdata[0], ""), p_page_encoding, p_encoding);
                    p_multiplier_description[i] = getEncodedString(getDefaultValue(elementdata[1], ""), p_page_encoding, p_encoding);
                    p_multiplier_value[i] = parseNumber(getDefaultValue(elementdata[2], "1"));
                } else if (filetype == 1) { //category file
                    p_category_name[i] = getEncodedString(getDefaultValue(elementdata[0], ""), p_page_encoding, p_encoding);
                } else if (filetype == 2) { //units file
                    p_unit_category_id[i] = new Integer(elementdata[0]);
                    p_unit_symbol[i] = getEncodedString(getDefaultValue(elementdata[1], ""), p_page_encoding, p_encoding);
                    p_unit_name[i] = getEncodedString(getDefaultValue(elementdata[2], ""), p_page_encoding, p_encoding);
                    p_unit_scale[i] = parseNumber(getDefaultValue(elementdata[3], "1"));
                    p_unit_offset[i] = parseNumber(getDefaultValue(elementdata[4], "0"));
                    p_unit_power[i] = parseNumber(getDefaultValue(elementdata[5], "1"));
                    p_unit_description[i] = getEncodedString(getDefaultValue(elementdata[6], ""), p_page_encoding, p_encoding);
                } else if (filetype == 3) { //labels file
                    p_label[i] = getEncodedString(getDefaultValue(elementdata[0], ""), p_page_encoding, p_encoding);
                }
                i++;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * set arrays size for units
     *
     * @param i size of array
     */
    private void setUnitsArraySize(int i) {
        p_unit_category_id = new Integer[i];
        p_unit_symbol = new String[i];
        p_unit_name = new String[i];
        p_unit_description = new String[i];
        p_unit_scale = new Double[i];
        p_unit_offset = new Double[i];
        p_unit_power = new Double[i];
    }

    /**
     * Split a string in array of predefined size
     *
     * @param input_string string to split
     * @param sep_ch separator character
     * @param size max elements to retrieve, remaining elements will be filled
     * with empty string
     * @return splitted_array of strings
     */
    private String[] splitData(String input_string, char sep_ch, int size) {
        String str1 = new String(); // temp var to contain found strings
        String splitted_array[] = new String[size]; // array of splitted string to return
        int element_num = 0; //number of found elements
        // analize string char by char
        for (int i = 0; i < input_string.length(); i++) {
            if (input_string.charAt(i) == sep_ch) { //separator found
                splitted_array[element_num] = str1; //put string to array
                str1 = new String(); //reinitialize variable
                element_num++; //count strings
                if (element_num >= size) {
                    break; //quit if limit is reached
                }
            } else {
                str1 += input_string.charAt(i);
            }
        }
        //get last element
        if (element_num < size) {
            splitted_array[element_num] = str1; //put string to vector
            element_num++;
        }
        //fill remaining values with empty string
        for (int i = element_num; i < size; i++) {
            splitted_array[i] = "";
        }
        return splitted_array;
    }

    /**
     * Return "def" if "str" is null or empty.
     *
     * @param str value to return if not null
     * @param def default value to return
     * @return def or str by case
     */
    private String getDefaultValue(String str, String def) {
        if ((str != null) && (str.length() > 0)) {
            return str;
        }
        return def;
    }

    /**
     * Convert string to specified encoding.
     *
     * @param original original string
     * @param encoding_in input encoding table
     * @param encoding_out output encoding table
     * @return encoded string
     */
    private String getEncodedString(String original, String encoding_in, String encoding_out) {
        String encoded_string;
        if (encoding_in.compareTo(encoding_out) != 0) {
            byte[] encoded_bytes;
            try {
                encoded_bytes = original.getBytes(encoding_in);
            } catch (UnsupportedEncodingException e) {
                System.out.println("Unsupported Charset: " + encoding_in);
                return original;
            }
            try {
                encoded_string = new String(encoded_bytes, encoding_out);
                return encoded_string;
            } catch (UnsupportedEncodingException e) {
                //e.printStackTrace();
                System.out.println("Unsupported Charset: " + encoding_out);
                return original;
            }
        }
        return original;
    }

    /**
     * simple number parser (allows to use math operators operators:
     * +,-,*,/,^,P=PI,X=exp) operator precedence: P X * / + - ^
     *
     * @param num string to parse
     * @return Double parsed number
     */
    private Double parseNumber(String num) {
        Double tempnum = new Double(0);
        int opos; //operator position
        if ((num == null) || (num.length() < 1)) {
            return tempnum;
        }

        //replace constants with their value
        while (num.indexOf("P") >= 0) { //PI constant
            String[] numparts = splitData(num, 'P', 2);
            num = numparts[0] + String.valueOf(Math.PI) + numparts[1];
        }
        while (num.indexOf("X") >= 0) { //e constant
            String[] numparts = splitData(num, 'X', 2);
            num = numparts[0] + String.valueOf(Math.E) + numparts[1];
        }

        if (num.indexOf("^") >= 0) { //allows to specify powers (e.g.: 2^10)
            String[] numparts = splitData(num, '^', 2);
            tempnum = new Double(Math.pow(parseNumber(numparts[0]).doubleValue(), parseNumber(numparts[1]).doubleValue()));
        } else if (((opos = num.indexOf("-")) > 0) && (num.charAt(opos - 1) != 'E') && (num.charAt(opos - 1) != '^')) {
            String[] numparts = splitData(num, '-', 2);
            tempnum = new Double(parseNumber(numparts[0]).doubleValue() - parseNumber(numparts[1]).doubleValue());
        } else if (((opos = num.indexOf("+")) > 0) && (num.charAt(opos - 1) != 'E') && (num.charAt(opos - 1) != '^')) {
            String[] numparts = splitData(num, '+', 2);
            tempnum = new Double(parseNumber(numparts[0]).doubleValue() + parseNumber(numparts[1]).doubleValue());
        } else if (num.indexOf("/") >= 0) {
            String[] numparts = splitData(num, '/', 2);
            tempnum = new Double(parseNumber(numparts[0]).doubleValue() / parseNumber(numparts[1]).doubleValue());
        } else if (num.indexOf("*") >= 0) {
            String[] numparts = splitData(num, '*', 2);
            tempnum = new Double(parseNumber(numparts[0]).doubleValue() * parseNumber(numparts[1]).doubleValue());
        } else {
            tempnum = Double.valueOf(num);
        }

        return tempnum;

    }

    /**
     * set arrays size for unit categories
     *
     * @param i size of array
     */
    private void setCategoriesArraySize(int i) {
        p_category_name = new String[i];
    }
}