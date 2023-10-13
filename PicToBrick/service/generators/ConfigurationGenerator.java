package pictobrick.service.generators;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import pictobrick.model.ColorObject;
import pictobrick.model.ElementObject;
import pictobrick.model.Mosaic;
import pictobrick.service.DataProcessor;
import pictobrick.ui.ProgressBarsAlgorithms;

/**
 * Generates configuration page. Code moved from OutputFileGenerator by John
 * Watne 10/2023.
 */
public class ConfigurationGenerator {
    /** Element start. */
    private static final String ELEMENT_START = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_7")
            + "</strong>\r\n<br />\r\n</p>\r\n<table>\r\n";
    /** Element end. */
    private static final String ELEMENT_END = "</table>\r\n";
    // ############################## elements.html
    // ---------->all:head
    // ---------->all without index: from menu_start till project_end
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
    // ---------->all:end
    /** Divisor for calculating referenceValue. */
    private static final int REFERENCE_VALUE_DIVISOR = 10;
    /** Length and width of 1x1 tile in pixels. */
    private static final int TILE_SIZE = 8;
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
    /** Normal color. */
    private final Color colorNormal = new Color(59, 45, 167);
    /** Light color. */
    private final Color colorLight = new Color(159, 145, 255);
    /** Dark color. */
    private final Color colorDark = new Color(0, 0, 67);
    /** White. */
    private final Color colorWhite = new Color(255, 255, 255);
    /** Common file generating code. */
    private FileGenerationCommon common;

    /**
     * Constructor.
     *
     * @param fileGenerationCommon common file generating code.
     */
    public ConfigurationGenerator(
            final FileGenerationCommon fileGenerationCommon) {
        this.common = fileGenerationCommon;
    }

    final String generateConfiguration(final boolean generateGraphics,
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
            common.setPercent(0);
            int referenceValue = (dataProcessing.getCurrentConfiguration()
                    .getQuantityColorsAndElements()) / REFERENCE_VALUE_DIVISOR;

            if (referenceValue == 0) {
                referenceValue = 1;
            }

            common.setReferenceValue(referenceValue);
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
            final int referenceValue = common.getReferenceValue();

            if (flag1 % referenceValue == 0) {
                // refresh progress bars -> gui thread
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            int percent = common.getPercent();
                            percent = percent + REFERENCE_VALUE_DIVISOR;
                            common.setPercent(percent);
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
                                2, height - FileGenerationCommon.HALF_TILE);
                    }

                    // border top
                    if (!matrixNew[row - 1][column]) {
                        g.fillRect(width * (column - 1) + 2, height * (row - 1),
                                width - FileGenerationCommon.HALF_TILE, 2);
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
                                height * (row - 1) + 2, 2,
                                height - FileGenerationCommon.HALF_TILE);
                    }
                    // border bottom
                    if (!matrixNew[row + 1][column]) {
                        g.fillRect(width * (column - 1) + 2,
                                height * (row - 1) + height - 2,
                                width - FileGenerationCommon.HALF_TILE, 2);
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

            if (flag1 % common.getReferenceValue() == 0) {
                // refresh progress bars -> gui thread
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            int percent = common.getPercent();
                            percent = percent + REFERENCE_VALUE_DIVISOR;
                            common.setPercent(percent);
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

}
