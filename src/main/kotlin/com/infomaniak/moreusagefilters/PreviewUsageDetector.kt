package com.infomaniak.moreusagefilters

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentsOfType
import org.jetbrains.kotlin.psi.KtAnnotated
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtProperty

/**
 * Detects whether a PSI element is located inside a declaration annotated with a preview annotation.
 */
object PreviewUsageDetector {

    fun isInsidePreviewDeclaration(element: PsiElement): Boolean {
        if (!element.isValid) return false

        return element.parentsOfType<KtAnnotated>(withSelf = true).any(::isPreviewDeclaration)
    }

    private fun isPreviewDeclaration(declaration: KtAnnotated): Boolean {
        // Only declarations can hold a preview annotation, skip the other annotated elements (expressions, ...).
        val isRelevantDeclaration = declaration is KtFunction || declaration is KtClassOrObject || declaration is KtProperty
        if (!isRelevantDeclaration) return false

        return declaration.annotationEntries.any { PreviewAnnotationMatcher.isPreviewAnnotationName(it.shortNameText()) }
    }

    private fun KtAnnotationEntry.shortNameText(): String? {
        calleeExpression?.constructorReferenceExpression?.getReferencedName()?.let { return it }

        return typeReference?.text?.substringBefore('<')
    }
}
