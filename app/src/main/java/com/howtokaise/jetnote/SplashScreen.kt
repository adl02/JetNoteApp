package com.howtokaise.jetnote

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.howtokaise.jetnote.Navigation.NotesNavigationItem
import com.howtokaise.jetnote.ui.theme.colorBlack
import kotlinx.coroutines.delay



@Composable
fun SplashScreen(navHostController: NavHostController) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF000000) )
        ){
            Column (modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(painterResource(id = R.drawable.download2),
                    contentDescription = "slap",
                    modifier = Modifier
                        .size(350.dp),
                )

                Text(text = "Let's make our Plans...", color = Color.White, fontSize = 20.sp)
            }
            Text(text = "    DESIGN BY \n How To Kaise", color = Color.White, fontSize = 16.sp,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp))
            
        }
    LaunchedEffect(Unit) {
        delay(1500)
        navHostController.navigate(NotesNavigationItem.HomeScreen.route){
            popUpTo(NotesNavigationItem.SplashScreen.route){
                inclusive = true
            }
        }
    }
}
