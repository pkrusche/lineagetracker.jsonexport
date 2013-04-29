

import ij.ImageJ;

/**
 * Class that can be used to run LineageTracker for testing/debugging.
 * 
 * @author Peter Krusche
 */
public class RunImageJ {
    private static final String pathToImageJ = "../fiji/";

/* Following block may be needed to tell ImageJ where the plugins are located.
 * This might remove the need to use the main() method below and just allow
 * new ImageJ() to be used instead.
 */
//
//    static{
//        System.setProperty("plugins.dir", pathToImageJ+"plugins");
//    }

    public static void main(String[] args) {
    	System.setProperty("plugins.dir", pathToImageJ+"plugins");
    	ij.Prefs.set("TrackApp.DEBUG","true");
    	new ImageJ();
        ImageJ.main(new String[]{"-ijpath", pathToImageJ});
    }
}
