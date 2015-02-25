import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Mixer {

	private String prefijoDiario_NombreNota;
	private String pathOrigen;
	private String pathDestino;
	boolean mezclarPorAño;

	/**
	 *
	 * @param prefijoDiario_NombreNota
	 *            : Es el comienzo del nombre del archivo, que debe ser el
	 *            nombre del diario. Por ejemplo, el prefijo puede ser
	 *            "LaNacion" si el nombre del archivo es "LaNacion_..."
	 * @param pathOrigen
	 *            : Ruta del directorio donde se van a buscar las notas
	 *            separadas.
	 * @param pathDestino
	 *            : Ruta del directorio donde se quieren guardar las notas
	 *            mezcladas.
	 * @param mezclarPorAño
	 *            : True si se quiere juntar en un archivo todas las notas
	 *            correspondientes a un año. Si es false, entonces se juntan las
	 *            correspondientes a un mismo mes.
	 */
	public Mixer(String prefijoDiario_NombreNota, String pathOrigen, String pathDestino, boolean mezclarPorAño) {
		super();
		this.prefijoDiario_NombreNota = prefijoDiario_NombreNota;
		this.pathOrigen = pathOrigen;
		this.pathDestino = pathDestino;
		this.mezclarPorAño = mezclarPorAño;
	}

	private Map<String, Set<File>> getNotasClasificadasPorMes() {

		Map<String, Set<File>> notasClasificadasPorMes = new HashMap<String, Set<File>>();

		File notas = new File(this.pathOrigen);

		if (notas.isDirectory()) {
			for (File F : notas.listFiles()) {
				String mesAño_O_Año = this.getMesAño_O_Año(F.getName());
				if (mesAño_O_Año.equalsIgnoreCase("2006") || mesAño_O_Año.equalsIgnoreCase("2008")
						|| mesAño_O_Año.equalsIgnoreCase("2009"))
					continue;
				if (!notasClasificadasPorMes.containsKey(mesAño_O_Año)) {
					Set<File> files = new HashSet<File>();
					notasClasificadasPorMes.put(mesAño_O_Año, files);
				}
				notasClasificadasPorMes.get(mesAño_O_Año).add(F);
			}
		}

		return notasClasificadasPorMes;

	}

	public void mixer() throws IOException {
		Map<String, Set<File>> notasClasificadas = getNotasClasificadasPorMes();

		for (Set<File> files : notasClasificadas.values()) {
			StringBuilder notasUnidas = new StringBuilder();
			String mesAño = "";
			for (File F : files) {
				ReaderFile rf = new ReaderFile(F);
				notasUnidas.append(rf.leer() + "\n\n\n");
				mesAño = getMesAño_O_Año(F.getName());
			}
			StoreFile sf = new StoreFile(this.pathDestino, ".txt", notasUnidas.toString(),
					this.prefijoDiario_NombreNota + "_" + mesAño, "");
			sf.store();
		}
	}

	// ejemplo nombre archivo: "LaNacion_01-02-2012_tituloNota"
	private String getMesAño_O_Año(String fileName) {
		// por defecto buscaría mesAño
		int cantCaracteres = 4;
		if (this.mezclarPorAño)
			cantCaracteres = 7;
		return fileName.substring(prefijoDiario_NombreNota.length() + cantCaracteres, fileName.lastIndexOf("_"));
	}
}
