package controllers;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.velocity.VelocityTemplateEngine;

public class ViewController {
  public static Route task1 = (Request req, Response resp) -> {
    Map<String, Object> model = new HashMap<>();
    model.put("taskTitle", "Task 1");
    model.put("taskHeader", "Task 1: The top 10 authors for venue 'ArXiv'");
    model.put("taskJs", "http://localhost:4567/task1.js");
    model.put("taskCss", "http://localhost:4567/task1.css");
    return new VelocityTemplateEngine().render(
        new ModelAndView(model, "/velocity/layout.vm")
    );
  };

  public static Route task2 = (Request req, Response resp) -> {
    Map<String, Object> model = new HashMap<>();
    model.put("taskTitle", "Task 2");
    model.put("taskHeader", "Task 2: The top 5 papers for venue 'ArXiv'");
    model.put("taskJs", "http://localhost:4567/task2.js");
    model.put("taskCss", "http://localhost:4567/task2.css");
    return new VelocityTemplateEngine().render(
        new ModelAndView(model, "/velocity/layout.vm")
    );
  };

  public static Route task3 = (Request req, Response resp) -> {
    Map<String, Object> model = new HashMap<>();
    model.put("taskTitle", "Task 3");
    model.put("taskHeader", "Task 3: Trend of the amount of publications for venue 'ICSE'");
    model.put("taskJs", "http://localhost:4567/task3.js");
    model.put("taskCss", "http://localhost:4567/task3.css");
    return new VelocityTemplateEngine().render(
        new ModelAndView(model, "/velocity/layout.vm")
    );
  };

  public static Route task4 = (Request req, Response resp) -> {
    return "Hello World";
  };

  public static Route task5 = (Request req, Response resp) -> {
    Map<String, Object> model = new HashMap<>();
    model.put("taskTitle", "Task 5");
    model.put("taskHeader", "Task 5: The top 10 key phrases for venue 'ArXiv'");
    model.put("taskJs", "http://localhost:4567/task5.js");
    model.put("taskCss", "http://localhost:4567/task5.css");
    return new VelocityTemplateEngine().render(
        new ModelAndView(model, "/velocity/layout.vm")
    );
  };
}
