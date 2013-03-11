/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import java.security.SecureRandom;

/**
 *
 * @author Ibrahim
 */
public class Util {

    /*
     * Return 1 Random between 0 to max (exclusive)
	 *
     */
    public static int RandomInt(int max) {
        return new SecureRandom().nextInt(max);
    }

    /*
     * Return 1 Random Int between min (inclusive ) to max (inclusive)
	 *
     */
    public static int RandomInt(int min, int max) {
        return new SecureRandom().nextInt(max - min + 1) + min;
    }

    /*
     * Return N Random Number from min (inclusive ) to max (exclusive)
	 *
     */
    public static int[] GetNRandomInt(int n, int min, int max) {
        ArrayList<Integer> indexArray = new ArrayList();
        for (int i = min; i < max; i++) {
            indexArray.add(i);
        }        
        Collections.shuffle(indexArray, new Random());
        
        int[] randomInt = new int[n];
        for (int i = 0; i < n; i++) {
            randomInt[i] = indexArray.get(i);
        }
        return randomInt;
    }

    public static double GetRandom() {
        
        return new SecureRandom().nextDouble();
    }

    public static String join(Collection s, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator iter = s.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }

    public static int GetIntValueSlow(int[] values) {

        int integerValue = 0;
        for (int i = 0; i < values.length; i++) {
            integerValue += values[i] * Math.pow(2, values.length - 1 - i);
        }
        return integerValue;
    }

//	public static int[] GetBinaryValueSlow(int value) {
//
//		int[] binaryValue = new int[Constants.ATTR_ENCODED_BIT];
//		int index = Constants.ATTR_ENCODED_BIT - 1;
//		while( index >= 0 )
//		{
//			binaryValue[index--] = (value & 1) == 1 ? 1 : 0;
//			value >>= 1;
//		}	
//		return binaryValue;
//
//	}
//	public static int GetIntValue(int[] values) {
//	    
//	    String temp = IntArray2String(values);
//	    return Constants.binary2Int.get(temp);	    
//	}
//
//	public static int[] GetBinaryValue(int value) {
//
//		return Constants.int2Binary.get(value);
//
//	}
    public static boolean InArray(int key, Integer[] array) {
        return Arrays.asList(array).contains(key);

    }

    public static double Log2(double num) {
        return (Math.log(num) / Math.log(2));
    }

//	public static boolean InArray(int key,Integer[] array){	    
//	    for(int i=0;i<array.length;i++){
//		if(array[i] == key)
//		    return true;
//	    }
//	    return false;	    
//	}
    public static boolean InArray(double key, double[] array) {
        return Arrays.asList(array).contains(key);

    }

    public static boolean InArray(String key, String[] array) {
        return Arrays.asList(array).contains(key);

    }

    public static String IntArray2String(int[] array) {
        String temp = "";
        for (int i = 0; i < array.length; i++) {
            temp += array[i];
        }
        return temp;
    }

    public static void SortLexographicaly(ArrayList< ArrayList<Integer>> listToSort) {


        Collections.sort(listToSort, new Comparator< ArrayList<Integer>>() {

            @Override
            public int compare(ArrayList<Integer> instance1, ArrayList<Integer> instance2) {

                for (int i = 0; i < instance1.size(); i++) {
                    if (instance1.get(i) != instance2.get(i)) {
                        if (instance1.get(i) < instance2.get(i)) {
                            return -1;
                        } else {
                            return +1;
                        }
                    }

                }
                return 0;
            }
        });

    }

    public static int GetRandomRaulatteIndex(double[] probabilities) {

        double randomValue = Util.GetRandom();
        int index = 0;
        for (int j = 0; j < probabilities.length - 1; j++) {
            if (randomValue <= probabilities[j + 1] && randomValue >= probabilities[j]) {
                index = j;
                break;
            }
        }
        // Printer.print(randomValue);
        // Printer.print(probabilities);
        return index;
    }
    public static int GetRandomRaulatteIndex(ArrayList<Double> probabilities) {

        double randomValue = Util.GetRandom();
        int index = 0;
        for (int j = 0; j < probabilities.size() - 1; j++) {
            if (randomValue <= probabilities.get(j + 1)
                    && randomValue >= probabilities.get(j)) {
                index = j;
                break;
            }
        }
        // Printer.print(randomValue);
        // Printer.print(probabilities);
        return index;
    }
    
    public static String insert(String beforeChar,String src,String add){
    	int i = src.indexOf(beforeChar);
    	String temp = src.substring(0,i);
		return temp + add + src.substring(i);
    }
}
