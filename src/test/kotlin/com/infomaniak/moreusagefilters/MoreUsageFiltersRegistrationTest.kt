package com.infomaniak.moreusagefilters

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.EmptyAction
import com.intellij.openapi.util.IconLoader
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.usages.rules.UsageFilteringRuleProvider
import java.awt.GraphicsEnvironment

class MoreUsageFiltersRegistrationTest : BasePlatformTestCase() {

    fun `test every filtering rule is registered`() {
        val registeredRuleIds = UsageFilteringRuleProvider.EP_NAME.extensionList
            .flatMap { it.getApplicableRules(project) }
            .map { it.ruleId }

        for (rule in ALL_RULES) {
            assertTrue("The ${rule.ruleId} rule is not registered", registeredRuleIds.contains(rule.ruleId))
        }
    }

    fun `test every toggle action is an EmptyAction with a presentation`() {
        // The platform requires the action of a filtering rule to be an EmptyAction,
        // see com.intellij.usages.impl.UsageFilteringRuleActions.
        for ((actionId, expectedText) in EXPECTED_ACTION_TEXTS) {
            val action = ActionManager.getInstance().getAction(actionId)

            assertNotNull("The $actionId action is not registered", action)
            assertInstanceOf(action, EmptyAction::class.java)
            assertEquals(expectedText, action.templatePresentation.text)
            assertNotNull("The icon of $actionId failed to load", action.templatePresentation.icon)
        }
    }

    fun `test both variants of every icon are shipped`() {
        for (icon in ICONS) {
            for (path in listOf("/icons/$icon.svg", "/icons/${icon}_dark.svg")) {
                assertNotNull("$path is missing from the plugin resources", javaClass.getResource(path))
            }
        }
    }

    fun `test every icon rasterizes to 16x16`() {
        // Rasterizing icons is disabled as soon as the JVM is headless, which is the case on CI:
        // com.intellij.ui.icons.isIconActivated defaults to !GraphicsEnvironment.isHeadless(), and every
        // CachedImageIcon then resolves to the 1x1 EMPTY_ICON. It has to be turned on explicitly here.
        val wasActivated = !GraphicsEnvironment.isHeadless()
        IconLoader.activate()

        try {
            for (name in ICONS) {
                val icon = IconLoader.getIcon("/icons/$name.svg", javaClass.classLoader)

                assertEquals("$name is not 16 wide", 16, icon.iconWidth)
                assertEquals("$name is not 16 high", 16, icon.iconHeight)
            }
        } finally {
            if (wasActivated) IconLoader.activate() else IconLoader.deactivate()
        }
    }

    private companion object {
        val ALL_RULES = listOf(PreviewUsageFilteringRule, CommentUsageFilteringRule)

        val EXPECTED_ACTION_TEXTS = mapOf(
            ActionIds.SHOW_PREVIEW_USAGES to "Show Preview Usages",
            ActionIds.SHOW_COMMENT_USAGES to "Show Comment Usages",
        )

        val ICONS = listOf("showPreviewUsages", "showCommentUsages")
    }
}
