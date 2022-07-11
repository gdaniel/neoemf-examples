package fr.gdaniel.neoemf.example.parser;

import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import Core.CoreFactory;
import Core.CorePackage;
import Core.IField;
import Core.IMethod;
import Core.IType;
import DOM.DOMPackage;
import PrimitiveTypes.PrimitiveTypesPackage;

public class CreateModel {
	
	public static void main(String[] args) throws Exception{
		/*
		 * Register the EPackages from the JDTAST metamodel.
		 */
		EPackage.Registry.INSTANCE.put(CorePackage.eINSTANCE.getNsURI(), CorePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(DOMPackage.eINSTANCE.getNsURI(), DOMPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(PrimitiveTypesPackage.eINSTANCE.getNsURI(), PrimitiveTypesPackage.eINSTANCE);
		
		ResourceSet rSet = new ResourceSetImpl();
		rSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		
		URI resourceURI = URI.createFileURI("resources/model.xmi");
		Resource resource = rSet.createResource(resourceURI);
		
		System.out.println("Creating sample model for JDTAST metamodel");
		
		IMethod method = CoreFactory.eINSTANCE.createIMethod();
		method.setElementName("myMethod");
		
		IField field1 = CoreFactory.eINSTANCE.createIField();
		field1.setElementName("field1");
		field1.setTypeSignature("int");
		
		IField field2 = CoreFactory.eINSTANCE.createIField();
		field2.setElementName("field2");
		field2.setTypeSignature("String");
		
		IType type = CoreFactory.eINSTANCE.createIType();
		type.setElementName("MyType");
		type.getMethods().add(method);
		type.getFields().add(field1);
		type.getFields().add(field2);
		
		resource.getContents().add(type);
		resource.save(Collections.emptyMap());
		
		System.out.println("Done");
	}

}
