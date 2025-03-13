package kg.automoika.db

import org.jetbrains.exposed.sql.Table

object CarWashTable : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val date = varchar("date", 255)

    val city = varchar("city", 255)
    val street = varchar("street", 255)
    val lat = varchar("lat", 255)
    val lon = varchar("lon", 255)

    val image = varchar("image", 255)
    val boxesCount = varchar("boxesCount", 255)
    val freeBoxes = varchar("freeBoxes", 255)

    val inFavorite = varchar("favourites", 255)
    val cleaningType = varchar("cleaningType", 255)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

object CarWashBoxesTable : Table() {
    val id = integer("id").autoIncrement()
    val freeBoxes = varchar("freeBoxes", 255)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
