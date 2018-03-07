package io.boonlogic.soul.land.microservices.hello.dao

import io.boonlogic.soul.land.microservices.hello.domain.Person
import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import javax.sql.DataSource

class PersonDAO(
    private val create: DSLContext
) {

    fun findAll() = create
        .select(name, greeting)
        .from(personTable).fetch { Person(it[name], it[greeting]) }

    fun createPerson(personName: String, personGreeting: String) = create
        .insertInto(personTable)
        .columns(name, greeting)
        .values(personName, personGreeting).execute() > 0

}

val personTable = table("person")
val name = field("name", String::class.java)
val greeting = field("greeting", String::class.java)
