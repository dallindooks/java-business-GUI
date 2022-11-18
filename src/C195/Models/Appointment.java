package C195.Models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class Appointment {
    public int appointmentId, customerId, userId, contactId;
    public String title;
    public String description;
    public String location;
    public String type;
    public LocalDateTime start;
    public LocalDateTime end;
    public Date createDate;
    public String createdBy;
    public Timestamp lastUpdated;
    public String lastUpdatedBy;

    public Appointment(int appointmentId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end,
                       Date createDate, String createdBy, Timestamp lastUpdated, String lastUpdatedBy, int customerId, int userId, int contactId){
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    public int getAppointmentId() {

        return appointmentId;
    }


    public String getTitle() {

        return title;
    }

    public String getDescription() {

        return description;
    }

    public String getLocation() {

        return location;
    }

    public String getType() {

        return type;
    }


    public LocalDateTime getStart() {

        return start;
    }

    public LocalDateTime getEnd() {

        return end;
    }

    public int getCustomerId () {

        return customerId;
    }

    public int getUserId() {

        return userId;
    }

    public int getContactId() {

        return contactId;
    }

}
