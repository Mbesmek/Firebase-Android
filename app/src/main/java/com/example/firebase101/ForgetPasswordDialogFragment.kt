package com.example.firebase101

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordDialogFragment : DialogFragment() {

    lateinit var emailEditText: EditText

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forget_password_dialog, container, false)
        val btnCancel = view.findViewById<Button>(R.id.btnCancelForgetPsw)
        val btnSend = view.findViewById<Button>(R.id.btnSendForgetPsw)

        emailEditText = view.findViewById(R.id.edtForgetPasswordMail)

        btnCancel.setOnClickListener {
            dismiss()
        }
        btnSend.setOnClickListener {
            FirebaseAuth.getInstance().sendPasswordResetEmail(emailEditText.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Password Reset Mail was Sent", Toast.LENGTH_SHORT).show()
                            dismiss()
                        } else {
                            Toast.makeText(requireContext(), "Check email! " + " " + task.exception, Toast.LENGTH_SHORT).show()
                            dismiss()
                        }
                    }
        }
        return view
    }
}