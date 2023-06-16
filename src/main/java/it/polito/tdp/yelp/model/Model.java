package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LatLngConfig;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private List<String>allCities;
	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge>grafo;
	private List<Business>allBusinesses;
	private Map<String, Business>businessNameMap;
	private Map<String, Business>idMap;
	private double distanzaMAX;
		
	private List<Business>percorsoMigliore;
	private int numMaxLocali;

	public Model() {
		this.dao = new YelpDao();
		this.allCities = new ArrayList<>(dao.getAllCities());
		this.allBusinesses = new ArrayList<>();
		this.businessNameMap = new HashMap<>();
		this.idMap = new HashMap<>();
		
	}
	
	public String creaGrafo(String city) {
		this.grafo = new SimpleWeightedGraph<Business, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//vertici
		this.allBusinesses = dao.getAllBusiness(city);
		Graphs.addAllVertices(grafo, this.allBusinesses);
		//id map e mappa nomi
		for(Business x : this.allBusinesses) {
			this.idMap.put(x.getBusinessId(), x);
			this.businessNameMap.put(x.getBusinessName(), x);
		}
		//archi pesati
		for(Business x : this.allBusinesses) {
			for(Business y : this.allBusinesses) {
				if(!x.equals(y)) {
					double peso = this.distanceFromLocation(x, y);
					Graphs.addEdge(grafo, x, y, peso);
				}
			}
		}
		return "Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi.";
	}	
	/**
	 * metodo per calcolare la distanza tra due Business(ogni Business ha tra gli attributi double latitude e double longitude)
	 * @param b1 è il primo nodo
	 * @param b2 è il secondo nodo
	 * @return la distanza tra i nodi
	 */
	public Double distanceFromLocation(Business b1, Business b2) {
		LatLng ll1 = new LatLng(b1.getLatitude(), b1.getLongitude());
	    LatLng ll2 = new LatLng(b2.getLatitude(), b2.getLongitude());

	    return (double) 1000*Math.round(LatLngTool.distance(ll1, ll2, LengthUnit.METER));
	  }
	
	public Business calcolaLocaleDistante(String b1ID) {
		Business b1 = this.businessNameMap.get(b1ID);
		this.distanzaMAX = 0.0;
		Business distante = null;
		ConnectivityInspector<Business, DefaultWeightedEdge> inspector = new ConnectivityInspector<>(this.grafo);
        Set<Business> connectedComponents = inspector.connectedSetOf(b1);
        for(Business b : connectedComponents) {
        	if(distanceFromLocation(b1, b)> distanzaMAX) {
        		distante = b;
        		this.distanzaMAX = distanceFromLocation(b1, b);
        	}
        }
        return distante;
	}

	public void  calcolaPercorso(double mediaMIN, Business b1, Business b2) {
		this.numMaxLocali = 0;
		this.percorsoMigliore = new ArrayList<>();
		
		List<Business> rimanenti = new ArrayList<>(this.grafo.vertexSet());
		List<Business>parziale = new ArrayList<Business>();
		
		ricorsione(parziale, rimanenti, mediaMIN, b1, b2);
	}
	
	
	
	/**
	 * La ricorsione vera e propria
	 * @param parziale
	 * @param rimanenti
	 */
	private void ricorsione(List<Business> parziale, List<Business> rimanenti, Double mediaMIN, Business b1, Business b2){
		/*
		 * L'idea della ricorsione è di prendere un giocatore, metterlo nella lista parziale,
		 * e rimuoverlo dalla lista di giocatori rimanenti.
		 * Dopodichè, ripeto la ricorsione, usando rimanenti,
		 * fino a che non li finisco.
		 */
		//calcolo costo
		int numeroLocali = parziale.size();
		if(numeroLocali == 0) {
			parziale.add(b1);
			rimanenti.remove(b1);
		}
		if (numeroLocali>numMaxLocali && parziale.get(0)==b1 && parziale.get(parziale.size()-1)==b2) {
			this.numMaxLocali = numeroLocali;
			this.percorsoMigliore = parziale;
		}
		
       	for (Business p : rimanenti) {
       		if(dao.calcolaMediaRecensioni(p) > mediaMIN)
 				parziale.add(p);
 				rimanenti.remove(p);
 				ricorsione(parziale, rimanenti, mediaMIN, b1, b2);
 				parziale.remove(parziale.size()-1);
 				rimanenti.add(p);
 		}
				
	}
	
	
	public double getDistanzaMAX() {
		return distanzaMAX;
	}

	public List<String> getAllCities() {
		return allCities;
	}

	public Graph<Business, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public List<Business> getAllBusinesses() {
		return allBusinesses;
	}

	public Map<String, Business> getBusinessNameMap() {
		return businessNameMap;
	}

	public Map<String, Business> getIdMap() {
		return idMap;
	}

	public List<Business> getPercorsoMigliore() {
		return percorsoMigliore;
	}

	public int getNumMaxLocali() {
		return numMaxLocali;
	}
	
	

	
	
	
	
}
