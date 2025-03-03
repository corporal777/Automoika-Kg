package kg.automoika.db

import kg.automoika.data.CarWashLocal
import kg.automoika.data.CarWashTable
import kg.automoika.data.CarWashTable.id
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import javax.annotation.Priority

object DatabaseUtils {

    suspend fun <T> suspendTransaction(block: suspend () -> T): T {
        return newSuspendedTransaction(Dispatchers.IO) { block() }
    }

    suspend fun addSingleCarWashPoint(model: CarWashLocal): CarWashLocal? = suspendTransaction {
        val insertCarWash = CarWashTable.insert {
            it[id] = model.id
            it[name] = model.name
            it[desc] = model.desc
            it[city] = model.city
        }
        insertCarWash.resultedValues?.singleOrNull()?.let { resultRowToModel(it) }
    }

    suspend fun addManyCarWashPoints() = suspendTransaction {
        val mappings = listOf(
            CarWashLocal(102, "Saray", "description", "Bishkek"),
            CarWashLocal(103, "Toy", "description", "Bishkek"),
            CarWashLocal(104, "Some", "description", "Bishkek")
        )
        val insertCarWash = CarWashTable.batchInsert(mappings) {
            this[CarWashTable.id] = it.id
            this[CarWashTable.name] = it.name
            this[CarWashTable.desc] = it.desc
            this[CarWashTable.city] = it.city
        }
        insertCarWash.firstOrNull()?.let { resultRowToModel(it) }
    }

    private fun resultRowToModel(row: ResultRow): CarWashLocal {
        return CarWashLocal(
            id = row[CarWashTable.id],
            name = row[CarWashTable.name],
            desc = row[CarWashTable.desc],
            city = row[CarWashTable.city]
        )
    }
}