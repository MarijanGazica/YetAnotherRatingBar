package com.github.marijangazica.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.github.marijangazica.yetanotherratingbar.YetAnotherRatingBar

class SampleActivity : AppCompatActivity() {

    lateinit var bar: YetAnotherRatingBar
    lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        bar = findViewById(R.id.rating_bar)
        status = findViewById(R.id.current_value)
        val inc = findViewById<View>(R.id.rating_increase)
        val dec = findViewById<View>(R.id.rating_decrease)

        bar.onRatingChanged = { _, _ ->
            status.text = String.format("%.2f/%d", bar.rating, bar.maxRating)
        }

        inc.onClick {
            bar.rating = (bar.rating + 0.1f)
        }
        dec.onClick {
            bar.rating = (bar.rating - 0.1f)
        }

        bar.rating = 3.5f
    }

}