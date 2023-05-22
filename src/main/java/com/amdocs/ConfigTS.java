package com.amdocs;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class ConfigTS {
    private Map<Character, String> specSimb = new HashMap<Character, String>();

    {
        specSimb.put('\"', "%22");
        specSimb.put('<', "%3c");
        specSimb.put('^', "%5e");
        specSimb.put('#', "%23");
        specSimb.put('>', "%3e");
        specSimb.put('{', "%7b");
        specSimb.put('}', "%7d");
        specSimb.put('|', "%7c");
        specSimb.put('\\', "%5c");
        specSimb.put('[', "%5b");
        specSimb.put(']', "%5d");
        specSimb.put('`', "%60");
        specSimb.put('~', "%7e");
        specSimb.put('\'', "%20");
    }

    final String url = "prj.bi-telco.com/pwa/Timesheet.aspx";
    private String userName;
    private String password;
    private boolean isTestedMode;


    public ConfigTS(String jsonConf) {
        ConfigTS configTS = new Gson().fromJson(jsonConf, ConfigTS.class);
        userName = configTS.userName;
        //Password = changeScpec(DecodingPassword(configTS.Password));
        password = changeScpec(configTS.password);
        isTestedMode = configTS.isTestedMode;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrlBaseAuth() {
        return new StringBuilder("https://bell-main%5c" + userName + ":" + password + "@" + url).toString();
    }

    static public String readFileConfig(File file) {
        StringBuilder body = new StringBuilder();
        try (FileReader reader = new FileReader(file)) {
            int c;
            while ((c = reader.read()) != -1) {
                body.append((char) c);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return body.toString();
    }

    private String decodingPassword(String password) {
        var pas = password.toCharArray();
        int sumCodeWord = 0;
        String codeWord = new String();

        try {
            codeWord = InetAddress.getLocalHost().getHostName().toString();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        for (char c : codeWord.toCharArray()) {
            sumCodeWord += (int) c;
        }

        for (int i = 0; i < pas.length; ++i) {
            pas[i] -= 'a';
            pas[i] += (char) sumCodeWord;
        }
        return new String(pas);
    }

    public boolean isTestedMode() {
        return isTestedMode;
    }

    public void setTestedMode(boolean testedMode) {
        isTestedMode = testedMode;
    }

    private String changeScpec(String str) {
        StringBuffer strbl = new StringBuffer();
        if (str == null) return null;

        for (Map.Entry<Character, String> entry : specSimb.entrySet()) {
            //str.replaceAll(entry.getKey().toString(), entry.getValue());
            for (int i = 0; i < str.length(); ++i) {
                if (!entry.getKey().equals(str.charAt(i))) {
                    strbl.append(str.charAt(i));
                } else {
                    strbl.append(entry.getValue());
                }
            }
        }

        return str;
    }
}
