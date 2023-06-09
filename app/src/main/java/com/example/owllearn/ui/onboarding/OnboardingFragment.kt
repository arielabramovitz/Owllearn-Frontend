package com.example.owllearn.ui.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.owllearn.R
import com.example.owllearn.databinding.FragmentOnboardingBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import org.json.JSONException
import java.util.*
import com.example.owllearn.util.consts

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        preferences = requireActivity().getSharedPreferences("PREFERENCE", AppCompatActivity.MODE_PRIVATE)
        if (preferences.getBoolean(consts.FIRST_TIME, true)) {

            _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
            val root: View = binding.root
            root.findViewById<DrawerLayout>(R.id.drawer_layout)?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            initForm()
            initFacebook()
            return root
        }
        moveToDashboard(null, null, null, null, null)

        return view
    }

    private fun initFacebook() {
        val callbackManager = CallbackManager.Factory.create()
        val loginButton = binding.regFacebook

        loginButton.setReadPermissions(listOf("public_profile", "email"))

        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val token = loginResult.accessToken
                var userFirstName = ""
                var userId: String? = null
                var userLastName = ""
                var userEmail = ""
                var userPic: String? = null
                val request = GraphRequest.newMeRequest(token) { obj, response ->
                    try {
                        val email = obj!!.getString("email")
                        userFirstName = obj.getString("first_name")
                        userLastName = obj.getString("last_name")
                        userPic = obj.getJSONObject("picture").getJSONObject("data").getString("url")
                        userId = UUID.randomUUID().toString()
                        preferences.edit()
                            .putString(consts.FIRST_NAME, userFirstName)
                            .putString(consts.LAST_NAME, userLastName)
                            .putString(consts.EMAIL, email)
                            .putString(consts.PROFILE_PICTURE, userPic)
                            .putBoolean(consts.FIRST_TIME, false)
                            .putString(consts.UID, userId)
                            .apply()

                    } catch (e: JSONException) {
                        Toast.makeText(
                            context,
                            "Facebook Authentication Failed.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                val parameters = Bundle()
                parameters.putString("fields", "email,first_name,last_name,picture")
                request.parameters = parameters
                request.executeAsync()
                // move to dashboard
                moveToDashboard(userId, userFirstName, userLastName, userEmail, userPic)
            }

            override fun onCancel() {
                Toast.makeText(context, "Login Cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun initForm() {
        val submitButton = binding.submitButton
        submitButton.setOnClickListener {
            var cont = false

            val firstName = binding.onboardingFirstnameEdit
            val lastName = binding.onboardingLastnameEdit
            val email = binding.onboardingEmailEdit
            val userId = UUID.randomUUID().toString()
            if (firstName.text?.isNotEmpty() == true && lastName.text?.isNotEmpty() == true && email.text?.isNotEmpty() == true) {
                preferences.edit()
                    .putString(consts.FIRST_NAME, firstName.text.toString())
                    .putString(consts.LAST_NAME, lastName.text.toString())
                    .putString(consts.EMAIL, email.text.toString())
                    .apply()
                cont = true
            }
            if (cont) {
                preferences.edit().putBoolean(consts.FIRST_TIME, false)
                    .putString(consts.UID, userId).apply()
                moveToDashboard(userId, firstName.toString(), lastName.toString(), email.toString(), null)
            } else {
                val badFormToast = Toast.makeText(context, R.string.bad_form, Toast.LENGTH_LONG)
                badFormToast.show()
            }
        }
    }

    private fun verifyForm(): Boolean {
        val firstName = binding.onboardingFirstnameEdit
        val lastName = binding.onboardingLastnameEdit
        val email = binding.onboardingEmailEdit
        if (firstName.text?.isNotEmpty() == true && lastName.text?.isNotEmpty() == true && email.text?.isNotEmpty() == true) {
            preferences.edit()
                .putString(consts.FIRST_NAME, firstName.text.toString())
                .putString(consts.LAST_NAME, lastName.text.toString())
                .putString(consts.EMAIL, email.text.toString())
                .apply()
            return true
        }
        return false
    }

    fun moveToDashboard(
        userId: String?,
        userFirstName: String?,
        userLastName: String?,
        userEmail: String?,
        userPic: String?
    ) {
        if (userFirstName != null) {
            view?.findViewById<TextView>(R.id.dashboard_title)?.text =
                String.format(resources.getString(R.string.title), userFirstName)

        }
        val action = OnboardingFragmentDirections.actionOnboardingToDashboard(
            userId = userId.toString(),
            firstName = userFirstName.toString(),
            lastName = userLastName.toString(),
            userEmail = userEmail.toString(),
            userPic = userPic.toString()
        )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}