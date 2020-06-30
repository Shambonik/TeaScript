package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Base obj = new Base();
        while(true) {
            String str = in.nextLine();
            str = obj.normalize(str);
            obj.parse(str);
            //obj.printElements();
            obj.perform();
            //obj.printNumbers();
        }
    }
}

