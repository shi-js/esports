package com.example.esportsarena.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.esportsarena.Firebase.FirestoreClass
import com.example.esportsarena.R
import com.example.esportsarena.models.User
import com.example.projemanage.activities.BaseActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {

    lateinit var gso: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupActionBar()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val signIn = findViewById<View>(R.id.GsignIn) as SignInButton
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        signIn.setOnClickListener {
                view: View? -> GsignIn()
        }
    }

    private fun GsignIn() {

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult (task)
        }else {
            Toast.makeText(this, "Problem in execution order :(", Toast.LENGTH_LONG).show()
        }
    }
    private fun handleResult (completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                GregisterUser(account)
                
                finish()
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun userRegisteredSuccess(){
        hideProgressDialog()
        Toast.makeText(this,
            "you have successfully registered",Toast.LENGTH_SHORT).show()


        FirebaseAuth.getInstance().signOut()
        finish()
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_sign_up_activity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)

        }

        toolbar_sign_up_activity.setNavigationOnClickListener { onBackPressed() }

        btn_sign_up.setOnClickListener {
            registerUser()
        }

    }

    private fun validateForm(name: String, email: String, password: String) : Boolean{

        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter a name")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter a email address")
                false
            }
            TextUtils.isEmpty(password)-> {
                showErrorSnackBar("Please enter a password")
                false
            }else -> {
                true
            }
        }
    }

    private fun registerUser(){
        val name: String = et_name.text.toString().trim { it <= ' '}
        val email: String = et_email.text.toString().trim { it <= ' '}
        val password: String = et_password.text.toString().trim { it <= ' '}

        if(validateForm(name,email,password)){
            showprogressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!


                        val user = User(firebaseUser.uid,name,registeredEmail)

                        FirestoreClass().registerUser(this,user)
                    } else {
                        Toast.makeText(
                            this,
                            task.exception!!.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun GregisterUser(account: GoogleSignInAccount){
        val name: String? = account.displayName
        val email: String? = account.email
        val pass: Int? = 123456

        if (email != null) {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, pass.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!


                        val user = name?.let { User(firebaseUser.uid, it,registeredEmail) }

                        if (user != null) {
                            FirestoreClass().registerUser(this,user)
                        }
                    } else {
                        Toast.makeText(
                            this,
                            task.exception!!.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
}
}