package com.example.al_pc.administrationrecipes.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatSpinner
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.example.al_pc.administrationrecipes.R
import com.example.al_pc.administrationrecipes.data.SearchRepository
import com.example.al_pc.administrationrecipes.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UpdateUsersCategoryActivity : AppCompatActivity()  {

    @BindView(R.id.email) lateinit var email:AppCompatEditText
    @BindView(R.id.category) lateinit var category:AppCompatSpinner

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_users_category)
        ButterKnife.bind(this)

        findViewById<AppCompatButton>(R.id.update).setOnClickListener {
            if (!email.text.toString().isEmpty()) {
                lateinit var putCategory: String
                when (category.selectedItem.toString()) {
                    "Зритель" -> putCategory = "0"
                    "Редактор" -> putCategory = "1"
                    "Администратор" -> putCategory = "2"
                }
                compositeDisposable.add(
                        repository.updateUsersCategory(email.text.toString())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe({ result ->
                                    if (result.result.equals("true")) {
                                        Toast.makeText(this, "Категория была изменена", Toast.LENGTH_LONG).show()
                                        startActivity(Intent(this, CabinetAdministrationActivity::class.java))
                                    } else if(result.result.equals("error 1")){
                                        Toast.makeText(this, "Данный пользователь уже имеет такую категория", Toast.LENGTH_LONG).show()
                                    }else if(result.result.equals("error 2")){
                                        Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_LONG).show()
                                    }else if(result.result.equals("error 3")) {
                                        Toast.makeText(this, "Ошибка соединения", Toast.LENGTH_LONG).show()
                                    }
                                })
                )
            }else{
                Toast.makeText(this, "Вы не ввели email", Toast.LENGTH_LONG).show()
            }
        }
    }
}