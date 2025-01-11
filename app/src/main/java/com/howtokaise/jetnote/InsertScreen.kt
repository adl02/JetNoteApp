import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.howtokaise.jetnote.Models.Notes

@Composable
fun InsertScreen(navHostController: NavHostController, id: String?) {

    val context = LocalContext.current

    val detabase = FirebaseFirestore.getInstance()
    val notesDB = detabase.collection("notes")

    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val isSaving = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (id != "defaultId") {
            notesDB.document(id.toString()).get().addOnSuccessListener {
                val singleData = it.toObject(Notes::class.java)
                title.value = singleData!!.title
                description.value = singleData.description
            }
        }
    }

    val saveNote = {
        if (!isSaving.value && (title.value.isNotEmpty() || description.value.isNotEmpty())) {
            isSaving.value = true
            val myNotesId = if (id != "defaultId") id.toString() else notesDB.document().id
            val notes = Notes(
                id = myNotesId,
                title = title.value,
                description = description.value
            )
            notesDB.document(myNotesId).set(notes).addOnCompleteListener {
                isSaving.value = false

            }
        }
    }
    BackHandler {
        if (!isSaving.value){
            saveNote()
            navHostController.popBackStack()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!isSaving.value){
                        if (title.value.isEmpty() && description.value.isEmpty()) {
                            Toast.makeText(context, "Enter valid data", Toast.LENGTH_SHORT).show()
                        } else {
                            saveNote()
                            navHostController.popBackStack()
                        }
                    }

                },
                contentColor = Color.White,
                containerColor = Color(0xFFFFC107), shape = CircleShape,

            ) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null, modifier = Modifier.size(35.dp))
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                // .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .background(Color(0xFFe7e1eb))
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .size(30.dp)
                            .clickable {
                                if (!isSaving.value) {
                                    saveNote()
                                    navHostController.popBackStack()
                                }

                            }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Today's Plan.",
                        color = Color.Blue,
                        fontSize = 30.sp,
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                    Spacer(modifier = Modifier.weight(2f))
                }

                Divider(
                    color = Color.Blue,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    label = {
                        Text(text = "Title")
                    }, singleLine = true,
                    value = title.value,
                    onValueChange = {
                        title.value = it
                    }, modifier = Modifier
                        .fillMaxWidth()
                )

                TextField(colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                    label = {
                        Text(text = "Description")
                    },
                    value = description.value,
                    onValueChange = {
                        description.value = it
                    }, modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
