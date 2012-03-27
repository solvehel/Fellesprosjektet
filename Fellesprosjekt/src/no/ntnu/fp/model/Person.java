package no.ntnu.fp.model;

import java.util.Date;

/**
 * The <code>Person</code> class stores information about a single person.
 * 
 * @author Thomas &Oslash;sterlie
 *
 * @version $Revision: 1.5 $ - $Date: 2005/02/20 14:52:29 $
 */
public class Person {
	
	/**
	 * This member variable holds the person's name.
	 */
	private String name;
	
	/**
	 * This member variable holds the person's email address.
	 */
	private String email;
	
	/**
	 * This member variable holds the person's date of birth.
	 */
	private Date dateOfBirth;
	
	/**
	 * Default constructor. Must be called to initialize the object's member variables.
	 * The constructor sets the name and email of this person to empty
	 * {@link java.lang.String}, while the date of birth is given today's date.
	 */
	public Person() {
		name = "";
		email = "";
		dateOfBirth = new Date();
	}
	
	/**
	 * Constructs a new <code>Person</code> object with specified name, email, and date
	 * of birth.
	 * 
	 * @param name The name of the person.
	 * @param email The person's e-mail address
	 * @param dateOfBirth The person's date of birth.
	 */
	public Person(String name, String email, Date dateOfBirth) {
		this();
		this.name = name;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
	}
	
	/**
	 * Assigns a new name to the person.<P>
	 * 
	 * @param name The person's new name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Assigns a new email address to the person.<P>
	 * 
	 * @param email The person's new email address.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Assigns a new date of birth to the person.<P>
	 * 
	 * @param dateOfBirth The person's new date of birth.
	 */	
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	/**
	 * Returns the person's name.
	 * 
	 * @return The person's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the person's email address.
	 * 
	 * @return The person's email address.
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Returns the person's date of birth.
	 * 
	 * @return The person's date of birth.
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		
		if (obj.getClass() != this.getClass())
			return false;
		
		Person aPerson = (Person)obj;
		
		if (aPerson.getName().compareTo(getName()) != 0) 
			return false;
		if (aPerson.getEmail().compareTo(getEmail()) != 0)
			return false;
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		String s = "Name: " + getName() + "; ";
		s += "Email: " + getEmail() + "; ";
		return s;
	}
}
