package io.github.marijangazica.yetanotherratingbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout


/**
 * Created by Marijan on 13/01/2017
 */

class YetAnotherRatingBar(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    /**
     * @param halfMark - drawable which will be displayed if fraction part of rating is bigger than .25 and smaller than .75 (only when half mark mode is enabled)
     */
    var halfMark: Drawable

    /**
     * @param emptyMark - drawable which will be displayed as empty mark
     */
    var emptyMark: Drawable

    /**
     * @param fullMark - drawable which will be displayed as full mark
     */
    var fullMark: Drawable

    /**
     * @param mode - marks the mode of value display
     * 0 -> half mode enabled
     * 1 -> half mode disabled
     */
    private var mode: Int = 0

    /**
     * @param maxRating - sets the maximum rating that can be displayed
     */
    var maxRating: Int = 5
        set(newValue) {
            field = newValue
            refreshMarks()
        }

    /**
     * @param newRating - sets the rating which will be displayed according to display mode
     */
    var rating: Float = 0f
        set(newValue) {
            field = newValue
            setMarkIcons()
        }

    /**
     * @param ratingEditable - marks if user clicks will change rating according to icon clicked
     */
    var ratingEditable: Boolean = false

    /**
     * @param clickListener - listener for users tap input
     */
    private var clickListener: (Int, Int) -> Unit = { rating: Int, max: Int -> }


    /**
     * @param drawableLeftMargin (in pixels) - sets left margin for all marks in view
     */
    var drawableLeftMargin: Float = 0f
        set(newValue) {
            field = newValue
            reloadMarkMargins()
        }

    /**
     * @param drawableRightMargin (in pixels) - sets right margin for all marks in view
     */
    private var drawableRightMargin: Float = 0f
        set(newValue) {
            field = newValue
            reloadMarkMargins()
        }

    /**
     * @param drawableTopMargin (in pixels) - sets top margin for all marks in view
     */
    private var drawableTopMargin: Float = 0f
        set(newValue) {
            field = newValue
            reloadMarkMargins()
        }

    /**
     * @param drawableBottomMargin (in pixels) sets bottom margin for all marks in view
     */
    private var drawableBottomMargin: Float = 0f
        set(newValue) {
            field = newValue
            reloadMarkMargins()
        }

    init {
        orientation = LinearLayout.HORIZONTAL
        val a = context.obtainStyledAttributes(attrs, R.styleable.YetAnotherRatingBar)
        halfMark = a.getDrawable(R.styleable.YetAnotherRatingBar_yarb_half_mark)
        emptyMark = a.getDrawable(R.styleable.YetAnotherRatingBar_yarb_empty_mark)
        fullMark = a.getDrawable(R.styleable.YetAnotherRatingBar_yarb_full_mark)
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
                clickListener(indexOfChild(it) + 1, maxRating)
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
                if (rating >= position + 0.5) {
                    view.setImageDrawable(fullMark)
                } else {
                    view.setImageDrawable(emptyMark)
                }
            }

            // half marks mode enabled
            if (mode == 0) {
                if (rating >= position + 1) {
                    view.setImageDrawable(fullMark)
                } else if (rating - position > 0.25 && rating - position < 0.75) {
                    view.setImageDrawable(halfMark)
                } else if (rating - position <= 0.25) {
                    view.setImageDrawable(emptyMark)
                } else if (rating - position >= 0.75) {
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
     * @param halfMarkEnabled Sets if half marks will be used
     * true - half star if fraction part of rating is bigger than .25 and smaller than .75
     * false - full star added if fraction part of rating is bigger or equal to .5
     */
    fun setHalfMarksEnabledMode(halfMarkEnabled: Boolean) {
        this.mode = if (halfMarkEnabled) 0 else 1
    }

}
