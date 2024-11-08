package code.fortomorrow.happyshopping.view

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import code.fortomorrow.happyshopping.R
import code.fortomorrow.happyshopping.prevalent.Prevalent
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class SettinsActivity : AppCompatActivity() {
    private var profileImageView: CircleImageView? = null
    private var fullNameEditText: EditText? = null
    private lateinit var userPhoneEditText: EditText
    private var addressEditText: EditText? = null
    private var profileChangeTextBtn: TextView? = null
    private lateinit var closeTextBtn: TextView
    private lateinit var saveTextButton: TextView
    private val securityQuestionBtn: Button? = null

    private var imageUri: Uri? = null
    private var myUrl = ""
    private var uploadTask: StorageTask<*>? = null
    private var storageProfilePictureRef: StorageReference? = null
    private var checker = ""

    // Define an image picker launcher
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            cropImageLauncher.launch(
                CropImageContractOptions(
                    uri = it,
                    cropImageOptions = CropImageOptions(guidelines = CropImageView.Guidelines.ON)
                )
            )
        }
    }

    // Define a crop image launcher
    private val cropImageLauncher = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            imageUri = result.uriContent
            profileImageView?.setImageURI(imageUri)
        } else {
            Toast.makeText(this, "Image crop failed, try again", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settins)
        storageProfilePictureRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")

        profileImageView = findViewById(R.id.settings_profile_image)
        fullNameEditText = findViewById(R.id.settings_full_name)
        userPhoneEditText = findViewById(R.id.settings_phone_number)
        userPhoneEditText.setText(Prevalent.currentOnlineUser!!.phone)
        addressEditText = findViewById(R.id.settings_address)
        profileChangeTextBtn = findViewById(R.id.profile_image_change_btn)
        closeTextBtn = findViewById(R.id.close_settings_btn)
        saveTextButton = findViewById(R.id.update_account_settings_btn)

        userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, addressEditText)

        closeTextBtn.setOnClickListener { finish() }
        saveTextButton.setOnClickListener { updateOnlyUserInfo() }

        profileChangeTextBtn?.setOnClickListener {
            // Launch the image picker
            imagePickerLauncher.launch("image/*")
        }
    }

    private fun updateOnlyUserInfo() {
        val ref = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["name"] = fullNameEditText!!.text.toString()
        userMap["address"] = addressEditText!!.text.toString()
        userMap["phoneOrder"] = userPhoneEditText.text.toString()

        ref.child(Prevalent.currentOnlineUser!!.phone!!).updateChildren(userMap)

        startActivity(Intent(this@SettinsActivity, HomeActivity::class.java))
        Toast.makeText(this@SettinsActivity, "Profile Info Updated Successfully", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun userInfoSaved() {
        if (TextUtils.isEmpty(fullNameEditText!!.text.toString())) {
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(addressEditText!!.text.toString())) {
            Toast.makeText(this, "Address is mandatory", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(userPhoneEditText.text.toString())) {
            Toast.makeText(this, "Phone number is mandatory", Toast.LENGTH_SHORT).show()
        } else if (checker == "clicked") {
            uploadImage()
        }
    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Update Profile")
        progressDialog.setMessage("Please wait, While we are updating your account information")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        if (imageUri != null) {
            val fileRef = storageProfilePictureRef?.child(Prevalent.currentOnlineUser!!.phone + ".jpg")
            uploadTask = fileRef?.putFile(imageUri!!)

            uploadTask!!.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    myUrl = downloadUrl.toString()
                    val ref = FirebaseDatabase.getInstance().reference.child("Users")

                    val userMap = HashMap<String, Any>()
                    userMap["name"] = fullNameEditText!!.text.toString()
                    userMap["address"] = addressEditText!!.text.toString()
                    userMap["phoneOrder"] = userPhoneEditText.text.toString()
                    userMap["image"] = myUrl
                    ref.child(Prevalent.currentOnlineUser!!.phone!!).updateChildren(userMap)

                    progressDialog.dismiss()
                    startActivity(Intent(this@SettinsActivity, HomeActivity::class.java))
                    Toast.makeText(this@SettinsActivity, "Profile Info Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this@SettinsActivity, "Error!", Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            Toast.makeText(this, "Image is not Selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun userInfoDisplay(
        profileImageView: CircleImageView?,
        fullNameEditText: EditText?,
        userPhoneEditText: EditText?,
        addressEditText: EditText?
    ) {
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(
            Prevalent.currentOnlineUser!!.phone!!
        )

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("image").exists()) {
                    val image = dataSnapshot.child("image").value.toString()
                    val name = dataSnapshot.child("name").value.toString()
                    val phone = dataSnapshot.child("phone").value.toString()
                    val address = dataSnapshot.child("address").value.toString()

                    Picasso.get().load(image).into(profileImageView)
                    fullNameEditText?.setText(name)
                    userPhoneEditText?.setText(phone)
                    addressEditText?.setText(address)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}
