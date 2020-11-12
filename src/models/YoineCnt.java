package models;

public class YoineCnt {

    public void yoineCount(Report r) {
        int count = r.getYoine();
        count++;
        r.setYoine(count);
    }

}