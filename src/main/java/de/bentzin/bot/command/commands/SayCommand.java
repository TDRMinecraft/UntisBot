package de.bentzin.bot.command.commands;

import de.bentzin.bot.UntisBot;
import de.bentzin.bot.command.Command;
import de.bentzin.bot.command.CommandSystem;
import de.bentzin.bot.command.Target;
import de.bentzin.bot.message.MessageGenerator;
import de.bentzin.bot.permission.Role;
import it.auties.whatsapp4j.protobuf.chat.Chat;
import it.auties.whatsapp4j.protobuf.info.MessageInfo;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * The type Say command.
 */
public class SayCommand extends Command {

    /**
     * Instantiates a new Say command.
     */
    public SayCommand() {
        super("say", Role.MANAGER, Target.GROUP, Target.DIRECT);
    }

    @Override
    public void execute(@Nullable Chat chat, String[] args, @Nullable MessageInfo label, boolean fromSystem, Optional sender) {
        UntisBot.getApi().sendMessage(chat, MessageGenerator.generateBotTextMessage("Version: 0.00", label.container().textMessage().text().replace(CommandSystem.INITIATOR + getKey() + " ", "")));
        return;
    }
}
