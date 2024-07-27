package com.hackathon.devlabsuser.ui.main.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ActivityEditProfileBinding
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.ui.authentication.LoginActivity
import com.hackathon.devlabsuser.utils.reduceFileImage
import com.hackathon.devlabsuser.utils.rotateFile
import com.hackathon.devlabsuser.utils.uriToFile
import com.hackathon.devlabsuser.viewmodel.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class EditProfileActivity : AppCompatActivity(), OnOptionSelectedListener {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var profileViewModel: ProfileViewModel
    private val dialogProfPictFragment = ModelDialogProfPictFragment()
    private var getFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationManager = AuthenticationManager(this)
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        val namaLengkap = intent.getStringExtra("nama_lengkap")
        val nomorTelepon = intent.getStringExtra("nomor_telepon")
        val bio = intent.getStringExtra("bio")

        val getToken = authenticationManager.getAccess(AuthenticationManager.TOKEN).toString()
        val token = "Bearer $getToken"

        binding.apply {
            edtNamaLengkap.setText(namaLengkap)
            edtTelepon.setText(nomorTelepon)
            edtDescription.setText(bio)
            binding.profileContainer.setOnClickListener {
                dialogProfPictFragment.show(supportFragmentManager, ModelDialogProfPictFragment::class.java.simpleName)
            }
            btnBatalkan.setOnClickListener {
                finish()
            }
            btnSimpan.setOnClickListener {
                val inputNamaLengkap = edtNamaLengkap.text.toString()
                val inputNomorTelepon = edtTelepon.text.toString()
                val inputBio = edtDescription.text.toString()

                val profileNameRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), inputNamaLengkap)
                val phoneNumberRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), inputNomorTelepon)
                val bioRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), inputBio)

                val file = getFile ?: return@setOnClickListener
                val reducedFile = reduceFileImage(file)
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), reducedFile)
                val profilePicturePart = MultipartBody.Part.createFormData("profile_picture", reducedFile.name, requestFile)

                profileViewModel.putProfile(token, profileNameRequestBody, phoneNumberRequestBody, bioRequestBody, profilePicturePart)
                profileViewModel.putProfileResponse.observe(this@EditProfileActivity) { getProfile ->
                    if (getProfile != null) {
                        authenticationManager.apply {
                            Log.e("Get Profile : ", getProfile.toString())
                            login(AuthenticationManager.NAME, inputNamaLengkap)
                            login(AuthenticationManager.PHONE_NUMBER, inputNomorTelepon)
                            login(AuthenticationManager.PROFILE_PICTURE, "")
                            login(AuthenticationManager.PROFILE_DESCRIPTION, inputBio)
                        }
                    }
                }
            }
        }
    }

    override fun onOptionSelected(option: String) {
        when (option) {
            "camera" -> {
                val intent = Intent(this, CameraActivity::class.java)
                launcherIntentCameraX.launch(intent)
            }
            "gallery" -> {
                startGallery()
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.ivProfileImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@EditProfileActivity)
                getFile = myFile
                binding.ivProfileImage.setImageURI(uri)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.Permisson_denied),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}