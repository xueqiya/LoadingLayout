package com.xueqiya.loading.demo

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xueqiya.loading.LoadingLayout.Companion.wrap

class ListActivity : AppCompatActivity() {
    private var mCount = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val list = findViewById<RecyclerView>(R.id.list)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
        val loading = wrap(list)
        loading.showContent()
        loading.setRetryListener(View.OnClickListener { v ->
            Toast.makeText(v.context, "retry", Toast.LENGTH_LONG).show()
        })
        val refresh = findViewById<SwipeRefreshLayout>(R.id.refresh)
        refresh.setOnRefreshListener {
            mCount += 10
            list.adapter!!.notifyDataSetChanged()
            refresh.isRefreshing = false
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