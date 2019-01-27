// DO NOT EDIT.  Make changes to Product.java instead.
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
public abstract class _Product extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "Product";

  // Attribute Keys
  public static final ERXKey<String> DESCR = new ERXKey<String>("descr");
  public static final ERXKey<String> NAME = new ERXKey<String>("name");
  public static final ERXKey<Integer> NUM = new ERXKey<Integer>("num");

  // Relationship Keys
  public static final ERXKey<your.app.model.Customer> CUSTOMERS = new ERXKey<your.app.model.Customer>("customers");

  // Attributes
  public static final String DESCR_KEY = DESCR.key();
  public static final String NAME_KEY = NAME.key();
  public static final String NUM_KEY = NUM.key();

  // Relationships
  public static final String CUSTOMERS_KEY = CUSTOMERS.key();

  private static final Logger log = LoggerFactory.getLogger(_Product.class);

  public Product localInstanceIn(EOEditingContext editingContext) {
    Product localInstance = (Product)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String descr() {
    return (String) storedValueForKey(_Product.DESCR_KEY);
  }

  public void setDescr(String value) {
    log.debug( "updating descr from {} to {}", descr(), value);
    takeStoredValueForKey(value, _Product.DESCR_KEY);
  }

  public String name() {
    return (String) storedValueForKey(_Product.NAME_KEY);
  }

  public void setName(String value) {
    log.debug( "updating name from {} to {}", name(), value);
    takeStoredValueForKey(value, _Product.NAME_KEY);
  }

  public Integer num() {
    return (Integer) storedValueForKey(_Product.NUM_KEY);
  }

  public void setNum(Integer value) {
    log.debug( "updating num from {} to {}", num(), value);
    takeStoredValueForKey(value, _Product.NUM_KEY);
  }

  public NSArray<your.app.model.Customer> customers() {
    return (NSArray<your.app.model.Customer>)storedValueForKey(_Product.CUSTOMERS_KEY);
  }

  public NSArray<your.app.model.Customer> customers(EOQualifier qualifier) {
    return customers(qualifier, null);
  }

  public NSArray<your.app.model.Customer> customers(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    NSArray<your.app.model.Customer> results;
      results = customers();
      if (qualifier != null) {
        results = (NSArray<your.app.model.Customer>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<your.app.model.Customer>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }

  public void addToCustomers(your.app.model.Customer object) {
    includeObjectIntoPropertyWithKey(object, _Product.CUSTOMERS_KEY);
  }

  public void removeFromCustomers(your.app.model.Customer object) {
    excludeObjectFromPropertyWithKey(object, _Product.CUSTOMERS_KEY);
  }

  public void addToCustomersRelationship(your.app.model.Customer object) {
    log.debug("adding {} to customers relationship", object);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      addToCustomers(object);
    }
    else {
      addObjectToBothSidesOfRelationshipWithKey(object, _Product.CUSTOMERS_KEY);
    }
  }

  public void removeFromCustomersRelationship(your.app.model.Customer object) {
    log.debug("removing {} from customers relationship", object);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      removeFromCustomers(object);
    }
    else {
      removeObjectFromBothSidesOfRelationshipWithKey(object, _Product.CUSTOMERS_KEY);
    }
  }

  public your.app.model.Customer createCustomersRelationship() {
    EOEnterpriseObject eo = EOUtilities.createAndInsertInstance(editingContext(),  your.app.model.Customer.ENTITY_NAME );
    addObjectToBothSidesOfRelationshipWithKey(eo, _Product.CUSTOMERS_KEY);
    return (your.app.model.Customer) eo;
  }

  public void deleteCustomersRelationship(your.app.model.Customer object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _Product.CUSTOMERS_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllCustomersRelationships() {
    Enumeration<your.app.model.Customer> objects = customers().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteCustomersRelationship(objects.nextElement());
    }
  }


  public static Product createProduct(EOEditingContext editingContext, String name
, Integer num
) {
    Product eo = (Product) EOUtilities.createAndInsertInstance(editingContext, _Product.ENTITY_NAME);
    eo.setName(name);
    eo.setNum(num);
    return eo;
  }

  public static ERXFetchSpecification<Product> fetchSpec() {
    return new ERXFetchSpecification<Product>(_Product.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<Product> fetchAllProducts(EOEditingContext editingContext) {
    return _Product.fetchAllProducts(editingContext, null);
  }

  public static NSArray<Product> fetchAllProducts(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _Product.fetchProducts(editingContext, null, sortOrderings);
  }

  public static NSArray<Product> fetchProducts(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<Product> fetchSpec = new ERXFetchSpecification<Product>(_Product.ENTITY_NAME, qualifier, sortOrderings);
    NSArray<Product> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static Product fetchProduct(EOEditingContext editingContext, String keyName, Object value) {
    return _Product.fetchProduct(editingContext, ERXQ.equals(keyName, value));
  }

  public static Product fetchProduct(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<Product> eoObjects = _Product.fetchProducts(editingContext, qualifier, null);
    Product eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Product that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static Product fetchRequiredProduct(EOEditingContext editingContext, String keyName, Object value) {
    return _Product.fetchRequiredProduct(editingContext, ERXQ.equals(keyName, value));
  }

  public static Product fetchRequiredProduct(EOEditingContext editingContext, EOQualifier qualifier) {
    Product eoObject = _Product.fetchProduct(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Product that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static Product localInstanceIn(EOEditingContext editingContext, Product eo) {
    Product localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
