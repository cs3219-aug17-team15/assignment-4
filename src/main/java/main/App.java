package main;

import controllers.LogicController;

public class App {

  public static void main(String[] args) {
    //Router router = new Router();
    //router.initAndServe();
	  
	LogicController logic = LogicController.getInstance();
	System.out.print(logic.task1());
	System.out.print(logic.task2());
	System.out.print(logic.task3());
	System.out.print(logic.task4());
	System.out.print(logic.task5());
  }
}
