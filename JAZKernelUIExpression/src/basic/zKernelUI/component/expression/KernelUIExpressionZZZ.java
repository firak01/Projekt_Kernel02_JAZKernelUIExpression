package basic.zKernelUI.component.expression;

import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;

import basic.zKernel.KernelZZZ;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.log.ReportLogZZZ;
import basic.zKernel.KernelUseObjectZZZ;
import basic.zKernelUI.component.KernelJFrameCascadedZZZ;
import basic.zKernelUI.component.KernelJPanelCascadedZZZ;

/**Diese Klasse dient zum "Rechnen" mit Komponenten.
 *  Es wird dabei von den Aliaswerten ausgegangen, die in dem "KernelJPanelCascadedZZZ" hinterlegt sind.
 *  So soll dann folgender Ausdruck aufgelöst werden:
 *  		"textLastName?textFirstName + ' ' + textLastName : "" 
 *  
 *  ==> 
 *  Was dann als ein Stringwert herauskommt ist der Wert von den Feldern mit dem Alias 'textLastName' und 'textFirstName',
 *  aber nur wenn in 'textLastName' ein anderer Wert als der Leerwert drin steht.
 *         
 * @author lindhaueradmin
 *
 */
public class KernelUIExpressionZZZ extends KernelUseObjectZZZ{
	JexlContext jc = null; 
	KernelJFrameCascadedZZZ frameParent = null;
	KernelJPanelCascadedZZZ panelDefault = null;
	String sFormulaCurrent = null;
	String sFormulaPartCurrent = null;
	
