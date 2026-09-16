package com.petrolal.templates.landingpagesoftwareboilerplate.docs

data class TocItem(
    val id: String,
    val title: String,
    val level: Int = 2,
)

data class DocNavPage(
    val slug: String,
    val title: String,
    val category: String,
    val badge: String? = null,
    val order: Int = 0,
) {
    val url: String
        get() = "/docs/$slug"
}

data class DocCategory(
    val name: String,
    val icon: String,
    val pages: List<DocNavPage>,
)

data class DocPage(
    val slug: String,
    val title: String,
    val description: String,
    val category: String,
    val badge: String? = null,
    val order: Int = 0,
    val contentHtml: String,
    val toc: List<TocItem> = emptyList(),
    val prevPage: DocNavPage? = null,
    val nextPage: DocNavPage? = null,
    val filePath: String = "",
) {
    val url: String
        get() = "/docs/$slug"
}

data class SearchItem(
    val title: String,
    val category: String,
    val snippet: String,
    val slug: String,
    val url: String = "/docs/$slug",
)
