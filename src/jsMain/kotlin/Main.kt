package org.example

import kotlin.js.Promise

private val LANGUAGE_SEGMENT = Regex("/(en|ru)/")

fun main() {
    chrome.action.onClicked.addListener { tab -> openSplitView(tab) }
}

private fun openSplitView(tab: Tab) {
    val tabId = tab.id ?: return
    // A tab can belong to only one split view
    if (tab.splitViewId != null && tab.splitViewId != chrome.tabs.SPLIT_VIEW_ID_NONE) return

    val translatedUrl = tab.url?.let(::switchLanguage)
    val split: Promise<*> = if (translatedUrl != null) {
        chrome.tabs.create(createProperties(url = translatedUrl, splitWithTabId = tabId))
    } else {
        splitWithDuplicate(tabId)
    }
    split.catch { console.error("Failed to open split view:", it) }
}

/** Swaps the first `/en/` or `/ru/` segment of [url] for the other language, or returns null if there is none. */
internal fun switchLanguage(url: String): String? {
    val match = LANGUAGE_SEGMENT.find(url) ?: return null
    val other = if (match.groupValues[1] == "en") "ru" else "en"
    return url.replaceRange(match.range, "/$other/")
}

private fun splitWithDuplicate(tabId: Int): Promise<Int> =
    // The duplicate opens right next to the original, in the same window/group/pinned state,
    // which is exactly what createSplit requires.
    chrome.tabs.duplicate(tabId).then { duplicate ->
        val duplicateId = duplicate?.id ?: error("Failed to duplicate tab $tabId")
        chrome.tabs.createSplit(arrayOf(tabId, duplicateId))
    }.unsafeCast<Promise<Int>>()

private fun createProperties(url: String, splitWithTabId: Int): CreateProperties =
    js("{}").unsafeCast<CreateProperties>().apply {
        this.url = url
        this.splitWithTabId = splitWithTabId
    }
