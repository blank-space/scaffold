package com.compose.app.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.app.HomeFeedActionBar
import com.compose.app.HomeFeedHeader
import com.compose.app.NetworkImage
import com.compose.app.data.MultiImage
import com.example.compose.IComposableService
import com.google.auto.service.AutoService

@AutoService(IComposableService::class)
class MultiImageComposable : IComposableService<MultiImage> {
    override val content: @Composable (item: MultiImage) -> Unit = { item ->
        HomeFeedHeader(item.tag)
        MultiImageSection(item = item)
    }

    override val type: String
        get() = MultiImage::class.java.name
}

@Composable
fun MultiImageSection(item: MultiImage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        Text(
            text = item.content,
            fontSize = 14.sp,
            maxLines = 5,
            modifier = Modifier.padding(end = 10.dp),
            color =  MaterialTheme.colors.secondary
        )
        Row(
            Modifier
                .requiredHeight(140.dp)
                .padding(top = 10.dp)
                .fillMaxWidth()
        ) {
            item.images.split(",").take(3).forEachIndexed { index, item ->
                NetworkImage(
                    url = item,
                    modifier = Modifier.weight(0.3f),
                )
                if (index != 2) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
        //action bar
        HomeFeedActionBar(
            item.interaction.likeNum.toString(),
            item.interaction.replyNum.toString(),
            Modifier
                .padding(top = 10.dp, end = 10.dp)
                .align(Alignment.End)
        )
    }

}