	public KernelUIExpressionZZZ() throws ExceptionZZZ{				
		String[] saFlag = {"init"};
		KernelExpressionUINew_(null, null, saFlag);
	}	
	public KernelUIExpressionZZZ(KernelZZZ objKernel, KernelJFrameCascadedZZZ frameParent, String[] saFlag) throws ExceptionZZZ{
		super(objKernel);
		KernelExpressionUINew_(frameParent, null, null);
	}
	public KernelUIExpressionZZZ(KernelZZZ objKernel, KernelJFrameCascadedZZZ frameParent) throws ExceptionZZZ{
		super(objKernel);
		KernelExpressionUINew_(frameParent, null, null);
	}
	public KernelUIExpressionZZZ(KernelZZZ objKernel, KernelJFrameCascadedZZZ frameParent, KernelJPanelCascadedZZZ panelDefault) throws ExceptionZZZ{
		super(objKernel);
		KernelExpressionUINew_(frameParent, panelDefault, null);
	}
	
	
	private void KernelExpressionUINew_(KernelJFrameCascadedZZZ frameParent, KernelJPanelCascadedZZZ panelDefault, String[] saFlag) throws ExceptionZZZ{
		main:{	
			 //setzen der übergebenen Flags	
			  if(saFlag != null){
				  for(int iCount = 0;iCount<=saFlag.length-1;iCount++){
					  String stemp = saFlag[iCount];
					  boolean btemp = setFlag(stemp, true);
					  if(btemp==false){						
						  ExceptionZZZ ez = new ExceptionZZZ("the flag '" + stemp + "' is not available.", iERROR_PARAMETER_VALUE, this,  ReflectCodeZZZ.getMethodCurrentName()); 
						  throw ez;		 
					  }
				  }
				  if(this.getFlag("INIT")==true)	break main; 	
			  }
			  
			  //Parameter checken
			  if(frameParent==null){
				  ExceptionZZZ ez = new ExceptionZZZ("FrameParent", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
				  throw ez;
			  }
			  
			  //Setzen des PanelParents, in dem die FeldAliassse gemappt sind
			  this.setFrameParent(frameParent);
			  
			  if(panelDefault != null){
				  this.setPanelDefault(panelDefault);
			  }
			  
			  
			  //Vereinfacht jetzt den benötigten JexlContext erstellen
			  this.jc = JexlHelper.createContext();	
			 	
		}//end main:
	}
	
	/**Berechnet den Ausdruck. In einem Ausdruck muss immer er PanelAlias stehen oder das Default Panel muß im Constructor übergeben worden sein !!!
	 *  Merke: Bei Verwendung dieser Methode gibt es auch keinen Alternativen DEFAUTL PANEL ALIAS
	 *  
	 * Beispiel für einen Ausdruck, der hiermit berechnet werden soll und aus einem Bedingungsteil und einem Ergebnisteil besteht
	 * 
	 *         myPanel1<./>textLastName.getText()<?/>myPanel1<./>textFirstName.getText<+/>' '<+/>myPanel1<./>textLastName.getText<:/>''
	 *         
	 *         Also: Falls der Nachname in dem entsprechenden Feld eingegeben ist,
	 *                 Wird eine Berechnung zurückgegeben Vorname und Nachname.
	 *                 Ansonsten nur ein Leerstring.
	 *         
	 *         //MERKE: Eine Verschachtelung der Ausdrücke ist momentan nicht erlaubt.
	 *		   //MERKE: Eine Formel im Bedingungsteil ist momentan nicht erlaubt. Es kann nur auf gefüllt/nicht gefüllt geprüft werden.
	 *		   //MERKE: Es ist nicht möglich Parameter in den 'konfigurierten Methoden der Komponenten' zu übergeben.
	 *        //            Daher sind Klammern hinter den Methoden nicht erlaubt
	 *        //MERKE:  Lediglich PLUS ist im Ergebnisteil erlaubt.
	 *         
	 *         
	* @param sPanelAliasDefault
	* @param sExpressionRaw
	* @return
	* @throws ExceptionZZZ
	* 
	* lindhaueradmin; 16.03.2007 12:22:21
	 */
	public String computeString(String sExpression) throws ExceptionZZZ{
		return this.computeString(null, sExpression);
	}
	
	/**Berechnet den Ausdruck. In einem Ausdruck muss immer er PanelAlias stehen. Falls nicht, wird hier ein Default-Alias angegeben.
	 *  
	 * Beispiel für einen Ausdruck, der hiermit berechnet werden soll und aus einem Bedingungsteil und einem Ergebnisteil besteht
	 * 
	 *         myPanel1<./>textLastName.getText()<?/>myPanel1<./>textFirstName.getText<+/>' '<+/>myPanel1<./>textLastName.getText<:/>''
	 *         
	 *         Also: Falls der Nachname in dem entsprechenden Feld eingegeben ist,
	 *                 Wird eine Berechnung zurückgegeben Vorname und Nachname.
	 *                 Ansonsten nur ein Leerstring.
	 *         
	 *         //MERKE: Eine Verschachtelung der Ausdrücke ist momentan nicht erlaubt.
	 *		   //MERKE: Eine Formel im Bedingungsteil ist momentan nicht erlaubt. Es kann nur auf gefüllt/nicht gefüllt geprüft werden.
	 *		   //MERKE: Es ist nicht möglich Parameter in den 'konfigurierten Methoden der Komponenten' zu übergeben.
	 *        //            Daher sind Klammern hinter den Methoden nicht erlaubt
	 *        //MERKE:  Lediglich PLUS ist im Ergebnisteil erlaubt.
	 *         
	 *         
	* @param sPanelAliasDefault
	* @param sExpressionRaw
	* @return
	* @throws ExceptionZZZ
	* 
	* lindhaueradmin; 16.03.2007 12:22:21
	 */
	public String computeString(String sPanelAliasDefault, String sExpressionRaw) throws ExceptionZZZ{
		String sReturn = null;
		main:{
			if(StringZZZ.isEmpty(sExpressionRaw)){
				ExceptionZZZ ez = new ExceptionZZZ("Expression String", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			this.setFormulaCurrent(sExpressionRaw);
			
			//#####################
			System.out.println(ReflectCodeZZZ.getMethodCurrentName() + "# processing raw-formula: " + sExpressionRaw);			
			//JexlContext jc = this.getJexlContextCurrent(); 	
		
			boolean bConditionFullfilled = false;
			//String sFormulaFilled = "";
			
			//+++++++++++++++   0. Zerlegen in Bedingungsteil und Ausdruck  +++++++++++++++++++++++
			//TODO: Die ganzen Konstanten "Operanden" als static Variablen definieren	
			//Merke: Tokenizer betrachtet nur jeweils eines der angegebenen Zeichen	StringTokenizer tokenizerpre = new StringTokenizer(sExpressionRaw, "<?/>", false);
			String[] saCondition = StringZZZ.explode(sExpressionRaw, "<?/>");
			if(saCondition.length >= 3){  
				ExceptionZZZ ez = new ExceptionZZZ("Error Processing Formula: There are more than one condition in this formula (separated with '<?/>') [" + sExpressionRaw + "]. Using '<?/>' in a constant string is not allowed !!!", iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
					
			//### BEDINGUNGSTEIL ###############################################################
			String sFormulaFormulaRaw = null;
			if(saCondition.length>=2){			
				sFormulaFormulaRaw = saCondition[1];			
				String sFormulaCondition = saCondition[0];
				String sValue = this.computeExpressionPartString(sPanelAliasDefault, sFormulaCondition);											
				if(sValue.equals("")){
					bConditionFullfilled = false;
				}else{
					bConditionFullfilled = true;
				}
			}else{
				//Es gibt keine Formel für den Bedingungsteil
				sFormulaFormulaRaw = saCondition[0];
				bConditionFullfilled = true;
			}
				
//			### ERGEBNISTEIL ####################################################################
			if(StringZZZ.isEmpty(sFormulaFormulaRaw)) break main;
			String[] saFormulaFormula = StringZZZ.explode(sFormulaFormulaRaw, "<:/>");
			String sFormulaFormula = null;
			if(bConditionFullfilled == false){
				sFormulaFormula = saFormulaFormula[1];
			}else{
				sFormulaFormula = saFormulaFormula[0];
			}
								
			sReturn = this.computeExpressionPartString(sPanelAliasDefault, sFormulaFormula);
		}//END main:
		return sReturn;
	}
	
	
	public String computeExpressionPartString(String sPanelAliasDefault, String sExpressionPartRaw) throws ExceptionZZZ{
		String sReturn = null;
		main:{
			if(StringZZZ.isEmpty(sExpressionPartRaw)){
				ExceptionZZZ ez = new ExceptionZZZ("ExpressionString", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			this.setFormulaPartCurrent(sExpressionPartRaw);
			
			
//			Test auf das Panel, das diesem Frame hinzugefügt wurde
			KernelJFrameCascadedZZZ frmExpressionTest = this.getFrameParent();
			KernelJPanelCascadedZZZ panelRoot = frmExpressionTest.getPaneContent();
			
			
			String sFormulaFilled=null;
			
//			1. Tokenizer mit den Aliasnamen erstellen. Trennzeichen "<+/>"
			//NEIN, der Tokenizer betrachtet nur einzelne Charkter-Zeichen als Delimiter    StringTokenizer tokenizer = new StringTokenizer(sExpressionPartRaw, "<+/>", false); //Der Delimiter selber soll nicht zurückgegeben werden
			String[] saExpressionPart = StringZZZ.explode(sExpressionPartRaw, "<+/>");
						
			
//			2. In einer Schleife die Werte der Aliasnamen durchgehen. 
			//        2a. Falls der Aliasname nicht existiert, wird der String komplett als konstante gesehen und übernommen
			//        2b. Falls der Aliasname existiert, wird der Komponentenname geholt,
			for(int icountPart=0; icountPart<=saExpressionPart.length-1; icountPart++){				
				String sExpressionPart = saExpressionPart[icountPart];
				
//				++++++++    3. diesen Ausdruck ggf. noch in einen PanelAlias und einen FeldAlias zerlegen
				//Merke: Die Methoden einer Komponente müssen auch angegeben werden und werden nur durch einfachen Punkt '.' hinter den FeldAlias gehängt.
				KernelJPanelCascadedZZZ panelCascaded = null;
				String[] saPanelPart = StringZZZ.explode(sExpressionPart, "<./>");
				
				String sPanel=null;
				String sObject=null;
				if(saPanelPart.length>=3){
					ExceptionZZZ ez = new ExceptionZZZ("Error Processing Formula: There are more than one panel-seperators in the Expression: [" + sExpressionPart + "] of the formula: ["+ this.getFormulaCurrent() + "], (separated with '<./>')", iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}else	if(saPanelPart.length==2){
					//FALL: IM AUSDRUCK WURDE EIN KONKRETER PANELNAME ANGEGEBEN
					sPanel  = saPanelPart[0];
					if(StringZZZ.isEmpty(sPanel)){
						ExceptionZZZ ez = new ExceptionZZZ("Error Processing Formula: There is no panelalias provided in the field: [" + sExpressionPart + "] of the formula: ["+ this.getFormulaCurrent() + "], (separated with '<./>') !!!", iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
					sObject = saPanelPart[1];
					
					
//					+++++++++++++++++++ 4a. Suchen der durch die Aliasse bestimmten Komponenten/Panels ++++++++++++++++++++++
					if(!StringZZZ.isEmpty(sPanel)){
						//2a. Das Panel holen. Dabei stellt sich das Problem, dass nach dem Panel gesucht werden muss					
						panelCascaded = panelRoot.searchPanelSub(sPanel);
						if(panelCascaded==null){
							ExceptionZZZ ez = new ExceptionZZZ("Error Processing Formula: The panel defined in the field: [" + sExpressionPartRaw + "] of the formula: ["+ this.getFormulaCurrent() + "], (separated with '<./>') IS NOT AVAILABLE !!!", iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
							throw ez;
						}
					}
				}else{
					//FALL: IM AUSDRUCK WURDE KEIN PANELNAME ANGEGEBEN. ERGO AUF EIN DEFAULT-PANEL ZURÜCKGREIFEN
					sObject = saPanelPart[0];
					
					
					if(!(sObject.endsWith("'") && sObject.startsWith("'"))){
						//FALL: KEIN KONSTANTER STRING
//						Hier das Panel anhand des defaultPanelAliasses suchen.
						if(StringZZZ.isEmpty(sPanelAliasDefault) && this.getPanelDefault()==null){
							ExceptionZZZ ez = new ExceptionZZZ("Error Processing Formula: There is no panel in the field: [" + sExpressionPart + "] of the formula: ["+ this.getFormulaCurrent() + "], (separated with '<./>') AND A DEFAULT PANEL IS NOT DEFINED !!!", iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
							throw ez;
							
						}else if(StringZZZ.isEmpty(sPanelAliasDefault) && this.getPanelDefault()!=null){
							panelCascaded = this.getPanelDefault();
							
						}else if(! StringZZZ.isEmpty(sPanelAliasDefault) && this.getPanelDefault() == null){
							sPanel = sPanelAliasDefault;
							panelCascaded = panelRoot.searchPanelSub(sPanel);
							if(panelCascaded==null){
								ExceptionZZZ ez = new ExceptionZZZ("Error Processing Formula: The panel defined in the field: [" + sExpressionPartRaw + "] of the formula: ["+ this.getFormulaCurrent() + "], (separated with '<./>') IS NOT AVAILABLE !!!", iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
								throw ez;
							}
						}
					}else if(!sObject.endsWith("'")){
						ExceptionZZZ ez = new ExceptionZZZ("Error Processing Formula: There is the starting ' missing: [" + sExpressionPart + "] of the formula: ["+ this.getFormulaCurrent() + "], (separated with '<./>') AND A DEFAULT PANEL IS NOT DEFINED !!!", iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}else if(!sObject.endsWith("'")){
						ExceptionZZZ ez = new ExceptionZZZ("Error Processing Formula: There is the closing ' missing: [" + sExpressionPart + "] of the formula: ["+ this.getFormulaCurrent() + "], (separated with '<./>') AND A DEFAULT PANEL IS NOT DEFINED !!!", iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
				}
				

				
					//++++++++++++++++++ 4b. Den Objekt Teil noch in Objekt und Methode zerlegen				
					if(!StringZZZ.isEmpty(sObject)  && panelCascaded != null){
						StringTokenizer tokenizermethod = new StringTokenizer(sObject, ".", false);
						//TODO: Merke: ggf. sind mehrere Methoden, die aufeinander folgen möglich. Diese Aufsummierung fehlt noch.
						
						String sAliasComponent = tokenizermethod.nextToken();
						//4a. Die Komponente holen und JEXL verfügbar machen
						//???Object objComponent = panelCascaded.getComponent(sAliasComponent);
						JComponent objComponent = panelCascaded.getComponent(sAliasComponent);
						//JTextField objComponent = (JTextField) panelCascaded.getComponent(sAliasComponent);
						//System.out.println("getName()=" + objComponent.getName());
						//System.out.println("getText()=" + objComponent.getText());
						if(objComponent==null){
							ExceptionZZZ ez = new ExceptionZZZ("Error Processing Formula: The alias '" + sAliasComponent +"' contained in the formula is not mapped in the panel '" + sPanel + "]'");
							throw ez;
						}
						jc.getVars().put(sAliasComponent, objComponent);
						
						
						
	//					4ba. Die Formel erstellen, die dann durch JEXL aufgelöst werden kann
						String sMethodComponent = tokenizermethod.nextToken();
						if(StringZZZ.isEmpty(sFormulaFilled)){
							sFormulaFilled = sAliasComponent + "." + sMethodComponent + "()";
						}else{
							sFormulaFilled = sFormulaFilled + "+" + sAliasComponent + "." + sMethodComponent + "()";
						}
					}else if(!StringZZZ.isEmpty(sObject)  && panelCascaded == null){
						//4bb. Die Formel erstellen für KONSTANTEN String					
						if(StringZZZ.isEmpty(sFormulaFilled)){
							sFormulaFilled = sObject;
						}else{
							sFormulaFilled = sFormulaFilled + "+" + sObject;
						}						
					}//end 	if(!StringZZZ.isEmpty(sObject)){												
					System.out.println(ReflectCodeZZZ.getMethodCurrentName() + "# "+ (icountPart+1) + ". Part of evaluationformula: [" + sFormulaFilled + "]");
			}//end for(... icountPart<=saExpressionPart.length-1; ...)
				
			//++++++++++++++  5. Durchführen der Berechnung			
			try {
				Expression  expr = ExpressionFactory.createExpression(sFormulaFilled);							
				sReturn = (String) expr.evaluate(jc);			
			} catch (Exception e) {
				ReportLogZZZ.write(ReportLogZZZ.ERROR, e.getMessage());
				e.printStackTrace();		
				ExceptionZZZ ez = new ExceptionZZZ("Error Processing Formula: "  + e.getMessage(), iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
		}//End main
		return sReturn;
	}
	
	//#### Getter / Setter
	public void setFrameParent(KernelJFrameCascadedZZZ frameParent){
		this.frameParent = frameParent;
	}
	public KernelJFrameCascadedZZZ getFrameParent(){
		return this.frameParent;
	}
	public void setPanelDefault(KernelJPanelCascadedZZZ panelDefault){
		this.panelDefault = panelDefault;
	}
	public KernelJPanelCascadedZZZ getPanelDefault(){
		return this.panelDefault;
	}
	public void setFormulaCurrent(String sFormula){
		this.sFormulaCurrent = sFormula;
	}
	public String getFormulaCurrent(){
		return this.sFormulaCurrent;
	}
	public void setFormulaPartCurrent(String sFormula){
		this.sFormulaPartCurrent = sFormula;
	}
	public String getFormulaPartCurrent(){
		return this.sFormulaPartCurrent;
	}
	public JexlContext getJexlContextCurrent(){
		return this.jc;
	}
	public void setJexlContextCurrent(JexlContext jc){
		this.jc = jc;
	}
}//end class
