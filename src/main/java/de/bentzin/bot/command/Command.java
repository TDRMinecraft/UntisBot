package de.bentzin.bot.command;

import de.bentzin.facharbeit.bot.Bot;
import de.bentzin.facharbeit.bot.MyContact;
import de.bentzin.facharbeit.bot.permission.Role;
import it.auties.whatsapp4j.protobuf.chat.Chat;
import it.auties.whatsapp4j.protobuf.contact.Contact;
import it.auties.whatsapp4j.protobuf.info.MessageInfo;
import it.auties.whatsapp4j.protobuf.message.model.MessageKey;
import it.auties.whatsapp4j.protobuf.message.standard.TextMessage;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.StringJoiner;


/**
 * The type Command.
 */
public abstract class Command {
    private final Target[] commandTargets;
    private final String key;
    private final Role permissionLevel;

    /**
     * Instantiates a new Command.
     *
     * @param key             the key
     * @param permissionLevel the permission level
     * @param commandTargets  the command targets
     */
    public Command(String key, Role permissionLevel, Target... commandTargets) {
        this.key = key;
        this.permissionLevel = permissionLevel;
        this.commandTargets = commandTargets;
    }

    /**
     * Gets permission level.
     *
     * @return the permission level
     */
    public Role getPermissionLevel() {
        return permissionLevel;
    }

    /**
     * Get command targets target [ ].
     *
     * @return the target [ ]
     */
    public Target[] getCommandTargets() {
        return commandTargets;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Execute.
     *
     * @param chat       the chat
     * @param args       the args
     * @param label      the label
     * @param fromSystem the from system
     * @param sender     the sender
     */
    public abstract void execute(@Nullable Chat chat, String[] args, @Nullable MessageInfo label, boolean fromSystem, Optional<Contact> sender);

    /**
     * Accepts boolean.
     *
     * @param contact    the contact
     * @param key        the key
     * @param messageKey the message key
     * @return the boolean
     */
    protected boolean accepts(@Nullable MyContact contact, String key, @Nullable MessageKey messageKey) {
        if (key.equals(getKey())) {
            if ((messageKey == null || messageKey.fromMe()) || Bot.getPermissionManager().getRole(contact).i <= getPermissionLevel().i)
                for (Target commandTarget : getCommandTargets()) {
                    if (commandTarget == Target.ALL) return true;
                    if (commandTarget == Target.CONSOLE && contact == null) return true;
                    if (contact != null) {
                        if (commandTarget == Target.DIRECT && contact.isContact()) return true;
                        if (commandTarget == Target.GROUP && !contact.isContact()) return true;
                    }

                }
        } else
            return false;
        return false;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                .add("commandTargets=" + Arrays.toString(commandTargets))
                .add("key='" + key + "'")
                .add("permissionLevel=" + permissionLevel)
                .toString();
    }


    /**
     * To short string string.
     *
     * @return the string
     */
    public String toShortString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                .add("key='" + key + "'")
                .add("permissionLevel=" + permissionLevel)
                .toString();
    }

    /**
     * Build args string.
     *
     * @param textMessage the text message
     * @return the string
     */
    protected String buildArgs(TextMessage textMessage) {
        return textMessage.text().replaceFirst(getKey() + " ", "");
    }

    /**
     * Remove key string.
     *
     * @param s the s
     * @return the string
     */
    @Nullable
    protected String removeKey(String s) {
        System.out.println("splitting: " + s);
        String[] split = s.split(" ", 2);
        if (split.length < 2)
            return "";
        else
            return split[1];
    }

    /**
     * Input.
     *
     * @param s the s
     */
    public void input(String s) {

    }


}

