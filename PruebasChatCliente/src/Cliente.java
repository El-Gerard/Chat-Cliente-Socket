import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Cliente {
	
	//String IpServer;
	// ----------------------Clase principal
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoCliente mimarco = new MarcoCliente();
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}// ----------------------------------**

}

//-----------------------------------Clase para dibujar la ventana
class MarcoCliente extends JFrame {

	public MarcoCliente() {

		setBounds(600, 300, 280, 350);
		LaminaMarcoCliente milamina = new LaminaMarcoCliente();
		add(milamina);
		setVisible(true);
		addWindowListener(new EnvioOnline());
	}
}// -------------------------------------------------------------**

// ----------------------------------Envio de señal onlain

// Clase para ejecutar una orden en cuanto se abre la ventana de ejecución
class EnvioOnline extends WindowAdapter {
	// Método que permitirá ejecutar una orden en cuando se abra la ventana
		
	public void windowOpened(WindowEvent e) {
	
		//Solicitud de la IP del servidor
		String IPE = JOptionPane.showInputDialog("iP servidor ");
		
		try {
			Socket miSocket = new Socket(IPE, 9999);
			Paquete datos = new Paquete();
			//Mensaje distintivo para indicarle al server que es un nuevo cliente
			datos.setMensaje(" online");
			ObjectOutputStream paquete_datos = new ObjectOutputStream(miSocket.getOutputStream());
			paquete_datos.writeObject(datos);
			miSocket.close();

		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}

	}
}//---------------------------------------------------------------------------**

//--------------------------------------------Creación de los elementos de la ventana
class LaminaMarcoCliente extends JPanel implements Runnable {
	
	//private String IpServer;
	private JTextField campo1;
	private JComboBox ip;
	private JLabel nick;
	private JTextArea campochat;
	private JButton miboton;
	EnvioOnline on;

	//-------------------------------------Elementos de la ventana
	
	public LaminaMarcoCliente() {

		//----------------
		//IpServer = JOptionPane.showInputDialog("iP servidor ");
		//on.recibir(JOptionPane.showInputDialog("iP servidor "));
		//----------------
		//Solicitud del nombre de usuario
		String nick_usuario = JOptionPane.showInputDialog("Nick: ");		
		JLabel n_nick = new JLabel("Nick: ");
		add(n_nick);
		nick = new JLabel();
		nick.setText(nick_usuario);
		add(nick);
		JLabel texto = new JLabel("Online: ");
		add(texto);
		ip = new JComboBox();
		add(ip);
		campochat = new JTextArea(12, 20);
		add(campochat);
		campo1 = new JTextField(20);
		add(campo1);
		miboton = new JButton("Enviar");
		EnviaTexto miEvento = new EnviaTexto();
		miboton.addActionListener(miEvento);
		add(miboton);
		Thread mihilo = new Thread(this);
		mihilo.start();
	}//-------------------------------------------------------**
	
	
	//----------------------------------------------------Acciones de los botones para envío de datos
	private class EnviaTexto implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {			
			// System.out.println(campo1.getText());

			campochat.append("\n" + campo1.getText());
			try {
				Socket miSocket = new Socket("localhost", 9999);

				Paquete datos = new Paquete();
				// SE envia el nombre al atributo nick capturado del TextArea
				datos.setNick(nick.getText());
				// Se envia el atributoo ip
				datos.setIp(ip.getSelectedItem().toString());
				// Se envia el atributo mensaje
				datos.setMensaje(campo1.getText());
				// Se crea un flujo de salida de datos (se enviaran objetos)
				ObjectOutputStream paquete_datos = new ObjectOutputStream(miSocket.getOutputStream());
				// Se envia el objeto
				paquete_datos.writeObject(datos);
				// Se cierra el soquet we
				miSocket.close();

				// Se debe serializar la clase: Convertir el objeto a bytes para ser enviado por
				// la red

				// DataOutputStream flujo_salida = new
				// DataOutputStream(miSocket.getOutputStream());

				// flujo_salida.writeUTF(campo1.getText());

				// flujo_salida.close();

			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
			}
		}
	}//---------------------------------------------------------------**

	//--------------------------------------------Hilo nativo para ejecutar las conexiones
	@Override
	public void run() {		
		
		try {

			ServerSocket servidor_cliente = new ServerSocket(9090);
			// Es posible usar el mismo socket pa no joder tanto
			Socket cliente;
			// Instancia de la clase de paquete para desglozar sus artibutos
			Paquete paqueteRecibido;
			// Permanece a la escucha indefinidamente
			while (true) {
				// Acepta las conexiones
				cliente = servidor_cliente.accept();
				// Se crea un flujo de entrada de datos
				ObjectInputStream flujoentrada = new ObjectInputStream(cliente.getInputStream());
				// Se iguala la instancia de clase al objeto que recibe de parte del servidor
				paqueteRecibido = (Paquete) flujoentrada.readObject();
				
				//System.out.println("Soy el cliente y tengo esto: "+paqueteRecibido.getMensaje());
				
				if (!paqueteRecibido.getMensaje().equals(" online")) {

					// Se imprime la información obtenida por atributos
					campochat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());

				} else {

					// campochat.append("\n" + paqueteRecibido.getIps());
					// Array para guardar las direcciones ip
					ArrayList<String> IpsMenu = new ArrayList<String>();
					// se guardan las direcciones acumuladas
					IpsMenu = paqueteRecibido.getIps();
					// Limpia el ComboBox de direcciones antes de agregar
					ip.removeAllItems();
					// Ciclo para agregar las direcciones de conectados al comboBox
					for (String z : IpsMenu) {

						ip.addItem(z);
					}
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}//-------------------------------------------------------**
}//--------------------------------------------------------------***
