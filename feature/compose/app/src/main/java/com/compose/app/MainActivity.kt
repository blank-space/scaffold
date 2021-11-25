package com.compose.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.compose.app.data.Message
import com.compose.app.data.SampleData
import com.compose.app.ui.theme.ScaffoldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScaffoldTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    conversation(messages = SampleData.conversationSample)
                }
            }
        }
    }
}


@Composable
fun conversation(messages: List<Message>) {
    // LazyColumn is the Compose version of a RecyclerView.
    // The lambda passed to items() is similar to a RecyclerView.ViewHolder.
    LazyColumn {
        items(messages) { it ->
            messageCard(msg = it)
        }
    }
}

@Composable
fun messageCard(msg: Message) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        var stateText by remember {
            if (msg.body.length > 22) {
                mutableStateOf("...")
            } else {
                mutableStateOf("")
            }
        }

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor: Color by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.secondary
            else MaterialTheme.colors.surface
        )
        // Create references for the composables to constrain
        val (image, title, content, more) = createRefs()


        Image(
            painter = painterResource(id = R.mipmap.avatar), contentDescription = "bird",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
                .constrainAs(image) {
                    top.linkTo(parent.top, 24.dp)
                    start.linkTo(parent.start, 18.dp)
                }
                .padding(horizontal = 4.dp)
        )

        Text(text = msg.title, color = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, 24.dp)
                start.linkTo(image.end, margin = 6.dp)
            })

        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 1.dp,
            color = surfaceColor,
            modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(title.bottom)
                    start.linkTo(image.end, margin = 6.dp)
                    end.linkTo(more.start)
                    width = Dimension.fillToConstraints
                }
        ) {
            Text(
                text = msg.body,
                modifier = Modifier.padding(all = 4.dp),
                maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                style = MaterialTheme.typography.body2,
            )
        }


        Text(text = stateText, modifier = Modifier
            .padding(4.dp)
            .clickable {
                isExpanded = !isExpanded
                stateText = if (isExpanded) {
                    "^"
                } else {
                    "..."
                }
            }
            .constrainAs(more) {
                bottom.linkTo(content.bottom)
                end.linkTo(parent.end, margin = 18.dp)
            })



    }

}


@Preview(name = "Light Mode", showBackground = true)

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)

@Composable
fun DefaultPreview() {
    ScaffoldTheme {
        conversation(messages = SampleData.conversationSample)
    }
}