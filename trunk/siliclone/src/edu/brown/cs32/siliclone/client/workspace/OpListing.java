package edu.brown.cs32.siliclone.client.workspace;


import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;


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
		
		addMember(opGrid);
	}
	public void addOpCreate(DragCreate c){
		opTree.add(c, opTree.getRoot());
	}
}
