package your.app.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.foundation.NSArray;

public class Project extends _Project {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(Project.class);
	
	
	// Let's have a calculated relationship
	public NSArray<Customer> potentialCustomers() {
		return (NSArray<Customer>)EOUtilities.objectsForEntityNamed(editingContext(),Customer.ENTITY_NAME);
	}
}
