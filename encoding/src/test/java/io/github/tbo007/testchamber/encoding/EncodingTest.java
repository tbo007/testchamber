package io.github.tbo007.testchamber.encoding;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class EncodingTest {

    private static final Logger log = LogManager.getLogger(EncodingTest.class);



    @Test
    void defaultEncoding_BOM () throws IOException {
        Path path = Path.of("./", "iso885915_withUTF8BOM.txt");
        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)) {

            int [] unicode_bom = {0xFEFF};
            String string = new String(unicode_bom,0,unicode_bom.length);
            System.out.println(toHexString(string.getBytes("UTF-8")));

            // UTF8-BOM
            out.write(string.getBytes("UTF-8"));

            String umlautz = "lösche Agent\n";

            // 1. mit BOM
            // 2. ohne BOM
            // 3. ohne BOM nur ISO oder nur utf
            out.write(umlautz.getBytes("UTF-8"));
            //out.write(umlautz.getBytes("ISO-8859-15"));
        }
    }

    @Test
    void platformEncoding() throws UnsupportedEncodingException {
        String umlautz = "äöü";
        byte[] ISO_8859_15 = umlautz.getBytes("ISO-8859-15");
        String isoString = new String(ISO_8859_15, "ISO-8859-15");
        String utf8String = new String(isoString.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        System.out.println(isoString);
        log.error("ISO STR: " +isoString);
        log.error("UTF STR: " +utf8String);

        System.out.println(System.getProperty("file.encoding"));
        System.out.println(Charset.availableCharsets());
        System.out.println(Charset.defaultCharset());
    }

    @Test
    void unicodeTest () {
        int[] codePoints = {0x4F60, 0x88AB, 0x9ED1, 0x4E86}; // Unicode-Codepoints für 你被黑了 (You been hacked)
        String hackedMessage = new String(codePoints, 0, codePoints.length);
        log.error(hackedMessage);
        System.out.println("Die Nachricht lautet: " + hackedMessage);
    }


        public static String toHexString(byte[] byteArray) {
            if (byteArray == null || byteArray.length == 0) {
                return ""; // Leerer String für leeres oder null-Array
            }

            StringBuilder hexString = new StringBuilder();
            for (byte b : byteArray) {
                // Jedes Byte in einen zweistelligen Hex-Wert umwandeln
                hexString.append(String.format("%02X", b));
            }
            return hexString.toString();
        }






}
