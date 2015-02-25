import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LimpiarHtml {
//	private final static Logger logger = LogManager.getLogger(CopyOfNoteProcessor.class.getName());

	public static void main(String[] args) throws IOException{
		String pathOrigen = "/home/pruebahadoop/Documentos/DescargasPeriodicos/Original/LaNacion/Notas/Economia/";
		String pathDestino = "/home/pruebahadoop/Documentos/DescargasPeriodicos/Procesado/LaNacion/PRUEBA/";

		limpiar(pathOrigen, pathDestino);
	}


	public static void limpiar(String pathOrigen, String pathDestino) throws IOException {

		// Obtener la carpeta donde se encuentran todos los archivos
		File carpeta = new File(pathOrigen);
		long init = new Date().getTime();
		assert(carpeta.isDirectory());
			int i = 1;
			// Recorrer cada archivo de la carpeta
			for (String archivo : carpeta.list()) {
				File file = new File(carpeta.getAbsolutePath() + "//" + archivo);
				if (file.isFile()) {

						if(i % 11 == 0){
							System.exit(0);
						}

					if (i % 250 == 0) {
						long now = new Date().getTime();
						System.out.println("Archivos procesados hasta el momento: '" + i + "' en " + (now - init)
								/ 1000 + " segs.");
					}
					i++;

						Document doc = Jsoup.parse(file, "utf-8");
						if (doc == null) {
							System.out.println();
							System.out.println("DOC NULL: " + file.getName());
							continue;
						}

//						long inicioParsearUnaNota = new Date().getTime();
						Note nota = getNotaFromDocument(doc);
						if(nota == null ){
							System.out.println("no está la nota del archivo: "+ archivo);
							continue;
						}
//						long tardoEnParsearUnaNota = new Date().getTime() - inicioParsearUnaNota;

//						long inicioGuardarUnaNota = new Date().getTime();
						guardarNota(nota, archivo, pathDestino);
//						long tardoEnGuardarUnaNota = new Date().getTime() - inicioGuardarUnaNota;
				}
			}
	}



	public static  Note getNotaFromDocument(Document doc) {
		if (doc.getElementById("encabezado") == null) {
			System.out.println("No tiene encabezado");
//			logger.error("Fail to process file {}");
			return null;
		}
		Element encabezado = doc.getElementById("encabezado");
		// Elements firma = encabezado.getElementsByAttributeValue("class",
		// "firma");
		encabezado.getElementsByClass("firma").remove();
		encabezado.getElementsByClass("bajada").remove();
		Elements volanta = encabezado.getElementsByAttributeValue("class", "volanta");
		Elements titulo = encabezado.getAllElements().select("h1");
		Elements descripcion = encabezado.getAllElements().select("p");
		descripcion.removeAll(volanta);
		Element cuerpo = doc.getElementById("cuerpo");
		Elements archRel = cuerpo.getElementsByAttributeValue("class", "archivos-relacionados");
		Elements fin = cuerpo.getElementsByAttributeValue("class", "fin");

		return new Note(volanta.text(), titulo.text(), descripcion.text(), cuerpo.text().replace(archRel.text(), "")
				.replace(fin.text(), ""), "", null);
	}

	public static void guardarNota(Note nota, String archivo, String pathAGuardar) {
		String nombreArchivo = archivo.replace(".html", "");
		if(nombreArchivo.contains("/")){
			nombreArchivo = nombreArchivo.replace("/", "-");
		}
		StoreFile sf = new StoreFile(pathAGuardar, ".txt", nota.toString(), nombreArchivo, "iso-8859-1");
		try {
			sf.store();
		} catch (IOException e) {
//			logger.error("Error al querer guardar en disco la nota {}", nota.getTitulo());
			e.printStackTrace();
		}
	}
}
