package com.a.todo.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.a.todo.design.CustomSingleButtonGroup
import kotlinx.coroutines.launch

@Composable
fun PagerAll(

) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        val buttonList = listOf("Accomplish", "Expired")
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
            state = pagerState
        ) { pager ->
            when (pager) {
                0 -> Text(text = "Accomplish")
                1 -> Text(text = "Expired")
            }
        }
    }
}