package com.infomaniak.moreusagefilters

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PreviewUsageDetectorTest : BasePlatformTestCase() {

    fun `test usage inside a preview function is detected`() {
        val element = elementAtCaret(
            """
            @Composable
            fun MyButton() = Unit

            @Preview
            @Composable
            fun MyButtonPreview() {
                MyBut<caret>ton()
            }
            """.trimIndent()
        )

        assertTrue(PreviewUsageDetector.isInsidePreviewDeclaration(element))
    }

    fun `test usage inside a regular function is kept`() {
        val element = elementAtCaret(
            """
            @Composable
            fun MyButton() = Unit

            @Composable
            fun MyScreen() {
                MyBut<caret>ton()
            }
            """.trimIndent()
        )

        assertFalse(PreviewUsageDetector.isInsidePreviewDeclaration(element))
    }

    fun `test usage inside a nested lambda of a preview function is detected`() {
        val element = elementAtCaret(
            """
            @Composable
            fun MyButton() = Unit

            @PreviewLightDark
            @Composable
            fun MyButtonPreview() {
                MyTheme {
                    MyBut<caret>ton()
                }
            }
            """.trimIndent()
        )

        assertTrue(PreviewUsageDetector.isInsidePreviewDeclaration(element))
    }

    fun `test usage inside a fully qualified preview annotation is detected`() {
        val element = elementAtCaret(
            """
            @androidx.compose.ui.tooling.preview.Preview(showBackground = true)
            @Composable
            fun MyButtonPreview() {
                MyBut<caret>ton()
            }
            """.trimIndent()
        )

        assertTrue(PreviewUsageDetector.isInsidePreviewDeclaration(element))
    }

    fun `test preview parameter annotation does not hide the usage`() {
        val element = elementAtCaret(
            """
            @Composable
            fun MyScreen(@PreviewParameter(MyProvider::class) name: String) {
                MyBut<caret>ton()
            }
            """.trimIndent()
        )

        assertFalse(PreviewUsageDetector.isInsidePreviewDeclaration(element))
    }

    private fun elementAtCaret(text: String) = myFixture
        .configureByText("Sample.kt", text)
        .findElementAt(myFixture.caretOffset)!!
}
