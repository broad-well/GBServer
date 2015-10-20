package com.broaderator.ServerDetector;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Executable {

    public static void main(String[] args) throws IOException, InterruptedException {
        boolean isOpen = true;
        boolean isPosted = false;
        Socket s = SocketFactory.getDefault().createSocket();
        while (true) {
            try {
                s.setSoTimeout(7000);
                s.connect(new InetSocketAddress("127.0.0.1", 25565));
                s.close();
                isOpen = true;
            } catch (ConnectException ce) {
                isOpen = false;
            }
            if (!isOpen) {
                if (!isPosted) {
                    Process p = Runtime.getRuntime().exec("/home/michael/TTYtter/ttytter -status=UnexpectedDowntime!");
                    System.err.println("I found downtime and posted Twitter for it.");
                    isPosted = true;
                }

            } else {
                isPosted = false;
            }
            Thread.sleep(10 * 60 * 1000);
        }
    }

}
