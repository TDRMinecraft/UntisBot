package de.bentzin.bot;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import it.auties.whatsapp4j.protobuf.contact.Contact;
import it.auties.whatsapp4j.protobuf.contact.ContactStatus;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * The type My contact.
 */
public class MyContact extends Contact {

    /**
     * Instantiates a new My contact.
     *
     * @param jid               the jid
     * @param chosenName        the chosen name
     * @param name              the name
     * @param shortName         the short name
     * @param lastKnownPresence the last known presence
     * @param lastSeen          the last seen
     */
    public MyContact(@NotNull String jid, String chosenName, String name, String shortName, ContactStatus lastKnownPresence, ZonedDateTime lastSeen) {
        super(jid, chosenName, name, shortName, lastKnownPresence, lastSeen);
    }

    /**
     * Instantiates a new My contact.
     *
     * @param contact the contact
     */
    public MyContact(Contact contact) {
        super(contact.jid(), contact.chosenName(), contact.name(), contact.shortName(), contact.lastKnownPresence().orElse(ContactStatus.UNAVAILABLE), contact.lastSeen().orElse(null));
    }

    /**
     * From contact my contact.
     *
     * @param contact the contact
     * @return the my contact
     */
    public static MyContact fromContact(Contact contact) {
        return new MyContact(contact);
    }

    /**
     * From contact my contact.
     *
     * @param contact the contact
     * @return the my contact
     */
    @Nullable
    public static MyContact fromContact(Optional<Contact> contact) {
        if (contact.isPresent())
            return new MyContact(contact.get());
        else
            return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("-----------------------<" + chosenName() + ">-----------------------" + "\n");
        builder.append("JID: " + jid() + "\n");
        builder.append("name: " + name() + "\n");
        builder.append("shortName: " + shortName() + "\n");
        builder.append("lkp: " + lastKnownPresence().get() + "\n");
        builder.append("lastSeen: " + lastSeen().orElse(null) + "\n");
        builder.append("-----------------------" + chosenName() + "-----------------------" + "\n");
        return builder.toString();
    }

    /**
     * To wa string string.
     *
     * @return the string
     */
    public String toWAString() {
        StringBuilder builder = new StringBuilder();
        builder.append("chosenName " + chosenName() + "\n");
        builder.append("JID: " + jid() + "\n");
        builder.append("name: " + name() + "\n");
        builder.append("shortName: " + shortName() + "\n");
        builder.append("lkp: " + lastKnownPresence().get() + "\n");
        builder.append("lastSeen: " + lastSeen().orElse(null) + "\n");
        return builder.toString();
    }


    /**
     * Gets number.
     *
     * @return the number
     * @throws NumberParseException the number parse exception
     */
    public Phonenumber.PhoneNumber getNumber() throws NumberParseException {
        char[] chars = jid().toCharArray();
        String numbers = "0123456789";
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (!numbers.contains(chars[i] + "")) {
                break;
            } else
                number.append(chars[i]);
        }
        PhoneNumberUtil numberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = numberUtil.parse(number, "DE");
        return phoneNumber;
    }

    /**
     * Is contact boolean.
     *
     * @return the boolean
     */
    public boolean isContact() {
        try {
            return (chosenName() != null && hasValidNumber());
        } catch (NumberParseException e) {
            System.err.println("Number cant be parsed!");
        }
        return false;
    }

    /**
     * Has valid number boolean.
     *
     * @return the boolean
     * @throws NumberParseException the number parse exception
     */
    public boolean hasValidNumber() throws NumberParseException {
        Phonenumber.PhoneNumber phoneNumber = getNumber();
        PhoneNumberUtil numberUtil = PhoneNumberUtil.getInstance();
        return numberUtil.isValidNumber(phoneNumber);
    }
}
