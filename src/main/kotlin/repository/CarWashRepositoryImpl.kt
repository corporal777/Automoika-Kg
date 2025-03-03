package kg.automoika.repository

import com.mongodb.MongoException
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kg.automoika.data.CarWashModel
import kg.automoika.data.CarWashRemote
import org.bson.BsonValue

class CarWashRepositoryImpl(private val database: MongoDatabase) : CarWashRepository {

    companion object {
        const val CAR_WASH_COLLECTION = "car-wash"
    }

    override suspend fun createCarWash(model: CarWashModel): BsonValue? {
        try {
            val result = database.getCollection<CarWashRemote>(CAR_WASH_COLLECTION).insertOne(model.toRemote())
            return result.insertedId
        } catch (e: MongoException) {
            System.err.println("Unable to insert due to an error: $e")
        }
        return null
    }

    override suspend fun getCarWashList(): List<CarWashRemote> {
        TODO("Not yet implemented")
    }

}