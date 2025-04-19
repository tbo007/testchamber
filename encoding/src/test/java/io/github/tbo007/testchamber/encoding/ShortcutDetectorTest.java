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

    static Stream<Arguments> testInput() {
        return Stream.of(
                  // Umlaute
                  Arguments.of(TEST_STR_UMLAUTZ,UTF_8),
                  Arguments.of(TEST_STR_UMLAUTZ,ISO_8859_15),
                  // Sonderzeichen
                  Arguments.of(TEST_STR_EURO,UTF_8),
                  Arguments.of(TEST_STR_EURO,ISO_8859_15),
                  Arguments.of(TEST_STR_EURO,CP1252)
       );
    }

    @ParameterizedTest
    @MethodSource("testInput")
    void detect(String input, Charset expectedCharset) {
        ShortcutDetector d = new ShortcutDetector(input.getBytes(expectedCharset));
        assertEquals(expectedCharset, d.detect());
    }
}