package app.messenger.slide.ui.core

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

import androidx.databinding.BindingAdapter

object DataBindingAdapters {
    @JvmStatic
    @BindingAdapter("layout_constraintHorizontal_bias")
    fun setLayoutConstraintGuideBegin(view: TextView, percent: Float) {
        setHorizontalBias(view, percent)
    }

    @JvmStatic
    @BindingAdapter("layout_constraintHorizontal_bias")
    fun setLayoutConstraintGuideBegin(view: CardView, percent: Float) {
        setHorizontalBias(view, percent)
    }

    @JvmStatic
    @BindingAdapter("android:layout_gravity")
    fun setLayoutGravity(view: TextView, percent: Float) {
        setGravity(view, percent)
    }

    @JvmStatic
    @BindingAdapter("android:layout_gravity")
    fun setLayoutGravity(view: CardView, percent: Float) {
        setGravity(view, percent)
    }

    @JvmStatic
    @BindingAdapter("app:set_box_color")
    fun setBoxColor(view: CardView, color: Int) {
        view.setCardBackgroundColor(ContextCompat.getColor(view.context, color))
    }

    private fun setGravity(view: View, percent: Float) {
        val params =
            view.layoutParams as LinearLayout.LayoutParams
        params.gravity = if (percent == 1f) Gravity.START else Gravity.END
        view.layoutParams = params
    }

    private fun setHorizontalBias(view: View, percent: Float) {
        val params =
            view.layoutParams as ConstraintLayout.LayoutParams
        params.horizontalBias = percent
        view.layoutParams = params
    }
}