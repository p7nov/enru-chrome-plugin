@file:Suppress("ClassName")

package org.example

import kotlin.js.Promise

// Minimal external declarations for the parts of the Chrome extension API we use.

external interface Tab {
    val id: Int?
    val url: String?
    val splitViewId: Int?
}

external interface CreateProperties {
    var url: String?
    var splitWithTabId: Int?
}

external interface ChromeEvent<T> {
    fun addListener(callback: T)
}

external object chrome {
    object action {
        val onClicked: ChromeEvent<(Tab) -> Unit>
    }

    object tabs {
        val SPLIT_VIEW_ID_NONE: Int
        fun create(createProperties: CreateProperties): Promise<Tab>
        fun duplicate(tabId: Int): Promise<Tab?>
        fun createSplit(tabIds: Array<Int>): Promise<Int>
    }
}
