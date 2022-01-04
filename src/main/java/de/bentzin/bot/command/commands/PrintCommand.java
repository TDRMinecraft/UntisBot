package de.bentzin.bot.command.commands;

import de.bentzin.bot.command.Command;
import it.auties.whatsapp4j.protobuf.chat.Chat;
import it.auties.whatsapp4j.protobuf.contact.Contact;
import it.auties.whatsapp4j.protobuf.info.MessageInfo;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * The type Print command.
 */
public class PrintCommand extends Command {

    /**
     * Instantiates a new Print command.
     */
    public PrintCommand() {
        super(null,null,null,null);
    }
    @Override
    public void execute(@Nullable Chat chat, String[] args, @Nullable MessageInfo label, boolean fromSystem, Optional<Contact> sender) {

    }
}
