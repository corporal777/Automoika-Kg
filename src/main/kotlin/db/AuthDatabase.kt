package kg.automoika.db

import kg.automoika.data.response.TokenResponse
import kg.automoika.extensions.generateId
import kg.automoika.extensions.generateToken
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object AuthDatabase {

    private suspend fun <T> suspendTransaction(block: suspend () -> T): T {
        return newSuspendedTransaction(Dispatchers.IO) { block() }
    }

    suspend fun getToken(token : String) = suspendTransaction {
        val local = TokenTable.select { TokenTable.token eq token }.firstOrNull()
        if (local == null) null else resultRowToToken(local)
    }

    suspend fun getTokenById(id : String) = suspendTransaction {
        try {
            val local = TokenTable.select { TokenTable.id eq id.toInt() }.firstOrNull()
            if (local == null) null else resultRowToToken(local)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createToken() = suspendTransaction {
        val token = TokenTable.insert {
            it[id] = generateId().toInt()
            it[token] = generateToken()
            it[createdAt] = System.currentTimeMillis().toString()
        }.resultedValues?.firstOrNull()

        if (token != null) resultRowToToken(token) else null
    }

    private fun resultRowToToken(row: ResultRow): TokenResponse {
        return TokenResponse(
            id = row[TokenTable.id].toString(),
            token = row[TokenTable.token],
            createdAt = row[TokenTable.createdAt]
        )
    }

}