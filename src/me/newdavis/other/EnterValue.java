package me.newdavis.other;
//Plugin by NewDavis

import me.newdavis.plugin.NewPerm;
import me.newdavis.manager.NewPermManager;
import me.newdavis.manager.player.PlayerMenu;
import me.newdavis.manager.player.PlayerPermissionMenu;
import me.newdavis.manager.player.PlayerRoleMenu;
import me.newdavis.manager.role.RoleInheritanceMenu;
import me.newdavis.manager.role.RoleMenu;
import me.newdavis.manager.role.RoleMenuSpecificRole;
import me.newdavis.manager.role.RolePermissionMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class EnterValue {

    public static HashMap<Player, String> forWhat = new HashMap<>();
    private static final String permPlayer = "newperm.player";
    private static final String permRole = "newperm.role";

    public static void sendChatMessage(Player p) {
        if(forWhat.containsKey(p)) {
            String forWhatString = forWhat.get(p);
            if(forWhatString.equalsIgnoreCase("perm")) {
                p.sendMessage(NewPerm.prefix + " §7Bitte gebe eine §cPermission §7ein.");
                p.sendMessage(NewPerm.prefix + " §7Zum abbrechen, schreibe §8'§ccancel§8'");
            }else if(forWhatString.equalsIgnoreCase("prefix")) {
                p.sendMessage(NewPerm.prefix + " §7Bitte gebe einen §cPrefix §7ein.");
                p.sendMessage(NewPerm.prefix + " §7Um den §cPrefix §7zurückzusetzen, schreibe §8'§creset§8'");
                p.sendMessage(NewPerm.prefix + " §7Zum abbrechen, schreibe §8'§ccancel§8'");
            }else if(forWhatString.equalsIgnoreCase("suffix")) {
                p.sendMessage(NewPerm.prefix + " §7Bitte gebe einen §cSuffix §7ein.");
                p.sendMessage(NewPerm.prefix + " §7Um den §cSuffix §7zurückzusetzen, schreibe §8'§creset§8'");
                p.sendMessage(NewPerm.prefix + " §7Zum abbrechen, schreibe §8'§ccancel§8'");
            }else if(forWhatString.equalsIgnoreCase("role")) {
                p.sendMessage(NewPerm.prefix + " §7Bitte gebe eine §cRolle §7ein.");
                p.sendMessage(NewPerm.prefix + " §7Zum abbrechen, schreibe §8'§ccancel§8'");
            }else if(forWhatString.equalsIgnoreCase("rolePerm")) {
                p.sendMessage(NewPerm.prefix + " §7Bitte gebe eine §cPermission §7ein.");
                p.sendMessage(NewPerm.prefix + " §7Zum abbrechen, schreibe §8'§ccancel§8'");
            }else if(forWhatString.equalsIgnoreCase("rolePrefix")) {
                p.sendMessage(NewPerm.prefix + " §7Bitte gebe eine §cPrefix §7ein.");
                p.sendMessage(NewPerm.prefix + " §7Zum abbrechen, schreibe §8'§ccancel§8'");
            }else if(forWhatString.equalsIgnoreCase("roleSuffix")) {
                p.sendMessage(NewPerm.prefix + " §7Bitte gebe eine §cSuffix §7ein.");
                p.sendMessage(NewPerm.prefix + " §7Zum abbrechen, schreibe §8'§ccancel§8'");
            }else{
                forWhat.remove(p);
            }
        }
    }

    public static void setValue(Player p, String value) {
        if(forWhat.containsKey(p)) {
            if(value.equalsIgnoreCase("cancel")) {
                openLastInventory(p);
                p.sendMessage(NewPerm.prefix + " §cDer Vorgang wurde abgebrochen!");
            }else if(value.equalsIgnoreCase("reset")) {
                if(NewPermManager.playerHasPermission(p, permPlayer)) {
                    if (forWhat.get(p).equalsIgnoreCase("prefix")) {
                        OfflinePlayer t = PlayerMenu.playerMenu.get(p);
                        if (NewPermManager.resetPlayerPrefix(t)) {
                            p.sendMessage(NewPerm.prefix + " §cDer Prefix von " + t.getName() + " wurde zurückgesetzt!");
                            Bukkit.getScheduler().runTask(NewPerm.getInstance(), new Runnable() {
                                @Override
                                public void run() {
                                    PlayerMenu.openPlayerMenu(p, t);
                                }
                            });
                        } else {
                            p.sendMessage(NewPerm.error);
                        }
                    } else if (forWhat.get(p).equalsIgnoreCase("suffix")) {
                        OfflinePlayer t = PlayerMenu.playerMenu.get(p);
                        if (NewPermManager.resetPlayerSuffix(t)) {
                            p.sendMessage(NewPerm.prefix + " §cDer Suffix von " + t.getName() + " wurde zurückgesetzt!");
                            Bukkit.getScheduler().runTask(NewPerm.getInstance(), new Runnable() {
                                @Override
                                public void run() {
                                    PlayerMenu.openPlayerMenu(p, t);
                                }
                            });
                        } else {
                            p.sendMessage(NewPerm.error);
                        }
                    }
                }else{
                    removeAll(p);
                }
            }else if(forWhat.get(p).equalsIgnoreCase("perm")) {
                if(NewPermManager.playerHasPermission(p, permPlayer)) {
                    OfflinePlayer t = PlayerMenu.playerMenu.get(p);
                    NewPermManager.addPlayerPermission(t, value);
                    p.sendMessage(NewPerm.prefix + " §7Die Permission §c" + value + " §7wurde §c" + t.getName() + " §7hinzugefügt§7.");
                    int page = PlayerPermissionMenu.playerPermPage.get(p);
                    Bukkit.getScheduler().runTask(NewPerm.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            PlayerPermissionMenu.openPermissionMenu(p, t, page);
                        }
                    });
                }else{
                    removeAll(p);
                }
            }else if(forWhat.get(p).equalsIgnoreCase("prefix")) {
                if(NewPermManager.playerHasPermission(p, permPlayer)) {
                    OfflinePlayer t = PlayerMenu.playerMenu.get(p);
                    NewPermManager.setPlayerPrefix(t, value);
                    p.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + t.getName() + " §7wurde zu §7" + value + " §7geändert.");
                    PlayerMenu.openPlayerMenu(p, t);
                }else{
                    removeAll(p);
                }
            }else if(forWhat.get(p).equalsIgnoreCase("suffix")) {
                if(NewPermManager.playerHasPermission(p, permPlayer)) {
                    OfflinePlayer t = PlayerMenu.playerMenu.get(p);
                    NewPermManager.setPlayerSuffix(t, value);
                    p.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + t.getName() + " §7wurde zu §7" + value + " §7geändert.");
                    Bukkit.getScheduler().runTask(NewPerm.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            PlayerMenu.openPlayerMenu(p, t);
                        }
                    });
                }else{
                    removeAll(p);
                }
            }else if(forWhat.get(p).equalsIgnoreCase("role")) {
                if(NewPermManager.playerHasPermission(p, permRole)) {
                    NewPermManager.addRole(value);
                    p.sendMessage(NewPerm.prefix + " §7Die Rolle §c" + value + " §7wurde §ahinzugefügt§7.");
                    int page = RoleMenu.roleMenuPage.get(p);
                    Bukkit.getScheduler().runTask(NewPerm.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            RoleMenu.openRoleMenu(p, page);
                        }
                    });
                }else{
                    removeAll(p);
                }
            }else if(forWhat.get(p).equalsIgnoreCase("rolePerm")) {
                if(NewPermManager.playerHasPermission(p, permRole)) {
                    String role = RoleMenu.roleMenu.get(p);
                    NewPermManager.addRolePermission(role, value);
                    p.sendMessage(NewPerm.prefix + " §7Die §cPermission §8'§7" + value + "§8' §7wurde §c" + role + " §7hinzugefügt.");
                    int page = RolePermissionMenu.rolePermissionPage.get(p);
                    Bukkit.getScheduler().runTask(NewPerm.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            RolePermissionMenu.openPermissionMenu(p, role, page);
                        }
                    });
                }else{
                    removeAll(p);
                }
            }else if(forWhat.get(p).equalsIgnoreCase("rolePrefix")) {
                if(NewPermManager.playerHasPermission(p, permRole)) {
                    String role = RoleMenu.roleMenu.get(p);
                    NewPermManager.setRolePrefix(role, value);
                    p.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + role + " §7wurde zu §7" + value + " §7geändert.");
                    Bukkit.getScheduler().runTask(NewPerm.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            RoleMenuSpecificRole.openSpecificRoleMenu(p, role);
                        }
                    });
                }else{
                    removeAll(p);
                }
            }else if(forWhat.get(p).equalsIgnoreCase("roleSuffix")) {
                if(NewPermManager.playerHasPermission(p, permRole)) {
                    String role = RoleMenu.roleMenu.get(p);
                    NewPermManager.setRoleSuffix(role, value);
                    p.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + role + " §7wurde zu §7" + value + " §7geändert.");
                    Bukkit.getScheduler().runTask(NewPerm.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            RoleMenuSpecificRole.openSpecificRoleMenu(p, role);
                        }
                    });
                }else{
                    removeAll(p);
                }
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(NewPerm.getInstance(), new Runnable() {
                @Override
                public void run() {
                    forWhat.remove(p);
                }
            }, 2);
        }
    }

    public static void openLastInventory(Player p) {
        if (forWhat.get(p).equalsIgnoreCase("prefix") || forWhat.get(p).equalsIgnoreCase("suffix")) {
            if (NewPermManager.playerHasPermission(p, permPlayer)) {
                Bukkit.getScheduler().runTask(NewPerm.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        PlayerMenu.openPlayerMenu(p, PlayerMenu.playerMenu.get(p));
                    }
                });
            } else {
                removeAll(p);
            }
        } else if (forWhat.get(p).equalsIgnoreCase("perm")) {
            if (NewPermManager.playerHasPermission(p, permPlayer)) {
                int page = PlayerPermissionMenu.playerPermPage.get(p);
                Bukkit.getScheduler().runTask(NewPerm.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        PlayerPermissionMenu.openPermissionMenu(p, PlayerMenu.playerMenu.get(p), page);
                    }
                });
            } else {
                removeAll(p);
            }
        } else if (forWhat.get(p).equalsIgnoreCase("rolePrefix") || forWhat.get(p).equalsIgnoreCase("roleSuffix")) {
            if (NewPermManager.playerHasPermission(p, permRole)) {
                String role = RoleMenu.roleMenu.get(p);
                Bukkit.getScheduler().runTask(NewPerm.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        RoleMenuSpecificRole.openSpecificRoleMenu(p, role);
                    }
                });
            } else {
                removeAll(p);
            }
        } else if (forWhat.get(p).equalsIgnoreCase("rolePerm")) {
            if (NewPermManager.playerHasPermission(p, permRole)) {
                String role = RoleMenu.roleMenu.get(p);
                int page = RolePermissionMenu.rolePermissionPage.get(p);
                Bukkit.getScheduler().runTask(NewPerm.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        RolePermissionMenu.openPermissionMenu(p, role, page);
                    }
                });
            } else {
                removeAll(p);
            }
        } else if (forWhat.get(p).equalsIgnoreCase("role")) {
            if (NewPermManager.playerHasPermission(p, permRole)) {
                int page = RoleMenu.roleMenuPage.get(p);
                Bukkit.getScheduler().runTask(NewPerm.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        RoleMenu.openRoleMenu(p, page);
                    }
                });
            } else {
                removeAll(p);
            }
        }
    }

    public static void removeRoleLists(Player p) {
        RoleMenu.roleMenu.remove(p);
        RoleMenu.roleMenuPage.remove(p);
        RolePermissionMenu.rolePermissionPage.remove(p);
        RoleInheritanceMenu.roleInheritancePage.remove(p);
    }

    public static void removePlayerLists(Player p) {
        PlayerMenu.playerMenu.remove(p);
        PlayerPermissionMenu.playerPermPage.remove(p);
        PlayerRoleMenu.playerRolePage.remove(p);
    }

    public static boolean hasToEnterValue(Player p) {
        return forWhat.containsKey(p);
    }

    public static void removeAll(Player p) {
        p.sendMessage(NewPerm.noPerm);
        p.closeInventory();
        Bukkit.getScheduler().scheduleSyncDelayedTask(NewPerm.getInstance(), new Runnable() {
            @Override
            public void run() {
                removePlayerLists(p);
                removeRoleLists(p);
                forWhat.remove(p);
            }
        }, 2);
    }

}
