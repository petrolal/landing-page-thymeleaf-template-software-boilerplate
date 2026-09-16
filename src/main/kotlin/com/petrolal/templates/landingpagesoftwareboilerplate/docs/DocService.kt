package com.petrolal.templates.landingpagesoftwareboilerplate.docs

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap

@Service
class DocService(
    private val markdownParser: DocMarkdownParser,
) {
    private val logger = LoggerFactory.getLogger(DocService::class.java)

    private val pagesMap = ConcurrentHashMap<String, DocPage>()
    private val searchIndex = mutableListOf<SearchItem>()
    private var categoriesCache = listOf<DocCategory>()

    // Canonical category ordering and icons
    private val categoryMetadata =
        mapOf(
            "Getting Started" to CategoryMeta("🚀", 1),
            "Configuration" to CategoryMeta("⚙️", 2),
            "Features & Plugins" to CategoryMeta("🧩", 3),
            "Deployment & Guides" to CategoryMeta("🚢", 4),
            "Reference & FAQ" to CategoryMeta("📚", 5),
        )

    private data class CategoryMeta(
        val icon: String,
        val order: Int,
    )

    @PostConstruct
    fun init() {
        loadDocs()
    }

    @Synchronized
    fun loadDocs() {
        val resolver = PathMatchingResourcePatternResolver()
        val resources: Array<Resource> =
            try {
                resolver.getResources("classpath:docs/**/*.md") + resolver.getResources("classpath:docs/*.md")
            } catch (e: Exception) {
                logger.warn("Could not find docs via pattern: ${e.message}")
                emptyArray()
            }

        val rawPages = mutableListOf<RawDocEntry>()

        for (resource in resources.toSet()) {
            try {
                val filename = resource.filename ?: continue
                if (!filename.endsWith(".md")) continue

                val slug = filename.removeSuffix(".md")
                val content = resource.inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
                val parsed = markdownParser.parse(content, slug)

                rawPages.add(RawDocEntry(slug, filename, parsed))
            } catch (e: Exception) {
                logger.error("Failed to load doc resource: ${resource.filename}", e)
            }
        }

        // Sort all raw pages globally by Category Order, then Page Order, then Title
        val sortedRawPages =
            rawPages.sortedWith(
                compareBy(
                    { categoryMetadata[it.parsed.category]?.order ?: 99 },
                    { it.parsed.order },
                    { it.parsed.title },
                ),
            )

        // Build Nav Pages & chaining for prev/next
        val navPages =
            sortedRawPages.map { entry ->
                DocNavPage(
                    slug = entry.slug,
                    title = entry.parsed.title,
                    category = entry.parsed.category,
                    badge = entry.parsed.badge,
                    order = entry.parsed.order,
                )
            }

        val newPagesMap = ConcurrentHashMap<String, DocPage>()
        val newSearchIndex = mutableListOf<SearchItem>()

        sortedRawPages.forEachIndexed { index, entry ->
            val prevPage = if (index > 0) navPages[index - 1] else null
            val nextPage = if (index < sortedRawPages.size - 1) navPages[index + 1] else null

            val docPage =
                DocPage(
                    slug = entry.slug,
                    title = entry.parsed.title,
                    description = entry.parsed.description,
                    category = entry.parsed.category,
                    badge = entry.parsed.badge,
                    order = entry.parsed.order,
                    contentHtml = entry.parsed.htmlContent,
                    toc = entry.parsed.toc,
                    prevPage = prevPage,
                    nextPage = nextPage,
                    filePath = "docs/${entry.filename}",
                )

            newPagesMap[entry.slug] = docPage

            // Populate search index
            val snippet =
                if (entry.parsed.description.isNotBlank()) {
                    entry.parsed.description
                } else {
                    entry.parsed.plainText.take(160)
                }

            newSearchIndex.add(
                SearchItem(
                    title = entry.parsed.title,
                    category = entry.parsed.category,
                    snippet = snippet,
                    slug = entry.slug,
                ),
            )

            // Also index each TOC heading for deep linking in search!
            entry.parsed.toc.forEach { tocItem ->
                newSearchIndex.add(
                    SearchItem(
                        title = "${entry.parsed.title} > ${tocItem.title}",
                        category = entry.parsed.category,
                        snippet = "Section in ${entry.parsed.title}",
                        slug = entry.slug,
                        url = "/docs/${entry.slug}#${tocItem.id}",
                    ),
                )
            }
        }

        // Build Categories Tree
        val grouped = navPages.groupBy { it.category }
        val categoryList =
            grouped
                .map { (catName, pages) ->
                    val meta = categoryMetadata[catName] ?: CategoryMeta("📄", 99)
                    DocCategory(
                        name = catName,
                        icon = meta.icon,
                        pages = pages,
                    )
                }.sortedBy { categoryMetadata[it.name]?.order ?: 99 }

        pagesMap.clear()
        pagesMap.putAll(newPagesMap)

        searchIndex.clear()
        searchIndex.addAll(newSearchIndex)

        categoriesCache = categoryList
        logger.info("Loaded ${pagesMap.size} documentation pages across ${categoriesCache.size} categories.")
    }

    fun getCategories(): List<DocCategory> = categoriesCache

    fun getPage(slug: String): DocPage? = pagesMap[slug]

    fun getAllPages(): List<DocPage> = pagesMap.values.toList()

    fun getFirstPageSlug(): String =
        categoriesCache
            .firstOrNull()
            ?.pages
            ?.firstOrNull()
            ?.slug ?: "overview"

    fun search(query: String): List<SearchItem> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return emptyList()

        return searchIndex
            .filter { item ->
                item.title.lowercase().contains(q) ||
                    item.snippet.lowercase().contains(q) ||
                    item.category.lowercase().contains(q)
            }.take(12)
    }

    private data class RawDocEntry(
        val slug: String,
        val filename: String,
        val parsed: ParsedDocResult,
    )
}
