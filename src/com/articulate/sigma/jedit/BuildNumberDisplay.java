package com.articulate.sigma.jedit;

import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.StatusBar;
import org.gjt.sp.util.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Build number display for jEdit status bar
 */
public class BuildNumberDisplay extends JPanel {
    
    private JLabel buildLabel;
    private static BuildNumberDisplay instance = null;
    
    private BuildNumberDisplay() {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        setOpaque(false);
        
        // Create separator
        JLabel separator = new JLabel(" | ");
        separator.setForeground(Color.GRAY);
        add(separator);
        
        // Create build label using BuildInfo
        String displayText = "v" + BuildInfo.VERSION + "-b" + BuildInfo.BUILD_NUMBER;
        buildLabel = new JLabel(displayText);
        
        // Style the label
        buildLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        buildLabel.setForeground(new Color(120, 120, 120));
        
        // Add tooltip
        StringBuilder tooltip = new StringBuilder("<html>");
        tooltip.append("<b>SUMOjEdit ").append(BuildInfo.VERSION).append("</b><br>");
        tooltip.append("Build: ").append(BuildInfo.BUILD_NUMBER).append("<br>");
        tooltip.append("Date: ").append(BuildInfo.BUILD_DATE).append("<br>");
        tooltip.append("Type: ").append(BuildInfo.BUILD_TYPE).append("<br>");
        tooltip.append("jEdit: ").append(jEdit.getVersion()).append("<br>");
        tooltip.append("<br><i>Double-click for details</i>");
        tooltip.append("</html>");
        buildLabel.setToolTipText(tooltip.toString());
        
        // Add mouse listener for interaction
        buildLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showBuildInfo();
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                buildLabel.setForeground(new Color(70, 130, 180));
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                buildLabel.setForeground(new Color(120, 120, 120));
                setCursor(Cursor.getDefaultCursor());
            }
        });
        
        add(buildLabel);
    }
    
    public static BuildNumberDisplay getInstance() {
        if (instance == null) {
            instance = new BuildNumberDisplay();
        }
        return instance;
    }
    
    public static void installInView(View view) {
        Log.log(Log.WARNING, BuildNumberDisplay.class, "installInView called for view: " + view);
        
        if (view == null || view.getStatus() == null) {
            Log.log(Log.WARNING, BuildNumberDisplay.class, "View or status bar is null!");
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                StatusBar statusBar = view.getStatus();
                
                // Check if already installed
                Component[] components = statusBar.getComponents();
                for (Component comp : components) {
                    if (comp instanceof BuildNumberDisplay) {
                        return; // Already installed
                    }
                }
                
                // Add the display panel to status bar
                BuildNumberDisplay display = getInstance();
                
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = GridBagConstraints.RELATIVE;
                gbc.weightx = 0;
                gbc.fill = GridBagConstraints.NONE;
                gbc.anchor = GridBagConstraints.EAST;
                
                statusBar.add(display, gbc);
                statusBar.revalidate();
                statusBar.repaint();
                
                Log.log(Log.WARNING, BuildNumberDisplay.class, 
                    "Build display installed: " + BuildInfo.FULL_VERSION);
                
            } catch (Exception e) {
                Log.log(Log.ERROR, BuildNumberDisplay.class, 
                    "Failed to install build display: " + e.getMessage());
            }
        });
    }
    
    private void showBuildInfo() {
        Runtime rt = Runtime.getRuntime();
        long maxMem = rt.maxMemory() / (1024 * 1024);
        long totalMem = rt.totalMemory() / (1024 * 1024);
        long freeMem = rt.freeMemory() / (1024 * 1024);
        long usedMem = totalMem - freeMem;
        
        StringBuilder info = new StringBuilder();
        info.append("SUMOjEdit Build Information\n");
        info.append("===========================\n\n");
        info.append("Version:      ").append(BuildInfo.VERSION).append("\n");
        info.append("Build Number: ").append(BuildInfo.BUILD_NUMBER).append("\n");
        info.append("Build Date:   ").append(BuildInfo.BUILD_DATE).append("\n");
        info.append("Build Type:   ").append(BuildInfo.BUILD_TYPE).append("\n");
        info.append("Full Version: ").append(BuildInfo.FULL_VERSION).append("\n\n");
        info.append("jEdit Version: ").append(jEdit.getVersion()).append("\n");
        info.append("jEdit Build:   ").append(jEdit.getBuild()).append("\n\n");
        info.append("System Information\n");
        info.append("------------------\n");
        info.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        info.append("Java Vendor:  ").append(System.getProperty("java.vendor")).append("\n");
        info.append("OS:           ").append(System.getProperty("os.name")).append(" ");
        info.append(System.getProperty("os.version")).append("\n");
        info.append("Architecture: ").append(System.getProperty("os.arch")).append("\n\n");
        info.append("Memory (MB):\n");
        info.append("  Max:        ").append(maxMem).append("\n");
        info.append("  Total:      ").append(totalMem).append("\n");
        info.append("  Used:       ").append(usedMem).append("\n");
        info.append("  Free:       ").append(freeMem).append("\n");
        info.append("Processors:   ").append(rt.availableProcessors()).append("\n");
        
        JTextArea textArea = new JTextArea(info.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(
            SwingUtilities.getWindowAncestor(this),
            scrollPane,
            "Build Information",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}