package analysers;

import ij.IJ;
import ij.Prefs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONArray;

import util.Cell;
import java.awt.GridLayout;

/** 
 * Copyright 2013 University of Warwick
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * Dialog to preview which lineages will be exported.
 *
 * @author Peter Krusche
 */
public class FilterValidatedDialog extends JDialog {

	private static final long serialVersionUID = 3582984663590000927L;
	private final JPanel contentPanel = new JPanel();
	private JTextField startLin;
	private JTextField minLinLen;
	private JLabel lblMinLineageLength;
	private JLabel lblStartFrameOf;
	private JTextArea preview;
	
	private List<Cell> cells = null; ///< Lineagetracker cells
	private static int valStartLin  = -1;
	private static int valMinLinLen = -1;
	private static int doExportMean = 1;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnExportMeanIntensity;
	private JRadioButton rdbtnExportMaxIntensity;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FilterValidatedDialog dialog = new FilterValidatedDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public FilterValidatedDialog() {
		setTitle("Validated Lineages: Export Filter");
		setBounds(100, 100, 632, 348);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{160, 440, 0};
		gbl_contentPanel.rowHeights = new int[]{1, 28, 28, 28, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblCriterion = new JLabel("Lineage Criterion");
			lblCriterion.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			GridBagConstraints gbc_lblCriterion = new GridBagConstraints();
			gbc_lblCriterion.insets = new Insets(0, 0, 5, 5);
			gbc_lblCriterion.gridx = 0;
			gbc_lblCriterion.gridy = 0;
			contentPanel.add(lblCriterion, gbc_lblCriterion);
		}
		{
			JLabel lblValue = new JLabel("Value");
			lblValue.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			GridBagConstraints gbc_lblValue = new GridBagConstraints();
			gbc_lblValue.insets = new Insets(0, 0, 5, 0);
			gbc_lblValue.gridx = 1;
			gbc_lblValue.gridy = 0;
			contentPanel.add(lblValue, gbc_lblValue);
		}
		{
			lblMinLineageLength = new JLabel("Min. Length (frames)");
			GridBagConstraints gbc_lblMinLineageLength = new GridBagConstraints();
			gbc_lblMinLineageLength.fill = GridBagConstraints.BOTH;
			gbc_lblMinLineageLength.insets = new Insets(0, 0, 5, 5);
			gbc_lblMinLineageLength.gridx = 0;
			gbc_lblMinLineageLength.gridy = 1;
			contentPanel.add(lblMinLineageLength, gbc_lblMinLineageLength);
		}
		{
			minLinLen = new JTextField();
			GridBagConstraints gbc_minLinLen = new GridBagConstraints();
			gbc_minLinLen.anchor = GridBagConstraints.NORTH;
			gbc_minLinLen.fill = GridBagConstraints.HORIZONTAL;
			gbc_minLinLen.insets = new Insets(0, 0, 5, 0);
			gbc_minLinLen.gridx = 1;
			gbc_minLinLen.gridy = 1;
			contentPanel.add(minLinLen, gbc_minLinLen);
			minLinLen.setText("1");
			minLinLen.setColumns(3);
		}
		{
			lblStartFrameOf = new JLabel("Max. Start Frame");
			GridBagConstraints gbc_lblStartFrameOf = new GridBagConstraints();
			gbc_lblStartFrameOf.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblStartFrameOf.insets = new Insets(0, 0, 5, 5);
			gbc_lblStartFrameOf.gridx = 0;
			gbc_lblStartFrameOf.gridy = 2;
			contentPanel.add(lblStartFrameOf, gbc_lblStartFrameOf);
		}
		{
			startLin = new JTextField();
			GridBagConstraints gbc_startLin = new GridBagConstraints();
			gbc_startLin.insets = new Insets(0, 0, 5, 0);
			gbc_startLin.fill = GridBagConstraints.HORIZONTAL;
			gbc_startLin.gridx = 1;
			gbc_startLin.gridy = 2;
			contentPanel.add(startLin, gbc_startLin);
			startLin.setText("-1");
			startLin.setColumns(3);
		}
		{
			JButton btnPreview = new JButton("Preview");
			btnPreview.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					preview.setText(makePreview());
				}
			});
			GridBagConstraints gbc_btnPreview = new GridBagConstraints();
			gbc_btnPreview.insets = new Insets(0, 0, 5, 5);
			gbc_btnPreview.gridx = 0;
			gbc_btnPreview.gridy = 3;
			contentPanel.add(btnPreview, gbc_btnPreview);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 1;
			gbc_scrollPane.gridy = 3;
			contentPanel.add(scrollPane, gbc_scrollPane);
			{
				preview = new JTextArea();
				scrollPane.setViewportView(preview);
				preview.setEditable(false);
				preview.setColumns(10);
				preview.setTabSize(4);
			}
		}
		{
			JLabel lblOptions = new JLabel("Options");
			GridBagConstraints gbc_lblOptions = new GridBagConstraints();
			gbc_lblOptions.insets = new Insets(0, 0, 0, 5);
			gbc_lblOptions.gridx = 0;
			gbc_lblOptions.gridy = 4;
			contentPanel.add(lblOptions, gbc_lblOptions);
		}
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 1;
			gbc_panel.gridy = 4;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new GridLayout(1, 2, 0, 0));
			{
				rdbtnExportMeanIntensity = new JRadioButton("Export Mean Intensity");
				rdbtnExportMeanIntensity.setSelected(true);
				buttonGroup.add(rdbtnExportMeanIntensity);
				panel.add(rdbtnExportMeanIntensity);
			}
			{
				rdbtnExportMaxIntensity = new JRadioButton("Export Max Intensity");
				buttonGroup.add(rdbtnExportMaxIntensity);
				panel.add(rdbtnExportMaxIntensity);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (validateFields()) {
							Prefs.set("TrackApp.FilterValidatedDialog.startLin", valStartLin);
							Prefs.set("TrackApp.FilterValidatedDialog.minLinLen", valMinLinLen);
							Prefs.set("TrackApp.FilterValidatedDialog.exportMean", doExportMean);
							Prefs.savePreferences();
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * 
	 * Set linegetracker cells and get defaults from config. 
	 * @param cells the cells to set
	 */
	public void setCells(List<Cell> cells) {
		this.cells = cells;
		
		// IJ.Prefs.set adds a ., getInt doesn't. 
		valStartLin = Prefs.getInt(".TrackApp.FilterValidatedDialog.startLin", valStartLin);
		valMinLinLen = Prefs.getInt(".TrackApp.FilterValidatedDialog.minLinLen", valMinLinLen);
		doExportMean = Prefs.getInt(".TrackApp.FilterValidatedDialog.exportMean", doExportMean);
		
		startLin.setText(""+valStartLin);
		minLinLen.setText(""+valMinLinLen);
		if(doExportMean != 0) {
			rdbtnExportMeanIntensity.setSelected(true);
			rdbtnExportMaxIntensity.setSelected(false);
		} else {
			rdbtnExportMeanIntensity.setSelected(false);			
			rdbtnExportMaxIntensity.setSelected(true);
		}
	}
	
	/**
	 * Validate form values
	 */
	public boolean validateFields() {
		boolean valid = true;
		
		try {
			valStartLin = Integer.parseInt(startLin.getText());
			valMinLinLen = Integer.parseInt(minLinLen.getText());
		} catch (NumberFormatException e) {
			IJ.showMessage("Please enter numeric values for the minimum length / frame values.");
			valid = false;
		}
		doExportMean = getRdbtnExportMeanIntensity().isSelected() ? 1 : 0; 
		return valid;
	}
	
	/**
	 * Return true if the current filter accepts the given cell
	 * @param a the (ancestral!) cell
	 * @return true if cell is accepted.
	 */
	public boolean accepts(Cell a) {
		if(!a.isValidated()) {
			return false;
		}
		// 1) make sure the lineage starts early enough
		int start = a.getFrame();
		if(valStartLin > 0 && start > valStartLin) {
			return false;
		}
		
		// 2) find length of lineage
		int end = ExportValidated.findLastFrame(a);

		ArrayList<Cell> dcells = new ArrayList<Cell>();
		HashMap<Integer, String> cellNames = new HashMap<Integer, String>();
		JSONArray divns = new JSONArray();
		ExportValidated.getAllDaughters(a, dcells, cellNames, null, divns);
		
		for( Cell xc : dcells ) {
			int xend = ExportValidated.findLastFrame(xc);
			if( xend > end ) {
				end = xend;
			}
		}
		
		if(end - start + 1 < valMinLinLen) {
			return false;
		}
		return true;
	}
	
	/** 
	 * Make a preview of the number of cells
	 */
	public String makePreview() {
		if (cells == null) {
			return "";
		}
		if(!validateFields()) {
			return "Invalid parameters";
		}
		HashMap<Integer, Boolean> lins = new HashMap<Integer, Boolean>();
		int nlin = 1;
		String pvstr = "";
		for(Cell c : cells) {
			if(!c.isValidated()) {
				continue;
			}
			Cell a = ExportValidated.findFirstAncestor(c);
			
			if(lins.get(new Integer(a.getCellID())) == null) {
				// do it once for every lineage
				lins.put(new Integer(a.getCellID()), new Boolean(true));
				if(accepts(a)) {
					int start = a.getFrame();
					// 2) find last cell of lineage
					int end = ExportValidated.findLastFrame(a);
					String endDesc = a.toString();

					ArrayList<Cell> dcells = new ArrayList<Cell>();
					HashMap<Integer, String> cellNames = new HashMap<Integer, String>();
					JSONArray divns = new JSONArray();
					ExportValidated.getAllDaughters(a, dcells, cellNames, null, divns);
					
					for( Cell xc : dcells ) {
						int xend = ExportValidated.findLastFrame(xc);
						if( xend > end ) {
							end = xend;
							endDesc = xc.toString();
						}
					}
										
					pvstr+= "Lineage " + (nlin++) + " : "
							+ " max-len=" + (end - start + 1) + "frames; "
							+ " first=" + a.toString() + "; "
							+ " last=" + endDesc + "; "
							+ "\n";					
				}
				
			}
		}
		return pvstr;
	}
	protected JRadioButton getRdbtnExportMeanIntensity() {
		return rdbtnExportMeanIntensity;
	}
	protected JRadioButton getRdbtnExportMaxIntensity() {
		return rdbtnExportMaxIntensity;
	}
}
