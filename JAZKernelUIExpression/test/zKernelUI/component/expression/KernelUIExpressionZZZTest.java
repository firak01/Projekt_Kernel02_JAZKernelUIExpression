package zKernelUI.component.expression;

import basic.zBasic.ExceptionZZZ;
import basic.zKernelUI.component.expression.KernelUIExpressionZZZ;
import basic.zKernel.KernelZZZ;
import custom.zKernelUI.module.config.FrameConfigZZZ;
import custom.zKernelUI.module.config.PanelConfigZZZ;
import junit.framework.TestCase;

public class KernelUIExpressionZZZTest extends TestCase{
	FrmExpression4TestZZZ frmExpressionTest;
	KernelUIExpressionZZZ expressionTest;
	
	protected void setUp(){
		try {			

			//### Aufbau des TestFixtures
			//Das Kernel-Objekt PLUS DEM FRAME
			KernelZZZ objKernel = new KernelZZZ("TEST", "01", "", "ZKernelConfigKernelUIExpression_test.ini",(String)null);
			frmExpressionTest = new FrmExpression4TestZZZ(objKernel, "FGL", "01");
						
			//The main object used for testing.
			expressionTest = new KernelUIExpressionZZZ(objKernel, frmExpressionTest);
			
		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		} 
	}//END setup
	
	protected void tearDown(){
		//Den gestarteten Frame wieder schliessen, sonst �ffnet man permanent Fenster
		frmExpressionTest.dispose();
	}
	
	/** void, Test auf Sichtbarkeit und auf den angezeigten Titel des Frames
	* Lindhauer; 05.05.2006 07:49:38
	 */
	public void testFrameExpressionCreation(){	
		try{
			//Frame starten
			frmExpressionTest.launch("FGL expression test");  // Nun muss aber f�r jeden Test das Fenster wieder geschlossen werden.
	
			//Test auf Sichtbarkeit
			assertTrue("The expression frame is not showing.", frmExpressionTest.isShowing());
			
			//Test auf Titel
			assertEquals("FGL expression test", frmExpressionTest.getTitle());
		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		} 
	}
	
	
	/** void, Test auf Sichtbarkeit des Hauptpanels, das in den ContentPane des Frames eingef�gt wurde
	 * - Testet auch GetPanelSub(...)
	* Lindhauer; 05.05.2006 08:44:17
	 */
	public void testPanelContentPaneCreation(){		
		try{				
			//Frame starten. Erst dadarch werden die Panels hinzugef�gt
			frmExpressionTest.launch("FGL expression test");  // Nun muss aber f�r jeden Test das Fenster wieder geschlossen werden.
						
			//Test auf das Panel, das diesem Frame hinzugef�gt wurde
			PanelExpression4TestZZZ panelExpression = (PanelExpression4TestZZZ) frmExpressionTest.getPanelSub("ContentPane");
			assertNotNull("The panel 'ContentPane' of the frame was not found", panelExpression);	
			
			//Test auf Sichtbarkeit
			assertTrue("The 'ContentPane' panel is not showing", panelExpression.isShowing());			
			
		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		} 
	}
	
	public void testExpressionPartCompute(){
		try{
	//		Frame starten. Erst dadarch werden die Panels hinzugef�gt
			frmExpressionTest.launch("FGL expression test");  // Nun muss aber f�r jeden Test das Fenster wieder geschlossen werden.
		
			//ErsterTest: Ohne Konstanten
			String sExpressionWithAliasAll = "ContentPane<./>text1.getText<+/>ContentPane<./>text2.getText"; 
			String sValue = expressionTest.computeExpressionPartString("", sExpressionWithAliasAll);
			assertNotNull("Null value received.", sValue);
			assertEquals("EinsZwei", sValue);
			
			
			//Zweiter Test: Mit Konstanten 
			sExpressionWithAliasAll = "ContentPane<./>text1.getText<+/>' und '<+/>ContentPane<./>text2.getText"; 
			sValue = expressionTest.computeExpressionPartString("", sExpressionWithAliasAll);
			assertNotNull("Null value received.", sValue);
			assertEquals("Eins und Zwei", sValue);
			
		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		} 
	}
	
	public void testExpressionWholeCompute(){
		try{
//			Frame starten. Erst dadarch werden die Panels hinzugef�gt
			frmExpressionTest.launch("FGL expression test");  // Nun muss aber f�r jeden Test das Fenster wieder geschlossen werden.
			
			//A) Bedingung soll wahr sein
			//ErsterTest: Ohne Konstanten
			String sExpressionWithAliasAll = "ContentPane<./>text2.getText<?/>ContentPane<./>text1.getText<+/>ContentPane<./>text2.getText<:/>'nix'"; 
			String sValue = expressionTest.computeString(sExpressionWithAliasAll);
			assertNotNull("Null value received.", sValue);
			assertEquals("EinsZwei", sValue);
			
			
			//Zweiter Test: Mit Konstanten 
			sExpressionWithAliasAll = "ContentPane<./>text2.getText<?/>ContentPane<./>text1.getText<+/>' und '<+/>ContentPane<./>text2.getText<:/>'nix'"; 
			sValue = expressionTest.computeString("", sExpressionWithAliasAll);
			assertNotNull("Null value received.", sValue);
			assertEquals("Eins und Zwei", sValue);
			
			//B) Bedingung soll falsch sein
//			ErsterTest: Ohne Konstanten
			sExpressionWithAliasAll = "ContentPane<./>textEmpty.getText<?/>ContentPane<./>text1.getText<+/>ContentPane<./>text2.getText<:/>'nix'"; 
			sValue = expressionTest.computeString(sExpressionWithAliasAll);
			assertNotNull("Null value received.", sValue);
			assertEquals("nix", sValue);
			
			
			//Zweiter Test: Mit leerer Konstante 
			sExpressionWithAliasAll = "ContentPane<./>textEmpty.getText<?/>ContentPane<./>text1.getText<+/>' und '<+/>ContentPane<./>text2.getText<:/>''"; 
			sValue = expressionTest.computeString("", sExpressionWithAliasAll);
			assertNotNull("Null value received.", sValue);
			assertEquals("", sValue);
			
		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		} 
	}
}
