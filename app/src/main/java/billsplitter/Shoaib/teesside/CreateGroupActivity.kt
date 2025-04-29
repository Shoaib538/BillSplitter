package billsplitter.Shoaib.teesside

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class CreateGroupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateGroupScreen()
        }
    }
}

@Composable
fun CreateGroupScreen() {
    var groupName by remember { mutableStateOf("") }
    var memberEmail by remember { mutableStateOf("") }
    var allUsers by remember { mutableStateOf(listOf<String>()) }
    val memberList = remember { mutableStateListOf<String>() }

    val context = LocalContext.current

    // Fetch matching users
    LaunchedEffect(memberEmail) {
        if (memberEmail.length >= 2) {
            fetchMatchingUsers(memberEmail) { users ->
                allUsers = users.filterNot { it in memberList }
            }
        } else {
            allUsers = emptyList()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(WindowInsets.systemBars.asPaddingValues())) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(id = R.color.bt_color)
                )
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        (context as Activity).finish()
                    },
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Back"
            )

            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "Create Group",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold

            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {


            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Group Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {


                OutlinedTextField(
                    value = memberEmail,
                    onValueChange = { memberEmail = it },
                    label = { Text("Add Member Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Suggestion dropdown
                DropdownMenu(
                    expanded = allUsers.isNotEmpty(),
                    onDismissRequest = { allUsers = emptyList() }
                ) {
                    allUsers.forEach { email ->
                        DropdownMenuItem(
                            text = { Text(email) },
                            onClick = {
                                memberList.add(email)
                                memberEmail = ""
                                allUsers = emptyList()
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Text("Members:", fontWeight = FontWeight.Bold)

            // Display added members with remove icon
            memberList.forEach { member ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = member, modifier = Modifier.weight(1f))
                    IconButton(onClick = { memberList.remove(member) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove Member")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    memberList.add(BillSplitterData.readMail(context))
                    createGroup(context, groupName, memberList)
                },
                enabled = groupName.isNotBlank() && memberList.isNotEmpty()
            ) {
                Text("Create Group")
            }
        }
    }
}


data class GroupData(
    var groupId: String = "",
    var groupName: String = "",
    var members: Map<String, Boolean> = emptyMap()
)

fun fetchMatchingUsers(query: String, onResult: (List<String>) -> Unit) {
    val ref = FirebaseDatabase.getInstance().getReference("BillSplitterData")
    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val matches = mutableListOf<String>()
            for (child in snapshot.children) {
                val user = child.getValue(UserData::class.java)
                user?.let {
                    if (it.emailid.contains(query, ignoreCase = true)) {
                        matches.add(it.emailid)
                    }
                }
            }
            onResult(matches)
        }

        override fun onCancelled(error: DatabaseError) {
            onResult(emptyList())
        }
    })
}


fun createGroup(context: Context, groupName: String, memberEmails: List<String>) {
    val database = FirebaseDatabase.getInstance().getReference("BillSplitterGroups")
    val groupId = UUID.randomUUID().toString()
    val membersMap = memberEmails.associateBy({ it.replace(".", ",") }, { true })

    val group = GroupData(groupId, groupName, membersMap)

    database.child(groupId).setValue(group)
        .addOnSuccessListener {
            Toast.makeText(context, "Group created successfully!", Toast.LENGTH_SHORT).show()

            (context as Activity).finish()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to create group.", Toast.LENGTH_SHORT).show()
        }
}
