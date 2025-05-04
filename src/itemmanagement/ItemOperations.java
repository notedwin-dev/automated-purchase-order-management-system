/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itemmanagement;

/**
 *
 * @author nixon
 */
import java.util.*;
//----- Act as an Interface to enter add(), delete(), clean(), update(), filter(), refresh() -----//
// ----- ABSTRACTION used ----- //
public interface ItemOperations {
    void add();
    void update();
    void clean();
    void delete();
    void refresh();
}
