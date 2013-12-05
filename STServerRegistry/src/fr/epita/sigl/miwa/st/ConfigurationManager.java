package fr.epita.sigl.miwa.st;

/**
 * L'interface du gestionnaire de configuration
 * 
 * @author francois
 * 
 */
public interface ConfigurationManager {
	/**
	 * L'application utilise-t-elle la configuration local ?
	 * @return
	 */
	public boolean isLocal();

	/**
	 * Retourne le chemin vers le répertoire local où les fichiers asynchrones vont être stockés
	 * @return
	 */
	public String getLocalRepository();

	/**
	 * Retourne le nom de dossier correspondant à l'application en paramétre
	 * @param application 
	 * @return le nom du dossier de l'application
	 */
	public String getApplicationFolderName(EApplication application);

	/**
	 * L'addresse de l'hôte du serveur FTP
	 * @return
	 */
	public String getFTPHost();

	/**
	 * Le port du serveur FTP
	 * @return
	 */
	public int getFTPPort();

	/**
	 * Le nom d'utilisateur pour s'identifier auprès du serveur FTP
	 * @return
	 */
	public String getFTPUsername();

	/**
	 * Le mot de passe pour s'identifier auprès du serveur FTP
	 * @return
	 */
	public String getFTPPassword();
	
	/**
	 * Retourne l'application actuelle
	 * @return
	 */
	public EApplication getCurrentApplication();
}
