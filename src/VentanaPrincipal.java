import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class VentanaPrincipal {

	// La ventana principal, en este caso, guarda todos los componentes:
	JFrame ventana;
	JPanel panelImagen;
	JPanel panelEmpezar;
	JPanel panelPuntuacion;
	JPanel panelJuego;

	// Todos los botones se meten en un panel independiente.
	// Hacemos esto para que podamos cambiar después los componentes por otros
	JPanel[][] panelesJuego;
	JButton[][] botonesJuego;

	// Correspondencia de colores para las minas:
	Color correspondenciaColores[] = { Color.BLACK, Color.CYAN, Color.GREEN, Color.ORANGE, Color.RED, Color.RED,
			Color.RED, Color.RED, Color.RED, Color.RED };

	JButton botonEmpezar;
	JTextField pantallaPuntuacion;

	// LA VENTANA GUARDA UN CONTROL DE JUEGO:
	ControlJuego juego;

	// Constructor, marca el tamaño y el cierre del frame
	public VentanaPrincipal() {
		ventana = new JFrame();
		ventana.setBounds(100, 100, 700, 500);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		juego = new ControlJuego();
	}

	// Inicializa todos los componentes del frame
	public void inicializarComponentes() {

		// Definimos el layout:
		ventana.setLayout(new GridBagLayout());

		// Inicializamos componentes
		panelImagen = new JPanel();
		panelEmpezar = new JPanel();
		panelEmpezar.setLayout(new GridLayout(1, 1));
		panelPuntuacion = new JPanel();
		panelPuntuacion.setLayout(new GridLayout(1, 1));
		panelJuego = new JPanel();
		panelJuego.setLayout(new GridLayout(10, 10));

		botonEmpezar = new JButton("Go!");
		pantallaPuntuacion = new JTextField("0");
		pantallaPuntuacion.setEditable(false);
		pantallaPuntuacion.setHorizontalAlignment(SwingConstants.CENTER);

		// Bordes y colores:
		panelImagen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		panelEmpezar.setBorder(BorderFactory.createTitledBorder("Empezar"));
		panelPuntuacion.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		panelJuego.setBorder(BorderFactory.createTitledBorder("Juego"));

		// Colocamos los componentes:
		// AZUL
		GridBagConstraints settings = new GridBagConstraints();
		settings.gridx = 0;
		settings.gridy = 0;
		settings.weightx = 1;
		settings.weighty = 1;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelImagen, settings);
		// VERDE
		settings = new GridBagConstraints();
		settings.gridx = 1;
		settings.gridy = 0;
		settings.weightx = 1;
		settings.weighty = 1;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelEmpezar, settings);
		// AMARILLO
		settings = new GridBagConstraints();
		settings.gridx = 2;
		settings.gridy = 0;
		settings.weightx = 1;
		settings.weighty = 1;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelPuntuacion, settings);
		// ROJO
		settings = new GridBagConstraints();
		settings.gridx = 0;
		settings.gridy = 1;
		settings.weightx = 1;
		settings.weighty = 10;
		settings.gridwidth = 3;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelJuego, settings);

		// Paneles
		panelesJuego = new JPanel[10][10];
		for (int i = 0; i < panelesJuego.length; i++) {
			for (int j = 0; j < panelesJuego[i].length; j++) {
				panelesJuego[i][j] = new JPanel();
				panelesJuego[i][j].setLayout(new GridLayout(1, 1));
				panelJuego.add(panelesJuego[i][j]);
			}
		}
		
		panelImagen.setLayout(new GridLayout(1,1));
		JLabel imagen=new JLabel();
		imagen.setBounds(50,50,50,50);
		imagen.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			BufferedImage img=ImageIO.read(new File("buscaminas.png"));
			ImageIcon icon=new ImageIcon(img.getScaledInstance(imagen.getWidth(),imagen.getHeight(), Image.SCALE_SMOOTH));
			imagen.setIcon(icon);
			panelImagen.add(imagen);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Botones
		botonesJuego = new JButton[10][10];
		for (int i = 0; i < botonesJuego.length; i++) {
			for (int j = 0; j < botonesJuego[i].length; j++) {
				botonesJuego[i][j] = new JButton("-");
				panelesJuego[i][j].add(botonesJuego[i][j]);
			}
		}

		// BotónEmpezar:
		panelEmpezar.add(botonEmpezar);
		panelPuntuacion.add(pantallaPuntuacion);

	}

	/**
	 * Metodo que inicializa todos los listeners que necesita inicialmente el
	 * programa
	 */
	public void inicializarListeners() {
		for (int i = 0; i < botonesJuego.length; i++) {
			for (int j = 0; j < botonesJuego.length; j++) {
				botonesJuego[i][j].addActionListener(new ActionBoton(this,i,j));
			}
		}
		botonEmpezar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ventana.setVisible(false);
				ventana=new JFrame();
				ventana.setBounds(100, 100, 700, 500);
				ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				juego = new ControlJuego();
				inicializar();
			}
		});
	}

	/**
	 * Metodo que pinta en la pantalla el numero de minas que hay alrededor de la
	 * celda Saca el boton que haya en la celda determinada y a�ade un JLabel
	 * centrado y no editable con el numero de minas alrededor. Se pinta el color
	 * del texto segun la siguiente correspondecia (consultar la variable
	 * correspondeciaColor): - 0 : negro - 1 : cyan - 2 : verde - 3 : naranja - 4 o
	 * mas : rojo
	 * 
	 * @param i:
	 *            posicion vertical de la celda.
	 * @param j:
	 *            posicion horizontal de la celda.
	 */
	public void mostrarNumMinasAlrededor(int i, int j) {
		String minas;
		panelesJuego[i][j].removeAll();
		int minasEncontradas = juego.getMinasAlrededor(i, j);
		JLabel aux = new JLabel();
		minas = Integer.toString(minasEncontradas);
		aux.setText(minas);

		switch (minasEncontradas) {
		case 0: {
			aux.setForeground(correspondenciaColores[0]);
			break;
		}
		case 1: {
			aux.setForeground(correspondenciaColores[1]);
			break;
		}
		case 2: {
			aux.setForeground(correspondenciaColores[2]);
			break;
		}
		case 3: {
			aux.setForeground(correspondenciaColores[3]);
			break;
		}
		case 4: {
			aux.setForeground(correspondenciaColores[4]);
			break;
		}
		case 5: {
			aux.setForeground(correspondenciaColores[4]);
			break;
		}
		case 6: {
			aux.setForeground(correspondenciaColores[4]);
			break;
		}
		case 7: {
			aux.setForeground(correspondenciaColores[4]);
			break;
		}
		case 8: {
			aux.setForeground(correspondenciaColores[4]);
			break;
		}
		}

		panelesJuego[i][j].add(aux);
	}

	/**
	 * Metodo que muestra una ventana que muestra el fin del juego
	 * 
	 * @param porExplosion
	 *            : Un booleano que indica si es final del juego porque ha explotado
	 *            una mina (true) o bien porque hemos desactivado todas (false)
	 * @post : Todos los botones se desactivan excepto el de volver a iniciar el
	 *       juego.
	 */
	public void mostrarFinJuego(boolean porExplosion) {
		if (porExplosion == true) {
			JOptionPane.showMessageDialog(ventana, "GAME OVER");
		} else {
			if (porExplosion == false) {
				JOptionPane.showMessageDialog(ventana, "HAS GANADO :D");
			}
		}
	}

	/**
	 * Metodo que muestra la puntuacion por pantalla.
	 */
	public void actualizarPuntuacion() {
		String puntuacion;
		puntuacion = Integer.toString(juego.getPuntuacion());
		pantallaPuntuacion.setText(puntuacion);
	}

	/**
	 * Metodo para refrescar la pantalla
	 */
	public void refrescarPantalla() {
		ventana.revalidate();
		ventana.repaint();
	}

	/**
	 * Metodo que devuelve el control del juego de una ventana
	 * 
	 * @return un ControlJuego con el control del juego de la ventana
	 */
	public ControlJuego getJuego() {
		return juego;
	}

	/**
	 * Metodo para inicializar el programa
	 */
	public void inicializar() {
		// IMPORTANTE, PRIMERO HACEMOS LA VENTANA VISIBLE Y LUEGO INICIALIZAMOS LOS
		// COMPONENTES.
		ventana.setVisible(true);
		inicializarComponentes();
		inicializarListeners();
	}

}
