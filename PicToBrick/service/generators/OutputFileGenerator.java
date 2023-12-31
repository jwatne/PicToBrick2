package pictobrick.service.generators;

import java.util.Enumeration;

import javax.swing.SwingUtilities;

import pictobrick.service.DataProcessor;
import pictobrick.ui.ProgressBarsAlgorithms;
import pictobrick.ui.ProgressBarsOutputFiles;

/**
 * Build all output files.
 *
 * @author Tobias Reichling
 */
public class OutputFileGenerator {

    // ############################## all without index.html
    /** Start menu. */
    static final String START_MENU = "<div id=\"menu\">\r\n"
            + "<ul>\r\n<li>\r\n<a href=\"../index.html\">\r\n"
            + FileGenerationCommon.getTextbundle().getString(
                    "output_outputFiles_3")
            + "\r\n</a>\r\n</li>\r\n";
    /** Graphic menu. */
    static final String GRAPHIC_MENU = "<li>\r\n"
            + "<a href=\"grafic.html\">\r\n" + FileGenerationCommon
                    .getTextbundle().getString("output_outputFiles_4")
            + "\r\n</a>\r\n</li>\r\n";
    /** Configuration menu. */
    static final String CONFIGURATION_MENU = "<li>\r\n"
            + "<a href=\"configuration.html\">\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_5")
            + "\r\n</a>\r\n<ul>\r\n<li>\r\n<a href=\"colors.html\">\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_6")
            + "\r\n</a>\r\n</li>\r\n<li>\r\n<a href=\"elements.html\">\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_7")
            + "\r\n</a>\r\n</li>\r\n</ul>\r\n</li>\r\n";
    /** Material menu. */
    static final String MATERIAL_MENU = "<li>\r\n"
            + "<a href=\"billofmaterial.html\">\r\n" + FileGenerationCommon
                    .getTextbundle().getString("output_outputFiles_8")
            + "\r\n</a>\r\n</li>\r\n";
    /** Instruction menu. */
    static final String INSTRUCTION_MENU = "<li>\r\n"
            + "<a href=\"buildinginstruction.html\">\r\n" + FileGenerationCommon
                    .getTextbundle().getString("output_outputFiles_9")
            + "\r\n</a>\r\n</li>\r\n";
    /** XML menu. */
    static final String XML_MENU = "<li>\r\n<a href=\"xml.html\">\r\n"
            + FileGenerationCommon.getTextbundle().getString(
                    "output_outputFiles_10")
            + "\r\n</a>\r\n</li>\r\n";
    /** Additional menu. */
    static final String ADDITIONAL_MENU = "<li>\r\n"
            + "<a href=\"additional.html\">\r\n" + FileGenerationCommon
                    .getTextbundle().getString("output_outputFiles_11")
            + "\r\n</a>\r\n</li>\r\n";
    // ---------->all:projectname
    /** Project end. */
    static final String PROJECT_END = "</strong>\r\n</p>\r\n";

    // ############################## additional.html
    // ---------->all:head
    // ---------->all without index: from menu_start till project_end
    /** Additional start. */
    private static final String ADDITIONAL_START = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle().getString(
                    "output_outputFiles_11")
            + "</strong><br />\r\n</p>\r\n";
    // ---------->inelementation
    /** Additional cell end. */
    private static final String ADDITIONAL_CELL_END = "\r\n<br />\r\n";
    // ---------->all:end

    /** Common file generation code. */
    private FileGenerationCommon common;

    /**
     * Constructor.
     *
     * @param processor Data processor.
     *
     * @author Tobias Reichling
     */
    public OutputFileGenerator(final DataProcessor processor) {
        this.common = new FileGenerationCommon(processor);
    }

    /**
     * Returns common file generation code.
     *
     * @return common file generation code.
     */
    public FileGenerationCommon getCommon() {
        return common;
    }

    /**
     * Generates documents and returns a message.
     *
     * @author Tobias Reichling
     * @param generateGraphics true, if document will be generated
     * @param configuration    true, if document will be generated
     * @param material         true, if document will be generated
     * @param instruction      true, if document will be generated
     * @param xml              true, if document will be generated
     * @param infos
     * @return message error
     */
    public String generateDocuments(final boolean generateGraphics,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final Enumeration<String> infos) {
        String error = "";
        // index.html
        error = (new IndexGenerator(common)).generateIndex(generateGraphics,
                configuration, material, instruction, xml, error);
        // grafic.html
        error = (new GraphicsGenerator(common)).generateGraphics(
                generateGraphics, configuration, material, instruction, xml,
                error);
        // configuration.html
        error = (new ConfigurationGenerator(common)).generateConfiguration(
                generateGraphics, configuration, material, instruction, xml,
                error);
        // billofmaterial.html
        error = (new BillOfMaterialsGenerator(common)).generateBillOfMaterial(
                generateGraphics, configuration, material, instruction, xml,
                error);
        // instruction.html
        error = (new InstructionsGenerator(common)).generateInstructions(
                generateGraphics, configuration, material, instruction, xml,
                error);
        // xml.html
        error = (new XmlGenerator(common)).generateXml(generateGraphics,
                configuration, material, instruction, xml, error);
        // additional.html
        error = generateAdditional(generateGraphics, configuration, material,
                instruction, xml, infos, error);

        return error;
    }

    private String generateAdditional(final boolean generateGraphics,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final Enumeration<String> infos, final String initialError) {
        String error = initialError;
        final DataProcessor dataProcessing = common.getDataProcessing();

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    dataProcessing.refreshProgressBarOutputFiles(
                            ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT,
                            ProgressBarsOutputFiles.MISCELLANEOUS);
                }
            });
        } catch (final Exception e) {
        }

        final StringBuilder additionalString = common.getCommonDocumentStart(
                generateGraphics, configuration, material, instruction, xml);
        additionalString.append(ADDITIONAL_START);

        if (!infos.hasMoreElements()) {
            additionalString.append(FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_43"));
            additionalString.append(ADDITIONAL_CELL_END);
        } else {
            while (infos.hasMoreElements()) {
                additionalString.append((String) infos.nextElement());
                additionalString.append(ADDITIONAL_CELL_END);
            }
        }

        additionalString.append(FileGenerationCommon.END);

        if (!(dataProcessing.generateUTFOutput(additionalString.toString(),
                "additional.html", true))) {
            error = error + "additional.html " + FileGenerationCommon
                    .getTextbundle().getString("output_outputFiles_38")
                    + ".\n\r";
        }

        return error;
    }

}
