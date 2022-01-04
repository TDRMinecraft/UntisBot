package de.bentzin.bot.command.commands;

import de.bentzin.facharbeit.bot.Bot;
import de.bentzin.facharbeit.bot.command.Command;
import de.bentzin.facharbeit.bot.command.CommandSystem;
import de.bentzin.facharbeit.bot.command.Target;
import de.bentzin.facharbeit.bot.message.MessageGenerator;
import de.bentzin.facharbeit.bot.permission.Role;
import it.auties.whatsapp4j.protobuf.chat.Chat;
import it.auties.whatsapp4j.protobuf.contact.Contact;
import it.auties.whatsapp4j.protobuf.info.MessageInfo;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * The type Help command.
 */
public class HelpCommand extends Command {

    /**
     * Instantiates a new Help command.
     */
    public HelpCommand() {
        super("help", Role.USER, Target.ALL);
    }


    @Override
    public void execute(@Nullable Chat chat, String[] args, @Nullable MessageInfo label, boolean fromSystem, Optional<Contact> sender) {
        Role role = Role.UNKNOWN;
        if (chat == null) {
            System.out.println(genHelp(Role.ADMIN));
            return;
        } else {
            if (label.key().fromMe()) {
                role = Role.ADMIN;
            } else {
                role = Bot.getPermissionManager().getRole(sender.get());
            }
            Bot.getApi().sendMessage(chat,
                    MessageGenerator.generateBotTextMessage(genHelp(role),
                            "This instance is running on a pre-alpha version! : Version: 0.00!"
                    ));
        }
        return;
    }

    /**
     * Gen help string.
     *
     * @param role the role
     * @return the string
     */
    public String genHelp(Role role) {
        StringBuilder builder = new StringBuilder();
        builder.append("Commands:" + "\n");
        for (Command command : Bot.getCommandsystem().cloneCommandSet()) {
            builder.append(command.getKey() + " | " + command.getPermissionLevel().name() + "\n");
        }
        builder.append("current prefix: '" + CommandSystem.INITIATOR + "'\n");
        builder.append("Your role: " + role.name());
        return builder.toString();
    }
}
