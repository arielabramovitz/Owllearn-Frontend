package com.example.owllearn

import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Path.Direction
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.example.owllearn.databinding.ActivityMainBinding
import com.example.owllearn.databinding.FragmentDashboardBinding
import com.example.owllearn.databinding.NavHeaderMainBinding
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import org.json.JSONException
import org.json.JSONObject
import java.util.prefs.Preferences
import kotlin.concurrent.fixedRateTimer
import kotlin.math.cbrt

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: SharedPreferences
    private val FIRST_NAME = "first_name"
    private val LAST_NAME = "last_name"
    private val EMAIL = "email"
    private val PROFILE_PICTURE = "profile_picture"
    private val FIRST_TIME = "first_time"


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splash = installSplashScreen()
        Thread.sleep(3000)
        binding = ActivityMainBinding.inflate(layoutInflater)
        preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)

        if (preferences.getBoolean(FIRST_TIME, true)) {
            // on first launch, go to profile creation form
            setContentView(R.layout.fragment_onboarding)
            val submitButton = findViewById<Button>(R.id.submit_button)
            submitButton.setOnClickListener {
                if (verifyForm()) {
                    // TODO: if the form is good, move the user to the dashboard
                    preferences.edit().putBoolean(FIRST_TIME, false).apply()
                    afterOnboarding()
                } else {
                    // TODO: check that this toast shows if any of the fields are empty
                    val badFormToast = Toast.makeText(applicationContext, R.string.bad_form, Toast.LENGTH_LONG)
                    badFormToast.show()
                }
            }

            initiateFacebookCallback()

        } else {
            afterOnboarding()
        }


    }

    private fun initiateFacebookCallback() {

        val callbackManager = CallbackManager.Factory.create()
        val loginButton = findViewById<LoginButton>(R.id.reg_facebook)

        loginButton.setReadPermissions(listOf("public_profile", "email"))
        // If you are using in a fragment, call loginButton.setFragment(this)

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
//                Log.d("TAG", loginResult.toString())
                val token = loginResult.accessToken

                val request = GraphRequest.newMeRequest(token) { obj, response ->
                    try {
                        // Save user email to variable
                        val email = obj!!.getString("email")
                        val userFirstName = obj.getString("first_name")
                        val userLastName = obj.getString("last_name")
                        val profilePic = obj.getJSONObject("picture").getJSONObject("data").getString("url")

                        preferences.edit()
                            .putString(FIRST_NAME, userFirstName)
                            .putString(LAST_NAME, userLastName)
                            .putString(EMAIL, email)
                            .putString(PROFILE_PICTURE, profilePic).apply()

                        preferences.edit().putBoolean(FIRST_TIME, false).apply()
                        afterOnboarding()
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@MainActivity,
                            "Facebook Authentication Failed.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                val parameters = Bundle()
                parameters.putString("fields", "email,first_name,last_name,picture")
                request.parameters = parameters
                request.executeAsync()
                afterOnboarding()
            }

            override fun onCancel() {
                Toast.makeText(this@MainActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun afterOnboarding() {
        setContentView(binding.root)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val userFirstName = preferences.getString(FIRST_NAME, getString(R.string.username))
        val userLastName = preferences.getString(LAST_NAME, "")
        val userEmail = preferences.getString(EMAIL, "")
        val userPicture = preferences.getString(PROFILE_PICTURE, null)
        val userFullname = "$userFirstName $userLastName"

        findViewById<TextView>(R.id.dashboard_title)?.text =
            String.format(resources.getString(R.string.title), userFirstName)
        findViewById<TextView>(R.id.user_full)?.text = userFullname
        findViewById<TextView>(R.id.header_email)?.text = userEmail
        val picture = findViewById<ImageView>(R.id.header_image)

        if (userPicture != null) {
            Glide.with(picture.context)
                .load(userPicture)
                .into(picture);
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard, R.id.nav_decks, R.id.nav_study
            ), drawerLayout
        )


        navView.setupWithNavController(navController)
    }

    private fun verifyForm(): Boolean {
        val firstName = findViewById<EditText>(R.id.first_name_edittext)
        val lastName = findViewById<EditText>(R.id.last_name_edittext)

        if (firstName.text.isNotEmpty() && lastName.text.isNotEmpty()) {
            preferences.edit().putString(FIRST_NAME, firstName.text.toString())
            .putString(LAST_NAME, lastName.text.toString()).apply()
            return true
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}