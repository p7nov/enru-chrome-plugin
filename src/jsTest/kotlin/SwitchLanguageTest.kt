package org.example

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SwitchLanguageTest {
    @Test
    fun englishToRussian() =
        assertEquals("https://example.com/docs/ru/guide/", switchLanguage("https://example.com/docs/en/guide/"))

    @Test
    fun russianToEnglish() =
        assertEquals("https://example.com/ru-docs/en/a#b", switchLanguage("https://example.com/ru-docs/ru/a#b"))

    @Test
    fun onlyFirstSegmentIsSwitched() =
        assertEquals("https://example.com/ru/en/", switchLanguage("https://example.com/en/en/"))

    @Test
    fun noLanguageSegment() =
        assertNull(switchLanguage("https://example.com/english/page"))
}
