package test;

import db.GetMessage;

import java.util.List;

public class TestGetMessage {
    public static void main(String[] args) {
        GetMessage gm = new GetMessage();
        List<String> messages = gm.getMessage(1);
        for (String message : messages) {
            System.out.println(message);
        }
    }
}
