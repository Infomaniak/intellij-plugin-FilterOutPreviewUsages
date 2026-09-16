package com.infomaniak.moreusagefilters

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Computable
import com.intellij.psi.PsiElement
import com.intellij.usages.Usage
import com.intellij.usages.UsageTarget
import com.intellij.usages.rules.PsiElementUsage
import com.intellij.usages.rules.UsageFilteringRule
import com.intellij.usages.rules.UsageFilteringRuleProvider

/**
 * Ids of the actions the platform turns into the filter toggles. They must match the ids declared in `plugin.xml`.
 */
object ActionIds {
    const val SHOW_PREVIEW_USAGES: String = "MoreUsageFilters.ShowPreviewUsages"
    const val SHOW_COMMENT_USAGES: String = "MoreUsageFilters.ShowCommentUsages"
}

/**
 * Declares every filtering rule of the plugin. The platform builds one toggle per rule and adds it to the filter
 * toolbar of the Find Usages tool window and of the Show Usages popup.
 */
class MoreUsageFiltersRuleProvider : UsageFilteringRuleProvider {

    override fun getApplicableRules(project: Project): Collection<UsageFilteringRule> = listOf(
        PreviewUsageFilteringRule,
        CommentUsageFilteringRule,
    )
}

/**
 * Base class of the rules that decide from the PSI element behind a usage.
 *
 * The rules are worded as "Show ...", so a usage is visible exactly when it is not filtered out.
 */
abstract class PsiElementUsageFilteringRule(
    private val id: String,
    private val action: String,
) : UsageFilteringRule {

    override fun getRuleId(): String = id

    override fun getActionId(): String = action

    override fun isVisible(usage: Usage, targets: Array<out UsageTarget>): Boolean {
        // Usages that are not backed by a PSI element cannot be judged, so they are always kept.
        val element = (usage as? PsiElementUsage)?.element ?: return true

        val isFilteredOut = ApplicationManager.getApplication().runReadAction(Computable { isFilteredOut(element) })

        return !isFilteredOut
    }

    protected abstract fun isFilteredOut(element: PsiElement): Boolean
}
