package me.newdavis.sql;

public enum SQLTables {

    ROLE("role"), ROLE_PERMISSIONS("role_permissions"),
    ROLE_INHERITANCE("role_inheritance"), PLAYER("player"),
    PLAYER_PERMISSIONS("player_permissions");

    private String table;

    SQLTables(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }
}
