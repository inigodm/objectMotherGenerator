package inigo.objectMotherCreator.infraestructure.config;

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import inigo.objectMotherCreator.infraestructure.config.persistence.PluginState
import org.jdesktop.swingx.VerticalLayout
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*
import javax.swing.*
import javax.swing.event.TableModelListener
import javax.swing.table.DefaultTableModel

class IntellijConfigurationPanel: Configurable {
    private val fakerTextField: JTextField = JTextField()
    private val methodPrefixTextField: JTextField = JTextField()
    private lateinit var table: JBTable
    private val columnNames = Vector(listOf("Class", "Comma separated imports", "Code to generate random object"))
    private var tableModel: TableModelSpecial = TableModelSpecial(IntellijPluginService.getInstance().getMappings(), columnNames)

    override fun createComponent(): JComponent {
        println("New pane")
        val main = JPanel()
        main.layout = BorderLayout()
        val top = JPanel(VerticalLayout())
        top.add(buildFakerClassnameComponent())
        top.add(buildMethodPrefixComponent())
        main.add(top, BorderLayout.NORTH)
        main.add(buildTable(columnNames), BorderLayout.CENTER)
        val bottom = JPanel(VerticalLayout())
        bottom.add(buildButton("Add new mapping", "+", tableModel.insertRow()))
        bottom.add(buildButton( "", "Default values", defaultValues()))
        main.add(bottom, BorderLayout.SOUTH)
        return main
    }

    private fun defaultValues(): (e: ActionEvent) -> Unit = {
        run {
            val defaultState = IntellijPluginService.defaultState()
            fakerTextField.text = defaultState.fakerClassname
            methodPrefixTextField.text = defaultState.prefixes
            tableModel.removeAllElements()
            tableModel.addAll(defaultState.mappings)
            tableModel.fireTableDataChanged();
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
        val borderLayout = BorderLayout()
        val pack = JPanel(borderLayout)
        table = JBTable(tableModel)
        table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
        table.columnModel.getColumn(0).setPreferredWidth(100)
        table.columnModel.getColumn(1).setPreferredWidth(100)
        table.columnModel.getColumn(2).setPreferredWidth(100)
        table.fillsViewportHeight = true
        table.toolTipText = "Only has to be added the name of the class, without package, that " +
                "is wanted to be mapped to a generator text in the objectmother and the text which should be write to generate" +
                " an object of that class (generator text). \n\nIf you want to have random generators for the same class" +
                " you can put them all comma-separated and one will be chosen randomly. Additionally you should" +
                " add the needed imports for the generator and for the class, add them comma-separated"
        pack.add(JBScrollPane(table), BorderLayout.CENTER)
        return pack
    }

    override fun isModified(): Boolean {
        if (tableModel.dataVector.size >= 11) {
            println("<--${tableModel.dataVector[10]} \n -->${IntellijPluginService.getInstance().getMappings()[10]}")
        }
        println("is modified: ${!tableModel.dataEqualsTo(IntellijPluginService.getInstance().getMappings())}")
        val b = fakerTextField.text != IntellijPluginService.getInstance().getFakerClassName() ||
        methodPrefixTextField.text != IntellijPluginService.getInstance().getPrefixes()
                || !tableModel.dataEqualsTo(IntellijPluginService.getInstance().getMappings())
        return b
    }

    @Suppress("UNCHECKED_CAST")
    override fun apply() {
        tableModel.cleanVoids()
        println("applying changes")
        IntellijPluginService.getInstance().loadState(PluginState(fakerTextField.text, methodPrefixTextField.text, tableModel.dataVector as Vector<Vector<String>>))
    }

    override fun cancel() {
        reset()
    }

    override fun reset() {
        println("reset")
        tableModel.resetDataVector(IntellijPluginService.getInstance().getMappings())
        tableModel.fireTableDataChanged()
        fakerTextField.text = IntellijPluginService.getInstance().getFakerClassName()
        methodPrefixTextField.text = IntellijPluginService.getInstance().getPrefixes()
        super.reset()
    }

    override fun getDisplayName(): String {
        return "Object Mother Creator"
    }
}


class TableModelSpecial(data: Vector<Vector<String>>, columnNames: Vector<String>) : DefaultTableModel(data, columnNames) {

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return true
    }

    fun cleanVoids() {
        var i = 0
        val toRemove = mutableListOf<Int>()
        for (vector in (dataVector as Vector<Vector<String>>)) {
            if (!vector.any { !it.isNullOrEmpty() }) {
                toRemove.addFirst(i)
            }
            i++
        }
        if (!toRemove.isEmpty()) {
            toRemove.forEach { dataVector.removeAt(it) }
            fireTableDataChanged()
        }
    }

    fun removeAllElements() {
        dataVector.removeAllElements()
    }

    fun addAll(mappings: Vector<Vector<String>>) {
        dataVector.addAll(mappings)
    }

    fun dataEqualsTo(mappings: Vector<Vector<String>>): Boolean {
        if (mappings.size != dataVector.size) {
            return false
        }

        var i = 0
        var j = 0
        for (vector: Vector<String> in mappings) {
            for (string: String in vector) {
                if (!string.equals((dataVector as Vector<Vector<String>>)[i][j]))  {
                    return false
                }
                j++
            }
            j = 0
            i++
        }
        return true
    }

    fun insertRow(): (e: ActionEvent) -> Unit = {
        run {
            val tableVector = dataVector as Vector<Vector<String>>
            if (tableVector.size > 0) {
                val v = tableVector.getLast()
                if (v[0].isNotEmpty() || v[1].isNotEmpty()) {
                    insertRow(rowCount, arrayOf("", "", ""))
                    fireTableDataChanged();
                }
            }
        }
    }

    override fun addTableModelListener(l: TableModelListener?) {
        super.addTableModelListener(l)
    }

    fun resetDataVector(data: Vector<Vector<String>>) {
        dataVector.clear()
        dataVector.addAll(data)
    }
}

fun <E> MutableList<E>.addFirst(e: E) {
    this.add(0, e)
}

fun <E> MutableList<E>.getLast(): E {
    return this[this.size - 1]
}
infix fun <T> Collection<T>.sameContentWith(collection: Collection<T>?)
        = collection?.let { this.size == it.size && this.containsAll(it) }