package pictobrick.service.generators;

import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.service.DataProcessor;
import pictobrick.ui.ProgressBarsAlgorithms;

/**
 * Generates instructions page. Code moved from OutputFileGenerator by John
 * Watne 10/2023.
 */
public class InstructionsGenerator {
    // ############################## buildinginstruction.html
    // ---------->all:head
    // ---------->all without index: from menu_start till project_end
    /** Instruction start. */
    private static final String INSTRUCTION_START = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_9")
            + "</strong><br />\r\n</p>\r\n<ul>\r\n";
    /** Instruction cell start. */
    private static final String INSTRUCTION_CELL_START = "<li>\r\n";
    // ---------->row x, column y: name, color
    /** Instruction cell end. */
    private static final String INSTRUCTION_CELL_END = "\r\n</li>\r\n";
    /** Instruction end. */
    private static final String INSTRUCTION_END = "</ul>\r\n";
    // ---------->all:end
    /** Common file generating logic. */
    private final FileGenerationCommon common;

    /**
     * Constructor.
     *
     * @param fileGenerationCommon common file generating logic.
     */
    public InstructionsGenerator(
            final FileGenerationCommon fileGenerationCommon) {
        this.common = fileGenerationCommon;
    }

    /**
     * Generates instructions page if requested.
     *
     * @param generateGraphics <code>true</code> if generating graphics.
     * @param configuration    <code>true</code> if generating configuration.
     * @param material         <code>true</code> if generating bill of
     *                         materials.
     * @param instruction      <code>true</code> if generating instructions.
     * @param xml              <code>true</code> if generating XML.
     * @param initialError     any error text from previous file generation
     *                         steps.
     * @return updated error text.
     */
    String generateInstructions(final boolean generateGraphics,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final String initialError) {
        String error = initialError;
        final DataProcessor dataProcessing = common.getDataProcessing();

        if (instruction) {
            List<List<Vector<String>>> mosaicMatrix = dataProcessing
                    .getMosaic();
            final StringBuilder instructionString = common
                    .getCommonDocumentStart(generateGraphics, configuration,
                            material, instruction, xml);
            instructionString.append(INSTRUCTION_START);
            mosaicMatrix = dataProcessing.getMosaic();
            common.setPercent(0);
            common.setReferenceValue(dataProcessing.getMosaicHeight()
                    * dataProcessing.getMosaicWidth()
                    / ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT);

            for (int row = 0; row < dataProcessing.getMosaicHeight(); row++) {
                for (int column = 0; column < dataProcessing
                        .getMosaicWidth(); column++) {
                    instructionString.append(INSTRUCTION_CELL_START);
                    instructionString.append(FileGenerationCommon
                            .getTextbundle().getString("output_outputFiles_40")
                            + " " + (row + 1) + ", "
                            + FileGenerationCommon.getTextbundle()
                                    .getString("output_outputFiles_41")
                            + " " + (column + 1) + ": ");

                    if (mosaicMatrix.get(row).get(column).isEmpty()) {
                        instructionString.append("("
                                + FileGenerationCommon.getTextbundle()
                                        .getString("output_outputFiles_42")
                                + ")");
                    } else {
                        instructionString.append((String) mosaicMatrix.get(row)
                                .get(column).elementAt(0));
                        instructionString.append(", ");
                        instructionString.append((String) mosaicMatrix.get(row)
                                .get(column).elementAt(1));
                    }

                    instructionString.append(INSTRUCTION_CELL_END);

                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                int percent = common.getPercent();
                                percent++;
                                common.setPercent(percent);
                                dataProcessing.refreshProgressBarOutputFiles(
                                        percent,
                                        FileGenerationCommon.HALF_TILE);
                            }
                        });
                    } catch (final Exception e) {
                    }
                }
            }

            instructionString.append(INSTRUCTION_END);
            instructionString.append(FileGenerationCommon.END);

            if (!(dataProcessing.generateUTFOutput(instructionString.toString(),
                    "buildinginstruction.html", true))) {
                error = error + "buildinginstruction.html "
                        + FileGenerationCommon.getTextbundle()
                                .getString("output_outputFiles_38")
                        + ".\n\r";
            }
        }

        return error;
    }
}
