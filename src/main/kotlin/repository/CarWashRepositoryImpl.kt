package kg.automoika.repository

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.http.*
import kg.automoika.data.body.CarWashBody
import kg.automoika.data.body.CarWashFreeBoxesBody
import kg.automoika.data.remote.AccountRemote
import kg.automoika.data.remote.CarWashImageModel
import kg.automoika.data.remote.CarWashRemote
import kg.automoika.data.response.CarWashShortResponse
import kg.automoika.db.CarWashDatabase
import kg.automoika.extensions.hasText
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class CarWashRepositoryImpl(private val database: MongoDatabase, private val localDatabase: CarWashDatabase) :
    CarWashRepository {

    companion object {
        const val CAR_WASH_COLLECTION = "car-wash"
        const val ACCOUNTS_COLLECTION = "accounts"
    }

    override suspend fun updateCarWashBoxesState(model: CarWashFreeBoxesBody): Boolean {
        return localDatabase.updateCarWashFreeBoxes(model)
    }

    override suspend fun createCarWashPoint(model: CarWashBody, imagesList: List<CarWashImageModel>): CarWashRemote? {
        try {
            val remoteData = CarWashRemote.fromBody(model, imagesList)
            val dataCollection = database.getCollection<CarWashRemote>(CAR_WASH_COLLECTION)
            val accountsCollection = database.getCollection<AccountRemote>(ACCOUNTS_COLLECTION)

            val resultLocal = localDatabase.addSingleCarWashPoint(remoteData)
            if (!resultLocal) return null

            accountsCollection.insertOne(AccountRemote.setCarWashAccount(remoteData.id))
            val result = dataCollection.insertOne(remoteData)
            return if (result.wasAcknowledged()) remoteData else null
        } catch (e: MongoException) {
            System.err.println("Unable to insert due to an error: $e")
            return null
        }
    }



    override suspend fun getCarWashList(params: Parameters): List<CarWashShortResponse> {
        val localData = localDatabase.getCarWashListLocal()
        return if (localData.isNotEmpty()) localData.executeFilters(params)
        else database.getCollection<CarWashRemote>(CAR_WASH_COLLECTION)
            .find().toList().map { it.toResponse() }.executeFilters(params)
    }

    override suspend fun getCarWashById(id: String): CarWashRemote? {
        val local = localDatabase.getCarWashLocal(id)
        val dataCollection = database.getCollection<CarWashRemote>(CAR_WASH_COLLECTION)
        val remoteData = dataCollection.find(Filters.eq("_id", id)).firstOrNull()?.apply {
            freeBoxes = local?.freeBoxes ?: listOf("0")
        }
        return remoteData
    }



    private suspend fun List<CarWashShortResponse>.executeFilters(params: Parameters): List<CarWashShortResponse> {
        val search = params["search"]

        val status = params["status"]
        val limit = params["limit"]
        val offset = params["offset"]

        return withContext(Dispatchers.IO) {
            this@executeFilters
                .let { list ->
                    //search filter
                    if (search.isNullOrEmpty()) list
                    else list.filter { it.name.hasText(search) || it.address.street.hasText(search) }
                }
                .let { if (!status.isNullOrEmpty()) it.filter { it.name.contains(status) } else it }
        }
    }
}