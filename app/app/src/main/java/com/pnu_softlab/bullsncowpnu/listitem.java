package com.pnu_softlab.bullsncowpnu;

public class listitem {
    String round;
    String answer;
    String result;

    listitem(String round, String answer, String result){
        this.round = round;
        this.answer = answer;
        this.result = result;
    }

    public void setter(String answer, String result){
        this.answer = answer;
        this.result = result;
    }
}
