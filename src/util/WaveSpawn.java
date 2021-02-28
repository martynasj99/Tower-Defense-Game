package util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WaveSpawn {

    private Properties props;

    public WaveSpawn() {
        setUpProps();
    }

    public List<Integer> getWave(int wave){
        String result = props.getProperty(Integer.toString(wave));

        List<Integer> res = new ArrayList<>();
        for(String s : result.split(",")){
            res.add(Integer.parseInt(s));
        }
        return res;
    }

    private void setUpProps(){
        this.props = new Properties();
        try{
            String fileName = "resources/waves.properties";
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if(inputStream != null){
                props.load(inputStream);
            }else{
                throw new FileNotFoundException("File " + fileName + " not found");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
