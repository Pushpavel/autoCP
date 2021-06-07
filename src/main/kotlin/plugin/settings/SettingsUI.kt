package plugin.settings

import com.intellij.ui.JBSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.panel
import java.awt.BorderLayout

class SettingsUI : JBPanel<SettingsUI>(BorderLayout()) {

    private val model = SettingsModel()

    private val mainPanelContent by lazy { LanguagePanelUI(model.languagePanelModel) }

    private val mainPanel = JBPanelWithEmptyText().withEmptyText("This is an Empty Text")

    private val sideList = JBList(model)
        .also {
            it.selectionModel = model.sideListSelectionModel
            it.addListSelectionListener { _ ->
                // adding and removing of mainPanelContent
                mainPanel.removeAll()

                // model.languagePanelModel.getIndex() is not yet updated by SettingsModel
                // this is because list selection listeners are called in the opposite order they are added
                if (model.languagePanelModel.getIndex() == null && it.selectedIndex != -1)
                    mainPanel.add(mainPanelContent.component)

                // updates ui
                mainPanel.revalidate()
                mainPanel.repaint()
            }
        }
        .let {
            ToolbarDecorator
                .createDecorator(it)
                .createPanel()
        }


    init {
        add(panel {
            row("Preferred Language:") {
                comboBox(model, { null }, {})
            }
        }, BorderLayout.NORTH)

        add(JBSplitter(0.3F).apply {
            firstComponent = sideList
            secondComponent = mainPanel
        }, BorderLayout.CENTER)


    }
}