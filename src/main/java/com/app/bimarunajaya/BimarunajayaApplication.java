package com.app.bimarunajaya;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@Slf4j
@SpringBootApplication
public class BimarunajayaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BimarunajayaApplication.class, args);
        openBrowser();
    }

    private static void openBrowser() {
        String os = System.getProperty("os.name").toLowerCase();
        boolean win = os.indexOf("win") >= 0;
        boolean mac = os.indexOf("mac") >= 0;
        boolean linux = os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0;
        log.info("win={} mac={} linux={}", win, mac, linux);

        Runtime rt = Runtime.getRuntime();
        String url = "http://localhost:8080";
        try {
            if (win) rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            else if (mac) rt.exec("open " + url);
            else if (linux) {
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                        "netscape", "opera", "links", "lynx"};
                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < browsers.length; i++)
                    if (i == 0)
                        cmd.append(String.format("%s \"%s\"", browsers[i], url));
                    else
                        cmd.append(String.format(" || %s \"%s\"", browsers[i], url));
                rt.exec(new String[]{"sh", "-c", cmd.toString()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
