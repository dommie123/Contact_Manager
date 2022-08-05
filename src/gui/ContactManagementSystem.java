package gui;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import gui.custom.ImagePanel;
import models.Contact;
import net.miginfocom.swing.MigLayout;

public class ContactManagementSystem {

	private JFrame frmContactManager;
	private JTextField txtPhoneNumber;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTextField txtEmail;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ContactManagementSystem window = new ContactManagementSystem();
					window.frmContactManager.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ContactManagementSystem() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmContactManager = new JFrame();
		frmContactManager.setTitle("Contact Manager");
		frmContactManager.setBounds(100, 100, 654, 460);
		frmContactManager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmContactManager.getContentPane().setLayout(new MigLayout("", "[238.00,grow][98.00,grow][98.00,grow][119.00]", "[132.00][][19.00][][][][104.00,grow][]"));
		
		ImagePanel contactPhoto = new ImagePanel();
		frmContactManager.getContentPane().add(contactPhoto, "cell 0 0,grow");
		
		JLabel lblFirstName = new JLabel("First Name");
		frmContactManager.getContentPane().add(lblFirstName, "cell 1 0,aligny bottom");
		
		JLabel lblLastName = new JLabel("Last Name");
		frmContactManager.getContentPane().add(lblLastName, "cell 2 0,aligny bottom");
		
		JButton btnSetImage = new JButton("Set Image");
		btnSetImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
		        FileNameExtensionFilter filter = new FileNameExtensionFilter(
		                "Image Files", "jpg", "png", "gif");
		        
		        chooser.setFileFilter(filter);
		        
