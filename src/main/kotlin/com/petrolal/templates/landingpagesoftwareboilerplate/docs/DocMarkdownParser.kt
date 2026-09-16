package com.petrolal.templates.landingpagesoftwareboilerplate.docs

import org.commonmark.ext.autolink.AutolinkExtension
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import org.springframework.stereotype.Component
import java.util.regex.Matcher
import java.util.regex.Pattern

data class ParsedDocResult(
    val title: String,
    val description: String,
    val category: String,
    val badge: String?,
    val order: Int,
    val htmlContent: String,
    val toc: List<TocItem>,
    val plainText: String,
)

@Component
class DocMarkdownParser {
    private val extensions =
        listOf(
            TablesExtension.create(),
            AutolinkExtension.create(),
            HeadingAnchorExtension.builder().idPrefix("").build(),
        )

    private val parser: Parser =
        Parser
            .builder()
            .extensions(extensions)
            .build()

    private val renderer: HtmlRenderer =
        HtmlRenderer
            .builder()
            .extensions(extensions)
            .build()

    fun parse(
        rawMarkdown: String,
        fallbackSlug: String,
    ): ParsedDocResult {
        var markdown = rawMarkdown.trim()
        val frontmatterMap = mutableMapOf<String, String>()

        // 1. Parse YAML-like frontmatter if present (between leading --- and ---)
        if (markdown.startsWith("---")) {
            val secondSeparatorIndex = markdown.indexOf("---", 3)
            if (secondSeparatorIndex != -1) {
                val frontmatterContent = markdown.substring(3, secondSeparatorIndex)
                markdown = markdown.substring(secondSeparatorIndex + 3).trim()

                frontmatterContent.lines().forEach { line ->
                    val colonIndex = line.indexOf(':')
                    if (colonIndex != -1) {
                        val key = line.substring(0, colonIndex).trim().lowercase()
                        val value =
                            line
                                .substring(colonIndex + 1)
                                .trim()
                                .removeSurrounding("\"")
                                .removeSurrounding("'")
                        frontmatterMap[key] = value
                    }
                }
            }
        }

        // 2. Extract title if not in frontmatter
        var title = frontmatterMap["title"]
        if (title.isNullOrBlank()) {
            val titleMatch = Regex("""^#\s+(.+)$""", RegexOption.MULTILINE).find(markdown)
            if (titleMatch != null) {
                title = titleMatch.groupValues[1].trim()
                // Remove the h1 from markdown to avoid double title rendering
                markdown = markdown.replaceFirst(titleMatch.value, "").trim()
            } else {
                title = fallbackSlug.replace('-', ' ').replaceFirstChar { it.uppercase() }
            }
        } else {
            // Also remove top-level h1 if present to avoid redundancy
            val titleMatch = Regex("""^#\s+(.+)$""", RegexOption.MULTILINE).find(markdown)
            if (titleMatch != null && titleMatch.groupValues[1].trim().equals(title, ignoreCase = true)) {
                markdown = markdown.replaceFirst(titleMatch.value, "").trim()
            }
        }

        val description = frontmatterMap["description"] ?: ""
        val category = frontmatterMap["category"] ?: "General"
        val badge = frontmatterMap["badge"]
        val order = frontmatterMap["order"]?.toIntOrNull() ?: 0

        // 3. Process Custom Callouts / Admonitions (:::tip, :::info, :::warning, :::danger)
        markdown = processAdmonitions(markdown)

        // 4. Parse Markdown into HTML with CommonMark
        val document = parser.parse(markdown)
        var rawHtml = renderer.render(document)

        // 5. Enhance Headings and Build Table of Contents
        val (enhancedHtmlWithHeadings, toc) = processHeadingsAndToc(rawHtml)

        // 6. Enhance Code Blocks with header bar and copy button
        val finalHtml = processCodeBlocks(enhancedHtmlWithHeadings)

        // 7. Extract plain text for search indexing
        val plainText = extractPlainText(markdown)

        return ParsedDocResult(
            title = title,
            description = description,
            category = category,
            badge = badge,
            order = order,
            htmlContent = finalHtml,
            toc = toc,
            plainText = plainText,
        )
    }

