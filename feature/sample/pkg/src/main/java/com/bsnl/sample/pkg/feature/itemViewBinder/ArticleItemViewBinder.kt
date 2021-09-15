package com.bsnl.sample.pkg.feature.itemViewBinder

import android.content.Context
import android.widget.TextView
import com.bsnl.sample.pkg.feature.data.entity.Article
import com.drakeet.multitype.ViewDelegate

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
class ArticleItemViewBinder : ViewDelegate<Article,ArticleLayout>() {

    override fun onBindView(view: ArticleLayout, item: Article) {
       view.tvTitle.text = item.title
        view.tvAuthor.text = "作者：${item.author}"
        view.tvCategory.text = "分类：${item.superChapterName}"
        view.tvPublishTime.text ="时间：${item.niceDate}"
    }

    override fun onCreateView(context: Context): ArticleLayout {
        return  ArticleLayout(context)
    }

}