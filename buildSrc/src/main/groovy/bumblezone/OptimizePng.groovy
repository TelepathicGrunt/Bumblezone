package bumblezone

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileType
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.work.ChangeType
import org.gradle.work.Incremental
import org.gradle.work.InputChanges

import javax.inject.Inject

abstract class OptimizePng extends DefaultTask {

    @InputFiles
    @Incremental
    final ConfigurableFileCollection inputFiles

    @Inject
    OptimizePng(ObjectFactory objects) {
        this.inputFiles = objects.fileCollection()
    }

    @Inject
    protected abstract ExecOperations getExecOperations()

    @TaskAction
    void execute(InputChanges inputChanges) {
        for (def fileChange : inputChanges.getFileChanges(inputFiles)) {
            if (fileChange.changeType == ChangeType.REMOVED || fileChange.fileType != FileType.FILE) {
                //Don't care about files that were removed
                continue
            }

            def file = fileChange.file
            //Minimize/optimize all png files, requires oxipng on the PATH
            // Credits: BrainStone
            execOperations.exec { spec -> spec.commandLine('oxipng',
                    "-o", "max",
                    "-i", "off",
                    "--strip", "all",
                    "-a",
                    "-f", "0,1,2,3,4,5,6,7,8,9",
                    "--zopfli",
                    "--zi", "50",
                    "--ziwi", "5",
                    file) }
        }
    }
}