package de.bentzin.bot;

import com.google.i18n.phonenumbers.Phonenumber;
import de.bentzin.bot.permission.Role;
import it.auties.whatsapp4j.listener.WhatsappListener;
import it.auties.whatsapp4j.protobuf.chat.Chat;
import it.auties.whatsapp4j.protobuf.info.MessageInfo;
import it.auties.whatsapp4j.protobuf.message.standard.DocumentMessage;
import it.auties.whatsapp4j.response.impl.json.UserInformationResponse;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Listener.
 */
public class Listener implements WhatsappListener {

    /**
     * The constant filePath.
     */
    public static final Path filePath = Paths.get("").toAbsolutePath();

    @Nullable
    private UserInformationResponse informationResponse = null;
    private UntisBot Bot;

    /**
     * Read out string.
     *
     * @param message the message
     * @return the string
     */
    public static String readOut(@NotNull MessageInfo message) {
        String text;
        if (message.container().textMessage() != null) {
            text = message.container().textMessage().text();
        } else if (message.container().documentMessage() != null) {
            DocumentMessage documentMessage = message.container().documentMessage();
            text = documentMessage.fileName();
            try {
                System.out.println("doc: " + documentMessage);
                Files.write(new File(filePath  + "\\"+ UntisBot.getTime().replace(":",".") + "_" + documentMessage.fileName()).toPath(), documentMessage.decodedMedia());
                System.out.println("saved: " + documentMessage.fileName() + " in " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (message.container().contactMessage() != null) {
            text = "contact: " + message.container().contactMessage().displayName();
        } else if (message.container().call() != null) {
            text = "started call";
        } else if (message.container().audioMessage() != null) {
            text = "send audio [" + message.container().audioMessage().seconds() + "s]";
        } else if (message.container().deviceSyncMessage() != null) {
            text = "DeviceSync: " + message.container().deviceSyncMessage().serializedXmlBytes();
        } else text = null;


        return text;
    }

    /**
     * Gets information response.
     *
     * @return the information response
     */
    public UserInformationResponse getInformationResponse() {
        return informationResponse;
    }

    @SneakyThrows
    @Override
    public void onLoggedIn( @NotNull UserInformationResponse info) {
        this.informationResponse = info;
        System.out.println("Login: " + info.pushname() + "@" + info.platform());
        Map<Phonenumber.PhoneNumber, Role> map = new HashMap<>();
        Bot.getPermissionManager().setRoleMap(map);
        Thread.sleep(2);
    }

    @SneakyThrows
    @Override
    public void onChats() {
        System.out.println("The Bot is now ready!");
        Bot.setChat(Bot.getManager().findChatByName("Nils Semrau").get());
        System.out.println("The Chat \"" + Bot.getChat().displayName() + "\" is now ready!");
        //  Bot.getApi().sendMessage(Bot.getChat(), "connected - " + getInformationResponse().platform() + ": " + Bot.getTime());

        //PDFTest.sendPDF(Bot.getChat(), new File("C:/Users/tureb/OneDrive/Dokumente/Keys.pdf"));
    }

    @Override
    public void onDisconnected() {
        System.out.println("The connection was closed!");
        Bot.getApi().connect();
    }
    @Override
    public void onMessageDeleted( @NotNull Chat chat,  @NotNull MessageInfo message, boolean everyone) {
        System.out.println("Message: " + message + " was deleted in " + chat.displayName() + " for everyone? : " + everyone);
    }



    @SneakyThrows
    @Override
    public void onNewMessage( @NotNull Chat chat,  @NotNull MessageInfo message) {
        //while (!message.sender().isPresent()){
        //   Thread.sleep(20);
        // }
        String text = "Unknown Type!";
        if (message.globalStatus().name().equals("SERVER_ACK")) {
            return;
        }

/*
        if (message.container().textMessage() != null) {
            text = message.container().textMessage().text();
        } else if (message.container().documentMessage() != null) {
            text = message.container().documentMessage().fileName();
        }else if(message.container().call() != null) {
            text = "started call";
        }else if(message.container().audioMessage() != null) {
            text = "send audio [" + message.container().audioMessage().seconds() +"s]";
        } else {
 */
        text = Listener.readOut(message);
        if (text == null)
            if (message.key().fromMe()) {
                System.out.println("System send an unknown message to: " + chat.displayName());
            } else {
                System.out.println("\"" + message.sender().get().name() + "\" " + "send an unknown message to: " + chat.displayName());
                return;
            }


        text = text + " | " + message.globalStatus() + " @ " + message.timestamp();

        Bot.getCommandsystem().handleInput(message);

        if (message.key().fromMe()) {
            System.out.println("System" + "@" + chat.displayName() + ": " + text);
        } else {
            System.out.println(message.sender().get().bestName().get() + "@" + chat.displayName() + ": " + text);

        }
    }

    @Override
    public void onGroupDescriptionChange( @NotNull Chat group,  @NotNull String description,  @NotNull String descriptionId) {
        System.out.println();
    }
}
