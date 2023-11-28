package com.example.savannahwonders.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.savannahwonders.ui.viewmodels.DestinationScreenViewModel
import kotlin.math.absoluteValue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DestinatioinScreen(
    navHostController: NavHostController,
    destinationScreenViewModel: DestinationScreenViewModel
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Favoirtes",
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )

                },
                colors = TopAppBarDefaults.smallTopAppBarColors(),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navHostController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Menu Icon",
                            modifier = Modifier
                                .size(27.dp)
                        )
                    }

                },
                modifier = Modifier
                    .height(40.dp)
            )
        }
    ) {
        val scrollState = rememberScrollState()
        val uiState = destinationScreenViewModel.destinationScreenUiState.collectAsState()
        var isFavorite by rememberSaveable {
            mutableStateOf(false)
        }
        var favoriteIcon by remember{
            mutableStateOf(Icons.Outlined.FavoriteBorder)
        }
        if(isFavorite){
            favoriteIcon = Icons.Filled.Favorite
        }else{
            favoriteIcon = Icons.Outlined.FavoriteBorder
        }
        Column(
            modifier = Modifier
                .padding(top = 50.dp, bottom = 50.dp)
                .verticalScroll(scrollState)
        ) {
            var destinationImages by rememberSaveable {
                mutableStateOf(uiState.value.images!!)
            }
            var pagerState = rememberPagerState(
                pageCount = {
                    destinationImages.size
                }
            )
            val fling = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(10)
            )

            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fill,
                contentPadding = PaddingValues(12.dp),
                pageSpacing = 14.dp,
                flingBehavior = fling,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
            ) { page ->
                Surface(
                    shadowElevation = 5.dp,
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .height(220.dp)
                        .graphicsLayer {
                            val pageOffset = (
                                    (pagerState.currentPage - page) + pagerState
                                        .currentPageOffsetFraction
                                    ).absoluteValue
                            alpha = 0.5f + 0.5f * (1f - pageOffset.coerceIn(0f, 1f))
                        }
                ) {
                    if(destinationImages.isNotEmpty()) {
                        AsyncImage(
                            model = destinationImages[page],
                            contentDescription = "Images",
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
            if (pagerState.pageCount > 1) {
                Row(
                    Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color = if (isSystemInDarkTheme()) {
                            if (pagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
                        } else {
                            if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                        }
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)

                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                uiState.value.name?.let { it1 ->
                    Text(
                        text = it1,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(2f)
                    )
                }
                IconButton(
                    onClick = {
                        isFavorite = !isFavorite
                    }
                ) {
                    Icon(
                        imageVector = favoriteIcon,
                        contentDescription = "Favorites",
                    )
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(imageVector = Icons.Rounded.Star, contentDescription = "Rating")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Rating: "
                    )
                    Text(
                        text = "${uiState.value.rating}",
                    )
                }

            }
            Spacer(modifier = Modifier.height(25.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Description",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
                Text(text = uiState.value.description.toString())
            }
            Spacer(modifier = Modifier.height(25.dp))
            Text(text = "View In Map")
        }
    }
}