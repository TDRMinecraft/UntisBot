package de.bentzin.bot.command;

import de.bentzin.bot.MyContact;
import de.bentzin.bot.UntisBot;
import de.bentzin.bot.command.commands.HelpCommand;
import de.bentzin.bot.command.commands.InfoCommand;
import de.bentzin.bot.command.commands.SayCommand;
import de.bentzin.bot.command.commands.TestCommand;
import it.auties.whatsapp4j.protobuf.info.MessageInfo;
import it.auties.whatsapp4j.protobuf.message.standard.TextMessage;

import java.io.IOException;
import java.util.HashSet;

/**
 * The type Command system.
 */
public class CommandSystem {


    /**
     * The constant INITIATOR.
     */
    public static final char INITIATOR = '-';

    private final HashSet<Command> commandSet = new HashSet<>();

    /**
     * Register all.
     */
    public static void registerAll() {
        System.out.println("registering Commands!");
        UntisBot.getCommandsystem().registerCommand(new TestCommand());
        UntisBot.getCommandsystem().registerCommand(new HelpCommand());
        UntisBot.getCommandsystem().registerCommand(new SayCommand());
        UntisBot.getCommandsystem().registerCommand(new InfoCommand());

        System.out.println("registerd: " + UntisBot.getCommandsystem().commandSet);
    }

    /**
     * Clone command set hash set.
     *
     * @return the hash set
     */
    public HashSet<Command> cloneCommandSet() {
        return (HashSet<Command>) commandSet.clone();
    }

    /**
     * Register command boolean.
     *
     * @param command the command
     * @return the boolean
     */
    public boolean registerCommand(Command command) {
        return commandSet.add(command);
    }

    /**
     * Handle input.
     *
     * @param messageInfo the message info
     */
    public void handleInput(MessageInfo messageInfo) {
        if (messageInfo.container().textMessage() != null) {
            String key = messageInfo.container().textMessage().text();
            if (!key.startsWith(String.valueOf(CommandSystem.INITIATOR))) return;
            key = key.replaceFirst("-", "");

            String[] strings = key.split(" ", 2);
            String[] args;
            if (strings.length > 1)
                args = strings[1].split(" ", 2);
            else
                args = new String[0];

            key = strings[0];
            for (Command command : cloneCommandSet()) {
                if (command.accepts(MyContact.fromContact(messageInfo.sender()), key, messageInfo.key())) {
                    if (messageInfo.chat().isPresent()) {
                        command.input(messageInfo.container().textMessage().text());
                        if (messageInfo.sender().get().chosenName() == null)
                            System.out.println("[CMD]: \"" + "System" + "\" executed: " + key + "!");
                        else
                            System.out.println("[CMD]: \"" + messageInfo.sender().get().chosenName() + "\" executed: " + key + "!");
                        command.execute(messageInfo.chat().get(), args, messageInfo, messageInfo.key().fromMe(), messageInfo.sender());
                    }
                    return;
                }
            }
            TextMessage message = TextMessage.newTextMessage() // Create a new text message builder
                    .text("Command not found!") // Set the text to "Command not found!"
                    .canonicalUrl("") // Set the url to "" - avoid redirect!!!
                    .matchedText("!") // Set the text to a char so it will display properly
                    .title("Bot") // Set the title of the url to Bot (looks like a sender)
                    .description("Command \" " + key + "\" was not found!" + "\n" + "Type -help to see a list of available commands!") // Set the description to the "content"
                    .create();
            UntisBot.getApi().sendMessage(messageInfo.chat().get(),
                    message);

        } else
            return;
    }

    /**
     * Handles as ConsoleInput
     *
     * @param text the text
     * @throws IOException the io exception
     */
    public void handleInput(String text) throws IOException {

        String[] strings = text.split(" ", 2);
        String[] args;
        if (strings.length > 1)
            args = strings[1].split(" ", 2);
        else
            args = new String[0];

        String key = strings[0];

        System.out.println("handling:  " + key);
        for (Command command : cloneCommandSet()) {
            if (command.accepts(null, key, null)) {
                //System.out.println(command.getKey() + "-command " + "accepting -> " + text);
                command.input(text);
                System.out.println("[CMD]: \"" + Runtime.getRuntime().exec("hostname").info().user().get() + "\" executed: " + key + "!");
                command.execute(null, args, null, true, null);

            }
        }

    }
}

