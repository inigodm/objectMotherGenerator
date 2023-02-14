package inigo.objectMotherCreator.infraestructure;

import com.intellij.openapi.options.Configurable
import org.jdesktop.swingx.VerticalLayout
import java.awt.FlowLayout
import javax.swing.*


class IntellijConfiguration: Configurable {
    private val fakerTextField: JTextField = JTextField()
    private val methodPrefixTextField: JTextField = JTextField()

    override fun createComponent(): JComponent {
        val main = JPanel()
        main.layout = VerticalLayout()
        main.add(buildFakerClassnameComponent())
        main.add(buildMethodPrefixComponent())
        reset()
        return main
    }

    private fun buildFakerClassnameComponent(): JPanel {
        return textFieldWithLabel(
            "Faker imported class",
            IntellijPluginService.getAppInstance().state.fakerClassname,
            fakerTextField)
    }

    private fun buildMethodPrefixComponent(): JPanel {
        return textFieldWithLabel(
            "Prefix of builder functions",
            IntellijPluginService.getAppInstance().state.methodsPrefix,
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

    override fun isModified(): Boolean {
        return fakerTextField.text != IntellijPluginService.getAppInstance().state.fakerClassname ||
        methodPrefixTextField.text != IntellijPluginService.getAppInstance().state.methodsPrefix
    }

    override fun apply() {
        IntellijPluginService.getAppInstance().loadState(PluginState(fakerTextField.text, methodPrefixTextField.text))
    }

    override fun getDisplayName(): String {
        return "Object Mother Creator"
    }
}
