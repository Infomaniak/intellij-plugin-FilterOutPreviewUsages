package com.infomaniak.moreusagefilters

import com.intellij.psi.PsiElement

/**
 * Hides the usages that are located inside a comment, KDoc and JavaDoc references first and foremost.
 */
object CommentUsageFilteringRule : PsiElementUsageFilteringRule(
    id = "com.infomaniak.moreusagefilters.CommentUsages",
    action = ActionIds.SHOW_COMMENT_USAGES,
) {

    override fun isFilteredOut(element: PsiElement): Boolean = CommentUsageDetector.isInsideComment(element)
}
