package userInterface;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;

public class Add_car_brands extends JDialog {

	private int id;
	private String title;
	private Typ typ;
	private JTextField tfCompanyName;
	private Database db;
	private JLabel lblTitle;

	public enum Typ {
		NEW, EDIT
	};

	/**
	 * @wbp.parser.constructor
	 */
	public Add_car_brands(Typ typ, int id) {
		this.id = id;
		this.typ = typ;
		title = "Modify";
		setTitle(title);

		createInterface();
		filltfCompanyNames();
	}

	
	public Add_car_brands(Typ typ) {
		this.typ = typ;

		title = "Add";
		setTitle(title);

		createInterface();
	}

	private void createInterface() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 144, 115, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut.gridx = 2;
		gbc_verticalStrut.gridy = 0;
		getContentPane().add(verticalStrut, gbc_verticalStrut);

		lblTitle = new JLabel(title);
		lblTitle.setFont(new Font("Yu Gothic UI", Font.BOLD, 15));
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.gridwidth = 3;
		gbc_lblTitle.insets = new Insets(0, 0, 5, 0);
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 1;
		getContentPane().add(lblTitle, gbc_lblTitle);

		Component verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_1.gridx = 2;
		gbc_verticalStrut_1.gridy = 2;
		getContentPane().add(verticalStrut_1, gbc_verticalStrut_1);

		JLabel lblCompanyName = new JLabel("Company Name *");
		lblCompanyName.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		GridBagConstraints gbc_lblCompanyName = new GridBagConstraints();
		gbc_lblCompanyName.insets = new Insets(0, 0, 5, 5);
		gbc_lblCompanyName.gridx = 0;
		gbc_lblCompanyName.gridy = 3;
		getContentPane().add(lblCompanyName, gbc_lblCompanyName);

		tfCompanyName = new JTextField();
		tfCompanyName.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		tfCompanyName.setColumns(10);
		GridBagConstraints gbc_tfCompanyName = new GridBagConstraints();
		gbc_tfCompanyName.gridwidth = 2;
		gbc_tfCompanyName.insets = new Insets(0, 0, 5, 0);
		gbc_tfCompanyName.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfCompanyName.gridx = 1;
		gbc_tfCompanyName.gridy = 3;
		getContentPane().add(tfCompanyName, gbc_tfCompanyName);

		Component verticalStrut_2 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_2 = new GridBagConstraints();
		gbc_verticalStrut_2.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_2.gridx = 2;
		gbc_verticalStrut_2.gridy = 4;
		getContentPane().add(verticalStrut_2, gbc_verticalStrut_2);

		JLabel label = new JLabel("*mandatory fields");
		label.setFont(new Font("Yu Gothic", Font.PLAIN, 10));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 5;
		getContentPane().add(label, gbc_label);

		JButton button = new JButton("Save");
		button.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 5);
		gbc_button.gridx = 1;
		gbc_button.gridy = 5;
		getContentPane().add(button, gbc_button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				db = new Database();

				if (!checkMandatoryFields()) {
					JOptionPane.showMessageDialog(null, "Please fill all mandatory fields out.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					if (typ == Typ.NEW) {

						String sql = "INSERT INTO car_brands (company_name)" + "VALUES(' "+ tfCompanyName.getText()+" ')";
						if (!db.insertData(sql)) {
							JOptionPane.showMessageDialog(null, "Unable to save data!", "Error",
									JOptionPane.ERROR_MESSAGE);
						} else
							JOptionPane.showMessageDialog(null, "Saved successfully!");

					} else {
						String sql = "UPDATE car_brands set company_name = ' "+tfCompanyName.getText()+" ' WHERE brand_id =  ' "+id+" ';";
						if (!db.updateData(sql))
							JOptionPane.showMessageDialog(null, "Unable to save data!", "Error",
									JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(null, "Saved successfully!");
					}
				} catch (Exception e2) {
				}
				CloseDialog();
			}
		});

		JButton button_1 = new JButton("Cancel");
		button_1.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.insets = new Insets(0, 0, 5, 0);
		gbc_button_1.gridx = 2;
		gbc_button_1.gridy = 5;
		getContentPane().add(button_1, gbc_button_1);
		button_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CloseDialog();				
			}
		});
		pack();
	    setResizable(true);
	    setVisible(true);
	}

	private boolean checkMandatoryFields() {
		if (this.tfCompanyName.getText().trim().length() == 0)
			return false;
		return true;
	}

	/**
	 * Closes the frame
	 */
	private void CloseDialog() {
		this.dispose();
	}
	
	/**
	 * fills the tfCompanyNames if a users gets modified
	 */
	private void filltfCompanyNames() {
		Database db = new Database();
		ResultSet rs = db.getData("select * from car_brands where brand_id = '" + id + "';");
		try {
			while (rs.next()) {
				tfCompanyName.setText(rs.getString(2));
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
