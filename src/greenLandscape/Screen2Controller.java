

package greenLandscape;

import DBConnect.DBConnection;
import java.io.File;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * FXML Controller class
 *
 * @author Angie
 */

public class Screen2Controller implements Initializable , ControlledScreen {

    ScreensController myController;
    
    @FXML
    private GridPane insert1IDGridpane;
    
    @FXML
    private ComboBox comboBoxID1;
    
    @FXML
    private CheckBox checkBoxID1;
    
    @FXML WebView newCustomerView;
    
    @FXML
    private Button findLL;
    @FXML
    private TextField lat;
    @FXML
    private TextField lng;
    
    @FXML
    private TextField cID1;
    @FXML
    private TextField nameID1;
    @FXML
    private TextField lastID1;
    @FXML
    private TextField numID1;
    @FXML
    private TextField streetID1;
    @FXML
    private TextField cityID1;
    @FXML
    private TextField zipID1;
    @FXML
    private TextField phID1;
    
    @FXML
    private TextField hNumID1;
    @FXML
    private TextField hStreetID1;
    @FXML
    private TextField hCityID1;
    @FXML
    private TextField hZipID1;
    @FXML
    private TextField feeID1;
    @FXML
    private TextArea descID1; 
    
    @FXML
    private ToggleButton r1;
    @FXML
    private ToggleButton r2;
    @FXML
    private ToggleButton r3;
    @FXML
    private ToggleButton r4;
    
    private Connection conn;
    ResultSet[] rs;
    ResultSet   rsr;
    ObservableList<String> clients1;
    
    WebEngine webEngine;
    
