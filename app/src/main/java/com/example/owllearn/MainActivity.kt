package com.example.owllearn

import android.content.SharedPreferences
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
import com.example.owllearn.databinding.ActivityMainBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import java.util.prefs.Preferences
import kotlin.math.cbrt

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val firstName = "first_name"
    private val lastName = "last_name"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splash = installSplashScreen()
        Thread.sleep(3000)

        val preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)

        if (preferences.getBoolean("first_time", true)) {
            // on first launch, go to profile creation form
            setContentView(R.layout.fragment_onboarding)
            val submitButton = findViewById<Button>(R.id.submit_button)

            if (verifyForm(preferences)) {
                // TODO: if the form is good, move the user to the dashboard
                preferences.edit().putBoolean("first_time", false).apply()
            } else {
                // TODO: check that this toast shows if any of the fields are empty
                val badFormToast = Toast.makeText(applicationContext, R.string.bad_form, Toast.LENGTH_LONG)
                badFormToast.show()
            }

            // set first_time to false so that every other use will
        } else {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            val drawerLayout: DrawerLayout = binding.drawerLayout
            val navView: NavigationView = binding.navView
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            val userFirstName = preferences.getString(firstName, getString(R.string.username))
            val userLastName = preferences.getString(lastName, "")
            val userFullname = "$userFirstName $userLastName"

            findViewById<TextView>(R.id.dashboard_title)?.text = userFullname
            findViewById<TextView>(R.id.header_fullname)?.text = String.format(resources.getString(R.string.title), userFirstName)


            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_dashboard, R.id.nav_decks ,R.id.nav_study
                ), drawerLayout
            )
            navView.setupWithNavController(navController)
        }


    }

    private fun facebookCallBack() {
        val callbackManager = CallbackManager.Factory.create()
        val loginButton = findViewById<LoginButton>(R.id.reg_facebook)

        loginButton.setReadPermissions(listOf("public_profile", "email"))
        // If you are using in a fragment, call loginButton.setFragment(this)

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("TAG", "Success Login")
                // Get User's Info
            }

            override fun onCancel() {
                Toast.makeText(this@MainActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_LONG).show()
            }
        })


    }

    private fun verifyForm(preferences: SharedPreferences): Boolean {
        val firstName = findViewById<EditText>(R.id.first_name_edittext)
        val lastName = findViewById<EditText>(R.id.last_name_edittext)

        if (firstName.text.isNotEmpty() && lastName.text.isNotEmpty()) {
            preferences.edit().putString("first_name", firstName.text.toString()).apply()
            preferences.edit().putString("last_name", lastName.text.toString()).apply()
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