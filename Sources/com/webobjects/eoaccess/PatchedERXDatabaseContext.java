package com.webobjects.eoaccess;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.eocontrol.EOObjectStore;
import com.webobjects.eocontrol.EOObjectStoreCoordinator;
import com.webobjects.eocontrol.EOTemporaryGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSMutableSet;
import com.webobjects.foundation._NSArrayUtilities;
import com.webobjects.foundation._NSDictionaryUtilities;

import er.extensions.eof.ERXDatabaseContext;

public class PatchedERXDatabaseContext extends ERXDatabaseContext {

	public PatchedERXDatabaseContext(EODatabase database) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(database);
		// TODO Auto-generated constructor stub
	}

	private NSDictionary _primaryKeyForObject(EOEnterpriseObject object)
	{
		NSDictionary pkDict = null;
		EOEntity entity = this._database.entityForObject(object);


		pkDict = entity.primaryKeyForGlobalID(_globalIDForObject(object));
		NSDictionary classPropPKValues = valuesForKeys(entity.primaryKeyAttributeNames(), object);
		if (classPropPKValues.count() > 0) {
			if (pkDict != null)
			{
				Enumeration enumerator = classPropPKValues.keyEnumerator();
				NSMutableDictionary newDict = pkDict.mutableClone();

				while (enumerator.hasMoreElements()) {
					String key = (String)enumerator.nextElement();
					Object value = classPropPKValues.objectForKey(key);
					if ((value != null) && (value != NSKeyValueCoding.NullValue) && (((!(value instanceof Number)) || (((Number)value).intValue() != 0))))
						newDict.setObjectForKey(value, key);
				}
				pkDict = newDict;
			} else {
				pkDict = classPropPKValues;
			}
		}
		if (!(entity.isPrimaryKeyValidInObject(pkDict))) {
			pkDict = null;
		}

		if ((pkDict == null) && 
				(this._delegate.respondsTo("databaseContextNewPrimaryKey")))
			pkDict = (NSDictionary)this._delegate.perform("databaseContextNewPrimaryKey", this, object, entity);


		EOStoredProcedure sp;
		if ((pkDict == null) && ((sp = entity.storedProcedureForOperation("EONextPrimaryKeyProcedure")) != null)) {
			EOAdaptorChannel adaptorChannel;
			try { 
				adaptorChannel = _obtainOpenChannel().adaptorChannel();
				adaptorChannel.executeStoredProcedure(sp, null);
				pkDict = adaptorChannel.returnValuesForLastStoredProcedureInvocation();
			} catch (Exception localException) {
				if (_delegateHandledDatabaseException(localException))
				{
					adaptorChannel = _obtainOpenChannel().adaptorChannel();
					adaptorChannel.executeStoredProcedure(sp, null);
					pkDict = adaptorChannel.returnValuesForLastStoredProcedureInvocation();
				} else if (_isDroppedConnectionException(localException))
				{
					this._database.handleDroppedConnection();
					adaptorChannel = _obtainOpenChannel().adaptorChannel();
					adaptorChannel.executeStoredProcedure(sp, null);
					pkDict = adaptorChannel.returnValuesForLastStoredProcedureInvocation();
				} else {
					throw NSForwardException._runtimeExceptionForThrowable(localException);
				}      
			}      
			//NSLog._conditionallyLogPrivateException(localException);

			if (pkDict != null) {
				pkDict = entity.primaryKeyForRow(pkDict);
			}

		}

		if (pkDict == null)
		{
			NSArray pkAttrs = entity.primaryKeyAttributes();      EOAttribute pkAttr;
			if ((pkAttrs.count() == 1) && ((pkAttr = (EOAttribute)pkAttrs.objectAtIndex(0)) != null) && (pkAttr.adaptorValueType() == 2) && (pkAttr.width() == 24))

			{
				byte[] buf = new byte[24];
				EOTemporaryGlobalID.assignGloballyUniqueBytes(buf);
				Object value = pkAttr.newValueForImmutableBytes(buf);
				pkDict = new NSDictionary(value, pkAttr.name());
			}

		}

		if (pkDict == null)
		{
			try {
				pkDict = adaptorContext()._newPrimaryKey(object, entity);
			}
			catch (Exception e) {
				if (_delegateHandledDatabaseException(e))

				{
					pkDict = adaptorContext()._newPrimaryKey(object, entity);
				} else if (_isDroppedConnectionException(e))

				{
					this._database.handleDroppedConnection();

					pkDict = adaptorContext()._newPrimaryKey(object, entity);
				} else {
					throw NSForwardException._runtimeExceptionForThrowable(e);
				}
			}
			//NSLog._conditionallyLogPrivateException(e);



		}

		return pkDict;
	}


	private EODatabaseOperation databaseOperationForGlobalID(EOGlobalID gid)
	{
		if (this._dbOperationsByGlobalID == null) {
			return null;
		}
		return ((EODatabaseOperation)this._dbOperationsByGlobalID.objectForKey(gid));
	}

	private void recordDatabaseOperation(EODatabaseOperation dbOp)
	{
		if ((this._editingContext == null) || (this._dbOperationsByGlobalID == null) || 
				(this._dbOperationsByGlobalID.objectForKey(dbOp) != null)) return;
		this._dbOperationsByGlobalID.setObjectForKey(dbOp, dbOp.globalID());
	}


	protected EODatabaseOperation databaseOperationForObject(EOEnterpriseObject object)
	{
		EOGlobalID gid = _globalIDForObject(object);
		EODatabaseOperation dbOp = databaseOperationForGlobalID(gid);
		if (dbOp != null) {
			return dbOp;
		}
		EOEntity entity = this._database.entityForObject(object);
		if (entity == null) {
			return null;
		}

		if (entity.primaryKeyAttributes().count() == 0) {
			throw new IllegalStateException("databaseOperationForObject: " + super.getClass().getName() + " " + this + " attempted to process an EO mapped to entity '" + entity.name() + "' which has no primary key defined. All entities must have a primary key specified. You should run the EOModeler consistency checker on the model containing this entity and perform whatever actions are necessary to ensure that the model is in a consistent state.");	 
		}

		dbOp = new EODatabaseOperation(gid, object, entity);

		NSDictionary dbSnapshot = snapshotForGlobalID(gid);
		if (dbSnapshot == null)
			dbSnapshot = NSDictionary.EmptyDictionary;
		dbOp.setDBSnapshot(dbSnapshot);

		NSArray keys = entity.classPropertyNames();
		int nkeys = keys.count();
		NSMutableDictionary objectValues = new NSMutableDictionary(nkeys);
		for (int i = 0; i < nkeys; ++i) {
			String key = (String)keys.objectAtIndex(i);
			Object value = object.storedValueForKey(key);
			objectValues.setObjectForKey((value != null) ? value : NSKeyValueCoding.NullValue, key);
		}

		NSMutableDictionary newRow = dbSnapshot.mutableClone();
		_NSDictionaryUtilities.overrideEntriesWithObjectsFromDictionaryKeys(newRow, objectValues, entity.dbSnapshotKeys());
		dbOp.setNewRow(newRow);
		recordDatabaseOperation(dbOp);
		return dbOp;
	}



	protected void recordInsertForObject(EOEnterpriseObject object)
	{
		EODatabaseOperation dbOp = databaseOperationForObject(object);
		dbOp.setDatabaseOperator(1);
		if (dbOp.dbSnapshot().count() != 0)
			throw new IllegalStateException("recordInsertForObject: " + super.getClass().getName() + " " + this + " found a snapshot for EO with Global ID:" + dbOp.globalID() + " that has been inserted into " + this._editingContext.getClass().getName() + "" + this._editingContext + ". Cannot insert an object that is already in the database");
	}
	
   private NSDictionary primaryKeyForIntermediateRowFromSourceObject(EOEnterpriseObject sourceObject, EORelationship rel, EOEnterpriseObject destObject)
   {
	     NSDictionary keyMap = rel._leftSideKeyMap();
	     EODatabaseOperation dbOp = databaseOperationForObject(sourceObject);
	     NSArray keys = (NSArray)keyMap.objectForKey("sourceKeys");
	     NSMutableDictionary pkDict1 = _NSDictionaryUtilities.mutableValuesForKeys(dbOp.newRow(), keys);
	     if (_NSDictionaryUtilities.containsAnyNullObject(pkDict1))
	     {
	       NSDictionary sourcePK = _entityForObject(sourceObject).primaryKeyForGlobalID(dbOp.globalID());
	       pkDict1 = _NSDictionaryUtilities.mutableValuesForKeys(sourcePK, keys);
	     }
	     _NSDictionaryUtilities.translateFromKeysToKeys(pkDict1, keys, (NSArray)keyMap.objectForKey("destinationKeys"));
	 
	     keyMap = rel._rightSideKeyMap();
	     dbOp = databaseOperationForObject(destObject);
	     keys = (NSArray)keyMap.objectForKey("destinationKeys");
	     NSMutableDictionary pkDict2 = _NSDictionaryUtilities.mutableValuesForKeys(dbOp.newRow(), keys);
	     if (_NSDictionaryUtilities.containsAnyNullObject(pkDict2))
	     {
	       NSDictionary destPK = _entityForObject(destObject).primaryKeyForGlobalID(dbOp.globalID());
	       pkDict2 = _NSDictionaryUtilities.mutableValuesForKeys(destPK, keys);
	     }
	     _NSDictionaryUtilities.translateFromKeysToKeys(pkDict2, keys, (NSArray)keyMap.objectForKey("sourceKeys"));
	 
	     pkDict1.addEntriesFromDictionary(pkDict2);
	     return pkDict1;
	}
	
	
	private EODatabaseOperation databaseOperationForIntermediateRowFromSourceObject(EOEnterpriseObject sourceObject, EORelationship rel, EOEnterpriseObject destObject)
	   {
	     NSDictionary pkDict = primaryKeyForIntermediateRowFromSourceObject(sourceObject, rel, destObject);
	     EOEntity entity = rel.intermediateEntity();
	     EOGlobalID gid = entity.globalIDForRow(pkDict);
	     if (gid == null) {
	       if (NSLog.debugLoggingAllowedForLevelAndGroups(1, 32768L)) {
	         NSLog.debug.appendln("Attempt to obtain globalID failed for entity:" + entity + "\n     relationship: " + rel + "\n     source object: " + sourceObject + "\n     destination object: " + destObject);
	       }
	 
	       throw new IllegalStateException("A valid global ID could not be obtained for entity named " + entity.name() + ", relationship named " + rel.name() + ", primary key dictionary " + pkDict + ".");
	     }
	 
	     EODatabaseOperation dbOp = databaseOperationForGlobalID(gid);
	     if (dbOp == null) {
	       dbOp = new EODatabaseOperation(gid, null, entity);
	       dbOp.setDBSnapshot(pkDict);
	       dbOp.setNewRow(pkDict.mutableClone());
	       recordDatabaseOperation(dbOp);
	     }
	 
	     return dbOp;
	}
	
	
    private void recordInsertForIntermediateRowFromSourceObject(EOEnterpriseObject sourceObject, EORelationship rel, EOEnterpriseObject destObject)
	{
	     EODatabaseOperation dbOp = databaseOperationForIntermediateRowFromSourceObject(sourceObject, rel, destObject);
	     dbOp.setDatabaseOperator(1);
	}
	 

    private NSMutableDictionary _mutableValuesForKeys(NSArray keys, EOEnterpriseObject object) {
        return ((NSMutableDictionary)valuesForKeys(keys, object));
      }
    
	
	
	private NSDictionary relayAttributesInRelationshipSourceObjectDestinationObject(EORelationship rel, EOEnterpriseObject sourceObject, EOEnterpriseObject destObject)
	{
		NSMutableDictionary changes = null;

		if (destObject == null) {
			return null;
		}
		EODatabaseOperation sourceDBOp = databaseOperationForObject(sourceObject);
		if (rel.isToManyToOne()) {
			recordInsertForIntermediateRowFromSourceObject(sourceObject, rel, destObject);
		} else {
			NSDictionary keyMap = rel._sourceToDestinationKeyMap();
			NSArray sourceKeys = (NSArray)keyMap.objectForKey("sourceKeys");
			NSArray destKeys = (NSArray)keyMap.objectForKey("destinationKeys");
			if (rel.foreignKeyInDestination()) {
				changes = _NSDictionaryUtilities.mutableValuesForKeys(sourceDBOp.newRow(), sourceKeys);
				_NSDictionaryUtilities.translateFromKeysToKeys(changes, sourceKeys, destKeys);
				recordUpdateForObject(destObject, changes);
			} else {
				changes = _mutableValuesForKeys(destKeys, destObject);
				_NSDictionaryUtilities.translateFromKeysToKeys(changes, destKeys, sourceKeys);
				sourceDBOp.newRow().addEntriesFromDictionary(changes);
			}
		}

		return changes;
	}




	private void relayPrimaryKeySourceObjectDestObjectRelationship(NSDictionary pkDict, EOEnterpriseObject sourceObject, EOEnterpriseObject destObject, EORelationship rel) {
		boolean destinationNeedsValues = false;
		NSArray destAttNames = _NSArrayUtilities.resultsOfPerformingSelector(rel.destinationAttributes(), _NSArrayUtilities._nameSelector);
		NSDictionary values = valuesForKeys(destAttNames, destObject);
		Enumeration enumerator = values.objectEnumerator();    
		Object value;    
		do {
			if (!(enumerator.hasMoreElements())) break;
			value = enumerator.nextElement(); 
		} while (value != NSKeyValueCoding.NullValue);
		destinationNeedsValues = true;



		if (!(destinationNeedsValues)) {
			return;
		}

		NSDictionary newPKDict = relayAttributesInRelationshipSourceObjectDestinationObject(rel, sourceObject, destObject);


		relayPrimaryKeyObjectEntity(newPKDict, destObject, rel.destinationEntity());
	}


	private void relayPrimaryKeyObjectEntity(NSDictionary pkDict, EOEnterpriseObject sourceObject, EOEntity entity) {
		NSArray rels = entity.relationships();
		NSArray classPropNames = entity.classPropertyNames();
		EODatabaseOperation dbOp = databaseOperationForObject(sourceObject);
		NSDictionary snapshot = (dbOp == null) ? NSDictionary.EmptyDictionary : dbOp.dbSnapshot();
		rels = entity.relationships();
		if (rels != null) {
			int iCount = rels.count();
			for (int i = iCount - 1; i >= 0; --i) {
				EORelationship rel = ((EORelationship)rels.objectAtIndex(i))._substitutionRelationshipForRow(snapshot);
				if (rel == null) {
					continue;
				}
				if ((!(rel.propagatesPrimaryKey())) || (!(classPropNames.containsObject(rel.name()))))
				{
					continue;
				}

				Object destObject = sourceObject.storedValueForKey(rel.name());
				if (destObject == null)
				{
					continue;
				}

				NSDictionary objectSnapshot = _currentCommittedSnapshotForObject(sourceObject);
				Object snapshotDestObject = objectSnapshot.objectForKey(rel.name());

				if (destObject == snapshotDestObject) {
					continue;
				}
				if (rel.isToMany())

				{
					Object cheapCopy = ((NSArray)destObject).immutableClone();
					if (snapshotDestObject != cheapCopy) {
						NSArray destObjects = (NSArray)destObject;
						for (int j = destObjects.count() - 1; j >= 0; --j) {
							destObject = destObjects.objectAtIndex(j);
							relayPrimaryKeySourceObjectDestObjectRelationship(pkDict, sourceObject, (EOEnterpriseObject)destObject, rel);
						}
					}
					cheapCopy = null;
				}
				else {
					relayPrimaryKeySourceObjectDestObjectRelationship(pkDict, sourceObject, (EOEnterpriseObject)destObject, rel);
				}
			}
		}
	}


	public void prepareForSaveWithCoordinator(EOObjectStoreCoordinator coordinator, EOEditingContext editingContext)
	{
		_EOAssertSafeMultiThreadedAccess(this);
		if (this._flags_preparingForSave) {
			throw new IllegalStateException("prepareForSaveWithCoordinator: " + this + " is currently saving for " + this._editingContext + " so it cannot prepare to save for " + editingContext + ".  " + ((coordinator == null) ? "Coordinator is null." : new StringBuilder().append(coordinator).append(" has sources ").append(coordinator.cooperatingObjectStores()).toString()));

		}

		if (coordinator != null) {
			EOObjectStoreCoordinator current = coordinator();
			if (current == null)
				setCoordinator(coordinator);
			else if (current != coordinator) {
				throw new IllegalStateException("prepareForSaveWithCoordinator: " + this + " already has an EOObjectStoreCoordinator.  The database context needs to be removed from the cooperating object stores before it can be assigned to a different coordinator.");


			}

		}

		this._editingContext = editingContext;

		this._dbOperationsByGlobalID = new NSMutableDictionary(100);

		this._flags_willPrepareForSave = false;
		this._flags_preparingForSave = true;

		if (this._missingObjectGIDs.count() != 0)
		{
			NSArray updatedObjs = editingContext.updatedObjects();
			for (int i = updatedObjs.count() - 1; i >= 0; --i) {
				Object gid = editingContext.globalIDForObject((EOEnterpriseObject)updatedObjs.objectAtIndex(i));
				if (this._missingObjectGIDs.member(gid) == null)
					continue;
				throw new EOObjectNotAvailableException("prepareForSaveWithCoordinator: Cannot save the object with globalID " + gid + ". The row referenced by this globalID was missing from the database at the time a fetch was attempted. Either it was removed from the database after this application got a pointer to it, or there is a referential integrity problem with your database. To be notified when fetches fail, implement a delegate on EODatabaseContext that responds to databaseContextFailedToFetchObject().");
			}

		}

		_buildPrimaryKeyGeneratorListForEditingContext(editingContext);

		NSArray insertedObjects = editingContext.insertedObjects();

		int cnt = insertedObjects.count();
		NSMutableDictionary pkNeededByEntityName = new NSMutableDictionary();


		this._checkPropagatedPKs = new NSMutableArray(cnt);
		for (int i = 0; i < cnt; ++i) {
			EOEnterpriseObject o = (EOEnterpriseObject)insertedObjects.objectAtIndex(i);
			EOEntity entity = this._database.entityForObject(o);
			if (entity == null) {
				continue;
			}
			recordInsertForObject(o);
			if (_shouldGeneratePrimaryKeyForEntityName(entity.name())) {
				NSDictionary pkDict = _primaryKeyForObject(o);
				if (pkDict == null)
				{
					String rootEntityName = entity.rootParent().name();
					NSMutableArray insertedForEntity = (NSMutableArray)pkNeededByEntityName.objectForKey(rootEntityName);
					if (insertedForEntity == null) {
						insertedForEntity = new NSMutableArray(cnt);
						pkNeededByEntityName.setObjectForKey(insertedForEntity, rootEntityName);
					}
					insertedForEntity.addObject(o);
				} else {
					databaseOperationForObject(o).newRow().addEntriesFromDictionary(pkDict);
					relayPrimaryKeyObjectEntity(pkDict, o, entity);
				}
			} else {
				this._checkPropagatedPKs.addObject(o);
			}
		}
		Enumeration enumerator = pkNeededByEntityName.keyEnumerator();
		while (enumerator.hasMoreElements()) {
			String rootEntityName = (String)enumerator.nextElement();
			NSMutableArray insertedForEntity = (NSMutableArray)pkNeededByEntityName.objectForKey(rootEntityName);
			int pkCount = insertedForEntity.count();
			NSArray newPKs = _batchNewPrimaryKeysWithEntity(pkCount, this._database.entityNamed(rootEntityName));
			if ((newPKs == null) || (newPKs.count() != pkCount)) {
				throw new IllegalStateException("Adaptor " + this._database.adaptor() + " failed to provide new primary keys for entity '" + rootEntityName + "'");
			}
			for (int j = 0; j < pkCount; ++j) {
				NSDictionary pk = (NSDictionary)newPKs.objectAtIndex(j);
				EOEnterpriseObject eo = (EOEnterpriseObject)insertedForEntity.objectAtIndex(j);
				databaseOperationForObject(eo).newRow().addEntriesFromDictionary(pk);
				relayPrimaryKeyObjectEntity(pk, eo, this._database.entityForObject(eo));
			}
		}
	}

	private NSArray _batchNewPrimaryKeysWithEntity(int cnt, EOEntity rootEntity)
	{
		NSArray pks = null;
		try {
			pks = _obtainOpenChannel().adaptorChannel().primaryKeysForNewRowsWithEntity(cnt, rootEntity);
		} catch (Exception localException) {
			if (_delegateHandledDatabaseException(localException))
			{
				pks = _obtainOpenChannel().adaptorChannel().primaryKeysForNewRowsWithEntity(cnt, rootEntity);
			} else if (_isDroppedConnectionException(localException))
			{
				this._database.handleDroppedConnection();
				pks = _obtainOpenChannel().adaptorChannel().primaryKeysForNewRowsWithEntity(cnt, rootEntity);
			} else {
				throw NSForwardException._runtimeExceptionForThrowable(localException);
			}
		}
		return pks; }










	protected void _buildPrimaryKeyGeneratorListForEditingContext(EOEditingContext ec)
	{
		NSMutableArray array = new NSMutableArray(3);

		if (this._nonPrimaryKeyGenerators == null) {
			this._nonPrimaryKeyGenerators = new NSMutableSet();

		}

		NSMutableSet processedEntities = new NSMutableSet(16);
		this._nonPrimaryKeyGenerators.removeAllObjects();

		array.addObject(ec.updatedObjects());
		array.addObject(ec.insertedObjects());
		array.addObject(ec.deletedObjects());

		try
		{
			for (int i = array.count() - 1; i >= 0; --i) {
				NSArray objects = (NSArray) array.objectAtIndex(i);
				for (int j = objects.count() - 1; j >= 0; --j) {
					EOEnterpriseObject obj = (EOEnterpriseObject) objects.objectAtIndex(j);
					EOEntity entity = _entityForObject(obj);

					String entityName = entity.name();
					if (processedEntities.member(entityName) != null)
					{
						continue;
					}

					processedEntities.addObject(entityName);

					NSArray rels = entity.relationships();
					int iCount = rels.count();
					for (int k = iCount - 1; k >= 0; --k) {
						EORelationship rel = (EORelationship) rels.objectAtIndex(k);
						EORelationship reverseRel = rel.inverseRelationship();
						rel = reverseRel == null ? rel : reverseRel;
						if (rel.propagatesPrimaryKey()) {
							EOEntity destEntity = rel.destinationEntity();
							NSMutableArray array1 = new NSMutableArray(8);
							array1.addObject(entity);
							NSMutableArray array2 = new NSMutableArray(8);
							EOEntity._assertNoPropagateKeyCycleWithEntitiesRelationships(array1, array2);
							this._nonPrimaryKeyGenerators.addObject(destEntity.name());
						}
					}

					EOEntity rootParent = entity.rootParent();
					if (rootParent == entity)
						continue;
					EOModel rootParentModel = rootParent.model();
					if ((rootParentModel == null) || (this._database.models().indexOfIdenticalObject(rootParentModel) != -1)) {
						continue;
					}
					EOObjectStore rootObjectStore = ec.rootObjectStore();
					if (!(rootObjectStore instanceof EOObjectStoreCoordinator))
						continue;
					EOObjectStoreCoordinator coord = (EOObjectStoreCoordinator) rootObjectStore;

					EOObjectStore objStore = coord.objectStoreForFetchSpecification(new EOFetchSpecification(rootParent.name(), null, null, false, false, null));
					if (objStore == null)
						throw new IllegalStateException("Unable to find an EOObjectStore for root parent entity '" + rootParent.name() + "' of entity '" + entityName + "'.");
					if (this._database.models().indexOfIdenticalObject(rootParentModel) != -1) {
						continue;
					}

					this._nonPrimaryKeyGenerators.addObject(entityName);
				}

			}

		} catch (Exception localException)
		{
			throw NSForwardException._runtimeExceptionForThrowable(localException);
		}
	}
	protected EOEntity _entityForObject(
			EOEnterpriseObject enterpriseObject)
	{
		if (enterpriseObject == null) {
			throw new IllegalArgumentException(
					"Enterprise object cannot be null");
		}

		EOEntity domesticEntity = this._database
				.entityForObject(enterpriseObject);

		if (domesticEntity != null) {
			return domesticEntity;
		}

		EOObjectStoreCoordinator coordinator = coordinator();

		if (coordinator == null) {
			throw new IllegalStateException(
					"Object store coordinator cannot be null");
		}

		NSArray objectStores = coordinator.cooperatingObjectStores();

		int i = 0;
		for (int c = objectStores.count(); i < c; ++i) {
			Object objectStore = objectStores.objectAtIndex(i);

			if (objectStore instanceof EODatabaseContext) {
				EOEntity foreignEntity = ((EODatabaseContext) objectStore)
						.database().entityForObject(enterpriseObject);

				if (foreignEntity != null)
				{
					return foreignEntity;
				}
			}
		}

		throw new IllegalStateException(
				"Unable to find entity for object " + enterpriseObject);
	}

}
