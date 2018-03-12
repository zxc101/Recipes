package com.example.al_pc.administrationrecipes.Activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import com.example.al_pc.administrationrecipes.R
import org.jetbrains.anko.startActivity

class CabinetAdministrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cabinet_administration)

        val pref = getSharedPreferences("User", Context.MODE_PRIVATE)
        if(pref.getString("nickname", "").isEmpty()){
            startActivity<AutentificationActivity>()
        }

        findViewById<AppCompatButton>(R.id.insert_ingredient).setOnClickListener {
            startActivity<InsertIngredientActivity>()
        }

        findViewById<AppCompatButton>(R.id.show_dishes).setOnClickListener {
            startActivity<DishesActivity>()
        }

        findViewById<AppCompatButton>(R.id.show_inquiries).setOnClickListener {
            startActivity<InquiryActivity>()
        }

        findViewById<AppCompatButton>(R.id.show_logs).setOnClickListener {
            startActivity<LogActivity>()
        }

        findViewById<AppCompatButton>(R.id.exit).setOnClickListener {
            startActivity<AutentificationActivity>()
        }
    }

    override fun onBackPressed() {
    }
}
