package pictobrick.service.generators;

import pictobrick.service.DataProcessor;
import pictobrick.ui.ProgressBarsAlgorithms;

/**
 * Generates index for output files. Code moved from OutputFileGenerator by John
 * Watne 10/2023.
 */
public class IndexGenerator {

    // ############################## index.html
    // ---------->all:head
    /** Start menu index. */
    private static final String START_MENU_INDEX = "<div id=\"menu\">\r\n"
            + "<ul>\r\n<li>\r\n<a href=\"index.html\">\r\n"
            + FileGenerationCommon.getTextbundle().getString(
                    "output_outputFiles_3")
            + "\r\n</a>\r\n</li>\r\n";
    /** Graphics menu index. */
    private static final String GRAPHICS_MENU_INDEX = "<li>\r\n"
            + "<a href=\"data/grafic.html\">\r\n" + FileGenerationCommon
                    .getTextbundle().getString("output_outputFiles_4")
            + "\r\n</a>\r\n</li>\r\n";
    /** Configuration menu index. */
    private static final String CONFIGURATION_MENU_INDEX = "<li>\r\n"
            + "<a href=\"data/configuration.html\">\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_5")
            + "\r\n</a>\r\n<ul>\r\n<li>\r\n<a href=\"data/colors.html\">\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_6")
            + "\r\n</a>\r\n</li>\r\n<li>\r\n<a href=\"data/elements.html\">\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_7")
            + "\r\n</a>\r\n</li>\r\n</ul>\r\n</li>\r\n";
    /** Material menu index. */
    private static final String MATERIAL_MENU_INDEX = "<li>\r\n"
            + "<a href=\"data/billofmaterial.html\">\r\n" + FileGenerationCommon
                    .getTextbundle().getString("output_outputFiles_8")
            + "\r\n</a>\r\n</li>\r\n";
    /** Instruction menu index. */
    private static final String INSTRUCTION_MENU_INDEX = "<li>\r\n"
            + "<a href=\"data/buildinginstruction.html\">\r\n"
            + FileGenerationCommon.getTextbundle().getString(
                    "output_outputFiles_9")
            + "\r\n</a>\r\n</li>\r\n";
    /** XML menu index. */
    private static final String XML_MENU_INDEX = "<li>\r\n"
            + "<a href=\"data/xml.html\">\r\n" + FileGenerationCommon
                    .getTextbundle().getString("output_outputFiles_10")
            + "\r\n</a>\r\n</li>\r\n";
    /** Additional menu index. */
    private static final String ADDITIONAL_MENU_INDEX = "<li>\r\n"
            + "<a href=\"data/additional.html\">\r\n" + FileGenerationCommon
                    .getTextbundle().getString("output_outputFiles_11")
            + "\r\n</a>\r\n</li>\r\n";
    // ---------->projectname
    /** Project content index 2. */
    private static final String PROJECT_CONTENT_INDEX_2 = "</strong>\r\n"
            + "</p>\r\n<p>\r\n<strong>" + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_12")
            + ":</strong>\r\n<br />\r\n";
    // ---------->width x height
    /** Project content index 3. */
    private static final String PROJECT_CONTENT_INDEX_3 = " "
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_13")
            + "\r\n<br />\r\n";
    // ---------->width x height
    /** Project content index 4. */
    private static final String PROJECT_CONTENT_INDEX_4 = " mm\r\n"
            + "<br />\r\n</p>\r\n";
    /** Graphics content index. */
    private static final String GRAPHICS_CONTENT_INDEX = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_4")
            + "</strong><br />\r\n" + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_14")
            + "\r\n</p>\r\n";
    /** Configuration content index. */
    private static final String CONFIGURATION_CONTENT_INDEX = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_5")
            + "</strong><br />\r\n" + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_15")
            + "\r\n</p>\r\n";
    /** Material content index. */
    private static final String MATERIAL_CONTENT_INDEX = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_8")
            + "</strong><br />\r\n" + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_16")
            + "\r\n</p>\r\n";
    /** Instruction content index. */
    private static final String INSTRUCTION_CONTENT_INDEX = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_9")
            + "</strong><br />\r\n" + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_17")
            + "\r\n</p>\r\n";
    /** XML content index. */
    private static final String XML_CONTENT_INDEX = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_10")
            + "</strong><br />\r\n" + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_18")
            + "\r\n</p>\r\n";
    // ---------->all:end

    /** Common file generating code. */
    private FileGenerationCommon common;

    /**
     * Constructs the IndexGenerator with the shared common file generation
     * code.
     *
     * @param fileGenerationCommon the shared common file generation code.
     */
    public IndexGenerator(final FileGenerationCommon fileGenerationCommon) {
        this.common = fileGenerationCommon;
    }

    /**
     * Generates index HTML file.
     *
     * @param generateGraphics <code>true</code> if generating graphics output.
     * @param configuration    <code>true</code> if generating configuration
     *                         output.
     * @param material         <code>true</code> if generating material output.
     * @param instruction      <code>true</code> if generating instructions.
     * @param xml              <code>true</code> if generating XML.
     * @param initialError     error String generated so far, if any.
     * @return initialError, with any new errors in generating the index
     *         appended.
     */
    public final String generateIndex(final boolean generateGraphics,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final String initialError) {
        String error = initialError;
        final DataProcessor dataProcessing = common.getDataProcessing();
        final StringBuilder index = new StringBuilder();
        index.append(FileGenerationCommon.HEADER);
        index.append(START_MENU_INDEX);

        if (generateGraphics) {
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
        index.append(FileGenerationCommon.PROJECT_NAME);
        index.append(common.getProject());
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
                + FileGenerationCommon.getTextbundle()
                        .getString("output_decimalPoint")
                + (width2 % ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT) + " x "
                + (height2 / ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT)
                + FileGenerationCommon.getTextbundle()
                        .getString("output_decimalPoint")
                + (height2 % ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT));
        index.append(PROJECT_CONTENT_INDEX_4);

        if (generateGraphics) {
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

        index.append(FileGenerationCommon.END);

        if (!(dataProcessing.generateUTFOutput(index.toString(), "index.html",
                false))) {
            error = error + "index.html " + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_38") + ".\n\r";
        }
        return error;
    }

}
