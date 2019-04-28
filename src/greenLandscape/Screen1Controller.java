
package greenLandscape;

import DBConnect.DBConnection;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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
    private GridPane houses2IDGridpane;
    @FXML
    private GridPane houses3IDGridpane;
    @FXML
    private GridPane houses4IDGridpane;
    
    @FXML
    private Tab route1IDTab;
    @FXML
    private Tab route2IDTab;
    @FXML
    private Tab route3IDTab;
    @FXML
    private Tab route4IDTab;
    
    @FXML
    private WebView mapIDWebView;
    @FXML
    private WebView mapIDWebView22;
    @FXML
    private WebView mapIDWebView2;
    @FXML
    private WebView mapIDWebView4;
    
    int routeID = 1;
    int routeView;
    int r1Label;
    int count;
    
    private Connection conn;
    ResultSet[] rs;
    
    Label[] labelList1;
    Button[] buttonlist1;
    
    Button b;
    
    private String delAddress;
    private String upAddress;
    
    private Button newButtonID;
    

    ScreensController myController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      // if (!isSpalshLoaded) {
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
            loadSplashScreen();
            
            //Image newHouse = new Image(getClass().getResourceAsStream("imgs/addHouse.png"));
            //newButtonID.setGraphic(new ImageView(newHouse));
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
        if (upAddress == null) {
                System.out.printf("No address selected \n");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("view house on map first!");
                alert.showAndWait();
        } else {
            myController.setScreen(ScreensFramework.screen3ID);
            //Screen3Controller s3C = new Screen3Controller();    
            //s3C.upAddress = upAddress;
            //s3C = null;
            //s3C.loadUpdate();
        }
       
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
    private void loadRep(ActionEvent event) throws SQLException{
       PrintReport viewReport = new PrintReport();
       //viewReport.showReport();
       viewReport.showReport(routeID);
       //viewReport.
    }
    
    
    @FXML
    void event(Event ev) throws SQLException{
        //housesIDGridpane.getChildren().clear();
        //houses2IDGridpane.getChildren().clear();
        
        if (route1IDTab.isSelected()) {
            //for (int i=0; i < r1Label; i++)
            //    rLabelList[i].setText("");
            housesIDGridpane.getChildren().clear();
            //routeView = 1;
            routeID = 1;
        }else if (route2IDTab.isSelected()) {
            houses2IDGridpane.getChildren().clear();
            routeID = 2;
        }else if (route3IDTab.isSelected()) {
            houses3IDGridpane.getChildren().clear();
            routeID = 3;
        }else if (route4IDTab.isSelected()) {
            houses4IDGridpane.getChildren().clear();
            routeID = 4;
        }
        
        loadClients(routeID);
    }
    
    EventHandler<ActionEvent> clientLocationHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            Screen3Controller sC3 = new Screen3Controller();

            String address;

            b = (Button) event.getSource();
            address = b.getId();
            delAddress = address.replaceAll("\\+", ";");
            upAddress = delAddress;
            
            //sC3.upAddress = delAddress;
            sC3.setAddr(delAddress);
            //sC3.loadUpdate(delAddress);
            
            //System.out.println(sC3.upAddress);
            
                    
            switch (routeID) {
                case 1:
                    //label.setText("Client Details for ID: "+b.getId());
                    //scrollPaneID1.setTranslateX(-5000);
                    //route1DetailIDGridPane.setTranslateX(-5000);
                    WebEngine webEngine = mapIDWebView.getEngine();
                    webEngine.load("http://maps.google.co.in/maps?q=" + b.getId());
                    break;

                case 2: //route 2
                    //tab2label.setText("Client Details for ID: "+b.getId());
                    //scrollPaneID2.setTranslateX(-5000);
                    //route22DetailIDGridPane.setTranslateX(-5000);
                    WebEngine webEngine22 = mapIDWebView22.getEngine();
                    webEngine22.load("http://maps.google.co.in/maps?q=" + b.getId());
                    break;

                case 3: //route 3
                    //tab2label.setText("Client Details for ID: "+b.getId());
                    //scrollPaneID3.setTranslateX(-5000);
                    //route2DetailIDGridPane.setTranslateX(-5000);
                    WebEngine webEngine2 = mapIDWebView2.getEngine();
                    webEngine2.load("http://maps.google.co.in/maps?q=" + b.getId());
                    break;

            case 4:
                //tablabel.setText("Client Details for ID: "+b.getId());
                //route4DetailIDGridPane.setTranslateX(-5000);
                WebEngine webEngine4 = mapIDWebView4.getEngine();
                webEngine4.load("http://maps.google.co.in/maps?q=" + b.getId());
                break;

            /*case 5:
                //tablabel.setText("Client Details for ID: "+b.getId());
                route5DetailIDGridPane.setTranslateX(-5000);
                WebEngine webEngine5 = mapIDWebView5.getEngine();
                webEngine5.load("http://maps.google.co.in/maps?q=" + b.getId());
                break;

            case 6:
                //tab6label.setText("Client Details for ID: "+b.getId());
                route4DetailIDGridPane.setTranslateX(-5000);
                WebEngine webEngine6 = mapIDWebView6.getEngine();
                webEngine6.load("http://maps.google.co.in/maps?q=" + b.getId());
                break;*/
            }
        }
    };
    
    private void loadClients(int ID) throws SQLException{
        conn = DBConnection.connect();
        System.out.println("Connection on "+ID);
        
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
        
        //housesIDGridpane.getChildren().clear();
        //housesIDGridpane2.getChildren().clear();

        
        while(rs[index].next()){ /*Number, street, city, zipcode*/
                labelList1[i] = new Label();
                labelList1[i].setText("Number:");
                labelList1[i].setStyle("-fx-font-weight: bold");
                if (route1IDTab.isSelected()) {
                    housesIDGridpane.add(labelList1[i], h, v);
                }else if (route2IDTab.isSelected()) { 
                    houses2IDGridpane.add(labelList1[i], h, v);
                }else if (route3IDTab.isSelected()) { 
                    houses3IDGridpane.add(labelList1[i], h, v);
                }else if (route4IDTab.isSelected()) { 
                    houses4IDGridpane.add(labelList1[i], h, v);
                }
                i++;

                labelList1[i] = new Label();
                labelList1[i].setText(rs[index].getString("h_number"));
                if (route1IDTab.isSelected()) {
                    housesIDGridpane.add(labelList1[i], h+1,v);
                }else if (route2IDTab.isSelected()) {
                    houses2IDGridpane.add(labelList1[i], h+1,v);
                }else if (route3IDTab.isSelected()) {
                    houses3IDGridpane.add(labelList1[i], h+1,v);
                }else if (route4IDTab.isSelected()) {
                    houses4IDGridpane.add(labelList1[i], h+1,v);
                }
                i++;v++;
                
                labelList1[i] = new Label();
                labelList1[i].setText("Street:");
                labelList1[i].setStyle("-fx-font-weight: bold");
                if (route1IDTab.isSelected()) {
                    housesIDGridpane.add(labelList1[i], h, v);
                }else if (route2IDTab.isSelected()) {
                    houses2IDGridpane.add(labelList1[i], h, v);
                }else if (route3IDTab.isSelected()) {
                    houses3IDGridpane.add(labelList1[i], h, v);
                }else if (route4IDTab.isSelected()) {
                    houses4IDGridpane.add(labelList1[i], h, v);
                }
                i++;
                
                labelList1[i] = new Label();
                labelList1[i].setText(rs[index].getString("street_name"));
                if (route1IDTab.isSelected()) {
                    housesIDGridpane.add(labelList1[i], h+1,v);
                }else if (route2IDTab.isSelected()) {
                    houses2IDGridpane.add(labelList1[i], h+1,v);
                }else if (route3IDTab.isSelected()) {
                    houses3IDGridpane.add(labelList1[i], h+1,v);
                }else if (route4IDTab.isSelected()) {
                    houses4IDGridpane.add(labelList1[i], h+1,v);
                }
                i++;v++;
                
                labelList1[i] = new Label();
                labelList1[i].setText("City:");
                labelList1[i].setStyle("-fx-font-weight: bold");
                if (route1IDTab.isSelected()) {
                    housesIDGridpane.add(labelList1[i], h, v);
                }else if (route2IDTab.isSelected()) {
                    houses2IDGridpane.add(labelList1[i], h, v);
                }else if (route3IDTab.isSelected()) {
                    houses3IDGridpane.add(labelList1[i], h, v);
                }else if (route4IDTab.isSelected()) {
                    houses4IDGridpane.add(labelList1[i], h, v);
                }
                i++;
                
                labelList1[i] = new Label();
                labelList1[i].setText(rs[index].getString("city"));
                if (route1IDTab.isSelected()) {
                    housesIDGridpane.add(labelList1[i], h+1,v);
                }else if (route2IDTab.isSelected()) {
                    houses2IDGridpane.add(labelList1[i], h+1,v);
                }else if (route3IDTab.isSelected()) {
                    houses3IDGridpane.add(labelList1[i], h+1,v);
                }else if (route4IDTab.isSelected()) {
                    houses4IDGridpane.add(labelList1[i], h+1,v);
                }
                i++;v++;
                
                labelList1[i] = new Label();
                labelList1[i].setText("Zip Code:");
                labelList1[i].setStyle("-fx-font-weight: bold");
                if (route1IDTab.isSelected()) {
                    housesIDGridpane.add(labelList1[i], h, v);
                }else if (route2IDTab.isSelected()) {
                    houses2IDGridpane.add(labelList1[i], h, v);
                }else if (route3IDTab.isSelected()) {
                    houses3IDGridpane.add(labelList1[i], h, v);
                }else if (route4IDTab.isSelected()) {
                    houses4IDGridpane.add(labelList1[i], h, v);
                }
                i++;
                
                labelList1[i] = new Label();
                labelList1[i].setText(rs[index].getString("zip_code"));
                if (route1IDTab.isSelected()) {
                    housesIDGridpane.add(labelList1[i], h+1,v);
                }else if (route2IDTab.isSelected()) {
                    houses2IDGridpane.add(labelList1[i], h+1,v);
                }else if (route3IDTab.isSelected()) {
                    houses3IDGridpane.add(labelList1[i], h+1,v);
                }else if (route4IDTab.isSelected()) {
                    houses4IDGridpane.add(labelList1[i], h+1,v);
                }
                v++;
                
                buttonlist1[j] = new Button();
                buttonlist1[j].setText("< map");
                buttonlist1[j].setStyle("-fx-font-weight: bold");
                buttonlist1[j].setId(rs[index].getString("h_number")+"+"+rs[index].getString("street_name")+"+"+rs[index].getString("city")+"+"+rs[index].getString("zip_code"));
                buttonlist1[j].setOnAction(clientLocationHandler);
                if (route1IDTab.isSelected()) {
                    housesIDGridpane.add(buttonlist1[j], h, v);
                }else if (route2IDTab.isSelected()) {
                    houses2IDGridpane.add(buttonlist1[j], h, v);
                }else if (route3IDTab.isSelected()) {
                    houses3IDGridpane.add(buttonlist1[j], h, v);
                }else if (route4IDTab.isSelected()) {
                    houses4IDGridpane.add(buttonlist1[j], h, v);
                }
                j++;
                
                buttonlist1[j] = new Button();
                buttonlist1[j].setText("more details >");
                buttonlist1[j].setStyle("-fx-font-weight: bold");
                buttonlist1[j].setId(rs[index].getString("house_id"));
                //buttonlist1[j].setOnAction(clientDetailsHandler);
                if (route1IDTab.isSelected()) {
                    housesIDGridpane.add(buttonlist1[j], h+1, v);
                }else if (route2IDTab.isSelected()) {
                    houses2IDGridpane.add(buttonlist1[j], h+1, v);
                }else if (route3IDTab.isSelected()) {
                    houses3IDGridpane.add(buttonlist1[j], h+1, v);
                }else if (route4IDTab.isSelected()) {
                    houses4IDGridpane.add(buttonlist1[j], h+1, v);
                }
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
    
    @FXML
    private void delete(ActionEvent event){
        try {
            
            boolean inserted = false;
            if (delAddress == null) {
                System.out.printf("address is empty \n");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("view house on map first!");
                alert.showAndWait();
                inserted = false;

            } else {
                
                String[] add = delAddress.split(";");
                System.out.printf("address: %s %s\n", add[0], add[1], add[2], add[3]);
                String num = add[0];
                String street = add[1];
                String zip = add[3];
                
                conn = DBConnection.connect();
                String sql = "select * from delHouse(?,?,?);";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, num); 
                pstmt.setString(2, street);
                pstmt.setString(3, zip); 

                inserted = pstmt.execute();
                
                if (inserted){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("House Deleted!");
                    alert.showAndWait();
                }

          }

        } catch (SQLException e) {
            System.err.print("Err on connection: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } 
        
    }
        
    
}
