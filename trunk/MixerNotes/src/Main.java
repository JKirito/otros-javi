import java.io.IOException;
import java.util.Date;


public class Main {

	public static void main(String[] args) {

		System.out.println("empezó: "+ new Date());
		String pathNotas = "//home//pruebahadoop//Documentos//DescargasPeriodicos//Procesado//LaNacion//Economia//";
		String pathGuardar = "//home//pruebahadoop//Documentos//DescargasPeriodicos//Procesado//LaNacion//Prueba2//";

		Mixer m = new Mixer("LaNacion", pathNotas, pathGuardar, true);
		try {
			m.mixer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("OK!!! :D");
		System.out.println("Terminó: "+ new Date());

	}

}
