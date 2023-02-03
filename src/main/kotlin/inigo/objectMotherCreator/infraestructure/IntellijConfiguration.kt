package inigo.objectMotherCreator.infraestructure;

import com.intellij.openapi.options.Configurable
import java.awt.FlowLayout
import javax.swing.*


class IntellijConfiguration: Configurable {
    private val fakerTextField: JTextField = JTextField()

    override fun createComponent(): JComponent {
        val main = JPanel()
        val label = JLabel("Faker imported class")
        main.layout = BoxLayout(main, BoxLayout.Y_AXIS)
        val pack = JPanel(FlowLayout(FlowLayout.LEFT))
        fakerTextField.text = IntellijPluginService.getAppInstance().state.state
        pack.add(label)
        pack.add(fakerTextField)
        main.add(pack)
        /*val border = JPanel(FlowLayout(FlowLayout.LEFT))
                val main = JPanel()
                main.setLayout(BoxLayout(main, BoxLayout.Y_AXIS))
                border.add(main, BorderLayout.NORTH)
                fakerTextField.text = IntellijPluginService.getAppInstance().state.state

                var panel = JPanel(FlowLayout(FlowLayout.LEFT))
                panel.add(fakerTextField)
                val cleanHistoryBtn = JButton()
                cleanHistoryBtn.setText("Clear History")
                panel.add(cleanHistoryBtn)
                main.add(panel)

                panel = JPanel(FlowLayout(FlowLayout.LEFT))
                panel.add(Label("Vamooooooos"))
                main.add(panel)

                panel = JPanel(FlowLayout(FlowLayout.LEFT))
                panel.add(Label("SFTP settings"))
                main.add(panel)*/

        reset()
        return main
    }

    override fun isModified(): Boolean {
        return fakerTextField.text != IntellijPluginService.getAppInstance().state.state;
    }

    override fun apply() {
        IntellijPluginService.getAppInstance().loadState(PluginState(fakerTextField.text))
    }

    override fun getDisplayName(): String {
        return "Object Mother Creator"
    }
}
