package piixcolor.utilitaire;


public interface Observateur {
	
	public static final int SIG_COLORS_UPDATE = 0;
	public static final int SIG_FORMES_UPDATE = 1;
	public static final int SIG_RESERVE_UPDATE = 2;
	public static final int SIG_IMAGE_DELETE = 3;
	public static final int SIG_IMAGE_SAVE = 3;
	public static final int SIG_PARTIE_FINIE = 4;
	
	void actualise(int sig);
}
