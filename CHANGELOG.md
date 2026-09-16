# More Usage Filters Changelog

## [1.0.0]

### Added

- `Show Preview Usages` toggle in the Find Usages tool window and in the Show Usages popup. When it is off, every usage
  located inside a declaration annotated with a preview annotation (`@Preview`, multipreview annotations, custom
  `*Preview*` annotations) is hidden.
- `Show Comment Usages` toggle, next to it. When it is off, every usage located inside a comment is hidden, the
  `[Link]` references of a KDoc first and foremost.
- A dedicated toolbar icon for each toggle, `@P` and `/*`, traced from JetBrains Mono so that each one echoes the
  syntax it filters out, like the serif `i` of the import filter.
