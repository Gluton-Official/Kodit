package database.tables

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Transaction

abstract class SimpleIdTable<ID : Comparable<ID>, E : Entity<ID>>(
    name: String = "",
    private val entityClassSupplier: () -> EntityClass<ID, E>
) : IdTable<ID>(name) {
    context(Transaction) fun get(id: ID) = entityClassSupplier().findById(id)
    context(Transaction) fun create(id: ID, builder: E.() -> Unit = {}) = entityClassSupplier().new(id, builder)
    context(Transaction) fun createOrUpdate(id: ID, builder: E.() -> Unit = {}) = get(id)?.apply(builder) ?: create(id, builder)
    context(Transaction) fun delete(id: ID) = get(id)?.delete()
}
