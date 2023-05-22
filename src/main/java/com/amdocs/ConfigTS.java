package com.amdocs;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ConfigTS {
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
        return str == null ? null
                : URLEncoder.encode(str, UTF_8).replace("+", "%20");
    }
}
