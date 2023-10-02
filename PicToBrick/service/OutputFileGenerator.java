package pictobrick.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.model.ColorObject;
import pictobrick.model.ElementObject;
import pictobrick.model.Mosaic;
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

    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");

    // ############################## all
    /** Header. */
    private static final String HEADER = """
            <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
            "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
            <title>pictobrick</title>
            <meta http-equiv="content-type"
             content="text/html; charset=utf-8" />
            <style type="text/css">
            #menu {
                left:0;
                top:10;
                width:200px;
                position:absolute;
                padding-left:10px;
                padding-right:10px;
                padding-top:0px;
                margin-left:0;
                margin-top:0;
                margin-right:0;
                font-family:Verdana, Arial;
                font-size:small;
                }
                #content {
                margin-top:0px;
                margin-left:200px;
                margin-right:0px;
                padding-left:10px;
                padding-right:10px;
                padding-top:0px;
                font-family:Verdana, Arial;
                font-size:small;
                }
                #headline {
                font-size:x-large;
                }
                a:link {
                text-decoration:none;
                color:blue;
                }
                a:visited {
                text-decoration:none;
                color:blue;
                }
                a:hover {
                text-decoration:underline;
                color:blue;
                }
                a:active {
                text-decoration:underline;
                color:blue;
                }
                td {
                padding-right:10px;
                padding-left:10px;
                }
                </style>
                </head>
                <body>
                            """;
    /** End of page. */
    private static final String END = "<br />\r\n</div>\r\n</body>\r\n</html>";
    /** Project name. */
    private static final String PROJECT_NAME = "</ul>\r\n</div>\r\n"
            + "<div id=\"content\">\r\n<p>\r\n<strong>pictobrick - "
            + textbundle.getString("output_outputFiles_1")
            + "</strong>\r\n</p>\r\n<p id=\"headline\">\r\n<strong>"
            + textbundle.getString("output_outputFiles_2") + ": ";

    // ############################## all without index.html
    /** Start menu. */
    private static final String START_MENU = "<div id=\"menu\">\r\n"
            + "<ul>\r\n<li>\r\n<a href=\"../index.html\">\r\n"
            + textbundle.getString("output_outputFiles_3")
            + "\r\n</a>\r\n</li>\r\n";
    /** Graphic menu. */
    private static final String GRAPHIC_MENU = "<li>\r\n"
            + "<a href=\"grafic.html\">\r\n"
            + textbundle.getString("output_outputFiles_4")
            + "\r\n</a>\r\n</li>\r\n";
    /** Configuration menu. */
    private static final String CONFIGURATION_MENU = "<li>\r\n"
            + "<a href=\"configuration.html\">\r\n"
            + textbundle.getString("output_outputFiles_5")
            + "\r\n</a>\r\n<ul>\r\n<li>\r\n<a href=\"colors.html\">\r\n"
            + textbundle.getString("output_outputFiles_6")
            + "\r\n</a>\r\n</li>\r\n<li>\r\n<a href=\"elements.html\">\r\n"
            + textbundle.getString("output_outputFiles_7")
            + "\r\n</a>\r\n</li>\r\n</ul>\r\n</li>\r\n";
    /** Material menu. */
    private static final String MATERIAL_MENU = "<li>\r\n"
            + "<a href=\"billofmaterial.html\">\r\n"
            + textbundle.getString("output_outputFiles_8")
            + "\r\n</a>\r\n</li>\r\n";
    /** Instruction menu. */
    private static final String INSTRUCTION_MENU = "<li>\r\n"
            + "<a href=\"buildinginstruction.html\">\r\n"
            + textbundle.getString("output_outputFiles_9")
            + "\r\n</a>\r\n</li>\r\n";
    /** XML menu. */
    private static final String XML_MENU = "<li>\r\n<a href=\"xml.html\">\r\n"
            + textbundle.getString("output_outputFiles_10")
            + "\r\n</a>\r\n</li>\r\n";
    /** Additional menu. */
    private static final String ADDITIONAL_MENU = "<li>\r\n"
            + "<a href=\"additional.html\">\r\n"
            + textbundle.getString("output_outputFiles_11")
            + "\r\n</a>\r\n</li>\r\n";
    // ---------->all:projectname
    /** Project end. */
    private static final String PROJECT_END = "</strong>\r\n</p>\r\n";

    // ############################## index.html
    // ---------->all:head
    /** Start menu index. */
    private static final String START_MENU_INDEX = "<div id=\"menu\">\r\n"
            + "<ul>\r\n<li>\r\n<a href=\"index.html\">\r\n"
            + textbundle.getString("output_outputFiles_3")
            + "\r\n</a>\r\n</li>\r\n";
    /** Graphics menu index. */
    private static final String GRAPHICS_MENU_INDEX = "<li>\r\n"
            + "<a href=\"data/grafic.html\">\r\n"
            + textbundle.getString("output_outputFiles_4")
            + "\r\n</a>\r\n</li>\r\n";
    /** Configuration menu index. */
    private static final String CONFIGURATION_MENU_INDEX = "<li>\r\n"
            + "<a href=\"data/configuration.html\">\r\n"
            + textbundle.getString("output_outputFiles_5")
            + "\r\n</a>\r\n<ul>\r\n<li>\r\n<a href=\"data/colors.html\">\r\n"
            + textbundle.getString("output_outputFiles_6")
            + "\r\n</a>\r\n</li>\r\n<li>\r\n<a href=\"data/elements.html\">\r\n"
            + textbundle.getString("output_outputFiles_7")
            + "\r\n</a>\r\n</li>\r\n</ul>\r\n</li>\r\n";
    /** Material menu index. */
    private static final String MATERIAL_MENU_INDEX = "<li>\r\n"
            + "<a href=\"data/billofmaterial.html\">\r\n"
            + textbundle.getString("output_outputFiles_8")
            + "\r\n</a>\r\n</li>\r\n";
    /** Instruction menu index. */
    private static final String INSTRUCTION_MENU_INDEX = "<li>\r\n"
            + "<a href=\"data/buildinginstruction.html\">\r\n"
            + textbundle.getString("output_outputFiles_9")
            + "\r\n</a>\r\n</li>\r\n";
    /** XML menu index. */
    private static final String XML_MENU_INDEX = "<li>\r\n"
            + "<a href=\"data/xml.html\">\r\n"
            + textbundle.getString("output_outputFiles_10")
            + "\r\n</a>\r\n</li>\r\n";
    /** Additional menu index. */
    private static final String ADDITIONAL_MENU_INDEX = "<li>\r\n"
            + "<a href=\"data/additional.html\">\r\n"
            + textbundle.getString("output_outputFiles_11")
            + "\r\n</a>\r\n</li>\r\n";
    // ---------->projectname
    /** Project content index 2. */
    private static final String PROJECT_CONTENT_INDEX_2 = "</strong>\r\n"
            + "</p>\r\n<p>\r\n<strong>"
            + textbundle.getString("output_outputFiles_12")
            + ":</strong>\r\n<br />\r\n";
    // ---------->width x height
    /** Project content index 3. */
    private static final String PROJECT_CONTENT_INDEX_3 = " "
            + textbundle.getString("output_outputFiles_13") + "\r\n<br />\r\n";
    // ---------->width x height
    /** Project content index 4. */
    private static final String PROJECT_CONTENT_INDEX_4 = " mm\r\n"
            + "<br />\r\n</p>\r\n";
    /** Graphics content index. */
    private static final String GRAPHICS_CONTENT_INDEX = "<p>\r\n<strong>"
            + textbundle.getString("output_outputFiles_4")
            + "</strong><br />\r\n"
            + textbundle.getString("output_outputFiles_14") + "\r\n</p>\r\n";
    /** Configuration content index. */
    private static final String CONFIGURATION_CONTENT_INDEX = "<p>\r\n<strong>"
            + textbundle.getString("output_outputFiles_5")
            + "</strong><br />\r\n"
            + textbundle.getString("output_outputFiles_15") + "\r\n</p>\r\n";
    /** Material content index. */
    private static final String MATERIAL_CONTENT_INDEX = "<p>\r\n<strong>"
            + textbundle.getString("output_outputFiles_8")
            + "</strong><br />\r\n"
            + textbundle.getString("output_outputFiles_16") + "\r\n</p>\r\n";
    /** Instruction content index. */
    private static final String INSTRUCTION_CONTENT_INDEX = "<p>\r\n<strong>"
            + textbundle.getString("output_outputFiles_9")
            + "</strong><br />\r\n"
            + textbundle.getString("output_outputFiles_17") + "\r\n</p>\r\n";
    /** XML content index. */
    private static final String XML_CONTENT_INDEX = "<p>\r\n<strong>"
            + textbundle.getString("output_outputFiles_10")
            + "</strong><br />\r\n"
            + textbundle.getString("output_outputFiles_18") + "\r\n</p>\r\n";
    // ---------->all:end

    // ############################## grafic.html
    // ---------->all:head
    // ---------->all without index: from menu_start till project_end
    /** Graphics start. */
    private static final String GRAPHICS_START = "<p>\r\n<strong>"
            + textbundle.getString("output_outputFiles_4")
            + "</strong><br />\r\n"
            + textbundle.getString("output_outputFiles_19")
            + "\r\n</p>\r\n<p>\r\n<img src=\"mosaic.jpg\" width=\"";
    // ---------->image width
    /** Graphics height. */
    private static final String GRAPHICS_HEIGHT = "\" height=\"";
    // ---------->image height
    /** Graphics print width. */
    private static final String GRAPHICS_PRINT_WIDTH = "\" "
            + "alt=\"mosaic\" />\r\n"
            + "</p>\r\n<p>\r\n<a href=\"mosaic.jpg\">\r\n"
            + textbundle.getString("output_outputFiles_20")
            + "\r\n</a>\r\n</p>\r\n<p>\r\n"
            + textbundle.getString("output_outputFiles_21") + " ";
    // ---------->image print_width
    /** Graphics end. */
    private static final String GRAPHICS_END = " "
            + textbundle.getString("output_outputFiles_22") + "\r\n</p>\r\n";
    // ---------->all:end

    // ############################## configuration.html
    // ---------->all:head
    // ---------->all without index: from menu_start till project_end
    /** Configuration start. */
    private static final String CONFIGURATION_START = "<p>\r\n<strong>"
            + textbundle.getString("output_outputFiles_5")
            + "</strong><br />\r\n</p>\r\n";
    /** Configuration name. */
    private static final String CONFIGURATION_NAME = "<p>\r\n"
            + textbundle.getString("output_outputFiles_23") + ": ";
    // ---------->configuration name
    /** Configuration basis name. */
    private static final String CONFIGURATION_BASIS_NAME = "\r\n<br />\r\n"
            + textbundle.getString("output_outputFiles_24") + ": ";
    // ---------->basis name
    /** Configuration basis width. */
    private static final String CONFIGURATION_BASIS_WIDTH = "\r\n<br />\r\n"
            + textbundle.getString("output_outputFiles_25") + ": ";
    // ---------->basis width
    /** Configuration basis height. */
    private static final String CONFIGURATION_BASIS_HEIGHT = "\r\n<br />\r\n"
            + textbundle.getString("output_outputFiles_26") + ": ";
    // ---------->basis height
    /** Configuration basis width in mm. */
    private static final String CONFIGURATION_BASIS_WIDTH_MM = "\r\n<br />\r\n"
            + textbundle.getString("output_outputFiles_27") + ": ";
    // ---------->basis width mm
    /** Configuration end. */
    private static final String CONFIGURATION_END = "\r\n</p>\r\n<p>\r\n"
            + textbundle.getString("output_outputFiles_28")
            + " <a href=\"colors.html\">"
            + textbundle.getString("output_outputFiles_6") + "</a> "
            + textbundle.getString("output_outputFiles_29")
            + " <a href=\"elements.html\">"
            + textbundle.getString("output_outputFiles_7") + "</a> "
            + textbundle.getString("output_outputFiles_30") + ".\r\n</p>\r\n";
    // ---------->all:end

    // ############################## elements.html
    // ---------->all:head
    // ---------->all without index: from menu_start till project_end
    /** Element start. */
    private static final String ELEMENT_START = "<p>\r\n<strong>"
            + textbundle.getString("output_outputFiles_7")
            + "</strong>\r\n<br />\r\n</p>\r\n<table>\r\n";
    /** Element cell start. */
    private static final String ELEMENT_CELL_START = "<tr>\r\n<td>\r\n";
    /** Element name. */
    private static final String ELEMENT_NAME = "\r\n</td>\r\n<td>\r\n<strong>"
            + textbundle.getString("output_outputFiles_31") + ":</strong> ";
    // ---------->name
    /** Element width. */
    private static final String ELEMENT_WIDTH = "\r\n<br />\r\n"
            + textbundle.getString("output_outputFiles_32") + ": ";
    // ---------->width
    /** Element height. */
    private static final String ELEMENT_HEIGHT = "\r\n<br />\r\n"
            + textbundle.getString("output_outputFiles_33") + ": ";
    // ---------->height
    /** Element stability. */
    private static final String ELEMENT_STABILITY = "\r\n<br />\r\n"
            + textbundle.getString("output_outputFiles_34") + ": ";
    // ---------->stability
    /** Element costs. */
    private static final String ELEMENT_COSTS = "\r\n<br />\r\n"
            + textbundle.getString("output_outputFiles_35") + ": ";
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
            + textbundle.getString("output_outputFiles_6")
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
            + textbundle.getString("output_outputFiles_36") + ": ";
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
            + textbundle.getString("output_outputFiles_8")
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
            + textbundle.getString("output_outputFiles_9")
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
            + textbundle.getString("output_outputFiles_10")
            + "</strong><br />\r\n</p>\r\n<p>\r\n"
            + textbundle.getString("output_outputFiles_18")
            + "\r\n</p>\r\n<p>\r\n<a href=\"mosaic.xml\">\r\n"
            + textbundle.getString("output_outputFiles_37")
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
            + textbundle.getString("output_outputFiles_11")
            + "</strong><br />\r\n</p>\r\n";
    // ---------->inelementation
    /** Additional cell end. */
    private static final String ADDITIONAL_CELL_END = "\r\n<br />\r\n";
    // ---------->all:end

    /** Project. */
    private String project;
    /** Data processor. */
    private final DataProcessor dataProcessing;
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

    /**
     * Constructor.
     *
     * @param processor Data processor.
     *
     * @author Tobias Reichling
     */
    public OutputFileGenerator(final DataProcessor processor) {
        this.dataProcessing = processor;
    }

    /**
     * Sets the projects name.
     *
     * @author Tobias Reichling
     * @param name
     */
    public void setProject(final String name) {
        this.project = name;
    }

    /**
     * Generates documents and returns a message.
     *
     * @author Tobias Reichling
     * @param grafic        true, if document will be generated
     * @param configuration true, if document will be generated
     * @param material      true, if document will be generated
     * @param instruction   true, if document will be generated
     * @param xml           true, if document will be generated
     * @param infos
     * @return message error
     */
    public String generateDocuments(final boolean grafic,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final Enumeration<String> infos) {
        String error = "";
        // index.html
        final StringBuffer index = new StringBuffer();
        index.append(HEADER);
        index.append(START_MENU_INDEX);

        if (grafic) {
            index.append(GRAPHICS_MENU_INDEX);
        }

        if (configuration) {
            index.append(CONFIGURATION_MENU_INDEX);
        }

        if (material) {
            index.append(MATERIAL_MENU_INDEX);
        }

        if (instruction) {
            index.append(INSTRUCTION_MENU_INDEX);
        }

        if (xml) {
            index.append(XML_MENU_INDEX);
        }

        index.append(ADDITIONAL_MENU_INDEX);
        index.append(PROJECT_NAME);
        index.append(project);
        index.append(PROJECT_CONTENT_INDEX_2);
        index.append(dataProcessing.getMosaicWidth() + " x "
                + dataProcessing.getMosaicHeight());
        index.append(PROJECT_CONTENT_INDEX_3);
        final int width2 = (int) (100
                * (dataProcessing.getCurrentConfiguration().getBasisWidthMM()
                        * dataProcessing.getMosaicWidth()));
        final int height2 = (int) (100
                * (dataProcessing.getCurrentConfiguration().getBasisWidthMM()
                        / dataProcessing.getCurrentConfiguration()
                                .getBasisWidth()
                        * dataProcessing.getCurrentConfiguration()
                                .getBasisHeight()
                        * dataProcessing.getMosaicHeight()));
        index.append((width2 / ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT)
                + textbundle.getString("output_decimalPoint")
                + (width2 % ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT) + " x "
                + (height2 / ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT)
                + textbundle.getString("output_decimalPoint")
                + (height2 % ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT));
        index.append(PROJECT_CONTENT_INDEX_4);

        if (grafic) {
            index.append(GRAPHICS_CONTENT_INDEX);
        }

        if (configuration) {
            index.append(CONFIGURATION_CONTENT_INDEX);
        }

        if (material) {
            index.append(MATERIAL_CONTENT_INDEX);
        }

        if (instruction) {
            index.append(INSTRUCTION_CONTENT_INDEX);
        }

        if (xml) {
            index.append(XML_CONTENT_INDEX);
        }

        index.append(END);

        if (!(dataProcessing.generateUTFOutput(index.toString(), "index.html",
                false))) {
            error = error + "index.html "
                    + textbundle.getString("output_outputFiles_38") + ".\n\r";
        }

        // grafic.html
        if (grafic) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        dataProcessing
                                .animateGraficProgressBarOutputFiles(true);
                        dataProcessing.refreshProgressBarOutputFiles(0, 1);
                    }
                });
            } catch (final Exception e) {
                System.out.println(e);
            }
            final StringBuffer graficString = new StringBuffer();
            graficString.append(HEADER);
            graficString.append(START_MENU);
            if (grafic) {
                graficString.append(GRAPHIC_MENU);
            }
            if (configuration) {
                graficString.append(CONFIGURATION_MENU);
            }
            if (material) {
                graficString.append(MATERIAL_MENU);
            }
            if (instruction) {
                graficString.append(INSTRUCTION_MENU);
            }
            if (xml) {
                graficString.append(XML_MENU);
            }
            graficString.append(ADDITIONAL_MENU);
            graficString.append(PROJECT_NAME);
            graficString.append(project);
            graficString.append(PROJECT_END);
            graficString.append(GRAPHICS_START);
            final BufferedImage mosaikImage = dataProcessing.getImage(true);
            dataProcessing.generateImageOutput(mosaikImage, "mosaic");
            final int height = (int) (mosaikImage.getHeight()
                    * (600.0 / mosaikImage.getWidth()));
            final int width = 600;
            final int printWidth = (int) (100
                    * (dataProcessing.getMosaicWidth() * dataProcessing
                            .getCurrentConfiguration().getBasisWidthMM()));
            graficString.append(width);
            graficString.append(GRAPHICS_HEIGHT);
            graficString.append(height);
            graficString.append(GRAPHICS_PRINT_WIDTH);
            graficString.append((printWidth
                    / ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT)
                    + textbundle.getString("output_decimalPoint") + (printWidth
                            % ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT));
            graficString.append(GRAPHICS_END);
            graficString.append(END);
            if (!(dataProcessing.generateUTFOutput(graficString.toString(),
                    "grafic.html", true))) {
                error = error + "grafic.html "
                        + textbundle.getString("output_outputFiles_38")
                        + ".\n\r";
            }
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        dataProcessing
                                .animateGraficProgressBarOutputFiles(false);
                        dataProcessing.refreshProgressBarOutputFiles(
                                ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT, 1);
                    }
                });
            } catch (

            final Exception e) {
            }
        }

        // configuration.html
        if (configuration) {
            final StringBuffer configurationString = new StringBuffer();
            configurationString.append(HEADER);
            configurationString.append(START_MENU);
            if (grafic) {
                configurationString.append(GRAPHIC_MENU);
            }
            if (configuration) {
                configurationString.append(CONFIGURATION_MENU);
            }
            if (material) {
                configurationString.append(MATERIAL_MENU);
            }
            if (instruction) {
                configurationString.append(INSTRUCTION_MENU);
            }
            if (xml) {
                configurationString.append(XML_MENU);
            }
            configurationString.append(ADDITIONAL_MENU);
            configurationString.append(PROJECT_NAME);
            configurationString.append(project);
            configurationString.append(PROJECT_END);
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
            final int widthMM = (int) (100 * (dataProcessing
                    .getCurrentConfiguration().getBasisWidthMM()));
            configurationString.append((widthMM
                    / ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT)
                    + textbundle.getString("output_decimalPoint")
                    + (widthMM % ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT));
            configurationString.append(CONFIGURATION_END);
            configurationString.append(END);
            if (!(dataProcessing.generateUTFOutput(
                    configurationString.toString(), "configuration.html",
                    true))) {
                error = error + "configuration.html "
                        + textbundle.getString("output_outputFiles_38")
                        + ".\n\r";
            }

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

            final StringBuffer elementsString = new StringBuffer();
            elementsString.append(HEADER);
            elementsString.append(START_MENU);

            if (grafic) {
                elementsString.append(GRAPHIC_MENU);
            }

            if (configuration) {
                elementsString.append(CONFIGURATION_MENU);
            }

            if (material) {
                elementsString.append(MATERIAL_MENU);
            }

            if (instruction) {
                elementsString.append(INSTRUCTION_MENU);
            }

            if (xml) {
                elementsString.append(XML_MENU);
            }

            elementsString.append(ADDITIONAL_MENU);
            elementsString.append(PROJECT_NAME);
            elementsString.append(project);
            elementsString.append(PROJECT_END);
            elementsString.append(ELEMENT_START);
            ElementObject element = new ElementObject();
            boolean[][] matrix;
            boolean[][] matrixNew;
            BufferedImage elementImage;
            percent = 0;
            referenceValue = (dataProcessing.getCurrentConfiguration()
                    .getQuantityColorsAndElements()) / REFERENCE_VALUE_DIVISOR;

            if (referenceValue == 0) {
                referenceValue = 1;
            }

            int counter = 0;
            int flag1 = 0;

            for (final var elementsEnumeration = dataProcessing
                    .getCurrentConfiguration()
                    .getAllElements(); elementsEnumeration.hasMoreElements();) {
                counter++;
                element = (ElementObject) elementsEnumeration.nextElement();
                elementsString.append(ELEMENT_CELL_START);
                matrix = element.getMatrix();
                matrixNew = new boolean[element.getHeight()
                        + 2][element.getWidth() + 2];

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
                            matrixNew[row][column] = matrix[row - 1][column
                                    - 1];
                        }
                    }
                }

                elementImage = new BufferedImage(width * TILE_SIZE,
                        height * element.getHeight(),
                        BufferedImage.TYPE_INT_RGB);
                final Graphics2D g = elementImage.createGraphics();
                g.setColor(colorWhite);
                g.fillRect(0, 0, width * TILE_SIZE,
                        height * element.getHeight());

                for (int row = 0; row < (element.getHeight() + 2); row++) {
                    for (int column = 0; column < (element.getWidth()
                            + 2); column++) {
                        if (matrixNew[row][column]) {
                            g.setColor(colorNormal);
                            g.fillRect(width * (column - 1), height * (row - 1),
                                    width, height);
                            g.setColor(colorLight);

                            // border left
                            if (!matrixNew[row][column - 1]) {
                                g.fillRect(width * (column - 1),
                                        height * (row - 1) + 2, 2,
                                        height - HALF_TILE);
                            }

                            // border top
                            if (!matrixNew[row - 1][column]) {
                                g.fillRect(width * (column - 1) + 2,
                                        height * (row - 1), width - HALF_TILE,
                                        2);
                            }
                            // corner top left
                            if (!(matrixNew[row][column - 1]
                                    && matrixNew[row - 1][column]
                                    && matrixNew[row - 1][column - 1])) {
                                g.fillRect(width * (column - 1),
                                        height * (row - 1), 2, 2);
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
                                        height * (row - 1) + 2, 2,
                                        height - HALF_TILE);
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

                dataProcessing.generateImageOutput(elementImage,
                        "element_" + counter);

                if (flag1 % referenceValue == 0) {
                    // refresh progress bars -> gui thread
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                percent = percent + REFERENCE_VALUE_DIVISOR;
                                dataProcessing.refreshProgressBarOutputFiles(
                                        percent, 2);
                            }
                        });
                    } catch (final Exception e) {
                    }
                }

                flag1++;
                elementsString.append("<img src=\"" + "element_" + counter
                        + ".jpg\" width=\"" + width * TILE_SIZE + "\" height=\""
                        + height * element.getHeight()
                        + "\" alt=\"mosaic\" />");
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

            elementsString.append(ELEMENT_END);
            elementsString.append(END);

            if (!(dataProcessing.generateUTFOutput(elementsString.toString(),
                    "elements.html", true))) {
                error = error + "elements.html "
                        + textbundle.getString("output_outputFiles_38")
                        + ".\n\r";
            }

            // colors.html
            final StringBuffer colorsString = new StringBuffer();
            colorsString.append(HEADER);
            colorsString.append(START_MENU);

            if (grafic) {
                colorsString.append(GRAPHIC_MENU);
            }

            if (configuration) {
                colorsString.append(CONFIGURATION_MENU);
            }

            if (material) {
                colorsString.append(MATERIAL_MENU);
            }

            if (instruction) {
                colorsString.append(INSTRUCTION_MENU);
            }

            if (xml) {
                colorsString.append(XML_MENU);
            }

            colorsString.append(ADDITIONAL_MENU);
            colorsString.append(PROJECT_NAME);
            colorsString.append(project);
            colorsString.append(PROJECT_END);
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
                                dataProcessing.refreshProgressBarOutputFiles(
                                        percent, 2);
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
                colorsString.append(color.getRGB().getRed() + ", "
                        + color.getRGB().getGreen() + ", "
                        + color.getRGB().getBlue());
                colorsString.append(COLOR_CELL_END);
            }

            colorsString.append(COLOR_END);
            colorsString.append(END);

            if (!(dataProcessing.generateUTFOutput(colorsString.toString(),
                    "colors.html", true))) {
                error = error + "colors.html "
                        + textbundle.getString("output_outputFiles_38")
                        + ".\n\r";
            }
        }

        // billofmaterial.html
        if (material) {
            final StringBuffer billofmaterialString = new StringBuffer();
            billofmaterialString.append(HEADER);
            billofmaterialString.append(START_MENU);

            if (grafic) {
                billofmaterialString.append(GRAPHIC_MENU);
            }

            if (configuration) {
                billofmaterialString.append(CONFIGURATION_MENU);
            }

            if (material) {
                billofmaterialString.append(MATERIAL_MENU);
            }

            if (instruction) {
                billofmaterialString.append(INSTRUCTION_MENU);
            }

            if (xml) {
                billofmaterialString.append(XML_MENU);
            }

            billofmaterialString.append(ADDITIONAL_MENU);
            billofmaterialString.append(PROJECT_NAME);
            billofmaterialString.append(project);
            billofmaterialString.append(PROJECT_END);
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
                                + textbundle.getString("output_outputFiles_39")
                                + "");
                        billofmaterialString.append(MATERIAL_ROW_END);
                    }
                }
            }

            billofmaterialString.append(MATERIAL_END);
            billofmaterialString.append(END);

            if (!(dataProcessing.generateUTFOutput(
                    billofmaterialString.toString(), "billofmaterial.html",
                    true))) {
                error = error + "billofmaterial.html "
                        + textbundle.getString("output_outputFiles_38")
                        + ".\n\r";
            }
        }

        // instruction.html
        if (instruction) {
            List<List<Vector<String>>> mosaicMatrix = dataProcessing
                    .getMosaic();
            final StringBuffer instructionString = new StringBuffer();
            instructionString.append(HEADER);
            instructionString.append(START_MENU);

            if (grafic) {
                instructionString.append(GRAPHIC_MENU);
            }

            if (configuration) {
                instructionString.append(CONFIGURATION_MENU);
            }

            if (material) {
                instructionString.append(MATERIAL_MENU);
            }

            if (instruction) {
                instructionString.append(INSTRUCTION_MENU);
            }

            if (xml) {
                instructionString.append(XML_MENU);
            }

            instructionString.append(ADDITIONAL_MENU);
            instructionString.append(PROJECT_NAME);
            instructionString.append(project);
            instructionString.append(PROJECT_END);
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
                    instructionString.append(
                            textbundle.getString("output_outputFiles_40") + " "
                                    + (row + 1) + ", "
                                    + textbundle
                                            .getString("output_outputFiles_41")
                                    + " " + (column + 1) + ": ");

                    if (mosaicMatrix.get(row).get(column).isEmpty()) {
                        instructionString.append("("
                                + textbundle.getString("output_outputFiles_42")
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
            instructionString.append(END);
            if (!(dataProcessing.generateUTFOutput(instructionString.toString(),
                    "buildinginstruction.html", true))) {
                error = error + "buildinginstruction.html "
                        + textbundle.getString("output_outputFiles_38")
                        + ".\n\r";
            }
        }

        // xml.html
        if (xml) {
            List<List<Vector<String>>> mosaicMatrix = dataProcessing
                    .getMosaic();
            final StringBuffer xmlAusgabe = new StringBuffer();
            xmlAusgabe.append(HEADER);
            xmlAusgabe.append(START_MENU);

            if (grafic) {
                xmlAusgabe.append(GRAPHIC_MENU);
            }

            if (configuration) {
                xmlAusgabe.append(CONFIGURATION_MENU);
            }

            if (material) {
                xmlAusgabe.append(MATERIAL_MENU);
            }

            if (instruction) {
                xmlAusgabe.append(INSTRUCTION_MENU);
            }

            if (xml) {
                xmlAusgabe.append(XML_MENU);
            }

            xmlAusgabe.append(ADDITIONAL_MENU);
            xmlAusgabe.append(PROJECT_NAME);
            xmlAusgabe.append(project);
            xmlAusgabe.append(PROJECT_END);
            xmlAusgabe.append(XML_ALL);
            xmlAusgabe.append(END);

            if (!(dataProcessing.generateUTFOutput(xmlAusgabe.toString(),
                    "xml.html", true))) {
                error = error + "xml.html "
                        + textbundle.getString("output_outputFiles_38")
                        + ".\n\r";
            }

            // mosaic.xml
            final StringBuffer xmldocumentString = new StringBuffer();
            xmldocumentString.append(XML_DOCUMENT_START1);
            xmldocumentString.append(project);
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
                error = error + "mosaic.xml "
                        + textbundle.getString("output_outputFiles_38")
                        + ".\n\r";
            }
        }

        // additional.html
        final StringBuffer additionalString = new StringBuffer();

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

        additionalString.append(HEADER);
        additionalString.append(START_MENU);

        if (grafic) {
            additionalString.append(GRAPHIC_MENU);
        }

        if (configuration) {
            additionalString.append(CONFIGURATION_MENU);
        }

        if (material) {
            additionalString.append(MATERIAL_MENU);
        }

        if (instruction) {
            additionalString.append(INSTRUCTION_MENU);
        }

        if (xml) {
            additionalString.append(XML_MENU);
        }

        additionalString.append(ADDITIONAL_MENU);
        additionalString.append(PROJECT_NAME);
        additionalString.append(project);
        additionalString.append(PROJECT_END);
        additionalString.append(ADDITIONAL_START);

        if (!infos.hasMoreElements()) {
            additionalString
                    .append(textbundle.getString("output_outputFiles_43"));
            additionalString.append(ADDITIONAL_CELL_END);
        } else {
            while (infos.hasMoreElements()) {
                additionalString.append((String) infos.nextElement());
                additionalString.append(ADDITIONAL_CELL_END);
            }
        }

        additionalString.append(END);

        if (!(dataProcessing.generateUTFOutput(additionalString.toString(),
                "additional.html", true))) {
            error = error + "additional.html "
                    + textbundle.getString("output_outputFiles_38") + ".\n\r";
        }

        return error;
    }

    private Hashtable<String, Hashtable<String, Integer>> initMaterialHash() {
        final var mHash = new Hashtable<String, Hashtable<String, Integer>>();
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
