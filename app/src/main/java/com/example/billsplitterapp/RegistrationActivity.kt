package com.example.billsplitterapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterScreen()
        }
    }
}


@Composable
fun RegisterScreen()
{

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.bg_main)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome",
                color = Color.White,
                style = TextStyle(fontSize = 52.sp, fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Create your Account",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 16.sp)
            )

        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = colorResource(R.color.tran_white),
                focusedContainerColor = colorResource(R.color.tran_white),
                unfocusedTextColor = Color.White,
            ),
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Enter FullName") },
            textStyle = TextStyle(color = colorResource(id = R.color.white)),
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.user),
                    contentDescription = ""

                )
            },
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = colorResource(R.color.tran_white),
                focusedContainerColor = colorResource(R.color.tran_white),
                unfocusedTextColor = Color.White,
            ),
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter Email") },
            textStyle = TextStyle(color = colorResource(id = R.color.white)),
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.email),
                    contentDescription = ""

                )
            },
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = colorResource(R.color.tran_white),
                focusedContainerColor = colorResource(R.color.tran_white),
                unfocusedTextColor = Color.White,
            ),
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Enter PhoneNumber") },
            textStyle = TextStyle(color = colorResource(id = R.color.white)),
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.phone),
                    contentDescription = ""

                )
            },
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = colorResource(R.color.tran_white),
                focusedContainerColor = colorResource(R.color.tran_white),
                unfocusedTextColor = Color.White,
            ),
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter Password") },
            textStyle = TextStyle(color = colorResource(id = R.color.white)),
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.password),
                    contentDescription = ""

                )
            },
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier
                .clickable {
                    when {
                        email.isEmpty() -> {
                            Toast.makeText(context, " Please Enter Mail", Toast.LENGTH_SHORT).show()
                        }
                        fullName.isEmpty() -> {
                            Toast.makeText(context, " Please Enter Name", Toast.LENGTH_SHORT).show()
                        }

                        phoneNumber.isEmpty() -> {
                            Toast.makeText(context, " Please Enter PhoneNumber", Toast.LENGTH_SHORT).show()
                        }
                        password.isEmpty() -> {
                            Toast.makeText(context, " Please Enter Password", Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            val userData = UserData(
                                fullName,
                                email,
                                phoneNumber,
                                password
                            )
                            registerUser(userData,context)
                        }

                    }
                }
                .width(350.dp)
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
            text = "Register",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {

            Text(
                modifier = Modifier,
                text = "Already have an Account?  ",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = colorResource(id = R.color.white),
                )
            )


            Text(
                modifier = Modifier
                    .clickable {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                        context.finish()
                    },
                text = "Login",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = colorResource(id = R.color.white),
                    fontWeight = FontWeight.Bold
                )
            )

        }

        Spacer(modifier = Modifier.height(46.dp))
    }
}


fun registerUser(userData: UserData, context: Context) {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("BillSplitterData")

    databaseReference.child(userData.emailid.replace(".", ","))
        .setValue(userData)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "You Registered Successfully", Toast.LENGTH_SHORT)
                    .show()

            } else {
                Toast.makeText(
                    context,
                    "Registration Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        .addOnFailureListener { _ ->
            Toast.makeText(
                context,
                "Something went wrong",
                Toast.LENGTH_SHORT
            ).show()
        }
}

data class UserData(
    var name : String = "",
    var emailid : String = "",
    var area : String = "",
    var password: String = ""
)