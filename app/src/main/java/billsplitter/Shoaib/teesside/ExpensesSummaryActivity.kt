package billsplitter.Shoaib.teesside

import android.app.Activity
import android.os.Bundle
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
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

class ExpensesSummaryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseSummaryScreen(
                currentUserEmail = BillSplitterData.readMail(this).replace(".", ",")
            )
        }
    }
}

@Composable
fun ExpenseSummaryScreen(currentUserEmail: String) {
    val billList = remember { mutableStateListOf<BillData>() }
    val context = LocalContext.current

    var totalOwe by remember { mutableDoubleStateOf(0.0) }
    var totalReceive by remember { mutableDoubleStateOf(0.0) }

    val typeTotals = remember {
        mutableStateMapOf(
            "Food" to 0.0,
            "Travel" to 0.0,
            "Fashion" to 0.0,
            "Entertainment" to 0.0,
            "Sports" to 0.0
        )
    }

    LaunchedEffect(Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("Bills")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                billList.clear()
                totalOwe = 0.0
                totalReceive = 0.0
                typeTotals.replaceAll { _, _ -> 0.0 }

                for (child in snapshot.children) {
                    val bill = child.getValue(BillData::class.java)
                    bill?.let {
                        billList.add(it)


                        val type = it.type
                        val userShare = it.splits[currentUserEmail] ?: 0.0

                        if (it.createdBy == currentUserEmail) {
                            // Calculate receive from others (exclude own share)
                            it.splits.forEach { (email, amount) ->
                                if (email != currentUserEmail) {
                                    totalReceive += amount
                                }
                            }
                        } else {
                            // User owes
                            totalOwe += userShare
                        }

                        if (type in typeTotals) {
                            typeTotals[type] =
                                typeTotals[type]!! + (userShare.takeIf { it > 0 } ?: 0.0)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(WindowInsets.systemBars.asPaddingValues())
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
                text = "Expenses Summary",
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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()


                ) {

                    Column(
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "₹${"%.2f".format(totalReceive)}",
                            color = Color(0xFF388E3C),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            "💰 Total To Receive",
                            color = Color(0xFF388E3C),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "₹${"%.2f".format(totalOwe)}",
                            color = Color(0xFF388E3C),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            "💸 Total To Pay",
                            color = Color(0xFF388E3C),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("📊 Expense Summary by Type", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))

            typeTotals.forEach { (type, total) ->
                if (total > 0.0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text("• $type", modifier = Modifier.weight(1f), fontSize = 16.sp)
                        Text("₹${"%.2f".format(total)}", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
