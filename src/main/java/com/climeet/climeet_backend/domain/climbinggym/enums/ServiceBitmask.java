package com.climeet.climeet_backend.domain.climbinggym.enums;

public enum ServiceBitmask {
    샤워_시설(1),
    샤워_용품(2),
    수건_제공(4),
    간이_세면대(8),
    초크_대여(16),
    암벽화_대여(32),
    삼각대_대여(64),
    운동복_대여(128);

    private int value;

    ServiceBitmask(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}
