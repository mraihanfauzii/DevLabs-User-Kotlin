package com.hackathon.devlabsuser.ui.main.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hackathon.devlabsuser.databinding.FragmentModelProfPictDialogBinding

class ModelProfPictDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentModelProfPictDialogBinding
    private var shouldDismissDialog = false
    private var listener: OnOptionSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnOptionSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnOptionSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModelProfPictDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.galeri.setOnClickListener {
            listener?.onOptionSelected("gallery")
            shouldDismissDialog = true
        }

        binding.kamera.setOnClickListener {
            listener?.onOptionSelected("camera")
            shouldDismissDialog = true
        }
    }

    override fun onResume() {
        super.onResume()

        if (dialog?.isShowing == true && shouldDismissDialog) {
            dismiss()
        }

        shouldDismissDialog = false
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}