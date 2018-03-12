package com.example.al_pc.recipes.Activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.widget.Toast
import butterknife.ButterKnife
import com.example.al_pc.recipes.R
import com.example.al_pc.recipes.data.SearchRepository
import com.example.al_pc.recipes.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity

class OptionsActivity: AppCompatActivity(){

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        ButterKnife.bind(this)

        val userPref = getSharedPreferences("User", Context.MODE_PRIVATE)
        val email: String = userPref.getString("email", "")

        findViewById<AppCompatButton>(R.id.send_inquiry).setOnClickListener {
            compositeDisposable.add(
                    repository.sendInquiry(email)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                if (result.result == "error1") {
                                    Toast.makeText(this, "Ошибка соединения", Toast.LENGTH_SHORT).show()
                                } else if(result.result == "error2"){
                                    Toast.makeText(this, "Запрос уже рассматривается", Toast.LENGTH_SHORT).show()
                                } else if(result.result == "error3") {
                                    Toast.makeText(this, "У вас уже есть доступ", Toast.LENGTH_SHORT).show()
                                }else {
                                    Toast.makeText(this, "Запрос отправлен", Toast.LENGTH_SHORT).show()
                                }
                            })
            )
            startActivity<DishesMainActivity>()
        }

        findViewById<AppCompatButton>(R.id.deleteAccount).setOnClickListener {
            compositeDisposable.add(
                    repository.deleteAccount(email)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                if (result.result == "error") {
                                    Toast.makeText(this, "Ошибка соединения", Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(this, "Аккаунт был удалён", Toast.LENGTH_SHORT).show()
                                }
                            })
            )
            startActivity<AutentificationActivity>()
        }
    }
}