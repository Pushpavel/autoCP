package settings.langSettings.ui.dialogs.buildConfigDialog

import com.intellij.ide.macro.MacrosDialog
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.panel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import settings.langSettings.model.BuildConfig
import ui.vvm.swingModels.toPlainDocument
import javax.swing.JComponent

class BuildConfigDialog(buildConfig: BuildConfig, list: List<BuildConfig>) : DialogWrapper(false) {

    private val scope = MainScope()
    private val model = BuildConfigViewModel(buildConfig, list)

    init {
        title = "Edit ${buildConfig.name}"
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            row {
                val nameField = ExtendableTextField(10).apply {
                    document = model.name.toPlainDocument(scope)
                }
                val buildCommandField = ExtendableTextField(50).apply {
                    document = model.buildCommand.toPlainDocument(scope)
                }
                MacrosDialog.addTextFieldExtension(buildCommandField)

                row("Name:") { nameField() }
                row("Build Command:") {
                    buildCommandField(pushX).comment(
                        "This command will be executed to build the executable of your solution code.<br>" +
                                "@input@ will be replaced with \"path/to/input/file\" without quotes<br>" +
                                "@output@ will be replaced with \"path/to/output/file\" without quotes"
                    )
                }
                blockRow { }
            }
        }
    }

    fun showAndGetConfig(): BuildConfig? {
        val confirm = showAndGet()

        // TODO: validate build config
        return if (confirm)
            BuildConfig(model.name.value, model.buildCommand.value)
        else
            null
    }

    override fun dispose() {
        super.dispose()
        scope.cancel()
    }
}