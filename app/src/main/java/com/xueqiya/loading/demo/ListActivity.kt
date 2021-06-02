package com.xueqiya.loading.demo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xueqiya.loading.LoadingLayout
import com.xueqiya.loading.LoadingLayout.Companion.wrap

class ListActivity : AppCompatActivity() {
    private var mCount = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var loading: LoadingLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        initView()
        initLoading()
        Handler(Looper.getMainLooper()).postDelayed({
            loading.showContent()
        }, 1000)
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        mCount = 50
        adapter.notifyItemRangeChanged(0, mCount)
    }

    private fun initLoading() {
        loading = wrap(recyclerView)
        loading.showLoading()
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