import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

class ReaderFile {
	private File file;

	public ReaderFile(File file) {
		super();
		this.file = file;
	}

	public String leer() {
		StringBuilder txt = new StringBuilder();
		try {
			// Abrimos el archivo
			FileInputStream fstream = new FileInputStream(this.file);
			// Creamos el objeto de entrada
			DataInputStream entrada = new DataInputStream(fstream);
			// Creamos el Buffer de Lectura
			BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada, "iso-8859-1"));
			String strLinea = "";
			// Leer el archivo linea por linea
			while ((strLinea = buffer.readLine()) != null) {
				txt.append(strLinea + "\n");
			}
			// Cerramos el archivo
			entrada.close();
		} catch (Exception e) { // Catch de excepciones
			System.err.println("Ocurrio un error: " + e.getMessage());
		}
		return txt.toString();
	}
}