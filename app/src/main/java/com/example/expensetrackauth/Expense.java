package com.example.expensetrackauth;

public class Expense {
    private String date;
    private int depart; //first expense default name
    private int lunch; //second expense default name
    private int returnexp; //third expense default name
    private int other; //fourth expense default name
    private int total;

    private String expname1;
    private String expname2;
    private String expname3;
    private String expname4;


    public Expense(){

    }
    public Expense(String dt, int dep, int lun, int ret, int oth, String ename1, String ename2, String ename3, String ename4){
        this.date = dt;
        this.depart = dep;
        this.lunch = lun;
        this.returnexp = ret;
        this.other = oth;

        this.expname1 = ename1;
        this.expname2 = ename2;
        this.expname3 = ename3;
        this.expname4 = ename4;

        this.total = dep+lun+ret+oth;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDepart() {
        return depart;
    }

    public void setDepart(int depart) {
        this.depart = depart;
    }

    public int getLunch() {
        return lunch;
    }

    public void setLunch(int lunch) {
        this.lunch = lunch;
    }

    public int getReturnexp() {
        return returnexp;
    }

    public void setReturnexp(int returnexp) {
        this.returnexp = returnexp;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }
    public int getTotal(){
        return total;
    }
    public void setTotal(int d, int l, int r, int o){
        this.total = d+l+r+o;
    }



    public String getExpname1() {
        return expname1;
    }

    public void setExpname1(String expname1) {
        this.expname1 = expname1;
    }

    public String getExpname2() {
        return expname2;
    }

    public void setExpname2(String expname2) {
        this.expname2 = expname2;
    }

    public String getExpname3() {
        return expname3;
    }

    public void setExpname3(String expname3) {
        this.expname3 = expname3;
    }

    public String getExpname4() {
        return expname4;
    }

    public void setExpname4(String expname4) {
        this.expname4 = expname4;
    }


}
