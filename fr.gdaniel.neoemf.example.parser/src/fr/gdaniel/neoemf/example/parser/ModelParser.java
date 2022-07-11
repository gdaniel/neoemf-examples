package fr.gdaniel.neoemf.example.parser;

import java.io.File;

import org.eclipse.emf.ecore.EPackage;

import Core.CorePackage;
import DOM.DOMPackage;
import PrimitiveTypes.PrimitiveTypesPackage;
import fr.inria.atlanmod.neoemf.io.Migrator;

public class ModelParser {
	
	private static final String XMI_FILE = "resources/model.xmi";
	
	public static void main(String[] args) throws Exception {
		/*
		 * Register the EPackages from the JDTAST metamodel.
		 */
		EPackage.Registry.INSTANCE.put(CorePackage.eINSTANCE.getNsURI(), CorePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(DOMPackage.eINSTANCE.getNsURI(), DOMPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(PrimitiveTypesPackage.eINSTANCE.getNsURI(), PrimitiveTypesPackage.eINSTANCE);
		File model = new File(XMI_FILE);
		
        System.out.println("Importing NeoEMF database, this may take a while ...");
        long before = System.currentTimeMillis();
        Migrator.fromXmi(model).with(new CustomEventListener()).migrate();
        long after = System.currentTimeMillis();
        System.out.println("Imported in " + (after - before) + " milliseconds");
	}

}
