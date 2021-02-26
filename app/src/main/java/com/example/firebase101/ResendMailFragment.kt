package com.example.firebase101

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResendMailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResendMailFragment : DialogFragment() {

    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var mcontext:FragmentActivity

    // TODO: Rename and change types of parameters
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
        val view =inflater.inflate(R.layout.fragment_resend_mail, container, false)
        emailEditText=view.findViewById(R.id.edtDialogMail)
        passwordEditText=view.findViewById(R.id.edtDialogPassword)
        mcontext= requireActivity()
        val  btnCancel=view.findViewById<Button>(R.id.btnDialogCancel)
        val btnSend=view.findViewById<Button>(R.id.btnDialogSend)

        btnCancel.setOnClickListener { dismiss() }

        btnSend.setOnClickListener {
            if (emailEditText.text.toString().isNotEmpty() && passwordEditText.text.toString().isNotEmpty()){

                    sendResendMail(emailEditText.text.toString(),passwordEditText.text.toString())

            }else{
//            Toast.makeText(activity,"Mail send",Toast.LENGTH_SHORT).show()
                Toast.makeText(mcontext,"fill whole fields",Toast.LENGTH_SHORT).show()

            }
        }



        return view
    }

    private fun sendResendMail(email: String, password :String){
        var credential=EmailAuthProvider.getCredential(email,password)
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        reSendVerifiedMail()
                        dismiss()
                    }else{
                        Toast.makeText(mcontext,"fill whole fields",Toast.LENGTH_SHORT).show()
                    }
                }
        }

    private fun reSendVerifiedMail (){
        var user=FirebaseAuth.getInstance().currentUser
        if (user!=null){
            user.sendEmailVerification()

                    .addOnCompleteListener { p0 ->
                        if (p0.isSuccessful){

                            Toast.makeText(mcontext,"Verified mail sent : ",Toast.LENGTH_SHORT).show()
                        } else{
                            Toast.makeText(mcontext,"Error occured, While sending verified mail  "+p0.exception?.message,Toast.LENGTH_SHORT).show()

                        }
                    }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResendMailFragment.
         */
        // TODO: Rename and change types and number of parameters
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