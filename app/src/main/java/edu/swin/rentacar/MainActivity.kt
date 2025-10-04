package edu.swin.rentacar

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.materialswitch.MaterialSwitch
import edu.swin.rentacar.data.CarRepository
import edu.swin.rentacar.model.Car
import edu.swin.rentacar.model.Rental

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var tvBalance: TextView
    private lateinit var switchTheme: MaterialSwitch
    private lateinit var searchView: SearchView

    private lateinit var imgCar: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvModelYear: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var tvKm: TextView
    private lateinit var tvCost: TextView
    private lateinit var btnFavorite: ImageButton
    private lateinit var btnNext: Button
    private lateinit var btnRent: Button

    private lateinit var rvFavorites: RecyclerView
    private lateinit var favAdapter: FavoriteAdapter

    private lateinit var rvRentals: RecyclerView
    private lateinit var rentedAdapter: RentedAdapter
    private val rentals = mutableListOf<Rental>()

    private var currentIndex = 0
    private var visibleCars: MutableList<Car> = CarRepository.cars.toMutableList()
    private var creditBalance = 500

    companion object {
        const val REQ_DETAIL = 1001
        const val EXTRA_CAR = "extra_car"
        const val EXTRA_DAYS = "extra_days"
        const val EXTRA_RENTED = "extra_rented"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        tvBalance = findViewById(R.id.tvBalance)
        switchTheme = findViewById(R.id.switchTheme)
        searchView = findViewById(R.id.searchView)

        imgCar = findViewById(R.id.imgCar)
        tvName = findViewById(R.id.tvName)
        tvModelYear = findViewById(R.id.tvModelYear)
        ratingBar = findViewById(R.id.ratingBar)
        tvKm = findViewById(R.id.tvKm)
        tvCost = findViewById(R.id.tvCost)

        btnFavorite = findViewById(R.id.btnFavorite)
        btnNext = findViewById(R.id.btnNext)
        btnRent = findViewById(R.id.btnRent)

        // Favourites
        rvFavorites = findViewById(R.id.rvFavorites)
        favAdapter = FavoriteAdapter(mutableListOf()) { car ->
            val pos = visibleCars.indexOfFirst { it.id == car.id }
            if (pos >= 0) {
                currentIndex = pos
                showCar(visibleCars[currentIndex])
            }
        }
        rvFavorites.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvFavorites.adapter = favAdapter

        // My Rentals
        rvRentals = findViewById(R.id.rvRentals)
        rvRentals.layoutManager = LinearLayoutManager(this)
        rentedAdapter = RentedAdapter(rentals)
        rvRentals.adapter = rentedAdapter

        // Swipe to remove from My Rentals (history clean-up only)
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView, vh: RecyclerView.ViewHolder, tgt: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val pos = vh.adapterPosition
                rentedAdapter.removeAt(pos)
                Toast.makeText(this@MainActivity, "Removed from My Rentals", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(rvRentals)

        // Swipe to remove from Favorites
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView, vh: RecyclerView.ViewHolder, tgt: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val pos = vh.adapterPosition
                favAdapter.removeAt(pos)
                Toast.makeText(this@MainActivity, "Removed from favourites", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(rvFavorites)

        refreshBalance()

        if (visibleCars.isNotEmpty()) showCar(visibleCars[currentIndex]) else clearCard()
        updateButtonsEnabled()

        // Search by name/model
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val q = (newText ?: "").trim().lowercase()
                visibleCars = if (q.isEmpty()) {
                    CarRepository.cars.toMutableList()
                } else {
                    CarRepository.cars.filter {
                        it.name.lowercase().contains(q) || it.model.lowercase().contains(q)
                    }.toMutableList()
                }
                currentIndex = 0
                if (visibleCars.isNotEmpty()) showCar(visibleCars[currentIndex]) else clearCard()
                updateButtonsEnabled()
                return true
            }
        })

        // Dark mode toggle
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked)
                androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
            else
                androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(mode)
        }

        btnNext.setOnClickListener {
            if (visibleCars.isEmpty()) return@setOnClickListener
            currentIndex = (currentIndex + 1) % visibleCars.size
            showCar(visibleCars[currentIndex])
        }

        // ❤️ Heart now toggles favourites (add/remove)
        btnFavorite.setOnClickListener {
            if (visibleCars.isEmpty()) return@setOnClickListener
            val car = visibleCars[currentIndex]
            if (favAdapter.contains(car.id)) {
                favAdapter.remove(car)
                Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show()
            } else {
                favAdapter.addUnique(car)
                Toast.makeText(this, getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show()
            }
        }

        btnRent.setOnClickListener {
            if (visibleCars.isEmpty()) return@setOnClickListener
            val car = visibleCars[currentIndex]
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(EXTRA_CAR, car)
            intent.putExtra("balance", creditBalance)
            startActivityForResult(intent, REQ_DETAIL)
        }
    }

    private fun refreshBalance() {
        tvBalance.text = getString(R.string.balance_fmt, creditBalance)
    }

    private fun formatKm(km: Int) = getString(R.string.km_fmt, km)

    private fun showCar(car: Car) {
        imgCar.setImageResource(car.imageRes)
        imgCar.contentDescription = "${car.name} ${car.model} ${car.year}"
        tvName.text = car.name
        tvModelYear.text = "${car.model} • ${car.year}"
        ratingBar.rating = car.rating
        tvKm.text = formatKm(car.kilometres)
        tvCost.text = getString(R.string.cost_per_day_fmt, car.dailyCost)
    }

    private fun clearCard() {
        imgCar.setImageResource(R.drawable.car_placeholder)
        imgCar.contentDescription = null
        tvName.text = ""
        tvModelYear.text = ""
        ratingBar.rating = 0f
        tvKm.text = ""
        tvCost.text = ""
    }

    private fun updateButtonsEnabled() {
        val enabled = visibleCars.isNotEmpty()
        btnRent.isEnabled = enabled
        btnNext.isEnabled = enabled
        btnFavorite.isEnabled = enabled
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_rating -> visibleCars.sortByDescending { it.rating }
            R.id.action_sort_year -> visibleCars.sortByDescending { it.year }
            R.id.action_sort_cost -> visibleCars.sortBy { it.dailyCost }
            else -> return super.onOptionsItemSelected(item)
        }
        currentIndex = 0
        if (visibleCars.isNotEmpty()) showCar(visibleCars[currentIndex]) else clearCard()
        updateButtonsEnabled()
        return true
    }

    @Deprecated("Using legacy result API for simplicity; optional to migrate to Activity Result API")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_DETAIL && resultCode == RESULT_OK && data != null) {
            val rented = data.getBooleanExtra(EXTRA_RENTED, false)
            val days = data.getIntExtra(EXTRA_DAYS, 1)
            val car = data.getParcelableExtra<Car>(EXTRA_CAR) ?: return

            if (rented) {
                val total = car.dailyCost * days
                creditBalance -= total
                Toast.makeText(this, getString(R.string.booked), Toast.LENGTH_SHORT).show()

                // Remove from lists of available cars
                CarRepository.cars.removeAll { it.id == car.id }
                visibleCars.removeAll { it.id == car.id }
                favAdapter.remove(car) // also stop showing in favourites

                // Add to My Rentals history
                rentedAdapter.add(Rental(car, days, total))

                if (visibleCars.isNotEmpty()) {
                    currentIndex %= visibleCars.size
                    showCar(visibleCars[currentIndex])
                } else {
                    clearCard()
                }
                refreshBalance()
                updateButtonsEnabled()
            } else {
                Toast.makeText(this, getString(R.string.not_booked), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
