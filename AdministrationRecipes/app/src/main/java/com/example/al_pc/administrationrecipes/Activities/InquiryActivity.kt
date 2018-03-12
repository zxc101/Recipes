package com.example.al_pc.administrationrecipes.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.al_pc.administrationrecipes.Adapters.InquiryAdapter
import com.example.al_pc.administrationrecipes.R
import com.example.al_pc.administrationrecipes.data.Inquiry
import com.example.al_pc.administrationrecipes.data.SearchRepository
import com.example.al_pc.administrationrecipes.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class InquiryActivity:AppCompatActivity() {

    @BindView(R.id.inquiryRV) lateinit var inquiries: RecyclerView

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    val inquiriesList: MutableList<Inquiry> = mutableListOf()
    val inquiriesAdapter: InquiryAdapter = InquiryAdapter(inquiriesList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inquiry)
        ButterKnife.bind(this)

        val llm = LinearLayoutManager(applicationContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        inquiries.layoutManager = llm
        inquiriesAdapter.context = this
        inquiries.adapter = inquiriesAdapter

        compositeDisposable.add(
                repository.showInquiry()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({result ->
                            if(!result[0].email.equals("")) {
                                inquiriesList.addAll(result)
                                inquiriesAdapter.notifyData(inquiriesList)
                            }else{
                                inquiriesList.clear()
                                inquiriesAdapter.notifyData(inquiriesList)
                            }
                        })
        )
    }
}