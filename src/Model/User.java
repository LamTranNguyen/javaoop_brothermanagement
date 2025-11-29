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
    private char[] password;
    
    public User(){};
    
    public User(char[] password){
        this.password = password;
    }
    
    public User(String email, char[] password){
        this.email = email;
        this.password = password;
    }
    
    public User(String email, String username, char[] password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public void setUsername(String fullname){
        this.username = fullname;
    }
    
    public void setPassword(char[] pass){
        this.password = pass;
    }
    
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public char[] getPassword() { return password; }
}
