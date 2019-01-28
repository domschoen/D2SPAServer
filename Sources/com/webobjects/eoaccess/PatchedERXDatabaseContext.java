package com.webobjects.eoaccess;

import java.lang.reflect.InvocationTargetException;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOObjectStore;
import com.webobjects.eocontrol.EOObjectStoreCoordinator;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableSet;

import er.extensions.eof.ERXDatabaseContext;

public class PatchedERXDatabaseContext extends ERXDatabaseContext {

	public PatchedERXDatabaseContext(EODatabase database) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(database);
		// TODO Auto-generated constructor stub
	}

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
