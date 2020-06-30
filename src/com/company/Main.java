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
            obj.parse(str);
            obj.printElements();
        }
    }
}

