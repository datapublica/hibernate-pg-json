package com.datapublica.pg.dialect;

import java.sql.Types;

/**
 * Created by loic on 05/10/15.
 */
public class H2JsonDialect extends org.hibernate.dialect.H2Dialect {
    public H2JsonDialect() {
        super();

        registerColumnType(Types.JAVA_OBJECT, "OTHER");
        registerColumnType(Types.JAVA_OBJECT - 1, "OTHER");
    }
}
