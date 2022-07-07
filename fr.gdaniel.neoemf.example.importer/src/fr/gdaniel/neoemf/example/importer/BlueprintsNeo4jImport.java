package fr.gdaniel.neoemf.example.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;

import Core.CorePackage;
import DOM.DOMPackage;
import PrimitiveTypes.PrimitiveTypesPackage;
import fr.inria.atlanmod.neoemf.config.BaseConfig;
import fr.inria.atlanmod.neoemf.data.Backend;
import fr.inria.atlanmod.neoemf.data.blueprints.BlueprintsBackendFactory;
import fr.inria.atlanmod.neoemf.data.blueprints.neo4j.config.BlueprintsNeo4jConfig;
import fr.inria.atlanmod.neoemf.data.store.Store;
import fr.inria.atlanmod.neoemf.data.store.StoreFactory;
import fr.inria.atlanmod.neoemf.io.Migrator;

public class BlueprintsNeo4jImport {
	
	private static final String XMI_FILE = "resources/Grabats-set3.xmi";
	
	private static final String OUTPUT_DIRECTORY = "resources/output/";
	
	private static final String DATABASE_NAME = "Grabats-set3.graphdb";
	
	public static void main(String[] args) throws IOException {
		/*
		 * Register the EPackages from the JDTAST metamodel.
		 */
		EPackage.Registry.INSTANCE.put(CorePackage.eINSTANCE.getNsURI(), CorePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(DOMPackage.eINSTANCE.getNsURI(), DOMPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(PrimitiveTypesPackage.eINSTANCE.getNsURI(), PrimitiveTypesPackage.eINSTANCE);
		
		File model = new File(XMI_FILE);
		
		/*
		 * Configuration of the backend (the embedded Neo4j database).
		 */
		BlueprintsNeo4jConfig config = new BlueprintsNeo4jConfig();
        config.setLocation(Paths.get(OUTPUT_DIRECTORY));
        
        /*
         * Create an instance of the backend that can be used by the importer.
         */
        Backend backend = new BlueprintsBackendFactory().createBackend(URI.createURI(OUTPUT_DIRECTORY + DATABASE_NAME),
                config);
        
        /*
         * Additional configuration of NeoEMF. Here we want to set the autoSave feature, which forces the framework to commit
         * periodically the current transaction to avoid memory issues.
         */
        BaseConfig baseConfig = new BaseConfig<>();
        baseConfig.autoSave();
        
        /*
         * Create the store with the autoSave feature on top of the backend.
         */
        Store store = StoreFactory.getInstance().createStore(backend, baseConfig);

        /*
         * Migrate using the store (and not the backend). This way you benefit form the autoSave feature.
         */
        System.out.println("Importing NeoEMF database, this may take a while ...");
        long before = System.currentTimeMillis();
        Migrator.fromXmi(model).toMapper(store).migrate();
        long after = System.currentTimeMillis();
        System.out.println("Imported in " + (after - before) + " milliseconds");
	}

}
