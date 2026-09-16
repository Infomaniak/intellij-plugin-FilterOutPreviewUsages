package com.infomaniak.moreusagefilters

/**
 * Decides whether an annotation short name marks a declaration as a preview.
 *
 * The matching is intentionally name based so that it works with `@Preview` from Compose (both the Android and the
 * Desktop/Multiplatform ones), with the multipreview annotations shipped by Google (`@PreviewScreenSizes`,
 * `@PreviewLightDark`, ...) and with the custom multipreview annotations teams define themselves
 * (`@MyAppThemePreviews`, ...).
 */
object PreviewAnnotationMatcher {

    private const val PREVIEW_MARKER = "preview"

    /**
     * Annotations that contain "preview" but are not applied to preview declarations.
     */
    private val EXCLUDED_NAMES = setOf(
        "PreviewParameter",
        "PreviewParameterProvider",
    )

    fun isPreviewAnnotationName(shortName: String?): Boolean {
        val name = shortName?.substringAfterLast('.')?.trim().orEmpty()
        if (name.isEmpty() || name in EXCLUDED_NAMES) return false

        return name.contains(PREVIEW_MARKER, ignoreCase = true)
    }
}
