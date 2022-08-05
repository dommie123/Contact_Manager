package models;

import java.awt.Image;
import java.io.Serializable;
import java.util.Random;

public class Contact implements Serializable {
	
	private static final long serialVersionUID = 4970534920875843L;
	
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private byte[] imageData;
	private transient Image contactPhoto;
	
	public Contact(String firstName, String lastName, String email, String phoneNumber) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.imageData = null;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Image getContactPhoto() {
		return contactPhoto;
	}

	public void setContactPhoto(Image contactPhoto) {
		this.contactPhoto = contactPhoto;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
