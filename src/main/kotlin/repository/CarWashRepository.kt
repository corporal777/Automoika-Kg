package kg.automoika.repository

import kg.automoika.data.CarWashLocal
import kg.automoika.data.CarWashModel
import kg.automoika.data.CarWashRemote
import org.bson.BsonValue

interface CarWashRepository {

    suspend fun addTask() : CarWashLocal?
    suspend fun createCarWash(model : CarWashModel) : BsonValue?
    suspend fun getCarWashList() : List<CarWashRemote>
}