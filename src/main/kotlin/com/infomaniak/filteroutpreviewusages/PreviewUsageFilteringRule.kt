package com.infomaniak.filteroutpreviewusages

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Computable
import com.intellij.usages.Usage
import com.intellij.usages.UsageTarget
import com.intellij.usages.rules.PsiElementUsage
import com.intellij.usages.rules.UsageFilteringRule
import com.intellij.usages.rules.UsageFilteringRuleProvider

/**
 * Hides the usages that are located inside `@Preview` declarations, in the same way the platform hides usages located
 * inside imports or generated code.
 *
 * The rule is toggled through the [PreviewUsageFilteringRule.ACTION_ID] action, which the platform automatically adds to
 * the filter toolbar of the Find Usages tool window and of the Show Usages popup.
 */
class PreviewUsageFilteringRuleProvider : UsageFilteringRuleProvider {

    override fun getApplicableRules(project: Project): Collection<UsageFilteringRule> = listOf(PreviewUsageFilteringRule)
}

object PreviewUsageFilteringRule : UsageFilteringRule {

    const val ACTION_ID: String = "FilterOutPreviewUsages.ShowPreviewUsages"

    override fun getRuleId(): String = "com.infomaniak.filteroutpreviewusages.PreviewUsages"

    override fun getActionId(): String = ACTION_ID

    override fun isVisible(usage: Usage, targets: Array<out UsageTarget>): Boolean {
        val element = (usage as? PsiElementUsage)?.element ?: return true

        val isPreviewUsage = ApplicationManager.getApplication().runReadAction(
            Computable { PreviewUsageDetector.isInsidePreviewDeclaration(element) }
        )

        return !isPreviewUsage
    }
}
