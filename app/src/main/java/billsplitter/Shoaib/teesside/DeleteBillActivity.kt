package billsplitter.Shoaib.teesside

import android.app.Activity
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

class DeleteBillActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettledBillListScreen(
                currentUserEmail = BillSplitterData.readMail(this).replace(".", ",")
            )
        }
    }
}

@Composable
fun SettledBillListScreen(currentUserEmail: String) {
    val billList = remember { mutableStateListOf<Pair<String, BillData>>() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("Bills")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                billList.clear()
                for (child in snapshot.children) {
                    val bill = child.getValue(BillData::class.java)
                    val key = child.key

                    if (bill != null && key != null &&
                        bill.createdBy == currentUserEmail &&
                        bill.splits.values.all { it == 0.0 }
                    ) {
                        billList.add(key to bill)
                    }
                }
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
                text = "Settled Bills",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold

            )
        }

        Column(modifier = Modifier.fillMaxSize()) {


            if (billList.isNotEmpty()) {
                LazyColumn(modifier = Modifier.padding(12.dp)) {
                    items(billList) { (billKey, bill) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "💸 ${bill.title} - ${bill.type}",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text("Group: ${bill.groupName}")
                                    Text("Total: ₹${bill.amount}")
                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        "✅ All balances settled",
                                        color = Color(0xFF4CAF50),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(Modifier.height(8.dp))
                                }

                                Button(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    onClick = {
                                        FirebaseDatabase.getInstance()
                                            .getReference("Bills")
                                            .child(billKey)
                                            .removeValue()
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    context,
                                                    "Bill deleted",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                                billList.remove(billKey to bill)
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(
                                                    context,
                                                    "Failed to delete bill",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                ) {
                                    Text("Delete Bill", color = Color.White)
                                }
                            }
                        }
                    }

                }
            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "No Settled Bills To Delete",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color.Gray
                )


            }

        }
    }
}
