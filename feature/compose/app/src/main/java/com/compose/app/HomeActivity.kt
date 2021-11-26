package com.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.compose.app.data.BannerList
import com.compose.app.data.Cover
import com.compose.app.data.MultiImage
import com.compose.app.data.Video
import com.compose.app.home.CoverComposable
import com.compose.app.home.HomeBannerComposable
import com.compose.app.home.MultiImageComposable
import com.compose.app.home.VideoComposable
import com.compose.app.ui.theme.ScaffoldTheme
import com.compose.app.viewmodel.ManageShowcaseViewModel
import com.example.compose.ComposableItem
import com.example.compose.ComposableServiceManager
import com.example.compose.registerComposableService

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       /* registerComposableService(BannerList::class, HomeBannerComposable())
        registerComposableService(Video::class, VideoComposable())
        registerComposableService(Cover::class, CoverComposable())
        registerComposableService(MultiImage::class, MultiImageComposable())*/

       // ComposableServiceManager.collectServices()
        setContent {
            ScaffoldTheme {
                val viewModel: ManageShowcaseViewModel = viewModel()
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 10.dp,
                        top = 10.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(viewModel.produceItems()) {
                        ComposableItem(item = it)
                    }

                }
            }
        }
    }
}


