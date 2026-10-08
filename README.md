# EN/RU Split View

A Chrome extension for reading multi-language documentation. Click its toolbar icon and the current page
opens side by side, in Chrome's split view, with the same page in the other language:

- `https://example.com/docs/en/guide` → opens `https://example.com/docs/ru/guide` next to it
- `https://example.com/docs/ru/guide` → opens `https://example.com/docs/en/guide` next to it

Only the first `/en/` or `/ru/` in the URL is switched. If the URL has neither, the tab is split with a
duplicate of itself. Clicking the icon on a tab that is already in a split view does nothing.

## Requirements

- **Google Chrome 155 or newer.** Older versions don't let extensions create split views.
- **JDK 17–23** to run the Gradle build. Check with `java -version`.
- Internet access for the first build. Gradle downloads Kotlin, Node.js and the npm packages it needs,
  so you don't have to install Node yourself.

## Build

From the project root:

```sh
./gradlew buildExtension        # on Windows: gradlew.bat buildExtension
```

The ready-to-load extension is written to:

```
build/dist/js/productionExecutable/
├── background.js
├── background.js.map
├── icons/
└── manifest.json
```

## Install in Chrome

1. Open `chrome://extensions`.
2. Turn on **Developer mode** (top-right toggle).
3. Click **Load unpacked** and select the `build/dist/js/productionExecutable` folder.
4. Optional: click the puzzle-piece icon in the toolbar and pin **EN/RU Split View**.

To share the extension without building it, zip the `productionExecutable` folder and send it.
The recipient unzips it and follows the same steps.

## Update

After pulling changes, run `./gradlew buildExtension` again. Then click the reload button (↻) on the
extension's card in `chrome://extensions`.

## Development

| Path                                   | Purpose                                       |
|----------------------------------------|-----------------------------------------------|
| `src/jsMain/kotlin/Main.kt`            | Click handler and URL language switching      |
| `src/jsMain/kotlin/Chrome.kt`          | Kotlin declarations for the Chrome APIs used  |
| `src/jsMain/resources/manifest.json`   | Extension manifest (Manifest V3)              |
| `src/jsMain/resources/icons/`          | Rendered PNG icons used by the manifest       |
| `src/jsTest/kotlin/`                   | Unit tests                                    |
| `icon/icon.svg`                        | Icon source                                   |

The extension is written in Kotlin/JS and bundled into a single `background.js` service worker.

Run the tests (they run on Node, no browser needed):

```sh
./gradlew jsTest
```

Chrome needs PNG icons, so the PNGs are committed. After editing `icon/icon.svg`, regenerate them with
headless Chrome (on macOS; on other systems set `CHROME` to your Chrome binary):

```sh
./icon/render.sh
```

If you change dependencies or Kotlin targets and the build fails with *"Lock file was changed"*, run
`./gradlew kotlinUpgradeYarnLock` and commit the updated `kotlin-js-store/yarn.lock`.

To debug, click the **service worker** link on the extension's card in `chrome://extensions`.
That opens DevTools for the background script, where any errors are logged.
