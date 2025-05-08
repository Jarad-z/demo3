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
public class ContactsController implements Initializable { // 控制器类实现 Initializable 接口

    @FXML private Button addButton; // 添加按钮
    @FXML private TableView<Contact> contactsTable; // 联系人表格视图
    @FXML private Button deleteButton; // 删除按钮
    @FXML private Button editButton; // 编辑按钮

    @FXML private TableColumn<Contact, String> nameColumn; // 姓名列
    @FXML private TableColumn<Contact, String> phoneColumn; // 电话列
    @FXML private TableColumn<Contact, String> emailColumn; // 邮箱列
    @FXML private TableColumn<Contact, String> groupColumn; // 邮箱列

    @FXML private TextField nameField; // 输入框：姓名
    @FXML private TextField phoneField; // 输入框：电话
    @FXML private TextField emailField; // 输入框：邮箱

    @FXML private ComboBox<String> groupBox; // 下拉框：联系人分组
    @FXML private ComboBox<String> groupFilterBox; // 下拉框：按组过滤
    @FXML private TextField searchField; // 搜索输入框

    private ObservableList<Contact> allContacts = FXCollections.observableArrayList(); // 所有联系人列表（主数据源）

    @Override
    public void initialize(URL location, ResourceBundle resources) { // 初始化方法
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name")); // 将 name 属性绑定到列
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone")); // 将 phone 属性绑定到列
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email")); // 将 email 属性绑定到列

        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));

        ObservableList<String> groups = FXCollections.observableArrayList("家人", "朋友", "同事"); // 定义分组选项
        groupBox.setItems(groups); // 设置分组下拉框选项

        ObservableList<String> filterGroups = FXCollections.observableArrayList("全部"); // 创建过滤组选项，初始包含“全部”
        filterGroups.addAll(groups); // 添加所有分组
        groupFilterBox.setItems(filterGroups); // 设置过滤下拉框选项
        groupFilterBox.setValue("全部"); // 默认选择“全部”

        contactsTable.setItems(allContacts); // 表格绑定联系人数据列表

        addButton.setOnAction(e -> addContact()); // 添加按钮绑定事件
        deleteButton.setOnAction(e -> deleteContact()); // 删除按钮绑定事件
        editButton.setOnAction(e -> editContact()); // 编辑按钮绑定事件
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterContacts()); // 搜索框监听文本变化，实时过滤
        groupFilterBox.setOnAction(e -> filterContacts()); // 下拉框变化时也触发过滤
    }

    private void addContact() { // 添加联系人方法
        String name = nameField.getText(); // 获取姓名
        String phone = phoneField.getText(); // 获取电话
        String email = emailField.getText(); // 获取邮箱
        String group = groupBox.getValue(); // 获取分组

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || group == null) { // 校验是否完整
            showAlert("请填写完整信息"); // 弹出提示
            return;
        }

        Contact contact = new Contact(name, phone, email, group); // 创建联系人对象
        allContacts.add(contact); // 添加到联系人列表
        clearFields(); // 清空输入框
        filterContacts(); // 重新过滤（刷新视图）
    }

    private void deleteContact() { // 删除联系人方法
        Contact selected = contactsTable.getSelectionModel().getSelectedItem(); // 获取选中的联系人
        if (selected != null) {
            allContacts.remove(selected); // 从列表中移除
            filterContacts(); // 重新过滤
        } else {
            showAlert("请选择要删除的联系人"); // 未选择联系人时提示
        }
    }

    private void editContact() { // 编辑联系人方法
        Contact selected = contactsTable.getSelectionModel().getSelectedItem(); // 获取选中的联系人
        if (selected != null) {
            selected.setName(nameField.getText()); // 更新姓名
            selected.setPhone(phoneField.getText()); // 更新电话
            selected.setEmail(emailField.getText()); // 更新邮箱
            selected.setGroup(groupBox.getValue()); // 更新分组
            contactsTable.refresh(); // 刷新表格视图
            clearFields(); // 清空输入框
            filterContacts(); // 重新过滤
        } else {
            showAlert("请选择要编辑的联系人"); // 未选择联系人时提示
        }
    }

    private void filterContacts() { // 联系人过滤逻辑
        String searchText = searchField.getText().toLowerCase(); // 获取搜索关键字并转小写
        String selectedGroup = groupFilterBox.getValue(); // 获取选中的分组

        ObservableList<Contact> filtered = allContacts.stream() // 遍历所有联系人
                .filter(c -> (selectedGroup == null || selectedGroup.equals("全部") || c.getGroup().equals(selectedGroup))) // 分组过滤
                .filter(c -> c.getName().toLowerCase().contains(searchText) || // 姓名包含搜索词
                        c.getPhone().toLowerCase().contains(searchText) || // 电话包含搜索词
                        c.getEmail().toLowerCase().contains(searchText)) // 邮箱包含搜索词
                .collect(Collectors.toCollection(FXCollections::observableArrayList)); // 转为 ObservableList

        contactsTable.setItems(filtered); // 表格显示过滤后的结果
    }

    private void clearFields() { // 清空输入框
        nameField.clear(); // 清空姓名
        phoneField.clear(); // 清空电话
        emailField.clear(); // 清空邮箱
        groupBox.setValue(null); // 清空分组选择
    }

    private void showAlert(String message) { // 显示提示框
        Alert alert = new Alert(Alert.AlertType.WARNING); // 创建警告类型对话框
        alert.setTitle("提示"); // 设置标题
        alert.setHeaderText(null); // 不设置头部文字
        alert.setContentText(message); // 设置内容
        alert.showAndWait(); // 显示对话框
    }
}
