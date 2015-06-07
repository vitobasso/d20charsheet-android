package com.vituel.dndplayer.util.database;

/**
 * Created by Victor on 06/06/2015.
 */
public class Column {

    private String name;
    private ColumnType type;
    private boolean notNull;
    private boolean primaryKey;

    public Column(String name, ColumnType type, boolean notNull, boolean primaryKey) {
        this.name = name;
        this.type = type;
        this.notNull = notNull;
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public ColumnType getType() {
        return type;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public String toSql() {
        StringBuilder str = new StringBuilder();
        str.append(name);
        str.append(" ");
        str.append(type.name().toLowerCase());
        if (notNull) {
            str.append(" not null");
        }
        if (primaryKey) {
            str.append(" primary key");
        }
        return str.toString();
    }

    @Override
    public String toString() {
        return toSql();
    }
}
