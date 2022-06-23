package pl.memexurer.gaming.chat.commands;

import com.mojang.brigadier.tree.CommandNode;
import com.velocitypowered.api.command.CommandSource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.checkerframework.checker.nullness.qual.Nullable;

public record SimpleCommandMeta(Object plugin, String... name) implements
    com.velocitypowered.api.command.CommandMeta {

  @Override
  public Collection<String> getAliases() {
    return Arrays.asList(name);
  }

  @Override
  public Collection<CommandNode<CommandSource>> getHints() {
    return Collections.emptyList();
  }

  @Override
  public @Nullable Object getPlugin() {
    return plugin;
  }
}
