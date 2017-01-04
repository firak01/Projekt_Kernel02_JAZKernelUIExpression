package zKernelUI.component.expression;

import javax.swing.JComponent;

import basic.zBasic.ExceptionZZZ;
import basic.zKernelUI.component.KernelJFrameCascadedZZZ;
import basic.zKernelUI.component.KernelJPanelCascadedZZZ;
import basic.zKernelUI.util.JFrameHelperZZZ;
import basic.zKernel.KernelZZZ;
import custom.zKernelUI.module.config.PanelConfigZZZ;

public class FrmExpression4TestZZZ extends KernelJFrameCascadedZZZ{
	
	public FrmExpression4TestZZZ(KernelZZZ objKernelConfig, String sApplication2Config, String sSystemNumber2Config) throws ExceptionZZZ{
		super(objKernelConfig); //Es gibt keinen ParentFrame
		
		KernelZZZ objKernelChoosen = new KernelZZZ("TEST", "01", "", "ZKernelConfigKernelUIExpression_test.ini", (String)null);
		setTitle("setTitel() done in constructor");

	}
	public boolean launchCustom(){
		return true;
		
		/* !!!! NICHT L�SCHEN: WICHTIGER KOMMENTAR:       .... das wird nun alles in KernelJFrameCascaded gemacht
		main:
		{
	    //Aus Notes heraus gestartet funktioniert EXIT_ON_CLOSE nicht
		//setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			
		//Die Gr�sse des Frames, die Methode wird vom KernelFrame zur Verf�gung gestellt 
		setTitle(this.getKernelObject().getApplicationKey() + " module configuration");
		
		//Grundfl�che in den Rahmen hinzuf�gen...
		//	... das wird nun �ber das ContentPane der Frames gemacht
		PanelConfigZZZ objPanel = new PanelConfigZZZ(this.getKernelObject(), this, this.getKernelConfigObject());
		this.getContentPane().add(objPanel);
		this.setPanelSub("ContentPane", objPanel);
		
		
		//... sichtbar machen erst, nachdem alle Elemente im Frame hinzugef�gt wurden !!!
		//depreciated in 1.5 frame.show();
		//statt dessen...
		setVisible(true);
		bReturn = true;
		
		}//END main:
			*/
	}

	public KernelJPanelCascadedZZZ getPaneContent() throws ExceptionZZZ {
		PanelExpression4TestZZZ objPanel = new PanelExpression4TestZZZ(this.getKernelObject(), this);
		this.setPanelContent(objPanel);
		return objPanel;
	}
	public JComponent getPaneContent(String sAlias) throws ExceptionZZZ {
		// TODO Auto-generated method stub
		//Hier wird nix in einen anderen Pane als den ContentPane gestellt.
		return null;
	}
	public boolean setSizeDefault() throws ExceptionZZZ {
		JFrameHelperZZZ.setSizeDefault(this);
		return true;
	} 
	}//END class