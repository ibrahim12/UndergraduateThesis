/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import IR.MyPair;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 *
 * @author Ibrahim
 */
public class Printer {

    public static void print(ArrayList array) {
        System.out.print("[ ");
        for (int i = 0; i < array.size(); i++) {
            System.out.print(array.get(i) + " ");
        }
        System.out.println(" ]");
    }

    public static void print(List array) {
        System.out.print("[ ");
        for (int i = 0; i < array.size(); i++) {
            System.out.print(array.get(i) + " ");
        }
        System.out.println(" ]");
    }

    public static void print(int[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println(" ]");
    }

    public static void print(int[][] array) {
        System.out.println("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print("\t[");
            for (int j = 0; j < array.length; j++) {
                System.out.print(" " + array[i][j] + " ");
            }
            System.out.println("]");
        }
        System.out.println("]");
    }

    public static void print(double[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println(" ]");
    }

    public static void print(String[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println(" ]");
    }

    public static void print(MyPair[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i].index + ":" + array[i].value + " ");
        }
        System.out.println(" ]");
    }

    public static void print(TreeSet<String> treeSet) {
        for (String element : treeSet) {
            System.out.println(element);
        }
    }

    public static void print(String value) {
        System.out.println(value);
    }

    public static void print(int value) {
        System.out.println(value);
    }

    public static void print(double value) {
        System.out.println(value);
    }

    public static void print(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            print(pairs.getKey() + " = " + pairs.getValue());
        }
    }

    public static void print(HashMap mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            Object key = pairs.getKey();
            Object value = pairs.getValue();

            if (key instanceof Integer) {
                print((Integer) key);
            } else if (key instanceof int[]) {
                print((int[]) key);
            } else if (key instanceof String) {
                print((String) key);
            }



            if (value instanceof Integer) {
                print((Integer) value);
            } else if (value instanceof int[]) {
                print((int[]) value);
            }else if (value instanceof String) {
                print((String) value);
            }

        }
    }

    public static void write(String path, String str) {
        write(path, str, true);
    }

    public static void write(String path, String str, boolean isAppened) {
        try {
            // Open the file that is the first
            // command line parameter
            FileOutputStream fstream = new FileOutputStream(path, isAppened);
            // Get the object of DataInputStream
            DataOutputStream in = new DataOutputStream(fstream);
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(in));
            br.write(str + '\n');
            // Close the input stream
            br.close();
            in.close();
            fstream.close();
        } catch (Exception e) {// Catch exception if any
            e.printStackTrace();
        }
    }

    public void print(ArrayList[] listArray) {
        for (int i = 0; i < listArray.length; i++) {
            Printer.print(listArray[i]);
        }
    }
}
