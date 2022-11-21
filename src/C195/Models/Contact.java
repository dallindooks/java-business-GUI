package C195.Models;

public class Contact {
    public int contactId;
    public String contactName;
    public String email;

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
