package io.github.bedwarsrel.commands;

import io.github.bedwarsrel.BedwarsRel;
import java.util.ArrayList;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand extends BaseCommand implements ICommand {

  public StatsCommand(BedwarsRel plugin) {
    super(plugin);
  }

  @Override
  public boolean execute(CommandSender sender, ArrayList<String> args) {
    Player player = (Player) sender;
    player.sendMessage("\n");
    return false;
  }

  @Override
  public String[] getArguments() {
    return new String[]{};
  }

  @Override
  public String getCommand() {
    return "stats";
  }

  @Override
  public String getDescription() {
    return BedwarsRel._l("commands.stats.desc");
  }

  @Override
  public String getName() {
    return BedwarsRel._l("commands.stats.name");
  }

  @Override
  public String getPermission() {
    return "base";
  }
}
