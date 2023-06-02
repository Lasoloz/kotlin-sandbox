import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(Cities)

        val clujId = Cities.insert {
            it[name] = "Cluj-Napoca"
            it[population] = 300_000
        } get Cities.id
        println("Id for Cluj-Napoca: $clujId")

        Cities.insert {
            it[name] = "Vienna"
            it[population] = 2_500_000
        }

        Cities.slice(Cities.id, Cities.name).selectAll().forEach {
            println("Selected city: ${it[Cities.id]}, ${it[Cities.name]}")
            try {
                println(it[Cities.population])
            } catch (ex: Exception) {
                println("Population obviously wasn't selected!")
            }
        }
    }
}

object Cities : IntIdTable() {
    val name = varchar("name", 50)
    val population = integer("population")
}
