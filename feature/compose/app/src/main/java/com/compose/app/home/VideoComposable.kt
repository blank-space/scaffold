package com.compose.app.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.compose.app.NetworkImage
import com.compose.app.R
import com.compose.app.data.Video
import com.example.compose.IComposableService
import com.google.auto.service.AutoService

@AutoService(IComposableService::class)
class VideoComposable : IComposableService<Video> {
    
    override val content: @Composable (item: Video) -> Unit = { item ->
        VideoSection(item = item)
    }

    override val type: String = Video::class.java.name

}

@Composable
fun VideoSection(item: Video) {
    Box(modifier = Modifier.padding(top = 5.dp)) {
        NetworkImage(
            url = item.cover,
            modifier = Modifier
                .requiredHeight(height = 220.dp)
                .fillMaxWidth(),
        )
        Image(
            painter = painterResource(id = R.mipmap.video_play),
            "",
            modifier = Modifier.align(Alignment.Center)
        )
        Text(
            text = item.title,
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomStart)
        )
        Text(
            text = "视频", color = Color.White, modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 8.dp)
        )
    }

}