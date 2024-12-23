package com.example.oora

import android.content.Intent
import android.os.Build
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



class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
        }



        // Check if the user is already logged in
        val sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val isUserLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
//        Toast.makeText(this, "User Logged In: $isUserLoggedIn", Toast.LENGTH_SHORT).show()
        if (isUserLoggedIn) {
            // If user is already authenticated, skip login and go to MainActivity
            val intent =Intent(this@Login,Mainpage::class.java)
            startActivity(intent)
            finish()
        }


        setContentView(R.layout.activity_login)

        val number = findViewById<EditText>(R.id.editTextNumber)
        val sendotpbutton =findViewById<Button>(R.id.button)

        //initlize firebase auth

        auth =FirebaseAuth.getInstance()

        sendotpbutton.setOnClickListener {
            val phonenumber ="+91" + number.text.toString()
            Toast.makeText(this,"${phonenumber}",Toast.LENGTH_SHORT).show()
            if (phonenumber.isNotEmpty()){
                sendVerificationCode(phonenumber)

            }else{
                Toast.makeText(this, "please enter your mobile number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendVerificationCode(phonenumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phonenumber)           // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS)     // Timeout and unit
            .setActivity(this)                     // Activity (for callback binding)
            .setCallbacks(verificationCallbacks)   // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // Callback to handle verification events
    private val verificationCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
            // Auto-verification completed

        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Handle error (e.g., invalid phone number)
            Toast.makeText(this@Login, "Verification failed: ${e.message}", Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // Save the verification ID and resending token so we can use them later
            this@Login.verificationId = verificationId
            resendToken = token

            // Notify the user that the code has been sent (optional)

            Toast.makeText(this@Login, "Code sent", Toast.LENGTH_SHORT).show()

            // Start OtpHandlerActivity and pass verificationId
            val intent = Intent(this@Login, opthandler::class.java)
            intent.putExtra("verificationId", verificationId)

            startActivity(intent)

            // Proceed to ask for the OTP and verify it
        }
    }

}

