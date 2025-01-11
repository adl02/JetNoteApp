package com.howtokaise.jetnote

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.howtokaise.jetnote.Models.Notes
import com.howtokaise.jetnote.Navigation.NotesNavigationItem
import com.howtokaise.jetnote.ui.theme.colorBlack
import com.howtokaise.jetnote.ui.theme.colorGray


@Composable
fun NoteScreen(navHostController: NavHostController) {

    val detabase = FirebaseFirestore.getInstance()
    val notesDB = detabase.collection("notes")
    val notesList = remember { mutableStateListOf<Notes>() }
    val dataValue = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        notesDB.addSnapshotListener { value, error ->
            if (error == null) {
                val data = value?.toObjects(Notes::class.java)
                notesList.clear()
                notesList.addAll(data!!)
                dataValue.value = true
            } else {
                dataValue.value = false
            }
        }
    }



    Scaffold(floatingActionButton = {
        FloatingActionButton(contentColor = Color.White,
            containerColor = Color(0xFFFFC107),
            shape = CircleShape,
            onClick = {
                navHostController.navigate(NotesNavigationItem.InsertNotesScreen.route + "/defaultId")
            }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "icon",
                modifier = Modifier.size(40.dp)
            )
        }
    }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                // .padding(innerPadding)
                .background(color = colorBlack)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp)

            ) {
                Text(
                    text = "Welcome",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 35.dp, start = 15.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                if (dataValue.value) {

                    LazyColumn {
                        items(notesList) { notes ->
                            ListItems(notes, notesDB, navHostController)

                        }
                    }

                } else {

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(25.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ListItems(notes: Notes, notesDB: CollectionReference, navHostController: NavHostController) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clip(shape = RoundedCornerShape(corner = CornerSize(15.dp)))
        .size(80.dp)
        .background(color = colorGray)
        .clickable {
            navHostController.navigate(NotesNavigationItem.InsertNotesScreen.route + "/${notes.id}")
        }) {
        if (showDialog){
            androidx.compose.material3.AlertDialog(
                onDismissRequest = {showDialog = false},
                confirmButton = {
                    TextButton(
                        onClick = {
                            notesDB.document(notes.id).delete()
                            showDialog = false
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("No")
                    }
                },
                text = {
                    Text("Do you want to Delete this note ? ")
                }
            )
        }
        Icon(imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .padding(end = 10.dp)
                .align(Alignment.CenterEnd)
                .clickable {
                    showDialog = true
                })
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
        ) {

            Text(
                text = notes.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(9.dp))
            Text(
                text = notes.description, color = Color.White,

                )
        }

    }
}