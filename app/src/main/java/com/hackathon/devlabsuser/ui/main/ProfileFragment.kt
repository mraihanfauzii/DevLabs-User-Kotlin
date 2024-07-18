package com.hackathon.devlabsuser.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.FragmentProfileBinding
import com.hackathon.devlabsuser.ui.authentication.LoginActivity
import com.hackathon.devlabsuser.utils.Setting
import com.hackathon.devlabsuser.viewmodel.SettingViewModel
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private lateinit var viewModel: SettingViewModel
    private lateinit var imgUri : Uri

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            SettingViewModel.Factory(Setting(requireContext()))
        )[SettingViewModel::class.java]

//        auth = FirebaseAuth.getInstance()
//        val user = auth.currentUser
//
//        if (user != null) {
//            binding.apply {
//                edtName.text = user.displayName
//                edtEmail.text = user.email
//                val ref = FirebaseStorage.getInstance().reference.child("/img_user/${FirebaseAuth.getInstance().currentUser?.uid}/")
//                ref.downloadUrl.addOnCompleteListener { Task ->
//                    if (Task.isSuccessful) {
//                        val imgUri = Task.result
//                        Glide.with(this@ProfileFragment).load(imgUri).into(civUser)
//                    }
//                }
//
//                if (user.providerData.any { it.providerId == EmailAuthProvider.PROVIDER_ID }) {
//                    tvChangePassword.visibility = View.VISIBLE
//                    btnChangePassword.visibility = View.VISIBLE
//                } else {
//                    tvChangePassword.visibility = View.GONE
//                    btnChangePassword.visibility = View.GONE
//                }
//            }
//        }
//
//        binding.civUser.setOnClickListener {
//            requestCameraPermission()
//        }
//
//        binding.btnLogout.setOnClickListener {
//            logout()
//        }
//
//        binding.btnChangePassword.setOnClickListener {
//            changePassword()
//        }
//
//        viewModel.getSetting().observe(viewLifecycleOwner) {
//            if (it) {
//                binding.changeTheme.text = getString(R.string.dark_mode)
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            } else {
//                binding.changeTheme.text = getString(R.string.light_mode)
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//            binding.changeTheme.isChecked = it
//        }
//        binding.changeTheme.setOnCheckedChangeListener { _, isChecked ->
//            viewModel.saveSetting(isChecked)
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == CAMERA_PERMISSION_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                goToCamera()
//            } else {
//                Toast.makeText(requireContext(), "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun requestCameraPermission() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.CAMERA
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.CAMERA),
//                CAMERA_PERMISSION_CODE
//            )
//        } else {
//            goToCamera()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQ_CAM && resultCode == Activity.RESULT_OK) {
//            val imgBitmap = data?.extras?.get("data") as Bitmap
//            uploadImgToFirebase(imgBitmap)
//        }
//    }
//
//    private fun goToCamera() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
//            activity?.packageManager?.let {
//                intent?.resolveActivity(it).also {
//                    startActivityForResult(intent, REQ_CAM)
//                }
//            }
//        }
//    }
//
//    private fun uploadImgToFirebase(imgBitmap : Bitmap) {
//        val BAOS = ByteArrayOutputStream()
//        val ref = FirebaseStorage.getInstance().reference.child("/img_user/${FirebaseAuth.getInstance().currentUser?.uid}/")
//        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, BAOS)
//
//        val img = BAOS.toByteArray()
//        ref.putBytes(img)
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    ref.downloadUrl.addOnCompleteListener { Task->
//                        Task.result.let{ Uri->
//                            imgUri = Uri
//                            binding.civUser.setImageBitmap(imgBitmap)
//                        }
//                    }
//                }
//            }
//    }
//
//    private fun changePassword() {
//        auth = FirebaseAuth.getInstance()
//        val user = auth.currentUser
//
//        binding.apply {
//            cvCurrentPassword.visibility = View.VISIBLE
//
//            btnCancel.setOnClickListener {
//                cvCurrentPassword.visibility = View.GONE
//            }
//
//            btnConfirm.setOnClickListener {
//                val pass = edtCurrentPassword.text.toString()
//                if (pass.isEmpty()) {
//                    edtCurrentPassword.error = "Password Tidak Boleh Kosong!"
//                    binding.edtCurrentPassword.requestFocus()
//                }
//
//                user.let {
//                    val userCredential = EmailAuthProvider.getCredential(it?.email!!, pass)
//                    it.reauthenticate(userCredential).addOnCompleteListener { task ->
//                        when {
//                            task.isSuccessful -> {
//                                binding.apply {
//                                    cvCurrentPassword.visibility = View.GONE
//                                    cvUpdatePassword.visibility = View.VISIBLE
//                                }
//                            }
//                            task.exception is FirebaseAuthInvalidCredentialsException -> {
//                                binding.apply {
//                                    edtCurrentPassword.error = "Password Salah!"
//                                    edtCurrentPassword.requestFocus()
//                                }
//                            } else -> {
//                            Toast.makeText(activity, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                        }
//                        }
//                    }
//                }
//                binding.apply {
//                    btnNewCancel.setOnClickListener {
//                        cvUpdatePassword.visibility = View.GONE
//                    }
//                    btnNewChange.setOnClickListener {
//                        val newPass = edtNewPassword.text.toString()
//                        val passConfirm = edtConfirmPassword.text.toString()
//
//                        if (newPass.isEmpty()) {
//                            edtCurrentPassword.error = "Password Tidak Boleh Kosong!"
//                            edtCurrentPassword.requestFocus()
//                        }
//                        if (passConfirm.isEmpty()) {
//                            edtConfirmPassword.error = "Ulangi Password Baru!"
//                            edtConfirmPassword.requestFocus()
//                        }
//                        if (newPass.length < 6) {
//                            edtCurrentPassword.error = "Password Harus Lebih dari 6 Karakter!"
//                            edtCurrentPassword.requestFocus()
//                        }
//                        if (newPass != passConfirm) {
//                            edtCurrentPassword.error = "Password dan Password Konfirmasi Harus Sama!"
//                            edtCurrentPassword.requestFocus()
//                        }
//                        user?.let {
//                            user.updatePassword(newPass).addOnCompleteListener {
//                                if (it.isSuccessful) {
//                                    Toast.makeText(activity, "Password Berhasil Diupdate", Toast.LENGTH_SHORT).show()
//                                    logout()
//                                    Toast.makeText(activity, "Silahkan Login Kembali", Toast.LENGTH_SHORT).show()
//                                } else {
//                                    Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun logout() {
//        auth = FirebaseAuth.getInstance()
//        auth.signOut()
//        val intent = Intent(context, LoginActivity::class.java)
//        startActivity(intent)
//        activity?.finish()
//    }
//
//    companion object {
//        const val REQ_CAM = 100
//        const val CAMERA_PERMISSION_CODE = 101
    }
}