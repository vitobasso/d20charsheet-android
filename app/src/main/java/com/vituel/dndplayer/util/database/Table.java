package com.vituel.dndplayer.util.database;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vituel.dndplayer.util.database.ColumnType.INTEGER;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;

/**
 * Created by Victor on 06/06/2015.
 */
public class Table {

    private String name;
    private List<Column> columns = new ArrayList<>();
    private Map<String, Integer> mapByName = new HashMap<>();

    public Table(String name) {
        this.name = name;
        addPrimaryKey();
    }

    public Table col(String name, ColumnType type) {
        Column column = new Column(name, type, false, false);
        add(column);
        return this;
    }

    public Table colNotNull(String name, ColumnType type) {
        Column column = new Column(name, type, true, false);
        add(column);
        return this;
    }

    private void addPrimaryKey() {
        Column column = new Column(COLUMN_ID, INTEGER, true, true);
        add(column);
    }

    private void add(Column column) {
        int index = columns.size();
        columns.add(column);
        mapByName.put(column.getName(), index);
    }

    public String getColumnName(int index) {
        return columns.get(index).getName();
    }

    public int getIndex(String name) {
        return mapByName.get(name);
    }

    public String toSql() {
        StringBuilder str = new StringBuilder();
        str.append("CREATE TABLE ");
        str.append(name);
        str.append(" (");
        String cols = Joiner.on(", ").join(columns);
        str.append(cols);
        str.append(");");
        return str.toString();
    }

    public String[] getColumnNames() {
        int numCols = columns.size();
        String[] names = new String[numCols];
        for (int i = 0; i < numCols; i++) {
            names[i] = columns.get(i).getName();
        }
        return names;
    }

    public int countColumns() {
        return columns.size();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
