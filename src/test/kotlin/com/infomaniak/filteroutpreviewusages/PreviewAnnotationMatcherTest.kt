package com.infomaniak.filteroutpreviewusages

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PreviewAnnotationMatcherTest {

    @Test
    fun `matches the compose preview annotations`() {
        assertTrue(PreviewAnnotationMatcher.isPreviewAnnotationName("Preview"))
        assertTrue(PreviewAnnotationMatcher.isPreviewAnnotationName("androidx.compose.ui.tooling.preview.Preview"))
        assertTrue(PreviewAnnotationMatcher.isPreviewAnnotationName("PreviewScreenSizes"))
        assertTrue(PreviewAnnotationMatcher.isPreviewAnnotationName("PreviewLightDark"))
    }

    @Test
    fun `matches custom multipreview annotations`() {
        assertTrue(PreviewAnnotationMatcher.isPreviewAnnotationName("MyAppThemePreviews"))
        assertTrue(PreviewAnnotationMatcher.isPreviewAnnotationName("previewOfSomething"))
    }

    @Test
    fun `does not match unrelated annotations`() {
        assertFalse(PreviewAnnotationMatcher.isPreviewAnnotationName("Composable"))
        assertFalse(PreviewAnnotationMatcher.isPreviewAnnotationName("Test"))
        assertFalse(PreviewAnnotationMatcher.isPreviewAnnotationName(null))
        assertFalse(PreviewAnnotationMatcher.isPreviewAnnotationName(""))
    }

    @Test
    fun `does not match preview parameter annotations`() {
        assertFalse(PreviewAnnotationMatcher.isPreviewAnnotationName("PreviewParameter"))
        assertFalse(PreviewAnnotationMatcher.isPreviewAnnotationName("androidx.compose.ui.tooling.preview.PreviewParameter"))
    }
}
