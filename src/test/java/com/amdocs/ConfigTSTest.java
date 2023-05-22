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
    })
    void testChangeScpec(String original, String expected) {
        assertEquals(expected, changeScpec(original), original);
    }
}