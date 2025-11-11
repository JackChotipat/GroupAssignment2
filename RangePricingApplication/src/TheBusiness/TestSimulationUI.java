/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TheBusiness;

import TheBusiness.Business.Business;
import UserInterface.Main.WorkSpaceProfiles.OrderManagement.RunSimulationPanel;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author 123
 */
public class TestSimulationUI {
     public static void main(String[] args) {
        // Use Swing's event dispatch thread
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    
    private static void createAndShowGUI() {
        // Create main frame
        JFrame frame = new JFrame("Task 4: Run Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null); // Center on screen
        
        // Load business data
        Business business = ConfigureABusiness.initialize();
        
        // Create card panel for navigation
        JPanel cardPanel = new JPanel(new CardLayout());
        
        // Create and add the simulation panel
        RunSimulationPanel simulationPanel = new RunSimulationPanel(business, cardPanel);
        cardPanel.add(simulationPanel, "Simulation");
        
        // Add to frame
        frame.add(cardPanel);
        
        // Show the frame
        frame.setVisible(true);
        
        System.out.println("✓ Task 4 UI loaded successfully!");
        System.out.println("Business has " + 
            business.getSupplierDirectory().getSuplierList().size() + 
            " suppliers with products.");
    }
}
