package com.example.demo3; // 包名

import com.example.demo3.Contact; // 导入 Contact 实体类
import javafx.collections.FXCollections; // 用于创建可观察的集合
import javafx.collections.ObservableList; // 可观察列表，用于表格数据绑定
import javafx.fxml.FXML; // FXML 注解，用于标记从 FXML 加载的控件
import javafx.fxml.Initializable; // 实现初始化接口
import javafx.scene.control.*; // 导入所有常用控件

import java.net.URL; // URL 类，用于资源定位
import java.util.ResourceBundle; // 国际化资源绑定
import java.util.stream.Collectors; // 用于列表过滤
import javafx.scene.control.cell.PropertyValueFactory;

public class ContactController1 {
    @FXML
    private TreeView<String> groupTree;
    @FXML
    private TableView<Contact> contactsTable;
    @FXML
    private TableColumn<Contact, String> nameColumn;
    @FXML
    private TableColumn<Contact, String> phoneColumn;
    @FXML
    private TableColumn<Contact, String> emailColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<String> groupBox;
    @FXML
    private Button addGroupButton;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private final ObservableList<Contact> contactsList = FXCollections.observableArrayList();
    // 初始化联系人表格
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));



        groupBox.getItems().addAll("所有联系人", "未分组联系人", "同事", "朋友", "家人");
        contactsTable.setItems(contactsList);
        addGroupButton.setOnAction(e -> addGroup());
        addButton.setOnAction(e -> addContact());
        editButton.setOnAction(e -> editContact());
        deleteButton.setOnAction(e -> deleteContact());

        contactsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // 监听选中行的改变

    }

    private void addGroup() {
        String selectedGroup = groupBox.getValue();
        if (selectedGroup != null && !selectedGroup.isEmpty()) {
            TreeItem<String> groupItem = new TreeItem<>(selectedGroup);
            groupTree.getRoot().getChildren().add(groupItem);
        }
    }

    private void addContact() {
        String name = nameField.getText(); // 获取姓名
        String phone = phoneField.getText(); // 获取电话
        String email = emailField.getText(); // 获取邮箱
        String group = groupBox.getValue(); // 获取分组


        if (!name.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
            Contact newContact = new Contact(name, phone, email, group);
            contactsList.add(newContact);
            clearFields();
        }

    }

    private void editContact() {
        Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            int index = contactsList.indexOf(selectedContact);
            Contact updatedContact = new Contact(
                    nameField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    groupBox.getValue()
            );
            contactsList.set(index, updatedContact);
            contactsTable.getSelectionModel().select(updatedContact);
        }
    }

    private void deleteContact() {
        Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            contactsTable.getItems().remove(selectedContact);
        }
    }

    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        emailField.clear();
        groupBox.setValue(null);
    }
}
