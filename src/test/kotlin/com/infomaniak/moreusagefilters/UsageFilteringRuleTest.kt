package com.infomaniak.moreusagefilters

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.usageView.UsageInfo
import com.intellij.usages.UsageInfo2UsageAdapter
import com.intellij.usages.UsageTarget

class UsageFilteringRuleTest : BasePlatformTestCase() {

    fun `test the preview rule hides a usage located in a preview function`() {
        val usage = usageAtCaret(
            """
            @Preview
            @Composable
            fun MyButtonPreview() {
                MyBut<caret>ton()
            }
            """.trimIndent()
        )

        assertFalse(PreviewUsageFilteringRule.isVisible(usage, UsageTarget.EMPTY_ARRAY))
    }

    fun `test the preview rule keeps a usage located in a regular function`() {
        val usage = usageAtCaret(
            """
            @Composable
            fun MyScreen() {
                MyBut<caret>ton()
            }
            """.trimIndent()
        )

        assertTrue(PreviewUsageFilteringRule.isVisible(usage, UsageTarget.EMPTY_ARRAY))
    }

    fun `test the comment rule hides a usage located in a KDoc link`() {
        val usage = usageAtCaret(
            """
            /**
             * Wraps [MyBut<caret>ton].
             */
            fun MyWrapper() = Unit
            """.trimIndent()
        )

        assertFalse(CommentUsageFilteringRule.isVisible(usage, UsageTarget.EMPTY_ARRAY))
    }

    fun `test the comment rule hides a usage located in a line comment`() {
        val usage = usageAtCaret(
            """
            fun MyScreen() {
                // Replaces MyBut<caret>ton.
            }
            """.trimIndent()
        )

        assertFalse(CommentUsageFilteringRule.isVisible(usage, UsageTarget.EMPTY_ARRAY))
    }

    fun `test the comment rule keeps a usage located in code`() {
        val usage = usageAtCaret(
            """
            fun MyScreen() {
                MyBut<caret>ton()
            }
            """.trimIndent()
        )

        assertTrue(CommentUsageFilteringRule.isVisible(usage, UsageTarget.EMPTY_ARRAY))
    }

    fun `test the preview rule keeps a usage located in a comment`() {
        // The two rules are independent, each one only filters out what its own toggle is about.
        val usage = usageAtCaret(
            """
            fun MyScreen() {
                // Replaces MyBut<caret>ton.
            }
            """.trimIndent()
        )

        assertTrue(PreviewUsageFilteringRule.isVisible(usage, UsageTarget.EMPTY_ARRAY))
    }

    private fun usageAtCaret(text: String): UsageInfo2UsageAdapter {
        val element = myFixture.configureByText("Sample.kt", text).findElementAt(myFixture.caretOffset)!!

        return UsageInfo2UsageAdapter(UsageInfo(element))
    }
}
