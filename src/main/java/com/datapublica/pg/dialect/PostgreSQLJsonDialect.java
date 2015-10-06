package com.datapublica.pg.dialect;

import org.hibernate.dialect.PostgreSQL92Dialect;

import java.sql.Types;

public class PostgreSQLJsonDialect extends PostgreSQL92Dialect {
    public PostgreSQLJsonDialect() {
        registerColumnType(Types.JAVA_OBJECT - 1, "jsonb");
    }
}
