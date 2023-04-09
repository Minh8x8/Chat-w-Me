package test;

import db.DB;

import java.util.List;

public class TestDB {
    public static void main(String[] args) {
        DB db = new DB();
        db.setDB();
        List<String[]> tableDetail = db.getTableDetail();
        for (String[] rows : tableDetail) {
            for (String col : rows) {
                System.out.print(col + " ");
            }
            System.out.println();
        }
    }
}
