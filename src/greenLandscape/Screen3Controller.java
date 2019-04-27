package greenLandscape;

import DBConnect.DBConnection;
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
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;

public class Screen3Controller implements Initializable, ControlledScreen {

    ScreensController myController;

    //Screen1Controller s1C = new Screen1Controller();
    @FXML
    WebView upWebViewID;

    WebEngine webEngine;

    @FXML
    AnchorPane main3;

    @FXML
    private TextField uphID;
    @FXML
    private TextField upContractID;
    @FXML
    private TextField uphNum;
    @FXML
    private TextField uphStreetID1;
    @FXML
    private TextField uphCityID1;
    @FXML
    private TextField uphZipID1;
    @FXML
    private TextField upLat;
    @FXML
    private TextField upLng;
    @FXML
    private TextField uphRouteID1;
    @FXML
    private TextField upfeeID1;
    @FXML
    private TextArea updescID1;

    @FXML
    private ToggleButton upR1;
    @FXML
    private ToggleButton upR2;
    @FXML
    private ToggleButton upR3;
    @FXML
    private ToggleButton upR4;

    private Connection conn;
    ResultSet[] rs;
    int route;
    boolean here = false;

    /**
     *
     */
    public static String upAddress;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

        ToggleGroup group = new ToggleGroup();
        upR1.setToggleGroup(group);
        upR2.setToggleGroup(group);
        upR3.setToggleGroup(group);
        upR4.setToggleGroup(group);

        webEngine = upWebViewID.getEngine();
        final URL urlGoogleMaps = getClass().getResource("markers.html");
        
        webEngine.load(urlGoogleMaps.toExternalForm());

    }

    @FXML
    public void setAddr(String addss) {
        this.upAddress = addss;
        System.out.println("to me: " + upAddress);
    }

    @FXML
    private void route1(ActionEvent event) {
        route = 1;
        uphRouteID1.setText("1");

    }

    @FXML
    private void route2(ActionEvent event) {
        route = 2;
        uphRouteID1.setText("2");

    }

    @FXML
    private void route3(ActionEvent event) {
        route = 3;
        uphRouteID1.setText("3");

    }

    @FXML
    private void route4(ActionEvent event) {
        route = 4;
        uphRouteID1.setText("4");
    }

    @FXML
    public void loadUpdate(ActionEvent event) {

        //upAddress = addr;
        System.out.println("to me: " + upAddress);
        //upAddress = "1811;Fremont St.;Bakersfield;93304";

        try {

            System.out.println("add: " + upAddress);

            conn = DBConnection.connect();
            String[] add = upAddress.split(";");
            System.out.printf("address: %s %s\n", add[0], add[1], add[2], add[3]);

            conn = DBConnection.connect();
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "select h.*, c.fee, c.description from house h, contract c where h.contract_id=c.contract_id and h_number='" + add[0] + "' and street_name='" + add[1] + "' and zip_code='" + add[3] + "';";
            String[] query = sql.split(";");

            System.out.printf("query 1: %s\n", query[0]);

            rs = new ResultSet[query.length];
            rs[0] = stmt.executeQuery(query[0]);

            while (rs[0].next()) {
                //System.out.println(rs[0].getString("h_number"));
                uphID.setText(rs[0].getString("house_id"));
                upContractID.setText(rs[0].getString("contract_id"));
                uphNum.setText(rs[0].getString("h_number"));
                uphStreetID1.setText(rs[0].getString("street_name"));
                uphCityID1.setText(rs[0].getString("city"));
                uphZipID1.setText(rs[0].getString("zip_code"));
                upLat.setText(rs[0].getString("latitude"));
                upLng.setText(rs[0].getString("longitude"));
                uphRouteID1.setText(rs[0].getString("route_id"));
                upfeeID1.setText(rs[0].getString("fee"));
                updescID1.setText(rs[0].getString("description"));
            }
            rs[0].close();

        } catch (SQLException e) {
            System.err.print("Err on loadUpdate: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

    }

    @FXML
    private void update1(ActionEvent event) {

        try {
            conn = DBConnection.connect();
            boolean updated = false;

            String sql = "select * from upHouse(?,?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(upContractID.getText()));
            pstmt.setInt(2, Integer.parseInt(uphID.getText()));
            pstmt.setString(3, uphNum.getText());
            pstmt.setString(4, uphStreetID1.getText());
            pstmt.setString(5, uphCityID1.getText());
            pstmt.setString(6, uphZipID1.getText());
            pstmt.setBigDecimal(7, new BigDecimal(upLat.getText()));
            pstmt.setBigDecimal(8, new BigDecimal(upLng.getText()));
            pstmt.setInt(9, Integer.parseInt(uphRouteID1.getText()));
            pstmt.setBigDecimal(10, BigDecimal.valueOf(Double.parseDouble(upfeeID1.getText())));
            pstmt.setString(11, updescID1.getText());

            updated = pstmt.execute();

            if (updated) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Info Updated!");
                alert.showAndWait();

                //label.setText("Update succesfully!");
                updated = false;
                upContractID.clear();
                uphID.clear();
                uphNum.clear();
                uphStreetID1.clear();
                uphCityID1.clear();
                uphZipID1.clear();
                upLat.clear();
                upLng.clear();
                uphRouteID1.clear();
                upfeeID1.clear();
                updescID1.clear();
                //updatePane1.setTranslateX(-5000);

            }

        } catch (SQLException e) {
            System.err.print("Err on Update: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void getCoordinates(ActionEvent event) throws Exception {
        //String latLongs[];
        String address = uphNum.getText() + " " + uphStreetID1.getText() + " " + uphCityID1.getText() + " CA " + uphZipID1.getText();
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
                upLat.setText(latitude);

                expr = xpath.compile("//geometry/location/lng");
                String longitude = (String) expr.evaluate(document, XPathConstants.STRING);
                upLng.setText(longitude);

            } else {
                throw new Exception("Error from the API - response status: " + status);
            }
        }

        String str = uphNum.getText() + " " + uphStreetID1.getText();
        String cit = uphCityID1.getText() + " CA " + uphZipID1.getText();

        webEngine.executeScript("updateMarker(" + upLat.getText() + ", " + upLng.getText() + "," + "\'" + str + "\'" + ", " + "\'" + cit + "\'" + ")");

    }

    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @FXML
    private void goToScreen1(ActionEvent event) {
        upContractID.clear();
        uphID.clear();
        uphNum.clear();
        uphStreetID1.clear();
        uphCityID1.clear();
        uphZipID1.clear();
        upLat.clear();
        upLng.clear();
        uphRouteID1.clear();
        upfeeID1.clear();
        updescID1.clear();
        myController.setScreen(ScreensFramework.screen1ID);
    }

    @FXML
    private void goToScreen2(ActionEvent event) {
        myController.setScreen(ScreensFramework.screen2ID);
    }
}
