/**
 * ownCloud Android client application
 *
 * Copyright (C) 2026 ownCloud GmbH.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.owncloud.android.ui.preview

import com.owncloud.android.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.io.Reader
import java.io.StringReader

class PreviewTextFragmentTest {

    @Test
    fun `readTextPreview returns a short source unchanged`() {
        val source = "First line\n\nThird line\n"

        val result = PreviewTextFragment.readTextPreview(StringReader(source), 100)

        assertEquals(source, result.text)
        assertFalse(result.isTruncated)
    }

    @Test
    fun `readTextPreview preserves an empty source`() {
        val result = PreviewTextFragment.readTextPreview(StringReader(""), 100)

        assertEquals("", result.text)
        assertFalse(result.isTruncated)
    }

    @Test
    fun `readTextPreview returns an exact limit source without truncation`() {
        val source = "12345678"

        val result = PreviewTextFragment.readTextPreview(StringReader(source), source.length)

        assertEquals(source, result.text)
        assertFalse(result.isTruncated)
    }

    @Test
    fun `readTextPreview bounds an oversized line before reading the remainder`() {
        val limit = 8
        val reader = RepeatingReader(totalCharacters = 1_000_000)

        val result = PreviewTextFragment.readTextPreview(reader, limit)

        assertEquals("a".repeat(limit), result.text)
        assertTrue(result.isTruncated)
        assertEquals(limit + 1, reader.charactersRead)
    }

    @Test
    fun `readTextPreview applies the production character limit`() {
        val previewLimit = 1024 * 1024
        val reader = RepeatingReader(totalCharacters = previewLimit * 2)

        val result = PreviewTextFragment.readTextPreview(reader)

        assertEquals(previewLimit, result.text.length)
        assertTrue(result.isTruncated)
        assertEquals(previewLimit + 1, reader.charactersRead)
    }

    @Test
    fun `truncated result selects the localized notice without changing its text`() {
        val result = PreviewTextFragment.readTextPreview(StringReader("123456789"), 8)

        assertEquals("12345678", result.text)
        assertEquals(R.string.text_preview_truncated, PreviewTextFragment.truncationNotice(result))
    }

    @Test
    fun `untruncated result does not select a notice`() {
        val result = PreviewTextFragment.readTextPreview(StringReader("12345678"), 8)

        assertNull(PreviewTextFragment.truncationNotice(result))
    }

    @Test(expected = IOException::class)
    fun `readTextPreview propagates read failures`() {
        PreviewTextFragment.readTextPreview(InterruptedReader(), 8)
    }

    private class RepeatingReader(private val totalCharacters: Int) : Reader() {
        var charactersRead = 0
            private set

        override fun read(buffer: CharArray, offset: Int, length: Int): Int {
            if (charactersRead == totalCharacters) return -1

            val charactersToRead = minOf(length, totalCharacters - charactersRead)
            buffer.fill('a', offset, offset + charactersToRead)
            charactersRead += charactersToRead
            return charactersToRead
        }

        override fun close() = Unit
    }

    private class InterruptedReader : Reader() {
        override fun read(buffer: CharArray, offset: Int, length: Int): Int = throw IOException("Interrupted")

        override fun close() = Unit
    }
}
