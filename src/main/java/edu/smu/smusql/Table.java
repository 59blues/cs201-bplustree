package edu.smu.smusql;

import java.util.*;

public class Table {
    private String name;
    private List<String> columns;
    private BPlusTree data;
    
    public Table(String name, List<String> columns) {
        this.name = name;
        this.columns = new ArrayList<>();
        // Parse each column name individually
        for (String col : columns) {
            // Remove any parentheses, commas and trim whitespace
            String cleanColumn = col.replaceAll("[(),]", "").trim();
            if (!cleanColumn.isEmpty()) {
                this.columns.add(cleanColumn);
            }
        }
        this.data = new BPlusTree();
    }
    
    public void insert(int id, List<Object> values) {
        data.insert(id, values);
    }
    
    public List<List<Object>> select(Integer startId, Integer endId) {
        if (startId == null && endId == null) {
            // Return all records when no range is specified
            return data.rangeSearch(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        return data.rangeSearch(startId, endId);
    }

    public void update(int id, String column, Object newValue) {
        List<List<Object>> records = data.search(id);
        // System.out.println("Retrieved record: " + records);
        if (!records.isEmpty()) {
            int columnIndex = columns.indexOf(column);
            // System.out.println("Column index for '" + column + "': " + columnIndex);
            // System.out.println("Available columns: " + columns);
            // System.out.println("Looking for column: " + column);
            // System.out.println("Column index for '" + column + "': " + columnIndex);
            // System.out.println("Table name: " + name);
            // System.out.println("Columns at creation: " + columns);

            if (columnIndex != -1) {
                List<Object> record = records.get(0);
                record.set(columnIndex, newValue);
                data.delete(id);
                data.insert(id, record);
            }
        }
    }
    
    public void delete(int id) {
        data.delete(id);
    }
    
    public String getName() {
        return name;
    }
    
    public List<String> getColumns() {
        return columns;
    }
}
