package com.modifiedhuffman;

public class Node implements Comparable<Node>{
     String symbols,code;
     double prob;
     int freq;
     Node down,up,parent;
    public Node(){
        symbols="";
        code="";
        freq=0;
        prob=0.0;
        down=null;
        up=null;
        parent=null;
    }
    public Node(String symbols, String code, double prob,int freq,Node down,Node up) {
        this.symbols = symbols;
        this.code = code;
        this.prob = prob;
        this.freq=freq;
        this.down=down;
        this.up=up;
    }

    public String getSymbols() {
        return symbols;
    }

    public String getCode() {
        return code;
    }

    public int getFreq() {
        return freq;
    }

    public double getProb() {
        return prob;
    }

    /*@Override
    public int compareTo(Object o) {
        int value = ((Node)o).freq;
        return this.freq-value;
    }*/
    @Override
    public int compareTo(Node o) {
        //double otherAmount = o.prob;
        int otherAmount=o.freq;
        /*if (prob == otherAmount)
            return 0;*/
         if (freq > otherAmount)
            return 1;
        else
            return -1;
    }
}