    private fun processAdmonitions(markdown: String): String {
        val pattern =
            Pattern.compile(
                "(?m)^:::(tip|info|note|warning|danger|important)(?:\\s+(.*?))?\\s*\\n([\\s\\S]*?)^:::",
                Pattern.MULTILINE,
            )
        val matcher = pattern.matcher(markdown)
        val sb = StringBuffer()

        while (matcher.find()) {
            val type = matcher.group(1).lowercase()
            val customTitle = matcher.group(2)?.trim()
            val body = matcher.group(3)?.trim() ?: ""

            val (borderClass, bgClass, textClass, icon, defaultTitle) =
                when (type) {
                    "tip" ->
                        AdmonitionStyle(
                            "border-emerald-500/30 dark:border-emerald-500/20",
                            "bg-emerald-500/5 dark:bg-emerald-950/20",
                            "text-emerald-800 dark:text-emerald-300",
                            "💡",
                            "TIP",
                        )
                    "warning" ->
                        AdmonitionStyle(
                            "border-amber-500/30 dark:border-amber-500/20",
                            "bg-amber-500/5 dark:bg-amber-950/20",
                            "text-amber-800 dark:text-amber-300",
                            "⚠️",
                            "WARNING",
                        )
                    "danger", "important" ->
                        AdmonitionStyle(
                            "border-rose-500/30 dark:border-rose-500/20",
                            "bg-rose-500/5 dark:bg-rose-950/20",
                            "text-rose-800 dark:text-rose-300",
                            "🚨",
                            "IMPORTANT",
                        )
                    else ->
                        AdmonitionStyle(
                            "border-indigo-500/30 dark:border-indigo-500/20",
                            "bg-indigo-500/5 dark:bg-indigo-950/20",
                            "text-indigo-800 dark:text-indigo-300",
                            "ℹ️",
                            "NOTE",
                        )
                }

            val displayTitle = if (!customTitle.isNullOrBlank()) customTitle else defaultTitle

            val replacement =
                """
<div class="my-6 rounded-xl border-l-4 $borderClass $bgClass p-4 shadow-sm">
  <div class="flex items-center gap-2 font-semibold text-sm $textClass mb-2">
    <span>$icon</span>
    <span>$displayTitle</span>
  </div>
  <div class="text-sm text-slate-700 dark:text-slate-300 leading-relaxed space-y-2">
$body
  </div>
</div>
                """.trimIndent()

            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement))
        }
        matcher.appendTail(sb)
        return sb.toString()
    }

    private data class AdmonitionStyle(
        val border: String,
        val bg: String,
        val text: String,
        val icon: String,
        val title: String,
    )

    private fun processHeadingsAndToc(html: String): Pair<String, List<TocItem>> {
        val tocList = mutableListOf<TocItem>()
        val headingRegex = Regex("""<h([23])([^>]*)>(.*?)</h\1>""", RegexOption.DOT_MATCHES_ALL)

        val replacedHtml =
            headingRegex.replace(html) { matchResult ->
                val level = matchResult.groupValues[1].toInt()
                val existingAttrs = matchResult.groupValues[2]
                val innerHtml = matchResult.groupValues[3]

                // Clean title for TOC
                val titleText = innerHtml.replace(Regex("<[^>]*>"), "").trim()

                // Find or generate ID
                val idMatch = Regex("""id=["']([^"']+)["']""").find(existingAttrs)
                val slugId = idMatch?.groupValues?.get(1) ?: slugify(titleText)

                tocList.add(TocItem(id = slugId, title = titleText, level = level))

                val anchorTag =
                    """<a href="#$slugId" class="anchor-link ml-2 opacity-0 """ +
                        """group-hover:opacity-100 text-indigo-500 hover:text-indigo-600 transition-opacity" """ +
                        """aria-hidden="true">#</a>"""

                """<h$level id="$slugId" class="group relative flex items-center scroll-mt-24">$innerHtml$anchorTag</h$level>"""
            }

        return Pair(replacedHtml, tocList)
    }

    private fun processCodeBlocks(html: String): String {
        // Match <pre><code class="language-xyz">...</code></pre> or <pre><code>...</code></pre>
        val codeRegex = Regex("""<pre><code(?:\s+class="language-([^"]+)")?>([\s\S]*?)</code></pre>""")

        return codeRegex.replace(html) { matchResult ->
            val lang = matchResult.groupValues[1].ifBlank { "text" }
            val code = matchResult.groupValues[2]

            val displayLang =
                when (lang.lowercase()) {
                    "kts", "kotlin" -> "KOTLIN"
                    "sh", "bash", "shell", "zsh" -> "BASH"
                    "yml", "yaml" -> "YAML"
                    "docker", "dockerfile" -> "DOCKER"
                    "html" -> "HTML"
                    "js", "javascript" -> "JS"
                    "json" -> "JSON"
                    "sql" -> "SQL"
                    "lua" -> "LUA"
                    "css" -> "CSS"
                    else -> lang.uppercase()
                }

            """
<div class="code-block-wrapper my-6 rounded-xl border border-slate-200 dark:border-slate-800 bg-slate-900 text-slate-100 overflow-hidden shadow-sm">
  <div class="flex items-center justify-between px-4 py-2 bg-slate-800/80 border-b border-slate-700/50 text-xs font-mono text-slate-400">
    <span class="flex items-center gap-2 font-medium tracking-wider text-slate-300">
      <span class="w-2 h-2 rounded-full bg-indigo-500"></span>
      $displayLang
    </span>
    <button type="button" class="copy-code-btn flex items-center gap-1.5 px-2 py-1 rounded hover:bg-slate-700 text-slate-400 hover:text-slate-100 transition-all text-xs cursor-pointer" onclick="copyCodeBlock(this)">
      <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"></path>
      </svg>
      <span>Copy</span>
    </button>
  </div>
  <pre class="p-4 overflow-x-auto text-sm leading-relaxed font-mono bg-slate-900 text-slate-100"><code class="language-$lang">$code</code></pre>
</div>
            """.trimIndent()
        }
    }

    private fun slugify(input: String): String =
        input
            .lowercase()
            .replace(Regex("""[^a-z0-9\s-]"""), "")
            .trim()
            .replace(Regex("""\s+"""), "-")
            .ifBlank { "section" }

    private fun extractPlainText(markdown: String): String =
        markdown
            .replace(Regex("""```[\s\S]*?```"""), " ") // Remove code blocks
            .replace(Regex("""`[^`]+`"""), " ") // Remove inline code
            .replace(Regex("""\[([^\]]+)\]\([^\)]+\)"""), "$1") // Remove links
            .replace(Regex("""[#*_>~-]"""), " ") // Remove markdown symbols
            .replace(Regex("""\s+"""), " ")
            .trim()
}
