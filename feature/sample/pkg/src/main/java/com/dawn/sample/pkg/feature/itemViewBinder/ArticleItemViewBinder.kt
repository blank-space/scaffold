package com.dawn.sample.pkg.feature.itemViewBinder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import com.dawn.base.utils.onClick
import com.dawn.base.utils.showToast
import com.dawn.sample.export.api.ISampleService
import com.dawn.sample.pkg.feature.data.entity.Article
import com.dawn.sample.pkg.feature.view.ArticleLayout
import com.dawn.webview.view.XWebActivity
import com.drakeet.multitype.ViewDelegate

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
class ArticleItemViewBinder(var sampleService: ISampleService?) : ViewDelegate<Article, ArticleLayout>() {
    private var index = 0

    @SuppressLint("SetTextI18n")
    override fun onBindView(view: ArticleLayout, item: Article) {
        view.tvTitle.text = item.title

        view.tvAuthor.text =
            if (item.author?.isNotEmpty()==true) "作者：${item.author}" else "分享人：${item.shareUser}"

        view.tvCategory.text = "分类：${item.superChapterName}"

        view.tvPublishTime.text = "时间：${item.niceDate}"

        if (index % 2 == 0) {
            view.setBackgroundColor(Color.parseColor("#03DAC5"))
        } else {
            view.setBackgroundColor(Color.WHITE)
        }
        ++index
        view.onClick = {
            item.link?.let { url -> XWebActivity.startAction(it.context,url) }
        }
        view.ivFavorite.onClick = {
            "点击了收藏".showToast()
        }
    }

    override fun onCreateView(context: Context): ArticleLayout {
        return ArticleLayout(context)
    }

}