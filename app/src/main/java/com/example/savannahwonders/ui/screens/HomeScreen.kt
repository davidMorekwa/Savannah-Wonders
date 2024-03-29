package com.example.savannahwonders.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.savannahwonders.data.model.DestinationModel
import com.example.savannahwonders.ui.activities.AuthActivity
import com.example.savannahwonders.ui.activities.menuItems
import com.example.savannahwonders.ui.navigation.NavGraphDestinations
import com.example.savannahwonders.ui.viewmodels.AuthViewModel
import com.example.savannahwonders.ui.viewmodels.DestinationScreenViewModel
import com.example.savannahwonders.ui.viewmodels.HomeScreenViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    authViewModel: AuthViewModel,
    homeScreenViewModel: HomeScreenViewModel,
    destinationScreenViewModel: DestinationScreenViewModel,
    navHostController: NavHostController,
) {
    var uiState = homeScreenViewModel.homeScreenUiState.collectAsState()
    val selectedItem = remember { mutableStateOf(menuItems[0].id) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Home",
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )

                },
                colors = TopAppBarDefaults.smallTopAppBarColors(),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
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
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {

                ModalDrawerSheet(
                    modifier = Modifier
                        .width(258.dp)
                ) {
                    Spacer(Modifier.height(52.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Icon",
                            modifier = Modifier
                                .size(72.dp)
                        )
                        Firebase.auth.currentUser?.email?.let { it1 ->
                            Text(
                                text = it1,
                                fontWeight = FontWeight.Light,
                                fontSize = 13.sp
                            )
                        }
                    }
                    Spacer(Modifier.height(52.dp))
                    menuItems.forEach { item ->
                        NavigationDrawerItem(
                            icon = { Icon(item.icon, contentDescription = item.description) },
                            label = { Text(item.title) },
                            selected = item.id == selectedItem.value,
                            onClick = {
                                selectedItem.value = item.id
                                scope.launch {
                                    drawerState.close()
                                    if (item.id == 4) {
                                        authViewModel.logOut()
                                        context.startActivity(Intent(context, AuthActivity::class.java))
                                    }
                                    if (item.id == 3){
                                        navHostController.navigate(NavGraphDestinations.FAVORITES.name)
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .padding(top = 50.dp, bottom=50.dp)
                ) {
                    Box {
                        if (uiState.value.isEmpty()){
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                CircularProgressIndicator()
                                Text(text = "Loading...")
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(uiState.value) { item: DestinationModel ->
                                    Surface(
                                        tonalElevation = 2.dp,
                                        shape = RoundedCornerShape(20.dp),
                                        shadowElevation = 10.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                            .clickable {
                                                destinationScreenViewModel.selectDestination(item)
                                                navHostController.navigate(NavGraphDestinations.DESTINATION.name)
                                            }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            AsyncImage(
                                                model = item.mainImage?.let { it },
                                                contentDescription = "Image",
                                                modifier = Modifier
                                                    .size(180.dp),
                                                contentScale = ContentScale.FillBounds
                                            )
                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                            ) {
                                                item.name?.let { it1 ->
                                                    Text(
                                                        text = it1,
                                                        fontSize = 20.sp,
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(16.dp))
                                                item.description?.let { it1 -> Text(text = it1) }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}