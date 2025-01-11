package com.howtokaise.jetnote.Navigation

import InsertScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.howtokaise.jetnote.NoteScreen
import com.howtokaise.jetnote.SplashScreen

@Composable
fun Navigate(navHostController: NavHostController) {
    NavHost(
        navController = navHostController, startDestination = "splash"
    ) {
        composable(NotesNavigationItem.SplashScreen.route){
            SplashScreen(navHostController)
        }

        composable(NotesNavigationItem.HomeScreen.route) {
            NoteScreen(navHostController)
        }

        composable(NotesNavigationItem.InsertNotesScreen.route+"/{id}") {
            val id = it.arguments?.getString("id")
            InsertScreen(navHostController, id)
        }
    }
}