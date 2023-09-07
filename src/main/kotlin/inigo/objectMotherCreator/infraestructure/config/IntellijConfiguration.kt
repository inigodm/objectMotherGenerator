package inigo.objectMotherCreator.infraestructure.config;

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import org.jdesktop.swingx.VerticalLayout
import org.yaml.snakeyaml.Yaml
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.ActionListener
import java.io.InputStream
import java.io.OutputStream
import javax.swing.*
import javax.swing.table.DefaultTableModel


class IntellijConfiguration: Configurable {
    private val fakerTextField: JTextField = JTextField()
    private val methodPrefixTextField: JTextField = JTextField()
    private var mappings : Map<String, String> = mutableMapOf()
    override fun createComponent(): JComponent {
        val main = JPanel()

        main.layout = VerticalLayout()
        main.add(buildFakerClassnameComponent())
        main.add(buildMethodPrefixComponent())
        main.add(buildButton( "", "Default", ActionListener { e ->
            run {
                fakerTextField.text = IntellijPluginService.DEFAULT_STATE.getFakerClassName()
                methodPrefixTextField.text = IntellijPluginService.DEFAULT_STATE.getPrefixes()
                mappings = IntellijPluginService.DEFAULT_STATE.getMappings()
            }
        }))
        main.add(buildTable(loadMappingsFrom(IntellijPluginService.getInstance().state.getMappings()), arrayOf("Class", "Code to generate random object")), BorderLayout.CENTER)
        reset()
        return main
    }

    private fun loadMappingsFrom(map: Map<String, String>): Array<Array<String>> {
        val data = mutableListOf<Array<String>>()
        map.keys.forEach { data.add(arrayOf(it, map.get(it)!!)) }
        return data.toTypedArray()
    }


    private fun buildFakerClassnameComponent(): JPanel {
        return textFieldWithLabel(
            "Faker imported class",
            IntellijPluginService.getInstance().state.getFakerClassName(),
            fakerTextField)
    }

    private fun buildMethodPrefixComponent(): JPanel {
        return textFieldWithLabel(
            "Prefix of builder functions",
            IntellijPluginService.getInstance().state.getPrefixes(),
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

    private fun buildTable(data: Array<Array<String>>, columnNames: Array<String>): JPanel {
        val pack = JPanel(BorderLayout())
        val tableModel = DefaultTableModel(data, columnNames)
        val table = JBTable(tableModel)
        table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
        table.columnModel.getColumn(0).setPreferredWidth(100)
        table.columnModel.getColumn(1).setPreferredWidth(100)
        pack.add(JBScrollPane(table), BorderLayout.CENTER)
        return pack
    }

    override fun isModified(): Boolean {
        return fakerTextField.text != IntellijPluginService.getInstance().state.getFakerClassName() ||
        methodPrefixTextField.text != IntellijPluginService.getInstance().state.getPrefixes() ||
                mappings.equals(IntellijPluginService.getInstance().state.getMappings())
    }

    override fun apply() {
        IntellijPluginService.getInstance().loadState(PluginState(fakerTextField.text, methodPrefixTextField.text, mappings))
    }

    override fun getDisplayName(): String {
        return "Object Mother Creator"
    }
}
