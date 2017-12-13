package project.distributed.policyserver.manager;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Properties;

@Service
public class PropertiesManager {

    public String readFromPropertyFile() {
        Properties prop = new Properties();
        InputStream input = null;
        String property = null;

        try {

            input = new FileInputStream("policyserver.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            property = prop.getProperty("fetch_policy");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return property;
    }

    public boolean writeToPropertyFile(String policy) {
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("policyserver.properties");

            // set the properties value
            prop.setProperty("fetch_policy", policy);

            // save properties to project root folder
            prop.store(output, null);
            return true;

        } catch (IOException io) {
            io.printStackTrace();
            return false;
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
