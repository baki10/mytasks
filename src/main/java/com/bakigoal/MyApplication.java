package com.bakigoal;

import com.bakigoal.model.Task;
import com.bakigoal.repository.TaskRepository;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Main app
 * Created by ilmir on 03.03.17.
 */
@SpringBootApplication
public class MyApplication extends Application {

  private static String[] args;
  private TaskRepository repository;
  private TableView<Task> table = new TableView<>();
  private final ObservableList<Task> data = FXCollections.observableArrayList();
  private final VBox add = new VBox();
  private final HBox month = new HBox();
  private final ChoiceBox<String> monthChoiceBox = new ChoiceBox<>();
  private final HBox title = new HBox();
  private Label totalPrice = new Label();
  private final Button deleteRow = new Button("Delete task");

  public static void main(String[] args) {
    MyApplication.args = args;
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    // Bootstrap Spring context here.
    ApplicationContext context = SpringApplication.run(MyApplication.class, args);
    repository = context.getBean(TaskRepository.class);

    Scene scene = new Scene(new Group());
    stage.setTitle("Tasks");
    stage.setWidth(450);
    stage.setHeight(600);

    initTitle();
    initTable();
    initMonth();
    initAddTaskSection();
    initDeleteRowButton();

    final VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 0, 0, 10));
    vbox.getChildren().addAll(title, month, table, deleteRow, add);

    ((Group) scene.getRoot()).getChildren().addAll(vbox);

    stage.setScene(scene);
    stage.show();
  }

  private void initDeleteRowButton() {
    deleteRow.setMinWidth(100);
    deleteRow.setOnAction(event -> {
      ObservableList<Task> selectedItems = table.getSelectionModel().getSelectedItems();
      for(Task task: selectedItems){
        repository.delete(task);
      }
      refreshByMonth();
    });
  }

  private void initTitle() {
    final Label label = new Label("Total Price: $");
    label.setFont(new Font("Arial", 20));

    totalPrice.setFont(new Font("Arial", 20));
    totalPrice.setText("234");
    title.getChildren().addAll(label, totalPrice);
    title.setSpacing(3);
  }

  private void initMonth() {
    monthChoiceBox.getItems().addAll(Util.getMonths());
    monthChoiceBox.setValue(Util.getCurrentMonth());
    monthChoiceBox.setOnAction(event -> {
      refreshByMonth();
    });

    month.getChildren().addAll(monthChoiceBox);
    month.setSpacing(3);
  }

  private void initAddTaskSection() {
    final TextField taskTitle = new TextField();
    taskTitle.setPromptText("Task");
    taskTitle.setMaxWidth(100);
    final DatePicker deadline = new DatePicker();
    deadline.setConverter(new StringConverter<LocalDate>() {
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Task.DATE_PATTERN);

      @Override
      public String toString(LocalDate date) {
        if (date != null) {
          return dateFormatter.format(date);
        } else {
          return "";
        }
      }

      @Override
      public LocalDate fromString(String string) {
        if (string != null && !string.isEmpty()) {
          return LocalDate.parse(string, dateFormatter);
        } else {
          return null;
        }
      }
    });

    deadline.setPromptText("Deadline");
    deadline.setMaxWidth(120);
    final TextField price = new TextField();
    price.setMaxWidth(97);
    price.setPromptText("Price");

    final ChoiceBox<String> authorChoiceBox = new ChoiceBox<>();
    authorChoiceBox.getItems().addAll("ilmir", "aliya");
    authorChoiceBox.setValue("ilmir");
    authorChoiceBox.setMinWidth(100);

    final Button addButton = new Button("Add");
    addButton.setMinWidth(100);
    addButton.setOnAction(e -> {
      Task task = new Task(taskTitle.getText(), deadline.getValue(), Double.valueOf(price.getText()), authorChoiceBox.getValue());
      repository.save(task);
      refreshByMonth();
      taskTitle.clear();
      deadline.setValue(null);
      price.clear();
    });

    HBox hBox = new HBox();
    hBox.getChildren().addAll(taskTitle, deadline, price, authorChoiceBox);
    hBox.setSpacing(3);
    add.getChildren().addAll(hBox, addButton);
    add.setSpacing(3);
  }

  private void initTable() {
    table.setEditable(true);

    // title
    TableColumn titleColumn = new TableColumn("Task");
    titleColumn.setMinWidth(100);
    titleColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("title"));
    titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    titleColumn.setOnEditCommit(
        event -> {
          TableColumn.CellEditEvent<Task, String> cell = (TableColumn.CellEditEvent<Task, String>) event;
          Task task = cell.getTableView().getItems().get(cell.getTablePosition().getRow());
          task.setTitle(cell.getNewValue());
          repository.save(task);
        }
    );

    // deadline
    TableColumn deadlineColumn = new TableColumn("Deadline");
    deadlineColumn.setMinWidth(123);
    deadlineColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("deadline"));
    deadlineColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    deadlineColumn.setOnEditCommit(
        event -> {
          TableColumn.CellEditEvent<Task, String> cell = (TableColumn.CellEditEvent<Task, String>) event;
          Task task = cell.getTableView().getItems().get(cell.getTablePosition().getRow());
          task.setDeadline(cell.getNewValue());
          repository.save(task);
        }
    );

    // price
    TableColumn priceColumn = new TableColumn("Price");
    priceColumn.setMinWidth(100);
    priceColumn.setCellValueFactory(new PropertyValueFactory<Task, Double>("price"));
    priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {

      @Override
      public String toString(Double object) {
        return String.valueOf(object);
      }

      @Override
      public Double fromString(String string) {
        return Double.parseDouble(string);
      }
    }));
    priceColumn.setOnEditCommit(
        event -> {
          TableColumn.CellEditEvent<Task, Double> cell = (TableColumn.CellEditEvent<Task, Double>) event;
          Task task = cell.getTableView().getItems().get(cell.getTablePosition().getRow());
          task.setPrice(cell.getNewValue());
          repository.save(task);
          refreshByMonth();
        }
    );

    // author
    TableColumn authorColumn = new TableColumn("Author");
    authorColumn.setMinWidth(100);
    authorColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("author"));

    refreshByMonth();

    table.setItems(data);
    table.getColumns().addAll(titleColumn, deadlineColumn, priceColumn, authorColumn);
  }

  private void refreshByMonth() {
    String month = monthChoiceBox.getValue();
    if (month == null || month.length() == 0) {
      month = Util.getCurrentMonth();
    }
    List<Task> tasks = repository.findByMonth(month);
    refreshTable(tasks);
    double sum = tasks.stream().mapToDouble(Task::getPrice).sum();
    totalPrice.setText(String.valueOf(sum));
  }

  private void refreshTable(List<Task> tasks) {
    data.clear();
    data.addAll(tasks);
  }
}
