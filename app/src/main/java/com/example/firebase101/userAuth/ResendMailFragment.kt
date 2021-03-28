package com.example.firebase101.userAuth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.firebase101.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ResendMailFragment : DialogFragment() {
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_resend_mail, container, false)
        emailEditText = view.findViewById(R.id.edtDialogMail)
        passwordEditText = view.findViewById(R.id.edtDialogPassword)
        val btnCancel = view.findViewById<Button>(R.id.btnDialogCancel)
        val btnSend = view.findViewById<Button>(R.id.btnDialogSend)

        btnCancel.setOnClickListener { dismiss() }

        btnSend.setOnClickListener {
            if (emailEditText.text.toString().isNotEmpty() && passwordEditText.text.toString()
                    .isNotEmpty()
            ) {
                sendResendMail(emailEditText.text.toString(), passwordEditText.text.toString())
            } else {
                //Toast.makeText(activity,"Mail send",Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "fill whole fields", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun sendResendMail(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reSendVerifiedMail()
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "fill whole fields", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun reSendVerifiedMail() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {

                Toast.makeText(requireContext(), "Verified mail sent : ", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error occured, While sending verified mail  " + p0.exception?.message,
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResendMailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}