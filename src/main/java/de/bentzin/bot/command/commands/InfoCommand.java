package de.bentzin.bot.command.commands;

import de.bentzin.bot.MyContact;
import de.bentzin.bot.UntisBot;
import de.bentzin.bot.command.Command;
import de.bentzin.bot.command.Target;
import de.bentzin.bot.message.MessageGenerator;
import de.bentzin.bot.permission.Role;
import it.auties.whatsapp4j.protobuf.chat.Chat;
import it.auties.whatsapp4j.protobuf.contact.Contact;
import it.auties.whatsapp4j.protobuf.info.MessageInfo;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * The type Info command.
 */
public class InfoCommand extends Command {
    private String lastInput = "";

    /*
     else if (input.startsWith("info ")) {
                    String command = input.replace("info ", "");
                    Optional<Contact> contactByName = getManager().findContactByName(command);
                    if (contactByName.isPresent()) {
                        System.out.println(MyContact.fromContact(contactByName.get()));
                    } else {
                        System.err.println("Cant get the contact \"" + command + "\"!");
                        continue;
                    }
                }
     */

    /**
     * Instantiates a new Info command.
     */
    public InfoCommand() {
        super("info", Role.MANAGER, Target.ALL);
    }

    @Override
    public void execute(@Nullable Chat chat, String[] args, @Nullable MessageInfo label, boolean fromSystem, Optional<Contact> sender) {
        Optional<Contact> contactByName = UntisBot.getManager().findContactByName(removeKey(lastInput));
        if (chat == null && (sender == null) && fromSystem) {
            if (contactByName.isPresent()) {
                System.out.println(MyContact.fromContact(contactByName.get()));
            } else {
                System.err.println("Cant get the contact \"" + removeKey(lastInput) + "\"!");
            }
        } else {
            if (contactByName.isPresent()) {
                UntisBot.getApi().sendMessage(chat, MessageGenerator.generateBotTextMessage(MyContact.fromContact(contactByName.get()).toWAString(),
                        "Information about: \n \"" + contactByName.get().jid() + "\""));
            } else {
                contactByName = UntisBot.getManager().findContactByName(chat.displayName());
                UntisBot.getApi().sendMessage(chat, MessageGenerator.generateBotTextMessage(MyContact.fromContact(contactByName).toWAString(),
                        "Information about: \n \"" + contactByName.get().jid() + "\""));

                // Bot.getApi().sendMessage(chat, MessageGenerator.generateError("Cant get the contact \"" + buildArgs(label.container().textMessage()) + "\"!"));
            }
        }
    }

    @Override
    public void input(String s) {
        lastInput = s;
    }
}
