package com.example.esportsarena.Firebase

import android.util.Log
import android.widget.Toast
import com.example.esportsarena.activities.SignUpActivity
import com.example.esportsarena.models.User
import com.example.esportsarena.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class FirestoreClass {
    
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserid())
            .set(userInfo, SetOptions.merge())
            activity.userRegisteredSuccess()
//            .addOnSuccessListener{
//
//
//
//            }.addOnFailureListener {
//
//            Log.e(activity.javaClass.simpleName,"Error")
//            }
    }


//

//    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>){
//        mFireStore.collection(Constants.USERS)
//            .document(getCurrentUserid())
//            .update(userHashMap)
//            .addOnSuccessListener {
//                Log.i(activity.javaClass.simpleName, "Profile data updated successfully")
//                Toast.makeText(activity
//                    ,"Profile data updated successfully",Toast.LENGTH_LONG).show()
//                activity.profileUpdateSuccess()
//            }.addOnFailureListener {
//                e ->
//                activity.hideProgressDialog()
//                Log.e(
//                    activity.javaClass.simpleName,
//                    "Error while creating a board",
//                    e
//                )
//                Toast.makeText(activity
//                    ,"error when updating the profile",Toast.LENGTH_LONG).show()
//            }
////    }
//
//    fun loadUserData(activity: Activity, readBoardsList: Boolean = false){
//        mFireStore.collection(Constants.USERS)
//            .document(getCurrentUserid())
//            .get()
//            .addOnSuccessListener{document ->
//                val loggedInUser = document.toObject(User::class.java)
//
//                when(activity){
//                    is SignInActivity ->{
//                        if (loggedInUser != null) {
//                            activity.signInSuccess(loggedInUser)
//                        }
//                    }
////                    is MainActivity -> {
////                        if (loggedInUser != null) {
////                            activity.updateNavigationUserDetails(loggedInUser, readBoardsList )
////                        }
////                    }
////                    is MyProfileActivity -> {
////                        if (loggedInUser != null) {
////                            activity.setUserDataInUI(loggedInUser)
////                        }
////                    }
//                }
//            }.addOnFailureListener {
//                    e->
//                when(activity){
//                    is SignInActivity ->{
//                        activity.hideProgressDialog()
//                    }
////                    is MainActivity -> {
////                        activity.hideProgressDialog()
////                    }
//                }
//
//                Log.e("SignInUser","Error writing document")
//            }
//    }

    fun getCurrentUserid(): String{
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
}