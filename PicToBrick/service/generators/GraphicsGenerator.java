package pictobrick.service.generators;

import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import pictobrick.service.DataProcessor;
import pictobrick.ui.ProgressBarsAlgorithms;

/**
 * Graphics output generator. Code moved from OutputFileGenerator by John Watne
 * 10/2023.
 */
public class GraphicsGenerator {
    // ############################## grafic.html
    // ---------->all:head
    // ---------->all without index: from menu_start till project_end
    /** Graphics start. */
    private static final String GRAPHICS_START = "<p>\r\n<strong>"
            + FileGenerationCommon
                    .getTextbundle().getString("output_outputFiles_4")
            + "</strong><br />\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_19")
            + "\r\n</p>\r\n<p>\r\n<img src=\"mosaic.jpg\" width=\"";
    // ---------->image width
    /** Graphics height. */
    private static final String GRAPHICS_HEIGHT = "\" height=\"";
    // ---------->image height
    /** Graphics print width. */
    private static final String GRAPHICS_PRINT_WIDTH = "\" "
            + "alt=\"mosaic\" />\r\n"
            + "</p>\r\n<p>\r\n<a href=\"mosaic.jpg\">\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_20")
            + "\r\n</a>\r\n</p>\r\n<p>\r\n" + FileGenerationCommon
                    .getTextbundle().getString("output_outputFiles_21")
            + " ";
    // ---------->image print_width
    /** Graphics end. */
    private static final String GRAPHICS_END = " " + FileGenerationCommon
            .getTextbundle().getString("output_outputFiles_22")
            + "\r\n</p>\r\n";
    // ---------->all:end

    /** Common file generating code. */
    private final FileGenerationCommon common;

    /**
     * Constructor.
     *
     * @param fileGenerationCommon common file generating code.
     */
    public GraphicsGenerator(final FileGenerationCommon fileGenerationCommon) {
        this.common = fileGenerationCommon;
    }

    final String generateGraphics(final boolean generateGraphics,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final String initialError) {
        String error = initialError;
        final DataProcessor dataProcessing = common.getDataProcessing();

        if (generateGraphics) {
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

            final StringBuilder graficString = common.getCommonDocumentStart(
                    generateGraphics, configuration, material, instruction,
                    xml);
            graficString.append(GRAPHICS_START);
            final BufferedImage mosaicImage = dataProcessing.getImage(true);
            dataProcessing.generateImageOutput(mosaicImage, "mosaic");
            final int height = (int) (mosaicImage.getHeight()
                    * (600.0 / mosaicImage.getWidth()));
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
                    + FileGenerationCommon.getTextbundle()
                            .getString("output_decimalPoint")
                    + (printWidth
                            % ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT));
            graficString.append(GRAPHICS_END);
            graficString.append(FileGenerationCommon.END);

            if (!(dataProcessing.generateUTFOutput(graficString.toString(),
                    "grafic.html", true))) {
                error = error + "grafic.html " + FileGenerationCommon
                        .getTextbundle().getString("output_outputFiles_38")
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
            } catch (final Exception e) {
            }
        }

        return error;
    }

}
