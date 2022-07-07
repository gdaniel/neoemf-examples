package fr.gdaniel.neoemf.example.importer;

import java.io.File;
import java.util.stream.StreamSupport;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import Core.CorePackage;
import DOM.DOMPackage;
import PrimitiveTypes.PrimitiveTypesPackage;
import fr.inria.atlanmod.neoemf.config.BaseConfig;
import fr.inria.atlanmod.neoemf.data.blueprints.util.BlueprintsUriFactory;

public class BlueprintsNeo4jReader {
	
	private static String DATABASE_LOCATION = "resources/output/Grabats-set3.graphdb";
	
	public static void main(String[] args) throws Exception {
		/*
		 * Register the EPackages from the JDTAST metamodel.
		 */
		EPackage.Registry.INSTANCE.put(CorePackage.eINSTANCE.getNsURI(), CorePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(DOMPackage.eINSTANCE.getNsURI(), DOMPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(PrimitiveTypesPackage.eINSTANCE.getNsURI(), PrimitiveTypesPackage.eINSTANCE);
		
		/*
		 * Create a new ResourceSet (the same as in regular EMF)
		 */
		ResourceSet rSet = new ResourceSetImpl();
		
		/*
		 * Use the BlueprintsUriFactory to create a URI to your NeoEMF database. This will create a URI with a 
		 * specific protocol that can be used by NeoEMF to instantiate the correct backend.
		 */
		URI resourceURI = new BlueprintsUriFactory().createLocalUri(new File(DATABASE_LOCATION));
		
		/*
		 * You can configure NeoEMF to cache things if you want to speed up the computation (but this consumes more memory)
		 */
		BaseConfig config = new BaseConfig<>();
//		config.cacheContainers();
		
		/*
		 * Create the resource and immediately load it. This way you are sure you operate on top of the database, and not
		 * on an in-memory resource.
		 */
		Resource resource = rSet.createResource(resourceURI);
		resource.load(config.toMap());
		
		/*
		 * Some manipulation of the resource using the standard EMF API.
		 */
		EObject rootElement = resource.getContents().get(0);
		System.out.println(rootElement);
		Iterable<EObject> allContents = () -> resource.getAllContents();
		long before = System.currentTimeMillis();
		long eObjectCount = StreamSupport.stream(allContents.spliterator(), false).count();
		long after = System.currentTimeMillis();
		System.out.println("Resource contains " + eObjectCount + " EObjects");
		/*
		 * Note: a full traversal of the resource is quite costly in terms of memory/processor (you are basically loading everything
		 * from the database). This takes time with NeoEMF, but you can speed up things if you know what you are looking for, for
		 * example by using PersistentResource#allInstances(EClass).
		 */
		System.out.println("Traversal done in " + (after - before) + " milliseconds");
	}

}
