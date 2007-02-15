//import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
//import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.TextField;
import java.awt.TextArea;
import java.awt.Panel;
import java.awt.BorderLayout;
import java.awt.Label;
//import java.awt.Component;
import java.awt.Frame;
import java.awt.Button;


public class SeqInputDialog extends Frame
{
    

    String seq;
    String name;
    TextField nameField;
    TextArea seqArea;
    InputPanel inputPanel;
    
    public SeqInputDialog (InputPanel ip)
    {
		super ("Please type or paste a sequence");
	
		inputPanel = ip;
		
		setLayout (new BorderLayout ());
	
		Panel northPanel = new Panel ();
		Label nameLabel = new Label ("Name (optional): ");
		northPanel.add (nameLabel);
		nameField = new TextField (10);
		northPanel.add (nameField);
		add ("North", northPanel);
	
		Panel centerPanel = new Panel ();
		centerPanel.setLayout (new BorderLayout ());
		Label seqLabel = new Label ("Please type your sequence below (mandatory):");
		centerPanel.add ("North", seqLabel);
		seqArea = new TextArea (5, 40);
		centerPanel.add ("Center", seqArea);
		add ("Center", centerPanel);
		
		Panel southPanel = new Panel ();
		Button okButton = new Button ("Ok");
		okButton.addActionListener (new OkButtonHandler ());
		southPanel.add(okButton);
		Button resetButton = new Button ("Reset");
		resetButton.addActionListener (new ResetButtonHandler ());
		southPanel.add(resetButton);
		Button cancelButton = new Button ("Cancel");
		southPanel.add (cancelButton);
		cancelButton.addActionListener (new CancelButtonHandler ());
		add("South", southPanel);
	
		addWindowListener (new WindowCloseHandler ());
		
		setSize (400, 300);
		setVisible (true);
		/*modification by Olivier*/
		//show();
		/*end modification*/
    }

    private class OkButtonHandler implements ActionListener
    {
		public void actionPerformed (ActionEvent e)
		{
		    StringBuffer tmp = new StringBuffer ();
		    String rawSeq = seqArea.getText ();
	
		    for (int i = 0; i < rawSeq.length (); i++)
			if ((rawSeq.charAt (i) >= 'A' && rawSeq.charAt (i) <= 'Z') ||
			    (rawSeq.charAt (i) >= 'a' && rawSeq.charAt (i) <= 'z'))
				tmp.append (Character.toUpperCase (rawSeq.charAt (i)));
					
				
		    inputPanel.addSequence (nameField.getText (), tmp.toString ());
		    
		    /*modificaiton by Olivier*/
			inputPanel.setDialog(null);
			/*end modification*/
		    
		    dispose ();
		}
    }

    private class ResetButtonHandler implements ActionListener
    {
		public void actionPerformed (ActionEvent e)
		{
		    nameField.setText ("");
		    seqArea.setText ("");
		}
    }

    private class CancelButtonHandler implements ActionListener
    {
		public void actionPerformed (ActionEvent e)
		{
			/*modificaiton by Olivier*/
			inputPanel.setDialog(null);
			/*end modification*/
		    dispose ();
		}
    }

    private class WindowCloseHandler extends WindowAdapter
    {
		public void windowClosing (WindowEvent e)
		{
			/*modificaiton by Olivier*/
			inputPanel.setDialog(null);
			/*end modification*/
		    dispose ();
		}
    }
}
