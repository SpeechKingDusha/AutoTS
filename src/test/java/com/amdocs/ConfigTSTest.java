package com.amdocs;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.amdocs.ConfigTS.changeScpec;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigTSTest {
    @ParameterizedTest
    @CsvSource(delimiter = '\u0001', value = {
            "simple string with space\u0001simple string with spacesimple string with spacesimple string with spacesimple string with spacesimple string with spacesimple string with spacesimple string with spacesimple string with spacesimple string with spacesimple string with spacesimple string with spacesimple string with spacesimple string with spacesimple string with space",// todo Should encode to "simple string with space"
            "simple-string-with-dash\u0001simple-string-with-dashsimple-string-with-dashsimple-string-with-dashsimple-string-with-dashsimple-string-with-dashsimple-string-with-dashsimple-string-with-dashsimple-string-with-dashsimple-string-with-dashsimple-string-with-dashsimple-string-with-dashsimple-string-with-dashsimple-string-with-dashsimple-string-with-dash",// todo Should encode to "simple-string-with-dash"
            "replace1~\u0001replace1~replace1~replace1~replace1~replace1~replace1~replace1%7ereplace1~replace1~replace1~replace1~replace1~replace1~replace1~",// todo Should encode to "replace1%7e"
            "replace2{}\u0001replace2%7b}replace2{}replace2{%7dreplace2{}replace2{}replace2{}replace2{}replace2{}replace2{}replace2{}replace2{}replace2{}replace2{}replace2{}",// todo Should encode to "replace2%7b%7d"
    })
    void testChangeScpec(String original, String expected) {
        assertEquals(expected, changeScpec(original), original);
    }
}