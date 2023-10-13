package pictobrick.service.generators;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.model.ColorObject;
import pictobrick.model.ElementObject;
import pictobrick.model.Mosaic;
import pictobrick.service.DataProcessor;
import pictobrick.ui.ProgressBarsAlgorithms;
import pictobrick.ui.ProgressBarsOutputFiles;

/**
 * Build all output files.
 *
 * @author Tobias Reichling
 */
public class OutputFileGenerator {
    /** Divisor for calculating referenceValue. */
    private static final int REFERENCE_VALUE_DIVISOR = 10;
    /** Length and width of 1x1 tile in pixels. */
    private static final int TILE_SIZE = 8;
    /** 1/2 length and width of 1x1 tile in pixels; used to get midpoint. */
    private static final int HALF_TILE = 4;

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

    // ############################## configuration.html
    // ---------->all:head
    // ---------->all without index: from menu_start till project_end
    /** Configuration start. */
    private static final String CONFIGURATION_START = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle().getString(
                    "output_outputFiles_5")
            + "</strong><br />\r\n</p>\r\n";
    /** Configuration name. */
    private static final String CONFIGURATION_NAME = "<p>\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_23")
            + ": ";
    // ---------->configuration name
    /** Configuration basis name. */
    private static final String CONFIGURATION_BASIS_NAME = "\r\n<br />\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_24")
            + ": ";
    // ---------->basis name
    /** Configuration basis width. */
    private static final String CONFIGURATION_BASIS_WIDTH = "\r\n<br />\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_25")
            + ": ";
    // ---------->basis width
    /** Configuration basis height. */
    private static final String CONFIGURATION_BASIS_HEIGHT = "\r\n<br />\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_26")
            + ": ";
    // ---------->basis height
    /** Configuration basis width in mm. */
    private static final String CONFIGURATION_BASIS_WIDTH_MM = "\r\n<br />\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_27")
            + ": ";
    // ---------->basis width mm
    /** Configuration end. */
    private static final String CONFIGURATION_END = "\r\n</p>\r\n<p>\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_28")
            + " <a href=\"colors.html\">"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_6")
            + "</a> "
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_29")
            + " <a href=\"elements.html\">"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_7")
            + "</a> " + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_30")
            + ".\r\n</p>\r\n";
    // ---------->all:end

    // ############################## elements.html
    // ---------->all:head
    // ---------->all without index: from menu_start till project_end
    /** Element start. */
    private static final String ELEMENT_START = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_7")
            + "</strong>\r\n<br />\r\n</p>\r\n<table>\r\n";
    /** Element cell start. */
    private static final String ELEMENT_CELL_START = "<tr>\r\n<td>\r\n";
    /** Element name. */
    private static final String ELEMENT_NAME = "\r\n</td>\r\n<td>\r\n<strong>"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_31")
            + ":</strong> ";
    // ---------->name
    /** Element width. */
    private static final String ELEMENT_WIDTH = "\r\n<br />\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_32")
            + ": ";
    // ---------->width
    /** Element height. */
    private static final String ELEMENT_HEIGHT = "\r\n<br />\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_33")
            + ": ";
    // ---------->height
    /** Element stability. */
    private static final String ELEMENT_STABILITY = "\r\n<br />\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_34")
            + ": ";
    // ---------->stability
    /** Element costs. */
    private static final String ELEMENT_COSTS = "\r\n<br />\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_35")
            + ": ";
    // ---------->costs
    /** Element cell end. */
    private static final String ELEMENT_CELL_END = "\r\n</td>\r\n</tr>\r\n";
    /** Element end. */
    private static final String ELEMENT_END = "</table>\r\n";
    // ---------->all:end

    // ############################## colors.html
    // ---------->all:head
    // ---------->all without index: from menu_start till project_end
    /** Color start. */
    private static final String COLOR_START = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_6")
            + "</strong><br />\r\n</p>\r\n<table>\r\n";
    /** Color cell start. */
    private static final String COLOR_CELL_START = "<tr>\r\n<td>\r\n"
            + "<img src=\"";
    // ---------->file name image
    /** Color image. */
    private static final String COLOR_IMAGE = ".jpg\" width=\"300\""
            + " height=\"50\" alt=\"color\" />\r\n</td>\r\n<td>\r\n";
    // ---------->color name
    /** Color info. */
    private static final String COLOR_INFO = "\r\n<br />\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_36")
            + ": ";
    // ---------->rgb
    /** Color cell end. */
    private static final String COLOR_CELL_END = "\r\n</td>\r\n</tr>\r\n";
    /** Color end. */
    private static final String COLOR_END = "</table>\r\n";
    // ---------->all:end

    // ############################## billofmaterial.html
    // ---------->all:head
    // ---------->all without index: from menu_start till project_end
    /** Material start. */
    private static final String MATERIAL_START = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_8")
            + "</strong><br />\r\n</p>\r\n<table>\r\n";
    /** Material row start. */
    private static final String MATERIAL_ROW_START = "<tr>\r\n<td>\r\n";
    // ---------->color
    /** Material between. */
    private static final String MATERIAL_BETWEEN = "\r\n</td>\r\n<td>\r\n";
    // ---------->element
    // -----------material_between
    // ---------->quantity + " piece"
    /** Material row end. */
    private static final String MATERIAL_ROW_END = "\r\n</td>\r\n</tr>\r\n";
    /** Material end. */
    private static final String MATERIAL_END = "</table>\r\n";
    // ---------->all:end

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

    /** Normal color. */
    private final Color colorNormal = new Color(59, 45, 167);
    /** Light color. */
    private final Color colorLight = new Color(159, 145, 255);
    /** Dark color. */
    private final Color colorDark = new Color(0, 0, 67);
    /** White. */
    private final Color colorWhite = new Color(255, 255, 255);
    /** Percent generated. */
    private int percent = 0;
    /** Reference value. */
    private int referenceValue = 0;
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
        error = generateConfiguration(generateGraphics, configuration, material,
                instruction, xml, error);
        // billofmaterial.html
        error = generateBillOfMaterial(generateGraphics, configuration,
                material, instruction, xml, error);
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
            percent = 0;
            referenceValue = (dataProcessing.getMosaicHeight()
                    * dataProcessing.getMosaicWidth())
                    / ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT;

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
                                percent++;
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
            percent = 0;
            referenceValue = (dataProcessing.getMosaicHeight()
                    * dataProcessing.getMosaicWidth())
                    / ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT;

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
                                percent++;
                                dataProcessing.refreshProgressBarOutputFiles(
                                        percent, HALF_TILE);
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

    private String generateBillOfMaterial(final boolean generateGraphics,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final String initialError) {
        String error = initialError;
        final DataProcessor dataProcessing = common.getDataProcessing();

        if (material) {
            final StringBuilder billofmaterialString = common
                    .getCommonDocumentStart(generateGraphics, configuration,
                            material, instruction, xml);
            // END COMMON CODE
            billofmaterialString.append(MATERIAL_START);
            Enumeration<ColorObject> colorsEnumeration;
            Enumeration<ElementObject> elementsEnumeration;
            final var materialHash = initMaterialHash();

            // count elements
            final List<List<Vector<String>>> mosaicMatrix = dataProcessing
                    .getMosaic();
            String elementName;
            String colorName;
            int quantity;
            percent = 0;
            referenceValue = (dataProcessing.getMosaicHeight()
                    * dataProcessing.getMosaicWidth())
                    / ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT;

            for (int row = 0; row < dataProcessing.getMosaicHeight(); row++) {
                for (int column = 0; column < dataProcessing
                        .getMosaicWidth(); column++) {
                    if (!mosaicMatrix.get(row).get(column).isEmpty()) {
                        elementName = mosaicMatrix.get(row).get(column)
                                .elementAt(0);
                        colorName = mosaicMatrix.get(row).get(column)
                                .elementAt(1);
                        quantity = materialHash.get(colorName).get(elementName);
                        materialHash.get(colorName).put(elementName,
                                (quantity + 1));
                    }

                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                percent++;
                                dataProcessing.refreshProgressBarOutputFiles(
                                        percent,
                                        ProgressBarsOutputFiles.MATERIAL);
                            }
                        });
                    } catch (final Exception e) {
                    }
                }
            }

            // output
            colorsEnumeration = dataProcessing.getCurrentConfiguration()
                    .getAllColors();

            while (colorsEnumeration.hasMoreElements()) {
                colorName = ((ColorObject) colorsEnumeration.nextElement())
                        .getName();
                elementsEnumeration = dataProcessing.getCurrentConfiguration()
                        .getAllElements();

                while (elementsEnumeration.hasMoreElements()) {
                    elementName = elementsEnumeration.nextElement().getName();
                    quantity = materialHash.get(colorName).get(elementName);

                    if (quantity > 0) {
                        billofmaterialString.append(MATERIAL_ROW_START);
                        billofmaterialString.append(colorName);
                        billofmaterialString.append(MATERIAL_BETWEEN);
                        billofmaterialString.append(elementName);
                        billofmaterialString.append(MATERIAL_BETWEEN);
                        billofmaterialString.append(quantity + " "
                                + FileGenerationCommon.getTextbundle()
                                        .getString("output_outputFiles_39")
                                + "");
                        billofmaterialString.append(MATERIAL_ROW_END);
                    }
                }
            }

            billofmaterialString.append(MATERIAL_END);
            billofmaterialString.append(FileGenerationCommon.END);

            if (!(dataProcessing.generateUTFOutput(
                    billofmaterialString.toString(), "billofmaterial.html",
                    true))) {
                error = error + "billofmaterial.html " + FileGenerationCommon
                        .getTextbundle().getString("output_outputFiles_38")
                        + ".\n\r";
            }
        }
        return error;
    }

    private String generateConfiguration(final boolean generateGraphics,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final String initialError) {
        String error = initialError;
        final DataProcessor dataProcessing = common.getDataProcessing();

        if (configuration) {
            error = getConfigurationHtml(generateGraphics, configuration,
                    material, instruction, xml, error);

            // elements.html
            int width = dataProcessing.getCurrentConfiguration()
                    .getBasisWidth();
            int height = dataProcessing.getCurrentConfiguration()
                    .getBasisHeight();

            while (width < Mosaic.MIN_WIDTH_OR_HEIGHT
                    || height < Mosaic.MIN_WIDTH_OR_HEIGHT) {
                width = width * 2;
                height = height * 2;
            }

            final StringBuilder elementsString = common.getCommonDocumentStart(
                    generateGraphics, configuration, material, instruction,
                    xml);
            elementsString.append(ELEMENT_START);
            percent = 0;
            referenceValue = (dataProcessing.getCurrentConfiguration()
                    .getQuantityColorsAndElements()) / REFERENCE_VALUE_DIVISOR;

            if (referenceValue == 0) {
                referenceValue = 1;
            }

            final int flag1 = processElementsEnumeration(width, height,
                    elementsString);

            elementsString.append(ELEMENT_END);
            elementsString.append(FileGenerationCommon.END);

            if (!(dataProcessing.generateUTFOutput(elementsString.toString(),
                    "elements.html", true))) {
                error = error + "elements.html " + FileGenerationCommon
                        .getTextbundle().getString("output_outputFiles_38")
                        + ".\n\r";
            }

            // colors.html
            error = generateColorsHtml(generateGraphics, configuration,
                    material, instruction, xml, error, flag1);
        }

        return error;
    }

    private int processElementsEnumeration(final int width, final int height,
            final StringBuilder elementsString) {
        int counter = 0;
        int flag1 = 0;

        ElementObject element;
        boolean[][] matrix;
        boolean[][] matrixNew;
        final DataProcessor dataProcessing = common.getDataProcessing();

        for (final var elementsEnumeration = dataProcessing
                .getCurrentConfiguration().getAllElements(); elementsEnumeration
                        .hasMoreElements();) {
            counter++;
            element = (ElementObject) elementsEnumeration.nextElement();
            elementsString.append(ELEMENT_CELL_START);
            matrix = element.getMatrix();
            matrixNew = new boolean[element.getHeight() + 2][element.getWidth()
                    + 2];

            // add a NULL-border to the element matrix
            for (int row = 0; row < (element.getHeight() + 2); row++) {
                for (int column = 0; column < (element.getWidth()
                        + 2); column++) {
                    if (row == 0 || column == 0) {
                        matrixNew[row][column] = false;
                    } else if (row == element.getHeight() + 1
                            || column == element.getWidth() + 1) {
                        matrixNew[row][column] = false;
                    } else {
                        matrixNew[row][column] = matrix[row - 1][column - 1];
                    }
                }
            }

            processElementImage(width, height, counter, element, matrixNew);

            if (flag1 % referenceValue == 0) {
                // refresh progress bars -> gui thread
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            percent = percent + REFERENCE_VALUE_DIVISOR;
                            dataProcessing
                                    .refreshProgressBarOutputFiles(percent, 2);
                        }
                    });
                } catch (final Exception e) {
                }
            }

            flag1++;
            elementsString.append("<img src=\"" + "element_" + counter
                    + ".jpg\" width=\"" + width * TILE_SIZE + "\" height=\""
                    + height * element.getHeight() + "\" alt=\"mosaic\" />");
            elementsString.append(ELEMENT_NAME);
            elementsString.append(element.getName());
            elementsString.append(ELEMENT_WIDTH);
            elementsString.append(element.getWidth());
            elementsString.append(ELEMENT_HEIGHT);
            elementsString.append(element.getHeight());
            elementsString.append(ELEMENT_STABILITY);
            elementsString.append(element.getStability());
            elementsString.append(ELEMENT_COSTS);
            elementsString.append(element.getCosts());
            elementsString.append(ELEMENT_CELL_END);
        }

        return flag1;
    }

    private void processElementImage(final int width, final int height,
            final int counter, final ElementObject element,
            final boolean[][] matrixNew) {
        BufferedImage elementImage;
        final DataProcessor dataProcessing = common.getDataProcessing();
        elementImage = new BufferedImage(width * TILE_SIZE,
                height * element.getHeight(), BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = elementImage.createGraphics();
        g.setColor(colorWhite);
        g.fillRect(0, 0, width * TILE_SIZE, height * element.getHeight());

        for (int row = 0; row < (element.getHeight() + 2); row++) {
            for (int column = 0; column < (element.getWidth() + 2); column++) {
                if (matrixNew[row][column]) {
                    g.setColor(colorNormal);
                    g.fillRect(width * (column - 1), height * (row - 1), width,
                            height);
                    g.setColor(colorLight);

                    // border left
                    if (!matrixNew[row][column - 1]) {
                        g.fillRect(width * (column - 1), height * (row - 1) + 2,
                                2, height - HALF_TILE);
                    }

                    // border top
                    if (!matrixNew[row - 1][column]) {
                        g.fillRect(width * (column - 1) + 2, height * (row - 1),
                                width - HALF_TILE, 2);
                    }
                    // corner top left
                    if (!(matrixNew[row][column - 1]
                            && matrixNew[row - 1][column]
                            && matrixNew[row - 1][column - 1])) {
                        g.fillRect(width * (column - 1), height * (row - 1), 2,
                                2);
                    }
                    // corner bottom left
                    if (!(matrixNew[row][column - 1]
                            && matrixNew[row + 1][column]
                            && matrixNew[row + 1][column - 1])) {
                        if (matrixNew[row][column - 1]) {
                            g.setColor(colorDark);
                        }
                        g.fillRect(width * (column - 1),
                                height * (row - 1) + height - 2, 2, 2);
                        g.setColor(colorLight);
                    }
                    g.setColor(colorDark);
                    // border right
                    if (!matrixNew[row][column + 1]) {
                        g.fillRect(width * (column - 1) + width - 2,
                                height * (row - 1) + 2, 2, height - HALF_TILE);
                    }
                    // border bottom
                    if (!matrixNew[row + 1][column]) {
                        g.fillRect(width * (column - 1) + 2,
                                height * (row - 1) + height - 2,
                                width - HALF_TILE, 2);
                    }
                    // corner top right
                    if (!(matrixNew[row][column + 1]
                            && matrixNew[row - 1][column]
                            && matrixNew[row - 1][column + 1])) {
                        if (matrixNew[row][column + 1]) {
                            g.setColor(colorLight);
                        }

                        g.fillRect(width * (column - 1) + width - 2,
                                height * (row - 1), 2, 2);
                        g.setColor(colorDark);
                    }

                    // corner bottom right
                    if (!(matrixNew[row][column + 1]
                            && matrixNew[row + 1][column]
                            && matrixNew[row + 1][column + 1])) {
                        g.fillRect(width * (column - 1) + width - 2,
                                height * (row - 1) + height - 2, 2, 2);
                    }
                }
            }
        }

        dataProcessing.generateImageOutput(elementImage, "element_" + counter);
    }

    private String generateColorsHtml(final boolean generateGraphics,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final String initialError, final int initialFlag1) {
        final DataProcessor dataProcessing = common.getDataProcessing();
        int flag1 = initialFlag1;
        String error = initialError;
        final StringBuilder colorsString = common.getCommonDocumentStart(
                generateGraphics, configuration, material, instruction, xml);
        colorsString.append(COLOR_START);
        ColorObject color = new ColorObject();
        int counter2 = 0;

        for (final var colorsEnumeration = dataProcessing
                .getCurrentConfiguration().getAllColors(); colorsEnumeration
                        .hasMoreElements();) {
            counter2++;
            color = (ColorObject) colorsEnumeration.nextElement();
            dataProcessing.generateColorOutput(color.getRGB(),
                    "color_" + counter2);

            if (flag1 % referenceValue == 0) {
                // refresh progress bars -> gui thread
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            percent = percent + REFERENCE_VALUE_DIVISOR;
                            dataProcessing
                                    .refreshProgressBarOutputFiles(percent, 2);
                        }
                    });
                } catch (final Exception e) {
                }
            }

            flag1++;
            colorsString.append(COLOR_CELL_START);
            colorsString.append("color_" + counter2);
            colorsString.append(COLOR_IMAGE);
            colorsString.append(color.getName());
            colorsString.append(COLOR_INFO);
            colorsString.append(
                    color.getRGB().getRed() + ", " + color.getRGB().getGreen()
                            + ", " + color.getRGB().getBlue());
            colorsString.append(COLOR_CELL_END);
        }

        colorsString.append(COLOR_END);
        colorsString.append(FileGenerationCommon.END);

        if (!(dataProcessing.generateUTFOutput(colorsString.toString(),
                "colors.html", true))) {
            error = error + "colors.html " + FileGenerationCommon
                    .getTextbundle().getString("output_outputFiles_38")
                    + ".\n\r";
        }
        return error;
    }

    private String getConfigurationHtml(final boolean generateGraphics,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final String initialError) {
        String error = initialError;
        final DataProcessor dataProcessing = common.getDataProcessing();
        final StringBuilder configurationString = common.getCommonDocumentStart(
                generateGraphics, configuration, material, instruction, xml);
        configurationString.append(CONFIGURATION_START);
        configurationString.append(CONFIGURATION_NAME);
        configurationString
                .append(dataProcessing.getCurrentConfiguration().getName());
        configurationString.append(CONFIGURATION_BASIS_NAME);
        configurationString.append(
                dataProcessing.getCurrentConfiguration().getBasisName());
        configurationString.append(CONFIGURATION_BASIS_WIDTH);
        configurationString.append(
                dataProcessing.getCurrentConfiguration().getBasisWidth());
        configurationString.append(CONFIGURATION_BASIS_HEIGHT);
        configurationString.append(
                dataProcessing.getCurrentConfiguration().getBasisHeight());
        configurationString.append(CONFIGURATION_BASIS_WIDTH_MM);
        final int widthMM = (int) (100
                * (dataProcessing.getCurrentConfiguration().getBasisWidthMM()));
        configurationString
                .append((widthMM / ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT)
                        + FileGenerationCommon.getTextbundle()
                                .getString("output_decimalPoint")
                        + (widthMM
                                % ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT));
        configurationString.append(CONFIGURATION_END);
        configurationString.append(FileGenerationCommon.END);

        if (!(dataProcessing.generateUTFOutput(configurationString.toString(),
                "configuration.html", true))) {
            error = error
                    + "configuration.html " + FileGenerationCommon
                            .getTextbundle().getString("output_outputFiles_38")
                    + ".\n\r";
        }
        return error;
    }

    private Hashtable<String, Hashtable<String, Integer>> initMaterialHash() {
        final var mHash = new Hashtable<String, Hashtable<String, Integer>>();
        final DataProcessor dataProcessing = common.getDataProcessing();
        final Enumeration<ColorObject> colorsEnumeration = dataProcessing
                .getCurrentConfiguration().getAllColors();
        Enumeration<ElementObject> elementsEnumeration;

        while (colorsEnumeration.hasMoreElements()) {
            elementsEnumeration = dataProcessing.getCurrentConfiguration()
                    .getAllElements();
            final Hashtable<String, Integer> elementenHash = new Hashtable<>();

            while (elementsEnumeration.hasMoreElements()) {
                elementenHash
                        .put(((ElementObject) elementsEnumeration.nextElement())
                                .getName(), 0);
            }

            mHash.put(((ColorObject) colorsEnumeration.nextElement()).getName(),
                    elementenHash);
        }

        return mHash;
    }
}
