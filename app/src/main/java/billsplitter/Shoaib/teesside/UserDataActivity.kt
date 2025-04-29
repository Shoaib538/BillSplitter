package billsplitter.Shoaib.teesside

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class UserDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UserDataDesign()
        }
    }
}

@Composable
fun UserDataDesign() {

    val context = LocalContext.current

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
                text = "User Details",
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                Image(
                    bitmap = decodeBase64ToBitmap(BillSplitterData.readPhoto(context))!!.asImageBitmap(),
                    contentDescription = "User Photo",
                    modifier = Modifier
                        .width(100.dp)
                        .height(150.dp)
                        .padding(end = 8.dp)
                        .clip(RectangleShape)
                        .border(2.dp, Color.Black, RectangleShape),
                    contentScale = ContentScale.Crop
                )

                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "User Name : ${BillSplitterData.readUserName(context)}",
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "User EmailId : ${BillSplitterData.readMail(context)}",
                        color = Color.Black
                    )

                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            AboutUs()
        }
    }
}

@Composable
fun AboutUs() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE0F7FA), Color(0xFF80DEEA))
                )
            )
            .padding(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Contact Us", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Text("Name: Shoaib")
                Text("Email: ushoaib638@gmail.com")

            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("About Us", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Text("Welcome to the Bill Splitter App – making group expenses easy and hassle-free!\n" +
                        "Created by Shoaib, this app is designed to take the confusion out of splitting bills with friends, family, or colleagues. Whether you're dining out, traveling together, or just sharing household expenses, Bill Splitter helps you divide the costs quickly and accurately.\n" +
                        "Thank you for choosing us to take the math out of managing your expenses!\n")
            }
        }
    }
}


fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    val decodedString = Base64.decode(base64String, Base64.DEFAULT)
    val originalBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    return originalBitmap
}