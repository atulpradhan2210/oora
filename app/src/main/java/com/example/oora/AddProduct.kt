package com.example.oora

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
class AddProduct : AppCompatActivity() {

    private lateinit var selectImageBtn: Button
    private lateinit var uploadImageBtn: Button
    private lateinit var imageView: ImageView

    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1


    //progress bar
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val appCheck = FirebaseAppCheck.getInstance()
        appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )


        selectImageBtn = findViewById(R.id.button3)
        uploadImageBtn = findViewById(R.id.button4)
        imageView = findViewById(R.id.imageView3)

        //initilizing progress bar
        progressBar = findViewById(R.id.progressBar)

// for taking image from galary and setting it to the imageview
        val getcontent = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback { Uri ->
                imageUri = Uri
                imageView.setImageURI(imageUri)
            }
        )

        selectImageBtn.setOnClickListener {
            getcontent.launch("image/*")
        }

        uploadImageBtn.setOnClickListener {
            startprogessbar()
            upload(imageUri)
            stoprogressbar()
        }




    }

    private fun upload(imageUri: Uri?) {

        val fileName = UUID.randomUUID().toString()                   // Generate unique filename
        val storageref =FirebaseStorage.getInstance("gs://oora-f2972.appspot.com").reference.child("productImage/$fileName")
        val  descriptionEditText = findViewById<EditText>(R.id.textView2)
        val pric = findViewById<EditText>(R.id.editTextNumber3)

        if (imageUri != null) {
            storageref.putFile(imageUri).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "image uploaded", Toast.LENGTH_SHORT).show()
                    storageref.downloadUrl.addOnSuccessListener { url ->
                        val imageurl =url.toString()
                        val disc =descriptionEditText.text.toString()
                        val price =pric.text.toString()
                        savedatatofirestoredatabase(imageurl,disc,price)
                    }
                }else{
                    Toast.makeText(this, "image not uploaded", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun savedatatofirestoredatabase(imageurl: String, disc: String, price: String) {
         val firestoredatabase = FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app").reference
         val productdata = hashMapOf(
             "imageurl" to imageurl,
             "discription" to disc,
             "price" to price
         )

        firestoredatabase.child("productdetails").child("${disc}").setValue(productdata).addOnSuccessListener {

                Toast.makeText(this, "Details uploaded on database", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{e ->
            Toast.makeText(this, "Failed to save data: ${e.message}", Toast.LENGTH_SHORT).show()
        }

    }



    private  fun startprogessbar(){
        progressBar.visibility  =View.VISIBLE
    }
    private  fun stoprogressbar(){
        progressBar.visibility =View.GONE
    }
}


