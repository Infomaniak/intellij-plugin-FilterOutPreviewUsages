# More Usage Filters Changelog

## [1.1.0]

### Added

- `Show Comment Usages` toggle, hiding every usage located inside a comment: the `[Link]` references of a KDoc, the
  `{@link ...}` of a JavaDoc, and the plain comments reported when Find Usages searches in comments and strings.
- A `/*` toolbar icon for it, traced from JetBrains Mono like the `@P` one.

### Changed

- The plugin is now named `More Usage Filters`, since it is no longer only about previews.

## [1.0.0]

### Added

- `Show Preview Usages` toggle in the Find Usages tool window and in the Show Usages popup.
- Usages located inside declarations annotated with a preview annotation (`@Preview`, multipreview annotations, custom
  `*Preview*` annotations) are hidden when the toggle is off.
- A dedicated `@P` toolbar icon traced from JetBrains Mono, matching the serif `i` of the import filter.
- A plugin icon showing the same `@P` inside a "forbidden" sign.
