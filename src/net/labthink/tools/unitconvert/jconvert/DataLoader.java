package net.labthink.tools.unitconvert.jconvert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import net.labthink.tools.unitconvert.Unit;
import net.labthink.tools.unitconvert.UnitConverterFactory;
import net.labthink.tools.unitconvert.UnitType;

/**
 * This class is responsible for creating ConversionTypes by loading data from
 * the data files that contain the specifiactions. <br>
 * <br>
 * Here is an example of the datafile format:
 * <li>#Conversion Type Name, FromUnit, From Unit Abbr, To Unit, To Unit Abbr,
 * factor, offset
 * <li>Temperature,Celsius,�C,Celsius,�C,1,0
 * <li>Temperature,Celsius,�C,Fahrenheit,�F,9/5,32
 * <li>Time,century,,day,dy,36500,0
 * <li>Time,century,,decade,,10,0
 * <li>Time,century,,fortnight,,18250/7,0
 * <li>Time,century,,hour,hr,876000,0
 * <li>Time,century,,leap-year,,36500/366,0
 * <li>Time,century,,millennium,,0.10,0
 *
 * @author Ed Sarrazin Created on Jul 17, 2007 6:59:31 PM
 */
public class DataLoader {

    private static final String specialSaveChars = "=: \t\r\n\f#!";

    public static void main(String[] args) {
//        StringTokenizer stoken = new StringTokenizer("test1,test2,,test4".replaceAll(",", ", "), ",");
//        while (stoken.hasMoreTokens()) {
////            log.debug(stoken.nextToken());
//        }


        UnitConverterFactory ucf = new UnitConverterFactory();
        List domainData = new ArrayList();


        ucf.saveData(domainData, "outputUnitConvert.dat");

        DataLoader dl = new DataLoader();
        List la = dl.loadData();
        System.out.println(la);

        Iterator iter = la.iterator();
        while (iter.hasNext()) {
            ConversionType type = (ConversionType) iter.next();
            Collection cl = type.getConversions();
            System.out.println("type:" + type.getTypeName());

            UnitType ut = new UnitType();
            ut.setTypeName(type.getTypeName());




            Iterator iter2 = cl.iterator();
            Unit unit = null;
            while (iter2.hasNext()) {
                Conversion cu = (Conversion) iter2.next();
                if (cu.getToUnit().equalsIgnoreCase("g") || cu.getToUnitAbbr().equalsIgnoreCase("g")
                        || cu.getToUnit().equalsIgnoreCase("Celsius")
                        || cu.getToUnit().equalsIgnoreCase("second")
                        || (cu.getToUnit().equalsIgnoreCase("free fall") && cu.getToUnitAbbr().equalsIgnoreCase("g"))
                        || cu.getToUnitAbbr().equalsIgnoreCase("degree")
                        || cu.getToUnit().equalsIgnoreCase("meter\\u00B2")
                        || (cu.getToUnit().equalsIgnoreCase("meter") && cu.getToUnitAbbr().equalsIgnoreCase("m"))
                        || cu.getToUnit().equalsIgnoreCase("liter")
                        || cu.getToUnit().equalsIgnoreCase("pascal")
                        || cu.getToUnit().equalsIgnoreCase("gram/meter\\u00B3")
                        || cu.getToUnit().equalsIgnoreCase("joule")
                        || cu.getToUnit().equalsIgnoreCase("milliliter/second")
                        || cu.getToUnit().equalsIgnoreCase("newton")
                        || cu.getToUnit().equalsIgnoreCase("lux")
                        || (cu.getToUnit().equalsIgnoreCase("gram") && cu.getToUnitAbbr().equalsIgnoreCase("g"))
                        || cu.getToUnit().equalsIgnoreCase("watt")
                        || (cu.getToUnit().equalsIgnoreCase("mole") && cu.getToUnitAbbr().equalsIgnoreCase("mol"))
                        || cu.getToUnit().equalsIgnoreCase("mole/liter")
                        || cu.getToUnit().equalsIgnoreCase("meter/second")
                        || (cu.getToUnit().equalsIgnoreCase("newton meter") && cu.getToUnitAbbr().equalsIgnoreCase("Nm"))
                        || cu.getToUnit().equalsIgnoreCase("liter")) {

                    String tounit;
                    String fromunit;
                    if (cu.getToUnitAbbr().trim().length() == 0) {
                        tounit = cu.getToUnit();
                    } else {
                        tounit = cu.getToUnit() + "(" + cu.getToUnitAbbr() + ")";
                    }
                    if (cu.getFromUnitAbbr().trim().length() == 0) {
                        fromunit = cu.getFromUnit();
                    } else {
                        fromunit = cu.getFromUnit() + "(" + cu.getFromUnitAbbr() + ")";
                    }
                    boolean isbase = false;
                    if(cu.getFromUnit().equalsIgnoreCase(cu.getToUnit())){
                        isbase = true;
                    }
                    
                    if (cu instanceof FractionalConversion) {
                        double whole = cu.getFromToWholeNumber().doubleValue();
                        double top = cu.getFromToTopFactor().doubleValue();
                        double bottom = cu.getFromToBottomFactor().doubleValue();
                        double factor = top/bottom+whole;
                        if(factor==0){
                            int acasd = 0;//donothing
                        }
                        
                        unit = new Unit(type.getTypeName(), tounit, fromunit, isbase, factor, cu.getFromToOffset(), "");
                    } else {
                        unit = new Unit(type.getTypeName(), tounit, fromunit, isbase, cu.getFromToFactor(), cu.getFromToOffset(), "");
                    }


//                    System.out.println(cu.getToUnit());
                    ut.addConversions(unit);
                }
            }


            System.out.println("ut length:" + ut.getConversions().size());
            domainData.add(ut);
        }

        ucf.saveData(domainData, "outputUnitConvert.dat");
        
        List l = ucf.loadData("net/labthink/tools/unitconvert/outputUnitConvert.dat");
        System.out.println("lsize:"+l.size());
    }

