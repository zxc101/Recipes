package com.example.al_pc.recipescreator.Activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import com.example.al_pc.recipescreator.R

class CabinetCreatorActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        val userPref = getSharedPreferences("User", Context.MODE_PRIVATE)
        if(userPref.getString("nickname", "").isEmpty()){
            startActivity(Intent(this, AutentificationActivity::class.java))
        }

        val movePref = getSharedPreferences("Move", Context.MODE_PRIVATE).edit()
        movePref.putString("move", "")
        movePref.apply()

        val oldDishPref = getSharedPreferences("OldDish", Context.MODE_PRIVATE).edit()
        oldDishPref.putString("name", "")
        oldDishPref.putString("category", "")
        oldDishPref.putString("ingredients", "")
        oldDishPref.apply()

        val newDishPref = getSharedPreferences("NewDish", Context.MODE_PRIVATE).edit()
        newDishPref.putString("name", "")
        newDishPref.putString("category", "")
        newDishPref.putString("ingredients", "")
        newDishPref.putString("recipes", "")
        newDishPref.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cabinet_creator)
        val movePref = getSharedPreferences("Move", Context.MODE_PRIVATE).edit()

        findViewById<AppCompatButton>(R.id.add_dish).setOnClickListener {
            movePref.putString("move", "insert")
            movePref.apply()
            startActivity(Intent(this, CreateDishesNameActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.update_dish).setOnClickListener {
            movePref.putString("move", "update")
            movePref.apply()
            startActivity(Intent(this, SelectDishActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.delete_dish).setOnClickListener {
            movePref.putString("move", "delete")
            movePref.apply()
            startActivity(Intent(this, SelectDishActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.exit).setOnClickListener {
            val editor = getSharedPreferences("User", Context.MODE_PRIVATE).edit()
            editor.putString("email", "")
            editor.putString("nickname", "")
            editor.putString("category", "")
            editor.apply()
            startActivity(Intent(this, AutentificationActivity::class.java))
        }
    }

    override fun onBackPressed() {
    }
}
