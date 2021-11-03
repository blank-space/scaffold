package com.dawn.sample.pkg.feature.itemViewBinder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import com.dawn.sample.pkg.feature.data.entity.Article
import com.drakeet.multitype.ViewDelegate
import java.util.*

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
class ArticleItemViewBinder : ViewDelegate<Article, ArticleLayout>() {
    private var index = 0

    @SuppressLint("SetTextI18n")
    override fun onBindView(view: ArticleLayout, item: Article) {
        view.tvTitle.text = item.title
        view.tvAuthor.text = "作者：${item.author}"
        view.tvCategory.text = "分类：${item.superChapterName}"
        view.tvPublishTime.text = "时间：${item.niceDate}"
        if (index % 2 == 0) {
            view.setBackgroundColor(Color.parseColor("#03DAC5"))
        }else{
            view.setBackgroundColor(Color.WHITE)
        }
        ++index
    }

    override fun onCreateView(context: Context): ArticleLayout {
        return ArticleLayout(context)
    }

}