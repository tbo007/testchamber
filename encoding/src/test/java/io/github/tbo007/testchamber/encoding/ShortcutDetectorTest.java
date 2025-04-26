package io.github.tbo007.testchamber.encoding;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.Charset;
import java.util.stream.Stream;

import static io.github.tbo007.testchamber.encoding.ShortcutDetector.*;
import static org.junit.jupiter.api.Assertions.*;

class ShortcutDetectorTest {

    // 2 Byte UTF-8 // Non ASCII
    private static final String TEST_STR_UMLAUTZ = "Schönheitsköniginnen äffen adere nach";

    // 3 Byte UTF-8 // Non ASCII
    private static final String TEST_STR_EURO = "die EZB verteidigt den € ohne Gnade";

    // ASCII_ONLY --> ISO...
    private static final String TEST_STR_ASCII = "die EZB verteidigt den EURO ohne Gnade";

    private static final String TEST_STR_UTF8_MULTI2 = "Immer diese Ä";
    private static final String TEST_STR_UTF8_MULTI3 = "Immer dieser €";
    // Potentiell utf-8 n bytes, aber ist nicht
    private static final String TEST_STR_POT_UTF8_MULTI = "Das ist ein Test än";

    static Stream<Arguments> testInput() {
        return Stream.of(
                  // Umlaute
                  Arguments.of(TEST_STR_UMLAUTZ,UTF_8),
                  Arguments.of(TEST_STR_UMLAUTZ,ISO_8859_15),
                  // Sonderzeichen
                  Arguments.of(TEST_STR_EURO,UTF_8),
                  Arguments.of(TEST_STR_EURO,ISO_8859_15),
                  Arguments.of(TEST_STR_EURO,CP1252),
                  Arguments.of(TEST_STR_ASCII,ISO_8859_15),
                  // Last byte multi
                  Arguments.of(TEST_STR_UTF8_MULTI2,UTF_8),
                  Arguments.of(TEST_STR_UTF8_MULTI2,ISO_8859_15),
                  Arguments.of(TEST_STR_UTF8_MULTI3,UTF_8),
                  Arguments.of(TEST_STR_UTF8_MULTI3,ISO_8859_15),
                  // nicht genug follow bytes in utf-8. Detector geht in utf-8
                  // Zweig obwhol es iso ist...
                  // ä = E4 (iso) und in utf8 first byte of three byte. Aber
                  // es kommt nur noch n...
                  Arguments.of(TEST_STR_POT_UTF8_MULTI,ISO_8859_15),
                  Arguments.of(TEST_STR_POT_UTF8_MULTI,UTF_8),
                  Arguments.of("Test ISO § (A7) is not accidental UTF-8",ISO_8859_15)
       );
    }

    @ParameterizedTest
    @MethodSource("testInput")
    void detect(String input, Charset expectedCharset) {
        ShortcutDetector d = new ShortcutDetector(input.getBytes(expectedCharset));
        assertEquals(expectedCharset, d.detect());
    }
}