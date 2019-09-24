import java.io.Serializable;
import java.util.ArrayList;

/*Clase sencilla para utilizar sus getters y setters
 * para intercambiar datos en el objeto que se envía
 * Debe ser serializable para poder enviar los datos por la red*/

public class Paquete implements Serializable {

	private String nick, ip, mensaje;

	private ArrayList<String> Ips;

	public ArrayList<String> getIps() {
		return Ips;
	}

	public void setIps(ArrayList<String> ips) {
		Ips = ips;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}