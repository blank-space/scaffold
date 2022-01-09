package com.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.compose.app.custom.BodyContent2
import com.compose.app.ui.theme.ScaffoldTheme
import com.compose.app.viewmodel.ManageShowcaseViewModel
import com.example.compose.ComposableItem
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScaffoldTheme {
                val viewModel: ManageShowcaseViewModel = viewModel()
                val scrollState = rememberLazyListState()
                BodyContent2()
                /*Column {
                    buttons(scrollState, viewModel)
                    list(scrollState, viewModel)
                }*/
            }
        }
    }

    @Composable
    private fun buttons(scrollState: LazyListState, viewModel: ManageShowcaseViewModel) {
        val coroutineScope = rememberCoroutineScope()

        Row {
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Text("Scroll to the top")
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(viewModel.itemSize() - 1)
                }
            }) {
                Text("Scroll to the end")

            }
        }
    }

    @Composable
    private fun list(scrollState: LazyListState, viewModel: ManageShowcaseViewModel) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 10.dp,
                top = 10.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            state = scrollState
        ) {
            items(viewModel.produceItems()) {
                ComposableItem(item = it)
            }

        }
    }


}


