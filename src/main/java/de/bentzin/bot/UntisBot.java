package de.bentzin.bot;

import com.beust.jcommander.internal.Nullable;
import de.bentzin.bot.command.CommandSystem;
import de.bentzin.bot.command.vp.VPTask;
import de.bentzin.bot.permission.PermissionManager;
import it.auties.whatsapp4j.manager.WhatsappDataManager;
import it.auties.whatsapp4j.protobuf.chat.Chat;
import it.auties.whatsapp4j.whatsapp.WhatsappAPI;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public final class UntisBot {

    /**
     * The constant AUTORESTART.
     */
    public static final boolean AUTORESTART = true;
    private static final WhatsappAPI api = new WhatsappAPI();
    private static final WhatsappDataManager manager = api.manager();
    private static final CommandSystem commandSystem = new CommandSystem();
    private static final PermissionManager permissionManager = new PermissionManager();
    /**
     * The constant dateFormatter.
     */
    protected static DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * The Date.
     */
    static Date date;
    @Nullable
    private static Chat chat = null;
    private static final Runnable runnable = () -> {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(new SimpleDateFormat().format(new Date(System.currentTimeMillis())));
        }
    };
    private static final Runnable shutdownhook = () -> {
        UntisBot.getApi().disconnect();
        Logger.getGlobal().warning("Bot is going offline!");
    };

    static {
        try {
            date = dateFormatter.parse("2022-12-17 09:57:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String command;
    /**
     * Instantiates a new Bot.
     *
     * @throws ParseException the parse exception
     */
    public UntisBot() throws ParseException {
    }

    protected static void init(boolean debugmode) {
        registerEvents(api);
        getApi().connect();
        Runtime.getRuntime().addShutdownHook(new Thread(shutdownhook));
        CommandSystem.registerAll();

        Timer timer = new Timer("VP-Timer");
        timer.schedule(VPTask.getInstance(), date);

        Thread thread = new Thread(runnable);
    }

    protected static void connect() {
        try {
            bot();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected static void terminate() {

    }

    /**
     * Gets permission manager.
     *
     * @return the permission manager
     */
    public static PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * Gets commandsystem.
     *
     * @return the commandsystem
     */
    public static CommandSystem getCommandsystem() {
        return commandSystem;
    }

    /**
     * Gets chat.
     *
     * @return the chat
     */
    public static Chat getChat() {
        return chat;
    }

    /**
     * Sets chat.
     *
     * @param chat the chat
     */
    public static void setChat(Chat chat) {
        UntisBot.chat = chat;
    }

    /**
     * Gets api.
     *
     * @return the api
     */
    public static WhatsappAPI getApi() {
        return api;
    }

    /**
     * Gets manager.
     *
     * @return the manager
     */
    public static WhatsappDataManager getManager() {
        return manager;
    }

    /**
     * The entry point of Bot.
     *
     * @param args the input arguments
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    private static void bot() throws IOException, InterruptedException {

        //thread.start();
        while (true) {
            Scanner userInput = new Scanner(System.in);
            System.out.println("Type \"stop\" for exit!");
            while (userInput.hasNext()) {

                String input = "";
                if (userInput.hasNext()) input = userInput.nextLine();


                if (input.equals("stop")) {
                    api.disconnect();
                    userInput.close();
                    return;
                } else if (input.equals("clear")) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Console was cleared! - may not work in any Terminal!");
                } else if (input.equals("restart")) {
                    getApi().reconnect();
                } else if (input.startsWith("print-load ")) {
                    String command = input.replace("print-load ", "");
                    @NotNull Optional<Chat> optionalChat = manager.findChatByName(command);
                    if (!optionalChat.isPresent()) {
                        System.err.println("Cant get the chat \"" + command + "\"!");
                        continue;
                    }
                    Chat chat = optionalChat.get();
                    Logger.getGlobal().warning("Loading: " + chat.displayName() + " this can take a few minutes!");
                    api.loadChatHistory(chat, Integer.MAX_VALUE).thenRun(() -> {
                        printChat(chat);
                    });

                } else if (input.startsWith("print ")) {
                    String command = input.replace("print ", "");
                    @NotNull Optional<Chat> optionalChat = manager.findChatByName(command);
                    if (!optionalChat.isPresent()) {
                        System.err.println("Cant get the chat \"" + command + "\"!");
                        continue;
                    }
                    Chat chat = optionalChat.get();
                    printChat(chat);

                } else if (input.equals("chats")) {
                    printChats();
                } else if (input.equals("relog")) {

                    System.out.println("Try to restart API session!");
                    api.connect();
                } else if (input.startsWith("@")) {
                    String s = "";
                    StringBuilder chat = new StringBuilder();
                    boolean first = true;
                    for (char c : input.toCharArray()) {
                        if (first) {
                            first = false;
                            continue;
                        }
                        if (c == '~') {
                            break;
                        }
                        chat.append(c);
                    }
                    Optional<Chat> chat1 = getManager().findChatByName(chat.toString());
                    if (!chat1.isPresent()) {
                        System.err.println("Can´t find chat! : " + chat);

                    } else {
                        input = input.replace("@" + chat + "~", "");
                        getApi().sendMessage(chat1.get(), input);
                        System.out.println("System" + "@" + chat1.get().displayName() + ": " + input);
                    }
                } else {
                    UntisBot.getCommandsystem().handleInput(input);
                }

            }

            userInput.close();
        }
    }

    private static void printChat(Chat chat) {
        System.out.println("-----------------------<" + chat.displayName() + ">-----------------------");
        chat.messages().forEach(messageInfo -> {
            if (messageInfo.key().fromMe())
                System.out.println("[" + getDayTime(messageInfo.timestamp()) + "] " + "System " + ": " + Listener.readOut(messageInfo));
            else {
                System.out.println("[" + getDayTime(messageInfo.timestamp()) + "] " + messageInfo.sender().get().bestName().get() + ": " + Listener.readOut(messageInfo));
            }
        });
        System.out.println("-----------------------" + chat.displayName() + "-----------------------");
    }

    /**
     * Register events.
     *
     * @param api the api
     */
    public static void registerEvents(WhatsappAPI api) {

        api.registerListener(new Listener());
    }

    /**
     * Print chats.
     */
    public static void printChats() {
        List<String> stringList = new ArrayList<>();
        getManager().chats().forEach(chat -> stringList.add(chat.displayName()));
        System.out.println(stringList);
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

    /**
     * Gets time.
     *
     * @param milis the milis
     * @return the time
     */
    public static String getTime(long milis) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(milis);
        return formatter.format(date);
    }

    /**
     * Gets day time.
     *
     * @param milis the milis
     * @return the day time
     */
    public static String getDayTime(long milis) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss | dd.MMM.YYYY");
        Date date = new Date(milis);
        return formatter.format(date);
    }


}
