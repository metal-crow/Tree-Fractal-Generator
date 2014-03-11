import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*Frank C
 * 
 */



public class tree_fractal {

	public static void main(String[] args) {
		//get screen dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		createAndShowGUI((int)screenSize.getHeight(),(int)screenSize.getWidth());
	}
	
	private static void createAndShowGUI(int height, int width) {
        JFrame f = new JFrame("Tree Fractal");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(height,width);
        try {
			f.add(new Pane(height,width));
		} catch (Exception e) {
			e.printStackTrace();
		}
        f.pack();
        f.setVisible(true);
    }

}

@SuppressWarnings("serial")
class Pane extends JPanel {
	private int width;
	private int height;
	private int maxdepth=10;
	private int intitalangle=90;
	private int storedangle;
	private int branches=3;
	private set tree = new set();
	private boolean perfectfractal=true;
	private Random generator = new Random();
	boolean checkboxchangedsliderflag=false;
	private JCheckBox offangle = new JCheckBox("Set optimal fractal angle");//need this to be global because angleslider manually changes it
	private JSlider angleslider = new JSlider(JSlider.HORIZONTAL,0,90,90);//need global because offangle manually changes it
	
	public Pane(final int height, final int width) throws Exception{
		//this.setLayout(new BoxLayout(this,3)); I wanted to organize the GUI into a neat format, but i a) ran out of time and b)have to learn this layout thing
		this.width=width;
		this.height=height;
		
		//depth of recursion
		//create a slider that, when changed, alters the global variable maxdepth and calls the createfractal function.
		JSlider depthslider = new JSlider(JSlider.HORIZONTAL,2, 12, 10);
		JLabel labeldepthslider = new JLabel("Depth of recursion");
		this.add(labeldepthslider);
		this.add(depthslider, BorderLayout.SOUTH);
		
		depthslider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
			        maxdepth = (int)source.getValue();
			        try {
			        	createfractial();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			    }
			}
		});
		depthslider.setMajorTickSpacing(1);
		depthslider.setPaintTicks(true);
		depthslider.setPaintLabels(true);
		
		//angle of branches, slider which changes intitalangle variable, then calls createfractial
		JLabel labelangleslider = new JLabel("Angle of branches");
		this.add(labelangleslider);
		this.add(angleslider, BorderLayout.NORTH);
		angleslider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					intitalangle = (int)source.getValue();
					if(!checkboxchangedsliderflag){//this flag is used so that when the offangle checkbox changes the slider value by calling this function, it doesnt uncheck the box 
						offangle.setSelected(false);}
					checkboxchangedsliderflag=false;//except for the prevous flag, always uncheck the offangle checkbox when slider altered because we are no longer at the optimal angle
					try {
						createfractial();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			    }
			}
		});
		angleslider.setMajorTickSpacing(10);
		angleslider.setPaintTicks(true);
		angleslider.setPaintLabels(true);
		
		//number of branches, user enters number of branches, then calls createfractial
		JTextField numbranches = new JTextField(2);
		numbranches.setText(Integer.toString(branches));
		JLabel labelnumbranches = new JLabel("Number of branches");
		this.add(labelnumbranches);
		this.add(numbranches);
		numbranches.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
            {
				JTextField source = (JTextField)e.getSource();
				if(source.getText().matches("-?[0-9]+")){//if string is only a number
					branches=Integer.parseInt(source.getText());
					offangle.setSelected(false);
					try {
						createfractial();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
            }
		});
		
		//in case user doesn't want a fractal that looks like \ / but wants _\ /_
		//													  /|\			  |
		this.add(offangle, BorderLayout.NORTH);
		offangle.setSelected(false);
		offangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
            {
				JCheckBox source = (JCheckBox)e.getSource();
				if(source.isSelected()){//when box is checked, change intitalangle to the optimal value, call createfractial
					storedangle=intitalangle;//store the old angle value in a safe variable to restore if box unchecked
					if((branches%2)!=0){
						intitalangle=180/(branches-1);//angle generated to ensure even distribution above the horizontal above the starting trunk
					}
					else if((branches%2)==0){
						intitalangle=180/(branches);//this fixes dynamic angle not working/behaving incorrectly for even # branches
					}
				}
				else{//when the box is unchecked, restore old angle value
					intitalangle=storedangle;
				}
				checkboxchangedsliderflag=true;
				angleslider.setValue(intitalangle);//set the angle change slider value to optimal angle generated
				try {
					createfractial();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
		});
		
		//if user wants randomness or not
		JCheckBox randomness = new JCheckBox("Enable/Disable randomness");
		this.add(randomness, BorderLayout.NORTH);
		randomness.setSelected(false);
		randomness.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
            {
				JCheckBox source = (JCheckBox)e.getSource();
				if(source.isSelected()){
					perfectfractal=false;
				}
				else{
					perfectfractal=true;
				}
				try {
					createfractial();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
		});
		
		createfractial();//Initially called here because this constructor is only called once, at creation, so this function has to be called to have
		//the fractal ready on window load. this isnt called again because the constructor isnt called again.
	}
	
	public Dimension getPreferredSize() {
        return new Dimension(width,height);
    }
	
	//everything the fractal need to appear on screen
	public void createfractial() throws Exception{
		tree = new set();//wipe the current stored fractal (if any)
		tree.add(new int[]{width/2,height+100,width/2,height/2+100,width/2,height/2,0});//create base trunk of tree (the +100 is to add space at top of screen for sliders)
		recusecreatetree(1,branches,tree,intitalangle);//Recursive method to create fractal
		repaint();//draw fractal on screen
	}
	
	
	//real meat of program, algorithm with generates fractal
	public void recusecreatetree(int curdepth,int branches, set currentdepthobject,int angle) throws Exception{
		if(curdepth!=maxdepth){
			int i=-(branches/2);
			int t=branches/2;

			while(i<=t){
				set branch = new set (1);
				int[] bottonandtopxy = new int[7];
				bottonandtopxy[0]=((int[])currentdepthobject.get(0))[2];
				bottonandtopxy[1]=((int[])currentdepthobject.get(0))[3];
				
				//x change
				bottonandtopxy[4]=((int[])currentdepthobject.get(0))[4]/2;		//using sin for x because it works with the math i dunno
				bottonandtopxy[2] = (int)(bottonandtopxy[0] + bottonandtopxy[4] * Math.sin(Math.toRadians((i*angle) + ((int[])currentdepthobject.get(0))[6])));
				if(!perfectfractal){
					int amount=(int)(bottonandtopxy[4]*0.15*2);//the 0.15 here determine max percent of total to potently add or subtract
					if(amount==0){//in case amount truncates to zero(which random can't use), make it 1
						amount=1;}
					bottonandtopxy[2]+=generator.nextInt(amount)-amount/2;//randomly add plus/minus X% of width change
				}
				
				//y change
				bottonandtopxy[5]=((int[])currentdepthobject.get(0))[5]/2;
				bottonandtopxy[3] = (int)(bottonandtopxy[1] - bottonandtopxy[5] * Math.cos(Math.toRadians((i*angle) + ((int[])currentdepthobject.get(0))[6])));
				if(!perfectfractal){
					int amount=(int)(bottonandtopxy[5]*0.15*2);
					if(amount==0){
						amount=1;}
					bottonandtopxy[3]+=generator.nextInt(amount)-amount/2;
				}
				
				bottonandtopxy[6]=((int[])currentdepthobject.get(0))[6]+i*angle;
				
				
				branch.add(bottonandtopxy);
				//recusivly add next depth branch to this depth branch
				recusecreatetree(curdepth+1,branches,(set)branch,angle);
				
				//add branch to tree
				currentdepthobject.add(branch);
				
				i++;
				if((branches%2)==0 && i==0){//if an even # of branches, increment skips 0 EX: if #=2, increment, -1,1
					i++;
				}
			}
		}
	}
	
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
			paintcompmethod(g,tree);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    //go though set containing points for the fractal, draw it
    public void paintcompmethod(Graphics g, Object currentdepthobject) throws Exception{
    	//for given object(a set)
    	if(currentdepthobject instanceof set){
    		//go though all elements in set
	    	for(int i=0;i<((set)currentdepthobject).size();i++){
	    		//get the int[] value that stores positions
	    		if(((set)currentdepthobject).get(i) instanceof int[]){
	    			int[] bottonandtopxy = (int[]) ((set)currentdepthobject).get(i);
	    			g.drawLine(bottonandtopxy[0], bottonandtopxy[1], bottonandtopxy[2], bottonandtopxy[3]);
	    		}
	    		//if the next value is a set
	    		else{
	    			//recurse with that set as the object set
	    			paintcompmethod(g,((set)currentdepthobject).get(i));
	    		}
	    	}
    	}
    }
}
