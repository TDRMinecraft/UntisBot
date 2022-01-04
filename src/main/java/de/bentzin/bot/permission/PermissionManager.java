package de.bentzin.bot.permission;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.Phonenumber;
import de.bentzin.facharbeit.bot.MyContact;
import it.auties.whatsapp4j.protobuf.contact.Contact;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Permission manager.
 */
public class PermissionManager {

    private Map<Phonenumber.PhoneNumber, Role> roleMap = new HashMap<>();

    /**
     * Gets role map.
     *
     * @return the role map
     */
    public Map<Phonenumber.PhoneNumber, Role> getRoleMap() {
        return roleMap;
    }

    /**
     * Sets role map.
     *
     * @param roleMap the role map
     */
    public void setRoleMap(Map<Phonenumber.PhoneNumber, Role> roleMap) {
        this.roleMap = roleMap;
    }

    /**
     * Gets role.
     *
     * @param contact the contact
     * @return the role
     */
    public Role getRole(Contact contact) {
        try {
            Phonenumber.PhoneNumber number = MyContact.fromContact(contact).getNumber();
            if (userCheck(number) != null) {
                return userCheck(number);
            }
            return roleMap.get(number);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return Role.UNKNOWN;
    }

    /**
     * Gets role.
     *
     * @param contact the contact
     * @return the role
     */
    public Role getRole(MyContact contact) {
        try {
            if (userCheck(contact.getNumber()) != null) {
                return userCheck(contact.getNumber());
            }
            return roleMap.get(contact.getNumber());
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return Role.UNKNOWN;
    }

    /**
     * Gets role.
     *
     * @param phoneNumber the phone number
     * @return the role
     */
    public Role getRole(Phonenumber.PhoneNumber phoneNumber) {
        if (userCheck(phoneNumber) != null) {
            return userCheck(phoneNumber);
        }
        return roleMap.get(phoneNumber);
    }

    private Role userCheck(Phonenumber.PhoneNumber phoneNumber) {
        if (roleMap.get(phoneNumber) == null) {
            return Role.USER;
        }
        return null;
    }
}
