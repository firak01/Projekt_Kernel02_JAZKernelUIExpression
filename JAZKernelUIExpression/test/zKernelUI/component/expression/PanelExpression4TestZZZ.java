package zKernelUI.component.expression;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;

import basic.zBasic.ExceptionZZZ;
import basic.zKernelUI.component.KernelJPanelCascadedZZZ;
import basic.zKernel.IKernelZZZ;
import basic.zKernel.KernelZZZ;


public class PanelExpression4TestZZZ extends KernelJPanelCascadedZZZ{
		public PanelExpression4TestZZZ(IKernelZZZ objKernel, JFrame frameParent) throws ExceptionZZZ{
			super(objKernel, frameParent);
	       		
			//### Layout Manager
			this.setLayout(new BorderLayout());
					
		  //### Nun die Komponenten hier einsetzen und unter einem Aliasnamen speichern
		  JTextField text1 = new JTextField("Eins");
		  this.setComponent("text1", text1);
		  this.add(BorderLayout.NORTH, text1);
		  
		  JTextField text2 = new JTextField("Zwei");
		  this.setComponent("text2", text2);
		  this.add(BorderLayout.CENTER, text2);
		  
		  JTextField textEmpty = new JTextField("");
		  this.setComponent("textEmpty", textEmpty);
		  this.add(BorderLayout.SOUTH, textEmpty);
		
		}
	}//END Class
