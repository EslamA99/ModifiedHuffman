package com.modifiedhuffman;

import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static Scanner sc1 = new Scanner(System.in);
    static Map<String, String> table = new HashMap<>();
    static Map<Character, String> originalCodeTable = new HashMap<>();
    static Map<String,Character> originalCodeTableDeCompress = new HashMap<>();
    static int choice=0;
    public static void main(String[] args) {
        while (true) {
            table.clear();
            System.out.print("1- compress 2- deCompress\nchoice: ");
             choice = sc1.nextInt();
            if (choice == 1) {
                setOrignalTable();
                String seq = sc.nextLine();
                ArrayList<Character> arrSeq = new ArrayList<>();
                for (int i = 0; i < seq.length(); i++)
                    arrSeq.add(seq.charAt(i));
                ArrayList<Node> arr = new ArrayList<>();
                arr=setNodesInArr(arr,arrSeq,seq);

                arr = others(arr);/**set others**/
                arr = buildTree(arr);
                arr.get(0).code = "";
                setTable(arr.get(0));
                System.out.println(table);
                String compressedSeq = "";
                for (int i = 0; i < seq.length(); i++) {
                    if (table.containsKey(String.valueOf(seq.charAt(i)))) {
                        compressedSeq += table.get(String.valueOf(seq.charAt(i))) + " ";
                    } else {
                        compressedSeq += table.get("others") + originalCodeTable.get(seq.charAt(i)) + " ";
                    }
                }
                System.out.println(compressedSeq);
            }
            else if(choice==2){
                setOrignalTable();
                String seq = sc.nextLine();
                ArrayList<Node> arr = new ArrayList<>();
                table.clear();
                while(true){
                    String symbol=sc.next();
                    if (symbol.equals("^z"))
                        break;
                    //double prob=sc.nextDouble();
                    int freq=sc.nextInt();
                    arr.add(new Node(symbol,"",0.0,freq,null,null));
                }
                Collections.sort(arr,Collections.reverseOrder());
                arr=others(arr);
                arr = buildTree(arr);
                arr.get(0).code = "";
                setTable(arr.get(0));
                System.out.println(table);
               String deCompressedSeq="";
               String currCode="";
               for (int i=0;i<seq.length();i++){
                   currCode+=seq.charAt(i);
                   if(table.containsKey(currCode)){
                       if(table.get(currCode).equals("others")){
                           i++;
                          // deCompressedSeq+=originalCodeTableDeCompress.get(seq.substring(i,i+7));
                          // i+=6;
                           deCompressedSeq+=originalCodeTableDeCompress.get(seq.substring(i,i+3));
                           i+=2;
                       }
                       else{
                           deCompressedSeq+=table.get(currCode);
                       }
                       currCode="";
                   }
               }
                System.out.println(deCompressedSeq);
            }
        }

    }

    private static ArrayList<Node> setNodesInArr(ArrayList<Node> arr,ArrayList<Character>arrSeq,String seq) {
        for (int i = 0; i < arrSeq.size(); i++) {
            Node nw = new Node(String.valueOf(arrSeq.get(i)), "", 0, 0, null, null);
            int cont = 1;
            for (int j = i + 1; j < arrSeq.size(); j++) {
                if (arrSeq.get(i) == arrSeq.get(j)) {
                    arrSeq.remove(j--);
                    cont++;
                }
            }
            arrSeq.remove(i--);
            nw.prob = (double) cont / seq.length();
            nw.freq = cont;
            arr.add(nw);
        }
        Collections.sort(arr, Collections.reverseOrder());
        return arr;
    }

    public static void setTable(Node mover) {
        if (mover == null)
            return;
        if (mover.parent != null) {
            if (mover == mover.parent.up)
                mover.code = mover.parent.code + "0";
            else
                mover.code = mover.parent.code + "1";

        }
        if (mover.up == null && mover.down == null){
            if(choice==1)
                table.putIfAbsent(mover.symbols, mover.code);
            else table.putIfAbsent(mover.code,mover.symbols);
        }

        setTable(mover.up);
        setTable(mover.down);
    }

    private static void setOrignalTable() {
        for (int i = 0; i < 128; i++) {
            String code = Integer.toBinaryString(i);
            while (code.length() < 7)
                code = '0' + code;
            if(choice==1)
                originalCodeTable.put((char) i, code);
            else originalCodeTableDeCompress.put(code,(char) i);
        }
        /*originalCodeTableDeCompress.put("000",'D');
        originalCodeTableDeCompress.put("111",'F');*/
    }

    private static ArrayList<Node> others(ArrayList<Node> arr) {
        Node others=null;
        for (int i = 0; i < arr.size(); i++) {
            if(arr.get(i).freq==1){
                if(others==null)
                    others=new Node();
                others.symbols = "others";
                others.prob += arr.get(i).prob;
                others.freq += arr.get(i).freq;
                arr.remove(i--);
            }
            /*double tempProp = arr.get(i).prob;
            for (int j = i + 1; j < arr.size(); j++) {
                if (tempProp == arr.get(j).getProb()) {
                    //arr.get(i).symbols+=arr.get(j).symbols;
                    arr.get(i).symbols = "others";
                    arr.get(i).prob += arr.get(j).prob;
                    arr.get(i).freq += arr.get(j).freq;
                    arr.remove(j--);
                } else break;
            }*/
        }
        if(others!=null)
            arr.add(others);
        else if(arr.size()==1)
            arr.add(new Node());

        Collections.sort(arr, Collections.reverseOrder());
        return arr;
    }

    private static ArrayList<Node> buildTree(ArrayList<Node> arr) {

        while (arr.size() > 1) {
            print(arr);
            Node parentNode = new Node();
            parentNode.symbols = arr.get(arr.size() - 2).symbols + arr.get(arr.size() - 1).symbols;
            parentNode.prob = arr.get(arr.size() - 2).prob + arr.get(arr.size() - 1).prob;
            parentNode.freq = arr.get(arr.size() - 2).freq + arr.get(arr.size() - 1).freq;
            parentNode.down = arr.get(arr.size() - 1);
            parentNode.up = arr.get(arr.size() - 2);
            parentNode.down.parent = parentNode;
            parentNode.up.parent = parentNode;
            arr.remove(arr.size() - 1);
            arr.remove(arr.size() - 1);
            arr.add(parentNode);
            Collections.sort(arr, Collections.reverseOrder());
        }
        return arr;
    }

    private static void print(ArrayList<Node> arr) {
        for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i).getSymbols() + " " + arr.get(i).getFreq());
        }
    }
}
