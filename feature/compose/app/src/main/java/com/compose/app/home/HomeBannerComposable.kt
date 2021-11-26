package com.compose.app.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.compose.app.NetworkImage
import com.compose.app.data.BannerList
import com.example.compose.IComposableService
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.auto.service.AutoService

@AutoService(IComposableService::class)
class HomeBannerComposable : IComposableService<BannerList> {
    @ExperimentalPagerApi
    override val content: @Composable (item: BannerList) -> Unit = { item ->
        HomeBannerSection(item)
    }
    override val type: String
        get() = BannerList::javaClass.name
}


@ExperimentalPagerApi
@Composable
fun HomeBannerSection(data: BannerList) {
    val bannerState = remember {
        PagerState(data.list.size)
    }
    HorizontalPager(state = bannerState, modifier = Modifier.fillMaxWidth()) {
        val cover = data.list[this.currentPage].cover
        Box {
            NetworkImage(
                url = cover,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

    }
}