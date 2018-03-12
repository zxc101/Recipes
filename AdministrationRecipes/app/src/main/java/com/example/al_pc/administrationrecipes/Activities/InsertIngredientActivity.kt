package com.example.al_pc.administrationrecipes.Activities

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

class InsertIngredientActivity : AppCompatActivity() {

    @BindView(R.id.name) lateinit var name:AppCompatEditText

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_ingredient)
        ButterKnife.bind(this)
        findViewById<AppCompatButton>(R.id.add).setOnClickListener {
            compositeDisposable.add(
                    repository.insertIngredient(name.text.toString())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({result ->
                                if(result.result.equals("true")) {
                                    Toast.makeText(this, "Ингредиент был добавлен", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, CabinetAdministrationActivity::class.java))
                                }else{
                                    Toast.makeText(this, "Этот ингредиент уже есть", Toast.LENGTH_SHORT).show()
                                }
                            })
            )
        }
    }
}
