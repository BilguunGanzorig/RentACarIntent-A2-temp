package edu.swin.rentacar

object CarRepository {
    val cars: List<Car> = listOf(
        Car("1","Toyota Corolla","Ascent Sport",2020,4.5f,52300,70, R.drawable.car_placeholder),
        Car("2","Mazda 3","G20 Pure",2019,4.2f,68500,65, R.drawable.car_placeholder),
        Car("3","Hyundai i30","Active",2021,4.3f,41200,75, R.drawable.car_placeholder),
        Car("4","Kia Cerato","S",2018,4.0f,90500,50, R.drawable.car_placeholder),
        Car("5","Subaru Impreza","2.0i",2022,4.7f,21000,85, R.drawable.car_placeholder),
    )

    var creditBalance: Int = 500
        private set

    private val rentedIds = mutableSetOf<String>()
    private val favorites = linkedSetOf<String>()

    fun isAvailable(id: String) = !rentedIds.contains(id)
    fun availableCars(): List<Car> = cars.filter { isAvailable(it.id) }

    fun markFavorite(id: String, fav: Boolean) {
        if (fav) favorites.add(id) else favorites.remove(id)
    }
    fun isFavorite(id: String) = favorites.contains(id)
    fun favoritesList(): List<Car> = cars.filter { isFavorite(it.id) }

    fun canAfford(totalCost: Int): Boolean = totalCost <= 400 && totalCost <= creditBalance

    fun rent(carId: String, totalCost: Int): Boolean {
        if (!canAfford(totalCost)) return false
        if (!rentedIds.add(carId)) return false
        creditBalance -= totalCost
        return true
    }
}
