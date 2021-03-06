/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;


/**
 * 
 * @author Ibrahim
 */
public class Reader {

	public static ArrayList ReadInList(String path) {
		ArrayList list = new ArrayList();
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				list.add(strLine);
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
		}
		return list;

	}

	public static ArrayList<ArrayList<Integer>> ReadInDataSet(String path) {
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				if (!strLine.equals("")) {
					String[] attr = strLine.split(",");
					ArrayList<Integer> attribues = new ArrayList<Integer>();
					for (String e : attr) {
						attribues.add(Integer.parseInt(e));
					}
					list.add(attribues);
				}
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
		}		
		return list;

	}

	public static HashMap<String, Integer> ReadInHashMap(String path) {
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			int index = 0;
			while ((strLine = br.readLine()) != null) {
				hashMap.put(strLine, 0);
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
		}
		return hashMap;

	}

	public static HashMap<String, Integer> ReadInHashMapWithValue(String path) {
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			int index = 0;
			while ((strLine = br.readLine()) != null) {
				String[] temp = strLine.split(" ");
				hashMap.put(temp[0], Integer.parseInt(temp[1]));
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
		}
		return hashMap;

	}
        
       public static ArrayList<Section> ReadIniFile(String path) throws IOException{
           
            Wini ini = new Wini(new File(path));
            ArrayList<Section> allConfig = (ArrayList<Section>) ini.getAll("config");
            return allConfig;

       }

}
