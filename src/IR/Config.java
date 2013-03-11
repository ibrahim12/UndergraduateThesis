/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IR;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.ini4j.Wini;

/**
 *
 * @author ibrahim
 */
public class Config {
    
    private String config_ini_path = null;
    Map<String, String> allConfig = null;
    private String default_section = "config";
    Wini ini = null;

    public Config(String ini_path) {
        config_ini_path = ini_path;
    }
    
    public void ReadIni() throws IOException{
        ini = new Wini(new File(config_ini_path));
        allConfig = ini.get(default_section);
    }
    
    public String get(String key){
        return allConfig.get(key);
    }
    
    public Integer getInt(String key){
        return Integer.parseInt(allConfig.get(key));
    }
    
    public Double getDouble(String key){
        return Double.parseDouble(allConfig.get(key));
    }
    
    public void set(String key,String value) throws IOException{
        ini.put(default_section,key,value);
        ini.store();
    }

    @Override
    public String toString() {        
        return "Config{" + "allConfig=" + allConfig + ", default_section=" + default_section + '}';
    }
    
    
    
}
