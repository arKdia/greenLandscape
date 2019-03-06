
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
    
    Label[] labelList1;
    Button[] buttonlist1;
    
    

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
        int routeID = 1;
        if (route1IDTab.isSelected()) {
            //for (int i=0; i < r1Label; i++)
            //    rLabelList[i].setText("");
            routeID = 1;
            
        }
        loadClients(routeID);
    }
    
    private void loadClients(int ID) throws SQLException{
        conn = DBConnection.connect();
        System.out.println("Connection on tab1");
        
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String sql = "select * from house where route_id = "+ID+";select * from route11;";
        String[] query = sql.split(";");
        
        int index = 0;
        rs = new ResultSet[query.length];
        rs[0] = stmt.executeQuery(query[0]);
        rs[1] = conn.createStatement().executeQuery(query[1]);
        
        if (rs[index].last()){
                count = rs[index].getRow();
                rs[index].beforeFirst();
            }
        
        labelList1 = new Label[count*8];
        buttonlist1 = new Button[count*2];
        
        int h = 0; // ------
        int v = 0; // |
        int i = 0;
        int j = 0;
        
        housesIDGridpane.getChildren().clear();
        
        while(rs[index].next()){ /*Number, street, city, zipcode*/
                labelList1[i] = new Label();
                labelList1[i].setText("Number:");
                labelList1[i].setStyle("-fx-font-weight: bold");
                housesIDGridpane.add(labelList1[i], h, v);

                i++;
                labelList1[i] = new Label();
                labelList1[i].setText(rs[index].getString("h_number"));
                housesIDGridpane.add(labelList1[i], h+1,v);
                //System.out.println(h + " "+v);
                i++;v++;
                labelList1[i] = new Label();
                labelList1[i].setText("Street:");
                labelList1[i].setStyle("-fx-font-weight: bold");
                housesIDGridpane.add(labelList1[i], h, v);
                i++;
                labelList1[i] = new Label();
                labelList1[i].setText(rs[index].getString("street_name"));
                housesIDGridpane.add(labelList1[i], h+1,v);
                //System.out.println(h + " "+v);
                i++;v++;
                labelList1[i] = new Label();
                labelList1[i].setText("City:");
                labelList1[i].setStyle("-fx-font-weight: bold");
                housesIDGridpane.add(labelList1[i], h, v);
                i++;
                labelList1[i] = new Label();
                labelList1[i].setText(rs[index].getString("city"));
                housesIDGridpane.add(labelList1[i], h+1,v);
                //System.out.println(h + " "+v);
                i++;v++;
                labelList1[i] = new Label();
                labelList1[i].setText("Zip Code:");
                labelList1[i].setStyle("-fx-font-weight: bold");
                housesIDGridpane.add(labelList1[i], h, v);
                i++;
                labelList1[i] = new Label();
                labelList1[i].setText(rs[index].getString("zip_code"));
                housesIDGridpane.add(labelList1[i], h+1,v);
                //System.out.println(h + " "+v);
                v++;
                buttonlist1[j] = new Button();
                buttonlist1[j].setText("< map");
                buttonlist1[j].setStyle("-fx-font-weight: bold");
                buttonlist1[j].setId(rs[index].getString("h_number")+"+"+rs[index].getString("street_name")+"+"+rs[index].getString("city")+"+"+rs[index].getString("zip_code"));
                //buttonlist1[j].setOnAction(clientLocationHandler);
                housesIDGridpane.add(buttonlist1[j], h, v);
                j++;
                buttonlist1[j] = new Button();
                buttonlist1[j].setText("more details >");
                buttonlist1[j].setStyle("-fx-font-weight: bold");
                buttonlist1[j].setId(rs[index].getString("house_id"));
                //buttonlist1[j].setOnAction(clientDetailsHandler);
                housesIDGridpane.add(buttonlist1[j], h+1, v);
                //System.out.println(h + " "+v);
                v++;j++;
                
            }
            //loaded1 = i;

            rs[index].close();
        
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
