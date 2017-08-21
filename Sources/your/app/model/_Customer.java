// DO NOT EDIT.  Make changes to Customer.java instead.
package your.app.model;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;

import er.extensions.eof.*;
import er.extensions.foundation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public abstract class _Customer extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "Customer";

  // Attribute Keys
  public static final ERXKey<String> ACRONYM = new ERXKey<String>("acronym");
  public static final ERXKey<String> ADDRESS = new ERXKey<String>("address");
  public static final ERXKey<String> NAME = new ERXKey<String>("name");

  // Relationship Keys

  // Attributes
  public static final String ACRONYM_KEY = ACRONYM.key();
  public static final String ADDRESS_KEY = ADDRESS.key();
  public static final String NAME_KEY = NAME.key();

  // Relationships

  private static final Logger log = LoggerFactory.getLogger(_Customer.class);

  public Customer localInstanceIn(EOEditingContext editingContext) {
    Customer localInstance = (Customer)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String acronym() {
    return (String) storedValueForKey(_Customer.ACRONYM_KEY);
  }

  public void setAcronym(String value) {
    log.debug( "updating acronym from {} to {}", acronym(), value);
    takeStoredValueForKey(value, _Customer.ACRONYM_KEY);
  }

  public String address() {
    return (String) storedValueForKey(_Customer.ADDRESS_KEY);
  }

  public void setAddress(String value) {
    log.debug( "updating address from {} to {}", address(), value);
    takeStoredValueForKey(value, _Customer.ADDRESS_KEY);
  }

  public String name() {
    return (String) storedValueForKey(_Customer.NAME_KEY);
  }

  public void setName(String value) {
    log.debug( "updating name from {} to {}", name(), value);
    takeStoredValueForKey(value, _Customer.NAME_KEY);
  }


  public static Customer createCustomer(EOEditingContext editingContext, String acronym
, String address
, String name
) {
    Customer eo = (Customer) EOUtilities.createAndInsertInstance(editingContext, _Customer.ENTITY_NAME);
    eo.setAcronym(acronym);
    eo.setAddress(address);
    eo.setName(name);
    return eo;
  }

  public static ERXFetchSpecification<Customer> fetchSpec() {
    return new ERXFetchSpecification<Customer>(_Customer.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<Customer> fetchAllCustomers(EOEditingContext editingContext) {
    return _Customer.fetchAllCustomers(editingContext, null);
  }

  public static NSArray<Customer> fetchAllCustomers(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _Customer.fetchCustomers(editingContext, null, sortOrderings);
  }

  public static NSArray<Customer> fetchCustomers(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<Customer> fetchSpec = new ERXFetchSpecification<Customer>(_Customer.ENTITY_NAME, qualifier, sortOrderings);
    NSArray<Customer> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static Customer fetchCustomer(EOEditingContext editingContext, String keyName, Object value) {
    return _Customer.fetchCustomer(editingContext, ERXQ.equals(keyName, value));
  }

  public static Customer fetchCustomer(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<Customer> eoObjects = _Customer.fetchCustomers(editingContext, qualifier, null);
    Customer eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Customer that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static Customer fetchRequiredCustomer(EOEditingContext editingContext, String keyName, Object value) {
    return _Customer.fetchRequiredCustomer(editingContext, ERXQ.equals(keyName, value));
  }

  public static Customer fetchRequiredCustomer(EOEditingContext editingContext, EOQualifier qualifier) {
    Customer eoObject = _Customer.fetchCustomer(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Customer that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static Customer localInstanceIn(EOEditingContext editingContext, Customer eo) {
    Customer localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
