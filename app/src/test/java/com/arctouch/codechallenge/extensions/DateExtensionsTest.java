package com.arctouch.codechallenge.extensions;

import static junit.framework.Assert.*;

import org.junit.Test;

public class DateExtensionsTest {

    @Test
    public void dateBrazilianPortugueseTest(){

        String lang = "português";

        String dateToTest = "2018-04-01";

        String expectedDateResult = "01/04/2018";

        assertEquals(expectedDateResult, DateExtensionsKt.convertToFormattedDate(dateToTest, lang));

    }

    @Test
    public void dateEnglishTest(){

        String lang = "english";

        String dateToTest = "2018-11-25";

        String expectedDateResult = "11/25/2018";

        assertEquals(expectedDateResult, DateExtensionsKt.convertToFormattedDate(dateToTest, lang));

    }

}
