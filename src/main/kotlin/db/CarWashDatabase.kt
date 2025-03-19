package kg.automoika.db

import kg.automoika.data.body.CarWashFreeBoxesBody
import kg.automoika.data.remote.CarWashLocationModel
import kg.automoika.data.remote.CarWashRemote
import kg.automoika.data.response.CarWashShortResponse
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object CarWashDatabase {

    private suspend fun <T> suspendTransaction(block: suspend () -> T): T {
        return newSuspendedTransaction(Dispatchers.IO) { block() }
    }


    suspend fun updateCarWashFreeBoxes(model: CarWashFreeBoxesBody) = suspendTransaction {
        CarWashTable.update({ CarWashTable.id eq model.id.toInt() }) {
            it[freeBoxes] = model.freeBoxes
        } > 0
    }

    suspend fun addSingleCarWashPoint(model: CarWashRemote) = suspendTransaction {
        CarWashTable.insert {
            it[id] = model.id.toInt()
            it[name] = model.name
            it[date] = model.createdAt

            it[city] = model.address.city
            it[street] = model.address.street
            it[lat] = model.address.lat
            it[lon] = model.address.lon

            it[image] = model.backgroundImage.imageUrl
            it[freeBoxes] = model.freeBoxes.joinToString(",")
            it[boxesCount] = model.boxesCount

            it[inFavorite] = model.inFavorite.joinToString(",")
            it[cleaningType] = model.type
        }.insertedCount > 0
    }

    suspend fun addManyCarWashPoints(list: List<CarWashRemote>) = suspendTransaction {
        CarWashTable.batchInsert(list) {
            this[CarWashTable.id] = it.id.toInt()
            this[CarWashTable.name] = it.name
            this[CarWashTable.date] = it.createdAt

            this[CarWashTable.city] = it.address.city
            this[CarWashTable.street] = it.address.street
            this[CarWashTable.lat] = it.address.lat
            this[CarWashTable.lon] = it.address.lon

            this[CarWashTable.image] = it.backgroundImage.imageUrl
            this[CarWashTable.boxesCount] = it.boxesCount
            this[CarWashTable.freeBoxes] = it.freeBoxes.joinToString(",")

            this[CarWashTable.inFavorite] = it.inFavorite.joinToString(",")
            this[CarWashTable.cleaningType] = it.type
        }.map { resultRowToCarWashResponse(it) }
    }

    suspend fun getCarWashLocal(id : String) = suspendTransaction {
        try {
            val local = CarWashTable.select { CarWashTable.id eq id.toInt() }.firstOrNull()
            if (local == null) null else resultRowToCarWashResponse(local)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCarWashListLocal() = suspendTransaction {
        try {
            CarWashTable.selectAll().map { resultRowToCarWashResponse(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }



    private fun resultRowToCarWashResponse(row: ResultRow): CarWashShortResponse {
        return CarWashShortResponse(
            id = row[CarWashTable.id].toString(),
            name = row[CarWashTable.name],
            backgroundImage = row[CarWashTable.image],
            address = CarWashLocationModel(
                row[CarWashTable.street],
                row[CarWashTable.city],
                row[CarWashTable.lat],
                row[CarWashTable.lon],
                ""
            ),
            boxesCount = row[CarWashTable.boxesCount],
            freeBoxes = row[CarWashTable.freeBoxes].split(","),
            inFavorite = row[CarWashTable.inFavorite].split(",")
        )
    }
}