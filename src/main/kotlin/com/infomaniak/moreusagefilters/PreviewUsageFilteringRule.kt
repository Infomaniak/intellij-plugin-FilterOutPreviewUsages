package com.infomaniak.moreusagefilters

import com.intellij.psi.PsiElement

/**
 * Hides the usages that are located inside `@Preview` declarations, in the same way the platform hides usages located
 * inside imports or generated code.
 */
object PreviewUsageFilteringRule : PsiElementUsageFilteringRule(
    id = "com.infomaniak.moreusagefilters.PreviewUsages",
    action = ActionIds.SHOW_PREVIEW_USAGES,
) {

    override fun isFilteredOut(element: PsiElement): Boolean = PreviewUsageDetector.isInsidePreviewDeclaration(element)
}
