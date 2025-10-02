package edu.swin.rentacar.data

import edu.swin.rentacar.R
import edu.swin.rentacar.model.Car

object CarRepository {
    /** In-memory cars (5 total). Replace images with your own drawables. */
    val cars: MutableList<Car> = mutableListOf(
        Car(
            id = "1", name = "Toyota", model = "Corolla", year = 2020,
            rating = 4.5f, kilometres = 42000, dailyCost = 55,
            imageRes = R.drawable.car_corolla
        ),
        Car(
            id = "2", name = "Subaru", model = "Impreza", year = 2019,
            rating = 4.2f, kilometres = 58000, dailyCost = 50,
            imageRes = R.drawable.car_impreza
        ),
        Car(
            id = "3", name = "Honda", model = "Civic", year = 2021,
            rating = 4.6f, kilometres = 30000, dailyCost = 60,
            imageRes = R.drawable.car_civic
        ),
        Car(
            id = "4", name = "Toyota", model = "Camry", year = 2018,
            rating = 4.1f, kilometres = 73000, dailyCost = 48,
            imageRes = R.drawable.car_camry
        ),
        Car(
            id = "5", name = "Tesla", model = "Model 3", year = 2022,
            rating = 4.8f, kilometres = 18000, dailyCost = 85,
            imageRes = R.drawable.car_model3
        )
    )
}
