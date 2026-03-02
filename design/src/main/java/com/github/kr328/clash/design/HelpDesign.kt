package com.github.kr328.clash.design

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.text.HtmlCompat
import com.github.kr328.clash.design.compose.HelpLinkItemUi
import com.github.kr328.clash.design.compose.HelpScreen

class HelpDesign(
    context: Context,
    openLink: (Uri) -> Unit,
) : Design<Unit>(context) {
    private val links = listOf(
        HelpLinkItemUi(
            title = context.getString(R.string.clash_wiki),
            summary = context.getString(R.string.clash_wiki_url),
            url = context.getString(R.string.clash_wiki_url)
        ),
        HelpLinkItemUi(
            title = context.getString(R.string.clash_meta_wiki),
            summary = context.getString(R.string.clash_meta_wiki_url),
            url = context.getString(R.string.clash_meta_wiki_url)
        ),
        HelpLinkItemUi(
            title = context.getString(R.string.clash_meta_core),
            summary = context.getString(R.string.clash_meta_core_url),
            url = context.getString(R.string.clash_meta_core_url)
        ),
        HelpLinkItemUi(
            title = context.getString(R.string.clash_meta_for_android),
            summary = context.getString(R.string.meta_github_url),
            url = context.getString(R.string.meta_github_url)
        )
    )

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                // Convert HTML string to plain text
                val tipsHtml = context.getString(R.string.tips_help)
                val tipsText = HtmlCompat.fromHtml(tipsHtml, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
                
                HelpScreen(
                    title = context.getString(R.string.help),
                    tips = tipsText,
                    documentTitle = context.getString(R.string.document),
                    sourcesTitle = context.getString(R.string.sources),
                    documentLinks = links.take(2),
                    sourceLinks = links.drop(2),
                    onBackClick = {
                        (context as? AppCompatActivity)?.onBackPressedDispatcher?.onBackPressed()
                    },
                    onOpenLink = { openLink(Uri.parse(it.url)) }
                )
            }
        }
    }
}
