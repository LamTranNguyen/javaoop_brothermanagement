/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Dell Precision T5810
 */
public class User {
    private String email;
    private String username;
    private String password;
    
    public User(){};
    
    public User(String email, String password){
        this.email = email;
        this.password = password;
    }
    
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public void setFullname(String fullname){
        this.username = fullname;
    }
    
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
