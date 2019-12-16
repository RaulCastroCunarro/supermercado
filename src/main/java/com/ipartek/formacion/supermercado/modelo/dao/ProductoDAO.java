package com.ipartek.formacion.supermercado.modelo.dao;

import java.util.ArrayList;
import java.util.List;

import com.ipartek.formacion.supermercado.modelo.pojo.Producto;

public class ProductoDAO implements IDAO<Producto>{
	
	private static ProductoDAO INSTANCE;
	private ArrayList<Producto> registros;
	private int indice = 1;
	
	private ProductoDAO() {
		super();
		registros = new ArrayList<Producto>();
		
		//TODO 10 productos un poco mas elaborados
		registros.add(new Producto(indice++, "Absolute Vodka", 13.55f,
				"https://media-verticommnetwork1.netdna-ssl.com/wines/absolut-vodka-434778.jpg",
				"Botella de 70cc de Vodka Absolute", 15));
		registros.add(new Producto(indice++, "Campurrianas Cuetara", 2.26f,
				"https://supermercado.eroski.es/images/12465795.jpg",
				"Galleta Campurriana CUÉTARA, caja 800 g", 5));
		registros.add(new Producto(indice++, "Bombones Frutos del mar", 2.59f,
				"http://offers.kd2.org/pics/49/f2/49f2dcb6a0c04038b97cd657d504ebbcd5e55890.jpg",
				"Bombones de chocolate belga con formas de frutos del mar", 50));
		registros.add(new Producto(indice++, "Turrón Suchard", 3.20f,
				"https://supermercado.eroski.es/images/17009135.jpg",
				"Turrón de chocolate crujiente SUCHARD, tableta 260 g", 40));
		registros.add(new Producto(indice++, "Papel 24 unidades Scottex", 6.69f,
				"https://supermercado.eroski.es/images/16953523.jpg",
				"Papel higiénico original SCOTTEX, paquete 24 rollos", 20));
		registros.add(new Producto(indice++, "Palanca Half-Life", 11.55f,
				"https://vignette.wikia.nocookie.net/es.gta/images/9/9b/W_ME_Crowbar.png",
				"La palanca original de morgan Freeman para que te la lelves a casa", 60));
		registros.add(new Producto(indice++, "Stormbringer", 1500.0f,
				"http://3.bp.blogspot.com/_o84PlPGdijw/SxROeBtw3mI/AAAAAAAAAAM/23Bz2VSucUs/s1600/stormbringer.jpg",
				"Auténtica reproducción de la espada de Elreic de Melibone", 0));
		registros.add(new Producto(indice++, "Colección El Ciclo de la Puerta de la Muerte", 40f,
				"https://cloud10.todocoleccion.online/libros-segunda-mano-ciencia-ficcion-fantasia/tc/2015/04/08/13/48752601.jpg",
				"El Ciclo de la Puerta de la Muerte, completa en 7 libros, edición Folio / Timun Mas en tapa blanda, con los títulos: --Ala de Dragón, La Estrella de los Elfos, El Mar de Fuego, El Mago de la Serpiente, La Mano del Caos, En el Laberinto, La Séptima Puerta.", 10));
		registros.add(new Producto(indice++, "Niño coreano", 5000f,
				"https://i.pinimg.com/736x/b8/f4/7f/b8f47ff96d15b4d8fd9bce05b297b0b9.jpg",
				"Un niño coreano listo para disfrutar de la manera que desee, esta tan agradecido de que le salven de vivir en Corea del Norte que hará lo que quiera", 30));
		registros.add(new Producto(indice++, "Canis Mayoris", 749.99f,
				"https://images3.memedroid.com/images/UPLOADED123/56654b3c901c5.jpeg",
				"VY Canis Majoris (VY CMa) es una estrella hipergigante roja, localizada en la constelación de Canis Major. Es una de las estrellas conocidas más grandes y luminosas. En su momento fue la mayor estrella conocida, aunque luego se descubrieron otras estrellas de mayor tamaño. En la actualidad la estrella más grande conocida es UY Scuti (aunque posiblemente Westerlund 1-26).", 75));
	}
	
	public static synchronized ProductoDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ProductoDAO();
		}
		
		return INSTANCE;
	}

	@Override
	public List<Producto> getAll() {
		return registros;
	}

	@Override
	public Producto getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Producto delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Producto update(int id, Producto pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Producto create(Producto pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
