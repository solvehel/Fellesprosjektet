package no.ntnu.fp.model;

import java.util.Iterator;

/**
 * The <code>Project</code> class is a list of zero or more {@link Person} objects.
 * 
 * @author Thomas &Oslash;sterlie
 * @version $Revision: 1.9 $ - $Date: 2005/02/22 07:53:33 $
 *
 */
public class Project {
	/**
	 * The member variable storing all registered {@link Person} objects.
	 */
	private java.util.ArrayList<Person> personList;
	
	/**
	 * Default constructor.  Must be called to initialize the object's member variables.
	 *
	 */
	public Project() {
		personList = new java.util.ArrayList<Person>();
	}

	/**
	 * Returns the number of {@linkplain #addPerson(Person) <code>Person</code> objects
	 * registered} with this class.
	 * 
	 * @return The number of {@link Person} objects in this class.
	 */
	public int getPersonCount() {
		return personList.size();
	}
	
	/**
	 * Returns the {@link Person} object at the specified position in the list.
	 * 
	 * @param i Index of object to return.
	 * 
	 * @return The {@link Person} object at the specified position in the list.
	 */
	public Person getPerson(int i) {
		return (Person)personList.get(i);
	}
	
	/**
	 * Returns the index of the first occurrence of the specified object, or 
	 * -1 if the list does not contain this object.
	 * 
	 * @param obj Object to search for.
	 *
	 * @return The index in this list of the first occurrence of the specified element, 
	 * or -1 if this list does not contain this element.
	 */
	public int indexOf(Object obj) {
		return personList.indexOf(obj);
	}

	/**
	 * Returns an iterator over the elements in this list in proper sequence.<P>
	 *
	 * @return A {@link java.util.Iterator} over the elements in this list in proper sequence.
	 * 
	 * @see java.util.Iterator <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/Iterator.html">java.util.Iterator</a>.
	 */
	public Iterator<Person> iterator() {
		return personList.iterator();
	}

	/**
	 * Adds a new {@link Person} object to the <code>Project</code>.<P>
	 * 
	 * @param person The {@link Person} object added.
	 */
	public void addPerson(Person person) {
		personList.add(person);
	}

	/**
	 * Removes the specified {@link Person} object from the <code>Project</code>.<P>
	 * 
	 * @param person The {@link Person} object to be removed.
	 */
	public void removePerson(Person person) {
		personList.remove(person);
	}
	
	/**
	 * Assigns all person objects in this Project to the input array.
	 * 
	 * @param array - an array that that should have the same size as the person count in this Project.
	 */
	public void toArray(Person[] array) {
		if (array.length != getPersonCount())
			return;
		
		Iterator<Person> it = iterator();
		for (int i = 0; it.hasNext(); i++) {
			array[i] = (Person) it.next();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (super.equals(o))
			return true;

		if (o.getClass() != this.getClass())
			return false;
		
		Project aProject = (Project)o;
		
		if (aProject.getPersonCount() != getPersonCount())
			return false;
		
		Iterator<Person> it = this.iterator();
		while (it.hasNext()) {
			Person aPerson = (Person)it.next();
			if (aProject.indexOf(aPerson) < 0)
				return false;
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		String s = "project:\n";
		Iterator<Person> it = this.iterator();
		while (it.hasNext()) {
			s += it.next().toString() + "\n";
		}
		return s;
	}
	
}
