package billsplitter.Shoaib.teesside

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateBillActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddBillScreen()
        }
    }
}

@Composable
fun AddBillScreen() {
    val context = LocalContext.current
    val groupList = remember { mutableStateListOf<GroupData>() }
    var selectedGroup by remember { mutableStateOf<GroupData?>(null) }

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val selectedMembers = remember { mutableStateListOf<String>() }

    var selectedBillType by remember { mutableStateOf("Food") }


    // Fetch groups from Firebase
    LaunchedEffect(Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("BillSplitterGroups")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupList.clear()
                for (child in snapshot.children) {
                    val group = child.getValue(GroupData::class.java)
                    group?.let { groupList.add(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Column(modifier = Modifier.padding(WindowInsets.systemBars.asPaddingValues())) {

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
                text = "Create Bill",
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
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = { Text("Bill Title") })
            Spacer(Modifier.height(8.dp))

            DropDownBillType(
                selectedType = selectedBillType,
                onTypeSelected = { selectedBillType = it })


            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.height(8.dp))

            DropdownMenuBox(
                items = groupList.map { it.groupName },
                onItemSelected = {
                    selectedGroup = groupList[it]
                    selectedMembers.clear()
                })


            Spacer(Modifier.height(10.dp))


            selectedGroup?.let { group ->
                Spacer(Modifier.height(8.dp))
                Text("Select Members:")
                group.members.keys.forEach { emailKey ->
                    val cleanEmail = emailKey.replace(".", ",")
                    val isSelected = cleanEmail in selectedMembers
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isSelected, onCheckedChange = {
                            if (it) selectedMembers.add(cleanEmail) else selectedMembers.remove(
                                cleanEmail
                            )
                        })
                        Text(cleanEmail)
                    }
                }

                Spacer(Modifier.height(16.dp))
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        val amt = amount.toDoubleOrNull() ?: 0.0

                        val accountMail = BillSplitterData.readMail(context)
                        selectedMembers.remove(
                            accountMail.replace(
                                ".",
                                ","
                            )
                        ) // remove this and +1 to size

                        val perPerson =
                            if (selectedMembers.isNotEmpty()) amt / (selectedMembers.size + 1) else 0.0
                        val splits = selectedMembers.associateWith { perPerson }

                        val cleanEmail = accountMail.replace(".", ",")

                        val billData = BillData(
                            title, selectedBillType,
                            amt.toString(), group.groupName, splits, cleanEmail
                        )

                        try {
                            FirebaseDatabase.getInstance().getReference("Bills").push()
                                .setValue(billData)
                                .addOnCompleteListener {
                                    Toast.makeText(context, "Bill Added", Toast.LENGTH_SHORT).show()
                                    (context as Activity).finish()
                                    title = ""
                                    selectedBillType = ""
                                    amount = ""
                                    selectedGroup = null
                                    selectedMembers.clear()
                                }
                        } catch (e: Exception) {
                            Log.e("Test", "Error : ${e.message}")
                        }
                    }
                ) {
                    Text("Add Bill")
                }
            }
        }
    }
}


data class BillData(
    val title: String = "",
    val type: String = "",
    val amount: String = "",
    val groupName: String = "",
    val splits: Map<String, Double> = emptyMap(),
    val createdBy: String = ""
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownBillType(selectedType: String, onTypeSelected: (String) -> Unit) {

    val types = listOf(
        "Food",
        "Travel",
        "Fashion",
        "Entertainment",
        "Sports",
        "Health",
        "Education",
        "Gifts",
        "Shopping",
        "Groceries",
        "Utilities",
        "Insurance",
        "Rent",
        "Transportation",
        "Subscriptions",
        "Investment",
        "Personal Care",
        "Charity",
        "Pet Care",
        "Miscellaneous"
    )

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedType,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Bill Type") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor() // Important for anchoring the dropdown
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DropdownMenuBox(items: List<String>, onItemSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    Box {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            readOnly = true,
            label = { Text("Select") },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, null, Modifier.clickable { expanded = true })
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEachIndexed { index, label ->
                DropdownMenuItem(text = { Text(label) }, onClick = {
                    selectedText = label
                    expanded = false
                    onItemSelected(index)
                })
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CreateBillPreview() {
}

