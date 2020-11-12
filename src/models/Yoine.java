package models;

public class Yoine {

    public void yoineCount(Report r) {
        int count = r.getYoine();
        count++;
        r.setYoine(count);
    }

}