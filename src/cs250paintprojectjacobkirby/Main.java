/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cs250paintprojectjacobkirby;
import javax.swing.SwingUtilities;
/**
 *
 * @author Norboc
 */
public class Main {
    //jvm calls this first, gonna be honest idk how it works just know I need it
    public static void main(String[] args){
        //we send this to EDT, to keep code thread safe no race conditions wanted.
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PaintApp();
            }
        });
    }  
}