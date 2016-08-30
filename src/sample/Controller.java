package sample;

import com.sun.javafx.css.converters.StringConverter;
import com.sun.tools.javac.comp.Todo;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;


public class Controller implements Initializable {

    private String userName;

    @FXML
    ListView todoList;

    @FXML
    TextField todoText;

    ObservableList<ToDoItem> todoItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUserName();
        todoItems.equals(readFromFile(getUserName()));

        todoList.setItems(todoItems);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName() {
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("What's your name?");
        this.userName = inputScanner.next();
    }

    public void addItem() {
        todoItems.add(new ToDoItem(todoText.getText()));;
        todoText.setText("");
        writeUserFile();
    }

    public void removeItem() {
        ToDoItem todoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem();
        todoItems.remove(todoItem);
        writeUserFile();
    }

    public void toggleItem() {
        ToDoItem todoItem = (ToDoItem) todoList.getSelectionModel().getSelectedItem();
        if (todoItem != null) {
            todoItem.isDone = !todoItem.isDone;
            todoList.setItems(null);
            todoList.setItems(todoItems);
        }
        writeUserFile();
    }

    public void toggleAll() {
        for (ToDoItem currToDoItem : todoItems) {
            currToDoItem.isDone = !currToDoItem.isDone;
        }
        todoList.setItems(null);
        todoList.setItems(todoItems);
        writeUserFile();
    }

    public void allDone() {
        for (ToDoItem currToDoItem : todoItems) {
            if (todoItems != null) {
                currToDoItem.isDone = true;
            }
        }
        todoList.setItems(null);
        todoList.setItems(todoItems);
        writeUserFile();
    }

    public void allNotDone() {
        for (ToDoItem currToDoItem : todoItems) {
            currToDoItem.isDone = !true;
        }
        todoList.setItems(null);
        todoList.setItems(todoItems);
        writeUserFile();
    }

    public void writeUserFile() {
        FileWriter writeToFile = null;
        try {
            File userFile = new File(getUserName() + "-todoList.txt");
            writeToFile = new FileWriter(userFile);

            for (ToDoItem currenttodoItem : todoItems) {
                String checked;
                if (currenttodoItem.isDone) {
                    checked = "true";
                } else {
                    checked = "false";
                }
                writeToFile.write(currenttodoItem.text + "," + checked + "\n");
            }
            writeToFile.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (writeToFile != null) {
                try {
                    writeToFile.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public ListView readFromFile (String userName) {
        try {
            File userFile = new File(userName + "-todoList.txt");
            Scanner fileScanner = new Scanner(userFile);

            while (fileScanner.hasNextLine()) {
                String scanString = fileScanner.nextLine();
                String[] parts = scanString.split(",");
                ToDoItem thisItem = new ToDoItem(parts[0]);
                if (parts[1].equals("true")) {
                    thisItem.isDone = true;
                } else if (parts[1].equals("false")){
                    thisItem.isDone = false;
                }
                todoItems.add(thisItem);
            }
            todoList.setItems(null);
            todoList.setItems(todoItems);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return todoList;
    }
}