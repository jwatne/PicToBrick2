package pictobrick.service.generators;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

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

    // ############################## xml.html
    // ---------->all:head
    // ---------->all without index: from menu_start till project_end
    /** All XML. */
    private static final String XML_ALL = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_10")
            + "</strong><br />\r\n</p>\r\n<p>\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_18")
            + "\r\n</p>\r\n<p>\r\n<a href=\"mosaic.xml\">\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_37")
            + "\r\n</a>\r\n</p>";
    // ---------->all:end

    // ############################## mosaic.xml
    /** XML document start 1. */
    private static final String XML_DOCUMENT_START1 = "<?xml version=\""
            + "1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
            + "<project name=\"";
    // ---------->projectname
    /** XMLdocument start 2. */
    private static final String XML_DOCUMENT_START2 = "\">";
    /** XML document row start. */
    private static final String XML_DOCUMENT_ROW_START = "<row>\r\n";
    /** XML document row start. */
    private static final String XML_DOCUMENT_ELEMENT_START = "<unit>\r\n"
            + "<element>\r\n";
    // ---------->element
    /** XML document element. */
    private static final String XML_DOCUMENT_ELEMENT_CENTER = "\r\n"
            + "</element>\r\n<color>\r\n";
    // ---------->color
    /** XML document end. */
    private static final String XML_DOCUMENT_ELEMENT_END = "\r\n</color>\r\n"
            + "</unit>\r\n";
    /** XML document element empty. */
    private static final String XML_DOCUMENT_ELEMENT_EMPTY = "<unit />\r\n";
    /** XML document row end. */
    private static final String XML_DOCUMENT_ROW_END = "</row>\r\n";
    /** XML document end. */
    private static final String XML_DOCUMENT_END = "</project>\r\n";

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
        error = generateInstructions(generateGraphics, configuration, material,
                instruction, xml, error);
        // xml.html
        error = generateXml(generateGraphics, configuration, material,
                instruction, xml, error);
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

    private String generateXml(final boolean generateGraphics,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final String initialError) {
        String error = initialError;
        final DataProcessor dataProcessing = common.getDataProcessing();

        if (xml) {
            List<List<Vector<String>>> mosaicMatrix = dataProcessing
                    .getMosaic();
            final StringBuilder xmlString = common.getCommonDocumentStart(
                    generateGraphics, configuration, material, instruction,
                    xml);
            xmlString.append(XML_ALL);
            xmlString.append(FileGenerationCommon.END);

            if (!(dataProcessing.generateUTFOutput(xmlString.toString(),
                    "xml.html", true))) {
                error = error + "xml.html " + FileGenerationCommon
                        .getTextbundle().getString("output_outputFiles_38")
                        + ".\n\r";
            }

            // mosaic.xml
            final StringBuilder xmldocumentString = new StringBuilder();
            xmldocumentString.append(XML_DOCUMENT_START1);
            xmldocumentString.append(common.getProject());
            xmldocumentString.append(XML_DOCUMENT_START2);
            mosaicMatrix = dataProcessing.getMosaic();
            common.setPercent(0);
            common.setReferenceValue(FileGenerationCommon.HALF_TILE);
            common.setReferenceValue((dataProcessing.getMosaicHeight()
                    * dataProcessing.getMosaicWidth())
                    / ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT);

            for (int row = 0; row < dataProcessing.getMosaicHeight(); row++) {
                xmldocumentString.append(XML_DOCUMENT_ROW_START);

                for (int column = 0; column < dataProcessing
                        .getMosaicWidth(); column++) {
                    if (mosaicMatrix.get(row).get(column).isEmpty()) {
                        xmldocumentString.append(XML_DOCUMENT_ELEMENT_EMPTY);
                    } else {
                        xmldocumentString.append(XML_DOCUMENT_ELEMENT_START);
                        xmldocumentString.append(
                                mosaicMatrix.get(row).get(column).elementAt(0));
                        xmldocumentString.append(XML_DOCUMENT_ELEMENT_CENTER);
                        xmldocumentString.append(
                                mosaicMatrix.get(row).get(column).elementAt(1));
                        xmldocumentString.append(XML_DOCUMENT_ELEMENT_END);
                    }

                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                int percent = common.getPercent();
                                percent++;
                                common.setPercent(percent);
                                dataProcessing.refreshProgressBarOutputFiles(
                                        percent, ProgressBarsOutputFiles.XML);
                            }
                        });
                    } catch (final Exception e) {
                    }
                }

                xmldocumentString.append(XML_DOCUMENT_ROW_END);
            }

            xmldocumentString.append(XML_DOCUMENT_END);

            if (!(dataProcessing.generateUTFOutput(xmldocumentString.toString(),
                    "mosaic.xml", true))) {
                error = error + "mosaic.xml " + FileGenerationCommon
                        .getTextbundle().getString("output_outputFiles_38")
                        + ".\n\r";
            }
        }
        return error;
    }

    private String generateInstructions(final boolean generateGraphics,
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
