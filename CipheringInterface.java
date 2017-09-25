import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.filechooser.FileNameExtensionFilter ;
import java.io.* ;

public class CipheringInterface extends JFrame implements ActionListener {
	
	private File toModify ;
	private String input ;
	private String output ;
	private JRadioButton encrypt ;
	private JRadioButton decrypt ;
	private JRadioButton caesar ;
	private JRadioButton vegeniere ;
	
	public static void main (String[] args){
		CipheringInterface CI = new CipheringInterface() ;
		
	}
	
	public CipheringInterface(){
		setLayout(new GridLayout(0,1)) ;
		this.setTitle("(En-De)cryptor") ;
		
		JLabel firstLabel = new JLabel("Select a Process: ") ;
		JPanel first = new JPanel() ;
		first.setBorder(BorderFactory.createLineBorder(Color.black));
		first.setLayout(new GridLayout(0,1)) ;
		encrypt = new JRadioButton("Encryption") ;
		decrypt = new JRadioButton("Decryption") ;
		ButtonGroup firstGroup = new ButtonGroup() ;
		firstGroup.add(encrypt) ;
		firstGroup.add(decrypt) ;
		first.add(firstLabel) ;
		JPanel firstButtonPanel = new JPanel() ;
		firstButtonPanel.add(encrypt) ;
		firstButtonPanel.add(decrypt) ;
		first.add(firstButtonPanel) ;
		add(first,BorderLayout.NORTH) ;
		
		JLabel secondLabel = new JLabel("Select a Technique: ") ;
		JPanel second = new JPanel() ;
		second.setBorder(BorderFactory.createLineBorder(Color.black));
		second.setLayout(new GridLayout(0,1)) ;
		caesar = new JRadioButton("Caesar") ;
		vegeniere = new JRadioButton("Vigenère") ;
		ButtonGroup secondGroup = new ButtonGroup() ;
		secondGroup.add(caesar) ;
		secondGroup.add(vegeniere) ;
		second.add(secondLabel) ;
		JPanel secondButtonPanel = new JPanel() ;
		secondButtonPanel.add(caesar) ;
		secondButtonPanel.add(vegeniere) ;
		second.add(secondButtonPanel) ;
		add(second,BorderLayout.CENTER) ;
		
		
		JPanel third = new JPanel() ;
		third.setBorder(BorderFactory.createLineBorder(Color.black));
		JLabel thirdLabel = new JLabel("Select a .txt file: ") ;
		JButton select = new JButton("Select File*") ;
		select.addActionListener(this) ;
		third.add(thirdLabel) ;
		third.add(select) ;
		add(third) ;
		
		input = new String() ;
		output = new String() ;
		
		pack();
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	
	// Main-engine Modifier Method
	private void modifier(File file) throws IOException, FileNotFoundException{
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file))) ; 
		boolean done = false ;
		String tmp ;
		while (!done){
			tmp=in.readLine();
			if (tmp!=null){
				input += tmp + System.lineSeparator() ;
			} else {
				done = true ;
			}
		}
		in.close() ;
		
		boolean finished = false ;
		while(!finished){
			if(caesar.isSelected()){
				boolean caught = false ;
				int shift = -1 ;
				JPanel panel = new JPanel() ;
				panel.setLayout(new GridLayout(0,1)) ;
				panel.add(new JLabel("Provide a Shifting Integer (1-25) below: ")) ;
				JTextField text = new JTextField() ;
				panel.add(text) ;
				JOptionPane.showMessageDialog(this, panel, "SHIFT INT.", JOptionPane.PLAIN_MESSAGE, null) ;
				String toInt = text.getText() ;
				try{
					if(toInt==null){
						throw new UnsupportedOperationException() ;
					}
					shift = Integer.parseInt(toInt) ;
					if(shift > 25 || shift < 1){
						throw new NumberFormatException();
					} 
				} catch (NumberFormatException exception) {
					JOptionPane.showMessageDialog(this, "Please enter a valid Integer", "ERROR", JOptionPane.PLAIN_MESSAGE, null) ;
					caught = true ;
				} catch (UnsupportedOperationException ex) {
					break ;
				}
				if(!caught){
					try{
						if(encrypt.isSelected()){
							output = Encryption.caesarCipher(input,shift) ;
						} else {
							output = Encryption.caesarDicipher(input,shift) ;
						}
					} catch (UnsupportedOperationException exception) {
						JOptionPane.showMessageDialog(this, "The File provided is empty", "ERROR", JOptionPane.PLAIN_MESSAGE, null) ;
					}
					finished = true ;
				}
				
			} else {
				boolean caught = false ;
				JPanel panel = new JPanel() ;
				panel.setLayout(new GridLayout(0,1)) ;
				panel.add(new JLabel("Provide a Key (cannot contain spaces/numbers/special characters): ")) ;
				JTextField text = new JTextField() ;
				panel.add(text) ;
				JOptionPane.showMessageDialog(this, panel, "KEY", JOptionPane.PLAIN_MESSAGE, null) ;
				String key = text.getText() ;
				try{
					if(encrypt.isSelected()){
						output = Encryption.viginereCipher(input,key) ;
					} else {
						output = Encryption.viginereDicipher(input,key) ;
					}
					finished = true ;
				} catch(UnsupportedOperationException exception){
					String exceptionMessage = exception.getMessage() ;
					if(exceptionMessage.equals("Nothing to be encrypted.") || exceptionMessage.equals("Nothing to be decrypted.")){
						JOptionPane.showMessageDialog(this, "The File provided is empty", "ERROR", JOptionPane.PLAIN_MESSAGE, null) ;
						finished = true ;
					} else {
						JOptionPane.showMessageDialog(this, "Please enter a valid Key", "ERROR", JOptionPane.PLAIN_MESSAGE, null) ;
						finished = false ;
					}
				}
				
			}
		}
		String s = "" ;
		if(encrypt.isSelected()){
			s = "en" ;
		} else {
			s = "de" ;
		}
		
		String fileName = file.getName();
		int pos = fileName.lastIndexOf(".");
		if (pos > 0) {
			fileName = fileName.substring(0, pos);
		}
		
		OutputStreamWriter out = new OutputStreamWriter( new FileOutputStream( fileName+"("+s+"crypted).txt" ));
		out.write(output) ;
		out.close() ;
		
		input = new String() ;
		output = new String() ;
		
	}
	
	public void actionPerformed(ActionEvent event) {
		
		if (event.getActionCommand().equals("Select File*")){
			if((encrypt.isSelected() || decrypt.isSelected()) && (caesar.isSelected() || vegeniere.isSelected()) ){
				final String directory = System.getProperty("user.dir") ;
				JFileChooser chooser = new JFileChooser() ;
				chooser.setCurrentDirectory(new File(directory)) ;
				chooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text")) ;
				chooser.setAcceptAllFileFilterUsed(false) ;
				int returnVal = chooser.showOpenDialog(this) ;
				if (returnVal == JFileChooser.APPROVE_OPTION) {
                    toModify = chooser.getSelectedFile() ;
					try {
						modifier(toModify) ;
					} catch (IOException exception) {
						exception.printStackTrace() ;
					}
						
                } else {
					//pass 
				}
			} else {
				JOptionPane.showMessageDialog(this, "Please make proper Process/Technique selections before choosing file", "Notice", JOptionPane.PLAIN_MESSAGE, null) ;
			}
		}
		toModify = null ;
		
	}
	
	

}
