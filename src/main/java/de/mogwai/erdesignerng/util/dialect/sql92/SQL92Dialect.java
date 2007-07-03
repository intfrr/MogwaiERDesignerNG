package de.mogwai.erdesignerng.util.dialect.sql92;

import java.util.List;
import java.util.Vector;

import de.mogwai.erdesignerng.model.Attribute;
import de.mogwai.erdesignerng.model.Domain;
import de.mogwai.erdesignerng.model.Index;
import de.mogwai.erdesignerng.model.IndexType;
import de.mogwai.erdesignerng.model.Relation;
import de.mogwai.erdesignerng.model.Table;
import de.mogwai.erdesignerng.util.dialect.Dialect;

/**
 * Dialect for SQL92.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2007-06-29 16:59:20 $
 */
public abstract class SQL92Dialect extends Dialect {

	protected void addAttributeDefinition(StringBuffer aBuffer,
			Attribute aAttribute) {

		aBuffer.append(aAttribute.getName() + " "
				+ aAttribute.getDomain().getDatatype());
		if (!aAttribute.isNullable()) {
			aBuffer.append(" NOT NULL");
		}

	}

	@Override
	public List<String> createAddAttributeSQL(Attribute aAttribute) {
		StringBuffer theResult = new StringBuffer("ALTER TABLE ");
		theResult.append(aAttribute.getOwner().getName() + " ADD COLUMN ");

		addAttributeDefinition(theResult, aAttribute);

		Vector<String> theList = new Vector<String>();
		theList.add(theResult.toString());
		return theList;
	}

	@Override
	public List<String> createAddIndexSQL(Index aIndex) {
		List<String> theResult = new Vector<String>();

		if (aIndex.getIndexType().equals(IndexType.PRIMARYKEY)) {

			StringBuffer theStrResult = new StringBuffer("ALTER TABLE ");
			theStrResult.append(aIndex.getOwner().getName());
			theStrResult.append(" ADD CONSTRAINT ");
			theStrResult.append(aIndex.getName());
			theStrResult.append(" PRIMARY KEY (");

			boolean first = true;
			for (Attribute theAttribute : aIndex.getAttributes()) {
				if (!first) {
					theStrResult.append(",");
				}

				theStrResult.append(theAttribute.getName());

				first = false;
			}

			theStrResult.append(")");

			theResult.add(theStrResult.toString());

		} else {

			StringBuffer theStrResult = new StringBuffer("ALTER TABLE ");
			theStrResult.append(aIndex.getOwner().getName());
			
			theStrResult.append(" ADD CONSTRAINT");
			if (aIndex.getIndexType().equals(IndexType.UNIQUE)) {
				theStrResult.append(" UNIQUE");
			}
			theStrResult.append(" INDEX ");
			theStrResult.append(aIndex.getName());
			theStrResult.append(" (");

			boolean first = true;
			for (Attribute theAttribute : aIndex.getAttributes()) {
				if (!first) {
					theStrResult.append(",");
				}

				theStrResult.append(theAttribute.getName());

				first = false;
			}

			theStrResult.append(")");

			theResult.add(theStrResult.toString());

		}

		return theResult;
	}

	@Override
	public List<String> createAlterAttributeSQL(Table aTable,
			String attributeName, Domain aDomain, boolean aNullable) {
		//TODO [mse] implement functionality here
		return null;
	}

	@Override
	public List<String> createDropAttributeSQL(Attribute aAttribute) {
		StringBuffer theResult = new StringBuffer("ALTER TABLE ");
		theResult.append(aAttribute.getOwner().getName());
		theResult.append(" DROP COLUMN ");
		theResult.append(aAttribute.getName());

		Vector<String> theList = new Vector<String>();
		theList.add(theResult.toString());
		return theList;

	}

	@Override
	public List<String> createDropIndexSQL(Index aIndex) {
		List<String> theResult = new Vector<String>();

		StringBuffer theStrResult = null;

		if (aIndex.getIndexType().equals(IndexType.PRIMARYKEY)) {

			theStrResult = new StringBuffer("ALTER TABLE ");
			theStrResult.append(aIndex.getOwner().getName());
			theStrResult.append(" DROP PRIMARY KEY");
		} else {

			theStrResult = new StringBuffer("ALTER TABLE ");
			theStrResult.append(aIndex.getOwner().getName());
			theStrResult.append(" DROP INDEX ");
			theStrResult.append(aIndex.getName());
		}

		theResult.add(theStrResult.toString());

		return theResult;
	}

	@Override
	public List<String> createDropRelationSQL(Relation aRelation) {
		//TODO [mse] implement functionality here
		return null;
	}

	@Override
	public List<String> createDropTableSQL(Table aTable) {
		StringBuffer theResult = new StringBuffer("DROP TABLE ");
		theResult.append(aTable.getName());

		Vector<String> theList = new Vector<String>();
		theList.add(theResult.toString());
		return theList;

	}

	@Override
	public List<String> createAddRelationSQL(Relation aRelation) {
		//TODO [mse] implement functionality here
		return null;
	}

	@Override
	public List<String> createRenameAttributeSQL(Table aTable,
			Attribute aAttribute, String aNewName) {
		//TODO [mse] implement functionality here
		return null;
	}

	@Override
	public List<String> createRenameIndexSQL(Table aTable, Index index,
			String aNewName) {
		//TODO [mse] implement functionality here
		return null;
	}

	@Override
	public List<String> createRenameRelationSQL(Relation aRelation,
			String aNewName) {
		//TODO [mse] implement functionality here
		return null;
	}

	@Override
	public List<String> createRenameTableSQL(Table aTable, String aNewName) {
		StringBuffer theResult = new StringBuffer("ALTER TABLE ");
		theResult.append(aTable.getName());
		theResult.append(" RENAME TO ");
		theResult.append(aNewName);

		Vector<String> theList = new Vector<String>();
		theList.add(theResult.toString());
		return theList;

	}

	@Override
	public List<String> createAddTableSQL(Table aTable) {

		Vector<String> theList = new Vector<String>();

		StringBuffer theResult = new StringBuffer("CREATE TABLE ");
		theResult.append(aTable.getName() + " (");
		boolean first = true;
		for (Attribute theAttribute : aTable.getAttributes()) {
			if (!first) {
				theResult.append(",");
			}

			addAttributeDefinition(theResult, theAttribute);

			first = false;
		}
		theResult.append(")");

		theList.add(theResult.toString());

		for (Index theIndex : aTable.getIndexes()) {
			theList.addAll(createAddIndexSQL(theIndex));
		}

		return theList;

	}
}