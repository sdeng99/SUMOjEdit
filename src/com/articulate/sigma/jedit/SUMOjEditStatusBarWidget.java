package com.articulate.sigma.jedit;

import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.gui.statusbar.*;
import javax.swing.JComponent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.ToolTipManager;

/**
 * A status bar widget that displays the SUMOjEdit build version
 */
public class SUMOjEditStatusBarWidget implements StatusWidgetFactory {
    
    @Override
    public Widget getWidget(View view) {
        return new BuildWidget(view);
    }
    
    private static class BuildWidget implements Widget {
        private final JLabel label;
        private final View view;
        
        public BuildWidget(View view) {
            this.view = view;
            this.label = new JLabel();
            
            // Get version from BuildInfo class
            String version = BuildInfo.VERSION;
            String buildNumber = BuildInfo.BUILD_NUMBER;
            String buildDate = BuildInfo.BUILD_DATE;
            String buildType = BuildInfo.BUILD_TYPE;
            
            // Format the display text
            String displayText = "SUMOjEdit " + version + ".b" + buildNumber;
            
            label.setText(displayText);
            
            // Set tooltip with full info
            String tooltip = "<html>SUMOjEdit Version " + version + "<br>" +
                           "Build: #" + buildNumber + " (" + buildType + ")<br>" +
                           "Date: " + buildDate + "</html>";
            label.setToolTipText(tooltip);
            
            // Style the label
            Font f = label.getFont();
            label.setFont(f.deriveFont(Font.PLAIN, f.getSize() - 1.0f));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            
            // Enable tooltips
            ToolTipManager.sharedInstance().registerComponent(label);
        }
        
        @Override
        public JComponent getComponent() {
            return label;
        }
        
        @Override
        public void update() {
            // Nothing to update dynamically
        }
        
        @Override
        public void propertiesChanged() {
            // No properties to monitor
        }
    }
}