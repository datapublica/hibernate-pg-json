package com.datapublica.pg.dialect;

import org.hibernate.dialect.PostgreSQL9Dialect;

import java.sql.Types;

public class PostgreSQLJsonDialect extends PostgreSQL9Dialect {
    public PostgreSQLJsonDialect() {
        registerColumnType(Types.JAVA_OBJECT, "json");
        registerColumnType(Types.JAVA_OBJECT - 1, "jsonb");
    }
}
