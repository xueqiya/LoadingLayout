package com.xueqiya.loading

import android.content.Context

object DensityUtils {

    fun dip2px(mContext: Context, dpValue: Float): Int {
        val scale = mContext.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dip(mContext: Context, pxValue: Float): Int {
        val scale = mContext.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}
