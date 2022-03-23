package database

import dev.kord.common.entity.Snowflake
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.jetbrains.exposed.sql.vendors.currentDialect

class SnowflakeColumnType : ColumnType() {
    override fun sqlType(): String = currentDialect.dataTypeProvider.longType()

    override fun valueFromDB(value: Any): Any {
        return when (value) {
            is ULong -> Snowflake(value)
            is Long -> Snowflake(value.toULong())
            is Number -> Snowflake(value.toLong().toULong())
            is String -> Snowflake(value.toLong().toULong())
            else -> error("Unexpected value for Snowflake: $value of ${value::class.qualifiedName}")
        }
    }

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        val v = if (value is Snowflake) value.value.toLong() else value
        super.setParameter(stmt, index, v)
    }

    override fun notNullValueToDB(value: Any): Any {
        val v = if (value is Snowflake) value.value.toLong() else value
        return super.notNullValueToDB(v)
    }
}
