package fr.gdaniel.neoemf.example.parser;

import java.text.MessageFormat;
import java.util.Stack;

import fr.inria.atlanmod.neoemf.io.listener.AbstractEventListener;
import fr.inria.atlanmod.neoemf.io.proxy.ProxyAttribute;
import fr.inria.atlanmod.neoemf.io.proxy.ProxyElement;
import fr.inria.atlanmod.neoemf.io.proxy.ProxyReference;

public class CustomEventListener extends AbstractEventListener {

	private Stack<GenericObject> objects = new Stack<GenericObject>();
	

	@Override
	public void onInitialize() {
		System.out.println(MessageFormat.format("Initializing {0}", CustomEventListener.class.getSimpleName()));
	}

	@Override
	public void onStartElement(ProxyElement element) {
		GenericObject currentObject = new GenericObject();
		currentObject.setClassName(element.getMetaClass().getName());
		currentObject.setId(element.getId().getResolved().toLong());
		this.objects.push(currentObject);
	}

	@Override
	public void onAttribute(ProxyAttribute attribute) {
		GenericObject currentObject = this.objects.peek();
		currentObject.getAttributes().put(attribute.getName(), attribute.getValue().getResolved());
	}

	@Override
	public void onReference(ProxyReference reference) {
		/*
		 * Dirty hack: the parser first sends the onStartElement event, then the onReference event
		 * that associate the parent to the object currently at the top of the stack. We need to 
		 * retrieve the parent by popping the stack, update it to add the reference, then push the 
		 * current object back on the stack.
		 */
		GenericObject currentObject = this.objects.pop();
		GenericObject parentObject = this.objects.peek();
		parentObject.addReference(reference.getOrigin().getName(), reference.getValue().getResolved().toLong());
		this.objects.push(currentObject);
	}

	@Override
	public void onEndElement() {
		GenericObject currentObject = this.objects.pop();
		System.out.println(MessageFormat.format("New instance created\n{0}", currentObject.toString()));
	}

	@Override
	public void onComplete() {
	}

}
