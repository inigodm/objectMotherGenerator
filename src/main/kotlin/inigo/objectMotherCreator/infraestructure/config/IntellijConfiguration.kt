package inigo.objectMotherCreator.infraestructure.config;

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import org.jdesktop.swingx.VerticalLayout
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*
import javax.swing.*
import javax.swing.table.DefaultTableModel


class IntellijConfiguration: Configurable {
    private val fakerTextField: JTextField = JTextField()
    private val methodPrefixTextField: JTextField = JTextField()
    private lateinit var tableVector : Vector<Vector<String>>
    private lateinit var table: JBTable
    private lateinit var tableModel: TableModelSpecial
    override fun createComponent(): JComponent {
        val main = JPanel()

        main.layout = VerticalLayout()
        main.add(buildFakerClassnameComponent())
        main.add(buildMethodPrefixComponent())
        main.add(buildTable(Vector(listOf("Class", "Comma separated imports", "Code to generate random object"))))
        main.add(buildButton("Add new mapping", "+", insertRow()))
        main.add(buildButton( "", "Default", defaultValues()))
        reset()
        return main
    }

    private fun insertRow(): (e: ActionEvent) -> Unit = {
        run {
            val model = (table.model as DefaultTableModel)
            val data = model.dataVector
            if (data.size > 0) {
                val k = data[data.size - 1] as Vector<String>
                val v = data[data.size - 1] as Vector<String>
                if (v[0].isNotEmpty() || k[1].isNotEmpty()) {
                    model.insertRow(model.rowCount, arrayOf("1", "2"))
                }
            }
        }
    }

    private fun defaultValues(): (e: ActionEvent) -> Unit = {
        run {
            val defaultState = IntellijPluginService.defaultState()
            fakerTextField.text = defaultState.fakerClassname
            methodPrefixTextField.text = defaultState.prefixes
            tableVector = defaultState.mappings
        }
    }

    private fun buildFakerClassnameComponent(): JPanel {
        return textFieldWithLabel(
            "Faker imported class",
            IntellijPluginService.getInstance().getFakerClassName(),
            fakerTextField)
    }

    private fun buildMethodPrefixComponent(): JPanel {
        return textFieldWithLabel(
            "Prefix of builder functions",
            IntellijPluginService.getInstance().getPrefixes(),
            methodPrefixTextField)
    }

    private fun textFieldWithLabel(labelText : String, initialValue: String, textField: JTextField): JPanel {
        val pack = JPanel(FlowLayout(FlowLayout.LEADING))
        textField.text = initialValue
        val label = JLabel(labelText)
        pack.add(label)
        pack.add(textField)
        return pack
    }

    private fun buildButton(labelText : String, buttonText: String, function: ActionListener): JPanel {
        val pack = JPanel(FlowLayout(FlowLayout.LEADING))
        val button = JButton(buttonText)
        button.addActionListener(function)
        val label = JLabel(labelText)
        pack.add(label)
        pack.add(button)
        return pack
    }

    private fun buildTable(columnNames: Vector<String>): JPanel {
        val pack = JPanel(BorderLayout())
        tableVector = IntellijPluginService.getInstance().getMappings()
        tableModel = TableModelSpecial(tableVector, columnNames)
        val listSelection = tableModel.listSelectionListenerCreator()
        table = JBTable(tableModel)
        table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
        table.columnModel.getColumn(0).setPreferredWidth(100)
        table.columnModel.getColumn(1).setPreferredWidth(100)
        table.columnModel.getColumn(2).setPreferredWidth(100)
        table.fillsViewportHeight = true
        table.selectionModel.addListSelectionListener(listSelection);
        table.columnModel.selectionModel.addListSelectionListener(listSelection);
        pack.add(JBScrollPane(table), BorderLayout.CENTER)
        return pack
    }

    override fun isModified(): Boolean {
        return fakerTextField.text != IntellijPluginService.getInstance().getFakerClassName() ||
        methodPrefixTextField.text != IntellijPluginService.getInstance().getPrefixes()
                || tableVector.equals(IntellijPluginService.getInstance().getMappings())
    }

    override fun apply() {
        IntellijPluginService.getInstance().loadState(PluginState(fakerTextField.text, methodPrefixTextField.text, tableVector))
    }

    override fun getDisplayName(): String {
        return "Object Mother Creator"
    }
}

