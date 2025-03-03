package kg.automoika.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
class CarWashLocal(
    val id: Int = 0,
    val name: String,
    val desc: String,
    val city: String,
)

object CarWashTable : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val desc = varchar("desc", 255)
    val city = varchar("city", 255)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
