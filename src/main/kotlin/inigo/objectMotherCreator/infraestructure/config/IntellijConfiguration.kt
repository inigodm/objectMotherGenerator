package inigo.objectMotherCreator.infraestructure.config;

import com.intellij.openapi.options.Configurable
import org.jdesktop.swingx.VerticalLayout
import java.awt.FlowLayout
import java.awt.event.ActionListener
import javax.swing.*


class IntellijConfiguration: Configurable {
    private val fakerTextField: JTextField = JTextField()
    private val methodPrefixTextField: JTextField = JTextField()

    override fun createComponent(): JComponent {
        val main = JPanel()
        main.layout = VerticalLayout()
        main.add(buildFakerClassnameComponent())
        main.add(buildMethodPrefixComponent())
        main.add(buildButton( "", "Default", ActionListener { e ->
            run {
                fakerTextField.text = IntellijPluginService.DEFAULT_STATE.getFakerClassName()
                methodPrefixTextField.text = IntellijPluginService.DEFAULT_STATE.getPrefixes()
            }
        }))
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
