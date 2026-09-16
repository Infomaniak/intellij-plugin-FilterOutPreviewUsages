package com.infomaniak.moreusagefilters

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType

/**
 * Detects whether a PSI element is located inside a comment.
 *
 * KDoc and JavaDoc are both comments as far as the PSI is concerned (`KDoc` implements `PsiDocCommentBase`, which
 * extends [PsiComment]), so matching on [PsiComment] covers the `[Link]` references of a KDoc, the `{@link ...}` of a
 * JavaDoc, and the plain comments that only show up when Find Usages is asked to search in comments and strings.
 */
object CommentUsageDetector {

    fun isInsideComment(element: PsiElement): Boolean {
        if (!element.isValid) return false

        return element.parentOfType<PsiComment>(withSelf = true) != null
    }
}
