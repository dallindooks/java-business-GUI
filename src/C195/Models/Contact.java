package C195.Models;

/**
 * Contact class
 */
public class Contact {
    public int contactId;
    public String contactName;
    public String email;

    /**
     * constructor for Contact class
     * @param contactId
     * @param contactName
     * @param email
     */
    public Contact (int contactId, String contactName, String email){
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    public int getContactId(){
        return contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public String getEmail() {
        return email;
    }
}
