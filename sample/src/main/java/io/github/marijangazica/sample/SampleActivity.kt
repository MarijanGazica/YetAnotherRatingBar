package io.github.marijangazica.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import io.github.marijangazica.yetanotherratingbar.YetAnotherRatingBar

class SampleActivity : AppCompatActivity() {

    lateinit var bar: YetAnotherRatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        bar = findViewById(R.id.rating_bar)
        val inc = findViewById<View>(R.id.rating_increase)
        val dec = findViewById<View>(R.id.rating_decrease)

        bar.rating = 3.5f

        inc.onClick {
            bar.rating = (bar.rating + 0.2f)
            Toast.makeText(this@SampleActivity, "Rating: " + bar.rating + "/" + bar.maxRating, Toast.LENGTH_SHORT).show()
        }
        dec.onClick {
            bar.rating = (bar.rating - 0.2f)
            Toast.makeText(this@SampleActivity, "Rating: " + bar.rating + "/" + bar.maxRating, Toast.LENGTH_SHORT).show()
        }
    }

}