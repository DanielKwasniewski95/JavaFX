package com.daniel.app.service;

import com.daniel.app.Main;

import java.io.*;

public class ConfigFile {

    private String path;

    public ConfigFile(){
        Main main = new Main();
        this.path = main.getPath();
        this.path = this.path.replaceAll("ElectricityCost.jar", "config.txt");
    }

    public void saveConfigFile(String path) {

        PrintWriter pw;
        BufferedWriter bw;
        FileWriter fw;

   File configFile = new File(this.path);
   try {
       fw = new FileWriter(configFile.getAbsoluteFile(), true);
       bw = new BufferedWriter(fw);
       pw = new PrintWriter(configFile.getAbsoluteFile());

       pw.flush();
       bw.write(path);

       configFile.createNewFile();
       bw.close();
       fw.close();
       pw.close();

   }catch(Exception e){

    }

    }

    public String readConfigFile(){
        String line;
        String path = null;

        FileReader fileReader;
        BufferedReader bufferedReader;

        try {
            fileReader = new FileReader(this.path);
            bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
               path = line;
            }
            bufferedReader.close();
        }
        catch(Exception e) {
        }

        return path;
    }

}
