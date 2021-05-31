package com.xueqiya.loading

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import java.util.*

class LoadingLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.styleLoadingLayout
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        @JvmStatic
        fun wrap(activity: Activity?): LoadingLayout {
            if (activity == null) {
                throw RuntimeException("content activity can not be null")
            }
            val viewGroup = activity.findViewById<View>(android.R.id.content) as ViewGroup
            val view = viewGroup.getChildAt(0)
            return wrap(view)
        }

        @JvmStatic
        fun wrap(fragment: Fragment?): LoadingLayout {
            if (fragment == null) {
                throw RuntimeException("content fragment can not be null")
            }
            val view = fragment.view
            return wrap(view)
        }

        @JvmStatic
        fun wrap(view: View?): LoadingLayout {
            if (view == null) {
                throw RuntimeException("content view can not be null")
            }
            val parent = view.parent as ViewGroup
            val lp = view.layoutParams
            val index = parent.indexOfChild(view)
            parent.removeView(view)
            val layout = LoadingLayout(view.context)
            parent.addView(layout, index, lp)
            layout.addView(view)
            layout.setContentView(view)
            return layout
        }
    }

    private var mEmptyImageResId: Int
    private var mEmptyText: CharSequence
    private var mErrorImageResId: Int
    private var mRetryText: CharSequence
    private var mRetryListener: OnClickListener? = null
    private var mOnEmptyInflateListener: OnInflateListener? = null
    private var mOnErrorInflateListener: OnInflateListener? = null
    private var mTextColor: Int
    private var mTextSize: Int
    private var mButtonTextColor: Int
    private var mButtonTextSize: Int
    private var mButtonBackground: Drawable
    private var mEmptyResId = View.NO_ID
    private var mLoadingResId = View.NO_ID
    private var mErrorResId = View.NO_ID
    private var mContentId = View.NO_ID
    private var pageState: State = State.CONTENT
    private var mLayouts: MutableMap<Int, View?> = HashMap()
    private var mInflater: LayoutInflater = LayoutInflater.from(context)

    init {
        val arrayType = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout, defStyleAttr, R.style.LoadingLayout_Style)
        mEmptyImageResId = arrayType.getResourceId(R.styleable.LoadingLayout_llEmptyImage, View.NO_ID)
        mEmptyText = arrayType.getString(R.styleable.LoadingLayout_llEmptyText).toString()
        mErrorImageResId = arrayType.getResourceId(R.styleable.LoadingLayout_llErrorImage, View.NO_ID)
        mRetryText = arrayType.getString(R.styleable.LoadingLayout_llRetryText).toString()
        mTextColor = arrayType.getColor(R.styleable.LoadingLayout_llTextColor, -0x666667)
        mTextSize = arrayType.getDimensionPixelSize(R.styleable.LoadingLayout_llTextSize, DensityUtils.dip2px(context, 16f))
        mButtonTextColor = arrayType.getColor(R.styleable.LoadingLayout_llButtonTextColor, -0x666667)
        mButtonTextSize = arrayType.getDimensionPixelSize(R.styleable.LoadingLayout_llButtonTextSize, DensityUtils.dip2px(context, 16f))
        mButtonBackground = arrayType.getDrawable(R.styleable.LoadingLayout_llButtonBackground)!!
        mEmptyResId = arrayType.getResourceId(R.styleable.LoadingLayout_llEmptyResId, R.layout.loading_layout_empty)
        mLoadingResId = arrayType.getResourceId(R.styleable.LoadingLayout_llLoadingResId, R.layout.loading_layout_loading)
        mErrorResId = arrayType.getResourceId(R.styleable.LoadingLayout_llErrorResId, R.layout.loading_layout_error)
        arrayType.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount == 0) {
            return
        }
        if (childCount > 1) {
            removeViews(1, childCount - 1)
        }
        val view = getChildAt(0)
        setContentView(view)
        pageState = State.CONTENT
    }

    interface OnInflateListener {
        fun onInflate(inflated: View?)
    }

    private var mRetryButtonClickListener = OnClickListener { v ->
        mRetryListener?.onClick(v)
    }

    private fun setContentView(view: View) {
        mContentId = view.id
        mLayouts[mContentId] = view
    }

    fun setLoading(@LayoutRes id: Int): LoadingLayout {
        if (mLoadingResId != id) {
            remove(mLoadingResId)
            mLoadingResId = id
        }
        return this
    }

    fun setEmpty(@LayoutRes id: Int): LoadingLayout {
        if (mEmptyResId != id) {
            remove(mEmptyResId)
            mEmptyResId = id
        }
        return this
    }

    fun setOnEmptyInflateListener(listener: OnInflateListener): LoadingLayout {
        mOnEmptyInflateListener = listener
        if (mOnEmptyInflateListener != null && mLayouts.containsKey(mEmptyResId)) {
            listener.onInflate(mLayouts[mEmptyResId])
        }
        return this
    }

    fun setOnErrorInflateListener(listener: OnInflateListener): LoadingLayout {
        mOnErrorInflateListener = listener
        if (mOnErrorInflateListener != null && mLayouts.containsKey(mErrorResId)) {
            listener.onInflate(mLayouts[mErrorResId])
        }
        return this
    }

    fun setRetryListener(listener: OnClickListener?): LoadingLayout {
        mRetryListener = listener
        return this
    }

    fun setEmptyImage(@DrawableRes resId: Int): LoadingLayout {
        mEmptyImageResId = resId
        image(mEmptyResId, R.id.empty_image, mEmptyImageResId)
        return this
    }

    fun setEmptyText(value: String): LoadingLayout {
        mEmptyText = value
        text(mEmptyResId, R.id.empty_text, mEmptyText)
        return this
    }

    fun setErrorImage(@DrawableRes resId: Int): LoadingLayout {
        mErrorImageResId = resId
        image(mErrorResId, R.id.error_image, mErrorImageResId)
        return this
    }

    fun setRetryText(text: String): LoadingLayout {
        mRetryText = text
        text(mErrorResId, R.id.retry_button, mRetryText)
        return this
    }

    fun showLoading() {
        pageState = State.LOADING
        show(mLoadingResId)
    }

    fun showEmpty() {
        pageState = State.EMPTY
        show(mEmptyResId)
    }

    fun showError() {
        pageState = State.ERROR
        show(mErrorResId)
    }

    fun showContent() {
        pageState = State.CONTENT
        show(mContentId)
    }

    fun getLoadingState(): State {
        return pageState
    }

    private fun show(layoutId: Int) {
        for (view in mLayouts.values) {
            view!!.visibility = View.GONE
        }
        layout(layoutId)!!.visibility = View.VISIBLE
    }

    private fun remove(layoutId: Int) {
        if (mLayouts.containsKey(layoutId)) {
            val vg = mLayouts.remove(layoutId)
            removeView(vg)
        }
    }

    private fun layout(layoutId: Int): View? {
        if (mLayouts.containsKey(layoutId)) {
            return mLayouts[layoutId]
        }
        val layout = mInflater.inflate(layoutId, this, false)
        layout.visibility = View.GONE
        addView(layout)
        mLayouts[layoutId] = layout
        if (layoutId == mEmptyResId) {
            val img = layout.findViewById<ImageView>(R.id.empty_image)
            if (img != null && mEmptyImageResId != View.NO_ID) {
                img.setImageResource(mEmptyImageResId)
            }
            val view = layout.findViewById<TextView>(R.id.empty_text)
            if (view != null) {
                view.text = mEmptyText
                view.setTextColor(mTextColor)
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
            }
            if (mOnEmptyInflateListener != null) {
                mOnEmptyInflateListener!!.onInflate(layout)
            }
        } else if (layoutId == mErrorResId) {
            val img = layout.findViewById<ImageView>(R.id.error_image)
            if (img != null && mErrorImageResId != View.NO_ID) {
                img.setImageResource(mErrorImageResId)
            }
            val btn = layout.findViewById<TextView>(R.id.retry_button)
            if (btn != null) {
                btn.text = mRetryText
                btn.setTextColor(mButtonTextColor)
                btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, mButtonTextSize.toFloat())
                btn.background = mButtonBackground
                btn.setOnClickListener(mRetryButtonClickListener)
            }
            if (mOnErrorInflateListener != null) {
                mOnErrorInflateListener!!.onInflate(layout)
            }
        }
        return layout
    }

    private fun text(layoutId: Int, ctrlId: Int, value: CharSequence?) {
        if (mLayouts.containsKey(layoutId)) {
            val view = mLayouts[layoutId]!!.findViewById<TextView>(ctrlId)
            if (view != null) {
                view.text = value
            }
        }
    }

    private fun image(layoutId: Int, ctrlId: Int, resId: Int) {
        if (mLayouts.containsKey(layoutId)) {
            val view = mLayouts[layoutId]!!.findViewById<ImageView>(ctrlId)
            view?.setImageResource(resId)
        }
    }
}