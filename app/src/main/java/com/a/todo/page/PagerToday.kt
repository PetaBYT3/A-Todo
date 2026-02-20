package com.a.todo.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a.todo.design.CustomBoxCard
import com.a.todo.design.CustomButton
import com.a.todo.design.CustomIconButton
import com.a.todo.design.CustomSingleButtonGroup
import com.a.todo.design.CustomTextContent
import com.a.todo.design.CustomTextTitle
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun PagerToday(

) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        val buttonList = listOf("Todo", "Done")
        var buttonGroupState by rememberSaveable { mutableStateOf(buttonList[0]) }
        val pagerState = rememberPagerState(pageCount = { buttonList.size })
        CustomSingleButtonGroup(
            modifier = Modifier.fillMaxWidth(),
            buttonList = buttonList,
            value = buttonGroupState,
            onCheckedChange = {
                buttonGroupState = it
                scope.launch {
                    pagerState.animateScrollToPage(buttonList.indexOf(it))
                }
            }
        )
        LaunchedEffect(pagerState.targetPage) {
            buttonGroupState = buttonList[pagerState.targetPage]
        }
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) { pager ->
            when (pager) {
                0 -> PagerTodo()
                1 -> PagerDone()
            }
        }
    }
}

@Composable
private fun PagerTodo() {
    CustomBoxCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.width(5.dp).fillMaxHeight(1f).clip(RoundedCornerShape(50)).background(Color.Red)
        ) {}
        Column(
            modifier = Modifier.fillMaxWidth(0.70f).padding(start = 15.dp).align(Alignment.CenterStart),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CustomTextTitle(
                    modifier = Modifier,
                    text = "Todo Title"
                )
                CustomTextContent(
                    modifier = Modifier,
                    text = "YYYY/MM/DD"
                )
            }
            CustomTextContent(
                modifier = Modifier,
                text = "Todo Content"
            )
        }
        CustomButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = "Done",
            onClick = {}
        )
    }
}

@Composable
private fun PagerDone() {

}