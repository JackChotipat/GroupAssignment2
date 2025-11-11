/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TheBusiness;

import TheBusiness.Business.Business;
import UserInterface.Main.WorkSpaceProfiles.OrderManagement.RunSimulationPanel;
import UserInterface.Main.WorkSpaceProfiles.OrderManagement.OptimizePanel;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author 123
 */
public class TestSimulationUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            showTaskSelector();
        });
    }
    
    private static void showTaskSelector() {
        String[] options = {"Task 4: Run Simulation", "Task 5: Optimize Profit"};
        int choice = JOptionPane.showOptionDialog(null,
            "Which task would you like to test?",
            "Select Task",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (choice == 0) {
            createTask4GUI();
        } else if (choice == 1) {
            createTask5GUI();
        }
    }
    
    private static void createTask4GUI() {
        JFrame frame = new JFrame("Task 4: Run Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        
        Business business = ConfigureABusiness.initialize();
        JPanel cardPanel = new JPanel(new CardLayout());
        
        RunSimulationPanel simulationPanel = new RunSimulationPanel(business, cardPanel);
        cardPanel.add(simulationPanel, "Simulation");
        
        frame.add(cardPanel);
        frame.setVisible(true);
        
        System.out.println("✓ Task 4 UI loaded!");
    }
    
    private static void createTask5GUI() {
        JFrame frame = new JFrame("Task 5: Maximize Profit Margins");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        
        Business business = ConfigureABusiness.initialize();
        JPanel cardPanel = new JPanel(new CardLayout());
        
        OptimizePanel optimizePanel = new OptimizePanel(business, cardPanel);
        cardPanel.add(optimizePanel, "Optimize");
        
        frame.add(cardPanel);
        frame.setVisible(true);
        
        System.out.println("✓ Task 5 UI loaded!");
    }
}
