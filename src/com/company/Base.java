package com.company;

import java.util.*;

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
        methods.put("print", this::print);
        methods.put("printNumbers", this::printNumbers);
        methods.put("printStrings", this::printStrings);
        methods.put("number", this::numberCreate);
        methods.put("string", this::stringCreate);
        methods.put("=", this::assignValue);
        methods.put("+", this::print);
        methods.put("-", this::print);
        methods.put("*", this::print);
        methods.put("/", this::print);
        methods.put("%", this::print);
        methods.put("==", this::print);
        methods.put(">=", this::print);
        methods.put("<=", this::print);
        methods.put("<", this::print);
        methods.put(">", this::print);
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

    String normalize(String str){
        char ch;
        char ch0;
        for(int i = 1; i<str.length(); i++){
            ch = str.charAt(i);
            if((ch == '-')||(ch == '+')||(ch=='/')||(ch=='*')||(ch=='%')) {
                str = str.substring(0,i)+" "+ ch + " " + str.substring(i+1);
                i+=2;
            }
            ch0 = str.charAt(i-1);
            if((ch == '=')) {
                if ((ch0 != '=') && (ch0 != '<') && (ch0 != '>')) {
                    if ((i < str.length() - 1) && (str.charAt(i + 1) != '=')) {
                        str = str.substring(0, i) + " " + ch + " " + str.substring(i+1);
                        i += 2;
                    } else {
                        str = str.substring(0, i) + " " + str.substring(i);
                        i++;
                    }
                } else str = str.substring(0, i+1) + " " + str.substring(i + 1);
            }
            if((ch == '<')||(ch == '>')){
                if ((i < str.length() - 1) && (str.charAt(i + 1) != '=')) {
                    str = str.substring(0, i) + " " + ch + " " + str.substring(i+1);
                    i += 2;
                } else {
                    str = str.substring(0, i) + " " + str.substring(i);
                    i++;
                }
            }
        }
        return str;
    }

    private int getStringArgument(String str, int index) {
        String result = "";
        index++;
        while(str.charAt(index)!='"') result+=str.charAt(index++);
        elements.add(new Element(true, null, new StringVar(result)));
        return index;
    }

    private int getFloatArgument(String str, int index) {
        float result = 0;
        boolean point = false;
        float fractionalPart = 0.1f;
        char ch = str.charAt(index);
        while(ch!=' ' && ch!=',' && ch!=')' && ch !=';'){
            if(ch!='.') {
                if(!point) result = result * 10 + (str.charAt(index) - '0');
                else {
                    result += (str.charAt(index) - '0') * fractionalPart;
                    fractionalPart /= 10;
                }
            }
            else point = true;
            index++;
            ch = str.charAt(index);
        }
        elements.add(new Element(true, null, new FloatVar(result)));
        return index;
    }

    void parse(String str){
        elements = new ArrayList<>();
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
                    if(ch=='"') index = getStringArgument(str, index);
                    else if(name == "" && Character.isDigit(ch)) index = getFloatArgument(str, index);
                    else {
                        name += str.charAt(index);
                    }
                    index++;
                }
                else break;
            }
            if(name!="") {
                if (ch == ' ') {
                    if (object.numbers.get(name) != null)
                        elements.add(new Element(true, null, object.numbers.get(name)));
                    else if (object.strings.get(name) != null)
                        elements.add(new Element(true, null, object.strings.get(name)));
                    else if (object.methods.get(name) != null)
                        elements.add(new Element(false, object.methods.get(name), null));
                    else elements.add(new Element(true, null, new StringVar(name)));
                    object = this;
                } else if (ch == '.') {
                    object = searchObjectByName(name);
                } else if (ch == '(') {
                    elements.add(new Element(false, object.methods.get(name), null));
                    object = this;
                } else if ((ch == ';') || (ch == ')') || (ch == ',')){
                    if (object.numbers.get(name) != null)
                        elements.add(new Element(true, null, object.numbers.get(name)));
                    else if (object.strings.get(name) != null)
                        elements.add(new Element(true, null, object.strings.get(name)));
                    else elements.add(new Element(true, null, new StringVar(name)));
                    object = this;
                }
                name = "";
            }
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

    void perform(){
        for(int i=0; i<elements.size(); i++){
            if(!elements.get(i).status){
                i = elements.get(i).method.call(i);
            }
        }
    }

    int numberCreate(int i){
        numbers.put(((StringVar)elements.get(i+1).value).value, new FloatVar(0));
        elements.get(i).status=true;
        elements.get(i).value = numbers.get(((StringVar)elements.get(i+1).value).value);
        elements.remove(i+1);
        return i;
    }

    int stringCreate(int i){
        strings.put(((StringVar)elements.get(i+1).value).value, new StringVar(""));
        elements.get(i).status=true;
        elements.get(i).value = strings.get(((StringVar)elements.get(i+1).value).value);
        elements.remove(i+1);
        return i;
    }

    int assignValue(int i){
        Element element = elements.get(i);
        elements.remove(i);
        perform();
        try {
            ((FloatVar)elements.get(i-1).value).value = ((FloatVar)elements.get(i).value).value;
        } catch (Exception e) {
            ((StringVar)elements.get(i-1).value).value = ((StringVar)elements.get(i).value).value;
        }
        return i;
    }

    int print(int i) {
        System.out.println();
        return i;
    }

    int printNumbers(int i){
        System.out.println("Числовые переменные: ");
        Iterator<HashMap.Entry<String, FloatVar>> iterator = numbers.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, FloatVar> pair = iterator.next();
            String key = pair.getKey();
            float value = pair.getValue().value;
            System.out.println(key + ":" + value);
        }
        return i;
    }

    int printStrings(int i){
        System.out.println("Строковые переменные: ");
        Iterator<HashMap.Entry<String, StringVar>> iterator = strings.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, StringVar> pair = iterator.next();
            String key = pair.getKey();
            String value = pair.getValue().value;
            System.out.println(key + ":" + value);
        }
        return i;
    }

    /*void printParent() {
        System.out.println(parent);
    }*/

}


@FunctionalInterface
interface Method{
    int call(int i);
}
