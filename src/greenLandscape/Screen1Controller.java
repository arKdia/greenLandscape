
package greenLandscape;

import DBConnect.DBConnection;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author 
 */
public class Screen1Controller implements Initializable, ControlledScreen {
    
    @FXML
    private AnchorPane root;
    
    @FXML
    private ImageView logoImage;
    
    @FXML
    private Button closeButtonID;
    
    @FXML
    private GridPane housesIDGridpane;
    
    @FXML
    private Tab route1IDTab;
    
    int r1Label;
    int count;
    
    private Connection conn;
    ResultSet[] rs;
    
    Label[] rLabelList;
    

    ScreensController myController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      // if (!isSpalshLoaded) {
            loadSplashScreen();
            Image closeImage = new Image(getClass().getResourceAsStream("imgs/close.png"));
            closeButtonID.setGraphic(new ImageView(closeImage));
      //      isSpalshLoaded = true;
       //}
    }
    
    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    @FXML
    private void goToScreen2(ActionEvent event){
       myController.setScreen(ScreensFramework.screen2ID);
    }
    
    @FXML
    private void goToScreen3(ActionEvent event){
       myController.setScreen(ScreensFramework.screen3ID);
    }
    
    @FXML
    private void goToScreen4(ActionEvent event){
       myController.setScreen(ScreensFramework.screen4ID);
    }
    
    @FXML
    private void closeButton(){
        Platform.exit();
    }
    
    @FXML
    void event(Event ev) throws SQLException{
        if (route1IDTab.isSelected()) {
            //for (int i=0; i < r1Label; i++)
            //    rLabelList[i].setText("");
            loadClients1();
        }
    }
    
    private void loadClients1() throws SQLException{
        conn = DBConnection.connect();
        System.out.println("Connection on tab1");
        
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String sql = "select * from house where route_id = 1;select * from route11;"; // from route 4
        String[] query = sql.split(";");
        
        int index = 0;
        rs = new ResultSet[query.length];
        rs[0] = stmt.executeQuery(query[0]);
        rs[1] = conn.createStatement().executeQuery(query[1]);
        
        if (rs[index].last()){
                count = rs[index].getRow();
                rs[index].beforeFirst();
            }
    }
    
    private void loadSplashScreen() {
        FadeTransition fadeout = new FadeTransition(Duration.seconds(5), logoImage);
        fadeout.setFromValue(1);
        fadeout.setToValue(0);
        fadeout.setCycleCount(1);

        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                fadeout.play();

            }
        });
        new Thread(sleeper).start();

    }
    
}
