package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Base {
    ArrayList<Base> children;
    HashMap<String, FloatVar> numbers;
    HashMap<String, StringVar> strings;
    HashMap<String, Method> methods;
    ArrayList<Element> elements;
    String objectName;
    Base parent;

    Base() {
        parent = null;
        children = new ArrayList<>();
        numbers = new HashMap<>();
        strings = new HashMap<>();
        methods = new HashMap<>();
        elements = new ArrayList<>();
        methods.put("print", (Method)this::print);
        methods.put("printParent", (Method)this::printParent);
        objectName = "root";
    }

    boolean compareStrings(String s1, String s2){
        if(s1.length()!=s2.length())return false;
        for(int i = 0; i<s1.length(); i++){
            if(s1.charAt(i)!=s2.charAt(i))return false;
        }
        return true;
    }

    Base searchObjectByName(String str){
        if(compareStrings(str, objectName)) return this;
        System.out.println(objectName);
        Base result;
        for(int i = 0; i<children.size(); i++) {
            result = children.get(i).searchObjectByName(str);
            if(result!=null) return result;
        }
        return null;
    }

    void parse(String str){
        Base object = this;
        char ch = ';';
        String name = "";
        int index = 0;
        int len = str.length();
        while((index<len)&&(!Character.isLetter(str.charAt(index)))) index++;
        while(index<len) {
            while (index < len){
                ch = str.charAt(index);
                if((ch!=' ')&&(ch!=';')&&(ch!='.')&&(ch!='(')&&(ch!=')')&&(ch!=',')){
                    name += str.charAt(index);
                    index++;
                }
                else break;
            }
            if(ch==' '){
                if (object.numbers.get(name)!=null) elements.add(new Element(true,null, object.numbers.get(name)));
                else if (object.strings.get(name)!=null) elements.add(new Element(true,null, object.strings.get(name)));
                else if (object.methods.get(name)!=null) elements.add(new Element(false, object.methods.get(name), null));
                object = this;
            }
            else if(ch=='.'){
                object = searchObjectByName(name);
            }
            else if(ch=='('){
                elements.add(new Element(false, object.methods.get(name), null));
                object = this;
            }
            else if((name!="")&&((ch==';')||(ch==')')||(ch == ','))){
                if (object.numbers.get(name)!=null) elements.add(new Element(true,null, object.numbers.get(name)));
                else if (object.strings.get(name)!=null) elements.add(new Element(true,null, object.strings.get(name)));
                else elements.add(new Element(true,null, new StringVar(name)));
                object = this;
            }
            name = "";
            index++;
            while((index<len)&&(str.charAt(index)==' ')) index++;
        }
    }

    void printElements(){
        Element element;
        for(int i = 0; i<elements.size(); i++){
            element = elements.get(i);
            if(element.value!= null) {
                try {
                    System.out.println(element.status + " " + element.method + " " + ((StringVar) element.value).value);
                } catch (Exception e) {
                    System.out.println(element.status + " " + element.method + " " + ((FloatVar) element.value).value);
                }
            }
            else System.out.println(element.status + " " + element.method);
        }
    }

    void print() {
        System.out.println();
    }

    void printParent() {
        System.out.println(parent);
    }

}


@FunctionalInterface
interface Method{
    void call();
}
