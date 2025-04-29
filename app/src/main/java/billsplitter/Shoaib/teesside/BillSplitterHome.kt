package billsplitter.Shoaib.teesside

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class BillSplitterHome : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BillSplitterHomeActivityScreen()
        }
    }
}


@Composable
fun BillSplitterHomeActivityScreen() {
    val context = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = colorResource(id = R.color.bg_main),
            )
            .padding(WindowInsets.systemBars.asPaddingValues()),
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
                    .size(32.dp),
                painter = painterResource(id = R.drawable.billsplitter_icon),
                contentDescription = "Icon"
            )

            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "Bill Splitter App",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold

            )
            Spacer(modifier = Modifier.weight(1f))

            Image(
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        BillSplitterData.writeLS(context, false)
                        context.startActivity(
                            Intent(
                                context,
                                LoginActivity::class.java
                            )
                        )
                        context.finish()
                    },
                painter = painterResource(id = R.drawable.iv_logout),
                contentDescription = "Create\nNew Bill"
            )

            Spacer(modifier = Modifier.width(12.dp))

        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .padding(horizontal = 30.dp, vertical = 6.dp)
                        .background(
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 12.dp)
                        .clickable {
                            context.startActivity(
                                Intent(
                                    context,
                                    CreateBillActivity::class.java
                                )
                            )

                        }


                ) {
                    Image(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.new_bill),
                        contentDescription = "Create\nNew Bill"
                    )

                    Spacer(modifier = Modifier.height(10.dp))


                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = "Create\nNew Bill",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .padding(horizontal = 30.dp, vertical = 6.dp)
                        .background(
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 12.dp)
                        .clickable {
                            context.startActivity(
                                Intent(
                                    context,
                                    ManageGroupActivity::class.java
                                )
                            )

                        }

                ) {
                    Image(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.add_bill),
                        contentDescription = "Manage\nGroup"
                    )

                    Spacer(modifier = Modifier.height(10.dp))


                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = "Manage\nGroup",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .padding(horizontal = 30.dp, vertical = 6.dp)
                        .background(
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 12.dp)
                        .clickable {
                            context.startActivity(
                                Intent(
                                    context,
                                    SeePendingBillsActivity::class.java
                                )
                            )

                        }


                ) {
                    Image(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.manage_bill),
                        contentDescription = "Manage\nBill"
                    )

                    Spacer(modifier = Modifier.height(10.dp))


                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = "See\nBills",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .padding(horizontal = 30.dp, vertical = 6.dp)
                        .background(
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable {
                            context.startActivity(
                                Intent(
                                    context,
                                    DeleteBillActivity::class.java
                                )
                            )

                        }
                        .padding(horizontal = 6.dp, vertical = 12.dp)


                ) {
                    Image(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.delete_bill),
                        contentDescription = "Delete\nBill"
                    )

                    Spacer(modifier = Modifier.height(10.dp))


                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = "Delete\nBill",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .padding(horizontal = 30.dp, vertical = 6.dp)
                        .background(
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 12.dp)
                        .clickable {
                            context.startActivity(
                                Intent(
                                    context,
                                    ExpensesSummaryActivity::class.java
                                )
                            )

                        }


                ) {
                    Image(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.summary_bill),
                        contentDescription = "View\nSummary"
                    )

                    Spacer(modifier = Modifier.height(10.dp))


                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = "View\nSummary",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .padding(horizontal = 30.dp, vertical = 6.dp)
                        .background(
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable {
                            context.startActivity(
                                Intent(
                                    context,
                                    UserDataActivity::class.java
                                )
                            )

                        }
                        .padding(horizontal = 6.dp, vertical = 12.dp)


                ) {
                    Image(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.access_profile),
                        contentDescription = "Access\nProfile"
                    )

                    Spacer(modifier = Modifier.height(10.dp))


                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = "Access\nProfile",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

            }

            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BillSplitterHomeActivityScreen()
}
