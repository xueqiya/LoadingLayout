package com.xueqiya.loading.demo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xueqiya.loading.LoadingLayout

class XmlActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var loading: LoadingLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xml)
        initView()
        initLoading()
    }

    private fun initView() {
        val actionLoading = findViewById<View>(R.id.action_loading)
        val actionContent = findViewById<View>(R.id.action_content)
        val actionEmpty = findViewById<View>(R.id.action_empty)
        val actionError = findViewById<View>(R.id.action_error)
        actionContent.setOnClickListener(this)
        actionEmpty.setOnClickListener(this)
        actionLoading.setOnClickListener(this)
        actionError.setOnClickListener(this)
    }

    private fun initLoading() {
        loading = findViewById(R.id.loading)
        loading.setRetryListener {
            Toast.makeText(this, "retry", Toast.LENGTH_LONG).show()
        }
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