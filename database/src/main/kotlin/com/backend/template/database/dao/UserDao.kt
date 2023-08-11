package com.backend.template.database.dao

import com.backend.template.dbschema.jooqGenerated.Tables.USERS
import com.backend.template.dbschema.jooqGenerated.tables.pojos.Users
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class UserDao (
    private val context: DSLContext
) {

    fun getUserByEmail(email: String) : Users? {
        return context.select()
            .from(USERS)
            .where(USERS.USERNAME.eq(email))
            .fetchOneInto(Users::class.java)
    }


}