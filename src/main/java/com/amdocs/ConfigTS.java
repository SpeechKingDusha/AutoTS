package com.amdocs;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class ConfigTS {
    private Map<Character,String> SpecSimb = new HashMap<Character,String>();
    {
        SpecSimb.put('\"',"%22");
        SpecSimb.put('<',"%3c");
        SpecSimb.put('^',"%5e");
        SpecSimb.put('#',"%23");
        SpecSimb.put('>',"%3e");
        SpecSimb.put('{',"%7b");
        SpecSimb.put('}',"%7d");
        SpecSimb.put('|',"%7c");
        SpecSimb.put('\\',"%5c");
        SpecSimb.put('[',"%5b");
        SpecSimb.put(']',"%5d");
        SpecSimb.put('`',"%60");
        SpecSimb.put('~',"%7e");
        SpecSimb.put('\'',"%20");
    }

    final String URL = "prj.bi-telco.com/pwa/Timesheet.aspx";
    private String UserName;
    private  String Password;
    private boolean isTestedMode;


    public ConfigTS(String jsonConf) {
        ConfigTS configTS = new Gson().fromJson(jsonConf, ConfigTS.class);
        UserName = configTS.UserName;
        //Password = changeScpec(DecodingPassword(configTS.Password));
        Password = changeScpec(configTS.Password);
        isTestedMode = configTS.isTestedMode;
    }
    public String getUsername() {
        return UserName;
    }
    public void setUsername(String username) {
        UserName = username;
    }

    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }

    public String getUrlBaseAuth() {
        return new StringBuilder ("https://bell-main%5c"+ UserName + ":" + Password + "@" + URL).toString();
    }

    static public String readFileConfig (File file) {

        StringBuilder body = new StringBuilder();
        try(FileReader reader = new FileReader(file))
        {
            int c;
            while((c=reader.read())!=-1){
                body.append((char)c);
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return body.toString();
    }
    private String DecodingPassword(String password) {
        var pas = password.toCharArray();
        int sumCodeWord = 0;
        String codeWord = new String();

        try {
            codeWord = InetAddress.getLocalHost().getHostName().toString();
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }

        for (char c: codeWord.toCharArray())
        {
            sumCodeWord += (int)c;
        }

        for (int i =0; i < pas.length; ++i)
        {
            pas[i] -='a';
            pas[i] += (char) sumCodeWord;
        }
        return  new String(pas);
    }

    public boolean isTestedMode() {
        return isTestedMode;
    }

    public void setTestedMode(boolean testedMode) {
        isTestedMode = testedMode;
    }

    private String changeScpec (String str){
        StringBuffer strbl = new StringBuffer();
        if (str==null) return null;

        for(Map.Entry<Character, String> entry : SpecSimb.entrySet()) {
            //str.replaceAll(entry.getKey().toString(), entry.getValue());
            for (int i = 0; i<str.length(); ++i){
                if (!entry.getKey().equals(str.charAt(i))) {
                    strbl.append(str.charAt(i));
                }
                else {
                    strbl.append(entry.getValue());
                }

            }

        }

        return str;
    }
}
