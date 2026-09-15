# Filter Out Preview Usages

IntelliJ Platform plugin (Android Studio / IntelliJ IDEA) that adds a **Show Preview Usages** toggle to the usages
filter toolbar, next to the built-in *Show Import Statements* and *Show Generated Code* toggles.

When the toggle is turned off, every usage located inside a declaration annotated with a preview annotation is hidden.
This makes `Cmd+B` / `Ctrl+B` (and Find Usages) actually useful on a Composable that is, of course, always used by its
own `@Preview`.

## What is considered a preview

A usage is hidden when one of its enclosing Kotlin declarations (function, class/object or property) carries an
annotation whose short name contains `preview`, case-insensitively:

- `@Preview` (Android Compose and Compose Multiplatform)
- Google multipreview annotations such as `@PreviewScreenSizes`, `@PreviewLightDark`, `@PreviewFontScale`
- your own multipreview annotations, e.g. `@MyAppThemePreviews`

`@PreviewParameter` / `@PreviewParameterProvider` are explicitly excluded, since they do not mark a preview declaration.

The matching is deliberately name based (no resolution, no index access), so it stays fast and works no matter which
preview library is used.

## Where the toggle shows up

The platform builds the filter toolbar from the registered filtering rules, so the toggle appears in:

- the **Find Usages** tool window toolbar (funnel/filter actions)
- the **Show Usages** popup (`Cmd+B` on a declaration), under the filter button

Its state is persisted between sessions like the other usage filters. A keyboard shortcut can be assigned in
**Settings | Keymap | Other | Show Preview Usages**.

## Building and running

```bash
./gradlew build          # compile, run unit tests and build the plugin distribution
./gradlew runIde         # start a sandbox IDE with the plugin installed
./gradlew verifyPlugin   # run the IntelliJ Plugin Verifier
```

The distribution to share with your colleagues is produced by:

```bash
./gradlew buildPlugin
# -> build/distributions/FilterOutPreviewUsages-<version>.zip
```

Install it in Android Studio via **Settings | Plugins | ⚙ | Install Plugin from Disk...**.

## Compatibility

Built against IntelliJ IDEA 2026.1 (branch 261) and declared compatible with builds 251 and newer, which covers
Android Studio 2025.1 (Narwhal) and later. The Kotlin plugin is required and is bundled in Android Studio.

## Implementation notes

| File | Role |
|------|------|
| `PreviewAnnotationMatcher.kt` | Decides whether an annotation short name marks a preview. Pure logic, unit tested. |
| `PreviewUsageDetector.kt` | Walks the PSI parents of a usage looking for an annotated declaration. |
| `PreviewUsageFilteringRule.kt` | The `UsageFilteringRule` and its `UsageFilteringRuleProvider` extension. |
| `META-INF/plugin.xml` | Registers the extension and the `EmptyAction` used as the toggle presentation. |
