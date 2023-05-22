package com.amdocs;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.amdocs.ConfigTS.changeScpec;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigTSTest {
    @ParameterizedTest
    @CsvSource(delimiter = '\u0001', value = {
            "simple string with space\u0001simple string with space",
            "simple-string-with-dash\u0001simple-string-with-dash",
            "replace1~\u0001replace1%7e",
            "replace2{}\u0001replace2%7b%7d",
            "replace3\"\u0001replace3%22",
            "replace4<\u0001replace4%3c",
            "replace5^\u0001replace5%5e",
            "replace6#\u0001replace6%23",
            "replace7>\u0001replace7%3e",
            "replace8|\u0001replace8%7c",
            "replace9\\\u0001replace9%5c",
            "replace10][\u0001replace10%5d%5b",
            "replace11`\u0001replace11%60",
            "replace12~\u0001replace12%7e",
            "replace13'\u0001replace13%20",
    })
    void testChangeScpec(String original, String expected) {
        assertEquals(expected, changeScpec(original), original);
    }
}