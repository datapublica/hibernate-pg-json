package com.datapublica.pg.types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by loic on 11/09/2017.
 * Copyright (C) by Data Publica, All Rights Reserved.
 */
public class RawJsonType implements UserType {
    private int[] sqlTypes;
    private boolean isBinary;

    protected RawJsonType() {

    }

    public RawJsonType(boolean isBinary) {
        init(isBinary);
    }

    protected void init(boolean isBinary) {
        this.isBinary = isBinary;
        this.sqlTypes = new int[]{Types.JAVA_OBJECT - (!isBinary ? 0 : 1)};
    }


    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        } else if (x == null || y == null) {
            return false;
        } else {
            return x.equals(y);
        }
    }

    public int hashCode(Object x) throws HibernateException {
        return null == x ? 0 : x.hashCode();
    }

    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        return nullSafeGet(rs, names, (SessionImplementor) session, owner);
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        nullSafeSet(st, value, index, (SessionImplementor) session);
    }

    public boolean isMutable() {
        return false;
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setObject(index, null);
            return;
        }
        PGobject pg = new PGobject();
        pg.setType(isBinary ? "jsonb" : "json");
        pg.setValue(serialize(value));
        st.setObject(index, pg);
    }

    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        final Object result = rs.getObject(names[0]);
        if (!rs.wasNull()) {
            String content;

            if (result instanceof String) {
                content = (String) result;
            } else if (result instanceof PGobject) {
                // If we get directly the PGobject for some reason (more exactly, if a DB like H2 does the serialization directly)
                content = ((PGobject) result).getValue();
            } else {
                throw new IllegalArgumentException("Unknown object type (excepted pgobject or json string)");
            }
            if (content != null) {
                return deserialize(content);
            }
        }
        return null;
    }

    public Object deepCopy(Object value) throws HibernateException {
        return deserialize(serialize(value));
    }


    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return deepCopy(original);
    }


    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }


    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return deepCopy(cached);
    }

    protected String serialize(Object o) {
        return o.toString();
    }

    protected Object deserialize(String o) {
        return o;
    }

    public int[] sqlTypes() {
        return sqlTypes;
    }


    public Class returnedClass() {
        return String.class;
    }
}