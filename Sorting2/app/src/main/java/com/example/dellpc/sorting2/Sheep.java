package com.example.dellpc.sorting2;

/**
 * Created by DELL PC on 10.07.2016.
 */
public class Sheep {
    int number;
    String text;
    Sheep(int number, String t) {
        this.number=number;
        this.text=t;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text= text;
    }

    public int getNumber(){
        return number;
    }
    public void setNumber(int number){
        this.number=number;
    }
}
