package edu.swin.rentacar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import edu.swin.rentacar.model.Car

class DetailActivity : AppCompatActivity() {

    private lateinit var img: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var rating: RatingBar
    private lateinit var tvKm: TextView
    private lateinit var tvDaily: TextView
    private lateinit var tvDays: TextView
    private lateinit var sliderDays: Slider
    private lateinit var btnBack: Button
    private lateinit var btnSave: Button

    private lateinit var car: Car
    private var balance: Int = 500
    private val perBookingLimit = 400

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        img = findViewById(R.id.imgCarDetail)
        tvTitle = findViewById(R.id.tvTitle)
        tvSubtitle = findViewById(R.id.tvSubtitle)
        rating = findViewById(R.id.ratingBarDetail)
        tvKm = findViewById(R.id.tvKmDetail)
        tvDaily = findViewById(R.id.tvDailyCost)
        tvDays = findViewById(R.id.tvDays)
        sliderDays = findViewById(R.id.sliderDays)
        btnBack = findViewById(R.id.btnBack)
        btnSave = findViewById(R.id.btnSave)

        car = intent.getParcelableExtra(MainActivity.EXTRA_CAR)!!
        balance = intent.getIntExtra("balance", 500)

        img.setImageResource(car.imageRes)
        tvTitle.text = "${car.name} ${car.model}"
        tvSubtitle.text = "${car.year}"
        rating.rating = car.rating
        tvKm.text = getString(R.string.km_fmt, car.kilometres)
        tvDaily.text = getString(R.string.cost_per_day_fmt, car.dailyCost)

        // Ensure slider is 1..7 and label shows current
        sliderDays.value = 1f
        tvDays.text = sliderDays.value.toInt().toString()
        sliderDays.addOnChangeListener { _, value, _ ->
            tvDays.text = value.toInt().toString()
        }

        btnBack.setOnClickListener {
            setResult(RESULT_OK, Intent().apply {
                putExtra(MainActivity.EXTRA_CAR, car)
                putExtra(MainActivity.EXTRA_DAYS, 1)
                putExtra(MainActivity.EXTRA_RENTED, false)
            })
            finish()
        }

        btnSave.setOnClickListener {
            val days = sliderDays.value.toInt()
            val total = car.dailyCost * days

            if (total > perBookingLimit) {
                Toast.makeText(this, getString(R.string.err_limit_400), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (total > balance) {
                Toast.makeText(this, getString(R.string.err_insufficient), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            setResult(RESULT_OK, Intent().apply {
                putExtra(MainActivity.EXTRA_CAR, car)
                putExtra(MainActivity.EXTRA_DAYS, days)
                putExtra(MainActivity.EXTRA_RENTED, true)
            })
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_OK, Intent().apply {
            putExtra(MainActivity.EXTRA_CAR, car)
            putExtra(MainActivity.EXTRA_DAYS, 1)
            putExtra(MainActivity.EXTRA_RENTED, false)
        })
        finish()
    }
}