    /**
     * Loads the data from the input stream into the HashMap of ConversionTypes.
     * If the conversion type does not exist in the map, one will be created.
     * This allows you to call this method multiple times and load data from
     * more than one file.
     *
     * @param stream InputStream that is pointing to a datafile to load.
     * @param map HashMap of ConversionTypes. Can start out empty or have data.
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
                    String tab = stok.nextToken().trim();
                    String fromUnit = stok.nextToken().trim();
                    String fromUnitAbbr = stok.nextToken().trim();
                    String toUnit = stok.nextToken().trim();
                    String toUnitAbbr = stok.nextToken().trim();
                    String factor = stok.nextToken().trim();
                    String offset = stok.nextToken().trim();

                    Conversion conversion = Conversion.createInstance(fromUnit, fromUnitAbbr, toUnit, toUnitAbbr,
                            factor, Double.parseDouble(offset));
                    ConversionType ct = getConversionType(map, tab);
                    ct.addConversion(conversion);
                }
            }
        } catch (FileNotFoundException ex) {
//            log.error("Could not load data from stream", ex);
        } catch (IOException ex) {
//            log.error("Could not load data from stream", ex);
        } catch (Exception ex) {
//            log.error("Could not load data from stream", ex);
        } finally {
            try {
                if (input != null) {
                    // flush and close both "input" and its underlying FileReader
                    input.close();
                }
            } catch (IOException ex) {
//                log.error("Could not load data from stream", ex);
            }
        }

    }

    /**
     * This method will load data from convert.dat and convert_custom.dat. It
     * will look in the classpath for these files.
     *
     * @return List of ConversionTypes
     */
    public List loadData() {
        //load each of the two files
        List main = loadData("net/labthink/tools/unitconvert/jconvert/convert.dat", false, null);
        List addOns = loadData("convert_custom.dat", true, main);

        //merge the data between the two.
        List rv = new ArrayList();
        rv.addAll(main);
        Iterator iter = addOns.iterator();
        while (iter.hasNext()) {
            ConversionType type = (ConversionType) iter.next();
            if (rv.contains(type)) {
                for (int i = 0; i < rv.size(); i++) {
                    ConversionType existing = (ConversionType) rv.get(i);
                    if (existing.equals(type)) {
                        mergeConversionType(existing, type);
                        break; //get out of for loop, we are done.
                    }
                }
            } else {
                rv.add(type);
            }
        }

        return rv;
    }

    /**
     * This method is responsible for merging the ConversionType data into the
     * existing ConversionType data. It uses some logic to determine if the new
     * data is non-existent in the existing data. If so it adds it. If it
     * already exists in the existing data, then it compares the two Conversions
     * and determines which conversion to use. This is simple logic looking at
     * the generation age of the data.
     *
     * @param dest ConversionType data that we want to merge into. This is
     * considered existing data
     * @param newData ConversionType data that we want to merge into the
     * destination.
     */
    private void mergeConversionType(ConversionType dest, ConversionType newData) {
        Iterator iter = newData.getConversions().iterator();
        while (iter.hasNext()) {
            Conversion newConversion = (Conversion) iter.next();
            if (dest.getConversions().contains(newConversion)) {
                Iterator iter2 = dest.getConversions().iterator();
                while (iter2.hasNext()) {
                    Conversion destConversion = (Conversion) iter2.next();
                    if (destConversion.equals(newConversion)) {
                        if (destConversion.getGenerationAge() >= newConversion.getGenerationAge()) {
                            dest.getConversions().remove(destConversion);
                            dest.getConversions().add(newConversion);
                        }
                        break; //shortcut out of while
                    }
                }
            } else {
                dest.getConversions().add(newConversion);
            }
        }
    }

