package billsplitter.Shoaib.teesside

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SeePendingBillsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BillListScreen(currentUserEmail = BillSplitterData.readMail(this))
        }
    }
}

@Composable
fun BillListScreen(currentUserEmail: String) {
    val billList = remember { mutableStateListOf<Pair<String, BillData>>() }
    val context = LocalContext.current

    val userEmail = currentUserEmail.replace(".", ",")

    LaunchedEffect(Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("Bills")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                billList.clear()
                for (child in snapshot.children) {
                    val bill = child.getValue(BillData::class.java)
                    val key = child.key
                    if (bill != null && key != null) {
                        Log.e("Test", "${bill.createdBy}")
                        // Show only bills where user is creator or part of split
                        if (bill.createdBy == userEmail || bill.splits.containsKey(userEmail)) {
                            billList.add(key to bill)
                        }
                    }
                }

                Log.e("Test", "Total Bills Found ${billList.size}")
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(WindowInsets.systemBars.asPaddingValues())
    ) {

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
                text = "Pending Bills",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold

            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {

            if (billList.isNotEmpty()) {
                LazyColumn(modifier = Modifier.padding(12.dp)) {
                    items(billList) { (billKey, bill) ->
                        val isCreator = bill.createdBy == currentUserEmail.replace(".", ",")

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("💸 ${bill.title} - ${bill.type}", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Group: ${bill.groupName}")
                                Spacer(modifier = Modifier.height(6.dp))

                                Text("Total: ₹${bill.amount}")
                                Spacer(modifier = Modifier.height(4.dp))

                                Text("Created By: ${bill.createdBy.replace(",", ".")}")
                                Spacer(modifier = Modifier.height(8.dp))

                                if (isCreator) {
                                    bill.splits.forEach { (email, due) ->
                                        if (email != currentUserEmail.replace(".", ","))
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 2.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {

                                                    Image(
                                                        modifier = Modifier.size(12.dp),
                                                        painter = painterResource(id = R.drawable.iv_groupmember),
                                                        contentDescription = "Group Member"
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        email.replace(
                                                            ",",
                                                            "."
                                                        )
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(8.dp))

                                                Row(
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    if (due <= 0.0) {
                                                        Text(
                                                            "Status : Settled",
                                                            color = Color(0xFF4CAF50),
                                                        )
                                                    } else {
                                                        Text(
                                                            text = "Status : owes ₹${
                                                                "%.2f".format(
                                                                    due
                                                                )
                                                            }"
                                                        )
                                                    }

                                                    Spacer(modifier = Modifier.width(24.dp))

                                                    if (email != currentUserEmail && due > 0.0) {
                                                        Text(
                                                            text = "Settle",
                                                            color = Color.White,
                                                            modifier = Modifier
                                                                .clickable {
                                                                    settleDue(
                                                                        billKey,
                                                                        email,
                                                                        context
                                                                    )
                                                                }
                                                                .background(
                                                                    color = colorResource(id = R.color.black),
                                                                    shape = RoundedCornerShape(
                                                                        6.dp
                                                                    )
                                                                )
                                                                .border(
                                                                    width = 1.dp,
                                                                    color = colorResource(id = R.color.black),
                                                                    shape = RoundedCornerShape(6.dp)
                                                                )
                                                                .padding(
                                                                    horizontal = 12.dp,
                                                                    vertical = 4.dp
                                                                )
                                                        )
                                                    }
                                                }

//                                            if (email != currentUserEmail && due > 0.0) {
//                                                Button(
//                                                    onClick = {
//                                                        settleDue(billKey, email, context)
//                                                    },
//                                                    modifier = Modifier.height(32.dp)
//                                                ) {
//                                                    Text("Settle", fontSize = 12.sp)
//                                                }
//                                            } else if (due <= 0.0) {
//                                                Text(
//                                                    "Settled",
//                                                    color = Color(0xFF4CAF50),
//                                                    fontSize = 12.sp
//                                                )
//                                            }
                                            }
                                    }
                                } else {
                                    val userMail = currentUserEmail.replace(".", ",")
                                    val userDue = bill.splits[userMail] ?: 0.0
                                    Text(
                                        "You Owe: ₹${"%.2f".format(userDue)}",
                                        color = if (userDue > 0.0) Color.Red else Color(0xFF4CAF50),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "No Pending Bills",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


fun settleDue(billKey: String, userEmail: String, context: Context) {
    val sanitizedEmail = userEmail.replace(".", ",")
    val ref = FirebaseDatabase.getInstance()
        .getReference("Bills")
        .child(billKey)
        .child("splits")
        .child(sanitizedEmail)

    ref.setValue(0.0)
        .addOnSuccessListener {
            Toast.makeText(context, "Amount Settled", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to settle", Toast.LENGTH_SHORT).show()
        }
}



