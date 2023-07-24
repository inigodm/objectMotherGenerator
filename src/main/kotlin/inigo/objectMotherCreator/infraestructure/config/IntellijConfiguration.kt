package inigo.objectMotherCreator.infraestructure.config;

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import org.jdesktop.swingx.VerticalLayout
import org.yaml.snakeyaml.Yaml
import java.awt.FlowLayout
import java.awt.event.ActionListener
import java.io.InputStream
import javax.swing.*
import javax.swing.table.DefaultTableModel


class IntellijConfiguration: Configurable {
    private val fakerTextField: JTextField = JTextField()
    private val methodPrefixTextField: JTextField = JTextField()

    override fun createComponent(): JComponent {
        val main = JPanel()
        // Cargar el archivo YAML desde la carpeta resources
        val inputStream: InputStream = IntellijConfiguration::class.java.getResourceAsStream("/application.yaml")!!
        val yaml = Yaml()

        // Analizar el contenido del archivo YAML
        val data = mutableListOf<Array<Any>>()
        val yamlData = yaml.load(inputStream) as LinkedHashMap<String, Any>
        for (entry in yamlData) {
            //val row = arrayOf(entry["columna1"], entry["columna2"])
            //data.add(row)
        }

        main.layout = VerticalLayout()
        main.add(buildFakerClassnameComponent())
        main.add(buildMethodPrefixComponent())
        main.add(buildButton( "", "Default", ActionListener { e ->
            run {
                fakerTextField.text = IntellijPluginService.DEFAULT_STATE.getFakerClassName()
                methodPrefixTextField.text = IntellijPluginService.DEFAULT_STATE.getPrefixes()
            }
        }))
        main.add(buildTable(arrayOf(arrayOf("uno", "dos")), arrayOf("h1", "h2")))
        reset()
        return main
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
        val pack = JPanel(FlowLayout(FlowLayout.LEADING))
        val tableModel = DefaultTableModel(data, columnNames)
        val table = JBTable(tableModel)
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS)
        table.getColumnModel().getColumn(0).setPreferredWidth(100)
        table.getColumnModel().getColumn(1).setPreferredWidth(100)

        // Agregar la tabla a un JScrollPane para permitir el desplazamiento

        // Agregar la tabla a un JScrollPane para permitir el desplazamiento
        val scrollPane = JBScrollPane(table)
        pack.add(scrollPane)
        return pack
    }

    override fun isModified(): Boolean {
        return fakerTextField.text != IntellijPluginService.getInstance().state.getFakerClassName() ||
        methodPrefixTextField.text != IntellijPluginService.getInstance().state.getPrefixes()
    }

    override fun apply() {
        IntellijPluginService.getInstance().loadState(PluginState(fakerTextField.text, methodPrefixTextField.text))
    }

    override fun getDisplayName(): String {
        return "Object Mother Creator"
    }
}
