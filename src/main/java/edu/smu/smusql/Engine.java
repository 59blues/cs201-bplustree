package edu.smu.smusql;

import java.util.*;

public class Engine {
    private Map<String, Table> tables;
    
    public Engine() {
        this.tables = new HashMap<>();
    }
    
    public String executeSQL(String query) {
        String[] tokens = query.trim().split("\\s+");
        String command = tokens[0].toUpperCase();
        
        try {
            switch (command) {
                case "CREATE":
                    return handleCreate(tokens);
                case "INSERT":
                    return handleInsert(tokens);
                case "SELECT":
                    return handleSelect(tokens);
                case "DELETE":
                    return handleDelete(tokens);
                case "UPDATE":
                    return handleUpdate(tokens);
                default:
                    return "Unknown command: " + command;
            }
        } catch (Exception e) {
            return "Error executing query: " + e.getMessage();
        }
    }
    
    private String handleCreate(String[] tokens) {
        String tableName = tokens[2];
        List<String> columns = new ArrayList<>();
        for (int i = 3; i < tokens.length; i++) {
            columns.add(tokens[i]);
        }
        tables.put(tableName, new Table(tableName, columns));
        return "Table created successfully";
    }
    
    private String handleInsert(String[] tokens) {
        String tableName = tokens[2];
        Table table = tables.get(tableName);
        if (table == null) return "Table not found";
        
        List<Object> values = new ArrayList<>();
        int id = Integer.parseInt(tokens[4]);
        for (int i = 4; i < tokens.length; i++) {
            values.add(tokens[i]);
        }
        
        table.insert(id, values);
        return "Record inserted successfully";
    }
    
    private String handleSelect(String[] tokens) {
        String tableName = tokens[3];
        Table table = tables.get(tableName);
        if (table == null) return "Table not found";

        // If it's just "SELECT * FROM table"
        if (tokens.length == 4) {
            return formatResults(table.select(null, null));
        }
        
        Integer startId = null, endId = null;
        if (tokens.length > 4) {
            startId = Integer.parseInt(tokens[6]);
            if (tokens.length > 8) {
                endId = Integer.parseInt(tokens[8]);
            }
        }
        
        List<List<Object>> results = table.select(startId, endId);
        return formatResults(results);
    }
    
    private String handleDelete(String[] tokens) {
        String tableName = tokens[2];
        Table table = tables.get(tableName);
        if (table == null) return "Table not found";
        
        int id = Integer.parseInt(tokens[6]);
        table.delete(id);
        return "Record deleted successfully";
    }

    private String handleUpdate(String[] tokens) {
        String tableName = tokens[1];
        Table table = tables.get(tableName);
        if (table == null) return "Table not found";
    
        String columnToUpdate = tokens[2];
        Object newValue = tokens[3];
        String whereColumn = tokens[4];
        int whereValue = Integer.parseInt(tokens[5]);
    
        table.update(whereValue, columnToUpdate, newValue);
        return "Record updated successfully";
    }
    
    private String formatResults(List<List<Object>> results) {
        if (results.isEmpty()) return "No results found";
        
        StringBuilder sb = new StringBuilder();
        for (List<Object> row : results) {
            sb.append(row.toString()).append("\n");
        }
        return sb.toString();
    }
}
