package test;

import db.GetChatInformation;

import java.util.List;

public class TestGetChatInformation {
    public static void main(String[] args) {
        GetChatInformation gc = new GetChatInformation();
        List<String[]> listChat = gc.getChatInformation();
        for(String[] chat : listChat) {
            for(String c : chat) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }
}
