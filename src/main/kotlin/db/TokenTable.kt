package kg.automoika.db

import kg.automoika.db.CarWashBoxesTable.autoIncrement
import org.jetbrains.exposed.sql.Table

object TokenTable : Table() {
    val id = integer("id").autoIncrement()
    val token = varchar("token", 255)
    val createdAt = varchar("createdAt", 255)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}