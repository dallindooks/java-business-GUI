package C195.Models;

import java.util.Date;

public class User {
    public int userId;
    public String username;
    public String password;
    public Date createdDate;
    public String createdBy;
    public Date lastUpdated;
    public String lastUpdatedBy;

    public User(int userId, String username, String password, Date createdDate, String createdBy, Date lastUpdated, String lastUpdatedBy){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
