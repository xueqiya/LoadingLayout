package com.xueqiya.loading.demo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xueqiya.loading.LoadingLayout
import com.xueqiya.loading.LoadingLayout.Companion.wrap

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var loading: LoadingLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.list).setOnClickListener(this)
        findViewById<View>(R.id.another).setOnClickListener(this)
        loading = wrap(this)
        loading.showContent()
        loading.setRetryListener(View.OnClickListener { v ->
            Toast.makeText(v.context, "retry", Toast.LENGTH_LONG).show()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_empty -> {
                loading.showEmpty()
                return true
            }
            R.id.action_loading -> {
                loading.showLoading()
                return true
            }
            R.id.action_content -> {
                loading.showContent()
                return true
            }
            R.id.action_error -> {
                loading.showError()
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.loading, menu)
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.list -> startActivity(Intent(v.context, ListActivity::class.java))
        }
        when (v.id) {
            R.id.another -> startActivity(Intent(v.context, LoadingActivity::class.java))
        }
    }
}