package com.datapublica.pg.dialect;

import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.dialect.PostgreSQL9Dialect;

import java.sql.Types;

public class PostgreSQL94JsonDialect extends PostgreSQL94Dialect {
    public PostgreSQL94JsonDialect() {
        registerColumnType(Types.JAVA_OBJECT - 1, "jsonb");
    }
}
