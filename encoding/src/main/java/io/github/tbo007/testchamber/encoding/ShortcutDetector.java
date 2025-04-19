package io.github.tbo007.testchamber.encoding;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/* Legal UTF-8 Byte Sequences
 *
 * #    Code Points      Bits   Bit/Byte pattern
 * 1                     7      0xxxxxxx
 *      U+0000..U+007F          00..7F
 *
 * 2                     11     110xxxxx    10xxxxxx
 *      U+0080..U+07FF          C2..DF      80..BF
 *
 * 3                     16     1110xxxx    10xxxxxx    10xxxxxx
 *      U+0800..U+0FFF          E0          A0..BF      80..BF
 *      U+1000..U+FFFF          E1..EF      80..BF      80..BF
 *
 * 4                     21     11110xxx    10xxxxxx    10xxxxxx    10xxxxxx
 *     U+10000..U+3FFFF         F0          90..BF      80..BF      80..BF
 *     U+40000..U+FFFFF         F1..F3      80..BF      80..BF      80..BF
 *    U+100000..U10FFFF         F4          80..8F      80..BF      80..BF
 *
 */


public class ShortcutDetector {

    public static final Charset CP1252 = Charset.forName("windows-1252");
    public static final Charset ISO_8859_15 = Charset.forName("ISO-8859-15");
    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    private final byte [] encodedBytes;

    // CR LF ist ASCII, also in allen drei. Merken ob detected, für unterscheidung iso / win
    // Wenn bytes 80–9F  und kein UTF-8 n byte Zeichen detektiert wurde, starkes Indiz, dass CP1252,
    // da ISO... in diesem Bereich nur Steuerzeichen enthält

    // Wenn A4 dabei ist, und kein UTF-8 n bytes, dann shortcut ISO, denn in CP1252 ist A4 Placeholder für Währung
    // und das nutzt keiner...


    public ShortcutDetector(byte[] encodedBytes) {
        this.encodedBytes = encodedBytes;
    }

    public Charset detect() {

        for (int i = 0; i <encodedBytes.length ; i++) {

            int unsigedByte = encodedBytes[i] & 0xFF;

            if (unsigedByte < 0x7F)  {
                //Ist sieben Bit ASCII. In allen drei Encodings gleich
                continue;
            }
            int unsignedNextByte = encodedBytes[i+1] & 0xFF;

            // Wette auf nicht UTF-8: Erstes UTF-8 zwei Byte beginnt bei C2. In ISO-8859-15 und CP1252 häufig
            // genutzte Zeichen die nicht ASCII sind ab C4 (Ä). Daher wenn currentbyte >= C2 und nextByte < 80, dann
            // ist es kein UTF8- zwei byte char und kann nur ISO-8859-15 oder CP1252 sein
            if (unsigedByte >= 0xC2 && unsignedNextByte < 0x80) {
                continue;
            }

            // Wenn bytes 80–9F, dann shortcut zu CP1252. Denn IS0 enthält in diesen Bereich nicht tipbare Steuerzeichen
            if (unsigedByte >= 0x80 && unsigedByte <= 0x9f) {
                return CP1252;
            }

            if (unsigedByte >= 0xC2 && unsigedByte <= 0xDF) {
                if (unsignedNextByte >= 80 && unsignedNextByte <= 0xBF) {
                    // Geringe Wahrscheinlichkeit  für zwei Byte ISO chars in Folge  C2..DF  /  80..BF sehr gering...
                    return UTF_8;
                }
            }
            // Wenn A4 (ISO 885916 € --> short cut denn in CP1252 Currency Placeholder und in UTF8 single byte undefiniert
            // und currenzy Placeholder ist C2 A4 und Euro drei byte E2 82 AC
            if (unsigedByte == 0xA4) {
                return ISO_8859_15;
            }
        }
        return ISO_8859_15;
    }
}
