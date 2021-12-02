package lcm.lanpush.utils;

import java.util.HashMap;

public class CDI {
	
	private static HashMap<Class<?>, Object> objetosPorClasse = new HashMap<>();
	private static HashMap<String, Object> objetosPorNome = new HashMap<>();
	
	/**
	 * Grava um objeto como referencia de sua classe CONCRETA.
	 */
	public static void set(Object objeto) {
		if (objetosPorClasse.get(objeto.getClass()) != null)
			throw new IllegalStateException("Tentativa de registro de um tipo ja existente no CDI! Eh precisso especificar um nome.");
		objetosPorClasse.put(objeto.getClass(), objeto);
	}
	
	/**
	 * Grava um objeto como referencia de sua classe CONCRETA.
	 * Caso ja exista um, pode sobrescrever de acordo com o parametro.
	 */
	public static void set(Object objeto, boolean sobrescreve) {
		objetosPorClasse.put(objeto.getClass(), objeto);
	}
	
	/**
	 * Grava um objeto como referencia da classe especificada por parametro.
	 */
	public static void set(Object objeto, Class<?> tipo) {
		if (!tipo.isInstance(objeto))
			throw new IllegalArgumentException("Tentativa de registrar objeto por classe incompativel no CDI!");
		objetosPorClasse.put(tipo, objeto);
	}
	
	/**
	 * Grava um objeto como referencia da sua classe CONCRETA e com o nome especificado.
	 * Esta eh a unica maneira de gravar no CDI 2+ objetos com a mesma classe concreta,
	 * devendo ser diferenciados por nome.
	 */
	@SuppressWarnings("unchecked")
	public static void set(Object objeto, CharSequence nome) {
		objetosPorNome.put(nome.toString(), objeto);
	}

	public static Object get(CharSequence nome) {
		return objetosPorNome.get(nome.toString());
	}
	
	/**
	 * Recupera do CDI o objeto representante do tipo passado por parametro.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> tipo) {
		return (T) objetosPorClasse.get(tipo);
	}
	
	/**
	 * Recupera do CDI o objeto representante do tipo passado por parametro e com o nome especificado.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> tipo, CharSequence nome) {
		HashMap<CharSequence, Object> mapa = (HashMap<CharSequence, Object>) objetosPorClasse.get(tipo);
		if (mapa != null) {
			return (T) mapa.get(nome);
		}
		return null;
	}
}
