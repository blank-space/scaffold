package com.compose.app.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.compose.app.HomeFeedActionBar
import com.compose.app.HomeFeedHeader
import com.compose.app.NetworkImage
import com.compose.app.data.Cover
import com.example.compose.IComposableService
import com.google.auto.service.AutoService

@AutoService(IComposableService::class)
class CoverComposable : IComposableService<Cover> {
    override val content: @Composable (item: Cover) -> Unit = { item ->
        HomeFeedHeader(item.tag)
        CoverSection(item = item)
    }


    override val type: String
        get() = Cover::class.java.name
}


@Composable
fun CoverSection(item: Cover) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        val (title, cover, actionBar) = createRefs()

        Text(text = item.title,
            fontSize = 16.sp,
            maxLines = 2,
            modifier = Modifier
                .padding(end = 6.dp)
                .constrainAs(title) {
                    start.linkTo(parent.start)
                    end.linkTo(cover.start)
                    top.linkTo(cover.top)
                    bottom.linkTo(actionBar.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            color =  MaterialTheme.colors.secondary
        )
        NetworkImage(
            url = item.cover,
            modifier = Modifier
                .requiredSize(110.dp, 70.dp)
                .constrainAs(cover) {
                    end.linkTo(parent.end)
                    start.linkTo(title.end)
                },
        )
        HomeFeedActionBar(item.interaction.likeNum.toString(),
            item.interaction.replyNum.toString(),
            modifier = Modifier
                .padding(end = 20.dp)
                .constrainAs(actionBar) {
                    end.linkTo(cover.start)
                    top.linkTo(title.bottom)
                    bottom.linkTo(cover.bottom)
                    width = Dimension.fillToConstraints
                })
    }


}
