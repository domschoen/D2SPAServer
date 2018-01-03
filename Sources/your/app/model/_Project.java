// DO NOT EDIT.  Make changes to Project.java instead.
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
public abstract class _Project extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "Project";

  // Attribute Keys
  public static final ERXKey<String> DESCR = new ERXKey<String>("descr");
  public static final ERXKey<Integer> PROJECT_NUMBER = new ERXKey<Integer>("projectNumber");

  // Relationship Keys
  public static final ERXKey<your.app.model.Customer> CUSTOMER = new ERXKey<your.app.model.Customer>("customer");

  // Attributes
  public static final String DESCR_KEY = DESCR.key();
  public static final String PROJECT_NUMBER_KEY = PROJECT_NUMBER.key();

  // Relationships
  public static final String CUSTOMER_KEY = CUSTOMER.key();

  private static final Logger log = LoggerFactory.getLogger(_Project.class);

  public Project localInstanceIn(EOEditingContext editingContext) {
    Project localInstance = (Project)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String descr() {
    return (String) storedValueForKey(_Project.DESCR_KEY);
  }

  public void setDescr(String value) {
    log.debug( "updating descr from {} to {}", descr(), value);
    takeStoredValueForKey(value, _Project.DESCR_KEY);
  }

  public Integer projectNumber() {
    return (Integer) storedValueForKey(_Project.PROJECT_NUMBER_KEY);
  }

  public void setProjectNumber(Integer value) {
    log.debug( "updating projectNumber from {} to {}", projectNumber(), value);
    takeStoredValueForKey(value, _Project.PROJECT_NUMBER_KEY);
  }

  public your.app.model.Customer customer() {
    return (your.app.model.Customer)storedValueForKey(_Project.CUSTOMER_KEY);
  }

  public void setCustomer(your.app.model.Customer value) {
    takeStoredValueForKey(value, _Project.CUSTOMER_KEY);
  }

  public void setCustomerRelationship(your.app.model.Customer value) {
    log.debug("updating customer from {} to {}", customer(), value);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      setCustomer(value);
    }
    else if (value == null) {
      your.app.model.Customer oldValue = customer();
      if (oldValue != null) {
        removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _Project.CUSTOMER_KEY);
      }
    } else {
      addObjectToBothSidesOfRelationshipWithKey(value, _Project.CUSTOMER_KEY);
    }
  }


  public static Project createProject(EOEditingContext editingContext, Integer projectNumber
) {
    Project eo = (Project) EOUtilities.createAndInsertInstance(editingContext, _Project.ENTITY_NAME);
    eo.setProjectNumber(projectNumber);
    return eo;
  }

  public static ERXFetchSpecification<Project> fetchSpec() {
    return new ERXFetchSpecification<Project>(_Project.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<Project> fetchAllProjects(EOEditingContext editingContext) {
    return _Project.fetchAllProjects(editingContext, null);
  }

  public static NSArray<Project> fetchAllProjects(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _Project.fetchProjects(editingContext, null, sortOrderings);
  }

  public static NSArray<Project> fetchProjects(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<Project> fetchSpec = new ERXFetchSpecification<Project>(_Project.ENTITY_NAME, qualifier, sortOrderings);
    NSArray<Project> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static Project fetchProject(EOEditingContext editingContext, String keyName, Object value) {
    return _Project.fetchProject(editingContext, ERXQ.equals(keyName, value));
  }

  public static Project fetchProject(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<Project> eoObjects = _Project.fetchProjects(editingContext, qualifier, null);
    Project eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Project that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static Project fetchRequiredProject(EOEditingContext editingContext, String keyName, Object value) {
    return _Project.fetchRequiredProject(editingContext, ERXQ.equals(keyName, value));
  }

  public static Project fetchRequiredProject(EOEditingContext editingContext, EOQualifier qualifier) {
    Project eoObject = _Project.fetchProject(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Project that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static Project localInstanceIn(EOEditingContext editingContext, Project eo) {
    Project localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
