package com.jvs.pi4j.test;

import java.util.BitSet;

public class BitSetTest {

    public static void main(String[] args){
        byte value = 12;
        BitSet bitset = fromByte(value);
        System.out.println("bitset.toString(): " + bitset.toString());

        for (int i = 0; i < 8; i++) {
            System.out.println(i + " - " + bitset.get(i));
        }
    }

    public static BitSet fromByte(byte b) {
        BitSet bits = new BitSet(8);

        for (int i = 0; i < 8; i++) {
            bits.set(i, (b & 1) == 1);
            b >>= 1;
        }
        return bits;
    }
}
