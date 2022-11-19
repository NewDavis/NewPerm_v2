package me.newdavis.command;

import me.newdavis.manager.NewPermManager;
import me.newdavis.plugin.NewPerm;
import me.newdavis.manager.player.PlayerMenu;
import me.newdavis.manager.role.RoleMenu;
import me.newdavis.manager.role.RoleMenuSpecificRole;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NewPermCmd implements CommandExecutor {

    public static String permUse = "newperm.use", permPlayer = "newperm.player", permRole = "newperm.role";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(NewPermManager.playerHasPermission(p, permUse)) {
                if (args.length == 0) {
                    p.sendMessage(NewPerm.usage);
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("rolle")) {
                        if(NewPermManager.playerHasPermission(p, permRole)) {
                            RoleMenu.openRoleMenu(p, 1);
                        }else{
                            p.sendMessage(NewPerm.noPerm);
                        }
                    } else if (args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("spieler")) {
                        if(NewPermManager.playerHasPermission(p, permPlayer)) {
                            p.sendMessage(NewPerm.usagePlayer);
                        }else{
                            p.sendMessage(NewPerm.noPerm);
                        }
                    } else {
                        p.sendMessage(NewPerm.usage);
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("rolle")) {
                        String role = args[1];
                        if(NewPermManager.playerHasPermission(p, permRole)) {
                            if (role.equalsIgnoreCase("list")) {
                                List<String> roles = new ArrayList<>(NewPermManager.getRoleList());
                                String rolesString = NewPerm.prefix + " §cKeine Rollen gesetzt!";
                                for (int i = 0; i < roles.size(); i++) {
                                    if (i == 0) {
                                        rolesString = NewPerm.prefix + " §c" + roles.get(i) + "§7, §c";
                                    } else if (i == roles.size() - 1) {
                                        rolesString = rolesString + "§c" + roles.get(i);
                                    } else {
                                        rolesString = rolesString + roles.get(i) + "§7, §c";
                                    }
                                }
                                p.sendMessage(NewPerm.prefix + " §7Es sind §c" + roles.size() + " Rolle§8(§cn§8) §7erstellt.");
                                p.sendMessage(rolesString);
                            } else if (NewPermManager.getRoleList().contains(role)) {
                                RoleMenuSpecificRole.openSpecificRoleMenu(p, role);
                            } else {
                                p.sendMessage(NewPerm.usageRole);
                            }
                        }else{
                            p.sendMessage(NewPerm.noPerm);
                        }
                    } else if (args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("spieler")) {
                        if(NewPermManager.playerHasPermission(p, permPlayer)) {
                            OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
                            PlayerMenu.openPlayerMenu(p, t);
                        }else{
                            p.sendMessage(NewPerm.noPerm);
                        }
                    } else {
                        p.sendMessage(NewPerm.usage);
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("rolle")) {
                        if (NewPermManager.playerHasPermission(p, permRole)) {
                            if (args[2].equalsIgnoreCase("info")) {
                                String role = args[1];
                                if (NewPermManager.getRoleList().contains(role)) {
                                    String prefix = NewPermManager.getRolePrefix(role);
                                    String suffix = NewPermManager.getRoleSuffix(role);
                                    boolean defaultRole = NewPermManager.isRoleDefault(role);
                                    List<String> permissions = new ArrayList<>(NewPermManager.getRolePermissions(role));
                                    String permissionString = NewPerm.prefix + " §cKeine Permissions gesetzt!";
                                    for (int i = 0; i < permissions.size(); i++) {
                                        if (i == 0) {
                                            permissionString = NewPerm.prefix + " §c" + permissions.get(i) + "§7, §c";
                                        } else if (i == permissions.size() - 1) {
                                            permissionString = permissionString + "§c" + permissions.get(i);
                                        } else {
                                            permissionString = permissionString + permissions.get(i) + "§7, §c";
                                        }
                                    }
                                    List<String> inheritances = new ArrayList<>(NewPermManager.getRoleInheritance(role));
                                    String inheritanceString = NewPerm.prefix + " §cKeine Inheritances gesetzt!";
                                    for (int i = 0; i < inheritances.size(); i++) {
                                        if (i == 0) {
                                            inheritanceString = NewPerm.prefix + " §c" + inheritances.get(i) + "§7, §c";
                                        } else if (i == inheritances.size() - 1) {
                                            inheritanceString = inheritanceString + "§c" + inheritances.get(i);
                                        } else {
                                            inheritanceString = inheritanceString + inheritances.get(i) + "§7, §c";
                                        }
                                    }

                                    p.sendMessage(NewPerm.prefix + " §8§m----------------------------------");
                                    p.sendMessage(NewPerm.prefix);
                                    p.sendMessage(NewPerm.prefix + " §7Rolle §8● §7" + role);
                                    p.sendMessage(NewPerm.prefix);
                                    p.sendMessage(NewPerm.prefix + " §7Prefix §8● " + prefix + " §8(§7" + prefix.replace("§", "&") + "§8)");
                                    p.sendMessage(NewPerm.prefix + " §7Suffix §8● " + suffix + " §8(§7" + suffix.replace("§", "&") + "§8)");
                                    p.sendMessage(NewPerm.prefix + " §7Standard §8● " + (defaultRole ? "§aJa" : "§cNein"));
                                    p.sendMessage(NewPerm.prefix);
                                    p.sendMessage(NewPerm.prefix + " §7" + permissions.size() + " §7Permission(s).");
                                    p.sendMessage(permissionString);
                                    p.sendMessage(NewPerm.prefix);
                                    p.sendMessage(NewPerm.prefix + " §7" + inheritances.size() + " §7Inheritances(s).");
                                    p.sendMessage(inheritanceString);
                                    p.sendMessage(NewPerm.prefix);
                                    p.sendMessage(NewPerm.prefix + " §8§m----------------------------------");
                                } else {
                                    p.sendMessage(NewPerm.prefix + " §cDiese Rolle existiert nicht!");
                                }
                            }else if(args[2].equalsIgnoreCase("default")) {
                                String role = args[1];
                                if (NewPermManager.getRoleList().contains(role)) {
                                    NewPermManager.setDefaultRole(role);
                                    p.sendMessage(NewPerm.prefix + " §7Die §cRolle " + role + " §7ist nun die §aStandard §7Rolle!");
                                } else {
                                    p.sendMessage(NewPerm.prefix + " §cDiese Rolle existiert nicht!");
                                }
                            } else {
                                p.sendMessage(NewPerm.usageRole);
                            }
                        } else {
                            p.sendMessage(NewPerm.noPerm);
                        }
                    } else if (args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("spieler")) {
                        if (NewPermManager.playerHasPermission(p, permPlayer)) {
                            OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
                            if (args[2].equalsIgnoreCase("info")) {
                                String prefix = NewPermManager.getPlayerPrefix(t);
                                String suffix = NewPermManager.getPlayerSuffix(t);
                                String role = NewPermManager.getPlayerRole(t);
                                List<String> permissions = new ArrayList<>(NewPermManager.getPlayerPermissions(t));
                                String permissionString = NewPerm.prefix + " §cKeine Permissions gesetzt!";
                                for (int i = 0; i < permissions.size(); i++) {
                                    if (i == 0) {
                                        permissionString = NewPerm.prefix + " §c" + permissions.get(i) + "§7, §c";
                                    } else if (i == permissions.size() - 1) {
                                        permissionString = permissionString + "§c" + permissions.get(i);
                                    } else {
                                        permissionString = permissionString + permissions.get(i) + "§7, §c";
                                    }
                                }

                                p.sendMessage(NewPerm.prefix + " §8§m----------------------------------");
                                p.sendMessage(NewPerm.prefix);
                                p.sendMessage(NewPerm.prefix + " §7Name §8● §7" + t.getName());
                                p.sendMessage(NewPerm.prefix + " §7Rolle §8● §7" + role);
                                p.sendMessage(NewPerm.prefix);
                                p.sendMessage(NewPerm.prefix + " §7Prefix §8● " + prefix + " §8(§7" + prefix.replace("§", "&") + "§8)");
                                p.sendMessage(NewPerm.prefix + " §7Suffix §8● " + suffix + " §8(§7" + suffix.replace("§", "&") + "§8)");
                                p.sendMessage(NewPerm.prefix);
                                p.sendMessage(permissionString);
                                p.sendMessage(NewPerm.prefix);
                                p.sendMessage(NewPerm.prefix + " §8§m----------------------------------");
                            } else {
                                p.sendMessage(NewPerm.usagePlayer);
                            }
                        }else{
                            p.sendMessage(NewPerm.noPerm);
                        }
                    } else {
                        p.sendMessage(NewPerm.usage);
                    }
                } else if (args.length == 4) {
                    if (args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("rolle")) {
                        if(NewPermManager.playerHasPermission(p, permRole)) {
                            String role = args[1];
                            if (NewPermManager.getRoleList().contains(role)) {
                                if (args[2].equalsIgnoreCase("prefix")) {
                                    String prefix = args[3].replace("&", "§");
                                    NewPermManager.setRolePrefix(role, prefix);
                                    p.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + role + " §7wurde zu §8'§7" + prefix + "§8' §7gesetzt.");
                                } else if (args[2].equalsIgnoreCase("suffix")) {
                                    String suffix = args[3].replace("&", "§");
                                    NewPermManager.setRoleSuffix(role, suffix);
                                    p.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + role + " §7wurde zu §8'§7" + suffix + "§8' §7gesetzt.");
                                } else if (args[2].equalsIgnoreCase("permission") || args[2].equalsIgnoreCase("perm") || args[2].equalsIgnoreCase("permissions") || args[2].equalsIgnoreCase("perms")) {
                                    if(args[3].equalsIgnoreCase("list")) {
                                        List<String> permissions = new ArrayList<>(NewPermManager.getRolePermissions(role));
                                        String permissionString = NewPerm.prefix + " §cKeine Permissions gesetzt!";
                                        for (int i = 0; i < permissions.size(); i++) {
                                            if (i == 0) {
                                                permissionString = NewPerm.prefix + " §c" + permissions.get(i) + "§7, §c";
                                            } else if (i == permissions.size() - 1) {
                                                permissionString = permissionString + "§c" + permissions.get(i);
                                            } else {
                                                permissionString = permissionString + permissions.get(i) + "§7, §c";
                                            }
                                        }
                                        p.sendMessage(NewPerm.prefix + " §7Die Rolle §c" + role + " §7besitzt §c" + permissions.size() + " Permissions.");
                                        p.sendMessage(permissionString);
                                    }else {
                                        p.sendMessage(NewPerm.prefix + " §8/§cNewPerm §7Role §8<§7Role§8> §7Permission §8<§7add§8/§7remove§8/§7list§8> <§7Permission§8>");
                                    }
                                } else if (args[2].equalsIgnoreCase("inheritance") || args[2].equalsIgnoreCase("vererbungen")) {
                                    if(args[3].equalsIgnoreCase("list")) {
                                        List<String> inheritance = new ArrayList<>(NewPermManager.getRoleInheritance(role));
                                        String inheritanceString = NewPerm.prefix + " §cKeine Inheritance gesetzt!";
                                        for (int i = 0; i < inheritance.size(); i++) {
                                            if (i == 0) {
                                                inheritanceString = NewPerm.prefix + " §c" + inheritance.get(i) + "§7, §c";
                                            } else if (i == inheritance.size() - 1) {
                                                inheritanceString = inheritanceString + "§c" + inheritance.get(i);
                                            } else {
                                                inheritanceString = inheritanceString + inheritance.get(i) + "§7, §c";
                                            }
                                        }
                                        p.sendMessage(NewPerm.prefix + " §7Die Rolle §c" + role + " §7besitzt §c" + inheritance.size() + " Vererbungen.");
                                        p.sendMessage(inheritanceString);
                                    }else {
                                        p.sendMessage(NewPerm.prefix + " §8/§cNewPerm §7Role §8<§7Role§8> §7Inheritance §8<§7add§8/§7remove§8/§7list§8> <§7Inheritance§8>");
                                    }
                                } else {
                                    p.sendMessage(NewPerm.usageRole);
                                }
                            } else {
                                p.sendMessage(NewPerm.prefix + " §cDiese Rolle existiert nicht!");
                            }
                        }else{
                            p.sendMessage(NewPerm.noPerm);
                        }
                    } else if (args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("spieler")) {
                        if(NewPermManager.playerHasPermission(p, permPlayer)) {
                            OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
                            if (args[2].equalsIgnoreCase("prefix")) {
                                String prefix = args[3].replace("&", "§");
                                NewPermManager.setPlayerPrefix(t, prefix);
                                p.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + t.getName() + " §7wurde zu §8'§7" + prefix + "§8' §7gesetzt.");
                            } else if (args[2].equalsIgnoreCase("suffix")) {
                                String suffix = args[3].replace("&", "§");
                                NewPermManager.setPlayerSuffix(t, suffix);
                                p.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + t.getName() + " §7wurde zu §8'§7" + suffix + "§8' §7gesetzt.");
                            } else if (args[2].equalsIgnoreCase("permission") || args[2].equalsIgnoreCase("perm") || args[2].equalsIgnoreCase("permissions") || args[2].equalsIgnoreCase("perms")) {
                                if(args[3].equalsIgnoreCase("list")) {
                                    List<String> permissions = new ArrayList<>(NewPermManager.getPlayerPermissions(t));
                                    String permissionString = NewPerm.prefix + " §cKeine Permissions gesetzt!";
                                    for (int i = 0; i < permissions.size(); i++) {
                                        if (i == 0) {
                                            permissionString = NewPerm.prefix + " §c" + permissions.get(i) + "§7, §c";
                                        } else if (i == permissions.size() - 1) {
                                            permissionString = permissionString + "§c" + permissions.get(i);
                                        } else {
                                            permissionString = permissionString + permissions.get(i) + "§7, §c";
                                        }
                                    }
                                    p.sendMessage(NewPerm.prefix + " §7Der Spieler §c" + t.getName() + " §7besitzt §c" + permissions.size() + " Permissions.");
                                    p.sendMessage(permissionString);
                                }else {
                                    p.sendMessage(NewPerm.prefix + " §8/§cNewPerm §7Spieler §8<§7Spieler§8> §7Permissions §8<§7add§8/§7remove§8/§7list§8> <§7Permission§8>");
                                }
                            } else if (args[2].equalsIgnoreCase("role") || args[2].equalsIgnoreCase("rolle") || args[2].equalsIgnoreCase("gruppe") || args[2].equalsIgnoreCase("group")) {
                                String role = args[3];
                                NewPermManager.setPlayerRole(t, role);
                                p.sendMessage(NewPerm.prefix + " §7Die §cRolle §7von §c" + t.getName() + " §7wurde zu §8'§7" + role + "§8' §7gesetzt.");
                            } else {
                                p.sendMessage(NewPerm.usagePlayer);
                            }
                        }else{
                            p.sendMessage(NewPerm.noPerm);
                        }
                    } else {
                        p.sendMessage(NewPerm.usage);
                    }
                } else if (args.length == 5) {
                    if (args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("rolle")) {
                        if(NewPermManager.playerHasPermission(p, permRole)) {
                            String role = args[1];
                            if (NewPermManager.getRoleList().contains(role)) {
                                if (args[2].equalsIgnoreCase("prefix")) {
                                    String prefix = (args[3] + args[4]).replace("&", "§");
                                    NewPermManager.setRolePrefix(role, prefix);
                                    p.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + role + " §7wurde zu §8'§7" + prefix + "§8' §7gesetzt.");
                                } else if (args[2].equalsIgnoreCase("suffix")) {
                                    String suffix = (args[3] + args[4]).replace("&", "§");
                                    NewPermManager.setRoleSuffix(role, suffix);
                                    p.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + role + " §7wurde zu §8'§7" + suffix + "§8' §7gesetzt.");
                                } else if (args[2].equalsIgnoreCase("permission") || args[2].equalsIgnoreCase("perm") || args[2].equalsIgnoreCase("permissions") || args[2].equalsIgnoreCase("perms")) {
                                    if (args[3].equalsIgnoreCase("add")) {
                                        String permission = args[4];
                                        NewPermManager.addRolePermission(role, permission);
                                        p.sendMessage(NewPerm.prefix + " §7Die §cPermission §8'§7" + permission + "§8' §7wurde §c" + role + " §7hinzugefügt.");
                                    } else if (args[3].equalsIgnoreCase("remove")) {
                                        String permission = args[4];
                                        NewPermManager.removeRolePermission(role, permission);
                                        p.sendMessage(NewPerm.prefix + " §7Die §cPermission §8'§7" + permission + "§8' §7wurde §c" + role + " §7entfernt.");
                                    } else if (args[3].equalsIgnoreCase("list")) {
                                        List<String> permissions = new ArrayList<>(NewPermManager.getRolePermissions(role));
                                        String permissionString = "";
                                        for (int i = 0; i < permissions.size(); i++) {
                                            if (i == 0) {
                                                permissionString = NewPerm.prefix + " §c" + permissions.get(i) + "§8, §c";
                                            } else if (i == permissions.size() - 1) {
                                                permissionString = permissionString + "§c" + permissions.get(i);
                                            } else {
                                                permissionString = permissionString + permissions.get(i) + "§7, §c";
                                            }
                                        }

                                        p.sendMessage(NewPerm.prefix + " §c" + role + " §7besitzt §c" + permissions.size() + " Permission§8(§cs§8)§7.");
                                        p.sendMessage(permissionString);
                                    } else {
                                        p.sendMessage(NewPerm.prefix + " §8/§cNewPerm §7Role §8<§7Role§8> §7Permissions §8<§7add§8/§7remove§8/§7list§8> <§7Permission§8>");
                                    }
                                } else if (args[2].equalsIgnoreCase("inheritance") || args[2].equalsIgnoreCase("vererbung")) {
                                    if (args[3].equalsIgnoreCase("add")) {
                                        String inheritance = args[4];
                                        NewPermManager.addInheritance(role, inheritance);
                                        p.sendMessage(NewPerm.prefix + " §7Die §cInheritance §8'§7" + inheritance + "§8' §7wurde §c" + role + " §7hinzugefügt.");
                                    } else if (args[3].equalsIgnoreCase("remove")) {
                                        String inheritance = args[4];
                                        NewPermManager.removeInheritance(role, inheritance);
                                        p.sendMessage(NewPerm.prefix + " §7Die §cInheritance §8'§7" + inheritance + "§8' §7wurde §c" + role + " §7entfernt.");
                                    } else if (args[3].equalsIgnoreCase("list")) {
                                        List<String> inheritances = new ArrayList<>(NewPermManager.getRoleInheritance(role));
                                        String inheritanceString = "";
                                        for (int i = 0; i < inheritances.size(); i++) {
                                            if (i == 0) {
                                                inheritanceString = NewPerm.prefix + " §c" + inheritances.get(i) + "§8, §c";
                                            } else if (i == inheritances.size() - 1) {
                                                inheritanceString = inheritanceString + "§c" + inheritances.get(i);
                                            } else {
                                                inheritanceString = inheritanceString + inheritances.get(i) + "§7, §c";
                                            }
                                        }

                                        p.sendMessage(NewPerm.prefix + " §c" + role + " §7besitzt §c" + inheritances.size() + " Inheritance§8(§cs§8)§7.");
                                        p.sendMessage(inheritanceString);
                                    } else {
                                        p.sendMessage(NewPerm.prefix + " §8/§cNewPerm §7Role §8<§7Role§8> §7Inheritance §8<§7add§8/§7remove§8/§7list§8> <§7Inheritance§8>");
                                    }
                                } else {
                                    p.sendMessage(NewPerm.usageRole);
                                }
                            } else {
                                p.sendMessage(NewPerm.prefix + " §cDiese Rolle existiert nicht!");
                            }
                        }else{
                            p.sendMessage(NewPerm.noPerm);
                        }
                    } else if (args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("spieler")) {
                        if(NewPermManager.playerHasPermission(p, permPlayer)) {
                            OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
                            if (args[2].equalsIgnoreCase("prefix")) {
                                String prefix = args[3] + args[4];
                                NewPermManager.setPlayerPrefix(t, prefix.replace("&", "§"));
                                p.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + t.getName() + " §7wurde zu §8'§7" + prefix.replace("&", "§") + "§8' §7gesetzt.");
                            } else if (args[2].equalsIgnoreCase("suffix")) {
                                String suffix = args[3] + args[4];
                                NewPermManager.setPlayerSuffix(t, suffix.replace("&", "§"));
                                p.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + t.getName() + " §7wurde zu §8'§7" + suffix.replace("&", "§") + "§8' §7gesetzt.");
                            } else if (args[2].equalsIgnoreCase("permission") || args[2].equalsIgnoreCase("perm") || args[2].equalsIgnoreCase("permissions") || args[2].equalsIgnoreCase("perms")) {
                                if (args[3].equalsIgnoreCase("add")) {
                                    String permission = args[4];
                                    NewPermManager.addPlayerPermission(t, permission);
                                    p.sendMessage(NewPerm.prefix + " §7Die §cPermission §8'§7" + permission + "§8' §7wurde §c" + t.getName() + " §7hinzugefügt.");
                                } else if (args[3].equalsIgnoreCase("remove")) {
                                    String permission = args[4];
                                    NewPermManager.removePlayerPermission(t, permission);
                                    p.sendMessage(NewPerm.prefix + " §7Die §cPermission §8'§7" + permission + "§8' §7wurde §c" + t.getName() + " §7entfernt.");
                                } else if (args[3].equalsIgnoreCase("list")) {
                                    List<String> permissions = new ArrayList<>(NewPermManager.getPlayerPermissions(t));
                                    String permissionString = "";
                                    for (int i = 0; i < permissions.size(); i++) {
                                        if (i == 0) {
                                            permissionString = NewPerm.prefix + " §c" + permissions.get(i) + "§8, §c";
                                        } else if (i == permissions.size() - 1) {
                                            permissionString = permissionString + "§c" + permissions.get(i);
                                        } else {
                                            permissionString = permissionString + permissions.get(i) + "§7, §c";
                                        }
                                    }

                                    p.sendMessage(NewPerm.prefix + " §c" + t.getName() + " §7besitzt §c" + permissions.size() + " Permission§8(§cs§8)§7.");
                                    p.sendMessage(permissionString);
                                } else {
                                    p.sendMessage(NewPerm.prefix + " §8/§cNewPerm §7Spieler §8<§7Spieler§8> §7Permissions §8<§7add§8/§7remove§8/§7list§8> <§7Permission§8>");
                                }
                            } else {
                                p.sendMessage(NewPerm.usagePlayer);
                            }
                        }else{
                            p.sendMessage(NewPerm.noPerm);
                        }
                    } else {
                        p.sendMessage(NewPerm.usage);
                    }
                } else {
                    if (args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("rolle")) {
                        if(NewPermManager.playerHasPermission(p, permRole)) {
                            String role = args[1];
                            if (NewPermManager.getRoleList().contains(role)) {
                                if (args[2].equalsIgnoreCase("prefix")) {
                                    String prefix = "";
                                    for (int i = 3; i < args.length; i++) {
                                        if (i == 3) {
                                            prefix = args[i].replace("&", "§");
                                        } else {
                                            prefix = prefix + " " + args[i].replace("&", "§");
                                        }
                                    }
                                    NewPermManager.setRolePrefix(role, prefix.replace("&", "§"));
                                    p.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + role + " §7wurde zu §8'§7" + prefix.replace("&", "§") + "§8' §7gesetzt.");
                                } else if (args[2].equalsIgnoreCase("suffix")) {
                                    String suffix = args[3] + args[4];
                                    for (int i = 3; i < args.length; i++) {
                                        if (i == 3) {
                                            suffix = args[i].replace("&", "§");
                                        } else {
                                            suffix = suffix + " " + args[i].replace("&", "§");
                                        }
                                    }
                                    NewPermManager.setRoleSuffix(role, suffix);
                                    p.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + role + " §7wurde zu §8'§7" + suffix + "§8' §7gesetzt.");
                                } else {
                                    p.sendMessage(NewPerm.usageRole);
                                }
                            } else {
                                p.sendMessage(NewPerm.prefix + " §cDiese Rolle existiert nicht!");
                            }
                        }else{
                            p.sendMessage(NewPerm.noPerm);
                        }
                    } else if (args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("spieler")) {
                        if(NewPermManager.playerHasPermission(p, permPlayer)) {
                            OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
                            if (args[2].equalsIgnoreCase("prefix")) {
                                String prefix = "";
                                for (int i = 3; i < args.length; i++) {
                                    if (i == 3) {
                                        prefix = args[i].replace("&", "§");
                                    } else {
                                        prefix = prefix + " " + args[i].replace("&", "§");
                                    }
                                }
                                NewPermManager.setPlayerPrefix(t, prefix.replace("&", "§"));
                                p.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + t.getName() + " §7wurde zu §8'§7" + prefix.replace("&", "§") + "§8' §7gesetzt.");
                            } else if (args[2].equalsIgnoreCase("suffix")) {
                                String suffix = args[3] + args[4];
                                for (int i = 3; i < args.length; i++) {
                                    if (i == 3) {
                                        suffix = args[i].replace("&", "§");
                                    } else {
                                        suffix = suffix + " " + args[i].replace("&", "§");
                                    }
                                }
                                NewPermManager.setPlayerSuffix(t, suffix);
                                p.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + t.getName() + " §7wurde zu §8'§7" + suffix + "§8' §7gesetzt.");
                            } else {
                                p.sendMessage(NewPerm.usagePlayer);
                            }
                        }else{
                            p.sendMessage(NewPerm.noPerm);
                        }
                    } else {
                        p.sendMessage(NewPerm.usage);
                    }
                }
            }else{
                p.sendMessage(NewPerm.noPerm);
            }
        }else{
            if(args.length == 0) {
                sender.sendMessage(NewPerm.usage);
            }else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("rolle")) {
                    sender.sendMessage(NewPerm.usageRole);
                }else if(args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("spieler")) {
                    sender.sendMessage(NewPerm.usagePlayer);
                }
            }else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("rolle")) {
                    String role = args[1];
                    if(role.equalsIgnoreCase("list")) {
                        List<String> roles = new ArrayList<>(NewPermManager.getRoleList());
                        String rolesString = NewPerm.prefix + " §cKeine Rollen gesetzt!";
                        for(int i = 0; i < roles.size(); i++) {
                            if(i == 0) {
                                rolesString = NewPerm.prefix + " §c" + roles.get(i) + "§7, §c";
                            }else if(i == roles.size()-1) {
                                rolesString = rolesString + "§c" + roles.get(i);
                            }else{
                                rolesString = rolesString + roles.get(i) + "§7, §c";
                            }
                        }
                        sender.sendMessage(NewPerm.prefix + " §7Es sind §c" + roles.size() + " Rolle§8(§cn§8) §7erstellt.");
                        sender.sendMessage(rolesString);
                    }else{
                        sender.sendMessage(NewPerm.usageRole);
                    }
                }else{
                    sender.sendMessage(NewPerm.usage);
                }
            }else if(args.length == 3) {
                if(args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("rolle")) {
                    if(args[2].equalsIgnoreCase("info")) {
                        String role = args[1];
                        if(NewPermManager.getRoleList().contains(role)) {
                            String prefix = NewPermManager.getRolePrefix(role);
                            String suffix = NewPermManager.getRoleSuffix(role);
                            boolean defaultRole = NewPermManager.isRoleDefault(role);
                            List<String> permissions = new ArrayList<>(NewPermManager.getRolePermissions(role));
                            String permissionString = NewPerm.prefix + " §cKeine Permissions gesetzt!";
                            for (int i = 0; i < permissions.size(); i++) {
                                if (i == 0) {
                                    permissionString = NewPerm.prefix + " §c" + permissions.get(i) + "§7, §c";
                                } else if (i == permissions.size() - 1) {
                                    permissionString = permissionString + "§c" + permissions.get(i);
                                } else {
                                    permissionString = permissionString + permissions.get(i) + "§7, §c";
                                }
                            }
                            List<String> inheritances = new ArrayList<>(NewPermManager.getRoleInheritance(role));
                            String inheritanceString = NewPerm.prefix + " §cKeine Inheritances gesetzt!";
                            for (int i = 0; i < inheritances.size(); i++) {
                                if (i == 0) {
                                    inheritanceString = NewPerm.prefix + " §c" + inheritances.get(i) + "§7, §c";
                                } else if (i == inheritances.size() - 1) {
                                    inheritanceString = inheritanceString + "§c" + inheritances.get(i);
                                } else {
                                    inheritanceString = inheritanceString + inheritances.get(i) + "§7, §c";
                                }
                            }

                            sender.sendMessage(NewPerm.prefix + " §8§m----------------------------------");
                            sender.sendMessage(NewPerm.prefix);
                            sender.sendMessage(NewPerm.prefix + " §7Rolle §8● §7" + role);
                            sender.sendMessage(NewPerm.prefix);
                            sender.sendMessage(NewPerm.prefix + " §7Prefix §8● " + prefix + " §8(§7" + prefix.replace("§", "&") + "§8)");
                            sender.sendMessage(NewPerm.prefix + " §7Suffix §8● " + suffix + " §8(§7" + suffix.replace("§", "&") + "§8)");
                            sender.sendMessage(NewPerm.prefix + " §7Standard §8● " + (defaultRole ? "§aJa" : "§cNein"));
                            sender.sendMessage(NewPerm.prefix);
                            sender.sendMessage(NewPerm.prefix + " §7" + permissions.size() + " §7Permission(s).");
                            sender.sendMessage(permissionString);
                            sender.sendMessage(NewPerm.prefix);
                            sender.sendMessage(NewPerm.prefix + " §7" + inheritances.size() + " §7Inheritances(s).");
                            sender.sendMessage(inheritanceString);
                            sender.sendMessage(NewPerm.prefix);
                            sender.sendMessage(NewPerm.prefix + " §8§m----------------------------------");
                        }else{
                            sender.sendMessage(NewPerm.prefix + " §cDiese Rolle existiert nicht!");
                        }
                    }else if(args[2].equalsIgnoreCase("default")) {
                        String role = args[1];
                        if (NewPermManager.getRoleList().contains(role)) {
                            NewPermManager.setDefaultRole(role);
                            sender.sendMessage(NewPerm.prefix + " §7Die §cRolle " + role + " §7ist nun die §aStandard §7Rolle!");
                        } else {
                            sender.sendMessage(NewPerm.prefix + " §cDiese Rolle existiert nicht!");
                        }
                    }else{
                        sender.sendMessage(NewPerm.usageRole);
                    }
                }else if(args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("spieler")) {
                    OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
                    if(args[2].equalsIgnoreCase("info")) {
                        String prefix = NewPermManager.getPlayerPrefix(t);
                        String suffix = NewPermManager.getPlayerSuffix(t);
                        String role = NewPermManager.getPlayerRole(t);
                        List<String> permissions = new ArrayList<>(NewPermManager.getPlayerPermissions(t));
                        String permissionString = NewPerm.prefix + " §cKeine Permissions gesetzt!";
                        for(int i = 0; i < permissions.size(); i++) {
                            if(i == 0) {
                                permissionString = NewPerm.prefix + " §c" + permissions.get(i) + "§7, §c";
                            }else if(i == permissions.size()-1) {
                                permissionString = permissionString + "§c" + permissions.get(i);
                            }else{
                                permissionString = permissionString + permissions.get(i) + "§7, §c";
                            }
                        }

                        sender.sendMessage(NewPerm.prefix + " §8§m----------------------------------");
                        sender.sendMessage(NewPerm.prefix);
                        sender.sendMessage(NewPerm.prefix + " §7Name §8● §7" + t.getName());
                        sender.sendMessage(NewPerm.prefix + " §7Rolle §8● §7" + role);
                        sender.sendMessage(NewPerm.prefix);
                        sender.sendMessage(NewPerm.prefix + " §7Prefix §8● " + prefix + " §8(§7" + prefix.replace("§", "&") + "§8)");
                        sender.sendMessage(NewPerm.prefix + " §7Suffix §8● " + suffix + " §8(§7" + suffix.replace("§", "&") + "§8)");
                        sender.sendMessage(NewPerm.prefix);
                        sender.sendMessage(permissionString);
                        sender.sendMessage(NewPerm.prefix);
                        sender.sendMessage(NewPerm.prefix + " §8§m----------------------------------");
                    }else{
                        sender.sendMessage(NewPerm.usagePlayer);
                    }
                }else{
                    sender.sendMessage(NewPerm.usage);
                }
            }else if(args.length == 4) {
                if(args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("rolle")) {
                    String role = args[1];
                    if(NewPermManager.getRoleList().contains(role)) {
                        if (args[2].equalsIgnoreCase("prefix")) {
                            String prefix = args[3].replace("&", "§");
                            NewPermManager.setRolePrefix(role, prefix);
                            sender.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + role + " §7wurde zu §8'§7" + prefix + "§8' §7gesetzt.");
                        } else if (args[2].equalsIgnoreCase("suffix")) {
                            String suffix = args[3].replace("&", "§");
                            NewPermManager.setRoleSuffix(role, suffix);
                            sender.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + role + " §7wurde zu §8'§7" + suffix + "§8' §7gesetzt.");
                        } else if (args[2].equalsIgnoreCase("permission") || args[2].equalsIgnoreCase("perm") || args[2].equalsIgnoreCase("permissions") || args[2].equalsIgnoreCase("perms")) {
                            if(args[3].equalsIgnoreCase("list")) {
                                List<String> permissions = new ArrayList<>(NewPermManager.getRolePermissions(role));
                                String permissionString = NewPerm.prefix + " §cKeine Permissions gesetzt!";
                                for (int i = 0; i < permissions.size(); i++) {
                                    if (i == 0) {
                                        permissionString = NewPerm.prefix + " §c" + permissions.get(i) + "§7, §c";
                                    } else if (i == permissions.size() - 1) {
                                        permissionString = permissionString + "§c" + permissions.get(i);
                                    } else {
                                        permissionString = permissionString + permissions.get(i) + "§7, §c";
                                    }
                                }
                                sender.sendMessage(NewPerm.prefix + " §7Die Rolle §c" + role + " §7besitzt §c" + permissions.size() + " Permissions.");
                                sender.sendMessage(permissionString);
                            }else {
                                sender.sendMessage(NewPerm.prefix + " §8/§cNewPerm §7Role §8<§7Role§8> §7Permission §8<§7add§8/§7remove§8/§7list§8> <§7Permission§8>");
                            }
                        } else if (args[2].equalsIgnoreCase("inheritance") || args[2].equalsIgnoreCase("vererbungen")) {
                            if(args[3].equalsIgnoreCase("list")) {
                                List<String> inheritance = new ArrayList<>(NewPermManager.getRoleInheritance(role));
                                String inheritanceString = NewPerm.prefix + " §cKeine Inheritance gesetzt!";
                                for (int i = 0; i < inheritance.size(); i++) {
                                    if (i == 0) {
                                        inheritanceString = NewPerm.prefix + " §c" + inheritance.get(i) + "§7, §c";
                                    } else if (i == inheritance.size() - 1) {
                                        inheritanceString = inheritanceString + "§c" + inheritance.get(i);
                                    } else {
                                        inheritanceString = inheritanceString + inheritance.get(i) + "§7, §c";
                                    }
                                }
                                sender.sendMessage(NewPerm.prefix + " §7Die Rolle §c" + role + " §7besitzt §c" + inheritance.size() + " Vererbungen.");
                                sender.sendMessage(inheritanceString);
                            }else {
                                sender.sendMessage(NewPerm.prefix + " §8/§cNewPerm §7Role §8<§7Role§8> §7Inheritance §8<§7add§8/§7remove§8/§7list§8> <§7Inheritance§8>");
                            }
                        } else {
                            sender.sendMessage(NewPerm.usageRole);
                        }
                    }else{
                        sender.sendMessage(NewPerm.prefix + " §cDiese Rolle existiert nicht!");
                    }
                }else if(args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("spieler")){
                    OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
                    if(args[2].equalsIgnoreCase("prefix")) {
                        String prefix = args[3].replace("&", "§");
                        NewPermManager.setPlayerPrefix(t, prefix);
                        sender.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + t.getName() + " §7wurde zu §8'§7" + prefix + "§8' §7gesetzt.");
                    }else if(args[2].equalsIgnoreCase("suffix")) {
                        String suffix = args[3].replace("&", "§");
                        NewPermManager.setPlayerSuffix(t, suffix);
                        sender.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + t.getName() + " §7wurde zu §8'§7" + suffix + "§8' §7gesetzt.");
                    } else if (args[2].equalsIgnoreCase("permission") || args[2].equalsIgnoreCase("perm") || args[2].equalsIgnoreCase("permissions") || args[2].equalsIgnoreCase("perms")) {
                        if(args[3].equalsIgnoreCase("list")) {
                            List<String> permissions = new ArrayList<>(NewPermManager.getPlayerPermissions(t));
                            String permissionString = NewPerm.prefix + " §cKeine Permissions gesetzt!";
                            for (int i = 0; i < permissions.size(); i++) {
                                if (i == 0) {
                                    permissionString = NewPerm.prefix + " §c" + permissions.get(i) + "§7, §c";
                                } else if (i == permissions.size() - 1) {
                                    permissionString = permissionString + "§c" + permissions.get(i);
                                } else {
                                    permissionString = permissionString + permissions.get(i) + "§7, §c";
                                }
                            }
                            sender.sendMessage(NewPerm.prefix + " §7Der Spieler §c" + t.getName() + " §7besitzt §c" + permissions.size() + " Permissions.");
                            sender.sendMessage(permissionString);
                        }else {
                            sender.sendMessage(NewPerm.prefix + " §8/§cNewPerm §7Spieler §8<§7Spieler§8> §7Permissions §8<§7add§8/§7remove§8/§7list§8> <§7Permission§8>");
                        }
                    }else if(args[2].equalsIgnoreCase("role") || args[2].equalsIgnoreCase("rolle") || args[2].equalsIgnoreCase("gruppe") || args[2].equalsIgnoreCase("group")) {
                        String role = args[3];
                        NewPermManager.setPlayerRole(t, role);
                        sender.sendMessage(NewPerm.prefix + " §7Die §cRolle §7von §c" + t.getName() + " §7wurde zu §8'§7" + role + "§8' §7gesetzt.");
                    }else{
                        sender.sendMessage(NewPerm.usagePlayer);
                    }
                }else{
                    sender.sendMessage(NewPerm.usage);
                }
            }else if(args.length == 5) {
                if(args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("rolle")) {
                    String role = args[1];
                    if(NewPermManager.getRoleList().contains(role)) {
                        if (args[2].equalsIgnoreCase("prefix")) {
                            String prefix = (args[3] + args[4]).replace("&", "§");
                            NewPermManager.setRolePrefix(role, prefix);
                            sender.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + role + " §7wurde zu §8'§7" + prefix + "§8' §7gesetzt.");
                        } else if (args[2].equalsIgnoreCase("suffix")) {
                            String suffix = (args[3] + args[4]).replace("&", "§");
                            NewPermManager.setRoleSuffix(role, suffix);
                            sender.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + role + " §7wurde zu §8'§7" + suffix + "§8' §7gesetzt.");
                        } else if (args[2].equalsIgnoreCase("permission") || args[2].equalsIgnoreCase("perm") || args[2].equalsIgnoreCase("permissions") || args[2].equalsIgnoreCase("perms")) {
                            if(args[3].equalsIgnoreCase("add")) {
                                String permission = args[4];
                                NewPermManager.addRolePermission(role, permission);
                                sender.sendMessage(NewPerm.prefix + " §7Die §cPermission §8'§7" + permission + "§8' §7wurde §c" + role + " §7hinzugefügt.");
                            }else if(args[3].equalsIgnoreCase("remove")) {
                                String permission = args[4];
                                NewPermManager.removeRolePermission(role, permission);
                                sender.sendMessage(NewPerm.prefix + " §7Die §cPermission §8'§7" + permission + "§8' §7wurde §c" + role + " §7entfernt.");
                            }else{
                                sender.sendMessage(NewPerm.prefix + " §8/§cNewPerm §7Role §8<§7Role§8> §7Permissions §8<§7add§8/§7remove§8/§7list§8> <§7Permission§8>");
                            }
                        } else if (args[2].equalsIgnoreCase("inheritance") || args[2].equalsIgnoreCase("vererbung")) {
                            if(args[3].equalsIgnoreCase("add")) {
                                String inheritance = args[4];
                                NewPermManager.addInheritance(role, inheritance);
                                sender.sendMessage(NewPerm.prefix + " §7Die §cPermission §8'§7" + inheritance + "§8' §7wurde §c" + role + " §7hinzugefügt.");
                            }else if(args[3].equalsIgnoreCase("remove")) {
                                String inheritance = args[4];
                                NewPermManager.removeInheritance(role, inheritance);
                                sender.sendMessage(NewPerm.prefix + " §7Die §cInheritance §8'§7" + inheritance + "§8' §7wurde §c" + role + " §7entfernt.");
                            }else {
                                sender.sendMessage(NewPerm.prefix + " §8/§cNewPerm §7Role §8<§7Role§8> §7Inheritance §8<§7add§8/§7remove§8/§7list§8> <§7Inheritance§8>");
                            }
                        } else {
                            sender.sendMessage(NewPerm.usageRole);
                        }
                    }else{
                        sender.sendMessage(NewPerm.prefix + " §cDiese Rolle existiert nicht!");
                    }
                }else if(args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("spieler")){
                    OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
                    if(args[2].equalsIgnoreCase("prefix")) {
                        String prefix = args[3] + args[4];
                        NewPermManager.setPlayerPrefix(t, prefix.replace("&", "§"));
                        sender.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + t.getName() + " §7wurde zu §8'§7" + prefix.replace("&", "§") + "§8' §7gesetzt.");
                    }else if(args[2].equalsIgnoreCase("suffix")) {
                        String suffix = args[3] + args[4];
                        NewPermManager.setPlayerSuffix(t, suffix.replace("&", "§"));
                        sender.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + t.getName() + " §7wurde zu §8'§7" + suffix.replace("&", "§") + "§8' §7gesetzt.");
                    }else if(args[2].equalsIgnoreCase("permission") || args[2].equalsIgnoreCase("perm") || args[2].equalsIgnoreCase("permissions") || args[2].equalsIgnoreCase("perms")) {
                        if(args[3].equalsIgnoreCase("add")) {
                            String permission = args[4];
                            NewPermManager.addPlayerPermission(t, permission);
                            sender.sendMessage(NewPerm.prefix + " §7Die §cPermission §8'§7" + permission + "§8' §7wurde §c" + t.getName() + " §7hinzugefügt.");
                        }else if(args[3].equalsIgnoreCase("remove")) {
                            String permission = args[4];
                            NewPermManager.removePlayerPermission(t, permission);
                            sender.sendMessage(NewPerm.prefix + " §7Die §cPermission §8'§7" + permission + "§8' §7wurde §c" + t.getName() + " §7entfernt.");
                        }else{
                            sender.sendMessage(NewPerm.prefix + " §8/§cNewPerm §7Spieler §8<§7Spieler§8> §7Permissions §8<§7add§8/§7remove§8/§7list§8> <§7Permission§8>");
                        }
                    }else{
                        sender.sendMessage(NewPerm.usagePlayer);
                    }
                }else{
                    sender.sendMessage(NewPerm.usage);
                }
            }else{
                if(args[0].equalsIgnoreCase("role") || args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("rolle")) {
                    String role = args[1];
                    if(NewPermManager.getRoleList().contains(role)) {
                        if (args[2].equalsIgnoreCase("prefix")) {
                            String prefix = "";
                            for (int i = 3; i < args.length; i++) {
                                if (i == 3) {
                                    prefix = args[i].replace("&", "§");
                                } else {
                                    prefix = prefix + " " + args[i].replace("&", "§");
                                }
                            }
                            NewPermManager.setRolePrefix(role, prefix.replace("&", "§"));
                            sender.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + role + " §7wurde zu §8'§7" + prefix.replace("&", "§") + "§8' §7gesetzt.");
                        } else if (args[2].equalsIgnoreCase("suffix")) {
                            String suffix = args[3] + args[4];
                            for (int i = 3; i < args.length; i++) {
                                if (i == 3) {
                                    suffix = args[i].replace("&", "§");
                                } else {
                                    suffix = suffix + " " + args[i].replace("&", "§");
                                }
                            }
                            NewPermManager.setRoleSuffix(role, suffix);
                            sender.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + role + " §7wurde zu §8'§7" + suffix + "§8' §7gesetzt.");
                        } else {
                            sender.sendMessage(NewPerm.usageRole);
                        }
                    }else{
                        sender.sendMessage(NewPerm.prefix + " §cDiese Rolle existiert nicht!");
                    }
                }else if(args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("user") || args[0].equalsIgnoreCase("spieler")){
                    OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
                    if(args[2].equalsIgnoreCase("prefix")) {
                        String prefix = "";
                        for(int i = 3; i < args.length; i++) {
                            if(i == 3) {
                                prefix = args[i].replace("&", "§");
                            }else{
                                prefix = prefix + " " + args[i].replace("&", "§");
                            }
                        }
                        NewPermManager.setPlayerPrefix(t, prefix.replace("&", "§"));
                        sender.sendMessage(NewPerm.prefix + " §7Der §cPrefix §7von §c" + t.getName() + " §7wurde zu §8'§7" + prefix.replace("&", "§") + "§8' §7gesetzt.");
                    }else if(args[2].equalsIgnoreCase("suffix")) {
                        String suffix = args[3] + args[4];
                        for(int i = 3; i < args.length; i++) {
                            if(i == 3) {
                                suffix = args[i].replace("&", "§");
                            }else{
                                suffix = suffix + " " + args[i].replace("&", "§");
                            }
                        }
                        NewPermManager.setPlayerSuffix(t, suffix);
                        sender.sendMessage(NewPerm.prefix + " §7Der §cSuffix §7von §c" + t.getName() + " §7wurde zu §8'§7" + suffix + "§8' §7gesetzt.");
                    }else{
                        sender.sendMessage(NewPerm.usagePlayer);
                    }
                }else{
                    sender.sendMessage(NewPerm.usage);
                }
            }
        }
        return false;
    }
}
