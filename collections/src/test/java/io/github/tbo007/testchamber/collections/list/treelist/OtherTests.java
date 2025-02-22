package io.github.tbo007.testchamber.collections.list.treelist;

import org.junit.jupiter.api.Test;

public class OtherTests {

    @Test
    void shifttest() {
        System.out.println(paddedBinary(1,10) );
        System.out.println(paddedBinary(42,10) );
        System.out.println( paddedBinary(42 >>1, 10));
        System.out.println( paddedBinary(42 <<1,10));

    }

    private String paddedBinary(int decimal, int length) {
        String binary = Integer.toBinaryString(decimal);
       return  String.format("%" + length + "s", binary).replace(' ', '0');
    }

}
