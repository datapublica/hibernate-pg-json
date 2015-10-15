# hibernate-pg-json
Provide json/jsonb mapping for hibernate

# Configuration
1) Import dependency in maven
```xml
    <dependency>
      <groupId>com.datapublica.pg</groupId>
      <artifactId>hibernate-json</artifactId>
      <version>${hibernate-pg-json.version}</version>
    </dependency>
```

NB: This has not direct dependency to avoid version clashes.

2) Configure the dialect
Use ```com.datapublica.pg.dialect.PostgreSQLJsonDialect``` as the DatabasePlatform.

If you have Hibernate 5, you can use ```PostgreSQL94JsonDialect``` which includes 9.4 new ```make_*``` functions.

3) Import types
Add ```com.datapublica.pg.types``` to your entity packages to scan.

4) [Test] To use H2 in tests
Configure the dialect ```com.datapublica.pg.dialect.H2JsonDialect```

# Example
```java
@Type(type = "JsonSet", parameters = {
        @Parameter(name = "type", value = "String")
})
private Set<String> tags;
```

This will map the field tags to a ```jsonb``` column which is a list of strings (structured as Set in your domain).

If you want to map to a simple ```json``` column, do the following.

```java
@Type(type = "JsonSet", parameters = {
        @Parameter(name = "type", value = "String"),
        @Parameter(name = "binary", value = "false")
})
private Set<String> tags;
```

# Generic Types
As stated previously, all types support the parameter ```binary``` which is by default ```true``` which selects ```jsonb``` over ```json```.

Classes references are any class name available via ```Class.forName```. A default prefix may be given to enhance readability.

## Json
Map a complex object directly.

Object class is given via parameter ```type``` (default prefix ```java.lang.```).

## JsonList
Map a list of objects.

List content class is given via parameter ```type``` (default prefix ```java.lang.```).

## JsonSet
Map a set of objects.

Set content class is given via parameter ```type``` (default prefix ```java.lang.```).

## JsonMap
Map a map object.

Key class is given via parameter ```key``` (default prefix ```java.lang.```).

Value class is given via parameter ```value``` (default prefix ```java.lang.```).

## JsonCollection
Map a generic collection.

Collection content class is given via parameter ```type``` (default prefix ```java.lang.```).

Container class name must be given via parameter ```container``` (default prefix ```java.util.```)

# Specific type
If you want to define a complex type composition like ```Map<String, List<Integer>>``` you have to create a new type.

```java
public class MyComplexType extends JsonType {
    public MyComplexType() {
        TypeFactory tf = MAPPER.getTypeFactory();
        CollectionType listIntegerType = tf.constructCollectionType(List.class, Integer.class);
        // First parameter is the jackson type, second is the binary parameter.
        init(tf.constructMapType(HashMap.class, SimpleType.construct(String.class), listIntegerType), true);
    }
}
```

After that you can use ```@Type(type = "a.b.c.MyComplexType")```.

You can either use the ```JsonType(JavaType, boolean)``` constructor or the method to have a later initialization (to create parametrized types)

# Mapper customization
The jackson Object Mapper is (currently) defined as static final in ```com.datapublica.pg.types.JsonType``` (field ```MAPPER```).
If you want more customization on this (say for date serialization for instant), feel free to add options.

# Debug helper
* ```Unknown SQL type code 1999``` or ```2000```

You forgot to include the dialect (database-platform) in your configuration.

* ```Unknown type Json```

You forgot to import types (packages to scan) in your configuration.

* ```Class not found PostgreSQL94Dialect```

You are using Hibernate 4.x and therefore cannot use ```PostgreSQL94JsonDialect``` in your code, just use ```PostgreSQLJsonDialect```.