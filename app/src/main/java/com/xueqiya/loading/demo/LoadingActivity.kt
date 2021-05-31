package com.xueqiya.loading.demo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xueqiya.loading.LoadingLayout

class LoadingActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var loading: LoadingLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        findViewById<View>(R.id.action_content).setOnClickListener(this)
        findViewById<View>(R.id.action_empty).setOnClickListener(this)
        findViewById<View>(R.id.action_loading).setOnClickListener(this)
        findViewById<View>(R.id.action_error).setOnClickListener(this)
        loading = findViewById(R.id.loading)
        loading.showContent()
        loading.setRetryListener(View.OnClickListener { v ->
            Toast.makeText(v.context, "retry", Toast.LENGTH_LONG).show()
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.action_empty -> loading.showEmpty()
            R.id.action_loading -> loading.showLoading()
            R.id.action_content -> loading.showContent()
            R.id.action_error -> loading.showError()
        }
    }
}