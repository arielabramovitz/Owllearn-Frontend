package com.example.owllearn

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Path.Direction
import android.os.*
import android.util.AttributeSet
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
import com.example.owllearn.data.provider.SharedProvider
import com.example.owllearn.data.viewmodel.SharedViewModel
import com.example.owllearn.databinding.ActivityMainBinding
import com.example.owllearn.databinding.FragmentDashboardBinding
import com.example.owllearn.databinding.NavHeaderMainBinding
import com.example.owllearn.util.consts
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.prefs.Preferences
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: SharedPreferences
    private var updatedCreds = false


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splash = installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard, R.id.nav_decks, R.id.nav_study
            ), drawerLayout
        )
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        navView.setupWithNavController(navController)



        val listener =  object: DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                if (!updatedCreds) {
                    updatedCreds = true
                    val userFirstName = preferences.getString(consts.FIRST_NAME, getString(R.string.username))
                    val userLastName = preferences.getString(consts.LAST_NAME, "")
                    val userEmail = preferences.getString(consts.EMAIL, "")
                    val userPicture = preferences.getString(consts.PROFILE_PICTURE, null)
                    val userFullname = "$userFirstName $userLastName"
                    val picture = findViewById<ImageView>(R.id.header_image)
                    findViewById<TextView>(R.id.user_full)?.text = userFullname
                    findViewById<TextView>(R.id.header_email)?.text = userEmail

                    if (userPicture != null) {
                        Glide.with(picture.context)
                            .load(userPicture)
                            .into(picture)
                    }
                }
                super.onDrawerSlide(drawerView, slideOffset)
            }
        }

        drawerLayout.addDrawerListener(listener)

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