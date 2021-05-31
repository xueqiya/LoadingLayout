package com.xueqiya.loading.demo

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xueqiya.loading.LoadingLayout.Companion.wrap

class ListActivity : AppCompatActivity() {
    private var mCount = 10
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        initView()
        initLoading()
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val refresh = findViewById<SwipeRefreshLayout>(R.id.refresh)
        refresh.setOnRefreshListener {
            mCount += 10
            adapter.notifyItemRangeChanged(mCount, mCount + 10)
            refresh.isRefreshing = false
        }
    }

    private fun initLoading() {
        val loading = wrap(recyclerView)
        loading.showContent()
        loading.setRetryListener {
            Toast.makeText(this, "retry", Toast.LENGTH_LONG).show()
        }
    }

    class ItemHolder(view: View?) : RecyclerView.ViewHolder(view!!)

    private val adapter = object : RecyclerView.Adapter<ItemHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view = TextView(parent.context)
            view.gravity = Gravity.CENTER
            view.setPadding(40, 40, 40, 40)
            return ItemHolder(view)
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            (holder.itemView as TextView).text = "text $position"
        }

        override fun getItemCount(): Int {
            return mCount
        }
    }
}