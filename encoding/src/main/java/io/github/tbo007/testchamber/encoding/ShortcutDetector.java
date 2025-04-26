package io.github.tbo007.testchamber.encoding;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Encoding Detector der auf Basis der im Code beschriebenen Heuristik entscheidet, welches
 * der folgenden Encodings geeignet ist um das übergebene byte Array als String zu interpretieren
 * ISO_8859_15 , CP1252 und UTF_8
 *
 * @author Daniel Stein
 * @version 1.1
 * https://github.com/tbo007
 */
public class ShortcutDetector {

    public static final Charset CP1252 = Charset.forName("windows-1252");
    public static final Charset ISO_8859_15 = Charset.forName("ISO-8859-15");
    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    private final byte [] encodedBytes;
    private final int iEndExclusive;

    public ShortcutDetector(byte[] encodedBytes, int iEndExclusive) {
        this.encodedBytes = encodedBytes;
        this.iEndExclusive = iEndExclusive;
    }
    public ShortcutDetector(byte[] encodedBytes) {
        this(encodedBytes,encodedBytes.length);
    }

    public Charset detect() {

        for (int i = 0; i <iEndExclusive ; i++) {

            int unsigedByte = encodedBytes[i] & 0xFF;

            if (unsigedByte < 0x7F)  {
                //Ist sieben Bit ASCII. In allen drei Encodings gleich
                continue;
            }

            // Wenn bytes 80–9F, dann shortcut zu CP1252. Denn IS0 enthält in diesen Bereich nicht tipbare Steuerzeichen
            // und CP1252 darstellbare Zeichen wie das Euro Zeichen
            if (unsigedByte >= 0x80 && unsigedByte <= 0x9f) {
                return CP1252;
            }

            // Wenn A4 (ISO 885916 € --> short cut denn in CP1252 Currency Placeholder und in UTF8 single byte undefiniert
            // und currenzy Placeholder ist C2 A4 und Euro drei byte E2 82 AC. und A4 ist auch kein gültiges Single-Byte
            // Zeichen in UTF8
            if (unsigedByte == 0xA4) {
                return ISO_8859_15;
            }

            // Das erste UTF-8 Multibyte beginnt bei C2
            if(unsigedByte < 0xC2) {
                continue;
            }

            // UTF-8 Shortcut
            int iExpectedFollowBytes = 0;
            // UTF8-4 Byte 11110xxx (F0)   10xxxxxx    10xxxxxx    10xxxxxx
            if(unsigedByte >= 0xF0) {
                iExpectedFollowBytes = 3;
            // UTF8-3 Byte  1110xxxx (E0)    10xxxxxx    10xxxxxx
            } else if (unsigedByte >= 0xE0) {
                iExpectedFollowBytes = 2;
            // UTF8-2 Byte 110xxxxx (C2)   10xxxxxx
            } else if (unsigedByte >= 0xC2 ) {
                iExpectedFollowBytes = 1;
            }
            int  iuntil = Math.min(encodedBytes.length-1, i+iExpectedFollowBytes);
            // Follow bytes (80..BF)
            for (int j = i+1; j <=iuntil ; j++) {
                int unsignedfollowByte = encodedBytes[j] & 0xFF;
                if (unsignedfollowByte >= 0x80 && unsignedfollowByte <=0xBF) {
                    iExpectedFollowBytes--;
                }
            }
            if (iExpectedFollowBytes == 0) {
                return UTF_8;
            }
        }
        return ISO_8859_15;
    }
}
