package edu.brown.cs32.siliclone.client.workspace;


import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;

import edu.brown.cs32.siliclone.plugins.Plugins;
import edu.brown.cs32.siliclone.operators.OperatorAdder;
import edu.brown.cs32.siliclone.operators.OperatorTemplate;

public class OpListing extends VLayout {
	private final TreeGrid opGrid;
	private final Tree opTree;
	
	public OpListing(){
		setWidth("200px");
		setShowResizeBar(true);
        setOverflow(Overflow.HIDDEN);
        
        opTree = new Tree();
        opTree.setTitleProperty("Main Operators");
        opTree.setModelType(TreeModelType.CHILDREN);

        opGrid = new TreeGrid();
        opGrid.setData(opTree);
        opGrid.setTitle("Add Operator:");
        
        opGrid.setCanDragRecordsOut(true);
        
        Plugins.defineOperators(new OperatorAdderImpl());

		addMember(opGrid);
		

	}
	
	private class OperatorAdderImpl implements OperatorAdder{
		
		public void addOperator(OperatorTemplate toBeAdded){
			opTree.add(new DragCreate(toBeAdded), opTree.getRoot());
		}
		
	}
}