		        int returnVal = chooser.showOpenDialog(null);
		        if(returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = chooser.getSelectedFile();
		            String path = file.getAbsolutePath();
		            ImageIcon imgPath = new ImageIcon(path);
		            
		            contactPhoto.setImage(
		            		imgPath.getImage().getScaledInstance(
		            				contactPhoto.getWidth(), 
		            				contactPhoto.getHeight(), 
		            				Image.SCALE_SMOOTH
		            				)
		            		);
		        }
			}
		});
		frmContactManager.getContentPane().add(btnSetImage, "cell 0 1,alignx right");
		
		txtFirstName = new JTextField();
		frmContactManager.getContentPane().add(txtFirstName, "cell 1 1,growx");
		txtFirstName.setColumns(10);
		
		txtLastName = new JTextField();
		frmContactManager.getContentPane().add(txtLastName, "cell 2 1,growx");
		txtLastName.setColumns(10);
		
		JLabel lblPhoneNumber = new JLabel("Phone Number");
		frmContactManager.getContentPane().add(lblPhoneNumber, "cell 1 3,aligny bottom");
		
		JLabel lblEmailAddress = new JLabel("Email Address");
		frmContactManager.getContentPane().add(lblEmailAddress, "cell 2 3,aligny bottom");
		
		txtPhoneNumber = new JTextField();
		frmContactManager.getContentPane().add(txtPhoneNumber, "cell 1 4,growx");
		txtPhoneNumber.setColumns(10);
		
		txtEmail = new JTextField();
		frmContactManager.getContentPane().add(txtEmail, "cell 2 4,growx");
		txtEmail.setColumns(10);
		
		JLabel lblContactList = new JLabel("Contacts");
		frmContactManager.getContentPane().add(lblContactList, "cell 0 5");
		
		DefaultListModel<Contact> contactModel = new DefaultListModel<>();
		try {
			List<Contact> savedContacts = deserializeList("Contacts.ser");
			for (Contact c : savedContacts) {
				if (c.getImageData() != null || c.getImageData().length > 0) {
					try (InputStream in = new ByteArrayInputStream(c.getImageData())) {
						Image img = ImageIO.read(in);//.getScaledInstance(contactPhoto.getWidth(), contactPhoto.getHeight(), Image.SCALE_SMOOTH);	
						c.setContactPhoto(img);
					} catch (IOException ex) {
						ex.printStackTrace();
					}

				}
				contactModel.addElement(c);
			}
		} catch (NullPointerException ex) {
			// do nothing
		}
		
		
		JList<Contact> list = new JList<Contact>(contactModel);
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				Contact selectedContact = list.getSelectedValue();
				
				if (selectedContact == null)
					return;
				
				txtFirstName.setText(selectedContact.getFirstName());
				txtLastName.setText(selectedContact.getLastName());
				txtPhoneNumber.setText(selectedContact.getPhoneNumber());
				txtEmail.setText(selectedContact.getEmail());
				contactPhoto.setImage(selectedContact.getContactPhoto());
			}
			
		});
		frmContactManager.getContentPane().add(list, "cell 0 6,grow");
		
		JButton btnNewContact = new JButton("New Contact");
		btnNewContact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {		
				String userFirstName = txtFirstName.getText();
				String userLastName = txtLastName.getText();
				String userPhoneNumber = txtPhoneNumber.getText();
				String userEmail = txtEmail.getText();
				Image userContactPhoto = contactPhoto.getImage();
				
				if (userFirstName != "" && userLastName != "") {
					Contact addedContact = new Contact(userFirstName, userLastName, userPhoneNumber, userEmail);
					addedContact.setContactPhoto(userContactPhoto);
					
					contactModel.addElement(addedContact);
				} else {
					JOptionPane.showMessageDialog(null, "Please add a first name and last name.", "First or Last Name Missing!", JOptionPane.WARNING_MESSAGE);
				}
				
				resetForm(txtFirstName, txtLastName, txtPhoneNumber, txtEmail, contactPhoto);
				
			}
		});
		frmContactManager.getContentPane().add(btnNewContact, "cell 1 7,growx");
		
		JButton btnUpdateContact = new JButton("Update Contact");
		btnUpdateContact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (list.getSelectedIndex() < 0) {
					JOptionPane.showMessageDialog(null, "Please select a contact from the list first!", "Contact Not Selected", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				Contact selectedContact = list.getSelectedValue();
				String userFirstName = txtFirstName.getText();
				String userLastName = txtLastName.getText();
				String userPhoneNumber = txtPhoneNumber.getText();
				String userEmail = txtEmail.getText();
				
				if (userFirstName != "" && userLastName != "") {
					selectedContact.setFirstName(userFirstName);
					selectedContact.setLastName(userLastName);
					selectedContact.setPhoneNumber(userPhoneNumber);
					selectedContact.setEmail(userEmail);
					selectedContact.setContactPhoto(contactPhoto.getImage());
					
					contactModel.setElementAt(selectedContact, list.getSelectedIndex());
					
					JOptionPane.showMessageDialog(null, selectedContact + " has been updated!", "Update Successful!", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "First and last name cannot be empty. Please try again.", "First or Last Name Missing!", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		frmContactManager.getContentPane().add(btnUpdateContact, "cell 2 7,growx");
		
		JButton btnDeleteContact = new JButton("Remove Contact");
		btnDeleteContact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() < 0) {
					JOptionPane.showMessageDialog(null, "Please select a contact from the list first!", "Contact Not Selected", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this contact?", "About to remove contact", JOptionPane.YES_NO_OPTION);
				
				if (response == JOptionPane.YES_OPTION) {
					contactModel.removeElementAt(list.getSelectedIndex());
					resetForm(txtFirstName, txtLastName, txtPhoneNumber, txtEmail, contactPhoto);
				}
			}
		});
		frmContactManager.getContentPane().add(btnDeleteContact, "cell 3 7,growx");
		
		JMenuBar menuBar = new JMenuBar();
		frmContactManager.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmSaveContacts = new JMenuItem("Save Contacts");
		mntmSaveContacts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				List<Contact> cList = new ArrayList<>(list.getModel().getSize());
				for (int i = 0; i < list.getModel().getSize(); i++) {
					Contact current = list.getModel().getElementAt(i);
					Contact updated = updateContactImageData(current, current.getContactPhoto());
				    cList.add(updated);
				}
				
				serializeList(cList, "Contacts.ser");
				JOptionPane.showMessageDialog(null, "Contants saved!", "Save", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnFile.add(mntmSaveContacts);
	}
	
	private void resetForm(JTextField txtFirstName, JTextField txtLastName, JTextField txtPhoneNumber, JTextField txtEmail, ImagePanel contactPhoto) {
		txtFirstName.setText("");
		txtLastName.setText("");
		txtPhoneNumber.setText("");
		txtEmail.setText("");
		contactPhoto.setImage(null);
	}
	
	private void serializeList(List<Contact> pList, String fileName)
	{
		try (ObjectOutputStream output = new ObjectOutputStream(
				new FileOutputStream(fileName)))
		{
			output.writeObject(pList);
		} catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "A Problem has occurred during serialization!"
					, e.getMessage(), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Contact> deserializeList(String fileName)
	{
		List<Contact> pList = null;
		try (ObjectInputStream input = new ObjectInputStream(
				new FileInputStream(fileName)))
		{
			pList = (List<Contact>) input.readObject();
		} catch (ClassNotFoundException | IOException e)
		{
			JOptionPane.showMessageDialog(null, "A Problem has occurred while deserializing the list!"
					, e.getMessage(), JOptionPane.ERROR_MESSAGE);
//			e.printStackTrace();
		}
		return pList;
	}
	
	private Contact updateContactImageData(Contact c, Image img) {
		try(ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			BufferedImage bImage = toBufferedImage(img);
			ImageIO.write(bImage, "png", output);
			byte[] imgBytes = output.toByteArray();
			
			c.setImageData(imgBytes);
			
			return c;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	private BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		
		BufferedImage bImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = bImage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		
		return bImage;
	}

}
