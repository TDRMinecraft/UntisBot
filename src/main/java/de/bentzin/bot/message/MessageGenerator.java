package de.bentzin.bot.message;

import it.auties.whatsapp4j.protobuf.message.standard.TextMessage;

/**
 * The type Message generator.
 */
public class MessageGenerator {

    /**
     * Generate bot text message text message.
     *
     * @param text        the text
     * @param description the description
     * @return the text message
     */
    public static TextMessage generateBotTextMessage(String text, String description) {
        return TextMessage.newTextMessage() // Create a new text message builder
                .text(text)
                .canonicalUrl("")
                .matchedText(text.toCharArray()[0] + "")
                .title("Bot")
                .description(description)
                .create();
    }

    /**
     * Generate error text message.
     *
     * @param error the error
     * @return the text message
     */
    public static TextMessage generateError(String error) {
        return TextMessage.newTextMessage() // Create a new text message builder
                .text("Error!")
                .canonicalUrl("")
                .matchedText("!")
                .title("Bot  -  Error:")
                .description(error)
                .create();
    }

    /**
     * Generate error text message.
     *
     * @param throwable the throwable
     * @return the text message
     */
    public static TextMessage generateError(Throwable throwable) {
        return generateError(throwable.getMessage());
    }
}