    /**
     * This method is responsible for loading data from a resource into a list.
     * Existing conversion types are passed into this method to provide support
     * for generating gaps. For example if you create a conversion of decades to
     * doubledecades and that is it. We will look at the existing conversions to
     * also generate gaps such as doubledecades to year, months, days, etc.
     *
     * @param resourceName String name of the resource that has the data to be
     * loaded
     * @param generateGaps boolean that indicates whether or not to spend time
     * generating missing conversions. This is needed because generating these
     * gaps takes significant time and the default conversions that are supplied
     * will have all combinatorics created before hand
     * @param existingConversionTypes List of existing conversion types that
     * will be used in aiding generation of missing conversion types.
     * @return List of ConversionTypes.
     */
    public List loadData(String resourceName, boolean generateGaps, List existingConversionTypes) {
        HashMap map = new HashMap();
        Date date1 = new Date();

        loadDataFromStream(ResourceManager.getResourceAsStream(resourceName), map);

        Date date2 = new Date();
        List rv = new ArrayList(map.values());

        Iterator iter = rv.iterator();
        while (iter.hasNext()) {
            ConversionType ct = (ConversionType) iter.next();
            ConversionGapBuilder.createOneToOneConversions(ct);
        }

        if (!generateGaps) {
            return rv;
        }

        iter = rv.iterator();
        while (iter.hasNext()) {
            ConversionType ct = (ConversionType) iter.next();
            Iterator existingIter = existingConversionTypes.iterator();
            while (existingIter.hasNext()) {
                ConversionType existingCt = (ConversionType) existingIter.next();
                if (ct.getTypeName().equals(existingCt.getTypeName())) {
                    ct.getConversions().addAll(existingCt.getConversions());
                    break;
                }
            }
            ConversionGapBuilder.createMissingConversions(ct);
        }

        date1 = new Date();
//        log.debug("Created conversions - " + (date1.getTime() - date2.getTime()) + " miliseconds");
        //        this.unloadData(rv);
        return rv;
    }

    /**
     * Simply looks in the map for the conversionType (indicated by the string)
     * and returns it. If it is not found, then it creates it, puts it in the
     * HashMap and returns it to you.
     *
     * @param map HashMap of existing ConversionTypes
     * @param type String name of the conversion type
     * @return ConversionType
     */
    private ConversionType getConversionType(HashMap map, String type) {
        if (map.containsKey(type)) {
            return (ConversionType) map.get(type);
        }
        ConversionType ct = new ConversionType();
        ct.setTypeName(type);
        map.put(type, ct);
        return ct;
    }

    /**
     * Writes the List of ConversionTypes to output.dat.
     *
     * @param domainData List of ConversionTypes
     */
    public void unloadData(List domainData, String outputFileName) {
        //iterate through conversiontypes and write out.
        Iterator iter = domainData.iterator();
        try {
//            String jarPath = ResourceManager.getJarPath();
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
            while (iter.hasNext()) {
                ConversionType ct = (ConversionType) iter.next();
                List lst = new ArrayList();
                lst.addAll(ct.getConversions());
                Collections.sort(lst);
                Iterator iter2 = lst.iterator();
                while (iter2.hasNext()) {
                    Conversion c = (Conversion) iter2.next();
                    if (c instanceof FractionalConversion) {
                        String temp = ct.getTypeName() + "," + c.getFromUnit() + "," + c.getFromUnitAbbr() + ","
                                + c.getToUnit() + "," + c.getToUnitAbbr() + "," + c.getFromToTopFactor() + "/"
                                + c.getFromToBottomFactor() + "," + c.getFromToOffset() + "\n";
                        writer.write(saveConvert(temp, false));
                    } else {
                        String temp = ct.getTypeName() + "," + c.getFromUnit() + "," + c.getFromUnitAbbr() + ","
                                + c.getToUnit() + "," + c.getToUnitAbbr() + "," + c.getFromToFactor() + ","
                                + c.getFromToOffset() + "\n";
                        writer.write(saveConvert(temp, false));
                    }
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
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
}
