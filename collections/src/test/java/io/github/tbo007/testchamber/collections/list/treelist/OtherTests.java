package io.github.tbo007.testchamber.collections.list.treelist;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class OtherTests {

    @Test
    void shifttest() {
        System.out.println(paddedBinary(1,10) );
        System.out.println(paddedBinary(42,10) );
        System.out.println( paddedBinary(42 >>1, 10));
        System.out.println( paddedBinary(42 <<1,10));
    }

    @Test
    void listRemoveAll() {
        List<String> a = new ArrayList<>(List.of("A", "A", "A"));
        List<String> b = List.of("A");
        a.removeAll(b);
        System.out.println(a);

    }


    private String paddedBinary(int decimal, int length) {
        String binary = Integer.toBinaryString(decimal);
       return  String.format("%" + length + "s", binary).replace(' ', '0');
    }

}
