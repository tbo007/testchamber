package io.github.tbo007.testchamber.encoding;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.detect.CompositeEncodingDetector;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.detect.EncodingDetector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.txt.Icu4jEncodingDetector;
import org.apache.tika.parser.txt.UniversalEncodingDetector;
import org.apache.tika.sax.BodyContentHandler;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class EncodingTest {

    private static final Logger log = LogManager.getLogger(EncodingTest.class);

    private static final String CS_ISO_15 = "ISO-8859-15";
    private static final String CS_UTF_8 = "UTF-8";



    @Test
    void defaultEncoding_BOM () throws IOException {
        Path path = Path.of("./target/", "iso885915_withUTF8BOM.txt");
        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)) {

            int [] unicode_bom = {0xFEFF};
            String string = new String(unicode_bom,0,unicode_bom.length);
            System.out.println(toHexString(string.getBytes(CS_UTF_8)));

            // UTF8-BOM
            out.write(string.getBytes(CS_UTF_8));

            String umlautz = "lösche Agent\n";

            // 1. mit BOM
            // 2. ohne BOM
            // 3. ohne BOM nur ISO oder nur utf
            out.write(umlautz.getBytes(CS_UTF_8));
            //out.write(umlautz.getBytes(CS_ISO_15));
        }
    }

    /** log4j2.xml: charset = iso8859-15 **/
    @Test
    void platformEncoding() throws UnsupportedEncodingException {
        String umlautz = "äöü";
        byte[] ISO_8859_15 = umlautz.getBytes(CS_ISO_15);
        String isoString = new String(ISO_8859_15, CS_ISO_15);
        String utf8String = new String(isoString.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        System.out.println(isoString);
        for (int i = 0; i < 10; i++) {
            log.error("ISO STR: " +isoString);
            log.error("UTF STR: " +utf8String);
        }
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

    @Test
    void buildInDetectorTest () throws Exception {
        CharsetDecoder utf8Decoder = StandardCharsets.UTF_8.newDecoder();
        String string ="öööööööääääää";
        assertThrows(MalformedInputException.class, ()->
        utf8Decoder.decode(ByteBuffer.wrap(string.getBytes(CS_ISO_15))));
        assertDoesNotThrow( ()->
        utf8Decoder.decode(ByteBuffer.wrap(string.getBytes(CS_UTF_8))));
    }

    @Test
    void tikaTest2 () throws Exception {
        String join = String.join("", Collections.nCopies(2000, "ööööaaaa"));
        ByteArrayInputStream str = new ByteArrayInputStream(join.getBytes(CS_ISO_15));

 //       try (InputStream input = str) {
        // log42.log
        try (FileInputStream input = new FileInputStream("./target/encoding-20250412.log")) {
            byte [] bytes = new byte[4000];
            input.read(bytes);


            System.out.println(tikaDetect(bytes));
            String string = new String(bytes,CS_ISO_15);
            // Prepend € because it is different in all Three Encodings
            //string = "€" + string;
            System.out.println(tikaDetect(string.getBytes(CS_UTF_8)));
            String winString = string.replaceAll("\\n", "\\r\\n");
            System.out.println(winString.equals(string));
            System.out.println(tikaDetect(winString.getBytes("windows-1252")));
            System.out.println(tikaDetect(string.getBytes(CS_ISO_15)));

            /* Plan:
             * Eigener Encoding detector
             * Wenn UTF-X detektiert wird dieses nutzen. Wenn ISO-8859-X oder Windows-12XX detektiert
             * wird, Strategie aus UniversalEncodingListener nutzen: Wenn \r vorhanden: CP1252
             * ansonsten 8859-15
              */
        }
    }

    private static Charset tikaDetect(byte[] bytes) throws IOException {
        Metadata metadata = new Metadata();
        metadata.set(Metadata.CONTENT_ENCODING,CS_ISO_15);
        EncodingDetector detector = new Icu4jEncodingDetector();
        //EncodingDetector detector = new UniversalEncodingDetector();
        return detector.detect(new ByteArrayInputStream(bytes), metadata);
    }


    public static String detectDocTypeUsingDetector(InputStream stream)
            throws IOException {
        Detector detector = new DefaultDetector();
        Metadata metadata = new Metadata();

        MediaType mediaType = detector.detect(stream, metadata);
        return mediaType.toString();
    }







}
