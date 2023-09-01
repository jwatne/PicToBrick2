package pictobrick.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.*;
import java.util.*;


/**
 * class:          ProgressBarsAlgorithms
 * layer:          Gui (three tier architecture)
 * description:    shows 4 progress bars
 * @author         Adrian Schuetz
 */
public class ProgressBarsAlgorithms
extends JDialog
{
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	JProgressBar quantisation;
	JProgressBar tiling;
	JProgressBar statistic;
	JProgressBar paint;

	/**
	 * method:          ProgressBarsAlgorithms
	 * description:     constructor
	 * @author          Adrian Schuetz
	 * @param           owner
	 */
	public ProgressBarsAlgorithms(Frame owner)
	{
		super(owner,textbundle.getString("dialog_progressBarsAlgorithms_frame"),false);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setLocation(100,100);
		this.setResizable(false);
		JPanel content = new JPanel(new GridLayout(4,1));
		JPanel quantisationPanel = new JPanel(new GridLayout(1,1));
		quantisation = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		quantisation.setStringPainted(true);
		quantisation.setValue(0);
		quantisation.setForeground(new Color(0,0,150));
		Dimension d = new Dimension(300,25);
		quantisation.setMaximumSize(d);
		quantisation.setPreferredSize(d);
		quantisation.setMinimumSize(d);
		TitledBorder quantisationBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_progressBarsAlgorithms_border_1"));
		quantisationPanel.setBorder(quantisationBorder);
		quantisationBorder.setTitleColor(new Color(100,100,100));
		quantisationPanel.add(quantisation);
		JPanel tilingPanel = new JPanel(new GridLayout(1,1));
		tiling = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		tiling.setStringPainted(true);
		tiling.setValue(0);
		tiling.setForeground(new Color(0,0,150));
		tiling.setMaximumSize(d);
		tiling.setPreferredSize(d);
		tiling.setMinimumSize(d);
		TitledBorder tilingBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_progressBarsAlgorithms_border_2"));
		tilingPanel.setBorder(tilingBorder);
		tilingBorder.setTitleColor(new Color(100,100,100));
		tilingPanel.add(tiling);
		JPanel statisticPanel = new JPanel(new GridLayout(1,1));
		statistic = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		statistic.setStringPainted(true);
		statistic.setValue(0);
		statistic.setForeground(new Color(0,0,150));
		statistic.setMaximumSize(d);
		statistic.setPreferredSize(d);
		statistic.setMinimumSize(d);
		TitledBorder statisticBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_progressBarsAlgorithms_border_3"));
		statisticPanel.setBorder(statisticBorder);
		statisticBorder.setTitleColor(new Color(100,100,100));
		statisticPanel.add(statistic);
		JPanel paintPanel = new JPanel(new GridLayout(1,1));
		paint = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		paint.setStringPainted(true);
		paint.setValue(0);
		paint.setForeground(new Color(0,0,150));
		paint.setMaximumSize(d);
		paint.setPreferredSize(d);
		paint.setMinimumSize(d);
		TitledBorder paintBorder = BorderFactory.createTitledBorder(textbundle.getString("dialog_progressBarsAlgorithms_border_4"));
		paintPanel.setBorder(paintBorder);
		paintBorder.setTitleColor(new Color(100,100,100));
		paintPanel.add(paint);
		content.add(quantisationPanel);
		content.add(tilingPanel);
		content.add(statisticPanel);
		content.add(paintPanel);
		this.getContentPane().add(content);
		this.pack();
		this.setModal(false);
	}

	/**
	 * method:          setStatus
	 * description:     sets the status of the progress bars (enabled/disabled)
	 * @author          Adrian Schuetz
	 * @param           active (false = disabled, true = enabled)
	 */
	public void setStatus(boolean active){
		if (!active){
			this.statistic.setString(textbundle.getString("dialog_progressBarsAlgorithms_progressBar"));
		}else{
			this.statistic.setString(null);
		}
	}

	/**
	 * method:          showValue
	 * description:     change the progress bar value
	 * @author          Adrian Schuetz
	 * @param           value
	 * @param           number = 1:quantisation, 2:tiling, 3: paint, 4: statistic
	 */
	public void showValue(int value, int number){
		if (number==1){
			quantisation.setValue(value);
		}else if (number==2){
			tiling.setValue(value);
		}else if (number==3){
			paint.setValue(value);
		}else if (number==4){
			statistic.setValue(value);
		}
	}

	/**
	 * method:          hideDialog
	 * description:     hides the dialog
	 * @author          Adrian Schuetz
	 */
	public void hideDialog(){
		this.setVisible(false);
	}

	/**
	 * method:          showDialog
	 * description:     shows the dialog
	 * @author          Adrian Schuetz
	 */
	public void showDialog(){
		this.setLocation(100,100);
		this.setVisible(true);
	}

}
