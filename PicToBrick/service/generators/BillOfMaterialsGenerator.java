package pictobrick.service.generators;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.model.ColorObject;
import pictobrick.model.ElementObject;
import pictobrick.service.DataProcessor;
import pictobrick.ui.ProgressBarsAlgorithms;
import pictobrick.ui.ProgressBarsOutputFiles;

/**
 * Generator of Bill of Materials. Code moved from OutputFileGenerator by John
 * Watne 10/2023.
 */
public class BillOfMaterialsGenerator {
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
    /** Common file generation logic. */
    private FileGenerationCommon common;

    /**
     * Constructor.
     *
     * @param fileGenerationCommon common file generation logic.
     */
    public BillOfMaterialsGenerator(
            final FileGenerationCommon fileGenerationCommon) {
        this.common = fileGenerationCommon;
    }

    /**
     * Generates Bill of Materials if requested by user.
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
    String generateBillOfMaterial(final boolean generateGraphics,
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
            common.setPercent(0);
            common.setReferenceValue((dataProcessing.getMosaicHeight()
                    * dataProcessing.getMosaicWidth())
                    / ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT);

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
                                int percent = common.getPercent();
                                percent++;
                                common.setPercent(percent);
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
