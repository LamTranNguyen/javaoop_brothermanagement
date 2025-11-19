/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package brothermanagement;

/**
 *
 * @author ThinkBook
 */
public class BrotherManagement {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Loginform logframe = new Loginform();
        logframe.setTitle("Log in");
        logframe.setVisible(true);
        logframe.pack();
        logframe.setLocationRelativeTo(null);
    }
    
}
