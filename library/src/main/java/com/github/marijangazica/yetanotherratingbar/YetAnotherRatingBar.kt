package com.github.marijangazica.yetanotherratingbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout


/**
 * Created by Marijan Gazica on 10/03/2018
 */
open class YetAnotherRatingBar(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    /**
     * @property halfMark - drawable which will be displayed as half mark if
     * [mode] is 0 (half mark enabled) and fraction part of [rating] is
     * bigger than [halfMarkBottomThreshold] and
     * smaller than [halfMarkTopThreshold] (only when half mark mode is enabled)
     */
    var halfMark: Drawable

    /**
     * @property emptyMark - drawable which will be displayed as empty mark
     */
    var emptyMark: Drawable

    /**
     * @property fullMark - drawable which will be displayed as full mark
     */
    var fullMark: Drawable

    /**
     * @property mode - marks the mode of [rating] display
     * 0 -> half mode enabled
     * 1 -> half mode disabled
     */
    private var mode: Int = 0

    /**
     * @property halfMarkBottomThreshold - marks at which threshold will [rating]
     * decimal part be shown as [halfMark] instead of [emptyMark]
     */
    var halfMarkBottomThreshold: Float = 0.25f
        set(newValue) {
            val newBottomThreshold = newValue % 1
            if (newBottomThreshold > halfMarkTopThreshold) halfMarkTopThreshold = newBottomThreshold
            field = newBottomThreshold
            refreshMarks()
        }

    /**
     * @property halfMarkTopThreshold - marks at which threshold will [rating]
     * decimal part be shown as [fullMark] instead of [halfMark]
     */
    var halfMarkTopThreshold: Float = 0.75f
        set(newValue) {
            val newTopThreshold = newValue % 1
            if (newTopThreshold < halfMarkBottomThreshold) halfMarkBottomThreshold = newTopThreshold
            field = newTopThreshold
            refreshMarks()
        }

    /**
     * @property fullMarkThreshold - marks at which threshold will [rating]
     * decimal part be shown as [fullMark] instead of [emptyMark]
     */
    var fullMarkThreshold: Float = 0.5f
        set(newValue) {
            field = newValue % 1
            refreshMarks()
        }

    /**
     * @property maxRating - sets the maximum [rating] that can be displayed
     */
    var maxRating: Int = 5
        set(newValue) {
            field = newValue
            refreshMarks()
            onRatingChanged(rating, newValue)
        }

    /**
     * @property rating - sets the rating which will be displayed according to display mode
     */
    var rating: Float = 0f
        set(newValue) {
            field = newValue
            setMarkIcons()
            onRatingChanged(newValue, maxRating)
        }

    /**
     * @property ratingEditable - marks if user clicks will change [rating] displayed
     */
    var ratingEditable: Boolean = false

    /**
     * @property onRatingChanged - listener for rating bar value changes
     */
    var onRatingChanged: (Float, Int) -> Unit = { rating: Float, max: Int -> }

    /**
     * @property drawableLeftMargin (in pixels) - sets left margin for all marks in view
     */
    var drawableLeftMargin: Float = 0f
        set(newValue) {
            field = newValue
            reloadMarkMargins()
        }

    /**
     * @property drawableRightMargin (in pixels) - sets right margin for all marks in view
     */
    var drawableRightMargin: Float = 0f
        set(newValue) {
            field = newValue
            reloadMarkMargins()
        }

    /**
     * @property drawableTopMargin (in pixels) - sets top margin for all marks in view
     */
    var drawableTopMargin: Float = 0f
        set(newValue) {
            field = newValue
            reloadMarkMargins()
        }

    /**
     * @property drawableBottomMargin (in pixels) sets bottom margin for all marks in view
     */
    var drawableBottomMargin: Float = 0f
        set(newValue) {
            field = newValue
            reloadMarkMargins()
        }

    init {
        orientation = LinearLayout.HORIZONTAL
        val a = context.obtainStyledAttributes(attrs, R.styleable.YetAnotherRatingBar)
        halfMark = a.getDrawable(R.styleable.YetAnotherRatingBar_yarb_half_mark) ?: context.resources.getDrawable(R.drawable.half_star)
        emptyMark = a.getDrawable(R.styleable.YetAnotherRatingBar_yarb_empty_mark) ?: context.resources.getDrawable(R.drawable.empty_star)
        fullMark = a.getDrawable(R.styleable.YetAnotherRatingBar_yarb_full_mark) ?: context.resources.getDrawable(R.drawable.full_star)
        halfMarkBottomThreshold = a.getFloat(R.styleable.YetAnotherRatingBar_yarb_half_mark_bottom_threshold, 0.25f)
        halfMarkTopThreshold = a.getFloat(R.styleable.YetAnotherRatingBar_yarb_half_mark_top_threshold, 0.75f)
        fullMarkThreshold = a.getFloat(R.styleable.YetAnotherRatingBar_yarb_full_mark_threshold, 0.5f)
        mode = a.getInt(R.styleable.YetAnotherRatingBar_yarb_mode, 0)
        maxRating = a.getInt(R.styleable.YetAnotherRatingBar_yarb_max, 5)
        rating = a.getFloat(R.styleable.YetAnotherRatingBar_yarb_rating, 0f)
        ratingEditable = a.getBoolean(R.styleable.YetAnotherRatingBar_yarb_clickable, false)
        drawableLeftMargin = a.getDimension(R.styleable.YetAnotherRatingBar_yarb_drawable_left_margin, 0f)
        drawableRightMargin = a.getDimension(R.styleable.YetAnotherRatingBar_yarb_drawable_right_margin, 0f)
        drawableTopMargin = a.getDimension(R.styleable.YetAnotherRatingBar_yarb_drawable_top_margin, 0f)
        drawableBottomMargin = a.getDimension(R.styleable.YetAnotherRatingBar_yarb_drawable_bottom_margin, 0f)
        addMarkViews(context)
        a.recycle()
    }

    private fun addMarkViews(context: Context) {
        for (i in 0 until maxRating) {
            val imageView = getRankingDrawable(context)

            imageView.setOnClickListener {
                if (ratingEditable) {
                    rating = (indexOfChild(it) + 1).toFloat()
                }
            }
            addView(imageView)
        }
        setMarkIcons()
    }

    private fun getRankingDrawable(context: Context): ImageView {
        val imageView = ImageView(context)
        val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(Math.round(drawableLeftMargin), Math.round(drawableTopMargin), Math.round(drawableRightMargin), Math.round(drawableBottomMargin))
        imageView.layoutParams = params
        return imageView
    }

    private fun reloadMarkMargins() {
        for (i in 0 until childCount) {
            val imageView = getChildAt(i) as ImageView
            val params = imageView.layoutParams as LinearLayout.LayoutParams
            params.setMargins(Math.round(drawableLeftMargin), Math.round(drawableTopMargin), Math.round(drawableRightMargin), Math.round(drawableBottomMargin))
            imageView.layoutParams = params
        }
    }

    private fun setMarkIcons() {
        for (position in 0 until childCount) {
            val view = getChildAt(position) as ImageView

            // only full marks mode
            if (mode == 1) {
                if (rating >= position + fullMarkThreshold) {
                    view.setImageDrawable(fullMark)
                } else {
                    view.setImageDrawable(emptyMark)
                }
            }

            // half marks mode enabled
            if (mode == 0) {
                if (rating >= position + 1) {
                    view.setImageDrawable(fullMark)
                } else if (rating - position > halfMarkBottomThreshold && rating - position < halfMarkTopThreshold) {
                    view.setImageDrawable(halfMark)
                } else if (rating - position <= halfMarkBottomThreshold) {
                    view.setImageDrawable(emptyMark)
                } else if (rating - position >= halfMarkTopThreshold) {
                    view.setImageDrawable(fullMark)
                }
            }
        }
    }

    /**
     * redraws all marks
     */
    private fun refreshMarks() {
        if (childCount > 0) {
            val context = getChildAt(0).context
            removeAllViews()
            addMarkViews(context)
        }
    }

    /**
     * @param halfMarkEnabled - defines if half marks will be used
     */
    fun setHalfMarksEnabledMode(halfMarkEnabled: Boolean) {
        this.mode = if (halfMarkEnabled) 0 else 1
    }
}
