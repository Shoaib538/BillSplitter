package com.example.billsplitterapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateBillActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}


@Composable
fun CreateBill()
{

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.bg_main)),
//        horizontalAlignment = Alignment.CenterHorizontally
    ){



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(id = R.color.bt_color)
                ),
            verticalAlignment = Alignment.CenterVertically,


        ) {

            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "Create New Bill",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold

            )
//            Spacer(modifier = Modifier.weight(1f))
//
//            Image(
//                modifier = Modifier
//                    .size(32.dp),
//                painter = painterResource(id = R.drawable.profile_user),
//                contentDescription = "Create\nNew Bill"
//            )
//
//            Spacer(modifier = Modifier.width(12.dp))

        }


        Spacer(modifier = Modifier.height(24.dp))


        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            text = "Bill Title",
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleMedium.copy(
                color = colorResource(id = R.color.white),
            )
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = colorResource(R.color.tran_white),
                focusedContainerColor = colorResource(R.color.tran_white),
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter Bill Title") },
            textStyle = TextStyle(color = colorResource(id = R.color.white)),

        )

        Spacer(modifier = Modifier.height(12.dp))


        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            text = "Bill Type",
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleMedium.copy(
                color = colorResource(id = R.color.white),
            )
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter Bill Type") },
            textStyle = TextStyle(color = colorResource(id = R.color.white)),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = colorResource(R.color.tran_white),
                focusedContainerColor = colorResource(R.color.tran_white),
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            trailingIcon = {
                IconButton(onClick = {
                    // Your button logic here (e.g., open contacts picker)
                }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown, // Or use any other icon
                        contentDescription = "Select Contact",
                        tint = Color.White
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))


        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            text = "Amount",
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleMedium.copy(
                color = colorResource(id = R.color.white),
            )
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = colorResource(R.color.tran_white),
                focusedContainerColor = colorResource(R.color.tran_white),
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter Amount") },
            textStyle = TextStyle(color = colorResource(id = R.color.white)),

            )

        Spacer(modifier = Modifier.height(24.dp))


        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            text = "Spilt",
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleMedium.copy(
                color = colorResource(id = R.color.white),
            )
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            value = email,
            onValueChange = { email = it },
            label = { Text("Select Contacts") },
            textStyle = TextStyle(color = colorResource(id = R.color.white)),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = colorResource(R.color.tran_white),
                focusedContainerColor = colorResource(R.color.tran_white),
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            trailingIcon = {
                IconButton(onClick = {
                    // Your button logic here (e.g., open contacts picker)
                }) {
                    Icon(
                        imageVector = Icons.Default.Add, // Or use any other icon
                        contentDescription = "Select Contact",
                        tint = Color.White
                    )
                }
            }
        )

//        OutlinedTextField(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 12.dp),
//            colors = TextFieldDefaults.colors(
//                unfocusedContainerColor = colorResource(R.color.tran_white),
//                focusedContainerColor = colorResource(R.color.tran_white),
//                unfocusedTextColor = Color.White,
//            ),
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Select Contacts") },
//            textStyle = TextStyle(color = colorResource(id = R.color.white)),
//
//            )


        Spacer(modifier = Modifier.height(48.dp))


        Text(
            modifier = Modifier
                .clickable {

                }
                .width(160.dp)
                .background(
                    color = colorResource(id = R.color.bt_color),
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.bt_color),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(vertical = 12.dp)
                .align(Alignment.CenterHorizontally),
            text = "Create",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
            )
        )




    }
}

@Preview(showBackground = true)
@Composable
fun CreateBillPreview() {
    CreateBill()
}

