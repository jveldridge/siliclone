package edu.brown.cs32.siliclone.client;


import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;

import edu.brown.cs32.siliclone.plugins.Plugins;
import edu.brown.cs32.siliclone.operators.OperatorAdder;
import edu.brown.cs32.siliclone.operators.OperatorTemplate;

/**
 * Represents the tree of OperatorTemplates that is shown
 * on the left-hand side of the home screen.  OperatorTemplates
 * can be dragged from this OpTemplateListing to a Workspace
 * to add new Operators to the Workspace.
 */
public class OpTemplateListing extends VLayout {
	private final TreeGrid opGrid;
	private final Tree opTree;
	
	public OpTemplateListing() {
		this.setWidth("200px");
		this.setShowResizeBar(true);
		this.setOverflow(Overflow.HIDDEN);
        
        opTree = new Tree();
        opTree.setTitleProperty("Main Operators");
        opTree.setModelType(TreeModelType.CHILDREN);

        opGrid = new TreeGrid();
        opGrid.setData(opTree);
        opGrid.setTitle("Add Operator:");
        
        opGrid.setCanDragRecordsOut(true);
        
        //add all of the operators defined in the Plugins
        //class' defineOperators(...) method to the opGrid
        Plugins.defineOperators(new OperatorAdderImpl());

        this.addMember(opGrid);
	}
	
	/**
	 * TODO why interface?
	 */
	private class OperatorAdderImpl implements OperatorAdder {
		
		public void addOperator(OperatorTemplate toBeAdded){
			opTree.add(new OpTemplateView(toBeAdded), opTree.getRoot());
		}
		
	}
}
