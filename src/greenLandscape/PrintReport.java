/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenLandscape;

import DBConnect.DBConnection;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Mario
 */
public class PrintReport extends JFrame{
    
    Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public PrintReport() throws HeadlessException{
        //this.conn = DBConnection.connect();
    }
    
    
    public void showReport(int ID) throws SQLException{
        try {
            conn = DBConnection.connect();
            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\Mario\\Documents\\NetBeansProjects\\JFX-MultiScreen-master\\Green\\src\\greenLandscape\\route"+ID+"Report.jrxml");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conn);
            JRViewer viewer = new JRViewer(jasperPrint);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            
            this.add(viewer);
            this.setSize(1600, 1100);
            this.setVisible(true);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }
    
    
}
