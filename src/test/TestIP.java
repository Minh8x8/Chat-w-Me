package test;

import java.net.InetAddress;

public class TestIP {
    public static void main(String[] args) {
        try {
            // 10.25.82.45
            // Student: 10.25.88.172
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("IP address of server: " + ip.getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String clientMessage = "Server [09/04/2023 22:59:36]: L1VuZG8";
        clientMessage = clientMessage.substring(clientMessage.indexOf(": ") + 2);
        System.out.println(clientMessage.equals("L1VuZG8"));
        System.out.println(clientMessage);
    }
}
