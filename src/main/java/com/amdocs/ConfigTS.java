package com.amdocs;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

public class ConfigTS {
    private static final Map<Character, String> SPEC_SIMB = Map.ofEntries(
            Map.entry('\"', "%22"),
            Map.entry('<', "%3c"),
            Map.entry('^', "%5e"),
            Map.entry('#', "%23"),
            Map.entry('>', "%3e"),
            Map.entry('{', "%7b"),
            Map.entry('}', "%7d"),
            Map.entry('|', "%7c"),
            Map.entry('\\', "%5c"),
            Map.entry('[', "%5b"),
            Map.entry(']', "%5d"),
            Map.entry('`', "%60"),
            Map.entry('~', "%7e"),
            Map.entry('\'', "%20")
    );
    private static final String URL = "prj.bi-telco.com/pwa/Timesheet.aspx";

    private String userName;
    private String password;
    private boolean isTestedMode;


    public ConfigTS(String jsonConf) {
        ConfigTS configTS = new Gson().fromJson(jsonConf, ConfigTS.class);
        userName = configTS.userName;
        //Password = changeScpec(DecodingPassword(configTS.Password));
        password = configTS.password;
        isTestedMode = configTS.isTestedMode;
    }

    public String getUsername() {
        return "bell-main\\" + userName;
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

    public String getUrlBaseAuth(boolean authenticateByUrl) {
        return authenticateByUrl
                ? new StringBuilder("https://" + changeScpec(getUsername()) + ":" + changeScpec(getPassword()) + "@" + URL).toString()
                : new StringBuilder("https://" + URL).toString();
    }

    public static String readFileConfig(File file) {
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

    private static String decodingPassword(String password) {
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

    static String changeScpec(String str) {
        StringBuilder strbl = new StringBuilder();
        if (str == null) return null;

        for (Map.Entry<Character, String> entry : SPEC_SIMB.entrySet()) {
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
