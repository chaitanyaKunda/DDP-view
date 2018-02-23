package com.agility.ddp.view.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MetaUtil {
	/**
	 * 
	 * @param selectedGroupList
	 * @param dbList
	 * @return
	 */
	public Set updateList(List<String> selectedGroupList,List<String>  dbList ){
		ArrayList unionAll = new ArrayList();
		Set updateList = new HashSet();
		unionAll.addAll(selectedGroupList);
		unionAll.addAll(dbList);
		//Prepare Update List
		Set uniqueEntries = new HashSet();
	    for (Iterator iter = unionAll.iterator(); iter.hasNext(); ) {
	      Object element = iter.next();
	      if (!uniqueEntries.add(element)){ // if current element is a duplicate,
//		    	  System.out.println("Update record:"+element);
	      	  updateList.add(element);
	      	  iter.remove();
	      }// remove it
	    }
		return updateList;
	}
	public Set removeDuplicates( List<String> selectedGroupList, ArrayList dbGroupList) {
	    ArrayList union = new ArrayList(selectedGroupList);
	    union.addAll(dbGroupList);
	    // Prepare an intersection
	    ArrayList intersection = new ArrayList(selectedGroupList);
	    intersection.retainAll(dbGroupList);
	    // Subtract the intersection from the union
	    union.removeAll(intersection);
	    Set set = new HashSet(union);
	    
	    return set;
	}
	public Set saveList( List<String> selectedGroupList, ArrayList dbGroupList) {
	    Set updateVlaue = removeDuplicates(selectedGroupList,dbGroupList);
		Set updateList = new HashSet();
	    for(Object object : updateVlaue){
			for(Object obj :selectedGroupList){
				if(object.equals(obj)){
					updateList.add(object);
				}
			}
		}
	    return updateList;
	}
	public Set delList(List<String> selectedGroupList, ArrayList dbGroupList) {
		Set updateVlaue = removeDuplicates(selectedGroupList,dbGroupList);
		Set updateList = new HashSet();
	    for(Object object : updateVlaue){
			for(Object obj :dbGroupList){
				if(object.equals(obj)){
					updateList.add(object);
				}
			}
		}
		
		return updateList;
	}
	
	public static void main(String args[]){
		
	MetaUtil metaUtil = new MetaUtil();
	
	List<String> selectedGroupList= new ArrayList<String>();
	selectedGroupList.add("1");
	selectedGroupList.add("2");
	selectedGroupList.add("8");
	selectedGroupList.add("6");
	selectedGroupList.add("10");
	
	ArrayList dbGroupList = new 	ArrayList();
	dbGroupList.add("1");
	dbGroupList.add("1");
	dbGroupList.add("1");
	dbGroupList.add("2");
	dbGroupList.add("4");
	dbGroupList.add("5");
	dbGroupList.add("6");
	
		   Set updateGroupList = metaUtil.updateList(selectedGroupList, dbGroupList);
		   System.out.println(updateGroupList);
		   
	        // Save List
	        Set saveGroupList =  metaUtil.saveList(selectedGroupList, dbGroupList);
	        System.out.println(saveGroupList);
	    	
	    	//Delete List
	        Set deleteGroupList =  metaUtil.delList(selectedGroupList, dbGroupList);
	        System.out.println(deleteGroupList);
	
//		ArrayList<String> firstList = new ArrayList<String>();
//	    firstList.add("book1");
//	    firstList.add("book3");
//	    firstList.add("book4");
//	    firstList.add("book2");
//	    ArrayList<String> secondList = new ArrayList<String>();
//	    secondList.add("book1");
//	    secondList.add("book2");
//	    secondList.add("book3");
//	    List<Integer> comparingList = new ArrayList<Integer>();
//	    // adding default values as one
//	    for (int a = 0; a < firstList.size(); a++) {
//	        comparingList.add(0);
//
//	    }
//	    for (int counter = 0; counter < firstList.size(); counter++) {
//	        if (secondList.contains(firstList.get(counter))) {
//	            comparingList.set(counter, 1);
//	        }
//	    }
//	    System.out.println(comparingList);
	 // Make the two lists
	    // Prepare a union
	}
}
