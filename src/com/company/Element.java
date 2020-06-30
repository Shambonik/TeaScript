package com.company;


public class Element {
    boolean status;
    //Base object;
    Method method;
    Variable value;
    //int[] parameters;
    Element(boolean status, Method method, Variable value){
        this.status = status;
        //this.object = object;
        this.method = method;
        this.value = value;
    }
}
