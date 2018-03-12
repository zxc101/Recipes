package com.example.al_pc.administrationrecipes.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatEditText
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.example.al_pc.administrationrecipes.R
import com.example.al_pc.administrationrecipes.data.SearchRepository
import com.example.al_pc.administrationrecipes.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AutentificationActivity : AppCompatActivity() {

    @BindView(R.id.email) lateinit var email:AppCompatEditText
    @BindView(R.id.password) lateinit var password: AppCompatEditText

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autentification)
        ButterKnife.bind(this)

        findViewById<AppCompatButton>(R.id.autentification).setOnClickListener {
            compositeDisposable.add(
                    repository.autentificationUser(email.text.toString(), password.text.toString())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({result ->
                                if(!result.error.toString().isEmpty()) {
                                    Toast.makeText(this, result.error.toString(), Toast.LENGTH_SHORT).show()
                                }else{
                                    if(result.category.toString().equals("Администратор")) {
                                        val editor = getSharedPreferences("User", Context.MODE_PRIVATE).edit()
                                        editor.putString("email", result.email.toString())
                                        editor.putString("nickname", result.nickname.toString())
                                        editor.putString("category", result.category.toString())
                                        editor.apply()
                                        startActivity(Intent(this, CabinetAdministrationActivity::class.java))
                                    }else{
                                        Toast.makeText(this, "Отказано в доступе", Toast.LENGTH_LONG).show()
                                    }
                                }
                            })
            )
        }
    }

    override fun onBackPressed() {
    }
}