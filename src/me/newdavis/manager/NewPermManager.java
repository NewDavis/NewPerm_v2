package me.newdavis.manager;

import me.newdavis.api.NameFetcher;
import me.newdavis.file.PermissionsFile;
import me.newdavis.plugin.NewPerm;
import me.newdavis.file.SettingsFile;
import me.newdavis.sql.MySQL;
import me.newdavis.sql.SQLTables;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class NewPermManager {

    private static final boolean mySQLEnabled = SettingsFile.getMySQLEnabled();
    private static final MySQL mysql = NewPerm.getMySQL();

    private static final Collection<String> roleList = new ArrayList<>();
    private static final HashMap<String, Boolean> defaultRole = new HashMap<>();
    private static final HashMap<String, List<String>> rolePermission = new HashMap<>();
    private static final HashMap<String, List<String>> roleInheritance = new HashMap<>();
    private static final HashMap<String, String> rolePrefix = new HashMap<>();
    private static final HashMap<String, String> roleSuffix = new HashMap<>();

    //Role Methods
    public static Collection<String> getRoleList() {
        Collection<String> roles = new ArrayList<>();
        if (!roleList.isEmpty()) {
            return roleList;
        }

        if (mySQLEnabled) {
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT ROLE FROM " + SQLTables.ROLE.getTable());
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    roles.add(rs.getString("ROLE"));
                }
                mysql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (PermissionsFile.isPathSet("Role")) {
                roles = PermissionsFile.getConfigurationSection("Role");
            }
        }

        roleList.addAll(roles);
        return roleList;
    }

    public static List<String> getRoleInheritance(String role) {
        Collection<String> roles = getRoleList();
        List<String> inheritance = new ArrayList<>();
        if (roles.contains(role)) {
            if (roleInheritance.containsKey(role)) {
                return roleInheritance.get(role);
            }

            if (mySQLEnabled) {
                try {
                    PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT INHERITANCE FROM " + SQLTables.ROLE_INHERITANCE.getTable() + " WHERE ROLE=?");
                    ps.setString(1, role);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        inheritance.add(rs.getString("INHERITANCE"));
                    }
                    mysql.disconnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (PermissionsFile.isPathSet("Role." + role + ".inheritance")) {
                    inheritance = PermissionsFile.getStringList("Role." + role + ".inheritance");
                }
            }

            roleInheritance.put(role, inheritance);
        }
        return inheritance;
    }

    public static List<String> getRolePermissions(String role) {
        Collection<String> roles = getRoleList();
        List<String> permissions = new ArrayList<>();
        if (roles.contains(role)) {
            if (rolePermission.containsKey(role)) {
                return rolePermission.get(role);
            }

            if(mySQLEnabled) {
                try {
                    PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT PERMISSION FROM " + SQLTables.ROLE_PERMISSIONS.getTable() + " WHERE ROLE=?");
                    ps.setString(1, role);
                    ResultSet rs = ps.executeQuery();

                    while(rs.next()) {
                        permissions.add(rs.getString("PERMISSION"));
                    }
                    mysql.disconnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                if (PermissionsFile.isPathSet("Role." + role + ".permissions")) {
                    permissions = PermissionsFile.getStringList("Role." + role + ".permissions");
                }
            }

            rolePermission.put(role, permissions);
        }
        return permissions;
    }

    public static String getRolePrefix(String role) {
        Collection<String> roles = getRoleList();
        String prefix = "";
        if (roles.contains(role)) {
            if (rolePrefix.containsKey(role)) {
                return rolePrefix.get(role);
            }

            if(mySQLEnabled) {
                try {
                    PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT PREFIX FROM " + SQLTables.ROLE.getTable() + " WHERE ROLE=?");
                    ps.setString(1, role);
                    ResultSet rs = ps.executeQuery();

                    if(rs.next()) {
                        prefix = rs.getString("PREFIX");
                    }
                    mysql.disconnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                if (PermissionsFile.isPathSet("Role." + role + ".settings.prefix")) {
                    prefix = PermissionsFile.getString("Role." + role + ".settings.prefix");
                }
            }

            rolePrefix.put(role, prefix);
        }
        return prefix;
    }

    public static String getRoleSuffix(String role) {
        Collection<String> roles = getRoleList();
        String suffix = "";
        if (roles.contains(role)) {
            if (roleSuffix.containsKey(role)) {
                return roleSuffix.get(role);
            }

            if(mySQLEnabled) {
                try {
                    PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT SUFFIX FROM " + SQLTables.ROLE.getTable() + " WHERE ROLE=?");
                    ps.setString(1, role);
                    ResultSet rs = ps.executeQuery();

                    if(rs.next()) {
                        suffix = rs.getString("SUFFIX");
                    }
                    mysql.disconnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                if (PermissionsFile.isPathSet("Role." + role + ".settings.suffix")) {
                    suffix = PermissionsFile.getString("Role." + role + ".settings.suffix");
                }
            }

            roleSuffix.put(role, suffix);
        }
        return suffix;
    }

    public static void addRole(String role) {
        Collection<String> roles = getRoleList();
        if(!roles.contains(role)) {
            if(mySQLEnabled) {
                try {
                    PreparedStatement ps = mysql.getConnection().prepareStatement("INSERT INTO " + SQLTables.ROLE.getTable() + " (ROLE,PREFIX,SUFFIX,DEFAULT_ROLE) VALUES (?,?,?,?)");
                    ps.setString(1, role);
                    ps.setString(2, "§f");
                    ps.setString(3, "§f");
                    ps.setInt(4, 0);
                    ps.executeUpdate();
                    mysql.disconnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                PermissionsFile.setPath("Role." + role + ".settings.prefix", "§f");
                PermissionsFile.setPath("Role." + role + ".settings.suffix", "§f");
                PermissionsFile.setPath("Role." + role + ".permissions", Collections.singletonList(""));
            }

            roleList.add(role);
        }
    }

    public static void addRoles(String[] roleList) {
        Collection<String> roles = getRoleList();
        for(String role : roleList) {
            if (!roles.contains(role)) {
                addRole(role);
            }
        }
    }

    public static void removeRole(String role) {
        Collection<String> roles = getRoleList();
        if (roles.contains(role)) {
            if(mySQLEnabled) {
                try {
                    PreparedStatement ps = mysql.getConnection().prepareStatement("DELETE FROM " + SQLTables.ROLE.getTable() + " WHERE ROLE=?");
                    ps.setString(1, role);
                    ps.executeUpdate();

                    ps = mysql.getConnection().prepareStatement("DELETE FROM " + SQLTables.ROLE_PERMISSIONS.getTable() + " WHERE ROLE=?");
                    ps.setString(1, role);
                    ps.executeUpdate();

                    ps = mysql.getConnection().prepareStatement("DELETE FROM " + SQLTables.ROLE_INHERITANCE.getTable() + " WHERE ROLE=?");
                    ps.setString(1, role);
                    ps.executeUpdate();
                    mysql.disconnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                PermissionsFile.setPath("Role." + role, null);
            }

            roleList.remove(role);
        }
    }

    public static boolean isRoleDefault(String role) {
        Collection<String> roles = getRoleList();
        boolean isRoleDefault = false;
        if (roles.contains(role)) {
            if (defaultRole.containsKey(role)) {
                return defaultRole.get(role);
            }

            if(mySQLEnabled) {
                try {
                    PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT DEFAULT_ROLE FROM " + SQLTables.ROLE.getTable() + " WHERE ROLE=?");
                    ps.setString(1, role);
                    ResultSet rs = ps.executeQuery();

                    if(rs.next()) {
                        isRoleDefault = (rs.getInt("DEFAULT_ROLE") == 1);
                    }
                    mysql.disconnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                if (PermissionsFile.isPathSet("Role." + role + ".settings.default")) {
                    isRoleDefault = PermissionsFile.getBoolean("Role." + role + ".settings.default");
                }
            }

            defaultRole.put(role, isRoleDefault);
        }
        return isRoleDefault;
    }

    public static String getDefaultRole() {
        Collection<String> roles = getRoleList();
        for(String role : roles) {
            if(isRoleDefault(role)) {
                return role;
            }
        }
        return "";
    }

    public static void setDefaultRole(String role) {
        String defaultRole = getDefaultRole();
        if (isRoleDefault(defaultRole)) {
            if (mySQLEnabled) {
                try {
                    PreparedStatement ps = mysql.getConnection().prepareStatement("UPDATE " + SQLTables.ROLE.getTable() + " SET DEFAULT_ROLE=? WHERE ROLE=?");
                    ps.setInt(1, 0);
                    ps.setString(2, defaultRole);
                    ps.executeUpdate();
                    mysql.disconnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                PermissionsFile.setPath("Role." + defaultRole + ".settings.default", false);
            }

            NewPermManager.defaultRole.put(defaultRole, false);
        }
        if (mySQLEnabled) {
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement("UPDATE " + SQLTables.ROLE.getTable() + " SET DEFAULT_ROLE=? WHERE ROLE=?");
                ps.setInt(1, 1);
                ps.setString(2, role);
                ps.executeUpdate();
                mysql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            PermissionsFile.setPath("Role." + role + ".settings.default", true);
        }
        NewPermManager.defaultRole.put(role, true);
    }

    public static void addRolePermission(String role, String permission) {
        Collection<String> roles = getRoleList();
        if(roles.contains(role)) {
            List<String> permissions = getRolePermissions(role);
            if(!permissions.contains(permission)) {
                permissions.add(permission);
                if(mySQLEnabled) {
                    try {
                        PreparedStatement ps = mysql.getConnection().prepareStatement("INSERT INTO " + SQLTables.ROLE_PERMISSIONS.getTable() + " (ROLE,PERMISSION) VALUES (?,?)");
                        ps.setString(1, role);
                        ps.setString(2, permission);
                        ps.executeUpdate();
                        mysql.disconnect();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    PermissionsFile.setPath("Role." + role + ".permissions", permissions);
                }

                rolePermission.put(role, permissions);
            }
        }
    }

    public static void addRolePermission(String role, String[] permissionList) {
        for(String permission : permissionList) {
            addRolePermission(role, permission);
        }
    }

    public static void addRolePermission(String role, List<String> permissionList) {
        for(String permission : permissionList) {
            addRolePermission(role, permission);
        }
    }

    public static boolean removeRolePermission(String role, String permission) {
        Collection<String> roles = getRoleList();
        if(roles.contains(role)) {
            List<String> permissions = getRolePermissions(role);
            if(permissions.contains(permission)) {
                permissions.remove(permission);
                if(mySQLEnabled) {
                    try {
                        PreparedStatement ps = mysql.getConnection().prepareStatement("DELETE FROM " + SQLTables.ROLE_PERMISSIONS.getTable() + " WHERE ROLE=? AND PERMISSION=?");
                        ps.setString(1, role);
                        ps.setString(2, permission);
                        ps.executeUpdate();
                        mysql.disconnect();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    PermissionsFile.setPath("Role." + role + ".permissions", permissions);
                }

                rolePermission.put(role, permissions);
                return true;
            }
        }
        return false;
    }

    public static void removeRolePermission(String role, String[] permissionList) {
        Collection<String> roles = getRoleList();
        for(String permission : permissionList) {
            removeRolePermission(role, permission);
        }
    }

    public static void removeRolePermission(String role, List<String> permissionList) {
        Collection<String> roles = getRoleList();
        for(String permission : permissionList) {
            removeRolePermission(role, permission);
        }
    }

    public static void addInheritance(String role, String inheritance) {
        Collection<String> roles = getRoleList();
        if (roles.contains(role)) {
            List<String> inheritanceList = getRoleInheritance(role);
            if (!inheritanceList.contains(inheritance)) {
                inheritanceList.add(inheritance);

                if(mySQLEnabled) {
                    try {
                        PreparedStatement ps = mysql.getConnection().prepareStatement("INSERT INTO " + SQLTables.ROLE_INHERITANCE.getTable() + " (ROLE,INHERITANCE) VALUES (?,?)");
                        ps.setString(1, role);
                        ps.setString(2, inheritance);
                        ps.executeUpdate();
                        mysql.disconnect();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    inheritanceList.add(inheritance);
                    PermissionsFile.setPath("Role." + role + ".inheritance", inheritanceList);
                }

                roleInheritance.put(role, inheritanceList);
            }
        }
    }

    public static boolean addInheritances(String role, String[] inheritances) {
        for(String inheritance : inheritances) {
            addInheritance(role, inheritance);
        }
        return false;
    }

    public static boolean addInheritances(String role, List<String> inheritances) {
        for(String inheritance : inheritances) {
            addInheritance(role, inheritance);
        }
        return false;
    }

    public static void removeInheritance(String role, String inheritance) {
        Collection<String> roles = getRoleList();
        if (roles.contains(role)) {
            List<String> inheritanceList = getRoleInheritance(role);
            if (inheritanceList.contains(inheritance)) {
                inheritanceList.remove(inheritance);

                if(mySQLEnabled) {
                    try {
                        PreparedStatement ps = mysql.getConnection().prepareStatement("DELETE FROM " + SQLTables.ROLE_INHERITANCE.getTable() + " WHERE ROLE=? AND INHERITANCE=?");
                        ps.setString(1, role);
                        ps.setString(2, inheritance);
                        ps.executeUpdate();
                        mysql.disconnect();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    inheritanceList.remove(inheritance);
                    PermissionsFile.setPath("Role." + role + ".inheritance", inheritanceList);
                }

                roleInheritance.put(role, inheritanceList);
            }
        }
    }

    public static boolean removeInheritances(String role, String[] inheritances) {
        for(String inheritance : inheritances) {
            removeInheritance(role, inheritance);
        }
        return false;
    }

    public static boolean removeInheritances(String role, List<String> inheritances) {
        for(String inheritance : inheritances) {
            removeInheritance(role, inheritance);
        }
        return false;
    }

    public static void setRolePrefix(String role, String prefix) {
        Collection<String> roles = getRoleList();
        if (roles.contains(role)) {
            if(mySQLEnabled) {
                try {
                    PreparedStatement ps = mysql.getConnection().prepareStatement("UPDATE " + SQLTables.ROLE.getTable() + " SET PREFIX=? WHERE ROLE=?");
                    ps.setString(1, prefix);
                    ps.setString(2, role);
                    ps.executeUpdate();
                    mysql.disconnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                PermissionsFile.setPath("Role." + role + ".settings.prefix", prefix);
            }

            rolePrefix.put(role, prefix);
        }
    }

    public static void setRoleSuffix(String role, String suffix) {
        Collection<String> roles = getRoleList();
        if (roles.contains(role)) {
            if(mySQLEnabled) {
                try {
                    PreparedStatement ps = mysql.getConnection().prepareStatement("UPDATE " + SQLTables.ROLE.getTable() + " SET SUFFIX=? WHERE ROLE=?");
                    ps.setString(1, suffix);
                    ps.setString(2, role);
                    ps.executeUpdate();
                    mysql.disconnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                PermissionsFile.setPath("Role." + role + ".settings.suffix", suffix);
            }

            rolePrefix.put(role, suffix);
        }
    }

    private static final HashMap<String, String> playerRole = new HashMap<>();
    private static final HashMap<String, String> playerPrefix = new HashMap<>();
    private static final HashMap<String, String> playerSuffix = new HashMap<>();
    private static final HashMap<String, List<String>> playerPermission = new HashMap<>();

    //Player Methods
    public static void checkIfMigrated(OfflinePlayer p) {
        if(mySQLEnabled) {
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT UUID FROM " + SQLTables.PLAYER.getTable() + " WHERE UUID=?");
                ps.setString(1, p.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();

                if(!rs.next()) {
                    migrateToUUID(p);
                }
                mysql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            if(!PermissionsFile.isPathSet("Player." + p.getUniqueId())) {
                migrateToUUID(p);
            }
        }
    }

    public static void migrateToUUID(OfflinePlayer p) {
        if (mySQLEnabled) {
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT UUID FROM " + SQLTables.PLAYER.getTable() + " WHERE UUID=?");
                ps.setString(1, p.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();

                if(!rs.next()) {
                    ps = mysql.getConnection().prepareStatement("UPDATE " + SQLTables.PLAYER.getTable() + " SET UUID=? WHERE UUID=?");
                    ps.setString(1, p.getUniqueId().toString());
                    ps.setString(2, NameFetcher.getName(p.getUniqueId()));
                    ps.executeUpdate();

                    ps = mysql.getConnection().prepareStatement("UPDATE " + SQLTables.PLAYER_PERMISSIONS.getTable() + " SET UUID=? WHERE UUID=?");
                    ps.setString(1, p.getUniqueId().toString());
                    ps.setString(2, NameFetcher.getName(p.getUniqueId()));
                    ps.executeUpdate();
                }
                mysql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            if(PermissionsFile.isPathSet("Player." + p.getName())) {
                Collection<String> permissions = new ArrayList<>();
                String role = getDefaultRole();
                String prefix = "";
                String suffix = "";

                if (PermissionsFile.isPathSet("Player." + NameFetcher.getName(p.getUniqueId()) + ".role")) {
                    role = PermissionsFile.getString("Player." + NameFetcher.getName(p.getUniqueId()) + ".role");
                }
                if (hasPlayerPrefix(p)) {
                    prefix = PermissionsFile.getString("Player." + NameFetcher.getName(p.getUniqueId()) + ".settings.prefix");
                }
                if (hasPlayerSuffix(p)) {
                    suffix = PermissionsFile.getString("Player." + NameFetcher.getName(p.getUniqueId()) + ".settings.suffix");
                }

                if (PermissionsFile.isPathSet("Player." + NameFetcher.getName(p.getUniqueId()) + ".permissions")) {
                    permissions = PermissionsFile.getStringList("Player." + NameFetcher.getName(p.getUniqueId()) + ".permissions");
                }

                PermissionsFile.setPath("Player." + NameFetcher.getName(p.getUniqueId()), null);
                PermissionsFile.setPath("Player." + p.getUniqueId() + ".role", role);
                if (!prefix.equalsIgnoreCase("")) {
                    PermissionsFile.setPath("Player." + p.getUniqueId() + ".settings.prefix", prefix);
                }
                if (!suffix.equalsIgnoreCase("")) {
                    PermissionsFile.setPath("Player." + p.getUniqueId() + ".settings.suffix", suffix);
                }
                PermissionsFile.setPath("Player." + p.getUniqueId() + ".permissions", permissions);
            }
        }
    }

    public static String getPlayerRole(OfflinePlayer p) {
        String role = getDefaultRole();

        if(playerRole.containsKey(p.getUniqueId().toString())) {
            return playerRole.get(p.getUniqueId().toString());
        }

        if (mySQLEnabled) {
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT ROLE FROM " + SQLTables.PLAYER.getTable() + " WHERE UUID=?");
                ps.setString(1, p.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getString("ROLE");
                }else{
                    ps = mysql.getConnection().prepareStatement("INSERT INTO " + SQLTables.PLAYER.getTable() + " (UUID,ROLE,PREFIX,SUFFIX) VALUES (?,?,?,?)");
                    ps.setString(1, p.getUniqueId().toString());
                    ps.setString(2, role);
                    ps.setString(3, "");
                    ps.setString(4, "");
                    ps.executeUpdate();
                }
                mysql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (PermissionsFile.isPathSet("Player." + p.getUniqueId() + ".role")) {
                role = PermissionsFile.getString("Player." + p.getUniqueId() + ".role");
            }
        }

        playerRole.put(p.getUniqueId().toString(), role);
        return role;
    }

    public static void setPlayerRole(OfflinePlayer p, String role) {
        if (mySQLEnabled) {
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement("UPDATE " + SQLTables.PLAYER.getTable() + " SET ROLE=? WHERE UUID=?");
                ps.setString(1, role);
                ps.setString(2, p.getUniqueId().toString());
                ps.executeUpdate();
                mysql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            PermissionsFile.setPath("Player." + p.getUniqueId() + ".role", role);
        }

        playerRole.put(p.getUniqueId().toString(), role);

        if(p.isOnline()) {
            grantAll(p.getPlayer());
        }
    }

    public static boolean hasPlayerPrefix(OfflinePlayer p) {
        if(playerPrefix.containsKey(p.getUniqueId().toString())) {
            return true;
        }

        boolean hasPrefix = !getRolePrefix(getPlayerRole(p)).isEmpty();

        if (mySQLEnabled) {
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT PREFIX FROM " + SQLTables.PLAYER.getTable() + " WHERE UUID=?");
                ps.setString(1, p.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();

                if(rs.next()) {
                    hasPrefix = true;
                }
                mysql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (PermissionsFile.isPathSet("Player." + p.getUniqueId() + ".settings.prefix")) {
                return true;
            }
        }

        return hasPrefix;
    }

    public static String getPlayerPrefix(OfflinePlayer p) {
        String prefix = "";

        if(playerPrefix.containsKey(p.getUniqueId().toString())) {
            return playerPrefix.get(p.getUniqueId().toString());
        }

        if (mySQLEnabled) {
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT PREFIX FROM " + SQLTables.PLAYER.getTable() + " WHERE UUID=?");
                ps.setString(1, p.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();

                if(rs.next()) {
                    prefix = rs.getString("PREFIX");
                }else{
                    prefix = getRolePrefix(getPlayerRole(p));
                }
                mysql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (PermissionsFile.isPathSet("Player." + p.getUniqueId() + ".settings.prefix")) {
                prefix = PermissionsFile.getString("Player." + p.getUniqueId() + ".settings.prefix");
            } else {
                prefix = getRolePrefix(getPlayerRole(p));
            }
        }

        playerPrefix.put(p.getUniqueId().toString(), prefix);
        return prefix;
    }

    public static void setPlayerPrefix(OfflinePlayer p, String prefix) {
        if (mySQLEnabled) {
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement("UPDATE " + SQLTables.PLAYER.getTable() + " SET PREFIX=? WHERE UUID=?");
                ps.setString(1, prefix);
                ps.setString(2, p.getUniqueId().toString());
                ps.executeUpdate();
                mysql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            PermissionsFile.setPath("Player." + p.getUniqueId() + ".settings.prefix", prefix);
        }

        playerPrefix.put(p.getUniqueId().toString(), prefix);

        if(p.isOnline()) {
            grantDisplayName(p.getPlayer());
        }
    }

    public static boolean resetPlayerPrefix(OfflinePlayer p) {
        if(hasPlayerPrefix(p)) {
            setPlayerPrefix(p, "");
            return true;
        }
        return false;
    }

    public static boolean hasPlayerSuffix(OfflinePlayer p) {
        if(playerSuffix.containsKey(p.getUniqueId().toString())) {
            return true;
        }

        boolean hasSuffix = !getRoleSuffix(getPlayerRole(p)).isEmpty();

        if (mySQLEnabled) {
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT SUFFIX FROM " + SQLTables.PLAYER.getTable() + " WHERE UUID=?");
                ps.setString(1, p.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();

                hasSuffix = rs.next();
                mysql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (PermissionsFile.isPathSet("Player." + p.getUniqueId() + ".settings.suffix")) {
                return true;
            }
        }
        return hasSuffix;
    }

    public static String getPlayerSuffix(OfflinePlayer p) {
        String suffix = "";

        if(playerSuffix.containsKey(p.getUniqueId().toString())) {
            return playerSuffix.get(p.getUniqueId().toString());
        }

        if (mySQLEnabled) {
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT SUFFIX FROM " + SQLTables.PLAYER.getTable() + " WHERE UUID=?");
                ps.setString(1, p.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();

                if(rs.next()) {
                    suffix = rs.getString("SUFFIX");
                }
                mysql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (PermissionsFile.isPathSet("Player." + p.getUniqueId() + ".settings.suffix")) {
                suffix = PermissionsFile.getString("Player." + p.getUniqueId() + ".settings.suffix");
            } else {
                suffix = getRoleSuffix(getPlayerRole(p));
            }
        }

        playerSuffix.put(p.getUniqueId().toString(), suffix);
        return suffix;
    }

    public static void setPlayerSuffix(OfflinePlayer p, String suffix) {
        if (mySQLEnabled) {
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement("UPDATE " + SQLTables.PLAYER.getTable() + " SET SUFFIX=? WHERE UUID=?");
                ps.setString(1, suffix);
                ps.setString(2, p.getUniqueId().toString());
                ps.executeUpdate();
                mysql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            PermissionsFile.setPath("Player." + p.getUniqueId() + ".settings.suffix", suffix);
        }

        playerSuffix.put(p.getUniqueId().toString(), suffix);

        if(p.isOnline()) {
            grantDisplayName(p.getPlayer());
        }
    }

    public static boolean resetPlayerSuffix(OfflinePlayer p) {
        if(hasPlayerSuffix(p)) {
            setPlayerSuffix(p, "");
            return true;
        }
        return false;
    }

    public static List<String> getPlayerPermissions(OfflinePlayer p) {
        List<String> permissions = new ArrayList<>();

        if(playerPermission.containsKey(p.getUniqueId().toString())) {
            return playerPermission.get(p.getUniqueId().toString());
        }

        if(mySQLEnabled) {
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement("SELECT PERMISSION FROM " + SQLTables.PLAYER_PERMISSIONS.getTable() + " WHERE UUID=?");
                ps.setString(1, p.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();

                while(rs.next()) {
                    if(!permissions.contains(rs.getString("PERMISSION"))) {
                        permissions.add(rs.getString("PERMISSION"));
                    }
                }
                mysql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            if (PermissionsFile.isPathSet("Player." + p.getUniqueId() + ".permissions")) {
                permissions = PermissionsFile.getStringList("Player." + p.getUniqueId() + ".permissions");
            }
        }
        for(String perm : permissions) {
            if(!permissions.contains(perm)) {
                permissions.add(perm);
            }
        }

        playerPermission.put(p.getUniqueId().toString(), permissions);
        return permissions;
    }

    public static void addPlayerPermission(OfflinePlayer p, String permission) {
        List<String> permissions = getPlayerPermissions(p);
        if(!permissions.contains(permission)) {
            permissions.add(permission);
            if(mySQLEnabled) {
                try {
                    PreparedStatement ps = mysql.getConnection().prepareStatement("INSERT INTO " + SQLTables.PLAYER_PERMISSIONS.getTable() + " (UUID,PERMISSION) VALUES (?,?)");
                    ps.setString(1, p.getUniqueId().toString());
                    ps.setString(2, permission);
                    ps.executeUpdate();
                    mysql.disconnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                PermissionsFile.setPath("Player." + p.getUniqueId() + ".permissions", permissions);
            }

            playerPermission.put(p.getUniqueId().toString(), permissions);

            if(p.isOnline()) {
                grantPermissions(p.getPlayer());
            }
        }
    }

    public static void addPlayerPermission(OfflinePlayer p, String[] permissionList) {
        for(String perm : permissionList) {
            addPlayerPermission(p, perm);
        }

        if(p.isOnline()) {
            grantPermissions(p.getPlayer());
        }
    }

    public static void addPlayerPermission(OfflinePlayer p, List<String> permissionList) {
        for(String perm : permissionList) {
            addPlayerPermission(p, perm);
        }

        if(p.isOnline()) {
            grantPermissions(p.getPlayer());
        }
    }

    public static boolean removePlayerPermission(OfflinePlayer p, String permission) {
        List<String> permissions = getPlayerPermissions(p);
        if (permissions.contains(permission)) {
            permissions.remove(permission);
            if (mySQLEnabled) {
                try {
                    PreparedStatement ps = mysql.getConnection().prepareStatement("DELETE FROM " + SQLTables.PLAYER_PERMISSIONS.getTable() + " WHERE UUID=? AND PERMISSION=?");
                    ps.setString(1, p.getUniqueId().toString());
                    ps.setString(2, permission);
                    ps.executeUpdate();
                    mysql.disconnect();
                    if (p.isOnline()) {
                        grantPermissions(p.getPlayer());
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                PermissionsFile.setPath("Player." + p.getUniqueId() + ".permissions", permissions);
                if (p.isOnline()) {
                    grantPermissions(p.getPlayer());
                }
            }

            playerPermission.put(p.getUniqueId().toString(), permissions);
            return true;
        }
        return false;
    }

    public static void removePlayerPermissions(OfflinePlayer p, String[] permissionList) {
        for(String perm : permissionList) {
            removePlayerPermission(p, perm);
        }

        if(p.isOnline()) {
            grantPermissions(p.getPlayer());
        }
    }

    public static void removePlayerPermissions(OfflinePlayer p, List<String> permissionList) {
        for(String perm : permissionList) {
            removePlayerPermission(p, perm);
        }

        if(p.isOnline()) {
            grantPermissions(p.getPlayer());
        }
    }

    //Other
    
    public static List<String> getAllPlayerPermissions(OfflinePlayer p) {
        List<String> permissions = new ArrayList<>();
        String role = getPlayerRole(p);

        for(String perm : getPlayerPermissions(p)) {
            if(!permissions.contains(perm)) {
                permissions.add(perm);
            }
        }
        for(String rolePerm : getRolePermissions(role)) {
            if(!permissions.contains(rolePerm)) {
                permissions.add(rolePerm);
            }
        }
        for(String inheritance : getRoleInheritance(role)) {
            for(String inheritancePerm : getRolePermissions(inheritance)) {
                if(!permissions.contains(inheritancePerm)) {
                    permissions.add(inheritancePerm);
                }
            }
        }
        return permissions;
    }

    private static final HashMap<Player, PermissionAttachment> permissionAttachment = new HashMap<>();

    public static void grantPermissions(Player p) {
        List<String> allPerms = getAllPlayerPermissions(p);
        PermissionAttachment pa;
        if(permissionAttachment.get(p) != null) {
            pa = permissionAttachment.get(p);
        }else{
            pa = p.addAttachment(NewPerm.getInstance());
            permissionAttachment.put(p, pa);
        }
        pa.getPermissions().clear();
        for(String perm : allPerms) {
            pa.setPermission(perm, true);
        }
    }

    public static void grantDisplayName(Player p) {
        String prefix;
        String suffix;
        String role = getPlayerRole(p);

        if(hasPlayerPrefix(p) && !getPlayerPrefix(p).equalsIgnoreCase("")) {
            prefix = getPlayerPrefix(p);
        }else{
            prefix = getRolePrefix(role);
        }

        if(hasPlayerSuffix(p) && !getPlayerSuffix(p).equalsIgnoreCase("")) {
            suffix = getPlayerSuffix(p);
        }else{
            suffix = getRoleSuffix(role);
        }

        p.setDisplayName(prefix + p.getName() + suffix);
    }

    public static void grantAll(Player p) {
        migrateToUUID(p);
        grantDisplayName(p);
        grantPermissions(p);
    }

    public static boolean playerHasPermission(OfflinePlayer p, String permission) {
        List<String> permissions = getAllPlayerPermissions(p);

        if(permission.equalsIgnoreCase("")) {
            return true;
        }else if(permissions.contains("*")) {
            return true;
        }else{
            for(String perm : permissions) {
                if(perm.split("")[0].equalsIgnoreCase("-")) {
                    if(permission.equalsIgnoreCase(perm.replace("-", ""))) {
                        return false;
                    }
                }

                if(permission.equalsIgnoreCase(perm)) {
                    return true;
                }

                if(perm.contains("*")) {
                    String[] neededPermission = permission.split("\\.");
                    String[] currentPermission = perm.split("\\.");
                    int smallest = (Math.min(neededPermission.length, currentPermission.length));
                    boolean havePerm = true;

                    for (int i = 0; i < smallest; i++) {
                        if (i != smallest - 1) {
                            if (!currentPermission[i].equalsIgnoreCase(neededPermission[i])) {
                                havePerm = false;
                                break;
                            }
                        } else {
                            if (!currentPermission[i].equalsIgnoreCase("*")) {
                                havePerm = false;
                                break;
                            }
                        }
                    }
                    if (havePerm) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

}