    boolean oldCli = false;
    int route;
    int routeView;
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        this.comboBoxID1.disableProperty().bind(this.checkBoxID1.selectedProperty().not());
        ToggleGroup group = new ToggleGroup();
        r1.setToggleGroup(group);
        r2.setToggleGroup(group);
        r3.setToggleGroup(group);
        r4.setToggleGroup(group);

        
        webEngine = newCustomerView.getEngine();
        final URL urlGoogleMaps = getClass().getResource("markers.html");
        webEngine.load(urlGoogleMaps.toExternalForm());
        // TODO
    }
    
    
    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    @FXML
    private void goToScreen1(ActionEvent event){
        
        checkBoxID1.setSelected(false);        
        
        cID1.clear();
        nameID1.clear();
        lastID1.clear();
        numID1.clear();
        streetID1.clear();
        cityID1.clear();
        zipID1.clear();
        phID1.clear();
        
        hNumID1.clear();
        hStreetID1.clear();
        hCityID1.clear();
        hZipID1.clear();
        lat.clear();
        lng.clear();
        feeID1.clear();
        descID1.clear();
        

        myController.setScreen(ScreensFramework.screen1ID);
    }
    
    @FXML
    private void goToScreen3(ActionEvent event){
       myController.setScreen(ScreensFramework.screen3ID);
    }
    
    @FXML
    void insert(Event ev) {
        try {
            conn = DBConnection.connect();
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            String sql = "select * from client1";
            String sql = "select * from client";
            String[] query = sql.split(";");
            System.out.printf("query 1: %s\n", query[0]);

            rs = new ResultSet[query.length];
            rs[0] = stmt.executeQuery(query[0]);

            route = 1;
            clients1 = FXCollections.observableArrayList();

            while (rs[0].next()) {
                clients1.add(rs[0].getString("client_id") + ", " + rs[0].getString("lastname") + ", " + rs[0].getString("firstname"));
            }
            comboBoxID1.getItems().clear();
            comboBoxID1.setItems(clients1);

            rs[0].close();

        } catch (SQLException e) {
            System.err.print("Err on connection: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }
    
    @FXML
    public void setClients1(ActionEvent event){ //nameID1, lastID1, numID1, streetID1, zipID1, phID1;
        String value = (String) comboBoxID1.getValue();
        String[] cliID = value.split(",");
        //System.out.println(cliID[0]);
        oldCli = true;
        
        try {
            conn = DBConnection.connect();
            
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "select * from client where client_id = "+cliID[0];
            String[] query = sql.split(";");
            //System.out.printf("Insert 1 queries\n");
            //System.out.print("queries: " + query.length + "\n");
            System.out.printf("query 1: %s\n", query[0]);
            //System.out.printf("query 2: %s\n", query[1]);

            //int index = 0;
            
            rs = new ResultSet[query.length];
            rs[0] = stmt.executeQuery(query[0]);
            //rs[1] = conn.createStatement().executeQuery(query[1]);

            /*if (rs[index].last()) {
                count = rs[index].getRow();
                rs[index].beforeFirst();
            }*/
            //nameID1, lastID1, numID1, streetID1, zipID1, phID1;
            while(rs[0].next()){
                cID1.setText(rs[0].getString("client_id"));
                nameID1.setText(rs[0].getString("firstname"));
                lastID1.setText(rs[0].getString("lastname"));
                numID1.setText(rs[0].getString("h_number"));
                streetID1.setText(rs[0].getString("street_name"));
                cityID1.setText(rs[0].getString("city"));
                zipID1.setText(rs[0].getString("zip_code"));
                phID1.setText(rs[0].getString("phone"));
                
                //System.out.printf("%s, %s \n", rs[0].getString("lastname"), rs[0].getString("firstname"));
               //clients1.add(rs[0].getString("client_id")+", "+rs[0].getString("lastname")+", "+rs[0].getString("firstname"));
                //clients1.add(rs[0].getString("lastname"));
                //System.out.print(clients1);

            }
            //System.out.print(clients1);
            //comboBoxID1.getItems().clear();
            //comboBoxID1.setItems(clients1);

            rs[0].close();
            
        } catch (SQLException e) {
            System.err.print("Err on connection: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }   
    }
    
    @FXML
    public void getCoordinates(ActionEvent event) throws Exception{ 
        //String latLongs[];
        String address = hNumID1.getText()+" "+hStreetID1.getText()+" "+hCityID1.getText()+" CA "+hZipID1.getText();
        //String latitude = "";
        //String longitude = "";
        //System.out.println("Latitude: " + latLongs[0] + " and Longitude: " + latLongs[1]);
        int responseCode = 0;
        String api = "https://maps.googleapis.com/maps/api/geocode/xml?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=true&key=AIzaSyD0_iRKvxshSE6PqqTiBptnjy68KX94KxY";
        
        URL url = new URL(api);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.connect();
        responseCode = httpConnection.getResponseCode();
        
        if (responseCode == 200) {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();;
            Document document = builder.parse(httpConnection.getInputStream());
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("/GeocodeResponse/status");
            String status = (String) expr.evaluate(document, XPathConstants.STRING);

            if (status.equals("OK")) {
                expr = xpath.compile("//geometry/location/lat");
                String latitude = (String) expr.evaluate(document, XPathConstants.STRING);
                lat.setText(latitude);

                expr = xpath.compile("//geometry/location/lng");
                String longitude = (String) expr.evaluate(document, XPathConstants.STRING);
                lng.setText(longitude);

                //return new String[]{latitude, longitude};
            } else {
                throw new Exception("Error from the API - response status: " + status);
            }
        }

        String str = hNumID1.getText() + " " + hStreetID1.getText();
        String cit = hCityID1.getText() + " CA " + hZipID1.getText();
        webEngine.executeScript("updateMarker(" + lat.getText() + ", " + lng.getText() + "," + "\'" + str + "\'" + ", " + "\'" + cit + "\'" + ")");

    }
    
    @FXML
    private void showRoute1(ActionEvent event) {
        try {
            route = 1;

            conn = DBConnection.connect();
            Statement s = conn.createStatement();
            String sql = "select * from house where route_id="+route;
            rsr = s.executeQuery(sql);
            String strPath = "C:\\Users\\Mario\\Documents\\NetBeansProjects\\JFX-MultiScreen-master\\Green\\src\\greenLandscape\\myData.xml";

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();

            Element ele = doc.createElement("markers");
            doc.appendChild(ele);

            int iRows = 0;
            while ((rsr != null) && (rsr.next())) {
                ++iRows;
                Element marker = doc.createElement("marker");
                ele.appendChild(marker);

                Attr attRoute = doc.createAttribute("type");
                attRoute.setValue(rsr.getString("route_id"));
                marker.setAttributeNode(attRoute);

                Attr attLng = doc.createAttribute("lng");
                attLng.setValue(rsr.getString("longitude"));
                marker.setAttributeNode(attLng);

                Attr attLat = doc.createAttribute("lat");
                attLat.setValue(rsr.getString("latitude"));
                marker.setAttributeNode(attLat);

                Attr attAdd = doc.createAttribute("address");
                attAdd.setValue(rsr.getString("h_number") + " " + rsr.getString("street_name") + " " + rsr.getString("city") + ", CA " + rsr.getString("zip_code"));
                marker.setAttributeNode(attAdd);

                Attr attName = doc.createAttribute("name");
                attName.setValue("Erick Toscano");
                marker.setAttributeNode(attName);

                Attr attid = doc.createAttribute("id");
                attid.setValue(String.valueOf(iRows));
                marker.setAttributeNode(attid);

            }
            // creating and writing to xml file
            TransformerFactory tff = TransformerFactory
                    .newInstance();
            Transformer tf = tff.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(strPath));

            tf.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "2");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            tf.transform(domSource, streamResult);

            System.out.println("XML file created!");
            
            
            webEngine = newCustomerView.getEngine();
            final URL urlGoogleMaps = getClass().getResource("loadMarkers.html");
            
            
            com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(
            (webView, message, lineNumber, sourceId)-> System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message)
            );
            
            webEngine.load(urlGoogleMaps.toExternalForm());
            
        } catch (SQLException ex) {
            Logger.getLogger(Screen2Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(Screen2Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Screen2Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Screen2Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void showRoute2(ActionEvent event) {
        route = 2;
    }
    @FXML
    private void showRoute3(ActionEvent event) {
        route = 3;
    }
    @FXML
    private void showRoute4(ActionEvent event) {
        route = 4;
    }
    
    @FXML
    private void save1(ActionEvent event) {
        //System.out.println("here on save1 with route: "+route);
        //System.out.println("here on save1 with new customer: "+ oldCli);
        
        try {
            conn = DBConnection.connect();
            boolean inserted = false;
            
            if (oldCli) {
                String sql = "select * from newHouse(?,?,?,?,?,?,?,?,?,?);";

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(cID1.getText()));  //System.out.println("customerID: "+cID1.getText());
                pstmt.setInt(2, route);                             //System.out.println("route: "+route);
                pstmt.setString(3, hNumID1.getText());              //System.out.println("h_num: "+hNumID1.getText());
                pstmt.setString(4, hStreetID1.getText());           //System.out.println("h_street: "+hStreetID1.getText());
                pstmt.setString(5, hCityID1.getText());            //System.out.println("h_city: "+hCityID1.getText());
                pstmt.setString(6, hZipID1.getText());             //System.out.println(hZipID1.getText());
                //pstmt.setFloat(7, Float.parseFloat(lat.getText()));
                //pstmt.setFloat(8, Float.parseFloat(lng.getText()));
                pstmt.setBigDecimal(7, new BigDecimal(lat.getText()));
                pstmt.setBigDecimal(8, new BigDecimal(lng.getText()));
                pstmt.setLong(9, Long.parseLong(feeID1.getText()));  //System.out.println(feeID1.getText());
                pstmt.setString(10, descID1.getText());              //System.out.println(descID1.getText());
                
                inserted = pstmt.execute();
                oldCli = false;
            } else { 
                
                String sql = "select * from newCustomer(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, nameID1.getText());     
                pstmt.setString(2, lastID1.getText()); 
                pstmt.setString(3, numID1.getText());        
                pstmt.setString(4, streetID1.getText());         
                pstmt.setString(5, cityID1.getText());         
                pstmt.setString(6, zipID1.getText());         
                pstmt.setLong(7, Long.parseLong(phID1.getText()));  
                pstmt.setInt(8, route);
                pstmt.setString(9, hNumID1.getText());
                pstmt.setString(10, hStreetID1.getText());
                pstmt.setString(11, hCityID1.getText());
                pstmt.setString(12, hZipID1.getText());
                pstmt.setBigDecimal(7, new BigDecimal(lat.getText()));
                pstmt.setBigDecimal(8, new BigDecimal(lng.getText()));
                pstmt.setLong(15, Long.parseLong(feeID1.getText()));  
                pstmt.setString(16, descID1.getText());

                inserted = pstmt.execute();
            }
            
            if (inserted) {
                //label.setText("save succesfully!");
                inserted = false;
                cID1.clear();
                nameID1.clear();
                lastID1 .clear(); 
                numID1  .clear();
                streetID1.clear();
                cityID1.clear();
                zipID1.clear();
                phID1.clear();

                cID1.clear();
                hNumID1.clear();
                hStreetID1.clear();
                hCityID1.clear();
                hZipID1.clear();
                feeID1.clear();
                descID1.clear();
                lat.clear();
                lng.clear();
            }
            
        } catch (SQLException e) {
            System.err.print("Err on save: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } 

    }
    
    
    
}
