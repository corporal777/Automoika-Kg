package kg.automoika.repository

import kg.automoika.data.CarWashModel
import kg.automoika.data.CarWashRemote
import org.bson.BsonValue

interface CarWashRepository {

    suspend fun createCarWash(model : CarWashModel) : BsonValue?
    suspend fun getCarWashList() : List<CarWashRemote>
}