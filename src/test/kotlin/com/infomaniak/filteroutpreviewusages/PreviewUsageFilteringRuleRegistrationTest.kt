package com.infomaniak.filteroutpreviewusages

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.EmptyAction
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.usageView.UsageInfo
import com.intellij.usages.UsageInfo2UsageAdapter
import com.intellij.usages.UsageTarget
import com.intellij.usages.rules.UsageFilteringRuleProvider

class PreviewUsageFilteringRuleRegistrationTest : BasePlatformTestCase() {

    fun `test the filtering rule provider is registered`() {
        val rules = UsageFilteringRuleProvider.EP_NAME.extensionList
            .flatMap { it.getApplicableRules(project) }
            .map { it.ruleId }

        assertTrue(rules.contains(PreviewUsageFilteringRule.ruleId))
    }

    fun `test the toggle action is an EmptyAction with a presentation`() {
        // The platform requires the action of a filtering rule to be an EmptyAction,
        // see com.intellij.usages.impl.UsageFilteringRuleActions.
        val action = ActionManager.getInstance().getAction(PreviewUsageFilteringRule.ACTION_ID)

        assertNotNull("The ${PreviewUsageFilteringRule.ACTION_ID} action is not registered", action)
        assertInstanceOf(action, EmptyAction::class.java)
        assertEquals("Show Preview Usages", action.templatePresentation.text)

        val icon = action.templatePresentation.icon
        assertNotNull("The @P icon failed to load", icon)
        assertEquals(16, icon!!.iconWidth)
        assertEquals(16, icon.iconHeight)
    }

    fun `test the rule hides a usage located in a preview function`() {
        val previewUsage = usageAtCaret(
            """
            @Preview
            @Composable
            fun MyButtonPreview() {
                MyBut<caret>ton()
            }
            """.trimIndent()
        )

        assertFalse(PreviewUsageFilteringRule.isVisible(previewUsage, UsageTarget.EMPTY_ARRAY))
    }

    fun `test the rule keeps a usage located in a regular function`() {
        val regularUsage = usageAtCaret(
            """
            @Composable
            fun MyScreen() {
                MyBut<caret>ton()
            }
            """.trimIndent()
        )

        assertTrue(PreviewUsageFilteringRule.isVisible(regularUsage, UsageTarget.EMPTY_ARRAY))
    }

    private fun usageAtCaret(text: String): UsageInfo2UsageAdapter {
        val element = myFixture.configureByText("Sample.kt", text).findElementAt(myFixture.caretOffset)!!

        return UsageInfo2UsageAdapter(UsageInfo(element))
    }
}
