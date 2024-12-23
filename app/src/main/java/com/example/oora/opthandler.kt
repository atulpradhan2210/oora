package com.example.oora

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class opthandler : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var verificationId:String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opthandler)
        auth = FirebaseAuth.getInstance()

        verificationId = intent.getStringExtra("verificationId")!!
        println("Verification ID: $verificationId")

        val buttonX = findViewById<Button>(R.id.button2)
        val otp = findViewById<EditText>(R.id.editTextNumber2)


        buttonX.setOnClickListener{
            val otpCode = otp.text.toString()
            if (otpCode.isNotEmpty()) {
                verifyCode(otpCode)
            } else {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    // Sign in with the verified credential
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {val sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
                    sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()


                    // Sign in successful, you can move to the next activity
                    Toast.makeText(this, "Authentication successful", Toast.LENGTH_SHORT).show()
                    // Navigate to the next screen

                    val intent = Intent(this, Mainpage::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Handle failure
                    Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                }
            }
    }
}