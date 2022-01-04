package de.bentzin.bot.command.commands;

import de.bentzin.bot.UntisBot;
import de.bentzin.bot.command.Command;
import de.bentzin.bot.command.Target;
import de.bentzin.bot.permission.Role;
import it.auties.whatsapp4j.protobuf.chat.Chat;
import it.auties.whatsapp4j.protobuf.contact.Contact;
import it.auties.whatsapp4j.protobuf.info.MessageInfo;
import it.auties.whatsapp4j.protobuf.message.standard.TextMessage;

import java.util.Arrays;
import java.util.Optional;

/**
 * The type Test command.
 */
public class TestCommand extends Command {

    /**
     * Instantiates a new Test command.
     */
    public TestCommand() {
        super("test", Role.USER, Target.GROUP, Target.DIRECT, Target.CONSOLE);
    }

    @Override
    public void execute(Chat chat, String[] args, MessageInfo label, boolean fromSystem, Optional<Contact> sender) {
        if (chat == null && label == null && sender == null) {
            System.out.println("Test successful " + Arrays.toString(args));
        } else {
            UntisBot.getApi().sendMessage(chat, new TextMessage("Test successful " + Arrays.toString(args)), label);
        }
    }
}
