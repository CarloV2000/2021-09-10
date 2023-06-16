/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnDistante"
    private Button btnDistante; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcolaPercorso"
    private Button btnCalcolaPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB1"
    private ComboBox<String> cmbB1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB2"
    private ComboBox<String> cmbB2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String citta = this.cmbCitta.getValue();
    	if(citta == null) {
    		this.txtResult.setText("Inserire una citta nella boxCitta!");
    		return;
    	}
    	String r = model.creaGrafo(citta);
    	this.txtResult.setText(r);
    	for(Business b : model.getAllBusinesses()) {
    		this.cmbB1.getItems().add(b.getBusinessName());
    	
    	}
    }

    @FXML
    void doCalcolaLocaleDistante(ActionEvent event) {
    	String b1NAME= this.cmbB1.getValue();
    	if(b1NAME == null) {
    		this.txtResult.setText("Inserire un Business nella boxB1!");
    		return;
    	}
    	Business result = model.calcolaLocaleDistante(b1NAME);
    	for(Business b : model.getAllBusinesses()) {
    		this.cmbB2.getItems().add(b.getBusinessName());
    		this.cmbB2.getItems().remove(b1NAME);
    	}
    	this.txtResult.setText("Il locale piu distante da "+b1NAME+" è "+result.getBusinessName()+" distanza = "+model.getDistanzaMAX());
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {//c'è un problema: devo fare getter della mappa nomiBusiness per inserire
    	//i valori di b1 e b2 nel metodo ricorsivo
    	String xs = this.txtX2.getText();
    	Double x;
    	String b1NAME = this.cmbB1.getValue();
    	String b2NAME = this.cmbB2.getValue();
    	if(b1NAME == null) {
    		this.txtResult.setText("Inserire un Business nella boxB1!");
    		return;
    	}
    	if(b2NAME == null) {
    		this.txtResult.setText("Inserire un Business nella boxB2!");
    		return;
    	}
    	try {
    		x = Double.parseDouble(xs);
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero nel campo x!");
    		return;
    	}
    	Map<String, Business>mappaNomiBusiness = new HashMap<>(model.getBusinessNameMap());
    	Business b1 = mappaNomiBusiness.get(b1NAME);
    	Business b2 = mappaNomiBusiness.get(b2NAME);
    	model.calcolaPercorso(x, b1, b2);
    	List<Business>percorso = new ArrayList<>(model.getPercorsoMigliore());
    	Integer n = model.getNumMaxLocali();
    	String result = "Il percorso migliore tra "+b1.getBusinessName()+" e "+b2.getBusinessName()+" contiene "+n+" locali: \n";
    	for(Business y : percorso) {
    		result += y.getBusinessName();
    	}
    	this.txtResult.setText(result);
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDistante != null : "fx:id=\"btnDistante\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB1 != null : "fx:id=\"cmbB1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB2 != null : "fx:id=\"cmbB2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	for(String c : model.getAllCities()) {
    		this.cmbCitta.getItems().add(c);
    	}
    }
}
