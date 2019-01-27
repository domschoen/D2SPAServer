// DO NOT EDIT.  Make changes to CustomerProduct.java instead.
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
public abstract class _CustomerProduct extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "CustomerProduct";

  // Attribute Keys

  // Relationship Keys
  public static final ERXKey<your.app.model.Customer> CUSTOMER = new ERXKey<your.app.model.Customer>("customer");
  public static final ERXKey<your.app.model.Product> PRODUCT = new ERXKey<your.app.model.Product>("product");

  // Attributes

  // Relationships
  public static final String CUSTOMER_KEY = CUSTOMER.key();
  public static final String PRODUCT_KEY = PRODUCT.key();

  private static final Logger log = LoggerFactory.getLogger(_CustomerProduct.class);

  public CustomerProduct localInstanceIn(EOEditingContext editingContext) {
    CustomerProduct localInstance = (CustomerProduct)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public your.app.model.Customer customer() {
    return (your.app.model.Customer)storedValueForKey(_CustomerProduct.CUSTOMER_KEY);
  }

  public void setCustomer(your.app.model.Customer value) {
    takeStoredValueForKey(value, _CustomerProduct.CUSTOMER_KEY);
  }

  public void setCustomerRelationship(your.app.model.Customer value) {
    log.debug("updating customer from {} to {}", customer(), value);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      setCustomer(value);
    }
    else if (value == null) {
      your.app.model.Customer oldValue = customer();
      if (oldValue != null) {
        removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _CustomerProduct.CUSTOMER_KEY);
      }
    } else {
      addObjectToBothSidesOfRelationshipWithKey(value, _CustomerProduct.CUSTOMER_KEY);
    }
  }

  public your.app.model.Product product() {
    return (your.app.model.Product)storedValueForKey(_CustomerProduct.PRODUCT_KEY);
  }

  public void setProduct(your.app.model.Product value) {
    takeStoredValueForKey(value, _CustomerProduct.PRODUCT_KEY);
  }

  public void setProductRelationship(your.app.model.Product value) {
    log.debug("updating product from {} to {}", product(), value);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      setProduct(value);
    }
    else if (value == null) {
      your.app.model.Product oldValue = product();
      if (oldValue != null) {
        removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _CustomerProduct.PRODUCT_KEY);
      }
    } else {
      addObjectToBothSidesOfRelationshipWithKey(value, _CustomerProduct.PRODUCT_KEY);
    }
  }


  public static CustomerProduct createCustomerProduct(EOEditingContext editingContext, your.app.model.Customer customer, your.app.model.Product product) {
    CustomerProduct eo = (CustomerProduct) EOUtilities.createAndInsertInstance(editingContext, _CustomerProduct.ENTITY_NAME);
    eo.setCustomerRelationship(customer);
    eo.setProductRelationship(product);
    return eo;
  }

  public static ERXFetchSpecification<CustomerProduct> fetchSpec() {
    return new ERXFetchSpecification<CustomerProduct>(_CustomerProduct.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<CustomerProduct> fetchAllCustomerProducts(EOEditingContext editingContext) {
    return _CustomerProduct.fetchAllCustomerProducts(editingContext, null);
  }

  public static NSArray<CustomerProduct> fetchAllCustomerProducts(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _CustomerProduct.fetchCustomerProducts(editingContext, null, sortOrderings);
  }

  public static NSArray<CustomerProduct> fetchCustomerProducts(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<CustomerProduct> fetchSpec = new ERXFetchSpecification<CustomerProduct>(_CustomerProduct.ENTITY_NAME, qualifier, sortOrderings);
    NSArray<CustomerProduct> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static CustomerProduct fetchCustomerProduct(EOEditingContext editingContext, String keyName, Object value) {
    return _CustomerProduct.fetchCustomerProduct(editingContext, ERXQ.equals(keyName, value));
  }

  public static CustomerProduct fetchCustomerProduct(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<CustomerProduct> eoObjects = _CustomerProduct.fetchCustomerProducts(editingContext, qualifier, null);
    CustomerProduct eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one CustomerProduct that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static CustomerProduct fetchRequiredCustomerProduct(EOEditingContext editingContext, String keyName, Object value) {
    return _CustomerProduct.fetchRequiredCustomerProduct(editingContext, ERXQ.equals(keyName, value));
  }

  public static CustomerProduct fetchRequiredCustomerProduct(EOEditingContext editingContext, EOQualifier qualifier) {
    CustomerProduct eoObject = _CustomerProduct.fetchCustomerProduct(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no CustomerProduct that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static CustomerProduct localInstanceIn(EOEditingContext editingContext, CustomerProduct eo) {
    CustomerProduct localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
