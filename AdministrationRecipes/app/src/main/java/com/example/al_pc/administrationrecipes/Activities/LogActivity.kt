package com.example.al_pc.administrationrecipes.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.al_pc.administrationrecipes.Adapters.LogsAdapter
import com.example.al_pc.administrationrecipes.R
import com.example.al_pc.administrationrecipes.data.Log
import com.example.al_pc.administrationrecipes.data.SearchRepository
import com.example.al_pc.administrationrecipes.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LogActivity : AppCompatActivity() {

    @BindView(R.id.logRV) lateinit var logs: RecyclerView

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    val logsList: MutableList<Log> = mutableListOf()
    val logsAdapter: LogsAdapter = LogsAdapter(logsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        ButterKnife.bind(this)

        val llm = LinearLayoutManager(applicationContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        logs.layoutManager = llm
        logs.adapter = logsAdapter

        showLogs("1", "0", "0", "0", "0", "0")

        findViewById<AppCompatRadioButton>(R.id.timeRB).setOnClickListener {
            showLogs("1", "0", "0", "0", "0", "0")
        }

        findViewById<AppCompatRadioButton>(R.id.eventRB).setOnClickListener {
            showLogs("0", "1", "0", "0", "0", "0")
        }

        findViewById<AppCompatRadioButton>(R.id.userIdRB).setOnClickListener {
            showLogs("0", "0", "1", "0", "0", "0")
        }

        findViewById<AppCompatRadioButton>(R.id.userEmailRB).setOnClickListener {
            showLogs("0", "0", "0", "1", "0", "0")
        }

        findViewById<AppCompatRadioButton>(R.id.userNicknameRB).setOnClickListener {
            showLogs("0", "0", "0", "0", "1", "0")
        }

        findViewById<AppCompatRadioButton>(R.id.userCategoryRB).setOnClickListener {
            showLogs("0", "0", "0", "0", "0", "1")
        }
    }

    private fun showLogs(time:String, event:String, userId:String, userEmail:String, userNickname:String, userCategory:String){
        logsList.clear()
        logsAdapter.notifyData(logsList)
            compositeDisposable.add(
                    repository.showLogs(time, event, userId, userEmail, userNickname, userCategory)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({result ->
                                if(!result[0].time.equals("")) {
                                    logsList.addAll(result)
                                    logsAdapter.notifyData(logsList)
                                }else{
                                    logsList.clear()
                                    logsAdapter.notifyData(logsList)
                                }
                            })
            )
    }
}