package com.msa;

import com.msa.controller.Controller;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class HAProxyClientApp {

    public static void main(String[] args) throws IOException, InterruptedException {
        Controller c = new Controller();
        while (true) {
            try {
                c.updateHaproxy();
            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(2000);
        }
    }
}
