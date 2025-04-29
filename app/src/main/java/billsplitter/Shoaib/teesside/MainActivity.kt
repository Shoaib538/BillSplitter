package billsplitter.Shoaib.teesside

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.delay

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EntryScreenMA(this)
        }
    }
}

@Composable
fun EntryScreenMA(fragmentActivity: FragmentActivity) {
    val context = LocalContext.current as Activity

    LaunchedEffect(Unit) {
        delay(3000)
        val currentStatus = BillSplitterData.readLS(context)
        if (currentStatus) {
            val fingerPrintM = BiometricManager.from(fragmentActivity)
            if (fingerPrintM.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
                val executor = ContextCompat.getMainExecutor(fragmentActivity)
                val fingerPrintP =
                    BiometricPrompt(
                        fragmentActivity,
                        executor,
                        object : BiometricPrompt.AuthenticationCallback() {
                            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                super.onAuthenticationSucceeded(result)
                                context.startActivity(
                                    Intent(
                                        context,
                                        BillSplitterHome::class.java
                                    )
                                )

                                context.finish()

                            }

                            override fun onAuthenticationError(
                                errorCode: Int,
                                errString: CharSequence
                            ) {
                                super.onAuthenticationError(errorCode, errString)
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                                    .show()
                            }

                            override fun onAuthenticationFailed() {
                                super.onAuthenticationFailed()
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                                    .show()
                            }
                        })

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("FingerPrint Verification")
                    .setSubtitle("Place your finger to continue")
                    .setNegativeButtonText("Close")
                    .build()

                fingerPrintP.authenticate(promptInfo)
            } else {
                Toast.makeText(
                    fragmentActivity,
                    "No Finger Print Support for this device",
                    Toast.LENGTH_LONG
                ).show()
                context.startActivity(Intent(context, BillSplitterHome::class.java))
                context.finish()

            }
        } else {
            context.startActivity(Intent(context, LoginActivity::class.java))
            context.finish()
        }

    }

    EntryScreen()
}

@Composable
fun EntryScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.weight(1f))


            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.billsplitter_icon),
                contentDescription = "Bill Splitter",
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Welcome To",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Bill Splitter App\nby Shoaib",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))


        }
    }

}